package org.colston.podge.model;

import java.util.List;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import com.sun.mail.imap.IMAPFolder;

public class PodgeAccount implements PodgeItem
{
	private Session session;
	private Store store;
	private IMAPFolder defaultFolder;
	private List<PodgeFolder> folders;
	private PodgeModel model;
	
	public PodgeAccount(PodgeModel model, Session session)
	{
		this.model = model;
		this.session = session;
	}

	@Override
	public String toString()
	{
		return "MailAccount [name=" + session.getProperty("mail.user") + "]";
	}

	Session getSession()
	{
		return session;
	}

	void setStore(Store store)
	{
		this.store = store;
		
	}

	void setDefaultFolder(IMAPFolder defaultFolder)
	{
		this.defaultFolder = defaultFolder;
	}

	void setFolders(List<PodgeFolder> folders)
	{
		this.folders = folders;
	}
	
	public void connect()
	{
		try
		{
			model.connect(this);
		}
		catch (AuthenticationFailedException e)
		{
			//TODO display a message somewhere or re-show the dialogue?
			System.err.printf("Authentication failed: %s%n", e.getMessage());
		}
		catch (MessagingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void disconnect()
	{
		if (store == null)
		{
			return;
		}
		try
		{
			store.close();
		}
		catch (MessagingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public PodgeItem getChild(int index)
	{
		return folders.get(index);
	}

	@Override
	public int getChildCount()
	{
		return folders == null ? 0 : folders.size();
	}

	@Override
	public int getIndexOfChild(PodgeItem child)
	{
		return folders.indexOf(child);
	}

	@Override
	public PodgeItem getParent()
	{
		return model;
	}

	@Override
	public String getName()
	{
		return session.getProperty("mail.user");
	}

	@Override
	public String getDisplayText()
	{
		return getName();
	}
}
