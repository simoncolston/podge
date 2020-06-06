package org.colston.podge.gui;

import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.colston.podge.model.PodgeFolder;
import org.colston.podge.model.PodgeModel;
import org.colston.podge.model.PodgeModelEvent;
import org.colston.podge.model.PodgeModelListener;

public class MessageList
{
	private static final Logger logger = Logger.getLogger(MessageList.class.getName());
	
	private MessageListModel tableModel;
	private JTable table;

	public MessageList(PodgeModel model)
	{
		model.addPodgeModelListener(new PML());
		tableModel = new MessageListModel();
		table = new JTable(tableModel);
		table.getSelectionModel().addListSelectionListener(new LSL());
		initialiseColumns(200, 500, 200, 40);
	}
	
	private void initialiseColumns(int ... widths)
	{
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		int total = 0;
		for (int i = 0; i < widths.length; i++)
		{
			table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
			total += widths[i];
		}
		Dimension d = table.getPreferredScrollableViewportSize();
		d.width = total;
		d.height = table.getRowHeight() * 12;
		table.setPreferredScrollableViewportSize(d);
	}

	public JTable getComponent()
	{
		return table;
	}
	
	private class LSL implements ListSelectionListener
	{
		
		@Override
		public void valueChanged(ListSelectionEvent e)
		{
			// TODO Auto-generated method stub
			
		}
		
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
			int count = tableModel.getRowCount() - 1;
			table.getSelectionModel().setSelectionInterval(count , count);
			table.scrollRectToVisible(table.getCellRect(count, count, false));
		}
	}
}
