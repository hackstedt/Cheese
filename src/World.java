import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.LinkedList;


public class World {
	
	// beteiligte "reale" Objekte
	public Cheese cheese;
	public Craft craft;
	// second player
	// flys
	
	// Hilfsobjekte
	private Point entryOrExitPoint;
	private LinkedList<Point> Schnittkante;
	
	public World(){
		cheese = new Cheese();
		craft = new Craft();
		
	}
	
	/**
	 *  Changes the world every timer tick
	 */
	public void move(){
		// move craft
		craft.move();
		
		// check if craft entered the cheese
			// create schniitkanten objekt
		// check if craft left the cheese
			// cut cheese
		
	}
	
	/**
	 * Calculates the Entry or Exit point
	 * @return The Entrypoint into the cheese if craft is outside,
	 * the Exitpoint from the cheese if craft is inside or NULL if
	 * craft is outside and there is no entrypoint.
	 */
	public Point calcEntryOrExitPoint(){
		// construct flight route
		
		// get lines from the cheese
		
		//filter out lines intersecting flight route
		
		// no intersection -> return NULL
		return new Point(0,0);
		
		// intersection -> sortieren und nächsten zurüchgeben
	}
	
	public void changeDirection(Direction d){
		craft.setDirection(d);
	}
	
}


