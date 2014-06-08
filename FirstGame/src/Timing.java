import org.lwjgl.Sys;

public class Timing 
{
	// time at last frame
	long lastFrame;
	// frames per second
	int fps;
	int delta;
	int time;
	// last fps time
	long lastFPS;
	
	public Timing()
	{
		this.delta = 0;
		this.lastFPS = 0;
		this.time = 0;
	}
	
	public void updateTimer(int del)
	{
		time += del;
		if(time > 4000)
			time = 0;	
	}
	
	// calculate how many milliseconds have passed since last frame
	public void setDelta()
	{
		long time = getTime();
		delta = (int)(time - lastFrame);
		lastFrame = time;
	}
	
	public void setLastFPS()
	{
		lastFPS = (Sys.getTime() * 1000 / Sys.getTimerResolution());
	}
	
	public int getDelta()
	{
		return delta;
	}
	
	// get accurate system time
	private long getTime()
	{
		return (Sys.getTime() * 1000 / Sys.getTimerResolution());
	}
	
	// calculate the FPS and set it in the title bar
	public void updateFPS()
	{
		if (getTime() - lastFPS > 1000)
		{
			fps = 0;
			lastFPS += 1000;
		}
		++fps;
	}
	
	public int getFPS()
	{
		return fps;
	}
}
