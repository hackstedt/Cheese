/**
 * The world contains the cheeses, crafts and balls.
 *
 * @author Martin und Helge Boeschen
 *
 */
public class World {

	private Cheese[] cheeses;
	private Craft[] crafts;
	private Ball[] balls;
	private int cheeseSize, maxCheeseSize;
	private int level, players;
	private int left, right, bottom, top;

	/** Creates new World with specified size.
	 * @param left - xposition of left border
	 * @param right - xposition of right border
	 * @param bottom - yposition of down border
	 * @param top - yposition of upper border
	 * @param craftCount - number of crafts
	 * @param ballCount  - number of balls
	 */
	public World(int left, int right, int bottom, int top, int players, int level) {
		this.left = left; this.right = right;
		this.bottom = bottom; this.top = top;
		this.players = players;
		crafts = new Craft[players];
		int lives = 3;

		// create crafts
		for (int i = 0; i < players; ++i)
			crafts[i] = new Craft(new Point(0, 0), lives);
		initialize(level);
	}

	private void initialize(int level) {
		this.level = level;
		cheeseSize = 1;
		maxCheeseSize = 100;
		cheeses = new Cheese[cheeseSize];
		Point[] points = {new Point(left, bottom),
								 new Point(right, bottom),
								 new Point(right, top),
								 new Point(left, top) };
		cheeses[0] = new Cheese(points, 4, true, true);
		// place crafts equidistantly on the left edge
		for (int i = 0; i < players; ++i) {
			crafts[i].setPosition(new Point(left, bottom + i*(top - bottom)/players));
			crafts[i].setPreviousPosition(crafts[i].getPosition());
			++i;
		}
		balls = new Ball[level];
		for (int i = 0; i < level; ++i)
			balls[i] = (new Ball(new Point((left+right)/2 + i*(right - (left+right)/2)/level,
							   (bottom+top)/2 + i*(top - (bottom+top)/2)/level),
							   Direction.DOWNRIGHT));
	}

	/**
	 *  Changes the world every timer tick
	 */
	public void move(){
		Edge e;
		// move the crafts
		for (int i = 0; i < players; ++i) {
			crafts[i].move();
			// if moving moved the craft out of the world, set it on the
			// previous position
			if (!insideWorld(crafts[i].getPosition())) {
				crafts[i].setPosition(crafts[i].getPreviousPosition());
				crafts[i].setDirection(null);
				break;
			}
			// check if c hits a cutting edge and dies
			for (int j = 0; j < players; ++j) {
				if (crafts[j].isOnCuttingEdge(crafts[i].getPosition())) {
					crafts[i].kill();
					break;
				}
			}
			// check if c is on a cheese border
			boolean isOnBorder = false;
			for (int j = 0; j < cheeseSize; ++j) {
				if (cheeses[j].isOnBorder(crafts[i].getPosition())) {
					isOnBorder = true;
					// check if we have to cut
					if (cheeses[j].isAlive() && cheeses[j].isInside(crafts[i].getPreviousPosition())) {
						crafts[i].addCuttingEdgePoint();
						Cheese[] newCheeses = cheeses[j].cut(crafts[i].getCuttingEdge());
						cheeses[j] = newCheeses[0];
						if (cheeseSize + 1 >= maxCheeseSize)
							enlarge();
						cheeses[cheeseSize] = newCheeses[1];
						cheeseSize++;
						if (!containsBall(newCheeses[0])) {
							newCheeses[0].setIsAlive(false);
							crafts[i].addScore(newCheeses[0].getArea() / getMaxPoints());
						}
						if (!containsBall(newCheeses[1])) {
							newCheeses[1].setIsAlive(false);
							crafts[i].addScore(newCheeses[1].getArea() / getMaxPoints());
						}
						break;
					}
				}
			}
			if (isOnBorder || insideDeadCheese(crafts[i]))
				crafts[i].clearCuttingEdge();
			else
				crafts[i].addCuttingEdgePoint();
		}
		// move the balls
		for (int j = 0; j < level; ++j) {
			balls[j].move();
			// maybe reflect
			for (int i = 0; i < cheeseSize; ++i) {
				e = cheeses[i].getVertices().getEdge(balls[j].getPosition());
				if (e != null) {
					balls[j].setDirection(Direction.reflect(balls[j].getDirection(), e, balls[j].getPosition()));
					break;
				}
			}
			// maybe kill a player
			for (int i = 0; i < players; ++i) {
				if (crafts[i].isOnCuttingEdge(balls[j].getPosition()))
					crafts[i].kill();
			}
		}
       float allScores = 0;
       for (int i = 0; i < players; ++i)
    	   allScores += crafts[i].getScore();
       if (allScores > 0.9 * level * getMaxPoints())
    	   initialize(level + 1);
	}

	private void enlarge() {
		maxCheeseSize = maxCheeseSize * 2;
		Cheese[] tmp = cheeses;
		cheeses = new Cheese[maxCheeseSize];
		for (int i = 0; i < cheeseSize; ++i)
			cheeses[i] = tmp[i];
	}

	/**
	 * @param c Craft
	 * @return true iff the Craft c is inside the open cheese
	 */
	private boolean insideDeadCheese(Craft c) {
		for (int i = 0; i < cheeseSize; ++i) {
			if (!cheeses[i].isAlive() && cheeses[i].getVertices().isInside(c.getPosition()))
				return true;
		}
		return false;
	}

	/// true iff ch contains any ball
	private boolean containsBall(Cheese ch) {
		for (int i = 0; i < level; ++i) {
			if (ch.isInside(balls[i].getPosition()))
				return true;
		}
		return false;
	}

	public void setCraftDirection(Direction dir, int craft) {
		Craft c = crafts[craft];
		if (dir != c.getDirection())
			c.setDirection(dir);
	}

	private boolean insideWorld(Point p) {
		return p.x >= left && p.x <= right && p.y >= bottom && p.y <= top;
	}

	public Craft[] getCrafts() { return crafts; }
	public Ball[] getBalls() { return balls; }
	public Cheese[] getCheeses() { return cheeses; }
	public int getLevel() { return level; }
	public int getPlayerCount() { return players; }
	public int getCheeseCount() { return cheeseSize; }
	public float getMaxPoints() { return (float)((right - left) * (top - bottom)); }
}