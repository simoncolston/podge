package org.colston.podge.gui.icons;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class DraftsIcon extends IconBase
{
	private static int H = 5;
	private static int P = 2;
	
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Graphics2D g2 = (Graphics2D) g.create();
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int[] xPoints = new int[] {DIM - PAD - H - P, DIM - PAD - H - P, DIM - PAD - P};
		int[] yPoints = new int[] {PAD,               PAD + H,           PAD + H};

		//draw thick background colour
		g2.setColor(bg);
		g2.setStroke(thickStroke);
		g2.drawPolygon(xPoints, yPoints, xPoints.length);
		g2.drawLine(PAD + P, PAD, PAD + P, DIM - PAD);
		g2.drawLine(PAD + P, PAD, xPoints[0], yPoints[0]);
		g2.drawLine(xPoints[2], yPoints[2], xPoints[2], DIM - PAD);
		g2.drawLine(PAD + P, DIM - PAD, xPoints[2], DIM - PAD);
		
		
		//draw thin foreground colour
		g2.setColor(fg);
		g2.setStroke(thinStroke);
		g2.drawPolygon(xPoints, yPoints, xPoints.length);
		g2.drawLine(PAD + P, PAD, PAD + P, DIM - PAD);
		g2.drawLine(PAD + P, PAD, xPoints[0], yPoints[0]);
		g2.drawLine(xPoints[2], yPoints[2], xPoints[2], DIM - PAD);
		g2.drawLine(PAD + P, DIM - PAD, xPoints[2], DIM - PAD);
		
		g2.dispose();
	}
}
