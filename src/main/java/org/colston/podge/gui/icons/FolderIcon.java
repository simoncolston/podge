package org.colston.podge.gui.icons;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class FolderIcon extends IconBase
{
	private static int P = 2;
	
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Graphics2D g2 = (Graphics2D) g.create();
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int[] xPoints = new int[] {PAD,         DIM - PAD, DIM - PAD,     PAD};
		int[] yPoints = new int[] {PAD + P + P, PAD + P + P,   DIM - PAD - P, DIM - PAD - P};
		int[] xtab = new int[] {PAD,         PAD,     PAD + 5, PAD + 5};
		int[] ytab = new int[] {PAD + P + P, PAD + P, PAD + P, PAD + P + P};

		//draw thick background colour
		g2.setColor(bg);
		g2.setStroke(thickStroke);
		g2.drawPolygon(xPoints, yPoints, xPoints.length);
		g2.drawPolygon(xtab, ytab, xtab.length);
		
		//draw thin foreground colour
		g2.setColor(fg);
		g2.setStroke(thinStroke);
		g2.drawPolygon(xPoints, yPoints, xPoints.length);
		g2.drawPolygon(xtab, ytab, xtab.length);
		
		g2.dispose();
	}
}
