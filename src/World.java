import java.awt.Point;
import java.util.LinkedList;
import java.util.Vector;


/**
 * The world contains the cheeses, crafts and balls.
 * 
 * @author Martin und Helge Boeschen
 *
 */
public class World {

	private Vector<Cheese> cheeses;
	private Vector<Craft> crafts;
	private Vector<Ball> balls;
	private int left, right, bottom, top;

	/** Creates new World with specified size.
	 * @param left - xposition of left border
	 * @param right - xposition of right border
	 * @param bottom - yposition of down border
	 * @param top - yposition of upper border
	 * @param craftCount - number of crafts
	 * @param ballCount  - number of balls
	 */
	public World(int left, int right, int bottom, int top, int craftCount, int level) {
		this.left = left; this.right = right; this.bottom = bottom; this.top = top;

		crafts = new Vector<Craft>();
		int lives = 3;
		
		// place crafts equidistantly on the left edge
		for (int i = 0; i < craftCount; ++i)
			crafts.add(new Craft(left, bottom + i*(top - bottom)/craftCount, lives));
		initialize(crafts, level);
	}

	private void initialize(Vector<Craft> crafts, int level) {
		// create main cheese
		LinkedList<Point> vertices = new LinkedList<Point>();
		vertices.add(new Point(left, bottom));
		vertices.add(new Point(right, bottom));
		vertices.add(new Point(right, top));
		vertices.add(new Point(left, top));
		cheeses = new Vector<Cheese>();
		cheeses.add(new Cheese(vertices, true, true));
		this.crafts = crafts;
		balls = new Vector<Ball>();
		for (int i = 0; i < level; ++i)
			balls.add(new Ball((left+right)/2 + i*(right - (left+right)/2)/level,
							   (bottom+top)/2 + i*(top - (bottom+top)/2)/level, 
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
			// if moving moved the craft out of the world, set it on the previous position
			if (!insideWorld(c.getPosition())) {
				c.setPosition(c.getPreviousPosition());
				c.setDirection(null);
				break;
			}
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
					if (ch.isInside(c.getPreviousPosition())) {
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
	
	private boolean insideWorld(Point p) {
		return p.x >= left && p.x <= right && p.y >= bottom && p.y <= top;
	}
	
	public int getMaxPoints() {
		return (right - left) * (top - bottom);
	}
}