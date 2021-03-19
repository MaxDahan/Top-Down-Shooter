import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Block extends GameObject {
	private LoadedTextures LT = new LoadedTextures();
	private BlockType BT;
	private Rectangle Bound;
	
	public Block(int x, int y, BufferedImage img, ObjectID ID, BlockType BT, LoadedTextures LT) {
		super(x, y, img, ID, LT);
		start(BT);
	}
	public Block(int x, int y, int width, int height, ObjectID ID, BlockType BT, LoadedTextures LT) {
		super(x, y, width, height, ID, LT);
		start(BT);
	}
	private void start(BlockType BT) {
		this.BT = BT;
		setUpBounds();
	}
	
	private void setUpBounds() {
		b.setBounds((int)b.getX() - 2, (int)b.getY() - 2, (int)b.getWidth() + 4, (int)b.getHeight() + 4); // adding number just because makes it nicer
	}
	
	public void update() {
	}
	
	public void draw(Graphics2D g) {
		// draws image corresponding to block ID
		if(img != null) g.drawImage(LT.getBlockImage(BT), x, y, null);
		else { // draws a black rectangle if no image is found
			g.setColor(Color.BLACK);
			g.fillRect(x, y, width, height);
		}
	}
}
