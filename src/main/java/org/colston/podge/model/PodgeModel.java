package org.colston.podge.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.swing.event.EventListenerList;

import com.sun.mail.imap.IMAPFolder;

public class PodgeModel implements PodgeItem
{
	private EventListenerList listeners = new EventListenerList();
	
	private List<PodgeAccount> accounts = new ArrayList<>();

	
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
		account.setDefaultFolder(defaultFolder);
		List<Folder> fs = Arrays.asList(defaultFolder.list());
		List<PodgeFolder> folders = fs.stream().map(f -> new PodgeFolder((IMAPFolder) f, account)).collect(Collectors.toList());
		Collections.sort(folders, new FolderComparator());
		account.setFolders(folders);
		fireFoldersInserted(account, folders);

		addSubFolders(folders);
		
		//open inbox
		PodgeFolder inbox = folders.get(0);
		inbox.getFolder().open(Folder.READ_ONLY);
		inbox.getFolder().getMessages();  //load all messages ready in the cache
		fireFolderSelected(inbox);
	}

	private void addSubFolders(List<PodgeFolder> pfolders) throws MessagingException
	{
		for (PodgeFolder pfolder : pfolders)
		{
			Folder[] list = pfolder.getFolder().list();
			if (list.length == 0)
			{
				continue;
			}
			List<Folder> fs = Arrays.asList(list);
			List<PodgeFolder> folders = fs.stream().map(f -> new PodgeFolder((IMAPFolder) f, pfolder)).collect(Collectors.toList());
			Collections.sort(folders, new FolderComparator());
			pfolder.setFolders(folders);
			fireFoldersInserted(pfolder, folders);
			
			addSubFolders(folders);
		}
	}

	private List<PodgeFolder> processFolders(PodgeFolder parent, IMAPFolder folder)
	{
		
	}
	
	public void disconnectAll()
	{
		accounts.forEach(PodgeAccount::disconnect);
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
	public String getDisplayText()
	{
		return "ROOT";
	}

	public void addPodgeModelListener(PodgeModelListener l)
	{
		listeners.add(PodgeModelListener.class, l);
	}

	public void removePodgeModelListener(PodgeModelListener l)
	{
		listeners.remove(PodgeModelListener.class, l);
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
