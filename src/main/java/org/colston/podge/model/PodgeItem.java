package org.colston.podge.model;

public interface PodgeItem
{
	PodgeItem getChild(int index);

	int getChildCount();

	int getIndexOfChild(PodgeItem child);

	PodgeItem getParent();

	String getDisplayName();
}
