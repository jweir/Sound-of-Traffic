/**
* GUI For setting parameters and sudo password of TCPDump
*/

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class GUI_TCPDump extends GUI_Component implements ActionListener{
	
	private JComboBox ethDevices;
	private JButton submitButton;
	private JPasswordField password;
	
	private TCPDump tcpDump;
	
	private JFrame frame;
	
	private GraphicalDisplay gui;
	
	GUI_TCPDump(GraphicalDisplay gui)
	{
		this.gui = gui;

		System.out.println("Create Password Window");
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame = new JFrame("Sound of Traffic Start");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		frame.getContentPane().add(panel);
		
		panel.setPreferredSize(new Dimension(280,250));
		panel.setBackground(new Color(255,255,255));
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0;
		c.weightx = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(5,5,5,5);
		
		c.gridwidth = 2;
		JTextArea instructions = new JTextArea("Select your network interface, usually the default choice is correct.\n\n"+
		"Then enter your root password and press start.\n\n"+
		"If the application quits, the password was incorrect.  If no sound is heard after starting and using your network, try a different a network interface."+
		"");
		instructions.setOpaque(false);
		instructions.setLineWrap(true);
		instructions.setWrapStyleWord(true);
		instructions.setFont(smallFont);
		instructions.setPreferredSize(new Dimension(280,100));
		panel.add(instructions,c);

		c.gridwidth = 1;
		//Ethernet Device box
		c.gridy = 1;
		ethDevices = new JComboBox(TCPDump.getNetworkInterfaces());
		ethDevices.setOpaque(false);
		panel.add(new JLabel("Network Interface"),c);
		panel.add(new JLabel("Password"),c);

		
		c.gridy=2;
		//Password box
		password = new JPasswordField(15);
		password.setOpaque(false);
		password.setPreferredSize(new Dimension(100,20));
		panel.add(ethDevices,c);
		panel.add(password,c);
		
		c.gridy=3;
		c.gridwidth = 2;
		//Start button		
		submitButton = new JButton("Start");
		submitButton.setOpaque(false);
		submitButton.addActionListener(this);
		panel.add(submitButton,c);
			
		
		frame.pack();
		frame.setVisible(true);
		
		tcpDump = new TCPDump();

	}
	
	
	public void actionPerformed(ActionEvent e)
	{	
	try
		{
		if(tcpDump.isAlive())
			{
			tcpDump.end();
			//tcpDump.reset((String) password.getText(), (String) ethDevices.getSelectedItem());
			//tcpDump.end();
			submitButton.setText("Start");
			}
		else	
			{
			//tcpDump.reset((String) password.getText(), (String) ethDevices.getSelectedItem());
			startTcpDump();
			tcpDump.start();
			submitButton.setText("Stop");
			frame.hide();
			gui.showWindow();
			}
		
		} catch(NullPointerException npe)
		{
			//tcpDump = new TCPDump();			
			//startTcpDump();
			//tcpDump.start();
		}
	}
	
	private void startTcpDump()
	{		
		tcpDump.reset((String) password.getText(), (String) ethDevices.getSelectedItem());
		submitButton.setText("Stop");		
	}
	
}
