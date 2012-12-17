/** 
* Main menu 
*/

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.*; 
import java.awt.event.*;
import java.util.*;

public class GUI_MainMenu extends JMenuBar{

	private GraphicalDisplay gui;
	private GUI_AboutBox aboutBox;
	
	GUI_MainMenu(GraphicalDisplay gd)
	{
		super();
		gui = gd;
		
		ViewListener vl = new ViewListener();	
					
		JMenu menuView = new JMenu("Menu");
		add(menuView);
				
		menuView.add(addItem("Show Ports Table",vl));
		menuView.add(addItem("", vl));
		menuView.add(addItem("Save Ports Data",vl));
		menuView.add(addItem("Load Ports Data",vl));
		menuView.add(addItem("", vl));
		menuView.add(addItem("About Sound of Traffic",vl));
		
		aboutBox = new GUI_AboutBox();
		
	}
	
	protected void showAbout()
	{
		aboutBox.show();
	}
	
	protected JMenuItem addItem(String name, ActionListener ae)
	{
		JMenuItem mi = new JMenuItem(name);
		mi.setEnabled(true);
		mi.addActionListener(ae);
		return mi;
	}
	
	class ViewListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String command = e.getActionCommand();
			if(command == "Show Ports Table")
				gui.togglePortsWindow();
				
			if(command == "Save Ports Data")
				Controller.savePorts();
				
			if(command == "Load Ports Data")
				Controller.loadPorts();
				
			if(command == "About Sound of Traffic")
				showAbout();
		}
		
	}
	
}
