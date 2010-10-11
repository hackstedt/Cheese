import java.awt.Point;

/**
 * Class encapsulation an edge.
 *
 */
public class Edge {

	public Point start, end;
	
	public Edge(Point start, Point end) {
		this.start = start;
		this.end = end;
	}
	
	public boolean horizontal() {
		return start.y == end.y;
	}
	
	public int getLength() {
		if (horizontal())
			return Math.abs(start.x - end.x);
		else
			return Math.abs(start.y - end.y);
	}
	
	public static boolean isOnEdge(Point p, Edge e) {
		if (  e.horizontal() 
		   && p.y == e.start.y
		   && Math.min(e.start.x, e.end.x) <= p.x
		   && Math.max(e.start.x, e.end.x) >= p.x
		   && p.x != e.end.x )
			return true;
		if (  !e.horizontal() 
		   && p.x == e.start.x
	   	   && Math.min(e.start.y, e.end.y) <= p.y
		   && Math.max(e.start.y, e.end.y) >= p.y
		   && p.y != e.end.y )
			return true;
		else
			return false;
	}
}
