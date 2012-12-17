/**
* GUI for configuring the Auralizer
* Controls:
	tempo
	play addressess
	play ports
	set temp
	.volume
*/

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GUI_Auralizer extends GUI_Component implements ActionListener
{

	private JCheckBox portsBox;
	private JCheckBox modifiedBox;

	private JTextField allowPortHigh;
	private JTextField allowPortLow;

	GUI_Auralizer()
	{
		super();
				
		portsBox = new JCheckBox("Play Ports");
		portsBox.setSelected(true);

		modifiedBox = new JCheckBox("Play Modfied Only");		
		modifiedBox.setSelected(false);
		
		
		JButton portsOffButton = new JButton("Force Off");
		portsOffButton.addActionListener(new ForceNotesOffListener());
				
		modifiedBox.addActionListener(this);
		portsBox.addActionListener(this);
		
	
		addItem(modifiedBox);
		addItem(portsBox);
		addItem(portsOffButton);
		
		addItem(new JLabel("Tempo"), true);
		addItem(new TempoSlider(), false, 2, GridBagConstraints.REMAINDER);
		
		addItem(new JLabel("Volume"), true);
		addItem(new VolumeSlider(), false, 2, GridBagConstraints.REMAINDER);
		
		addItem(new JLabel("Highest Note"), true);
		addItem(new NoteSlider(), false, 2, GridBagConstraints.REMAINDER);

		addItem(new JLabel("Instrument Velocity On"), true);
		addItem(new VelocityOnSlider(), false, 2, GridBagConstraints.REMAINDER);
		
		addItem(new JLabel("Instrument Velocity Off"), true);
		addItem(new VelocityOffSlider(), false, 2, GridBagConstraints.REMAINDER);
		
		addItem(new JLabel("Icon Size"), true);
		addItem(new IconSlider(), false, 2, GridBagConstraints.REMAINDER);
		
		addItem(new JLabel("Base Max Hits"), true);
		addItem(new BaseHitsSlider(), false, 2, GridBagConstraints.REMAINDER);
		
		
		/*
		* Setup the TCP Port Filter
		*/
		
		allowPortHigh = new JTextField(Integer.toString(TCPFilter.getHighPort()),6);		
		allowPortLow = new JTextField(Integer.toString(TCPFilter.getLowPort()),6);
		
		JButton apply = new JButton("Reset Ports");
		apply.addActionListener(new PortFilterListener());
		
		
		addItem(new JLabel("Lowest Port"), true);
		addItem(new JLabel("Highest Port"));
		
			
		addItem(allowPortLow, true);
		addItem(allowPortHigh);
		addItem(apply);
		
		
		
	}
	
	
	public void actionPerformed(ActionEvent e)
	{
		try
			{
			Auralizer.setPlayPorts(portsBox.isSelected());
			Auralizer.setPlayModified(modifiedBox.isSelected());
			
			Controller.updateIcons();
			
			} catch(NullPointerException npe)
			{
				//tcpDump = new TCPDump();			
				//startTcpDump();
				//tcpDump.start();
			}
	}
	
	class ForceNotesOffListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("Force Notes Off");
			try
				{
				Controller.getAuralizer().forceSilence();
				} catch(NullPointerException npe)
				{
					System.out.println("Forces notes Failes \n"+npe);

				}
		}
	}
	
	class PortFilterListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("Reset");
			try
				{
				int hi = new Integer(allowPortHigh.getText()).intValue();
				int low = new Integer(allowPortLow.getText()).intValue();
				
				Controller.getAuralizer().resetPorts = true;
		
				
				TCPFilter.setPortHighLowFilter(hi,low);
				
				} catch(NullPointerException npe)
				{
					System.out.println("Problemo");
				}
		}
	}


	
	class TempoSlider extends JSlider implements ChangeListener
	{
		TempoSlider()
		{
			super(JSlider.HORIZONTAL, 0, 1000, Auralizer.getTempo());
			addChangeListener(this);
			setMajorTickSpacing(200);
			setMinorTickSpacing(20);
			setPaintTicks(true);
			setPaintLabels(true);
			
			Dictionary d = getLabelTable();
			JLabel t;
		 
			for (Enumeration en = d.elements(); en.hasMoreElements();)
			{
				t = (JLabel) en.nextElement();
				t.setFont(new Font(null, Font.PLAIN, 7));
			}
			
		}
		
		public void stateChanged(ChangeEvent e)
		{
			JSlider source = (JSlider) e.getSource();
			
			if(!source.getValueIsAdjusting())
			{
				int tempo = (int) source.getValue();
				Controller.setTempo(tempo);
			}
		
		
		}
		
	}
	
	class NoteSlider extends JSlider implements ChangeListener
	{
		NoteSlider()
		{
			super(JSlider.HORIZONTAL, 0, 127, Port.NOTE_RANGE);
			addChangeListener(this);

		}
		
		public void stateChanged(ChangeEvent e)
		{
			JSlider source = (JSlider) e.getSource();
			
			if(!source.getValueIsAdjusting())
			{
				int note = (int) source.getValue();
				MidiEngine.setHighestNote(note);
			}
		
		
		}
		
	}
	
	class VelocityOnSlider extends JSlider implements ChangeListener
	{
		VelocityOnSlider()
		{
			super(JSlider.HORIZONTAL, 1, 127, MidiEngine.velocityOn);
			addChangeListener(this);

		}
		
		public void stateChanged(ChangeEvent e)
		{
			JSlider source = (JSlider) e.getSource();
			
			if(!source.getValueIsAdjusting())
			{
				int v = (int) source.getValue();
				MidiEngine.setVelocityOn(v);
			}
		
		
		}
		
	}
	
	class VelocityOffSlider extends JSlider implements ChangeListener
	{
		VelocityOffSlider()
		{
			super(JSlider.HORIZONTAL, 0, 127, MidiEngine.velocityOff);
			addChangeListener(this);
		}
		
		public void stateChanged(ChangeEvent e)
		{
			JSlider source = (JSlider) e.getSource();
			
			if(!source.getValueIsAdjusting())
			{
				int v = (int) source.getValue();
				MidiEngine.setVelocityOff(v);
			}
		}		
	}
	
	class VolumeSlider extends JSlider implements ChangeListener
	{
		VolumeSlider()
		{
			super(JSlider.HORIZONTAL, 0, 100, (int) Controller.getVolume());
			addChangeListener(this);
			
			setMajorTickSpacing(20);
			setMinorTickSpacing(20);
			setPaintTicks(true);
			setPaintLabels(true);
			
			Dictionary d = getLabelTable();
			JLabel t;
		 
			for (Enumeration en = d.elements(); en.hasMoreElements();)
			{
				t = (JLabel) en.nextElement();
				t.setFont(new Font(null, Font.PLAIN, 7));
			}

		}
		
		public void stateChanged(ChangeEvent e)
		{
			JSlider source = (JSlider) e.getSource();
			
			if(!source.getValueIsAdjusting())
			{
				int volume = (int) source.getValue();
				Controller.setVolume(volume);
			}
		}
	}
	
	class IconSlider extends JSlider implements ChangeListener
	{
		IconSlider()
		{
			super(JSlider.HORIZONTAL, 40, 120, Port.iconHeight);
			addChangeListener(this);
			setMajorTickSpacing(10);
			setMinorTickSpacing(5);
			setPaintTicks(true);
			setPaintLabels(true);
			
			Dictionary d = getLabelTable();
			JLabel t;
		 
			for (Enumeration en = d.elements(); en.hasMoreElements();)
			{
				t = (JLabel) en.nextElement();
				t.setFont(new Font(null, Font.PLAIN, 7));
			}
			
		}
		
		public void stateChanged(ChangeEvent e)
		{
			JSlider source = (JSlider) e.getSource();
			
			if(!source.getValueIsAdjusting())
			{
				int s = (int) source.getValue();
				Port.setIconSize(s,s);//iconHeight = s;
				Controller.updateIcons();
			}
		}
		
	}
	
	class BaseHitsSlider extends JSlider implements ChangeListener
	{
		BaseHitsSlider()
		{
			super(JSlider.HORIZONTAL, 0, 200, (int) AudibleObject.baseTotalMaxHits);
			addChangeListener(this);
			
			setMajorTickSpacing(20);
			setMinorTickSpacing(20);
			setPaintTicks(true);
			setPaintLabels(true);
			
			Dictionary d = getLabelTable();
			JLabel t;
		 
			for (Enumeration en = d.elements(); en.hasMoreElements();)
			{
				t = (JLabel) en.nextElement();
				t.setFont(new Font(null, Font.PLAIN, 7));
			}

		}
		
		public void stateChanged(ChangeEvent e)
		{
			JSlider source = (JSlider) e.getSource();
			
			if(!source.getValueIsAdjusting())
			{
				AudibleObject.baseTotalMaxHits = (float) source.getValue();
			}
		}
	}

}
