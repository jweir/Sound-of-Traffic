/**
* Container for filling with filtered tcpdump data
* 
*/

import java.util.*;

public class Tank extends Observable {	

	
	private Hashtable ports = new Hashtable();
	
	private String rawTCPDump;
	
	public void addRawTCPDumpString(String line)
	{
		rawTCPDump+=line+"\n";
	}
	
	public void clearRawTCPDumpString()
	{
		rawTCPDump = "";
	}
	
	public String getRawTCPDumpString()
	{
		return rawTCPDump;		
	}
	
	public void handleHeader(PartialTrafficHeader header)
	{
		if(header != null)
		{
			handlePort(header);
		}
	}
	
	/*
	* Updates an existing port's data or creates a new port object if one does not exist
	*/
	private void handlePort(PartialTrafficHeader header)
	{
		Port port;
		Integer portId;
		portId =  header.getPortId();
		
		//if a port already exists
		try
		{
			port = (Port) ports.get(portId);
			port.update(header.getDirection());

		}
		//if the port does not exists
		catch(NullPointerException npe)
		{	
			port = new Port(portId);
			port.update(header.getDirection());
		}
							
		//add the port back into the table
		ports.put(portId, port);
	}
	
	/**
	* Sorts the ports by port id and returns a sorted vector of port keys (ids)
	*/
	
	synchronized public Vector sortPorts()
	{
		
		Vector v = new Vector(ports.keySet());
		Collections.sort(v, new Comparator()
			{
				public int compare(Object o0, Object o1)
				{
					Integer i0 = (Integer) o0;
					Integer i1 = (Integer) o1;
					
					if(i0.intValue() < i1.intValue()) return -1;
					if(i0.intValue() == i1.intValue()) return 0;
					else return 1;
				
				}

			});
		return v;
	}
	
	synchronized public Hashtable getPorts()
	{
		return ports;
	}
	
	synchronized public void setPorts(Hashtable p)
	{
		ports.clear();
		ports = new Hashtable();
		ports = p;
	}
	
	
	/* Notify observers of an update */
	public void refresh()
	{	
		setChanged();
		notifyObservers(null);
	}
	
	/*
	* Remove an individual port
	* not working
	*/
	synchronized public void removePort(Port p)
	{
		Hashtable t = new Hashtable();
		ports.remove(p.getPortId());
	}
	
	/*
	* Clear all ports to essentialy reset the insturments
	*/
	public void resetPorts()
	{		
	
		for(Enumeration keys = ports.keys(); keys.hasMoreElements();)
			{
				try
				{
					Integer id = (Integer) keys.nextElement();
					Port port = (Port)ports.get(id);
					port.remove();
				}
				catch(NoSuchElementException nse) {}
			}
			
		ports.clear();
	}
	
}
