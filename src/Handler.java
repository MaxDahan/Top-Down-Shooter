import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedList;

public class Handler {

	protected LinkedList<GameObject> objects = new LinkedList<GameObject>();
	protected int map;
	protected Player player;
	protected Camera camera;
	
	// methods called every frame
	public void update() {
		for(int i = 0; i < objects.size(); i++) {
			boolean inX = camera.x - 10 <= player.x && camera.x + 10 + camera.GWIDTH >= player.x;
			boolean inY = camera.y - 10 <= player.y && camera.y + 10 + camera.GHEIGHT >= player.y;
			if(inX && inY) objects.get(i).update();
		}
	}
	public void draw(Graphics2D g) {
		for(int i = 0; i < objects.size(); i++) {
			boolean inX = camera.x - 10 <= player.x && camera.x + 10 + camera.GWIDTH >= player.x;
			boolean inY = camera.y - 10 <= player.y && camera.y + 10 + camera.GHEIGHT >= player.y;
			if(inX && inY) objects.get(i).draw(g);
		}
	}
	
	// sets the camera
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	// editing the objects list
	public void addObject(GameObject tempObject) {
		objects.add(tempObject);
	}
	public void addPlayer(GameObject tempObject) {
		player = (Player)tempObject;
		objects.add(tempObject);
	}
	public void removeObject(GameObject tempObject) {
		objects.remove(tempObject);
	}
}
