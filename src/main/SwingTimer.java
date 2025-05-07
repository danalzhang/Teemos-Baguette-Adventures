package main;

/** The "SwingTimer" class.
 * Demonstrates a Swing Timer
 */
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class SwingTimer
{
	public Timer timer;
	public int time;
	public int timeAllowed;
	game g;

	public SwingTimer (game g)
	{
		this.g = g;
		time = 2000;
		timeAllowed = 0;   //  10 seconds in this example

		// Create a timer object. This object generates an event every
		// 1/10 second (100 milliseconds)
		// The TimerEventHandler object that will handle this timer
		// event is defined below as a inner class
		timer = new Timer (100, new TimerEventHandler ());
	} // Constructor


	// An inner class to deal with the timer events
	private class TimerEventHandler implements ActionListener
	{
		// The following method is called each time a timer event is
		// generated (every 100 milliseconds in this example)
		// Put your code here that handles this event
		public void actionPerformed (ActionEvent event)
		{
			// Time is up!
			if (time <= timeAllowed)
			{
				timer.stop ();
				g.allowMove = false;
			}
			else
			{
				// Increment the time (you could also count down)
				time--;
			}
			g.repaint();
		}
	}

} // SwingTimer class


