package org.colston.podge.model;

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

	Store getStore() {
		return store;
	}
	
	void setStore(Store store)
	{
		this.store = store;
		
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
