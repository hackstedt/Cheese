
import java.awt.Image;
import java.awt.Point;
import java.util.LinkedList;

import javax.swing.ImageIcon;

/**
 * @author Martin und Helge Boeschen
 *
 */
public class Craft {

    private Direction direction;
	private int x, y, previousX, previousY;
    private Image image;
    private PointList cuttingEdge;
    private int lives;
    private float score;

    public Craft(int x, int y, int lives) {
        ImageIcon ii = new ImageIcon(this.getClass().getResource("craft.png"));
        image = ii.getImage();
        this.x = x;
        this.y = y;
        previousX = x;
        previousY = y;
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
    
    public void clearCuttingEdge() {
        cuttingEdge = new PointList(new LinkedList<Point>(), false);
    	cuttingEdge.addVertexToEnd(getPosition());
    }
    
    public void move() {
    	if (lives < 0)
    		return;
    	previousX = x;
    	previousY = y;
    	if (direction != null){
	    	switch(direction){
		    	case Up: y -= 1; break;
		    	case Right: x += 1; break;
		    	case Down: y += 1; break;
		    	case Left: x -= 1; break;
		    	default: break;
	    	}
    	}
    }
    
    // the craft is replaced to his entry point in the cheese
	public void kill() { 
		--lives; 
		x = cuttingEdge.getFirst().x;
		y = cuttingEdge.getFirst().y;
		previousX = x;
		previousY = y;
		clearCuttingEdge();
	}
	
	public String toString() { return "Score: " + (int)(1000*score) + ", Lives: " + lives; }
    public void setPosition(Point p) { x = p.x; y = p.y; }
    public void setPreviousPosition(Point p) { previousX = p.x; previousY = p.y; }
    public void setDirection(Direction direction) { this.direction = direction; }
    public void addScore(float score) { this.score += score; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Point getPosition(){	return new Point(x,y); }
    public Point getPreviousPosition(){ return new Point(previousX,previousY); }
    public Image getImage() { return image; }
    public PointList getCuttingEdge() {	return cuttingEdge; }
	public Direction getDirection() { return direction; }
	public int getLives() { return lives; }
	public float getScore() { return score; }
}
