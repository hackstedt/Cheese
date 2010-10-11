import java.awt.Point;
import java.awt.Polygon;
import java.util.LinkedList;
import java.util.Vector;

public class Cheese {

	private PointList vertices;
	private boolean isAlive;
	
	public Cheese(LinkedList<Point> vertices, boolean keepOrder, boolean isAlive){
		this.vertices = new PointList(vertices, true);
		if (!keepOrder)
			this.vertices = this.vertices.reverse();
		this.isAlive = isAlive;
	}
	
	/**
	 * 
	 * @param A PointList representing the cutting edge
	 * @return two cheese
	 */
	public Vector<Cheese> cut(PointList cuttingEdge) {
		// we build two cheeses by adding the appropriate vertices
		
		// both cheeses contain the cutting edge
		Cheese cheeseOne = new Cheese(cuttingEdge.points, true, true);
		Cheese cheeseTwo = new Cheese(cuttingEdge.points, false, true);
		
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
    		cheeseOne.vertices.addVertexToEnd(e.end);
    		e = vertices.nextEdge(e);
    	}
    	
		// create second cheese
    	while (!Edge.isOnEdge(cuttingEdge.getLast(), e)) {
    		cheeseTwo.vertices.addVertexToEnd(e.end);
    		e = vertices.nextEdge(e);
    	}
    	cheeseOne.vertices.deleteDoublePoints();
    	cheeseTwo.vertices.deleteDoublePoints();    	
		Vector<Cheese> toReturn = new Vector<Cheese>();
		toReturn.add(cheeseOne);
		toReturn.add(cheeseTwo);
		return toReturn;
	}
	
	public boolean isAlive() { return isAlive; }
	public void setIsAlive(boolean value) { isAlive = value; }
	public PointList getVertices(){	return vertices; }
	public int getArea() { return vertices.getArea(); }
	public boolean isInside(Point p) { return vertices.isInside(p);	}
	public boolean isOnBorder(Point p) { return vertices.isOnBorder(p);	}
	public String toString() { return vertices.toString(); }
	public Polygon getPolygon() { return vertices.getPolygon();	}
}
