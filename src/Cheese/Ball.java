public class Ball {

	private Point pos;
    private Point previousPos;
	private Direction direction;
	private Cheese cheese;

	public Ball(Point pos, Direction direction) {
		this.pos = pos;
        previousPos = pos;
		this.direction = direction;
	}

	public void move() {
		Point tmp = previousPos;
    	previousPos = pos;
		if (direction == Direction.UPRIGHT) {
			pos.x+=1; pos.y+=1;
		}
		else if (direction == Direction.UPLEFT) {
			pos.x-=1; pos.y+=1;
		}
		else if (direction == Direction.DOWNRIGHT) {
			pos.x+=1; pos.y-=1;
		}
		else if (direction == Direction.DOWNLEFT) {
			pos.x-=1; pos.y-=1;
		}
		// safety check
		if (cheese != null && !cheese.isInside(pos) && !cheese.isOnBorder(pos)) {
			pos = previousPos;
			previousPos = tmp;
		}
	}

	public void setCheese(Cheese c) { cheese = c; }
	public Cheese getCheese() { return cheese; }
    public void setDirection(Direction dir) { this.direction = dir;	}
	public Direction getDirection() { return direction; }
    public Point getPosition(){	return pos; }
    public Point getPrivousPosition(){ return previousPos; }
}
