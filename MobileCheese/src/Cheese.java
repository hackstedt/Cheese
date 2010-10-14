public class Cheese {

	private PointList vertices;
	private boolean isAlive;
	
	public Cheese(Point[] vertices, int size, boolean keepOrder, boolean isAlive){
		this.vertices = new PointList(vertices, size, true);
		if (!keepOrder)
			this.vertices = this.vertices.reverse();
		this.isAlive = isAlive;
	}
	
	/**
	 * 
	 * @param A PointList representing the cutting edge
	 * @return two cheese
	 */
	public Cheese[] cut(PointList cuttingEdge) {
		// we build two cheeses by adding the appropriate vertices
		Cheese[] cheese = new Cheese[2];

		// both cheeses contain the cutting edge
		cheese[0] = new Cheese(cuttingEdge.points, cuttingEdge.size, true, true);
		cheese[1] = new Cheese(cuttingEdge.points, cuttingEdge.size, false, true);
		
		// find position of cuttingEdge.getLast
		Edge e = vertices.getEdge(cuttingEdge.getLast());
		// now cuttingEge.getLast() lies on e
		
		// welcher Sonderfall wird hier behandelt? 
		if (e == vertices.getEdge(cuttingEdge.getFirst())) {
			vertices.addVertex(cuttingEdge.getFirst());
			vertices.addVertex(cuttingEdge.getLast());
			e = vertices.getEdge(cuttingEdge.getLast());
		}
		
		//create first cheese
    	while (!Edge.isOnEdge(cuttingEdge.getFirst(), e)) {
    		cheese[0].vertices.addVertexToEnd(e.end);
    		e = vertices.nextEdge(e);
    	}
    	
		// create second cheese
    	while (!Edge.isOnEdge(cuttingEdge.getLast(), e)) {
    		cheese[1].vertices.addVertexToEnd(e.end);
    		e = vertices.nextEdge(e);
    	}
    	cheese[0].vertices.deleteDoublePoints();
    	cheese[1].vertices.deleteDoublePoints();
		return cheese;
	}
	
	public boolean isAlive() { return isAlive; }
	public void setIsAlive(boolean value) { isAlive = value; }
	public PointList getVertices(){	return vertices; }
	public float getArea() { return vertices.getArea(); }
	public boolean isInside(Point p) { return vertices.isInside(p);	}
	public boolean isOnBorder(Point p) { return vertices.isOnBorder(p);	}
	public String toString() { return vertices.toString(); }
}
