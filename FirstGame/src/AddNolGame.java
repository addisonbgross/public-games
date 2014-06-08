import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
// input libs
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.geom.Rectangle;
// OpenGL libs
import static org.lwjgl.opengl.GL11.*;
//Slick2D libs
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.Image;


public class AddNolGame
{	
	// constants
	boolean VERBOSE = false;
	boolean HITBOXES = true;
	boolean TERRAINBOXES = true;
	boolean setBlock = false;
	int MAPWIDTH = 10;
	int MAPHEIGHT = 10;
	int SCREENWIDTH = 800;
	int SCREENHEIGHT = 600;
	int STARTX = 5;
	int STARTY = 0;
	int VEL = 1;
	int MAXVEL = 20;
	
	// jumping variables
	boolean jumping = true;
	boolean set = false;
	double jPhase = 0;
	double PI = Math.PI;
	double MAXJUMP = 10;
	
	// map matrix of all frames for this map
	org.newdawn.slick.Image[][] FrameMatrix = new org.newdawn.slick.Image[MAPWIDTH][MAPHEIGHT];
	// ful sprite sheet for testing main character
	org.newdawn.slick.SpriteSheet StrongGuySheet;
	// presents current frames for drawing
	Mainframe Frames;
	// maps FrameMatrix to a boolean matrix that states whether 'Frames' holds the data for that frame
	boolean MainFrame[][];	
	// tells whether blocks have been set or not set
	boolean SetBlocks[][];
	// Animations of main strong guy
	Animation Character, moveLeft, moveRight, moveLeftShield, moveRightShield;
	// handler of frame rate and vsync
	Timing TimeKeeper;
	// loads and holds all of the map tiles for the current level
	MapLoader map;
	
	// start up frame
	int xF = STARTX;
	int yF = STARTY;
	// velocity of character
	float xVel = 0;
	float yVel = 0;
	// main character position
	float xChar = SCREENWIDTH / 2 - 60;
	float yChar = SCREENHEIGHT / 2 - 90;//50;
	// reference axis
	float xRef = -(SCREENWIDTH * STARTX);
	float yRef = -(SCREENHEIGHT * STARTY);
	
	// set up main character
	Player rChar;
	
	//Rectangle rChar  = new Rectangle(xChar, yChar, 60, 90);	
	Rectangle bunkBlock = new Rectangle(0, 0, 0, 0);
	Graphics BlockArtist = new Graphics();
	
	public void start() throws SlickException
	{
		initGL(SCREENWIDTH, SCREENHEIGHT);
		init();
		TimeKeeper = new Timing();	
		Frames = new Mainframe(MAPWIDTH, MAPHEIGHT);
		
		// main game loop
		while (true)
		{
			update(TimeKeeper.getDelta());
			glClear(GL_COLOR_BUFFER_BIT);
			render();
			
			// update screen
			Display.update();
			Display.sync(100);
			
			if (Display.isCloseRequested() || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			{
				Display.destroy();
				System.exit(0);
			}
		}
	}
	
	// initialize images
	public void init() throws SlickException
	{
		try
		{
			// Load main character
			Image StrongGuyImage = new Image("src/res/strongguysheet.png", true);			
			rChar = new Player("strongguy", xChar, yChar, StrongGuyImage);
			Character = rChar.getAnimation("left");

			// prepare maploader
			map = new MapLoader("test", SCREENWIDTH, SCREENHEIGHT);
			String mapFilePath = "src/maps/" + map.prefix() + "_";
			
			// assemble FrameMatrix out of all screen Tiles for this world
			int i , j;
			for (i = 0; i < map.height(); ++i)
				for (j = 0; j < map.width(); ++j)
					FrameMatrix[i][j] = new org.newdawn.slick.Image(mapFilePath + i + "_" + j + ".gif", true);		
			
			SetBlocks = new boolean[MAPWIDTH][MAPHEIGHT];
			MainFrame = new boolean[MAPWIDTH][MAPHEIGHT];
			for (i = 0; i < MAPWIDTH; ++i)
				for (j = 0; j < MAPHEIGHT; ++j)
				{
					SetBlocks[i][j] = false;	
					MainFrame[i][j] = false;
				}
		}
		catch (SlickException e)
		{
			if (STARTX < 0 || STARTX > MAPWIDTH || STARTY < 0 || STARTY > MAPHEIGHT)
				System.out.println("Bad starting frame...\nValues for STARTX are [0][" + (MAPWIDTH - 1) + "]\nValues for STARTY are [0][" + (MAPHEIGHT - 1) + "]");
			e.printStackTrace();
		}
		finally
		{
			System.out.println("Images Found and loaded");
		}
	}
	
	// initialize OpenGL
	public void initGL(int width, int height)
	{
		// open window
		try
		{
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
			Display.setVSyncEnabled(true);
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		glEnable(GL_TEXTURE_2D);
		glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		
		// enable alpha blending
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glViewport(0, 0, width, height);
		glMatrixMode(GL_MODELVIEW);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, 0, height, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}
	
	public void update(int delta)
	{	
		TimeKeeper.updateTimer(delta);
		MainFrame = Frames.orient(xF,  yF);
		Character.start();
		
		int i, j, k = 0;
		
		// response for keyboard controls
		// SPACE key
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) || jumping)
		{			
			if (jumping == false)
			{
				rChar.setY(rChar.getY() + 1);	// just to get off the ground
				jumping = true;
				yVel += 500;
			}	
			
			// maintain horizontal momentum
			if (Keyboard.isKeyDown(Keyboard.KEY_A))
			{
				xVel -= VEL*0.4;
				Character = rChar.getAnimation("left");
				//Character = moveLeft;
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_D))
			{
				xVel += VEL*0.4;
				Character = rChar.getAnimation("right");
				//Character = moveRight;
			}
			Character.stop();
		}
		// A key
		else if (Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			xVel -= VEL;
			Character = rChar.getAnimation("left");
			//Character = moveLeft;
		}
		// D key
		else if (Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			xVel += VEL;
			Character = rChar.getAnimation("right");
			//Character = moveRight;
		}
		// no action, let friction set in
		else
		{
			xVel -= VEL;
			if (xVel < 0)
				xVel = 0;	
			Character.stop();
		}	
		
