import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;


public class World {
	
	// beteiligte "reale" Objekte
	// TODO: das sollte man eher nicht public machen. Es darf doch nur diese
	// Klasse die beiden Objekte aendern. In C++ wuerde man dann konstante Referenzen
	// als getter Methoden einbauen, sowas geht sicher auch in Java.
	public Cheese cheese;
	public Craft craft;
	// second player
	// flys
	
	// Hilfsobjekte
	public Point entryOrExitPoint;
	public LinkedList<Point> schnittkante;
	
	public World(){
		cheese = new Cheese();
		craft = new Craft();
	}
	
	/**
	 * 
	 * @return Whether the craft is inside the cheese or not.
	 */
	public boolean isCraftInside(){
		return (! (schnittkante == null));
	}
	
	/**
	 *  Changes the world every timer tick
	 */
	// TODO: Umbenennen, die Welt bewegt sich ja nicht.
	public void move(){
		// move craft
		craft.move();
		
		// check if craft just entered the cheese
		if(!isCraftInside() && cheese.isInside(craft.getPosition())){
			schnittkante = new LinkedList<Point>();
			schnittkante.add(entryOrExitPoint);
		}
		// check if craft just left the cheese
		if(isCraftInside() && !cheese.isInside(craft.getPosition())){
			// Käsezerteilungsalgoritmus
			schnittkante = null;
		}
		
	}
	
	/**
	 * Calculates the Entry or Exit point
	 * @return The Entrypoint into the cheese if craft is outside,
	 * the Exitpoint from the cheese if craft is inside or NULL if
	 * craft is outside and there is no entrypoint.
	 */
	public Point calcEntryOrExitPoint(){
		// construct flight route
		Line2D.Double flugroute;
		Direction d = craft.getDirection();
		int craftx = craft.getX();
		int crafty = craft.getY();
		if(d.equals(Direction.Right)){
			flugroute = new Line2D.Double(craftx,crafty, 800, craft.getY());
		}
		else if(d.equals(Direction.Down)){
			flugroute = new Line2D.Double(craftx,crafty, craftx, 600);
		}
		else if(d.equals(Direction.Left)){
			flugroute = new Line2D.Double(craftx,crafty, 0, crafty);
		}
		else if(d.equals(Direction.Up)){
			flugroute = new Line2D.Double(craftx,crafty, craftx, 0);
		}
		else 
			return null;
		
		// get lines from the cheese
		LinkedList<Line2D> lines = cheese.getLines();
		//filter out lines intersecting flight route
		Iterator<Line2D> it = lines.iterator();
		while (it.hasNext()) {
			Line2D s = it.next();
			if (! s.intersectsLine(flugroute))
				it.remove();
		}
		
		// no intersection 
		if (lines.isEmpty())
			return null;
		
		// intersection -> sortieren und nächsten zurüchgeben
			
		Line2D schnittline = lines.getFirst();
		if (d.equals(Direction.Right) || d.equals(Direction.Left))
			return new Point((int) schnittline.getX1(),crafty);
		else if (d.equals(Direction.Up) || d.equals(Direction.Down))
			return new Point(craftx,(int) schnittline.getY1());
		else {
			System.out.println("ERROR");
			return null;
		}
			
		
	}
	
	public void changeDirection(Direction d){
		craft.setDirection(d);
		entryOrExitPoint = calcEntryOrExitPoint();
		if (isCraftInside())
			schnittkante.add(craft.getPosition());
	}
	
	
}


