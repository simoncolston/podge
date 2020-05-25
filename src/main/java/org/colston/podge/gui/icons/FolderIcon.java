package org.colston.podge.gui.icons;

import java.awt.Graphics2D;

public class FolderIcon extends IconBase
{
	private static final int P = 2;
	private static final int[] xPoints = new int[] {PAD,         DIM - PAD, DIM - PAD,     PAD};
	private static final int[] yPoints = new int[] {PAD + P + P, PAD + P + P,   DIM - PAD - P, DIM - PAD - P};
	private static final int[] xtab    = new int[] {PAD,         PAD,     PAD + 5, PAD + 5};
	private static final int[] ytab    = new int[] {PAD + P + P, PAD + P, PAD + P, PAD + P + P};
	
	@Override
	protected void paintIcon(Graphics2D g2)
	{
		g2.drawPolygon(xPoints, yPoints, xPoints.length);
		g2.drawPolygon(xtab, ytab, xtab.length);
	}
}
