package org.colston.podge.gui.icons;

import java.awt.Graphics2D;

public class Read extends IconBase
{
	private static final Read icon = new Read();
	
	private Read()
	{
	}
	
	public static Read get()
	{
		return icon;
	}
	
	@Override
	protected void paintIcon(Graphics2D g2)
	{
		g2.drawRect(DIM / 2 - 1, DIM / 2 - 1, 2, 2);
	}
}
