/*
* Interface for data which will be output via audio
*/


import java.io.*;
import java.util.*;
import javax.sound.midi.*;

abstract class AudibleObject extends Observable implements Serializable
{
		
	protected int hits = 0;
	protected float maxHits = 0;
	protected float totalMaxHits = 0f;
	public static float baseTotalMaxHits = 100.0f;
	protected int instrument;
	public boolean hasPlayed;
	protected boolean isModified = false;
	
	private AudioEngine audioEngine;
	
	// custom values to be used by the AudioEngine
	public HashMap metaData = new HashMap();
	
	/*
	protected boolean muted;	
	protected int volume;
	*/
	
	AudibleObject()
	{
		audioEngine = Auralizer.getDefaultAudioEngine();
	}
	
	AudibleObject(AudioEngine ao)
	{
		setAudioEngine(ao);
	}
	
	public void setAudioEngine(AudioEngine ao)
	{
		instrument = 0;
		audioEngine = ao;		
	}
	
	public AudioEngine getAudioEngine()
	{
		return audioEngine;
	}
	
	public void play()
	{
		audioEngine.handleObject(this);		
	}
	
	public void mute()
	{
		audioEngine.muteObject(this);
	}
	
	
	public void setInstrument(int ins)
	{
		instrument = ins;
		refresh("setinstrument");
	}
	
	public int getInstrument()
	{
		return instrument;
	}
	
	public void setHasPlayed()
	{
		hasPlayed = true;
	}
	
	public void addHit()
	{
		hits++;
		if(hits > maxHits) maxHits = hits;		
		if(maxHits > totalMaxHits) totalMaxHits = maxHits;
		refresh("addhit");
	}
	
	public int getHits()
	{ 
		return hits;
	}
	
	public int getMaxHits()
	{
		return (int) maxHits;
	}
	
	public int getTotalHits()
	{
		return (int) totalMaxHits;
	}
	
	/* Clear the hits values. Most often called after the data has played 
		returns true if there were not any hits to flush
	*/
	public boolean flushHits()
	{
		boolean isEmpty = false;
		if(hits < 1) isEmpty = true;
		if (maxHits > 1) maxHits *= .90;
		if (maxHits < totalMaxHits) totalMaxHits *= .95;
		hits = 0;
		refresh("flush");
		return isEmpty;
	}
	
	protected void refresh(String message)
	{
		setChanged();
		notifyObservers(message);
	}
	
	abstract public boolean isModified();
}
