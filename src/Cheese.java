import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.util.Iterator;
import java.util.LinkedList;


public class Cheese {
	Polygon polygon;
	
	Cheese(){
		polygon=new Polygon();
		polygon.addPoint(50, 50); 
		polygon.addPoint(50, 550);
		polygon.addPoint(750, 550);
		polygon.addPoint(750, 50);
	}
	
	public Polygon getPolygon(){
		return polygon;
	}
	
	public boolean isInside(Point p){
		return polygon.contains(p);
	}
	
	public LinkedList<Point> getPoints(){
		LinkedList<Point> list = new LinkedList<Point>();
		for(int i = 0; i < polygon.npoints; i++){
			list.add(new Point(polygon.xpoints[i],polygon.ypoints[i]));
		}
		return list;
	}
	
	public LinkedList<Line2D> getLines(){
		LinkedList<Line2D> list = new LinkedList<Line2D>();
		for(int i = 1; i < polygon.npoints; i++){
			list.add(new Line2D.Double(polygon.xpoints[i-1],polygon.ypoints[i-1],polygon.xpoints[i],polygon.ypoints[i]));}
		return list;
	}
	
	
	private static boolean isHorizontal(Line2D l){
		return (l.getY1() ==l.getY2());
	}
}
