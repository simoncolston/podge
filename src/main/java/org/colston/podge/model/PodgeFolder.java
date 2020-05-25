package org.colston.podge.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Folder;

public class PodgeFolder implements PodgeItem
{
	private static Map<String, String> nameMap = new HashMap<>(3);
	
	private Folder folder;
	private PodgeItem parent;
	private List<PodgeFolder> childFolders = new ArrayList<>();

	static
	{
		nameMap.put("INBOX", "Inbox");
		nameMap.put("Trash", "Bin");
		nameMap.put("Junk", "Spam");
	}
	
	public PodgeFolder(Folder folder, PodgeItem parent)
	{
		this.folder = folder;
		this.parent = parent;
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
	public String getDisplayName()
	{
		String name = folder.getName();
		return nameMap.containsKey(name) ? nameMap.get(name) : name;
	}
}
