package org.colston.podge.gui.icons;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class BinIcon extends IconBase
{
	private static int P = 2;
	private static int H = 4;
	
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Graphics2D g2 = (Graphics2D) g.create();
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int[] xhandle = new int[] {DIM / 2 - 3, DIM / 2 + 3, DIM / 2 + 3, DIM / 2 - 3};
		int[] yhandle = new int[] {PAD,         PAD,         PAD + P,     PAD + P};
		int[] xlid = new int[] {PAD,     DIM - PAD, DIM - PAD,   PAD};
		int[] ylid = new int[] {PAD + P, PAD + P,   PAD + P + H, PAD + P + H};
		int[] xbody = new int[] {PAD + P + 1,     DIM - PAD - P - 1, DIM - PAD - P - 1, PAD + P + 1};
		int[] ybody = new int[] {PAD + P + H, PAD + P + H,   DIM - PAD,     DIM - PAD};
		

		//draw thick background colour
		g2.setColor(bg);
		g2.setStroke(thickStroke);
		g2.drawPolygon(xhandle, yhandle, yhandle.length);
		g2.drawPolygon(xlid, ylid, xlid.length);
		g2.drawPolygon(xbody, ybody, xbody.length);
		
		//draw thin foreground colour
		g2.setColor(fg);
		g2.setStroke(thinStroke);
		g2.drawPolygon(xhandle, yhandle, yhandle.length);
		g2.drawPolygon(xlid, ylid, xlid.length);
		g2.drawPolygon(xbody, ybody, xbody.length);
		
		g2.dispose();
	}
}
