import java.awt.Point;
import java.util.LinkedList;


public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Cheese2 c= new Cheese2();
		Point p1 = new Point(0,0);
		Point p2 = new Point(0,100);
		Point p3 = new Point(0,50);
		System.out.println("Test der Funktion isBetween");
		System.out.println(Cheese2.isBetween(p1, p2, p3));
		
		
		System.out.println("Test der Funktion getNachfolger()");
		Point Pk1 =new Point(50, 50); 
		Point Pk2 =new Point(750, 50);
		Point Pk3 =new Point(750, 550);
		Point Pk4 =new Point(50, 550);
		Point testpoint = new Point(500,50);
		LinkedList<Point> points = new LinkedList<Point>();
		points.add(Pk1); 
		points.add(Pk2);
		points.add(Pk3);
		points.add(Pk4);
		System.out.println(c.getNachfolger(testpoint));
		System.out.println(c.getVorgaenger(testpoint));
		
		System.out.println("Test wenn Funktion nicht auf dem Polygon liegt");
		System.out.println(c.getVorgaenger(new Point(0,0)));
		
		
		System.out.println("Test der Randfälle");
		Point testpunkt2 = new Point(50,100);
		
	    //	System.out.println(c.getNachfolger(testpunkt2));
		System.out.println(c.getVorgaenger(testpunkt2));	
		
		System.out.println("Test der cut-Methode");
		LinkedList<Point> schnittkante=new LinkedList<Point>();
		schnittkante.add(new Point(100,50));
		schnittkante.add(new Point(100,550));
		System.out.println( c.cut(schnittkante) );

	}

}
