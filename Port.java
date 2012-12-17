/*
*
*/

import java.util.*;
import javax.sound.midi.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

public class Port implements Constants, Serializable, Style
{ 	
	private Integer portId;	
	private String portName;
	private Direction source;
	private Direction destination;

	public int x;
	public int y;
	
	AudibleObjectEditor aoe;
	
	public boolean isModified = false; //set to true if the user has modified the instrument or port
	
	public static int NOTE_RANGE = 80; //value between 0-127 to modifiy how high a note can get
	
	public PortActivity imgActivity;
	public PortLabel imgLabel;
	
	public static int iconWidth = 80;
	public static int iconHeight = 80;
	
	public static int release = 32;
	
	Port(Integer portId)
	{
		this.portId = portId;
		source = new Direction((int) Math.round(365 * Math.random()));
		destination = new Direction((int) Math.round(365 * Math.random()));
		
		imgActivity = new PortActivity();
		imgLabel = new PortLabel();
		
		source.addObserver(imgLabel);
		destination.addObserver(imgLabel);
		
		source.addObserver(imgActivity);
		destination.addObserver(imgActivity);
	}
	
	Port(Integer portId, Vector ds)
	{
		this(portId);
		destination = (Direction) ds.get(DIRECTION_DESTINATION);
		source = (Direction)  ds.get(DIRECTION_SOURCE);		
	}
	
	public String getName()
	{
		return portId.toString();
	}
	
	public static void setIconSize(int h, int w)
	{
		iconHeight = h;
		iconWidth = w;
		Controller.updateIcons();
	}
	public void update(int direction)
	{
		if(direction == DIRECTION_SOURCE) source.addHit();
		if(direction == DIRECTION_DESTINATION) destination.addHit();
		//refresh();
	}
	
	/* Notify observers of an update */
	private void refresh()
	{	
		//setChanged();
		//notifyObservers(null);
	}
	
	public Integer getPortId()
	{
		return portId;
	}	
	
	public Direction getDirection(int direction)
	{
		if(direction == DIRECTION_SOURCE) return source;
		else if(direction == DIRECTION_DESTINATION) return destination;
		else return null;
	}
	
	
	public int getHits()
	{
		int i = (source.getHits() + destination.getHits());
		return i;
	}

	public void setPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public boolean mouseOver(Point p)
	{
		if((p.x >= x && p.y >= y) &&
		   (p.x <= x+Port.iconWidth && p.y <= y+Port.iconHeight)) return true;
		else return false;
	}
	
	public void clicked()
	{
		if(aoe == null) 
			{
			aoe = new MidiObjectEditor(this);
			//addObserver(moe);
			}
		else aoe.show();
	}
	
	public void remove()
	{
		if(aoe != null) 
			aoe.remove();
		
		source.mute();
		destination.mute();
		Controller.getTank().removePort(this);
	}
	
	/*
	* Image classes
	* need to move these so the port can be serialized
	* graphics can not be serialized (i guess)
	*/
	
	
	abstract class PortImage implements Serializable, Observer
	{
		public boolean needsUpdating = true;
		protected BufferedImage img = new BufferedImage(Port.iconHeight, Port.iconHeight, BufferedImage.TYPE_INT_ARGB);
		protected Graphics2D g = (Graphics2D) img.createGraphics();
		
		PortImage()
		{
			g.setBackground(Style.colorTrans);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			resize();
		}
		
		public BufferedImage getImage()
		{
			if(needsUpdating) draw();
			return img;
		}
		public void needsUpdating()
		{
			resize();
			needsUpdating = true;
		}
		
		abstract public void update(Observable o, Object s);
		
		abstract protected void draw();
		
		protected void resize()
		{
			img = new BufferedImage(Port.iconHeight, Port.iconHeight, BufferedImage.TYPE_INT_ARGB);
			g = (Graphics2D) img.createGraphics();
			g.setBackground(Style.colorTrans);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
		}
		
	}
	
	/**
	* Handles drawing of port activity
	*/
	
	class PortActivity extends PortImage 
	{
		private int barWidth = 15;
		
		protected void draw()
		{	

			g.setBackground(colorBG);
			
			if(getHits() > 0)
			{
				g.setBackground(Style.colorBGActive);
			}
			
			if(Auralizer.playModifiedOnly && isModified == false)
			{
				g.setBackground(Style.colorBGInactive);
			}
			g.clearRect(0,0,Port.iconHeight, Port.iconHeight);
			
			
			g.setColor(Style.colorBGMax);
			
			float avg = (float) (source.getTotalHits()+destination.getTotalHits())/2;
			if(avg < AudibleObject.baseTotalMaxHits)
			{
				avg = avg/AudibleObject.baseTotalMaxHits;
				g.fillRect(0,Port.iconHeight-(int)(Port.iconHeight * avg),(int) Port.iconWidth ,(int)(Port.iconHeight * avg));
			}
			int x = (Port.iconHeight/2);
			int y = (Port.iconHeight) - 40;
			
			g.setColor(Style.colorBarTotal);

			g.fillRect(x-(barWidth+1),y-(source.getTotalHits()),barWidth,source.getTotalHits());
			g.fillRect(x+1,y-(destination.getTotalHits()),barWidth,destination.getTotalHits());
			
			g.setColor(Style.colorBarActive);
			g.fillRect(x-(barWidth+1),y-(source.getHits()),barWidth,source.getHits());
			g.fillRect(x+1,y-(destination.getHits()),barWidth,destination.getHits());		

			needsUpdating = false;

		}
		
		public void update(Observable o, Object s)
		{
			if(s == "flushhits") needsUpdating();
		}

	
	}
	
	/**
	* Handles drawing of port label
	*/
	
	class PortLabel extends PortImage
	{
	
		private FontMetrics fm;
		private java.awt.geom.Rectangle2D rect;
		
		protected void resize()
		{
			super.resize();
			g.setFont(Style.fontFixedSmall);
			fm  = g.getFontMetrics(Style.fontFixedSmall);
		}
		
		protected void draw()
		{
			g.clearRect(0,0,Port.iconHeight, Port.iconHeight);			
			if(isModified)  g.setColor(Style.colorFontUserModified);
			else g.setColor(Style.colorFont);
			try
			{
			String portId = getPortId().toString();
			rect = fm.getStringBounds(portId, g);
			g.drawString(portId, (int)(Port.iconHeight/2)-((int) rect.getWidth())/2, Port.iconHeight-((int) rect.getHeight()*3));
			
			String portIns = (String) Controller.getInstrument(destination.getInstrument()).getName();
			rect = fm.getStringBounds(portIns, g);
			g.drawString(portIns, (int)(Port.iconHeight/2)-((int) rect.getWidth())/2, Port.iconHeight-((int) rect.getHeight()*2));
			
			portIns = (String) Controller.getInstrument(source.getInstrument()).getName();
			rect = fm.getStringBounds(portIns, g);
			g.drawString(portIns, (int)(Port.iconHeight/2)-((int) rect.getWidth())/2, Port.iconHeight-((int) rect.getHeight()));
			} catch(Exception e) {}
			needsUpdating = false;
		}
		
		public void update(Observable o, Object s)
		{
			if(s == "setinstrument") needsUpdating();
		}
	}
	
}
