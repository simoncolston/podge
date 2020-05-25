package org.colston.podge.gui.icons;

import java.awt.Graphics2D;

public class SpamIcon extends IconBase
{
	private static final SpamIcon icon = new SpamIcon();
	
	private SpamIcon()
	{
	}
	
	public static SpamIcon get()
	{
		return icon;
	}

	@Override
	protected void paintIcon(Graphics2D g2)
	{
		g2.drawOval(PAD, PAD, DIM - 2 * PAD, DIM - 2 * PAD);
		g2.drawLine(DIM / 2, DIM / 2, DIM / 2 + 5, DIM / 2 - 5);
		g2.drawLine(DIM / 2, DIM / 2, DIM / 2 - 5, DIM / 2 + 5);
	}
}
