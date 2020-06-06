package org.colston.podge.gui;

import java.util.StringJoiner;

import javax.mail.Address;
import javax.swing.table.AbstractTableModel;

import org.colston.podge.model.PodgeFolder;
import org.colston.podge.model.PodgeMessage;

public class MessageListModel extends AbstractTableModel
{
	private PodgeFolder folder;
	
	@Override
	public int getRowCount()
	{
		return folder == null ? 0 : folder.getMessageCount();
	}

	@Override
	public String getColumnName(int column)
	{
		return "";
	}


	@Override
	public int getColumnCount()
	{
		return 4;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		PodgeMessage m = folder.getMessage(rowIndex);
		switch (columnIndex)
		{
		case 0:
			return toText(m.getFrom());
		case 1:
			return m.getSubject();
		case 2:
			return m.getSentDate();
		case 3:
			return "*";
		}
		return "Hello!";
	}
	
	private String toText(Address[] from)
	{
		StringJoiner j = new StringJoiner("; "); 
		for (Address a : from)
		{
			j.add(a.toString());
		}
		return j.toString();
	}

	protected void setFolder(PodgeFolder folder)
	{
		this.folder = folder;
		fireTableDataChanged();
	}
}
