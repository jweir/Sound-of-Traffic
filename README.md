# Sound of Traffic

**Note** this was written way back in 2004/2005 and requires `tcpdump` to be
installed.  I have not looked at the code since then. Also, super user/root
access is required to get the network data.

Sound of Traffic is a Java "application" which converts TCP/IP header
information into midi notes via the Java Synthesizer. The purpose is to listen
in on network traffic in ordered time, via a tempo, rather than realtime, which
could be more chaotic. In this sense it becomes closer to music then noise.

Play back of traffic is sorted by source and destination addresses and ports.
Ports are assigned individual midi instruments and played on odd or even ticks
depending upon whether it is a source or destination packet. The note played by
the port is based upon the number of hits (amount of traffic) occurring on the
port.

Download and learn more http://www.smokinggun.com/projects/soundoftraffic/
