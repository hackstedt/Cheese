import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.util.Iterator;
import java.util.LinkedList;


public class Cheese {
	Polygon p;
	Cheese(){
		p=new Polygon();
		p.addPoint(50, 50); 
		p.addPoint(50, 550);
		p.addPoint(750, 550);
		p.addPoint(750, 50);
	}
	
	public Polygon getPolygon(){
		return p;
	}
	
	public LinkedList<Point> getPoints(){
		LinkedList<Point> list = new LinkedList<Point>();
		for(int i = 0; i < p.npoints; i++){
			list.add(new Point(p.xpoints[i],p.ypoints[i]));
		}
		return list;
	}
	
	public LinkedList<Line2D> getLines(){
		LinkedList<Line2D> list = new LinkedList<Line2D>();
		for(int i = 1; i < p.npoints; i++){
			list.add(new Line2D.Double(p.xpoints[i-1],p.ypoints[i-1],p.xpoints[i],p.ypoints[i]));}
		return list;
	}
	
	public Point getEntryOrExitPoint(Point p, int direction){
		// direction 	1 oben
		//				2 rechts
		//				3 unten
		//				4 links
						
		LinkedList l = getLines();
		// filter horizontal or vertical lines out
		Iterator<Line2D> it = l.iterator();
		while (it.hasNext()) {
			Line2D s = it.next();
			// filter out vertical lines if direction is up/down
			if((direction==1  || direction ==3) && isHorizontal(s)==false ){
				it.remove();
			}
			// filter out horizontal lines if direction is left/right
			if((direction==2  || direction ==4) && isHorizontal(s) ){
				it.remove();
			}
			
		}
		
		// filter out lines im Rücken des Flugzeugs
		it = l.iterator();
		while (it.hasNext()) {
			Line2D s = it.next();
			if(direction==1  && s.getY1()<p.y ){
				it.remove();
			}
			// filter out vertical lines if direction is up/down
			if(direction==2  && s.getX1()<p.x ){
				it.remove();
			}
			if(direction==3  && s.getY1()>p.y ){
				it.remove();
			}
			// filter out vertical lines if direction is up/down
			if(direction==4  && s.getX1()>p.x ){
				it.remove();
			}
		}
	
		
		// sort lines nach Nähe zum Flugzeug
		
		// return ersten aus der Liste
		Line2D schnitt = (Line2D) l.getFirst();
		
		// schnittpunkt berechnen
		if(direction==2 || direction ==4){
			return (new Point((int) schnitt.getX1(),(int)p.getY()));
		}
		if(direction==3 || direction ==1){
			return (new Point((int)p.getX(),(int) schnitt.getY1()));
		}
		
		return (new Point(0,0));
	}
	
	private static boolean isHorizontal(Line2D l){
		return (l.getY1() ==l.getY2());
	}
}
