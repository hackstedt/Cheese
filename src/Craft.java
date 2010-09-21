
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Craft {

    private String craft = "craft.png";

    private Direction direction;
    public void setDirection(Direction direction) {
		this.direction = direction;
	}
	public Direction getDirection() {
		return direction;
	}


	private int x;
    private int y;
    private Image image;
    

    public Craft() {
        ImageIcon ii = new ImageIcon(this.getClass().getResource(craft));
        image = ii.getImage();
        x = 0;
        y = 0;
    }


    public void move() {
    	switch(direction){
    	case Up: y-=1; break;
    	case Right: x=+1; break;
    	case Down:y+=1; break;
    	case Left: x-=1; break;
    	}
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
   

    public Image getImage() {
        return image;
    }

   
}

