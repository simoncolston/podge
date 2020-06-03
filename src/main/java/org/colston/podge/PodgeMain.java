package org.colston.podge;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.mail.Authenticator;
import javax.mail.Session;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import org.colston.podge.gui.FolderTree;
import org.colston.podge.gui.InteractiveAuthenticator;
import org.colston.podge.gui.MessageList;
import org.colston.podge.model.PodgeAccount;
import org.colston.podge.model.PodgeModel;
import org.colston.sclib.gui.GuiApp;
import org.colston.sclib.i18n.Messages;

public class PodgeMain extends GuiApp
{
	private static final String APPLICATION_NAME = "Podge Mail";

	private static PodgeMain podge;
	private static final String SPLASH_FILE_NAME = "splash.png";

	public static void main(String[] args)
	{
		podge = new PodgeMain();
		podge.start();
	}

	private PodgeModel model;
	private JPanel mainPanel;

	private static final Logger logger = Logger.getLogger(PodgeMain.class.getName());
	
	@Override
	protected Icon getSplashIcon()
	{
		URL url = PodgeMain.class.getResource(SPLASH_FILE_NAME);
		return new ImageIcon(url);
	}


	@Override
	protected String getApplicationName()
	{
		return APPLICATION_NAME;
	}

	@Override
	protected String getConfigDirName()
	{
		return ".podgemail";
	}

	@Override
	protected WindowListener getFrameWindowListener()
	{
		return new WindowAdapter()
		{
			
			@Override
			public void windowOpened(WindowEvent e)
			{
				//TODO take this off the event thread
				model.getAccounts().forEach(PodgeAccount::connect);
			}

			@Override
			public void windowClosing(WindowEvent e)
			{
				if (true) //TODO: if all work is finished and saved
				{
					model.disconnectAll();
					getFrame().dispose();
					return;
				}
				String message = Messages.get(PodgeMain.class, "window.closing.message");
				String title = Messages.get(PodgeMain.class, "window.closing.title");
				Object[] options = new Object[] 
						{
							Messages.get(PodgeMain.class, "window.closing.save"),
							Messages.get(PodgeMain.class, "window.closing.dont.save"),
							Messages.get(PodgeMain.class, "window.closing.cancel")
						};
				int ret = JOptionPane.showOptionDialog(getFrame(), message, title, JOptionPane.YES_NO_CANCEL_OPTION, 
						JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				switch (ret)
				{
				//save
				case 0:
					getFrame().dispose();
					break;
				
				//don't save	
				case 1:
					getFrame().dispose();
					break;
				
				//cancel
				case JOptionPane.CLOSED_OPTION:
				case 2:
				default:
					// Don't exit
					break;
				}
			}
		};
	}

	
	@Override
	protected List<? extends Image> getFrameIconImages()
	{
//		try
//		{
//			List<Image> images = new ArrayList<>(ICON_RESOURCES.length);
//			for (String iname : ICON_RESOURCES)
//			{
//				images.add(ImageIO.read(PodgeMain.class.getResource(iname)));
//			}
//			frame.setIconImages(images);
//		}
//		catch (IOException e1)
//		{
//			e1.printStackTrace();
//		}
		List<Image> images = new ArrayList<>(1);
		try
		{
			images.add(ImageIO.read(PodgeMain.class.getResource("icon.png")));
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return images;
	}


	@Override
	protected JComponent getFocusComponent()
	{
		return mainPanel;
	}

	@Override
	protected void loadApplicationData()
	{
		Authenticator auth = new InteractiveAuthenticator(getFrame());
		Properties props = new Properties();
		props.put("mail.from", "simon@colston.org");
		props.put("mail.user", "simon@colston.org");
		props.put("mail.store.protocol", "imap");
		props.put("mail.imap.host", "mail.lcn.com");
		props.put("mail.imap.port", "993");
		props.put("mail.imap.ssl.enable", "true");
//		props.put("mail.imap.starttls.enable", "true");
//		props.put("mail.imap.starttls.required", "true");
		Session session = Session.getInstance(props, auth);
		session.setDebug(true);
		model = new PodgeModel();
		model.addAccount(new PodgeAccount(model, session));
	}

	@Override
	protected JComponent createMainPanel()
	{
		mainPanel = new JPanel(new BorderLayout());
		
		FolderTree ft = new FolderTree(model);
		MessageList ml = new MessageList(model);
		JLabel thread = new JLabel("Thread");
		JLabel text = new JLabel("Text");
		JSplitPane listSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, new JScrollPane(ml.getComponent()), thread);
		JSplitPane textSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, listSplit, text);
		JSplitPane p = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, new JScrollPane(ft.getComponent()), textSplit);
		mainPanel.add(p, BorderLayout.CENTER);
		return mainPanel;
	}

	protected JToolBar createToolBar()
	{
		return new JToolBar();
	}

	protected JMenuBar createMenuBar()
	{
		return new JMenuBar();
	}

	@Override
	protected Logger getLogger()
	{
		return logger;
	}
}
