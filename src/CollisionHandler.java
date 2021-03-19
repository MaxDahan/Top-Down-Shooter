import java.awt.Rectangle;

public class CollisionHandler {
	protected Rectangle topBound, bottomBound, rightBound, leftBound;
	
	public CollisionHandler() {
		this.topBound = new Rectangle();
		this.bottomBound = new Rectangle();
		this.rightBound = new Rectangle();
		this.leftBound = new Rectangle();
	}
}
