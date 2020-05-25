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
import javax.swing.Icon;
import javax.swing.event.EventListenerList;

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
		
		Folder defaultFolder = store.getDefaultFolder();
		account.setDefaultFolder(defaultFolder);
		List<Folder> fs = Arrays.asList(defaultFolder.list());
		Collections.sort(fs, new FolderComparator());
		List<PodgeFolder> folders = fs.stream().map(f -> new PodgeFolder(f, account)).collect(Collectors.toList());
		account.setFolders(folders);
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
	public String getDisplayName()
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

	private void fireAccountConnected(PodgeAccount account)
	{
		PodgeModelEvent e = new PodgeModelEvent(this, account);
		for (PodgeModelListener l : listeners.getListeners(PodgeModelListener.class))
		{
			l.accountConnected(e);
		}
	}
	
	private static class FolderComparator implements Comparator<Folder>
	{
		private static String[] SS = new String[] {
			"junk", "trash", "drafts", "sent", "inbox"
		};
		private static List<String> SPECIALS = Arrays.asList(SS);
		
		@Override
		public int compare(Folder o1, Folder o2)
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
