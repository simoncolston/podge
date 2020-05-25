package org.colston.podge.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.InetAddress;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.colston.sclib.i18n.Messages;

public class InteractiveAuthenticator extends Authenticator
{
	private JFrame frame;

	public InteractiveAuthenticator(JFrame frame)
	{
		this.frame = frame;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication()
	{
		String prompt = getRequestingPrompt() != null ? getRequestingPrompt()
				: Messages.get(this.getClass(), "authenticator.prompt");
		String protocol = getRequestingProtocol() != null ? getRequestingProtocol()
				: Messages.get(this.getClass(), "authenticator.unknown.protocol");
		InetAddress inet = getRequestingSite();
		String host = inet.getHostName();
		host = host != null ? host : Messages.get(this.getClass(), "authenticator.unknown.host");
		String port = getRequestingPort() == -1 ? "" : String.valueOf(getRequestingPort());
		String info = Messages.get(this.getClass(), "authenticator.connection.info", protocol, host, port);

		GridBagLayout gb = new GridBagLayout();
		JPanel d = new JPanel(gb);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2, 2, 2, 2);

		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 0.0;
		d.add(constrain(new JLabel(info), gb, c));
		d.add(constrain(new JLabel(prompt), gb, c));

		c.gridwidth = 1;
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		String userPrompt = Messages.get(this.getClass(), "authenticator.user.prompt");
		d.add(constrain(new JLabel(userPrompt), gb, c));

		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;
		String user = getDefaultUserName();
		JTextField username = new JTextField(user, 30);
		d.add(constrain(username, gb, c));

		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		c.weightx = 0.0;
		String passPrompt = Messages.get(this.getClass(), "authenticator.pass.prompt");
		d.add(constrain(new JLabel(passPrompt), gb, c));

		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;
		JPasswordField password = new JPasswordField("", 30);
		d.add(constrain(password, gb, c));

		d.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentShown(ComponentEvent e)
			{
				if (user != null && user.length() > 0)
					password.requestFocus();
				else
					username.requestFocus();
			}
		});

		String title = Messages.get(this.getClass(), "authenticator.title");
		int result = JOptionPane.showConfirmDialog(frame, d, title, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);

		if (result == JOptionPane.OK_OPTION)
			return new PasswordAuthentication(username.getText(), new String(password.getPassword()));
		else
			return null;
	}

	private Component constrain(Component cmp, GridBagLayout gb, GridBagConstraints c)
	{
		gb.setConstraints(cmp, c);
		return (cmp);
	}

}
