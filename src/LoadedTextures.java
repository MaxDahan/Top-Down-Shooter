import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LoadedTextures {
	private BufferedImageLoader BIL = new BufferedImageLoader();
	// Paths
		//map
		private String mapPath = "data/maps/";
		//block
		private String blockPath = "data/blocks/";
		//tile
		private String tilePath = "data/tiles/";
			//types of tile or blocks
		private String dirtPath = "dirt/", castlePath = "castle/";
		//sprite paths
		private String spritePath = "data/sprites/";
			//types of sprites
		private String playerPath = "players/";
		private String bulletPath = "bullets/";
	// Blocks
		//dirt
		private BufferedImage dirtBlock,
		grassUpBlock, grassDownBlock, grassRightBlock, grassLeftBlock,
		grassUpRightBlock, grassDownRightBlock, grassUpLeftBlock, grassDownLeftBlock;
		//castle
		private BufferedImage castleBlock,
		castleUpBlock, castleDownBlock, castleRightBlock, castleLeftBlock,
		castleUpRightBlock, castleDownRightBlock, castleUpLeftBlock, castleDownLeftBlock,
		castleUpRightInnerBlock, castleDownRightInner, castleUpLeftInner, castleDownLeftInner;
		//etc
		private BufferedImage basicBlock;
	// Tile
		//dirt
		private BufferedImage dirtTile;
		//castle
		private BufferedImage castleTile;
		//etc
		private BufferedImage basicTile;
	// Maps, each map has 3 layers: a tile layer, a block layer, and an object layer (example: player, enemy)
		private ArrayList<BufferedImage> map1 = new ArrayList<BufferedImage>() {{
			add(BIL.loadImage(mapPath + "/1/map1TileLayer.png"));
			add(BIL.loadImage(mapPath + "/1/map1BlockLayer.png"));
			add(BIL.loadImage(mapPath + "/1/map1ObjectLayer.png"));}};
		private ArrayList<ArrayList<BufferedImage>> maps = new ArrayList<ArrayList<BufferedImage>>(){{add(map1);}};
	
	public LoadedTextures() {
		setUpBlocks();
		setUpTiles();
	}
	private void setUpBlocks() {
		// dirt 
		dirtBlock = BIL.loadImage(blockPath + dirtPath + "dirtBlock.png");
			//edge
		grassUpBlock = BIL.loadImage(blockPath + dirtPath + "dirtGrassUpBlock.png");
		grassDownBlock = BIL.loadImage(blockPath + dirtPath + "dirtGrassDownBlock.png");
		grassRightBlock = BIL.loadImage(blockPath + dirtPath + "dirtGrassRightBlock.png");
		grassLeftBlock = BIL.loadImage(blockPath + dirtPath + "dirtGrassLeftBlock.png");
			//corner
		grassUpRightBlock = BIL.loadImage(blockPath + dirtPath + "dirtGrassUpRightBlock.png");
		grassDownRightBlock = BIL.loadImage(blockPath + dirtPath + "dirtGrassDownRightBlock.png");
		grassUpLeftBlock = BIL.loadImage(blockPath + dirtPath + "dirtGrassUpLeftBlock.png");
		grassDownLeftBlock = BIL.loadImage(blockPath + dirtPath + "dirtGrassDownLeftBlock.png");
		
		// castle
		castleBlock = BIL.loadImage(blockPath + castlePath + "castleBlock.png");
			//edge
		castleUpBlock = BIL.loadImage(blockPath + castlePath + "castleUpBlock.png");
		castleDownBlock = BIL.loadImage(blockPath + castlePath + "castleDownBlock.png");
		castleRightBlock = BIL.loadImage(blockPath + castlePath + "castleRightBlock.png");
		castleLeftBlock = BIL.loadImage(blockPath + castlePath + "castleLeftBlock.png");
			//corner
		castleUpRightBlock = BIL.loadImage(blockPath + castlePath + "castleUpRightBlock.png");
		castleDownRightBlock = BIL.loadImage(blockPath + castlePath + "castleDownRightBlock.png");
		castleUpLeftBlock = BIL.loadImage(blockPath + castlePath + "castleUpLeftBlock.png");
		castleDownLeftBlock = BIL.loadImage(blockPath + castlePath + "castleDownLeftBlock.png");
			//inner
		castleUpRightInnerBlock = BIL.loadImage(blockPath + castlePath + "castleUpRightInnerBlock.png");
		castleDownRightInner = BIL.loadImage(blockPath + castlePath + "castleDownRightInnerBlock.png");
		castleUpLeftInner = BIL.loadImage(blockPath + castlePath + "castleUpLeftInnerBlock.png");
		castleDownLeftInner = BIL.loadImage(blockPath + castlePath + "castleDownLeftInnerBlock.png");
		
		// etc
		basicBlock = BIL.loadImage(blockPath + "basicBlock.png");
	}
	private void setUpTiles() {
		// dirt
		dirtTile = BIL.loadImage(tilePath + dirtPath + "dirtTile.png");
		
		// castle
		castleTile = BIL.loadImage(tilePath + castlePath + "castleTile.png");
		
		// etc
		basicTile = BIL.loadImage(tilePath + "basicTile.png");
	}
	
	// Get Images
	public BufferedImage getBlockImage(BlockType BT) {
		
		// dirt block
		if(BT == BlockType.dirt) return dirtBlock;
			//with grass
		else if(BT == BlockType.dirtGrassUp) return grassUpBlock;
		else if(BT == BlockType.dirtGrassDown) return grassDownBlock;
		else if(BT == BlockType.dirtGrassRight) return grassRightBlock;
		else if(BT == BlockType.dirtGrassLeft) return grassLeftBlock;
			//corner ones
		else if(BT == BlockType.dirtGrassUpRight) return grassUpRightBlock;
		else if(BT == BlockType.dirtGrassDownRight) return grassDownRightBlock;
		else if(BT == BlockType.dirtGrassUpLeft) return grassUpLeftBlock;
		else if(BT == BlockType.dirtGrassDownLeft) return grassDownLeftBlock;
		
		// castle block
		else if(BT == BlockType.castle) return castleBlock;
			//with edge
		else if(BT == BlockType.castleUp) return castleUpBlock;
		else if(BT == BlockType.castleDown) return castleDownBlock;
		else if(BT == BlockType.castleRight) return castleRightBlock;
		else if(BT == BlockType.castleLeft) return castleLeftBlock;
			//corner ones
		else if(BT == BlockType.castleUpRight) return castleUpRightBlock;
		else if(BT == BlockType.castleDownRight) return castleDownRightBlock;
		else if(BT == BlockType.castleUpLeft) return castleUpLeftBlock;
		else if(BT == BlockType.castleDownLeft) return castleDownLeftBlock;
			//inner
		else if(BT == BlockType.castleUpRightInner) return castleUpRightInnerBlock;
		else if(BT == BlockType.castleDownRightInner) return castleDownRightInner;
		else if(BT == BlockType.castleUpLeftInner) return castleUpLeftInner;
		else if(BT == BlockType.castleDownLeftInner) return castleDownLeftInner;
		
		// etc block
		else return basicBlock;
	}
	public BufferedImage getTileImage(TileType TT) {
		// dirt tile
		if(TT == TileType.dirt) return dirtTile;
		
		// castle tile
		else if(TT == TileType.castle) return castleTile;
		
		// etc tile
		return basicTile;
	}
	public BufferedImage getPlayerImage() {
		return BIL.loadImage(spritePath + playerPath + "player.png");
	}
	public BufferedImage getBulletImage() {
		return BIL.loadImage(spritePath + bulletPath + "bullet.png");
	}
	public ArrayList<BufferedImage> getMap(int mapNumber) {
		return maps.get(mapNumber);
	}
}