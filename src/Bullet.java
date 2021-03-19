import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {
	protected ObjectID fromWho;
	protected boolean shooting;
	protected int xOffset; // used for aligning it with barrel of gun
	
	public Bullet(int x, int y, BufferedImage img, ObjectID ID, LoadedTextures LT, ObjectID fromWho) {
		super(x, y, img, ID, LT);
		start(fromWho);
	}
	public Bullet(int x, int y, int width, int height, ObjectID ID, LoadedTextures LT, ObjectID fromWho) {
		super(x, y, width, height, ID, LT);
		start(fromWho);
	}
	public void start(ObjectID fromWho) {
		this.fromWho = fromWho;
		shooting = false;
		xOffset = 62;
		x += xOffset;
	}
	
	public void update(int X, int Y) {
		b.setBounds(x, y, width, height);
		if(shooting) {
			x += velX;
			y += velY;
		} // the not shooting position gets updated in the rotating draw method of the player
	}
	public void draw(Graphics2D g) {
		if(img != null) {
			if(!shooting) g.drawImage(img, xOffset, 0, null);
			else g.drawImage(img, x, y, null);
		} else {
			if(!shooting) g.fillRect(xOffset, 0, width, height);
			else g.fillRect(x, y, width, height);
		}
	}
	
	public void update() {}
}
