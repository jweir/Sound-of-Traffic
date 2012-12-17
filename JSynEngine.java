import com.softsynth.jsyn.*;
import java.util.*;

public class JSynEngine implements AudioEngine
{
	
	private int	volume = 90; // volume
	
	private	SineOscillator		sineOsc;
	private LineOut				lineOut;
	
	
	JSynEngine()
	{
	
		Synth.startEngine(0);
		sineOsc = new SineOscillator()   ;
		lineOut = new LineOut();

		sineOsc.output.connect( 0, lineOut.input, 0 );
		sineOsc.output.connect( 0, lineOut.input, 1 );
		
		lineOut.start();
		sineOsc.start();
	
	}

	public void handleObject(AudibleObject ao)
	{
		//if(ao.getHits() > 0) 
		playObject(ao);
		//else muteObject(ao);
		ao.flushHits();

	}

	public void playObject(AudibleObject ao)
	{
		sineOsc.frequency.set(ao.getHits() * 100.0);
	}
	
	public void muteObject(AudibleObject ao)
	{
		sineOsc.stop();
	}
	
	public void muteEngine()
	{
	}
	
	public void setVolume(int v)
	{
		volume = v;
	}
	
	public int getVolume()
	{
		return volume;
	}
	
	public Vector showInstruments()
	{
		Vector v = new Vector();
		v.add("Pulse");
		v.add("Wave");
		return v;
	}
	
	public Instrument getInstrument(int t)
	{
		return new Instrument();
	}
	
	class Instrument
	{
		String getName()
		{
			return "undefined";
		}
	}
	
}
