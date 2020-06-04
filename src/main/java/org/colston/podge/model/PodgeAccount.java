package org.colston.podge.model;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class PodgeAccount extends PodgeFolder
{
	private Session session;
	private Store store;
	private PodgeModel model;
	
	public PodgeAccount(PodgeModel model, Session session)
	{
		super(model);
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
