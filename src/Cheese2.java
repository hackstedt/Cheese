import java.awt.Point;
import java.awt.Polygon;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;


public class Cheese2 {
	private LinkedList<Point> points;
	
	public Cheese2(){
		points = new LinkedList<Point>();
		points.add(new Point(50, 50)); 
		points.add(new Point(750, 50));
		points.add(new Point(750, 550));
		points.add(new Point(50, 550));
	}
	
	
	public Cheese2(LinkedList<Point> points) {
		super();
		this.points = points;
	}


	public String toString() {
		return "Cheese2 [points=" + points + "]";
	}


	/**
	 * 
	 * @return The surrounding points of the cheese in clockwise order.
	 */
	public LinkedList<Point> getPoints(){
		return points;
	}
	
	public Polygon getPolygon(){
		ListIterator<Point> it= points.listIterator(0);
		Point p; 
		int size = points.size();
		int[] xpoints = new int[size];
		int[] ypoints = new int[size];
		int i=0;
		while(it.hasNext()){
			p= it.next();
			xpoints[i]=p.x;
			ypoints[i]=p.y;
			i++;
		}
		return new Polygon(xpoints,ypoints,size);
	}
	
	/**
	 * 
	 * @param p A point.
	 * @return Returns true if the point is on the border
	 */
	public boolean isBorder(Point p){
		return true;
	}
	
	public boolean isInside(Point p){
		return (getPolygon().contains(p));
	}
	
	public double area(){
		return 0;
	}
	
	/**Border
	 * 
	 * @param pls The schnittkante
	 */
	public Cheese2 cut(LinkedList<Point> pls){
		LinkedList<Point> newCheese;
		Point startpunkt =pls.getFirst();
		Point endpunkt = pls.getLast();
		Point vorgaenger = getVorgaenger(startpunkt);
		Point Nachfolger = getNachfolger(endpunkt);
		Point aktuellerPunkt=null;
		newCheese = (LinkedList<Point>) pls.clone();
		int index = points.indexOf(Nachfolger);
		ListIterator<Point> it =points.listIterator(index);
		while(it.hasNext() && aktuellerPunkt != vorgaenger){
			aktuellerPunkt =it.next();
			newCheese.add(aktuellerPunkt);
		}
		newCheese.add(vorgaenger);
		
		// Solange Punkte adden bis man zum Vorgäber des Startpunkts gelangt ist
		
		// reverse List and do the same
		
		return (new Cheese2(newCheese));
	}
	


	public Point getVorgaenger(Point p){
		Point pointa;
		Point pointb;
		Iterator<Point> it = points.iterator();
		pointa = it.next();
		while (it.hasNext()) {
			pointb = it.next();
			if(isBetween(pointa,pointb,p))
				return pointa;
			pointa=pointb;
		}
		// Sonderfall: Punkt zwischen letzen und ersten Punkt in der list
		if(isBetween(points.getFirst(),points.getLast(),p))
			return points.getLast();
		// test whether p lies on the line between pointa and pointbisBetween
		
		// Fehler
		return new Point(0,0);
	}
	
	public Point getNachfolger(Point p){
		Point p1= getVorgaenger(p);
		int index = points.indexOf(p1);
		// Noch nicht beachtet, dass inex zu groß sein kann
		return points.get((index+1) % points.size());
	}
	
	// p3 is the point to be checked
	public static boolean isBetween(Point p1, Point p2, Point p3){
		boolean allOnVerticalLine=p1.x == p2.x && p1.x==p3.x;
		boolean verticalBetween = (p1.y<=p3.y && p3.y<=p2.y) || (p2.y<=p3.y && p3.y<=p1.y);
		boolean allOnHorizontalLine=p1.y == p2.y && p1.y==p3.y;
		boolean horizontalBetween = (p1.x<=p3.x && p3.x<=p2.x) || (p2.x<=p3.x && p3.x<=p1.x);
		return (allOnVerticalLine && verticalBetween) || (allOnHorizontalLine && horizontalBetween);
		
	}

}
