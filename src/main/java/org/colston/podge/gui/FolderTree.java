package org.colston.podge.gui;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import org.colston.podge.gui.icons.BinIcon;
import org.colston.podge.gui.icons.DraftsIcon;
import org.colston.podge.gui.icons.FolderIcon;
import org.colston.podge.gui.icons.InboxIcon;
import org.colston.podge.gui.icons.MailIcon;
import org.colston.podge.gui.icons.SentIcon;
import org.colston.podge.gui.icons.SpamIcon;
import org.colston.podge.model.FolderUpdate;
import org.colston.podge.model.PodgeAccount;
import org.colston.podge.model.PodgeFolder;
import org.colston.podge.model.PodgeItem;
import org.colston.podge.model.PodgeModel;
import org.colston.podge.model.PodgeModelEvent;
import org.colston.podge.model.PodgeModelListener;
import org.colston.sclib.gui.chore.Chore;

public final class FolderTree
{
	private PodgeModel model;
	private JTree tree;
	private FolderTreeModel treeModel;
	private MessageList messageList;
	
	public FolderTree(PodgeModel model, MessageList messageList)
	{
		this.model = model;
		model.addPodgeModelListener(new PML());
		this.messageList = messageList;
		
		treeModel = new FolderTreeModel(model);
		tree = new JTree(treeModel);
		tree.addTreeSelectionListener(new TSL());
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

	private void accountConnected(PodgeFolder podgeFolder)
	{
		treeModel.fireTreeStructureChanged();
		Object[] p = treeModel.calculatePath(podgeFolder);
		TreePath path = new TreePath(p );
		tree.setSelectionPath(path);
	}
	
	private class PML implements PodgeModelListener
	{
		@Override
		public void accountConnected(PodgeModelEvent e)
		{
			EventQueue.invokeLater(() -> {
				FolderTree.this.accountConnected((PodgeFolder) e.getItem());
			});
		}
	}

	private class TSL implements TreeSelectionListener
	{
		@Override
		public void valueChanged(TreeSelectionEvent e)
		{
			TreePath olsp = e.getOldLeadSelectionPath();
			PodgeFolder prev = olsp == null ? null : (PodgeFolder) olsp.getLastPathComponent();
			TreePath nlsp = e.getNewLeadSelectionPath();
			PodgeFolder next = nlsp == null ? null : (PodgeFolder) nlsp.getLastPathComponent();
			Chore<FolderUpdate> chore = new Chore<>()
			{
				@Override
				protected FolderUpdate doChore() throws Exception
				{
					return model.changeCurrentFolder(prev, next);
				}

				@Override
				protected void updateUI()
				{
					FolderUpdate update = get();
					
					if (next != null) 
					{
						next.setUnreadMessageCount(update.getUnreadMessageCount());
						next.setTotalMessageCount(update.getTotalMessageCount());
						next.setMessages(update.getMessages());
						treeModel.fireTreeNodesChanged(e.getSource(), next);
						
						Object[] path = treeModel.calculatePath(next);
						TreePath tp = new TreePath(path);
						tree.scrollPathToVisible(tp);
					}
					
					messageList.folderSelected(next);
				}
			};
			chore.execute();
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
