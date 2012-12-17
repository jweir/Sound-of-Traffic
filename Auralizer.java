/**
* Handles tempo, overall volume and audio updates.  Makes connections between Tank and Insturments
* Need to add support for 0 (or other tolerance) value ports and addresses
* Will not generate the sound
*/

import javax.sound.midi.*;
import java.util.*;

public class Auralizer extends Thread implements Constants
{
	
	private static int tempo = 90;

	private static int tickInterval;
	private static int tickStep;
	private static int currentDirection;
	
	private boolean isRunning = true;
	
	public boolean resetPorts = false;
		
	private Tank sourceTank;
	
	public static boolean notesOff = true;
	
	private static boolean playPorts = true;
	private static boolean playAddress = true;
	
	private static AudioEngine defaultAudioEngine;
	
	
	public static boolean playModifiedOnly = false;
	
	private boolean silenceEngines = false;
	
	Auralizer()
	{
		sourceTank = Controller.getTank();
		tickStep = 0;
		
		//defaultAudioEngine = new JSynEngine();// MidiEngine();
		defaultAudioEngine = new MidiEngine();
	}
	
	public static AudioEngine getDefaultAudioEngine()
	{
		return defaultAudioEngine;
	}
	
	public void run()
	{
		System.out.println("Auralizer started");
		
		setTempo(tempo);
		setVolume(90);
			
		while(isRunning)
		{

			tick();
			try
			{
				sleep(tickInterval);
			}
			catch(InterruptedException iee) {}
		}
	}
	
	
	public static void setTempo(int t)
	{
		if(t == -1) System.out.println("Tempo is : "+tempo);
		else try
		{
			tempo = t;
			tickInterval = Math.round(60000/tempo);
			System.out.println("Tempo set to: "+tempo);
		}
		catch(Exception e) 
		{
			System.out.println("Hmm... correct value?");
		}
	}
	
	public static int getTempo()
	{
		return tempo;
	}
	
	public void setVolume(int v)
	{
		defaultAudioEngine.setVolume(v);
	}
	
	public float getVolume()
	{
		return defaultAudioEngine.getVolume();
	}
	
	/*
	* Clear all ports to essentialy reset the insturments
	*/
	private synchronized void resetPorts()
	{	
		silenceEngines();
		sourceTank.resetPorts();
		resetPorts = false;
		System.out.println("All Ports reset.  Instruments should sound different.");
		
	}
	
	public static int getTick()
	{
		return tickStep;
	}

	public static int getDirection()
	{
		return currentDirection;
	}
	
	/* 
	* if playModifiedOnly is true, only ports which have been modified by the user will be played
	*/	
	public static void setPlayModified(boolean state)
	{
		playModifiedOnly = state;
	}
	
	/*
	* The timed event for loading port data into the synth and playing a sound
	* source and destination sounds on different ticks
	* Note: diabled midiChannelIP.allNotesOff();
	*/
	private void tick()
	{	
		if(resetPorts) resetPorts();
		if(silenceEngines) silenceEngines();
		
		sourceTank.refresh();
		
		switch(tickStep)
		{
		case 0:	case 2:
			currentDirection = DIRECTION_SOURCE;			
			break;
		case 1: case 3:
			currentDirection = DIRECTION_DESTINATION;
			break;
		}
		
		playPortsTable();
		
		if(tickStep == 3) 
			{			
			tickStep = 0;			
			}
		else tickStep++;
	}
	
	public void forceSilence()
	{
		defaultAudioEngine.muteEngine();
	}
		
	public void setSilenceEnginesOn()
	{
		silenceEngines = true;
	}
			
	private synchronized void silenceEngines()
	{
		System.out.println("Turning off notes");
		if(playPorts)
		{
			for(Enumeration keys = sourceTank.getPorts().keys(); keys.hasMoreElements();)
			{
				try
				{
					Integer id = (Integer) keys.nextElement();
					Port port = (Port) sourceTank.getPorts().get(id);
					port.getDirection(DIRECTION_SOURCE).mute();
					port.getDirection(DIRECTION_DESTINATION).mute();
				}
				catch(NoSuchElementException nse) {}
			}
		}
		silenceEngines = false;
	}
	
	//Will need to alter this so it returns an AudioEngine object
	public AudioEngine getAudioEngine()
	{
		return defaultAudioEngine;
	}
		
	/**
	* Flushes data from Tank to port tables
	*/
	private synchronized void playPortsTable()
	{
		if(playPorts)
		{
			for(Enumeration keys = sourceTank.getPorts().keys(); keys.hasMoreElements();)
			{
				try
				{
					Integer id = (Integer) keys.nextElement();
					Port port = (Port) sourceTank.getPorts().get(id);
					port.getDirection(currentDirection).play();
				}
				catch(NoSuchElementException nse) {}
			}
		}
	}
	
	/*
	* Toggles whether the ports should be played or not
	*/
	
	public static boolean setPlayPorts(boolean p)
	{
		playPorts = p;
		return playPorts;
	}

	

	
}