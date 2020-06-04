package org.colston.podge.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;

import com.sun.mail.imap.IMAPFolder;

public class PodgeFolder implements PodgeItem
{
	private static final Logger logger = Logger.getLogger(PodgeFolder.class.getName());
	
	private static Map<String, String> nameMap = new HashMap<>(3);
	
	private IMAPFolder folder;
	private PodgeItem parent;
	private List<PodgeFolder> childFolders = new ArrayList<>();

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
		return nameMap.containsKey(name) ? nameMap.get(name) : name;
	}

	public int getMessageCount()
	{
		try
		{
			return folder.getMessageCount();
		}
		catch (MessagingException e)
		{
			logger.log(Level.SEVERE, "Getting message count", e);
		}
		return 0;
	}

	public Message getMessage(int rowIndex)
	{
		try
		{
			return folder.getMessages()[rowIndex];
		}
		catch (MessagingException e)
		{
			logger.log(Level.SEVERE, "Getting message", e);
		}
		return null;
	}
}
