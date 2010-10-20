package Cheese;

import java.awt.Polygon;

/**
 * Class encapsulating a list of points describing a polygon (cyclic) or a
 * line strip (not cyclic).
 * All edges must be either horizontal or vertical.
 *
 */
public class PointList {

	public Point[] points;
	public Edge[] edges;
	public int size, maxSize, edgeSize;
	private boolean cyclic;

	public PointList(Point[] points, int pointCount, boolean cyclic) {
		size = pointCount;
		maxSize = 2*pointCount;
		this.points = new Point[maxSize];
		for (int i = 0; i < size; ++i)
			this.points[i] = points[i];
		this.cyclic = cyclic;
		createEdges();
	}

	public PointList(boolean cyclic) {
		size = 0;
		maxSize = 16;
		points = new Point[maxSize];
		this.cyclic = cyclic;
		createEdges();
	}

	@Override
	public String toString() {
		String toReturn = "[";
		for (int i = 0; i < size; ++i)
			toReturn = toReturn.concat(points[i].x + "," + points[i].y + " ; ");
		toReturn = toReturn.concat("]");
		return toReturn;
	}

	private void enlargeArray() {
		maxSize = maxSize * 2;
		Point[] tmp = points;
		points = new Point[maxSize];
		for (int i = 0; i < size; ++i)
			points[i] = tmp[i];
	}

	/// this method create an edge list from the point list
	private void createEdges() {
		edges = new Edge[maxSize];
		if (size < 2)
			return;

		for (int i = 0; i < size - 1; ++i)
			edges[i] = new Edge(points[i], points[i+1]);
		if (cyclic) {
			edges[size - 1] = new Edge(points[size - 1], points[0]);
			edgeSize = size;
		}
		else
			edgeSize = size - 1;
	}

	public void addVertexToEnd(Point p) {
		if (points == null)
			points = new Point[maxSize];
		if (size + 1 >= maxSize)
			enlargeArray();
		points[size] = p;
		++size;
		createEdges();
	}

	/// searches the edge on which p lies and adds p to the right position
	public void addVertex(Point p) {
		if (size + 1 >= maxSize)
			enlargeArray();
		Edge e = getEdge(p);
		if (e != null) {
			int j = size - 1;
			while(e.start != points[j]) {
				points[j+1] = points[j];
				j--;
			}
			points[j+1] = p;
			++size;
			createEdges();
		}
	}

	/// return the edge that follows to e
	public Edge nextEdge(Edge e) {
		int j = 0;
		while (edges[j] != e)
			++j;
		++j;
		j = j % edgeSize;
		return edges[j];
	}

	/// reverses the point order
	public PointList reverse() {
		Point[] toReturn = new Point[size];
		for (int i = 0; i < size; ++i)
			toReturn[size - 1 - i] = points[i];
		return new PointList(toReturn, size, cyclic);
	}

	public void deleteDoublePoints() {
		for (int i = 0; i < size; ++i) {
			for (int j = i + 1; j < size; ++j) {
				while (j < size && points[i].equals(points[j]))
					remove(j);
			}
		}
	}

	public void remove(int i) {
		for (; i < size - 1; ++i)
			points[i] = points[i+1];
		--size;
	}

	public void deleteRedundantPoints() {
		if (size < 3)
			deleteDoublePoints();
		else {
			int[] toRemove = new int[size];
			int remSize = 0;

			for (int i = 1; i < size - 1; ++i) {
				Point a = points[i-1];
				Point b = points[i];
				Point c = points[i + 1];
				if ((a.x == b.x && b.x == c.x) || (a.y == b.y && b.y == c.y)) {
					toRemove[remSize] = i;
					++remSize;
				}
			}
			for (int i = remSize - 1; i >= 0; --i)
				remove(toRemove[i]);
		}
	}

	public Point get(int i) { return points[i]; }
	public Point getFirst() { return points[0]; }
	public Point getLast() { return points[size - 1]; }

	/// true iff p is inside the open polygon
	public boolean isInside(Point p) {
		if (!cyclic || isOnBorder(p))
			return false;
		boolean inside = false;
		for (int i = 0; i < edgeSize; ++i) {
			Point q = edges[i].start;
			Point r = edges[i].end;
			if ( ((r.y > p.y) != (q.y > p.y)) &&
					(p.x < (q.x - r.x) * (p.y-r.y) / (q.y-r.y) + r.x) )
				inside = !inside;
		}
		return inside;
	}

	public boolean isOnBorder(Point p) {
		return !(getEdge(p) == null);
	}

	/// return the edge on which p lies
	public Edge getEdge(Point p) {
		for (int i = 0; i < edgeSize; ++i) {
			if (Edge.isOnEdge(p, edges[i]))
				return edges[i];
		}
		return null;
	}

	public float getArea() {
		float area = 0;
		int j;
		if (cyclic) {
			for (int i = 0; i < size; ++i) {
				j = (i+1) % size;
				area += points[i].x * points[j].y - points[j].x * points[i].y;
			}
		}
		if (area < 0)
			area = - area;
		area = area / 2;
		return area;
	}

	public void setCyclic(boolean cyclic) { this.cyclic = cyclic; }

	public Polygon getPolygon() {
		int[] xpoints = new int[size];
		int[] ypoints = new int[size];
		for (int i = 0; i < size; ++i) {
			xpoints[i] = points[i].x;
			ypoints[i] = points[i].y;
		}
		return new Polygon(xpoints, ypoints, size);
	}

}
