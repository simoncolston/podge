package org.colston.podge.gui.icons;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class SentIcon extends IconBase
{
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Graphics2D g2 = (Graphics2D) g.create();
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//draw thick background colour
		g2.setColor(bg);
		g2.setStroke(thickStroke);
		g2.drawLine(PAD, PAD, DIM - PAD, DIM / 2);
		g2.drawLine(PAD, DIM - PAD, DIM - PAD, DIM / 2);
		g2.drawLine(PAD + HEAD, DIM / 2, DIM - PAD, DIM / 2);
		g2.drawLine(PAD, PAD, PAD + HEAD, DIM / 2);
		g2.drawLine(PAD, DIM - PAD, PAD + HEAD, DIM / 2);
		
		//draw thin foreground colour
		g2.setColor(fg);
		g2.setStroke(thinStroke);
		g2.drawLine(PAD, PAD, DIM - PAD, DIM / 2);
		g2.drawLine(PAD, DIM - PAD, DIM - PAD, DIM / 2);
		g2.drawLine(PAD + HEAD, DIM / 2, DIM - PAD, DIM / 2);
		g2.drawLine(PAD, PAD, PAD + HEAD, DIM / 2);
		g2.drawLine(PAD, DIM - PAD, PAD + HEAD, DIM / 2);
		
		g2.dispose();
	}
}
