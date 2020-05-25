package org.colston.podge.gui;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.colston.podge.gui.icons.BinIcon;
import org.colston.podge.gui.icons.DraftsIcon;
import org.colston.podge.gui.icons.FolderIcon;
import org.colston.podge.gui.icons.InboxIcon;
import org.colston.podge.gui.icons.MailIcon;
import org.colston.podge.gui.icons.SentIcon;
import org.colston.podge.gui.icons.SpamIcon;
import org.colston.podge.model.PodgeAccount;
import org.colston.podge.model.PodgeItem;
import org.colston.podge.model.PodgeModel;
import org.colston.podge.model.PodgeModelEvent;
import org.colston.podge.model.PodgeModelListener;

public final class FolderTree
{
	private JTree tree;
	private FolderTreeModel treeModel;
	
	public FolderTree(PodgeModel model)
	{
		model.addPodgeModelListener(new PML());
		treeModel = new FolderTreeModel(model);
		tree = new JTree(treeModel);
		tree.setCellRenderer(new TCR());
		tree.setRootVisible(false);
	}

	public JTree getComponent()
	{
		return tree;
	}
	
	private Icon getIconFor(PodgeItem item)
	{
		if ("Inbox".equals(item.getDisplayName()))
		{
			return new InboxIcon();
		}
		if ("Sent".equals(item.getDisplayName()))
		{
			return new SentIcon();
		}
		if ("Spam".equals(item.getDisplayName()))
		{
			return new SpamIcon();
		}
		if ("Drafts".equals(item.getDisplayName()))
		{
			return new DraftsIcon();
		}
		if ("Bin".equals(item.getDisplayName()))
		{
			return new BinIcon();
		}
		if (item instanceof PodgeAccount)
		{
			return new MailIcon();
		}
		return new FolderIcon();
	}

	public class PML implements PodgeModelListener
	{
		@Override
		public void accountConnected(PodgeModelEvent e)
		{
			treeModel.fireTreeNodesChanged(e.getSource(), e.getItem());
		}
	}

	public class TCR extends DefaultTreeCellRenderer
	{
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
				boolean leaf, int row, boolean hasFocus)
		{
			JLabel l = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
			PodgeItem item = (PodgeItem) value;
			l.setIcon(getIconFor(item));
			l.setText(item.getDisplayName());
			return l;
		}
	}
}
