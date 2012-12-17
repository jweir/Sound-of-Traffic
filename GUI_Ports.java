/**
* GUI for assign and updating the Ports and drawing them
*
* Todo: implement DoubleBuffering ??
*/

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.math.*;

public class GUI_Ports extends GUI_Component
{

	private JFrame portFrame;
	private PortPanel portPanel;
	private int COLUMNS;
	protected int yMouseOffset = 0;
	protected JScrollPane sp;
	 
	GUI_Ports()
	{
		panel = new JPanel();
		
		portFrame = new JFrame("Port Graphics");
				
		portPanel = new PortPanel();
		
		sp = new JScrollPane(portPanel);
		
		sp.setBorder(new EmptyBorder(0,0,0,0));
		
		portFrame.getContentPane().add(sp);
		
		
		portFrame.setLocation(500,0);
		portFrame.pack();
		portFrame.setSize(new Dimension(400,400));
		
		Controller.setGUI_Ports(this);
	}
	
	public void showWindow()
	{	
		portFrame.setVisible(true);
	}
	
	public void update()
	{
		portPanel.refresh();
	}
	
	public void show()
	{
		portFrame.setVisible(true);
	}
	
	
	class PortPanel extends JLabel implements ImageObserver, Style
	{
		private Tank tank;
		private BufferedImage img0;
		private BufferedImage img1;
		
		PortPanel()
		{
			super();
			tank = Controller.getTank();
			addMouseListener(new MListener(this));
			//addMouseMotionListener(new MMotionListener(this));
			
		}
		
		public void refresh()
		{
			repaint();
		}
		
		public synchronized void paintComponent(Graphics go)
		{	
			
			Graphics2D gf = (Graphics2D) go;
			
			gf.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			gf.setPaintMode();
			gf.setBackground(colorBG);
			
			gf.clearRect(0,0,getSize().width, getSize().height);
			
			int yOffSet = Port.iconHeight/2;
			
			COLUMNS = (sp.getViewport().getSize().width)/Port.iconHeight;

			int i = 0;
									
			//Get a sorted list of port keys
			Vector v = tank.sortPorts();
			Iterator it = v.iterator();
			int y = 0;
			while (it.hasNext()) 
			{	
				try	
				{
					Integer id = (Integer) it.next();
					Port port = (Port) tank.getPorts().get(id);
					int x = Port.iconHeight * (i - (COLUMNS * (int) Math.floor(i/COLUMNS)));
					y = ((Port.iconHeight * (int) Math.floor(i/COLUMNS)));
					port.setPosition(x,y);
					gf.drawImage(port.imgActivity.getImage(),x,y+ yMouseOffset,this);
					gf.drawImage(port.imgLabel.getImage(),x,y+ yMouseOffset,this);
					i++;
				}
				catch(NoSuchElementException nse) {}
			}
			
			Dimension d = new Dimension(COLUMNS*Port.iconWidth, (Port.iconHeight * (int) (Math.floor(i/COLUMNS)+1)));
			
			// tell the scrollpane that the size has changed
			setPreferredSize(d);
			revalidate();
			
			drawGrid(gf);

		}
		
		protected void drawGrid(Graphics2D g)
		{
			int c = 0; //column
			int r = 0; //row
			
			g.setColor(Style.colorGrid);
			g.setStroke(new BasicStroke(1));
			
			int h = getSize().height;
						
			for(r = 0; r < Math.floor(h/Port.iconHeight); r++)
			{
				int y = (r*Port.iconHeight)+Port.iconHeight+yMouseOffset;
				g.drawLine(0,y,getSize().width,y);
			}
			
			for(r = 0; r < Math.floor(getSize().width/Port.iconHeight); r++)
			{
				int x = (r*Port.iconHeight)+Port.iconHeight;
				g.drawLine(x,0+yMouseOffset,x,getSize().height+yMouseOffset);
			}
		
		}
		
		public boolean imageUpdate(Image img, int i, int x, int y, int w, int h)
		{
			return true;
		}
		
		/*
		* Locate the port which the user has clicked on, if any
		*/
		public Port findTargetPort(Point p)
		{
		
			Iterator it = tank.getPorts().keySet().iterator();
			while (it.hasNext()) 
			{	
				try	
					{
					Integer id = (Integer) it.next();
					Port port = (Port) tank.getPorts().get(id);
					if(port.mouseOver(p))
						{
						return port;
						}
					} catch(NoSuchElementException nse) {}
			}
			return null;
		}	
		
		class MListener extends MouseAdapter
		{
			PortPanel portPanel;
			
			MListener(PortPanel p)
			{
				super();
				portPanel = p;
			}
			
			public void mousePressed(MouseEvent e)
			{
				Point p = e.getPoint();
				p.y = p.y-yMouseOffset;
				Port port = portPanel.findTargetPort(p);
				if(port != null)
				{
					port.isModified = true;
					port.clicked();
				}
			}
		}
		
		class MMotionListener extends MouseMotionAdapter
		{
			PortPanel portPanel;
			
			MMotionListener(PortPanel p)
			{
				super();
				portPanel = p;
			}

			
			public void mouseMoved(MouseEvent e)
			{
			}
		}

	}
	
}
