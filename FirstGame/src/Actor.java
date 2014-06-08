import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;

public abstract class Actor {
	private int health;
	private int stamina;
	private float xVel;
	private float yVel;
	private Rectangle box;
	private SpriteSheet sprites;
	private Animation current, left, right, jump;
	//private String[] states = {"walking", "stopped", "bored", "attacking", "jumping"};
	//private String currentState;
	
	public Actor(String name, float x, float y, Image startSheet) {
		xVel = 0;
		yVel = 0;
		
		if (name == "strongguy") { 
			makeAnimations(name, startSheet);
			box = new Rectangle(x, y, 60, 90);
		}
	}
	
	// Getters
	public int getHealth() {
		return health;
	}
	public int getStamina() { 
		return stamina;
	}
	public int getX() {
		return (int) box.getX();
	}	
	public float getY() {
		return box.getY();
	}
	public float getWidth() {
		return box.getWidth();
	}
	public float getHeight() {
		return box.getHeight();
	}
	public float getXvel() {
		return xVel;
	}
	public float getYvel() {
		return yVel;
	}
	public Rectangle getBox() {
		return box;
	}
	public Animation getAnimation(String name) {
		if (name == "left")
			current = left;
		else if (name == "right")
			current = right;
		else if (name == "jump")
			current = jump;
		
		return current;
	}
	
	// Setters
	public void setVel(float x, float y) {
		xVel = x;
		yVel = y;
	}
	public void setX(float x) {
		box.setY(x);
	}
	public void setY(float y) {
		box.setY(y);
	}
	
	// abstraction of Rectangle's intersection
	public boolean intersects(Rectangle rect) {
		return box.intersects(rect);
	}
	
	// Assemble the sprite sheet to create the animations (Improve method TODO)
	private void makeAnimations(String name, Image startSheet) {
		if (name == "strongguy") {
			// load character images
			Image StrongGuyImage = startSheet;
	
			sprites = new SpriteSheet(StrongGuyImage, 60, 90);
			
			Image[] TempSheet = new Image[3];
			
			TempSheet[0] = sprites.getSprite(0, 1);
			TempSheet[1] = sprites.getSprite(1, 1);
			TempSheet[2] = sprites.getSprite(2, 1);
			left = new Animation(TempSheet, 150);
			
			TempSheet[0] = sprites.getSprite(0, 0);
			TempSheet[1] = sprites.getSprite(1, 0);
			TempSheet[2] = sprites.getSprite(2, 0);
			right = new Animation(TempSheet, 150);
			
			current = left;
			current.start();
		}
	}
}
