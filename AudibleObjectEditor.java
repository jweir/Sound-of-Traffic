import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;  
import java.util.*;

abstract public class AudibleObjectEditor extends GUI_Component implements Observer, Constants
{

	protected JFrame frame;
	protected Port port;
	
	AudibleObjectEditor(Port p)
	{
		super();
		port = p;
	}
	
	abstract public void update(Observable o, Object s);
	
	public void show()
	{
		frame.show();
		frame.toFront();
	}
	
		public void remove()
	{
		frame.dispose();
	}


}
