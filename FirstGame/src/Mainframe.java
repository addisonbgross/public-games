public class Mainframe 
{
	private boolean MainFrame[][];
	private int mapWidth;
	private int mapHeight;
	
	public Mainframe(int width, int height)
	{
		MainFrame = new boolean[width][height];
		mapWidth = width;
		mapHeight = height;
		
		int i, j;
		for (i = 0; i < width; ++i)
			for (j = 0; j < height; ++j)
				MainFrame[i][j] = false;
	}
	
	public boolean[][] orient(int x, int y)
	{
		// gather all Mainframe frames' coordinates
		int topLeftX = (x - 1), topLeftY = (y + 1);
		int midLeftX = (x - 1), midLeftY = y;
		int lowLeftX = (x - 1), lowLeftY = (y - 1);
		int topMidX = x, topMidY = (y + 1); 
		int lowMidX = x, lowMidY = (y - 1);
		int topRightX = (x + 1), topRightY = (y + 1); 
		int midRightX = (x + 1), midRightY = y;
		int lowRightX = (x + 1), lowRightY = (y - 1);
		
		// topLeft
		if (topLeftX >= 0 && topLeftY < mapHeight)
			MainFrame[topLeftX][topRightY] = true;
		// midLeft
		if (midLeftX >= 0 && midLeftY >= 0)
			MainFrame[midLeftX][midLeftY] = true;
		// lowLeft
		if (lowLeftX >= 0 && lowLeftY >= 0)
			MainFrame[lowLeftX][lowLeftY] = true;
		// topMid
		if (topMidY < mapHeight)	
			MainFrame[topMidX][topMidY] = true;
		// center
		MainFrame[x][y] = true;
		// lowMid
		if (lowMidY >= 0)
			MainFrame[lowMidX][lowMidY] = true;
		// topRight
		if (topRightX < mapWidth && topRightY < mapHeight)
			MainFrame[topRightX][topRightY] = true;
		// midRight
		if (midRightX < mapWidth)	
			MainFrame[midRightX][midRightY] = true;
		// lowRight
		if (lowRightX < mapWidth && lowRightY >= 0)
			MainFrame[lowRightX][lowRightY] = true;
		
		return MainFrame;
	}
}
