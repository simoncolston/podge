package org.colston.podge.gui.icons;

import java.awt.BasicStroke;
import java.awt.Color;

import javax.swing.Icon;

public abstract class IconBase implements Icon
{
	protected static final int DIM = 24;
	protected static final int PAD = 4;
	protected static final int HEAD = 4;
	
	protected BasicStroke thickStroke = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	protected BasicStroke thinStroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
//	protected Color bg = Color.WHITE;
	protected Color bg = new Color(0xC4, 0xC4, 0xC4);
	protected Color fg = Color.BLACK;
//	protected Color fg = new Color(0x04, 0x0B, 0x60);
//	protected Color fg = new Color(0x0A, 0x19, 0xC2);

	@Override
	public int getIconWidth()
	{
		return DIM;
	}

	@Override
	public int getIconHeight()
	{
		return DIM;
	}
}
