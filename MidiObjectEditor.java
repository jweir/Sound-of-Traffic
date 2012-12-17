/*
* User Interface for editting midi object data

Assign instrument
Set mute
Set volume
Set max note
Set veclocity

Delete Port
Set AudioEngine

*/

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;  
import java.util.*;

public class MidiObjectEditor extends AudibleObjectEditor
{
	private static Vector instruments;
	private JLabel test = new JLabel();
	
	MidiObjectEditor(Port p)
	{
		super(p);
		
		frame = new JFrame("Edit: "+p.getName());
		frame.setResizable(false);
		frame.getContentPane().add(panel);
		panel.setBackground(new Color(255,255,255));
		panel.setMinimumSize(new Dimension(250,140));
		
		
		addItem(new JLabel("Port:"+p.getName()), Style.largeFont);
		
		JButton removeButton = new JButton("Remove Port");
		removeButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					port.remove();
				}
			}
		);
		
		addItem(removeButton);
		
		addItem(new Layout(port.getDirection(Constants.DIRECTION_DESTINATION), "Desination").getPanel(), true);
		addItem(new Layout(port.getDirection(Constants.DIRECTION_SOURCE), "Source").getPanel());
		
		frame.pack();
		frame.setVisible(true);
		
		// Let the AudblieObjects know they have been touched
		port.getDirection(Constants.DIRECTION_DESTINATION).isModified = true;
		port.getDirection(Constants.DIRECTION_SOURCE).isModified = true;
	}
	
	
	public static void loadInstruments()
	{
		instruments = ((MidiEngine) SoundOfTraffic.auralizer.getAudioEngine()).showInstruments();		
	}
	
	
	
	public void update(Observable o, Object s)
	{
	}

	
	class Layout extends GUI_Component implements Observer
	{
		JLabel hits = new JLabel();
		JLabel maxHits = new JLabel();
		JLabel totalHits = new JLabel();
		
		Layout(AudibleObject ao, String direction)
		{
			c.gridwidth = 3;
			c.gridy = 1;
			
			addItem(new JLabel(direction), bodyFontBold);
			addItem(new InstrumentBox(ao).instrumentsBox, true);
			
			c.gridwidth = 1;
			c.gridy++;
			
			addItem(new JLabel("Hits"), bodyFontBold);
			addItem(new JLabel("Max Hits"), bodyFontBold);
			addItem(new JLabel("Base Hits"), bodyFontBold);
			
			addItem(hits, true);
			addItem(maxHits);
			addItem(totalHits);
			
			
			ao.addObserver(this);
		}
		
		public void update(Observable o, Object s)
		{
			AudibleObject ao = (AudibleObject) o;
			hits.setText(Integer.toString(ao.getHits()));
			maxHits.setText(Integer.toString(ao.getMaxHits()));
			totalHits.setText(Integer.toString(ao.getTotalHits()));
		}
	
	}
	
	class InstrumentBox implements ActionListener
	{
	
		public JComboBox instrumentsBox;
		private AudibleObject ao;
		
		InstrumentBox(AudibleObject ao)
		{
			this.ao = ao;
			instrumentsBox = new JComboBox(instruments);
			instrumentsBox.setFont(Style.smallFont);
			instrumentsBox.addActionListener(this);
			instrumentsBox.setMaximumRowCount(20);
			instrumentsBox.setSelectedIndex(ao.getInstrument());
		}
		
		public void actionPerformed(ActionEvent e)
		{
			ao.setInstrument(instrumentsBox.getSelectedIndex());
		}
	
	}	
}
