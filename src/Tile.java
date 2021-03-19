import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Tile extends GameObject {
	private LoadedTextures LT = new LoadedTextures();
	private TileType TT;
	private Rectangle Bound;

	public Tile(int x, int y, BufferedImage img, ObjectID ID, TileType TT, LoadedTextures LT) {
		super(x, y, img, ID, LT);
		start(TT);
	}

	public Tile(int x, int y, int width, int height, ObjectID ID, TileType TT, LoadedTextures LT) {
		super(x, y, width, height, ID, LT);
		start(TT);
	}

	private void start(TileType TT) {
		this.TT = TT;
	}

	// sets up the multiple edges on the tile, is re-used in the update method
	private void setUpBounds() {
		Bound = new Rectangle();
		Bound.setBounds((int) b.getX(), (int) b.getY(), (int) b.getWidth(), (int) b.getHeight());
	}

	public void update() {
		setUpBounds();
	}

	public void draw(Graphics2D g) {
		// draws image corresponding to tile ID
		if (img != null)
			g.drawImage(LT.getTileImage(TT), x, y, null);
		else { // draws a black rectangle if no image is found
			g.setColor(Color.BLACK);
			g.fillRect(x, y, width, height);
		}
	}
}
