import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {
	protected int x, y, width, height;
	protected LoadedTextures LT;
	protected float velX, velY;
	protected Rectangle b = new Rectangle();
	protected BufferedImage img;
	protected ObjectID ID;
	public Color B = new Color(0, 0, 0);
	
	// for the basic 4 percent inside boundary
	public GameObject(int x, int y, BufferedImage img, ObjectID ID, LoadedTextures LT) {
		this.LT = LT;
		this.img = img;
		setStart(x, y, img.getWidth(), img.getHeight(), ID);
	}
	
	// used for development when wanting to draw box and not image
	public GameObject(int x, int y, int width, int height, ObjectID ID, LoadedTextures LT) {
		this.LT = LT;
		setStart(x, y, width, height, ID);
	}
	
	// used for less repetition
	private void setStart(int x, int y, int width, int height, ObjectID ID) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.ID = ID;
		b.setBounds(x, y, width, height);
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	
	public abstract void update();
	
	public abstract void draw(Graphics2D g);
}
