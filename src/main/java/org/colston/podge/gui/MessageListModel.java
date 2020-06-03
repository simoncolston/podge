package org.colston.podge.gui;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.table.AbstractTableModel;

import org.colston.podge.model.PodgeFolder;

public class MessageListModel extends AbstractTableModel
{
	private PodgeFolder folder;
	
	@Override
	public int getRowCount()
	{
		return folder == null ? 0 : folder.getMessageCount();
	}

	@Override
	public int getColumnCount()
	{
		return 4;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Message m = folder.getMessage(rowIndex);
		try
		{
			switch (columnIndex)
			{
			case 0:
				return toText(m.getFrom());
			case 1:
				return m.getSubject();
			case 2:
				return m.getSentDate();
			case 3:
				return m.getMessageNumber();
			}
		}
		catch (MessagingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Hello!";
	}
	
	private Object toText(Address[] from)
	{
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Address a : from)
		{
			if (!first)
			{
				sb.append("; "); 
			}
			sb.append(a.toString());
			first = false;
		}
		return sb.toString();
	}

	protected void setFolder(PodgeFolder folder)
	{
		this.folder = folder;
		fireTableDataChanged();
	}
}
