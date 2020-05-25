package org.colston.podge.gui.icons;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class InboxIcon extends IconBase
{
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Graphics2D g2 = (Graphics2D) g.create();
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//draw thick background colour
		g2.setColor(bg);
		g2.setStroke(thickStroke);
		int p = DIM / 2;
		int q = 3 * DIM / 5;
		g2.drawLine(DIM / 2, PAD, p, q);
		g2.drawLine(p, q, p - HEAD, q - HEAD);
		g2.drawLine(p, q, p + HEAD, q - HEAD);
		g2.drawLine(PAD, DIM / 2, PAD, DIM - PAD);
		g2.drawLine(PAD, DIM - PAD, DIM - PAD, DIM - PAD);
		g2.drawLine(DIM - PAD, DIM / 2, DIM - PAD, DIM - PAD);
		
		//draw thin foreground colour
		g2.setColor(fg);
		g2.setStroke(thinStroke);
		g2.drawLine(DIM / 2, PAD, p, q);
		g2.drawLine(p, q, p - HEAD, q - HEAD);
		g2.drawLine(p, q, p + HEAD, q - HEAD);
		g2.drawLine(PAD, DIM / 2, PAD, DIM - PAD);
		g2.drawLine(PAD, DIM - PAD, DIM - PAD, DIM - PAD);
		g2.drawLine(DIM - PAD, DIM / 2, DIM - PAD, DIM - PAD);
		
		g2.dispose();
	}
}
