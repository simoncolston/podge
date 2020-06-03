package org.colston.podge.model;

public interface PodgeItem
{
	PodgeItem getChild(int index);

	int getChildCount();

	int getIndexOfChild(PodgeItem child);

	PodgeItem getParent();

	/**
	 * This is the base name that can be used for sorting and identifying special items, e.g. "Inbox".
	 * @return name
	 */
	String getName();
	
	/**
	 * Text that includes the name and other supplementary information that may be of interest, e.g. "Inbox (3).
	 * @return text for display
	 */
	String getDisplayText();
}
