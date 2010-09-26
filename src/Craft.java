
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

public class Craft {

    private String imageFilename = "craft.png";
    private Direction direction;
	private int x, y;
    private Image image;
    
    /**
     * Creates craft at initial position and loads icon.
     */  
    public Craft() {
        ImageIcon ii = new ImageIcon(this.getClass().getResource(imageFilename));
        image = ii.getImage();
        x = 0;
        y = 0;
    }

    /**
     * Moves one step along direction.
     */
    public void move() {
    	if (direction!=null){
	    	switch(direction){
		    	case Up: y-=1; break;
		    	case Right: x+=1; break;
		    	case Down:y+=1; break;
		    	case Left: x-=1; break;
	    	}
    	}
    }
    
    // getter and setter    
    public void setDirection(Direction dir) { this.direction = dir; }
	public Direction getDirection() { return direction; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Point getPosition(){ return new Point(x,y); }
    public Image getImage() { return image; }  
}

