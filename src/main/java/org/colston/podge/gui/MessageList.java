package org.colston.podge.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import org.colston.sclib.gui.chore.Chore;

public class MessageList
{
	private static final Logger logger = Logger.getLogger(MessageList.class.getName());
	
	private Config config;
	private PodgeModel model;
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
		this.model = model;

		panel = new JPanel(new BorderLayout());
		
		tableModel = new MessageListModel();
		table = new JTable(tableModel);
		initialiseColumns();
		table.addMouseListener(new MSL());
		table.getSelectionModel().addListSelectionListener(new LSL());
		table.setDefaultRenderer(LocalDateTime.class, dateTimeRenderer);
		table.setDefaultRenderer(String.class, stringRenderer);
		table.setRowHeight(MailIcon.get().getIconHeight());
		
		//both required to get rid of grid and spacing
		table.setShowGrid(false);
		table.setIntercellSpacing(new Dimension(0, 0));
		
		JScrollPane scr = new JScrollPane(table);
		panel.add(scr, BorderLayout.CENTER);
		
		JPanel info = new JPanel(new BorderLayout());
		info.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		infoLabel = new JLabel("Hello World!");
		info.add(infoLabel, BorderLayout.CENTER);
		countLabel = new JLabel();
		info.add(countLabel, BorderLayout.EAST);
		
		panel.add(info, BorderLayout.NORTH);
	}
	
	private void initialiseColumns()
	{
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		int total = 0;
		int w = 200;
		table.getColumnModel().getColumn(0).setPreferredWidth(w);
		total += w;
		w = 500;
		table.getColumnModel().getColumn(1).setPreferredWidth(w);
		total += w;
		w = 200;
		table.getColumnModel().getColumn(2).setPreferredWidth(w);
		table.getColumnModel().getColumn(2).setMaxWidth(w);
		table.getColumnModel().getColumn(2).setMinWidth(w);
		total += w;
		w = 26;
		table.getColumnModel().getColumn(3).setPreferredWidth(w);
		table.getColumnModel().getColumn(3).setMaxWidth(w);
		table.getColumnModel().getColumn(3).setMinWidth(w);
		total += w;

		Dimension d = table.getPreferredScrollableViewportSize();
		d.width = total;
		d.height = table.getRowHeight() * 12;
		table.setPreferredScrollableViewportSize(d);
	}

	public JPanel getComponent()
	{
		return panel;
	}
	
	public void folderSelected(PodgeFolder f)
	{
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
		
		int messageCount = f == null ? 0 : f.getMessageCount();
		int totalMessageCount = f == null ? 0 : f.getTotalMessageCount();
		countLabel.setText(String.format("[%d/%d]", messageCount, totalMessageCount));
	}


	private class LSL implements ListSelectionListener
	{
		
		@Override
		public void valueChanged(ListSelectionEvent e)
		{
			// TODO Auto-generated method stub
			
		}
		
	}

	public class MSL extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			int col = table.columnAtPoint(e.getPoint());
			if (col != 3)
			{
				return;
			}
			int row = table.rowAtPoint(e.getPoint());
			PodgeMessage message = tableModel.getMessage(row);
			Chore<Boolean> chore = new Chore<Boolean>()
			{
				@Override
				protected Boolean doChore() throws Exception
				{
					return model.toggleSeen(message);
				}

				@Override
				protected void updateUI()
				{
					message.setSeen(get());
					tableModel.messageUpdated(message);
				}
			};
			chore.execute();
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
