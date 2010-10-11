import java.awt.Point;

public class Ball {

	private int x,y;
    private int previousX, previousY;
	private Direction direction;
	
	public Ball(int x, int y, Direction direction) {
		this.x = x;
		this.y = y;
        previousX = x;
        previousY = y;
		this.direction = direction;
	}
	
	public void move() {
    	previousX = x;
    	previousY = y;
    	if (direction!=null){
	    	switch(direction){
		    	case UpRight: 
		    		x+=1; y+=1; 
		    		break;
		    	case UpLeft: 
		    		x-=1; y+=1; 
		    		break;
		    	case DownRight:
		    		x+=1; y-=1; 
		    		break;
		    	case DownLeft: 
		    		x-=1; y-=1; 
		    		break;
		    	default: break;
	    	}
    	}
	}
	
    public void setDirection(Direction direction) {
		this.direction = direction;
	}
	public Direction getDirection() { return direction; }

    public int getX() { return x; }

    public int getY() { return y; }
    
    public Point getPosition(){	return new Point(x,y); }
    
    public Point getPrivousPosition(){ return new Point(previousX,previousY); }
}
