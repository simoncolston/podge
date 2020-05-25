package org.colston.podge.gui;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class MessageList
{
	private MessageListModel model;
	private JTable table;

	public MessageList(JTree tree)
	{
		tree.addTreeSelectionListener(new TSL());
		model = new MessageListModel();
		table = new JTable(model);
	}
	
	public JTable getComponent()
	{
		return table;
	}
	
	public class TSL implements TreeSelectionListener
	{
		@Override
		public void valueChanged(TreeSelectionEvent e)
		{
			System.out.println("Tree selection: " + e.toString());
		}
	}
}
