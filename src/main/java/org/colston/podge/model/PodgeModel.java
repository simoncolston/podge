package org.colston.podge.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.mail.FetchProfile;
import javax.mail.Flags.Flag;
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
	
	private PodgeAccount account = null;
	
	public void setAccount(PodgeAccount a)
	{
		account = a;
	}
	
	public void connect() throws MessagingException
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
		fireAccountConnected(inbox);
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
		return folders;
	}
	
	public void disconnect()
	{
		Store store = account.getStore();
		if (store == null)
		{
			return;
		}
		try
		{
			store.close();
		}
		catch (MessagingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public FolderUpdate changeCurrentFolder(PodgeFolder prev, PodgeFolder next) throws MessagingException
	{
		FolderUpdate update = new FolderUpdate();
		if (prev != null && prev.getFolder().isOpen())
		{
			prev.getFolder().close();
		}
		if (next != null && next.getFolder() != null && (next.getFolder().getType() & Folder.HOLDS_MESSAGES) > 0)
		{
			next.getFolder().open(Folder.READ_WRITE);
			int ucount = next.getFolder().getUnreadMessageCount();
			update.setUnreadMessageCount(ucount);
			
			int count = next.getFolder().getMessageCount();
			update.setTotalMessageCount(count);
			
			Message[] messages;
			if (count > 500)
			{
				LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
				SearchTerm term = new SentDateTerm(ComparisonTerm.GE, Date.valueOf(oneMonthAgo));
				messages = next.getFolder().search(term);
			}
			else 
			{
				messages = next.getFolder().getMessages();
			}
			
			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.ENVELOPE);
			fp.add(FetchProfile.Item.FLAGS);
			next.getFolder().fetch(messages, fp);
			for (Message a : messages)
			{
				update.addMessage(new PodgeMessage((IMAPMessage) a));
			}
		}
		return update;
	}

	public boolean toggleSeen(PodgeMessage message) throws MessagingException
	{
		boolean set = !(message.getMessage().isSet(Flag.SEEN));
		message.getMessage().setFlag(Flag.SEEN, set);
		return set;
	}

	@Override
	public PodgeItem getChild(int index)
	{
		return account;
	}

	@Override
	public int getChildCount()
	{
		return 1;
	}

	@Override
	public int getIndexOfChild(PodgeItem child)
	{
		return 0;
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

	/**
	 * Inform listeners that the account has connected.
	 * @param folder the item that should be selected after connection e.g. the inbox folder
	 */
	private void fireAccountConnected(PodgeItem folder)
	{
		PodgeModelEvent e = new PodgeModelEvent(this, folder);
		for (PodgeModelListener l : listeners.getListeners(PodgeModelListener.class))
		{
			l.accountConnected(e);
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
