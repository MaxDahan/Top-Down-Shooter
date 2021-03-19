import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends GameObject {
	public boolean up, down, left, right, shoot, shotOnce; //shotOnce makes it so that when u press space it only shoots one bullet until you let go and click space again
	protected int playerSpeed, ammoCapacity, bulletSpeed;
	protected float airResistance, diagonalSlowDown;
	protected CollisionHandler CH;
	private Handler handler;
	private ArrayList<Bullet> ammo = new ArrayList<Bullet>();

	// Constructors
	public Player(int x, int y, BufferedImage img, ObjectID ID, LoadedTextures LT, Handler handler) {
		super(x, y, img, ID, LT);
		start(handler);
	}
	public Player(int x, int y, int width, int height, ObjectID ID, LoadedTextures LT, Handler handler) {
		super(x, y, width, height, ID, LT);
		start(handler);
	}
	private void start(Handler handler) {
		this.handler = handler;
		
		up = false;
		down = false;
		left = false;
		right = false;
		
		playerSpeed = 8;
		airResistance = 0.3f;
		diagonalSlowDown = (float)(Math.sqrt(2)/2);
		
		ammoCapacity = 10;
		bulletSpeed = 7;
		for(int i = 0; i < ammoCapacity; i++) ammo.add(new Bullet(x, y, LT.getBulletImage(), ObjectID.bullet, LT, ObjectID.player)); //+62 is the offset

		setUpBounds();
	}
	
	private void setUpBounds() { 
		CH = new CollisionHandler();
		b.setBounds(x, y, width, height);
		CH.topBound.setBounds((int)(b.getX() + playerSpeed), (int)b.getY(), (int)(b.getWidth() - playerSpeed*2), (int)(b.getHeight() - b.getHeight()/2));
		CH.bottomBound.setBounds((int)(b.getX() + playerSpeed), (int)(b.getY() + b.getHeight()/2), (int)(b.getWidth() - playerSpeed*2), (int)(b.getHeight() - b.getHeight()/2));
		CH.rightBound.setBounds((int)(b.getX() + b.getWidth() - playerSpeed), (int)(b.getY() + playerSpeed*0.75), playerSpeed, (int)(b.getHeight() - playerSpeed*0.75 * 2));
		CH.leftBound.setBounds((int)b.getX(), (int)(b.getY() + playerSpeed*0.75), playerSpeed, (int)(b.getHeight() - playerSpeed*0.75 * 2));
	}
	
	public void update() {
		
		if(up) {
			velY = -playerSpeed;
		}
		if(down) {
			velY = playerSpeed;
		}
		if(left) {
			velX = -playerSpeed;
		}
		if(right) {
			velX = playerSpeed;
		}
		if((up && right) || (up && left) || (down && right) || (down && left)) { // slows down the player if moving diagonally because otherwise he'll go super fast
			velX *= diagonalSlowDown;
			velY *= diagonalSlowDown;
		}
		airResistance();
		
		setUpBounds();
		collision();
		boolean found = false; //if there was an available bullet not currently shooting
		for(int i = 0; i < ammoCapacity; i++) {
			if(shoot && !shotOnce) {
				if(ammo.get(i).shooting == false && !found) {
					ammo.get(i).shooting = true;
					
					// shoots in the correct direction with the right speed
					double xDistance = MouseInfo.getPointerInfo().getLocation().getX() - (x - handler.camera.x);
					double yDistance = MouseInfo.getPointerInfo().getLocation().getY() - (y - handler.camera.y);
					double angle = Math.atan2(yDistance, xDistance);
					ammo.get(i).velX = (float) (bulletSpeed * Math.cos(angle));
					ammo.get(i).velY = (float) (bulletSpeed * Math.sin(angle));
					
					found = true;
					shotOnce = true;
				}
			}
			ammo.get(i).update(x, y);
		}
		
		x += velX;
		y += velY;
	}
	private void collision() {
		for(int i = 0; i < handler.objects.size(); i++) {
			GameObject tempObject = handler.objects.get(i);
			if(b.intersects(tempObject.b) && tempObject.ID == ObjectID.block) {
				if(velY < 0 && CH.topBound.intersects(tempObject.b)) { // going up
					velY = 0;
				} else if(velY > 0 && CH.bottomBound.intersects(tempObject.b)) { // going down
					velY = 0;
				}
				if(velX > 0 && CH.rightBound.intersects(tempObject.b)) { // going right
					velX = 0;
				} else if(velX < 0 && CH.leftBound.intersects(tempObject.b)) { // going left
					velX = 0;
				}
			}
		}
	}
	private void airResistance() { // slowly slows down the player in the direction in which that key was released
		if(!left || !right) { // used to horizontally slow the player down after all horizontal keys have stopped being pressed
			if(velX > 0) {
				velX = velX - playerSpeed*airResistance;
				if(velX <= 0) velX = 0; // if it subtracts too much just make it zero
			} else {
				velX = velX + Math.abs(playerSpeed*airResistance);
				if(velX >= 0) velX = 0; // if it adds too much just make it zero
			}
		}
		if(!down || !up) { // used to vertically slow the player down after all vertical keys have stopped being pressed
			if(velY > 0) {
				velY = velY - playerSpeed*airResistance;
				if(velY <= 0) velY = 0; // if it subtracts too much just make it zero
			} else {
				velY = velY + Math.abs(playerSpeed*airResistance);
				if(velY >= 0) velY = 0; // if it adds too much just make it zero
			}
		}
	}

	// draw your rectangle
	public void draw(Graphics2D g) {
		for(int i = 0; i < ammoCapacity; i++) {
         	if(ammo.get(i).shooting) ammo.get(i).draw(g);
 		}
		
		//drawHitBox(g);
		if(img != null) {
			int cx = img.getWidth()/2;
	        int cy = img.getHeight()/2;
	        
			AffineTransform at = new AffineTransform();
			double angle = Math.atan2(MouseInfo.getPointerInfo().getLocation().getY() - (y - handler.camera.y), MouseInfo.getPointerInfo().getLocation().getX() - (x - handler.camera.x)) + Math.toRadians(90);
            at.setToRotation(angle, x - handler.camera.x + cx, y - handler.camera.y + cy);
            at.translate(x - handler.camera.x, y - handler.camera.y);
            g.setTransform(at);
            
            //draw bullet first so they are under the barrel
            for(int i = 0; i < ammoCapacity; i++) {
            	if(!ammo.get(i).shooting) {
            		ammo.get(i).draw(g);
            		ammo.get(i).x = (int) (x + cx + (ammo.get(i).x - (x + cx)) * Math.cos(angle) - (ammo.get(i).y - (y + cy)) * Math.sin(angle));
            		ammo.get(i).y = (int) (y + cy + (ammo.get(i).x - (x + cx)) * Math.sin(angle) + (ammo.get(i).y - (y + cy)) * Math.cos(angle));
             	}
//            	if(!ammo.get(i).shooting) {
//            		ammo.get(i).draw(g);
//            		ammo.get(i).x = (int) at.createTransformedShape(ammo.get(i).b).getBounds().getX() + x;
//            		ammo.get(i).y = (int) at.createTransformedShape(ammo.get(i).b).getBounds().getY() + y;
////            		ammo.get(i).b.setBounds(ammo.get(i).b.getBounds().x + x, ammo.get(i).b.getBounds().y + y, ammo.get(i).b.getBounds().width, ammo.get(i).b.getBounds().height);
//            		System.out.println(ammo.get(0).x + " " + ammo.get(0).y + " : " + ammo.get(0).b.getBounds().x + " " + ammo.get(0).b.getBounds().y);
//             	}
    		}
            
            g.drawImage(img, 0, 0, null);
            
		}
		else g.fillRect(x, y, width, height);
	}
	public void drawHitBox(Graphics2D g) {
		// draws hit box
		g.setColor(Color.WHITE);
		g.drawRect((int) b.getX(), (int) b.getY(), (int) b.getWidth(), (int) b.getHeight());
		g.drawRect((int) CH.topBound.getX(), (int) CH.topBound.getY(), (int) CH.topBound.getWidth(), (int) CH.topBound.getHeight());
		g.drawRect((int) CH.bottomBound.getX(), (int) CH.bottomBound.getY(), (int) CH.bottomBound.getWidth(), (int) CH.bottomBound.getHeight());
		g.drawRect((int) CH.rightBound.getX(), (int) CH.rightBound.getY(), (int) CH.rightBound.getWidth(), (int) CH.rightBound.getHeight());
		g.drawRect((int) CH.leftBound.getX(), (int) CH.leftBound.getY(), (int) CH.leftBound.getWidth(), (int) CH.leftBound.getHeight());
	}
}
