package org.colston.podge.model;

import java.util.EventListener;

public interface PodgeModelListener extends EventListener
{
	void accountConnected(PodgeModelEvent e);

	void foldersInserted(PodgeModelEvent e);

	void folderSelected(PodgeModelEvent e);
}
