import org.newdawn.slick.geom.Rectangle;

public class MapLoader 
{
	private String mapPrefix;
	private int mapWidth;
	private int mapHeight;
	private Rectangle[][][] BlockList;
	private int blockLimit = 3;
	
	public MapLoader(String name, int screenWidth, int screenHeight)
	{
		if (name == "test")
		{
			mapPrefix = "test/f";
			mapWidth = 10;
			mapHeight = 10;
			BlockList = new Rectangle[mapWidth][mapHeight][blockLimit];			
			Rectangle bunkBlock = new Rectangle(0, 0, 0, 0);
			
			int i, j;
			for (i = 0; i < mapWidth; ++i)
				for (j = 0; j < mapHeight; ++j)
				{
					BlockList[i][j][0] = new Rectangle(500, 200, 200, 50);
					BlockList[i][j][1] = new Rectangle(100, 400, 200, 50);
					BlockList[i][j][2] = bunkBlock;
				}
			// set starting floor
			BlockList[5][0][2] = new Rectangle(0, 0, screenWidth, 30);
		}
	}
	
	public int width()
	{
		return mapWidth;
	}
	
	public int height()
	{
		return mapHeight;
	}
	
	public String prefix()
	{
		return mapPrefix;
	}
	
	public Rectangle block(int xFrame, int yFrame, int index)
	{
		Rectangle Current;
		Current = BlockList[xFrame][yFrame][index];
		return Current;
	}
	
	public int blockDepth()
	{
		return blockLimit;
	}
}
