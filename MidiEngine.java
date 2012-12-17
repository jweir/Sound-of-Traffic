/*
* Generates the actual MidiMusic, is independent of time 
*/

import javax.sound.midi.*;
import javax.sound.midi.MidiDevice.*;
import java.io.*;
import java.util.*;

public class MidiEngine implements AudioEngine
{

	private Synthesizer synth;
	private MidiChannel[] midiChannels;
	private Instrument[] instruments;
	private int activeChannel = 0;
	private int volume = 0; // volume
	public static int velocityOn = 45;
	public static int velocityOff = 127;
	public static int  highestNote = 45;
	
	MidiEngine()
	{
		synth  = null;
		try
		{
			synth = MidiSystem.getSynthesizer();
			synth.open();
		}
		catch (MidiUnavailableException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		midiChannels = synth.getChannels();
		
		instruments = synth.getAvailableInstruments();
		
		System.out.println("Midi Sound Engine Started");
		System.out.println("Channels: "+midiChannels.length);
		System.out.println("Voices: "+synth.getMaxPolyphony());

	}
	
	/*
	* Display list of instruments in Sound Bank
	*/
	public Vector showInstruments()
	{
		Vector insOut = new Vector();
		
		for(int i=0; i < instruments.length; i++)
		{
			insOut.add(instruments[i].getName());
		}
		
		return insOut;
	}
	
	public static void setVelocityOn(int v)
	{
		velocityOn = v;
		System.out.println("Velocity On: "+velocityOn);
	}
	
	public static void setVelocityOff(int v)
	{
		velocityOff = v;
		System.out.println("Velocity Off: "+velocityOff);
	}
	
	public static void setHighestNote(int n)
	{
		highestNote = n;
		System.out.println("Highest Note: "+highestNote);
	}
	
	public void setVolume(int v)
	{
		try
		{
			volume = v;
			for(int i=0; i < midiChannels.length; i++)
			{
				midiChannels[i].controlChange(7, (int) (127*(((float)volume)/100)));
			}
			System.out.println("Volume set to: "+ volume);
		}
		catch(Exception e) 
		{
			System.out.println(e);
		}
	}
	
	public Instrument getInstrument(int i)
	{
		return instruments[i];
	}
	
	public int getVolume()
	{
		return volume;
	}
	
	public void muteEngine()
	{
		System.out.println("All Notes Off");
		for(int i=0; i < midiChannels.length; i++)
				{
				midiChannels[i].allNotesOff();
				}
	}
	
	/*
	* Set the next  free midi channel
	*/
	
	private int setActiveChannel()
	{	
		activeChannel++;
		//Channel 10 (9 when counting from 0) is reserved for percusion instruments
		if(activeChannel == 9) activeChannel = 10;
		if(activeChannel >= 16) activeChannel = 0;
		return activeChannel;
	}
	
	public int getActiveChannel()
	{	
		return activeChannel;
	}
	
	/*
	* Assigns an instrument to the active channel
	*/
	private void setInstrument(int i)
	{
		Patch pat = getInstrument(i).getPatch();
		midiChannels[activeChannel].programChange(pat.getProgram());
	}
	
	private void channelOff(MidiObject mo)
	{
		//midiChannels[mo.midiChannel].setPolyPressure(mo.note, velocityOff);
		midiChannels[mo.midiChannel].noteOff(mo.note, velocityOff);
	}
	
	private void channelOn(MidiObject mo)
	{
		midiChannels[mo.midiChannel].noteOn(mo.note, velocityOn);
	}
	
	
	public void handleObject(AudibleObject ao)
	{
		setActiveChannel();
		setInstrument(ao.getInstrument());
		
		preUpdateAudibleObject(ao);
		
		
		if(ao.getHits() > 0) playObject(ao);
		ao.flushHits();

	}
	
	public void playObject(AudibleObject ao)
	{
		if((Auralizer.playModifiedOnly && ao.isModified() == true) || Auralizer.playModifiedOnly == false)
			{
			MidiObject mo = new MidiObject(ao, this);
			channelOn(mo);
			updateAudibleObject(ao, mo);
			ao.setHasPlayed();
			}
	}
	
	//may be buggy and need to add some more functionality
	public void muteObject(AudibleObject ao)
	{
		setInstrument(ao.getInstrument());
		preUpdateAudibleObject(ao);
		ao.flushHits();
	}

	
	/*
	* Handels turning off and updating past midi channel events
	*/	
	private void preUpdateAudibleObject(AudibleObject ao)
	{
		if(ao.hasPlayed)
			try
			{
				MidiObject mo = (MidiObject) ao.metaData.get("midiObject");
				channelOff(mo);
				ao.hasPlayed = false;
			}
			catch(NullPointerException npe)
			{
			}
	}
	
	private void updateAudibleObject(AudibleObject ao, MidiObject mo)
	{
		ao.metaData.put("midiObject",mo);
	}
	
	
	


}
