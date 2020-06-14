package org.colston.podge.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import com.sun.mail.imap.IMAPMessage;

public class PodgeMessage
{
	private InternetAddress[] from;
	private InternetAddress[] to;
	private String subject;
	private LocalDateTime sentDate;
	private boolean seen;
	
	public PodgeMessage(IMAPMessage a) throws MessagingException
	{
		Address[] af = a.getFrom();
		this.from = af == null ? new InternetAddress[0] : Arrays.copyOf(af, af.length, InternetAddress[].class);
		Address[] ato = a.getRecipients(Message.RecipientType.TO);
		this.to = ato == null ? new InternetAddress[0] : Arrays.copyOf(ato, ato.length, InternetAddress[].class);
		this.subject = a.getSubject();
		this.sentDate = LocalDateTime.ofInstant(a.getSentDate().toInstant(), ZoneId.systemDefault());
		seen = a.getFlags().contains(Flags.Flag.SEEN);
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

	public LocalDateTime getSentDate()
	{
		return sentDate;
	}

	public boolean isSeen()
	{
		return seen;
	}
}
