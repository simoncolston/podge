package org.colston.podge.gui;

import javax.swing.JTable;

import org.colston.podge.model.PodgeFolder;
import org.colston.podge.model.PodgeModel;
import org.colston.podge.model.PodgeModelEvent;
import org.colston.podge.model.PodgeModelListener;

public class MessageList
{
	private MessageListModel tableModel;
	private JTable table;

	public MessageList(PodgeModel model)
	{
		model.addPodgeModelListener(new PML());
		tableModel = new MessageListModel();
		table = new JTable(tableModel);
	}
	
	public JTable getComponent()
	{
		return table;
	}
	
	private class PML implements PodgeModelListener
	{

		@Override
		public void accountConnected(PodgeModelEvent e)
		{
		}

		@Override
		public void foldersInserted(PodgeModelEvent e)
		{
		}

		@Override
		public void folderSelected(PodgeModelEvent e)
		{
			tableModel.setFolder((PodgeFolder) e.getItem());
		}
	}
}
