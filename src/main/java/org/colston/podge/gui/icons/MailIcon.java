package org.colston.podge.gui.icons;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class MailIcon extends IconBase
{
	private static int P = 2;
	
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Graphics2D g2 = (Graphics2D) g.create();
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int[] xPoints = new int[] {PAD,     DIM - PAD, DIM - PAD,     PAD};
		int[] yPoints = new int[] {PAD + P, PAD + P,   DIM - PAD - P, DIM - PAD - P};

		//draw thick background colour
		g2.setColor(bg);
		g2.setStroke(thickStroke);
		g2.drawPolygon(xPoints, yPoints, xPoints.length);
		g2.drawLine(xPoints[0], yPoints[0], DIM / 2, DIM / 2);
		g2.drawLine(xPoints[1], yPoints[1], DIM / 2, DIM / 2);
		
		//draw thin foreground colour
		g2.setColor(fg);
		g2.setStroke(thinStroke);
		g2.drawPolygon(xPoints, yPoints, xPoints.length);
		g2.drawLine(xPoints[0], yPoints[0], DIM / 2, DIM / 2);
		g2.drawLine(xPoints[1], yPoints[1], DIM / 2, DIM / 2);
		
		g2.dispose();
	}
}
