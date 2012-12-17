
/*
* Fields and methods for handeling the source or desitination data for the port
*/
class Direction extends AudibleObject
{
	
	Direction(int ins)
	{
	super();
	totalMaxHits = baseTotalMaxHits;
	instrument = ins;
	}
	
	public boolean flushHits()
	{
		boolean isEmpty = super.flushHits();
		if(isEmpty == false) 
			{
			refresh("flushhits");
			}
		return isEmpty;
	}
	
	public boolean isModified()
	{
		return isModified;
	}
	
	public void setInstrument(int i)
	{
		super.setInstrument(i);			
	}
	
}
	