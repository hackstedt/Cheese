import java.awt.Point;
import java.util.LinkedList;
import java.util.Vector;


/**
 * The world contains the cheeses, crafts and balls.
 * 
 * @author Martin und Helge Böschen
 *
 */
public class World {

	private Vector<Cheese> cheeses;
	private Vector<Craft> crafts;
	private Vector<Ball> balls;

	/** Creates new World with specified size.
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 * @param craftCount
	 * @param ballCount
	 */
	public World(int left, int right, int bottom, int top, int craftCount, int ballCount) {
		// create main cheese
		LinkedList<Point> vertices = new LinkedList<Point>();
		vertices.add(new Point(left, bottom));
		vertices.add(new Point(right, bottom));
		vertices.add(new Point(right, top));
		vertices.add(new Point(left, top));
		cheeses = new Vector<Cheese>();
		cheeses.add(new Cheese(vertices, true, true));

		// create border cheeses
		vertices = new LinkedList<Point>();
		vertices.add(new Point(left, bottom));
		vertices.add(new Point(right, bottom));
		vertices.add(new Point(right, top));
		vertices.add(new Point(left, top));
		vertices.add(new Point(left, top + 5));
		vertices.add(new Point(right + 5, top + 5));
		vertices.add(new Point(right + 5, bottom - 5));
		vertices.add(new Point(left ,bottom - 5));
		cheeses.add(new Cheese(vertices, true, false));

		vertices = new LinkedList<Point>();
		vertices.add(new Point(left, bottom - 5));
		vertices.add(new Point(left, top + 5));
		vertices.add(new Point(left - 5, top + 5));
		vertices.add(new Point(left - 5, bottom - 5));
		cheeses.add(new Cheese(vertices, true, false));	

		crafts = new Vector<Craft>();
		int lives = 3;
		crafts.add(new Craft(left, bottom, lives));
		for (int i = 1; i < craftCount; ++i)
			crafts.add(new Craft(left, bottom + i*(top - bottom)/craftCount, lives));
		
		balls = new Vector<Ball>();
		balls.add(new Ball((left+right)/2, (bottom+top)/2, Direction.DownRight));
		for (int i = 1; i < craftCount; ++i)
			balls.add(new Ball((left+right)/2 + i*(right - (left+right)/2)/craftCount,
							   (bottom+top)/2 + i*(top - (bottom+top)/2)/craftCount, 
							   Direction.DownRight));	
	}

	public Vector<Craft> getCrafts() { return crafts; }
	public Vector<Ball> getBalls() { return balls; }
	public Vector<Cheese> getCheeses() { return cheeses; }	

	/**
	 *  Changes the world every timer tick
	 */
	public void move(){
		Edge e;
		// move the crafts
		for (Craft c : crafts) {
			c.move();
			// a craft can't enter a dead cheese
			if (insideDeadCheese(c))
				c.setPosition(c.getPrivousPosition());
			// check if c hits a cutting edge and dies
			for (Craft otherCraft : crafts) {
				if (otherCraft.isOnCuttingEdge(c.getPosition())) {
					c.kill();
					break;
				}					
			}
			// check if c is on a cheese border
			boolean isOnBorder = false;			
			for (Cheese ch : cheeses) {
				if (ch.isOnBorder(c.getPosition())) {
					isOnBorder = true;
					// check if we have to cut
					if (ch.isInside(c.getPrivousPosition())) {
						c.addCuttingEdgePoint();
						Vector<Cheese> newCheeses = ch.cut(c.getCuttingEdge());
						cheeses.remove(ch);
						cheeses.addAll(newCheeses);
						// check if the new cheeses contain balls
						for (Cheese nCh : newCheeses) {
							if (!containsBall(nCh)) {
								nCh.setIsAlive(false);
								c.givePoints(nCh.getArea());
							}
						} 
						break;
					}
				}
			}
			if (isOnBorder)
				c.clearCuttingEdge();
			c.addCuttingEdgePoint();
		}
		// move the balls
		for (Ball b : balls) {
			b.move();
			// maybe reflect
			for (Cheese ch : cheeses) {
				e = ch.getVertices().getEdge(b.getPosition());
				if (e != null) {
					b.setDirection(Direction.reflect(b.getDirection(), e, b.getPosition()));
					break;
				}
			}
			// maybe kill a player
			for (Craft c : crafts) {
				if (c.isOnCuttingEdge(b.getPosition()))
					c.kill();
			}
		}
	}

	/**
	 * @param c Craft
	 * @return true iff the Craft c is inside the open cheese
	 */
	private boolean insideDeadCheese(Craft c) {
		for (Cheese ch : cheeses) {
			if (!ch.isAlive() && ch.getVertices().isInside(c.getPosition()))
				return true;
		}
		return false;
	}
	
	/// true iff ch contains any ball
	private boolean containsBall(Cheese ch) {
		for (Ball b : balls) {
			if (ch.isInside(b.getPosition()))
				return true;
		}
		return false;
	}
	
	public void setCraftDirection(Direction dir, int craft) {
		Craft c = crafts.get(craft);
		if (dir != c.getDirection())
			c.setDirection(dir);
	}
}