package org.colston.podge.gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import org.colston.podge.gui.icons.BinIcon;
import org.colston.podge.gui.icons.DraftsIcon;
import org.colston.podge.gui.icons.FolderIcon;
import org.colston.podge.gui.icons.InboxIcon;
import org.colston.podge.gui.icons.MailIcon;
import org.colston.podge.gui.icons.SentIcon;
import org.colston.podge.gui.icons.SpamIcon;
import org.colston.podge.model.PodgeAccount;
import org.colston.podge.model.PodgeFolder;
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
		tree.addTreeSelectionListener(e -> model.selectFolder((PodgeFolder) e.getPath().getLastPathComponent()));
		tree.setCellRenderer(new TCR());
		tree.setRootVisible(false);
	}

	public JTree getComponent()
	{
		return tree;
	}
	
	private Icon getIconFor(PodgeItem item)
	{
		if (item instanceof PodgeAccount)
		{
			return MailIcon.get();
		}
		if ("Inbox".equals(item.getName()))
		{
			return InboxIcon.get();
		}
		if ("Sent".equals(item.getName()))
		{
			return SentIcon.get();
		}
		if ("Spam".equals(item.getName()))
		{
			return SpamIcon.get();
		}
		if ("Drafts".equals(item.getName()))
		{
			return DraftsIcon.get();
		}
		if ("Bin".equals(item.getName()))
		{
			return BinIcon.get();
		}
		return FolderIcon.get();
	}

	public class PML implements PodgeModelListener
	{
		@Override
		public void folderSelected(PodgeModelEvent e)
		{
			Object[] path = treeModel.calculatePath(e.getItem());
			TreePath tp = new TreePath(path);
			tree.scrollPathToVisible(tp);
			tree.setSelectionPath(tp);
			treeModel.fireTreeNodesChanged(e.getSource(), e.getItem());
		}

		@Override
		public void accountConnected(PodgeModelEvent e)
		{
			treeModel.fireTreeNodesChanged(e.getSource(), e.getItem());
		}

		@Override
		public void foldersInserted(PodgeModelEvent e)
		{
			treeModel.fireTreeNodesInserted(e.getSource(), e.getParent(), e.getItems());
		}

		@Override
		public void messageUpdated(PodgeModelEvent e)
		{
			// TODO Auto-generated method stub
			
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
			l.setText(item.getDisplayText());
			return l;
		}
	}
}
