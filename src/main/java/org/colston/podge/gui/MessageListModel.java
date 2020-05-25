package org.colston.podge.gui;

import javax.swing.table.AbstractTableModel;

public class MessageListModel extends AbstractTableModel
{
	@Override
	public int getRowCount()
	{
		return 3;
	}

	@Override
	public int getColumnCount()
	{
		return 4;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return String.format("%d:%d", rowIndex, columnIndex);
	}
}
