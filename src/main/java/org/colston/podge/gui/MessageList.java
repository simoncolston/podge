package org.colston.podge.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.colston.podge.Config;
import org.colston.podge.gui.icons.MailIcon;
import org.colston.podge.model.PodgeFolder;
import org.colston.podge.model.PodgeMessage;
import org.colston.podge.model.PodgeModel;
import org.colston.podge.model.PodgeModelEvent;
import org.colston.podge.model.PodgeModelListener;

public class MessageList
{
	private static final Logger logger = Logger.getLogger(MessageList.class.getName());
	
	private Config config;
	private MessageListModel tableModel;
	private JTable table;
	private JPanel panel;
	private JLabel infoLabel;
	private JLabel countLabel;

	private TableCellRenderer dateTimeRenderer = new LocalDateTimeTableCellRenderer();
	private TableCellRenderer stringRenderer = new StringTableCellRenderer();

	public MessageList(Config config, PodgeModel model)
	{
		this.config = config;
		model.addPodgeModelListener(new PML());

		panel = new JPanel(new BorderLayout());
		
		tableModel = new MessageListModel();
		table = new JTable(tableModel);
		JScrollPane scr = new JScrollPane(table);
		table.getSelectionModel().addListSelectionListener(new LSL());
		initialiseColumns(200, 500, 200, 26);
		table.setDefaultRenderer(LocalDateTime.class, dateTimeRenderer);
		table.setDefaultRenderer(String.class, stringRenderer);
		table.setRowHeight(MailIcon.get().getIconHeight());
		panel.add(scr, BorderLayout.CENTER);
		
		JPanel info = new JPanel(new BorderLayout());
		info.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		infoLabel = new JLabel("Hello World!");
		info.add(infoLabel, BorderLayout.CENTER);
		countLabel = new JLabel();
		info.add(countLabel, BorderLayout.EAST);
		
		panel.add(info, BorderLayout.NORTH);
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

	public JPanel getComponent()
	{
		return panel;
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
			PodgeFolder f = (PodgeFolder) e.getItem();
			tableModel.setFolder(f);
			/*
			 * select last message in list
			 */
			int count = tableModel.getRowCount() - 1;
			table.getSelectionModel().setSelectionInterval(count , count);
			/*
			 * scroll to that message - note the scroll to the top first is necessary when moving from a bigger folder
			 * to a smaller folder. 
			 */
			Rectangle r = table.getCellRect(0, 0, true);
			table.scrollRectToVisible(r);
			r = table.getCellRect(count, 0, true);
			table.scrollRectToVisible(r);
			
			countLabel.setText(String.format("[%d/%d]", f.getMessageCount(), f.getTotalMessageCount()));
		}
	}

	private class LocalDateTimeTableCellRenderer extends StringTableCellRenderer
	{
		private final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MMM-yy HH:mm (EEE)");
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column)
		{
			JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			LocalDateTime v = (LocalDateTime) value;
			l.setText(v.format(format));
			return l;
		}
	}

	private class StringTableCellRenderer extends DefaultTableCellRenderer
	{
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column)
		{
			JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			PodgeMessage m = ((MessageListModel) table.getModel()).getMessage(row);
			l.setFont(m.isSeen() ? config.getMonospaceFont() : config.getMonospaceFontBold());
			return l;
		}
	}
}
