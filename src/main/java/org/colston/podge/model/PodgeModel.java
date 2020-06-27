package org.colston.podge.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.mail.FetchProfile;
import javax.mail.Flags.Flag;
import javax.mail.event.MessageChangedEvent;
import javax.mail.event.MessageChangedListener;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SentDateTerm;
import javax.swing.event.EventListenerList;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;

public class PodgeModel implements PodgeItem
{
	private static final Logger logger = Logger.getLogger(PodgeModel.class.getName());
	
	private EventListenerList listeners = new EventListenerList();
	
	private List<PodgeAccount> accounts = new ArrayList<>();
	private PodgeFolder currentFolder;

	private MessageChangedListener messageChangedListener = e -> logger.fine(
			() -> String.format("*** messageChanged: %s, %d%n", e.getMessage(), e.getMessage().getMessageNumber()));;
	
	public void addAccount(PodgeAccount a)
	{
		accounts.add(a);
	}
	
	public List<PodgeAccount> getAccounts()
	{
		return accounts;
	}

	public void connect(PodgeAccount account) throws MessagingException
	{
		Store store = account.getSession().getStore();
		account.setStore(store);
		store.connect();
		fireAccountConnected(account);
		
		IMAPFolder defaultFolder = (IMAPFolder) store.getDefaultFolder();
		account.setFolder(defaultFolder);
		List<PodgeFolder> folders = processFolders(account);

		addSubFolders(folders);
		
		//open inbox
		PodgeFolder inbox = folders.get(0);
		selectFolder(inbox);
	}

	private void addSubFolders(List<PodgeFolder> pfolders) throws MessagingException
	{
		for (PodgeFolder pfolder : pfolders)
		{
			List<PodgeFolder> folders = processFolders(pfolder);
			if (folders.isEmpty())
			{
				continue;
			}
			addSubFolders(folders);
		}
	}

	private List<PodgeFolder> processFolders(PodgeFolder parent) throws MessagingException
	{
		Folder[] list = parent.getFolder().list();
		if (list.length == 0)
		{
			Collections.emptyList();
		}
		List<Folder> fs = Arrays.asList(list);
		List<PodgeFolder> folders = fs.stream()
				.map(f -> new PodgeFolder((IMAPFolder) f, parent))
				.sorted(new FolderComparator())
				.collect(Collectors.toList());
		for (PodgeFolder f : folders)
		{
			if (f.getFolder() != null && (f.getFolder().getType() & Folder.HOLDS_MESSAGES) > 0)
			{
				int ucount = f.getFolder().getUnreadMessageCount();
				f.setUnreadMessageCount(ucount);
			}
		}
		parent.setFolders(folders);
		fireFoldersInserted(parent, folders);
		return folders;
	}
	
	public void disconnectAll()
	{
		closeCurrentFolder();
		accounts.forEach(PodgeAccount::disconnect);
	}
	
	public void selectFolder(PodgeFolder f)
	{
		try
		{
			closeCurrentFolder();
			currentFolder = f;
			currentFolder.getFolder().addMessageChangedListener(messageChangedListener);
			currentFolder.clearMessages();
			if (currentFolder.getFolder() != null && (currentFolder.getFolder().getType() & Folder.HOLDS_MESSAGES) > 0)
			{
				currentFolder.getFolder().open(Folder.READ_WRITE);
				int ucount = f.getFolder().getUnreadMessageCount();
				f.setUnreadMessageCount(ucount);
				
				int count = currentFolder.getFolder().getMessageCount();
				currentFolder.setTotalMessageCount(count);
				Message[] messages;
				if (count > 500)
				{
					LocalDate now = LocalDate.of(2020, 5, 18);
					SearchTerm term = new SentDateTerm(ComparisonTerm.GE, Date.valueOf(now));
					messages = currentFolder.getFolder().search(term);
				}
				else 
				{
					messages = currentFolder.getFolder().getMessages();
				}
				
				FetchProfile fp = new FetchProfile();
				fp.add(FetchProfile.Item.ENVELOPE);
				fp.add(FetchProfile.Item.FLAGS);
				currentFolder.getFolder().fetch(messages, fp);
				for (Message a : messages)
				{
					currentFolder.addMessage(new PodgeMessage((IMAPMessage) a));
				}
			}
			fireFolderSelected(currentFolder);
		}
		catch (MessagingException e)
		{
			logger.log(Level.SEVERE, "Problem changing current folder", e);
		}
	}
	
