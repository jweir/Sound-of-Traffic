/**
* GUI for configuring the Auralizer
*/

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

abstract class GUI_Component implements Style
{

	protected JPanel panel;
	protected GridBagConstraints gbConstraint = new GridBagConstraints();
	protected GridBagConstraints c;
	protected Color background = new Color(255,255,255);
	protected Color foreground = new Color(0,0,0);
	
	GUI_Component()
	{
		panel = new JPanel();
	
		panel.setLayout(new GridBagLayout());

		c = new GridBagConstraints();
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridwidth = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5,5,5,5);
	}
	
	protected void addItem(JComponent e)
	{
		e.setBackground(background);
		e.setForeground(foreground);
		e.setFont(Style.smallFont);
		panel.add(e,c);
	}
	
	protected void addItem(JComponent e, Font f)
	{
		e.setBackground(background);
		e.setFont(f);
		panel.add(e,c);
	}
	
	protected void addItem(JComponent e, boolean newRow)
	{
		if(newRow) c.gridy++;
		addItem(e);
	}
	
	
	
	protected void addItem(JComponent e, boolean newRow, int w, int weighty)
	{
		c.weighty = weighty;
		c.gridwidth = w;
		addItem(e, newRow);
		c.gridwidth = 1;
		c.weighty = 1.0;
	}
	
	/*
	* Get the panel for the master GUI layout
	*/
	public JPanel getPanel()
	{
		panel.setBackground(new Color(255,255,255));
		return panel;
	}
}
