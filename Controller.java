/*
* 
* Central controller for routing object to object communication
*
*/

import java.io.*;
import java.util.*;
import javax.sound.midi.*;
import javax.sound.midi.MidiDevice.*;

public final class Controller implements Constants
{

	static Auralizer auralizer;
	static Tank tank;
	static GUI_Ports guiPorts;
	
	public static void setAuralizer(Auralizer au)
	{
		auralizer = au;
	}
	
	public synchronized static void loadPorts()
	{
		tank.resetPorts();
		auralizer.setSilenceEnginesOn();
		try
		{
			FileInputStream in = new FileInputStream("saved.sot");
			ObjectInputStream s = new ObjectInputStream(in);
			Hashtable loadedPorts = (Hashtable) s.readObject();
			Hashtable ports = new Hashtable();
			
			for(Enumeration keys = loadedPorts.keys(); keys.hasMoreElements();)
			{	
				try	
				{
					Integer id = (Integer) keys.nextElement();
					Port p = new Port(id, (Vector) loadedPorts.get(id));
					ports.put(id, p);
				}
				catch(NoSuchElementException nse) 
				{
					System.err.println(nse);
				}
			}
			tank.setPorts(ports);
		}	
		catch(Exception e)
		{
			System.out.println(e);
		}

	}
	
	public synchronized static void savePorts()
	{
		Hashtable ports = new Hashtable();//tank.getPorts().clone();
		
		//only save ports which have been user modified
		for(Enumeration keys = tank.getPorts().keys(); keys.hasMoreElements();)
		{	
			try	
				{
				Integer id = (Integer) keys.nextElement();
				Port port = (Port) tank.getPorts().get(id);
				
				Vector aoCollection = new Vector();
				
				System.out.println("yess "+port.getDirection(DIRECTION_DESTINATION).getHits());
				
				aoCollection.add(port.getDirection(DIRECTION_DESTINATION));
				aoCollection.add(port.getDirection(DIRECTION_SOURCE));
				
				ports.put(id, aoCollection);
				}
			catch(NoSuchElementException nse) 
			{
				System.err.println(nse);
			}
		}
		
		try
		{
			FileOutputStream out = new FileOutputStream("saved.sot");
			ObjectOutputStream s = new ObjectOutputStream(out);
			s.writeObject(ports);
			s.flush();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	
	}
	
				
	public static void setVolume(int v)
	{
		auralizer.setVolume(v);
	}
	
	public static float getVolume()
	{
		return 100;//auralizer.getVolume();		
	}
	
	public static Instrument getInstrument(int i)
	{
		return ((MidiEngine) auralizer.getAudioEngine()).getInstrument(i);	
	}
	
	public static void setTempo(int t)
	{
		auralizer.setTempo(t);
	}
	
	public static void setTank(Tank t)
	{
		tank = t;
	}
	
	public static Tank getTank()
	{
		return tank;
	}
	
	public static Auralizer getAuralizer()
	{
		return auralizer;
	}

	
	public synchronized static void updateIcons()
	{
		for(Enumeration keys = tank.getPorts().keys(); keys.hasMoreElements();)
			{
				try
				{
					Integer id = (Integer) keys.nextElement();
					Port port = (Port)tank.getPorts().get(id);
					port.imgActivity.needsUpdating();
					port.imgLabel.needsUpdating();
				}
				catch(NoSuchElementException nse) {}
			}
		guiPorts.update();
	}
	
	public static void setGUI_Ports(GUI_Ports gp)
	{
		guiPorts = gp;
	}
}
