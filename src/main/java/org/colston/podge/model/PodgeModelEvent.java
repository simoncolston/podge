package org.colston.podge.model;

import java.util.Collections;
import java.util.EventObject;
import java.util.List;

public class PodgeModelEvent extends EventObject
{
	private PodgeItem parent;
	private List<? extends PodgeItem> items;
	private PodgeMessage message;

	public PodgeModelEvent(Object source, PodgeItem item)
	{
		super(source);
		this.items = Collections.singletonList(item);
	}

	public PodgeModelEvent(Object source, PodgeItem parent, List<? extends PodgeItem> items)
	{
		super(source);
		this.parent = parent;
		this.items = items;
	}

	public PodgeModelEvent(Object source, PodgeMessage message)
	{
		super(source);
		this.message = message;
	}

	public PodgeItem getParent()
	{
		return parent;
	}
	
	public PodgeItem getItem()
	{
		return items.get(0);
	}
	
	public List<? extends PodgeItem> getItems()
	{
		return items;
	}
	
	public PodgeMessage getMessage()
	{
		return message;
	}
}
