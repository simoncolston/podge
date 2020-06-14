package org.colston.podge.gui;

import java.time.LocalDateTime;
import java.util.StringJoiner;

import javax.mail.Address;
import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;

import org.colston.podge.gui.icons.FolderIcon;
import org.colston.podge.gui.icons.MailIcon;
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
			return m.isSeen() ? FolderIcon.get() : MailIcon.get();
		}
		return "Hello!";
	}
	
	@Override
	public int getColumnCount()
	{
		return 4;
	}

	@Override
	public String getColumnName(int column)
	{
		return "";
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		switch (columnIndex)
		{
		case 2:
			return LocalDateTime.class;
		case 3:
			return Icon.class;
		default:
			break;
		}
		return String.class;
	}

	public PodgeMessage getMessage(int rowIndex)
	{
		return folder.getMessage(rowIndex);
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
