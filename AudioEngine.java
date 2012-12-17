/*
* The root interface for audio generating objects.
*/

import java.util.*;

interface AudioEngine 
{
	
	/*
	* Deals with an incoming AudibleObject
	*/
	void handleObject(AudibleObject ao);
	
	/*
	* Generate audio for the given AudibleObject
	*/
	void playObject(AudibleObject ao);
	
	/*
	* Silence any sound generated for given AudlibeObject
	*/
	void muteObject(AudibleObject ao);
	
	/*
	* Silence the entire engine
	*/
	void muteEngine();
	
	/*
	* Sets the max volume the engine can produce
	*/
	void setVolume(int v);
	
	/*
	* Returns the engines volume
	*/
	int getVolume();
	
	/*
	* Get a list of available instruments for the engine
	*/ 
	Vector showInstruments();

}
