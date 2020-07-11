package org.colston.podge.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.sun.mail.imap.IMAPFolder;

public class PodgeFolder implements PodgeItem
{
	private static final Logger logger = Logger.getLogger(PodgeFolder.class.getName());
	
	private static Map<String, String> nameMap = new HashMap<>(3);
	
	private IMAPFolder folder;
	private PodgeItem parent;
	private List<PodgeFolder> childFolders = new ArrayList<>();
	private List<PodgeMessage> messages = new ArrayList<>();
	private int totalMessageCount;
	private int unreadMessageCount;

	
	static
	{
		nameMap.put("INBOX", "Inbox");
		nameMap.put("Trash", "Bin");
		nameMap.put("Junk", "Spam");
	}
	
	protected PodgeFolder(PodgeItem parent)
	{
		this(null, parent);
	}
	
	public PodgeFolder(IMAPFolder folder, PodgeItem parent)
	{
		this.folder = folder;
		this.parent = parent;
	}
	
	protected IMAPFolder getFolder()
	{
		return folder;
	}

	protected void setFolder(IMAPFolder folder)
	{
		this.folder = folder;
	}
	
	protected void setFolders(List<PodgeFolder> folders)
	{
		this.childFolders = folders;
	}
	
	@Override
	public PodgeItem getChild(int index)
	{
		return childFolders.get(index);
	}

	@Override
	public int getChildCount()
	{
		return childFolders.size();
	}

	@Override
	public int getIndexOfChild(PodgeItem child)
	{
		return childFolders.indexOf(child);
	}

	@Override
	public PodgeItem getParent()
	{
		return parent;
	}

	@Override
	public String getName()
	{
		return folder.getName();
	}

	@Override
	public String getDisplayText()
	{
		String name = getName();
		name = nameMap.containsKey(name) ? nameMap.get(name) : name;
		if (unreadMessageCount > 0)
		{
			name += String.format(" (%d)", unreadMessageCount);
		}
		return name;
	}

	public int getMessageCount()
	{
		return messages.size();
	}

	public PodgeMessage getMessage(int rowIndex)
	{
		return messages.get(rowIndex);
	}
	
	public int getIndexOfMessage(PodgeMessage message)
	{
		return messages.indexOf(message);
	}
	
	protected void addMessage(PodgeMessage m)
	{
		messages.add(m);
	}
	
	public void setMessages(List<PodgeMessage> ms)
	{
		messages.clear();
		messages.addAll(ms);
	}

	protected void clearMessages()
	{
		messages.clear();
	}

	public int getTotalMessageCount()
	{
		return totalMessageCount;
	}

	public void setTotalMessageCount(int totalMessageCount)
	{
		this.totalMessageCount = totalMessageCount;
	}

	public void setUnreadMessageCount(int ucount)
	{
		this.unreadMessageCount = ucount;
	}
}
