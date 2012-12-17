/**
* Launches tcpdump and streams data to TCPFilter
* 
* Note: run tcpdump with -l to flush the contents immediaitley and -n to disable name resolving
* Bug: This does not properly work on devices other than en0.  Not sure why yet.
*/

import java.io.*;
import java.net.*;
import java.util.*;

public class TCPDump extends Thread
{
	private Process tcpProcess;
	private BufferedReader tcpStream;
	private InputStream tcpInput;
	private boolean keepRunning = true;
	
	TCPDump()
	{
	}
	
	/**
	* Get all network interfaces on the machine
	*/	
	public static Vector getNetworkInterfaces()
	{
		Vector interfaces = new Vector();
		try
		{
		for (Enumeration e = NetworkInterface.getNetworkInterfaces() ; e.hasMoreElements() ;) 
		{
			NetworkInterface ni = (NetworkInterface) e.nextElement();
			interfaces.add(ni.getName());
		}
		} catch(SocketException se) {}

		return interfaces;
	}
	
	/**
	* Resets the tcpdump process, but is not working
	*/
	public void reset(String password, String dev)
	{
	
		try {
			String params[] = {"sudo","tcpdump","-ln", "-i",dev};
			tcpProcess = Runtime.getRuntime().exec(params);

			sendPromptPassword(password);
			}
		catch(Exception e)
			{
			System.err.println("TCPDump initialize failure.");
			e.printStackTrace();
			}
	}
	
	/*
		
	*/
	
	public void end()
	{	
		keepRunning = false;
	}
	
	/**
	* Start thed buffered reader stream of tcdump output
	*/
	public void run()
	{
		keepRunning = true;
		
		System.out.println("TCPDump Started.");
		tcpStream = new BufferedReader(new InputStreamReader(tcpProcess.getInputStream()), 1);
		
		readStream();
		
		//Need to code to clean up and destroy the process
		System.out.println("TCPDump Stopped.");
		
	}
	
	/**
	* Reads the stream from the dump and passes it to the TCPFilter
	*/
	private void readStream()
	{	
		while(keepRunning)
		try
			{
				TCPFilter.parseLineFromStream(tcpStream.readLine());
			}
		catch(IOException ioe)
			{
			System.err.println(ioe);
			}
			
	}
	
	/**
	* Send sudo password 
	*/
	protected void sendPromptPassword(String rootPassword)
		{
		System.out.println("Sending password. Attempting to start tcpdump process.");
		try
			{
			OutputStream out = tcpProcess.getOutputStream();
			out.write(rootPassword.getBytes());
			out.close();
			}
		catch(IOException ioe)
			{
			}
		
			String lr = "";
		BufferedReader tempStream = new BufferedReader(new InputStreamReader(tcpProcess.getErrorStream()), 1);
		try {lr = tempStream.readLine();} catch(IOException ioe){}
		
		System.out.println(lr);
		}

}