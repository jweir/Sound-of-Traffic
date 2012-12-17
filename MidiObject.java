import java.io.*;

public class MidiObject implements Serializable 
{
	int note;
	int velocityOn;
	int velocityOff;
	int midiChannel;
	
	MidiObject(AudibleObject ao, MidiEngine me)
	{
		note = (int) (MidiEngine.highestNote * ((float)ao.getHits()/(float)ao.getTotalHits()));
		midiChannel = me.getActiveChannel();
	}
}
