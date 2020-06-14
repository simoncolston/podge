package org.colston.podge;

import java.awt.Font;

public class Config
{
	private Font monospaceFont;
	private Font monospaceFontBold;
	
	public Config()
	{
		this.monospaceFont = new Font("Source Code Pro", Font.PLAIN, 15);
		this.monospaceFontBold = new Font("Source Code Pro", Font.BOLD, 15);
	}

	public Font getMonospaceFont()
	{
		return monospaceFont;
	}
	
	public Font getMonospaceFontBold()
	{
		return monospaceFontBold;
	}
}
