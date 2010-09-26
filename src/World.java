import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;


public class World {
	
	// beteiligte "reale" Objekte
	public Cheese2 cheese;
	public Craft craft;
	// second player
	// flys
	
	// Hilfsobjekte
	public Point entryOrExitPoint;
	public LinkedList<Point> schnittkante;
	
	public World(){
		cheese = new Cheese2();
		craft = new Craft();
		
	}
	
	/**
	 * 
	 * @return Wheter the craft is inside the cheese or not.
	 */
	public boolean isCraftInside(){
		return (! (schnittkante == null));
	}
	
	/**
	 *  Changes the world every timer tick
	 */
	public void move(){
		// move craft
		craft.move();
		
		// check if craft just entered the cheese
		if(!isCraftInside() && cheese.isInside(craft.getPosition())){
			schnittkante = new LinkedList<Point>();
			schnittkante.add(craft.getPosition());
			System.out.println(craft.getPosition());
		}
		// check if craft just left the cheese
		if(isCraftInside() && !cheese.isInside(craft.getPosition())){
			System.out.println(craft.getPrivousPosition());
			// Cheese2 c=cheese.cut
			schnittkante = null;
		}
		
	}
	
	
	public void changeDirection(Direction d){
		craft.setDirection(d);
		if (isCraftInside())
			schnittkante.add(craft.getPosition());
	}
	
	
}


