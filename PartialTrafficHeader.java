/*
* Object represention either the destination or source traffic header (address and port)
*/

public class PartialTrafficHeader implements Constants
{
	
	private Integer portId;
	private int address[];
	private String fullAddress;
	private int direction;
	
	PartialTrafficHeader(String rawTrafficData, int direction)
	{
		this.direction = direction;
		
		address = new int[4];
		fullAddress = new String();
		
		String split[] = rawTrafficData.split("\\.");
		for(int i = 0; i < 4; i++)
			{
			address[i] = (int) new Integer(split[i]).intValue();
			}
			
		fullAddress = address[0]+"."+address[1]+"."+address[2]+"."+address[3];
		portId = new Integer(split[4]);
	}
	
	public Integer getPortId()
	{
		return portId;
	}
	
	public int getDirection()
	{
		return direction;
	}
	
	public String getFullAddress()
	{
		return fullAddress;
	}
	
	public int[] getAddress()
	{		
		return address;
	}
}
