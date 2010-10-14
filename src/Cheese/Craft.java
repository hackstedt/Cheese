/**
 * @author Martin und Helge Boeschen
 *
 */
public class Craft {

    private Direction direction;
	private Point pos, previousPos;
    private PointList cuttingEdge;
    private int lives;
    private float score;

    public Craft(Point pos, int lives) {
        this.pos = pos;
        previousPos = pos;
        clearCuttingEdge();
    	score = 0;
    	this.lives = lives;
    }

    public void addCuttingEdgePoint() {
    	cuttingEdge.addVertexToEnd(getPosition());
    	deleteRedundantCuttingEdgePoints();
    }

    public void deleteRedundantCuttingEdgePoints() {
    	cuttingEdge.deleteRedundantPoints();
    }

    public boolean isOnCuttingEdge(Point p) {
    	return cuttingEdge.isOnBorder(p);
    }

    final public void clearCuttingEdge() {
        cuttingEdge = new PointList(false);
    	cuttingEdge.addVertexToEnd(getPosition());
    }

    public void move() {
    	if (lives < 0)
    		return;
    	previousPos = pos;
		if (direction == Direction.UP)
			pos.y -= 1;
		else if (direction == Direction.RIGHT)
			pos.x += 1;
		else if (direction == Direction.DOWN)
			pos.y += 1;
		else if (direction == Direction.LEFT)
			pos.x -= 1;
    }

    // the craft is replaced to his entry point in the cheese
	public void kill() {
		--lives;
		pos.x = cuttingEdge.getFirst().x;
		pos.y = cuttingEdge.getFirst().y;
		previousPos = pos;
		clearCuttingEdge();
	}

	@Override
	public String toString() { return "Score: " + (int)(1000*score)
			+ ", Lives: " + lives; }
    public void setPosition(Point p) { pos = p; }
    public void setPreviousPosition(Point p) { previousPos = p; }
    public void setDirection(Direction dir) { this.direction = dir; }
    public void addScore(float score) { this.score += score; }
    public Point getPosition(){	return pos; }
    public Point getPreviousPosition(){ return previousPos; }
    public PointList getCuttingEdge() {	return cuttingEdge; }
	public Direction getDirection() { return direction; }
	public int getLives() { return lives; }
	public float getScore() { return score; }
}