	private void closeCurrentFolder()
	{
		try
		{
			if (currentFolder != null && currentFolder.getFolder().isOpen())
			{
				currentFolder.getFolder().removeMessageChangedListener(messageChangedListener);
				currentFolder.getFolder().close();
			}
		}
		catch (MessagingException e)
		{
			logger.log(Level.SEVERE, e, () -> String.format("Error closing current folder: %s", e.getMessage()));
		}
	}

	public void toggleSeen(PodgeMessage message)
	{
		try
		{
			boolean set = message.getMessage().isSet(Flag.SEEN);
			message.getMessage().setFlag(Flag.SEEN, !set);
			message.setSeen(!set);
			fireMessageUpdated(message);
		}
		catch (MessagingException e)
		{
			logger.log(Level.SEVERE, "Problem toggling seen flag", e);
		}
	}

	@Override
	public PodgeItem getChild(int index)
	{
		return accounts.get(index);
	}

	@Override
	public int getChildCount()
	{
		return accounts.size();
	}

	@Override
	public int getIndexOfChild(PodgeItem child)
	{
		return accounts.indexOf(child);
	}

	@Override
	public PodgeItem getParent()
	{
		return null;
	}

	@Override
	public String getName()
	{
		return "ROOT";
	}

	@Override
	public String getDisplayText()
	{
		return getName();
	}

	public void addPodgeModelListener(PodgeModelListener l)
	{
		listeners.add(PodgeModelListener.class, l);
	}

	public void removePodgeModelListener(PodgeModelListener l)
	{
		listeners.remove(PodgeModelListener.class, l);
	}

	private void fireMessageUpdated(PodgeMessage message)
	{
		PodgeModelEvent e = new PodgeModelEvent(this, message);
		for (PodgeModelListener l : listeners.getListeners(PodgeModelListener.class))
		{
			l.messageUpdated(e);
		}
	}

	private void fireFolderSelected(PodgeFolder podgeFolder)
	{
		PodgeModelEvent e = new PodgeModelEvent(this, podgeFolder);
		for (PodgeModelListener l : listeners.getListeners(PodgeModelListener.class))
		{
			l.folderSelected(e);
		}
	}
	
	private void fireAccountConnected(PodgeAccount account)
	{
		PodgeModelEvent e = new PodgeModelEvent(this, account);
		for (PodgeModelListener l : listeners.getListeners(PodgeModelListener.class))
		{
			l.accountConnected(e);
		}
	}

	private void fireFoldersInserted(PodgeItem account, List<PodgeFolder> folders)
	{
		PodgeModelEvent e = new PodgeModelEvent(this, account, folders);
		for (PodgeModelListener l : listeners.getListeners(PodgeModelListener.class))
		{
			l.foldersInserted(e);
		}
	}

	private static class FolderComparator implements Comparator<PodgeFolder>
	{
		private static String[] SS = new String[] {
			"junk", "trash", "drafts", "sent", "inbox"
		};
		private static List<String> SPECIALS = Arrays.asList(SS);
		
		@Override
		public int compare(PodgeFolder o1, PodgeFolder o2)
		{
			int s1 = SPECIALS.indexOf(o1.getName().toLowerCase());
			int s2 = SPECIALS.indexOf(o2.getName().toLowerCase());
			if (s1 >= 0 || s2 >= 0)
			{
				return s1 > s2 ? -1 : 1; 
			}
			return o1.getName().compareToIgnoreCase(o2.getName());
		}
	}
}
