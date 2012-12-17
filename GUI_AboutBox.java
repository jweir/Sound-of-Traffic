
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.*; 
import java.awt.event.*;

public class GUI_AboutBox extends GUI_Component
{
	private JFrame aboutFrame;

	GUI_AboutBox()
	{
		super();
		aboutFrame = new JFrame("About Sound Of Traffic");
		aboutFrame.setSize(new Dimension(300,100));
		aboutFrame.getContentPane().add(panel);
		
		
		JTextArea instructions = new JTextArea(""+
		"Sound of Traffic created by smokinggun.com\n\n"+
		"Version:"+Constants.VERSION+"\n\n"+
		"");
		instructions.setOpaque(false);
		instructions.setLineWrap(true);
		instructions.setWrapStyleWord(true);
		instructions.setFont(Style.smallFont);
		
		JButton link = new JButton("Visit Site");
		
		link.addActionListener(new ActionListener()
			{
			public void actionPerformed(ActionEvent e)
				{
				try
					{
					BrowserLauncher.openURL(Constants.SITE_URL);
					}
				catch(Exception ioe)
					{
					}
				}
			});
			
		addItem(instructions);
		addItem(link, true);

		aboutFrame.setResizable(false);
		aboutFrame.pack();
	}
	
	public void show()
	{
		aboutFrame.setVisible(true);
	}
}
