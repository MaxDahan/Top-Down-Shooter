import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Game extends Canvas implements Runnable, KeyListener {
	// basic setup
	private static final long serialVersionUID = 1L;
	protected final int GWIDTH = 1920/*(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()*/,
							   GHEIGHT =  1080/*(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()*/,
							   SCALE = 60,
							   PLAYERSCALE = (int)(SCALE*0.75);
	protected static Thread thread;
	protected boolean running, updateRun, drawRun;
	private LoadedTextures LT = new LoadedTextures();
	private Camera camera;
	// others
	protected Handler handler = new Handler();

	public static void main(String[] args) {
		new Game();
	}
	
	public Game() {
		new GameFrame(this, GWIDTH, GHEIGHT);
		
		// loads the map
		handler.map = 1;
		loadLevel(LT.getMap(0));
		start();
		
		camera = new Camera(0, 0, GWIDTH, GHEIGHT, LT.getMap(0).get(0), SCALE); // it does not matter which layer of the map is used because all layers are the same size
		handler.setCamera(camera);
	}
	
	// starting method/constructor called
	public void start() {
		addKeyListener(this);
		running = true;
		updateRun = true;
		drawRun = true;
		thread = new Thread(this);
		thread.start();
	}

	// calls the update method of every game object
	public void update() {
		if(updateRun) {
			for(int i = 0; i < handler.objects.size(); i++) {
				if(handler.objects.get(i).ID == ObjectID.player) {
					camera.update(handler.player);
				}
			}
			handler.update();
		}
	}
	
	// paints every object
	public void paint() {
		if(drawRun) {
			BufferStrategy bs = this.getBufferStrategy();
			if (bs == null) {
				this.createBufferStrategy(3);
				return;
			}
			Graphics2D g = (Graphics2D) bs.getDrawGraphics();
			////////////////////////////////////////////////
			
			g.setColor(new Color(155, 255, 255));
			g.fillRect(0, 0, GWIDTH, GHEIGHT);
				
			g.translate(-camera.x, -camera.y);
			
			g.setColor(Color.BLACK);
			handler.draw(g);
			
			g.translate(camera.x, camera.y);
			
			////////////////////////////////////////////////
			g.dispose();
			bs.show();
		}
	}
	
	// loading the level
	public void loadLevel(ArrayList<BufferedImage> map) {
		// it does not matter which layer of the map is used because all layers are the same size
		int w = map.get(0).getWidth();
		int h = map.get(0).getHeight();
		
		BufferedImage tileLayer = map.get(0);
		BufferedImage blockLayer = map.get(1);
		BufferedImage objectLayer = map.get(2);
		
		// load tiles
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				int pixel = tileLayer.getRGB(x, y);
				int red = (pixel >> 16) & 0xff; 
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				boolean loadImages = true; 
				loadTiles(red, green, blue, x, y, loadImages);
			}
		}
		// load blocks
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				int pixel = blockLayer.getRGB(x, y);
				int red = (pixel >> 16) & 0xff; 
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				boolean loadImages = true; 
				loadBlocks(red, green, blue, x , y, loadImages);
			}
		}
		// load objects
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				int pixel = objectLayer.getRGB(x, y);
				int red = (pixel >> 16) & 0xff; 
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				boolean loadImages = true; 
				loadObjects(red, green, blue, x, y, loadImages);
			}
		}
		
	}
	private void loadTiles(int red, int green, int blue, int x, int y, boolean loadImages) {
		// dirt tiles
		if(red == 255 && green == 255 && blue == 0) {
			if(loadImages) handler.addObject(new Tile(x * SCALE, y * SCALE, LT.getTileImage(TileType.dirt), ObjectID.tile, TileType.dirt, LT));
			else handler.addObject(new Tile(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.tile, TileType.dirt, LT));
		} 
		// castle tiles
		else if(red == 150 && green == 150 && blue == 255) {
			if(loadImages) handler.addObject(new Tile(x * SCALE, y * SCALE, LT.getTileImage(TileType.castle), ObjectID.tile, TileType.castle, LT));
			else handler.addObject(new Tile(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.tile, TileType.castle, LT));
		}
	}
	private void loadBlocks(int red, int green, int blue, int x, int y, boolean loadImages) {
		// dirt blocks
		if(red == 150 && green == 100 && blue == 50) {
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.dirt), ObjectID.block, BlockType.dirt, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.dirt, LT));
		}
		//edge
		else if(red == 0 && green == 255 && blue == 0) { // dirtGrassUpBlock
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.dirtGrassUp), ObjectID.block, BlockType.dirtGrassUp, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.dirtGrassUp, LT));
		} else if(red == 0 && green == 150 && blue == 0) { // dirtGrassDownBlock
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.dirtGrassDown), ObjectID.block, BlockType.dirtGrassDown, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.dirtGrassDown, LT));
		} else if(red == 0 && green == 200 && blue == 0) { // dirtGrassRightBlock
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.dirtGrassRight), ObjectID.block, BlockType.dirtGrassRight, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.dirtGrassRight, LT));	
		} else if(red == 0 && green == 100 && blue == 0) { // dirtGrassLeftBlock
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.dirtGrassLeft), ObjectID.block, BlockType.dirtGrassLeft, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.dirtGrassLeft, LT));
		} 
		//corner
		else if(red == 0 && green == 25 && blue == 0) { // dirtGrassUpRightBlock
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.dirtGrassUpRight), ObjectID.block, BlockType.dirtGrassUpRight, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.dirtGrassUpRight, LT));
		} else if(red == 0 && green == 50 && blue == 0) { // dirtGrassDownRightBlock
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.dirtGrassDownRight), ObjectID.block, BlockType.dirtGrassDownRight, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.dirtGrassDownRight, LT));
		} else if(red == 0 && green == 125 && blue == 0) { // dirtGrassUpLeftBlock
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.dirtGrassUpLeft), ObjectID.block, BlockType.dirtGrassUpLeft, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.dirtGrassUpLeft, LT));	
		} else if(red == 0 && green == 225 && blue == 0) { // dirtGrassDownLeftBlock
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.dirtGrassDownLeft), ObjectID.block, BlockType.dirtGrassDownLeft, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.dirtGrassDownLeft, LT));
		}
		
		// castle blocks
		else if(red == 100 && green == 100 && blue == 100) {
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.castle), ObjectID.block, BlockType.castle, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.castle, LT));
		}
		//edge
		else if(red == 150 && green == 150 && blue == 150) { // castleUpBlock
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.castleUp), ObjectID.block, BlockType.castleUp, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.castleUp, LT));
		}
		else if(red == 50 && green == 50 && blue == 50) { // castleDownBlock
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.castleDown), ObjectID.block, BlockType.castleDown, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.castleDown, LT));
		}
		else if(red == 75 && green == 75 && blue == 75) { // castleRightBlock
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.castleRight), ObjectID.block, BlockType.castleRight, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.castleRight, LT));
		}
		else if(red == 125 && green == 125 && blue == 125) { // castleLeftBlock
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.castleLeft), ObjectID.block, BlockType.castleLeft, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.castleLeft, LT));
		}
		//corner
		else if(red == 175 && green == 175 && blue == 175) { // castleUpRightBlock
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.castleUpRight), ObjectID.block, BlockType.castleUpRight, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.castleUpRight, LT));
		}
		else if(red == 200 && green == 200 && blue == 200) { // castleDownRightBlock
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.castleDownRight), ObjectID.block, BlockType.castleDownRight, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.castleDownRight, LT));
		}
		else if(red == 25 && green == 25 && blue == 25) { // castleUpLeftBlock
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.castleUpLeft), ObjectID.block, BlockType.castleUpLeft, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.castleUpLeft, LT));
		}
		else if(red == 225 && green == 225 && blue == 225) { // castleDownLeftBlock
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.castleDownLeft), ObjectID.block, BlockType.castleDownLeft, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.castleDownLeft, LT));
		}
		//inner
		else if(red == 140 && green == 140 && blue == 140) { // castleUpRightInner Block
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.castleUpRightInner), ObjectID.block, BlockType.castleUpRightInner, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.castleUpRightInner, LT));
		}
		else if(red == 120 && green == 120 && blue == 120) { // castleDownRightInner Block
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.castleDownRightInner), ObjectID.block, BlockType.castleDownRightInner, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.castleDownRightInner, LT));
		}
		else if(red == 130 && green == 130 && blue == 130) { // castleUpLeftInner Block
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.castleUpLeftInner), ObjectID.block, BlockType.castleUpLeftInner, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.castleUpLeftInner, LT));
		}
		else if(red == 110 && green == 110 && blue == 110) { // castleDownLeftInner Block
			if(loadImages) handler.addObject(new Block(x * SCALE, y * SCALE, LT.getBlockImage(BlockType.castleDownLeftInner), ObjectID.block, BlockType.castleDownLeftInner, LT));
			else handler.addObject(new Block(x * SCALE, y * SCALE, SCALE, SCALE, ObjectID.block, BlockType.castleDownLeftInner, LT));
		}
	}
	private void loadObjects(int red, int green, int blue, int x, int y, boolean loadImages) {
		// player
		if(red == 0 && green == 0 && blue == 255) {
			if(loadImages) handler.addPlayer(new Player(x * SCALE, y * SCALE, LT.getPlayerImage(), ObjectID.player, LT, handler));
			else handler.addPlayer(new Player(x * SCALE, y * SCALE, PLAYERSCALE, PLAYERSCALE, ObjectID.player, LT, handler));
		}
	}
	
	// Key Presses
	public void keyPressed(KeyEvent evt) {
		int key = evt.getKeyCode();
		if (key == KeyEvent.VK_ESCAPE) {
			stop();
		} else if(key == KeyEvent.VK_W) {
			handler.player.up = true;
		} else if(key == KeyEvent.VK_A) {
			handler.player.left = true;
		} else if(key == KeyEvent.VK_S) {
			handler.player.down = true;
		} else if(key == KeyEvent.VK_D) {
			handler.player.right = true;
		} else if(key == KeyEvent.VK_SPACE) {
			handler.player.shoot = true;
		}
	}
	public void keyReleased(KeyEvent evt) {
		int key = evt.getKeyCode();
		
		if(key == KeyEvent.VK_W) {
			handler.player.up = false;
		} else if(key == KeyEvent.VK_A) {
			handler.player.left = false;
		} else if(key == KeyEvent.VK_S) {
			handler.player.down = false;
		} else if(key == KeyEvent.VK_D) {
			handler.player.right = false;
		} else if(key == KeyEvent.VK_SPACE) {
			handler.player.shoot = false;
			handler.player.shotOnce = false;
		}
	}
	public void keyTyped(KeyEvent arg0) {}
	
	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				delta--;
			}
			paint();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frames = 0;
			}
		}
		stop();
	}
	public void stop() {
		running = false;
		try {
			thread.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}