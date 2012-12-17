/**
* Sets up the gui
*/

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;  
import java.util.*;

public class GraphicalDisplay extends GUI_Component implements Observer {
	
	
	private Tank sourceTank;
	private JFrame frame;
	private GUI_Ports portsTable;
	
	
	GraphicalDisplay(Tank t)
	{
		super();
		sourceTank = t;
		sourceTank.addObserver(this);
		
		GUI_TCPDump gtcp = new GUI_TCPDump(this);
		createWindow();
		
		portsTable = new GUI_Ports();
	}
	
	public void togglePortsWindow()
	{
		portsTable.show();
	}

	public void update(Observable o, Object a)
	{
		sourceTank.clearRawTCPDumpString();
		portsTable.update();
	}
	
	public void createWindow()
	{
		c.insets = new Insets(0,10,0,10);
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame = new JFrame("Sound of Traffic");
		
		Window w = new Window(frame);
		
		frame.setResizable(false);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().add(panel);
		panel.setBackground(new Color(255,255,255));
		
		panel.setPreferredSize(new Dimension(350,480));
		
				
		addItem(new GUI_Auralizer().getPanel());
		
		frame.setJMenuBar(new GUI_MainMenu(this));
		
		frame.pack();
		
	}
	
	public void showWindow()
	{
		portsTable.showWindow();
		frame.setVisible(true);
	}
	
	
}
