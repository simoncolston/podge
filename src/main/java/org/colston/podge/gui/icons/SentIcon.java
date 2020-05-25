package org.colston.podge.gui.icons;

import java.awt.Graphics2D;

public class SentIcon extends IconBase
{
	@Override
	protected void paintIcon(Graphics2D g2)
	{
		g2.drawLine(PAD, PAD, DIM - PAD, DIM / 2);
		g2.drawLine(PAD, DIM - PAD, DIM - PAD, DIM / 2);
		g2.drawLine(PAD + HEAD, DIM / 2, DIM - PAD, DIM / 2);
		g2.drawLine(PAD, PAD, PAD + HEAD, DIM / 2);
		g2.drawLine(PAD, DIM - PAD, PAD + HEAD, DIM / 2);
	}
}
