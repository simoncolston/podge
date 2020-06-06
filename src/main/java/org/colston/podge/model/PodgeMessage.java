package org.colston.podge.model;

import java.util.Arrays;
import java.util.Date;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import com.sun.mail.imap.IMAPMessage;

public class PodgeMessage
{
	private InternetAddress[] from;
	private InternetAddress[] to;
	private String subject;
	private Date sentDate;
	
	public PodgeMessage(IMAPMessage a) throws MessagingException
	{
		this.from = Arrays.copyOf(a.getFrom(), a.getFrom().length, InternetAddress[].class);
//		Address[] ato = a.getRecipients(Message.RecipientType.TO);
//		this.to = Arrays.copyOf(ato, ato.length, InternetAddress[].class);
		this.subject = a.getSubject();
		this.sentDate = a.getSentDate();
	}

	public InternetAddress[] getFrom()
	{
		return from;
	}

	public InternetAddress[] getTo()
	{
		return to;
	}

	public String getSubject()
	{
		return subject;
	}

	public Date getSentDate()
	{
		return sentDate;
	}
}
