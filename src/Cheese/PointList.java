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
		for (int i = 0; i < edgeSize; ++i) {
			if (Edge.isOnEdge(p, edges[i])) {
				int j = size-1;
				while(edges[j-1].end != points[j]) {
					points[j+1] = points[j];
					j--;
				}
				points[j+1] = p;
				createEdges();
				break;
			}
		}
	}

	/// return the edge that follows to e
	public Edge nextEdge(Edge e) {
		int j = 0;
		while (edges[j] != e)
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
			points[i] = points[+1];
		--size;
	}

	public void deleteRedundantPoints() {
		if (size < 3)
			deleteDoublePoints();
		else {
			/*Iterator<Point> it = points.iterator();
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
			points.removeAll(toRemove);*/
			//TODO implement
		}
	}

	public Point get(int i) { return points[i]; }
	public Point getFirst() { return points[0]; }
	public Point getLast() { return points[size - 1]; }

	/// true iff p is inside the open polygon
	public boolean isInside(Point p) {
		if (!cyclic)
			return false;
		//if (!getPolygon().contains(p))
		//	return false;
		//TODO impelment contains
		for (int i = 0; i < edgeSize; ++i) {
			if (Edge.isOnEdge(p, edges[i]))
				return false;
		}
		return true;
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

	/*public Polygon getPolygon() {
		int[] xpoints = new int[points.size()];
		int[] ypoints = new int[points.size()];
		int i = 0;
		for (Point p : points) {
			xpoints[i] = p.x;
			ypoints[i] = p.y;
			++i;
		}
		return new Polygon(xpoints, ypoints, points.size());
	}*/

}