		// do collision detection of blocks residing in the MainFrame
		jumping = true;
		for (i = 0; i < MAPWIDTH; ++i)
			for (j = 0; j < MAPHEIGHT; ++j)
				if (MainFrame[i][j])
					for (k = 0; k < map.blockDepth(); ++k)
					{
						boolean top = false;
						boolean btm = false;
						
						if (rChar.intersects(map.block(i, j, k)))
						{	
							
							if (rChar.getY() > map.block(i, j, k).getCenterY())
							{
								yVel = 0;
								jumping = false;
								top = true;
								rChar.setY(map.block(i, j, k).getY() + map.block(i, j, k).getHeight());
							}
							else if (rChar.getY() < map.block(i, j, k).getCenterY() && !top)
							{
								btm = true;
								yVel = 0;
								rChar.setY(map.block(i, j, k).getY() - rChar.getHeight());
							}
							
							if (rChar.getX() > map.block(i, j, k).getCenterX() && !top && !btm)
							{
								xVel = 0;
								rChar.setX(map.block(i, j, k).getX() + map.block(i, j, k).getWidth());
							}
							else if (rChar.getX() < map.block(i, j, k).getCenterX() && !top && !btm)
							{
								xVel = 0;
								rChar.setX(map.block(i, j, k).getX() - rChar.getWidth());
							}
						}
					}
		
		if (jumping)
			yVel -= 0.4;
		
		// limit character velocity
		if (xVel >= MAXVEL)
			xVel = MAXVEL;
		else if (xVel <= (-MAXVEL))
			xVel = (-MAXVEL);
		if (yVel >= MAXVEL)
			yVel = MAXVEL;
		else if (yVel <= (-MAXVEL))
			yVel = (-MAXVEL);
		
		rChar.setVel(xVel, yVel);
		xChar = rChar.getX();
		yChar = rChar.getY();
		xRef -= xVel;
		yRef -= yVel;
		
		// calculate which frames you are in
		//  and set MainFrame & Timer
		xF = (int)(-xRef) / SCREENWIDTH;
		yF = (int)(-yRef) / SCREENHEIGHT;
		MainFrame = Frames.orient(xF,  yF);		
		TimeKeeper.updateFPS();	// update the FPS counter

		for (i = 0; i < MAPWIDTH; ++i)
			for (j = 0; j < MAPHEIGHT; ++j)
				if (!SetBlocks[i][j])
				{
					SetBlocks[i][j] = true;
					map.block(i, j, 0).setLocation(xRef + (i*SCREENWIDTH) + map.block(i, j, 0).getX(), yRef + (j*SCREENHEIGHT) + map.block(i, j, 0).getY());
					map.block(i, j, 1).setLocation(xRef + (i*SCREENWIDTH) + map.block(i, j, 1).getX(), yRef + (j*SCREENHEIGHT) + map.block(i, j, 1).getY());
					if (map.block(i, j, 2).toString() != bunkBlock.toString())
						map.block(i, j, 2).setLocation(xRef + (i*SCREENWIDTH) + map.block(i, j, 2).getX(), yRef + (j*SCREENHEIGHT) + map.block(i, j, 2).getY());
				}
				else
				{
					map.block(i, j, 0).setLocation((-xVel) + map.block(i, j, 0).getX(), (-yVel) + map.block(i, j, 0).getY());
					map.block(i, j, 1).setLocation((-xVel) + map.block(i, j, 1).getX(), (-yVel) + map.block(i, j, 1).getY());
					if (map.block(i, j, 2).toString() != bunkBlock.toString())
						map.block(i, j, 2).setLocation((-xVel) + map.block(i, j, 2).getX(), (-yVel) + map.block(i, j, 2).getY());
				}			
	}
	
	// render MainFrame and controlled Block
	public void render()
	{	
		int i, j, k;
		for (j = 0; j < MAPHEIGHT; ++j)
			for (i = 0; i < MAPWIDTH; ++i)
				if (MainFrame[i][j] == true)
				{
					// render only the frames that are within the radius of the MainFrame
					//  all other frames remain un-drawn
					FrameMatrix[i][j].draw(xRef + (i * SCREENWIDTH), yRef + (j * SCREENHEIGHT));
					
					if (TERRAINBOXES)
					{
						for (k = 0; k < map.blockDepth(); ++k)
							BlockArtist.draw(map.block(i, j, k));
					}
					MainFrame[i][j] = false;
				}
						
		if (HITBOXES)
		{
			BlockArtist.draw(rChar.getBox());
		}
		
		Character.draw(rChar.getX(), rChar.getY());
		//Character.draw(xChar, yChar);
	}
	
	public static void main(String[] argv) throws SlickException
	{
		AddNolGame game = new AddNolGame();
		game.start();
	}
}
