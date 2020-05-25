package org.colston.podge.model;

import java.util.EventObject;

public class PodgeModelEvent extends EventObject
{
	private PodgeItem item;

	public PodgeModelEvent(Object source, PodgeItem item)
	{
		super(source);
		this.item = item;
	}

	public PodgeItem getItem()
	{
		return item;
	}
}
