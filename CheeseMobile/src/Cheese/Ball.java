package Cheese;

public class Ball {

	private Point pos;
    private Point previousPos;
	private Direction direction;

	public Ball(Point pos, Direction direction) {
		this.pos = pos;
        previousPos = pos;
		this.direction = direction;
	}

	public void move() {
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
	}

    public void setDirection(Direction direction) {
		this.direction = direction;
	}
	public Direction getDirection() { return direction; }
    public Point getPosition(){	return pos; }
    public Point getPrivousPosition(){ return previousPos; }
}
