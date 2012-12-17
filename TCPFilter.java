/**
* Filters tcpdump stream and outputs the filtered data to the correct Tank

   _____SOURCE______   ____DESTINATION___
IP 206.65.183.18.80 > 192.168.1.120.63887: FP 1:572(571) ack 618 win 63623 <nop,nop,timestamp 675491 629212298>

Pings do not return anything since there is no port in the address.


*/

import java.io.*;
import java.util.regex.*;

public final class TCPFilter implements Constants
{
	/* Current line of tcpdump data to be filtered */
	private static String currentLine;
	
	/* Pattern for IP address and numeric port */
	public static Pattern patIPAddress = Pattern.compile("((25[0-5]|2[0-4][0-9]|1?[0-9]?[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1?[0-9]?[0-9])(\\.[0-9]+)");
	
	private static int allowPortLow = 0;
	private static int allowPortHigh = 10000;
	
	private static Tank destinationTank;
	
	public static void setDestinationTank(Tank t)
	{
		destinationTank = t;
	}
	
	/**
	* Parse one line of tcpdump data
	* NOTE: Maybe rather than have seperate source/destination tables have 
		one table for the ports and the port object will have seperate fields to deal
		with destination 
	*/
	synchronized public static void parseLineFromStream(String line)
	{
	
			destinationTank.addRawTCPDumpString(line);
			
			//create a regex matcher based on the IP Address regex
			Matcher m = patIPAddress.matcher(line);
			
			try
			{
				if(m.find()) 
				{	 
					//Source Port
					destinationTank.handleHeader(parseAddress(m.group(), DIRECTION_SOURCE));
					 
					//Destination Port
					destinationTank.handleHeader(parseAddress(m.group(), DIRECTION_DESTINATION));
				}
			}
			catch(NullPointerException npe)
			{
				System.err.println("Error Probably a bad password. Now quitting.");
				System.exit(0);
			}
	}
	
	/* Parse the source or destination from an tcp packet */
	public static PartialTrafficHeader parseAddress(String rawTrafficData, int direction)
	{		
		try
			{
			PartialTrafficHeader header = new PartialTrafficHeader(rawTrafficData, direction);
			
			if(header.getPortId().intValue() >= allowPortLow && header.getPortId().intValue() <= allowPortHigh) 			
				{
				return header;
				}
			}
		catch(PatternSyntaxException pse)
			{
			System.err.println(pse);
			}
		return null;
	}
	
	/* Set the low high values to adjust port filtering */
	public static void setPortHighLowFilter(int hi, int low)
	{
		if(hi >= low && low >= 0)
		{
			destinationTank.resetPorts();
			allowPortHigh = hi;
			allowPortLow = low;
			System.out.println(hi+" "+low);
		}
	}
	
	public static int getHighPort()
	{
		return allowPortHigh;
	}
	
	public static int getLowPort()
	{
		return allowPortLow;
	}
	
}
