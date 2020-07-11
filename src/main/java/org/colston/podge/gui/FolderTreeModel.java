package org.colston.podge.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.colston.podge.model.PodgeItem;

public class FolderTreeModel implements TreeModel
{
	private PodgeItem root;
	private EventListenerList listeners = new EventListenerList();
	
	public FolderTreeModel(PodgeItem root)
	{
		this.root = root;
	}
	
	@Override
	public Object getRoot()
	{
		return root;
	}

	@Override
	public Object getChild(Object parent, int index)
	{
		return ((PodgeItem) parent).getChild(index);
	}

	@Override
	public int getChildCount(Object parent)
	{
		return ((PodgeItem) parent).getChildCount();
	}

	@Override
	public boolean isLeaf(Object node)
	{
		return getChildCount(node) == 0;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getIndexOfChild(Object parent, Object child)
	{
		return ((PodgeItem) parent).getIndexOfChild((PodgeItem) child);
	}

	@Override
	public void addTreeModelListener(TreeModelListener l)
	{
		listeners.add(TreeModelListener.class, l);
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l)
	{
		listeners.remove(TreeModelListener.class, l);
	}
	
	protected void fireTreeStructureChanged()
	{
		TreeModelEvent e = new TreeModelEvent(this, new Object[] {root});
		for (TreeModelListener l : listeners.getListeners(TreeModelListener.class))
		{
			l.treeStructureChanged(e);
		}
	}
	
	protected void fireTreeNodesChanged(Object source, PodgeItem child)
	{
		PodgeItem parent = child.getParent();
		Object[] path = calculatePath(parent);
		int[] childIndices = new int[] {parent.getIndexOfChild(child)};
		Object[] children = new Object[] {child};
		TreeModelEvent e = new TreeModelEvent(source, path, childIndices, children);
		for (TreeModelListener l : listeners.getListeners(TreeModelListener.class))
		{
			l.treeNodesChanged(e);
		}
	}
	
	protected void fireTreeNodesInserted(Object source, PodgeItem parent, List<? extends PodgeItem> items)
	{
		Object[] path = calculatePath(parent);
		int[] childIndices = new int[items.size()];
		Arrays.parallelSetAll(childIndices, p -> p);
		Object[] children = items.toArray();
		TreeModelEvent e = new TreeModelEvent(source, path, childIndices, children);
		for (TreeModelListener l : listeners.getListeners(TreeModelListener.class))
		{
			l.treeNodesInserted(e);
		}
	}

	protected Object[] calculatePath(PodgeItem item)
	{
		List<Object> path = new ArrayList<>();
		calculatePath(path, item);
		return path.toArray();
	}

	protected void calculatePath(List<Object> path, PodgeItem item)
	{
		path.add(0, item);
		if (item == root)
		{
			return;
		}
		calculatePath(path, item.getParent());
	}
}
