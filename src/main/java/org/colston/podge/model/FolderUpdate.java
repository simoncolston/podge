package org.colston.podge.model;

import java.util.ArrayList;
import java.util.List;

public class FolderUpdate
{
	private int unreadMessageCount;
	private int totalMessageCount;
	private List<PodgeMessage> messages = new ArrayList<>();
	
	public int getUnreadMessageCount()
	{
		return unreadMessageCount;
	}
	public void setUnreadMessageCount(int unreadMessageCount)
	{
		this.unreadMessageCount = unreadMessageCount;
	}
	public int getTotalMessageCount()
	{
		return totalMessageCount;
	}
	public void setTotalMessageCount(int totalMessageCount)
	{
		this.totalMessageCount = totalMessageCount;
	}
	public List<PodgeMessage> getMessages()
	{
		return messages;
	}
	public void addMessage(PodgeMessage message)
	{
		this.messages.add(message);
	}
}
