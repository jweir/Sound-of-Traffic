/**
* Application which filters tcp/ip traffic data into meaningful audio feedback
* 
* @author John Weir
* see N.A.G, wisl.info 

* Todo:
	modifiy settings for individual ports
		volume
		highest note
		instrument
	switch to using a gridbag layout manager for the gui
	simplify the code
	sort ports by order
	better instrument assignment
	save settings
	improve performance

	consider using http://www.ethereal.com/
* 
*/
import java.util.*;

public class SoundOfTraffic {
	
	public static Auralizer auralizer;
	public static Tank tank;
	
    public static void main (String args[]) {
		
		//Create the tank
		tank = new Tank();
		TCPFilter.setDestinationTank(tank);
		
		Controller.setTank(tank);
		
		//GraphicalDisplay gd = new GraphicalDisplay();
		GraphicalDisplay gui = new GraphicalDisplay(tank);	
					
		/* Create the Auralizer */
		auralizer = new Auralizer();					
		
		Controller.setAuralizer(auralizer);
				
		/* Start the Auralizer */
		auralizer.start();
		MidiObjectEditor.loadInstruments();
				
    }
}
