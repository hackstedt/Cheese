import java.awt.Point;
import java.awt.Polygon;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Class encapsulating a list of points describing a polygon (cyclic) or a line strip (not cyclic).
 * All edges must be either horizontal or vertical.
 *
 */
public class PointList {

	public LinkedList<Point> points;
	public LinkedList<Edge> edges;
	private boolean cyclic;
	
	public PointList(LinkedList<Point> points, boolean cyclic) {
		this.points = new LinkedList<Point>();
		for (Point p : points)
			this.points.add(p);
		this.cyclic = cyclic;
		createEdges();
	}
	
	public String toString() {
		String toReturn = "[";
		for (Point p : points)
			toReturn = toReturn.concat(p.x + "," + p.y + " ; ");
		toReturn = toReturn.concat("]");
		return toReturn;
	}
	
	/// this method create an edge list from the point list
	private void createEdges() {
		edges = new LinkedList<Edge>();
		if (points.size() < 2)
			return;
		Iterator<Point> it = points.iterator();
		Point start = it.next();
		Point p = start;
		Point q = null;
		while (it.hasNext()) {
			q = it.next();			
			edges.add(new Edge(p, q));
			p = q;
		}
		if (cyclic)
			edges.add(new Edge(p, start));		
	}
	
	public void addVertexToEnd(Point p) {
		if (points == null)
			points = new LinkedList<Point>();
		points.add(p);
		createEdges();
	}
	
	/// searches the edge on which p lies and adds p to the right position
	public void addVertex(Point p) {
		for (Edge e : edges) {
			if (Edge.isOnEdge(p, e)) {
				points.add(points.indexOf(e.end), p);
				createEdges();
				break;
			}
		}
	}
	
	/// return the edge that follows to e
	public Edge nextEdge(Edge e) {
		return edges.get((edges.indexOf(e) + 1) % edges.size());
	}
	
	/// reverses the point order
	public PointList reverse() {
		LinkedList<Point> toReturn = new LinkedList<Point>();
		for (Point p : points)
			toReturn.addFirst(p);
		return new PointList(toReturn, cyclic);
	}
	
	public void setCyclic(boolean cyclic) { this.cyclic = cyclic; }
	
	public void deleteDoublePoints() {
		for (int i = 0; i < points.size(); ++i) {
			for (int j = i + 1; j < points.size(); ++j) {
				while (j < points.size() && points.get(i).equals(points.get(j)))
					points.remove(j);					
			}
		}
	}
	
	public void deleteRedundantPoints() {
		if (points.size() < 3)
			deleteDoublePoints();
		else {
			Iterator<Point> it = points.iterator();
			LinkedList<Point> toRemove = new LinkedList<Point>();
			Point a = it.next();
			Point b = it.next();
			Point c = null;
			while (it.hasNext()) {
				c = it.next();
				if ((a.x == b.x && b.x == c.x) || (a.y == b.y && b.y == c.y))
					toRemove.add(b);
				a = b;
				b = c;
			}
			points.removeAll(toRemove);
		}
	}
	
	public Point get(int i) { return points.get(i); }
	public Point getFirst() { return points.getFirst(); }
	public Point getLast() { return points.getLast(); }	
	
	/// true iff p is inside the open polygon
	public boolean isInside(Point p) {
		if (!cyclic)
			return false;
		if (!getPolygon().contains(p))
			return false;
		for (Edge e : edges) {
			if (Edge.isOnEdge(p, e))
				return false;
		}
		return true;
	}
	
	public boolean isOnBorder(Point p) {
		return !(getEdge(p) == null);
	}
	
	/// return the edge on which p lies
	public Edge getEdge(Point p) {
		for (Edge e : edges) {
			if (Edge.isOnEdge(p, e))
				return e;
		}
		return null;
	}
	
	public int getLength() {
		int length = 0;
		for (Edge e : edges)
			length += e.getLength();
		return length;
	}
	public Polygon getPolygon() {
		int[] xpoints = new int[points.size()];
		int[] ypoints = new int[points.size()];
		int i = 0;
		for (Point p : points) {
			xpoints[i] = p.x;
			ypoints[i] = p.y;
			++i;
		}
		return new Polygon(xpoints, ypoints, points.size());
	}

}
