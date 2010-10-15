/**
 *
 * @author Helge
 */
public class Point {

    public int x, y;
    
    public Point(int x, int y) { this.x = x; this.y = y; }

	//copy constructor
	public Point(Point p) {this.x = p.x; this.y = p.y; }

	public boolean equals(Point rhs) { return (rhs.x == x && rhs.y == y); }
}
