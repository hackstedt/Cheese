import java.awt.Point;

public enum Direction { 
	Up, Right, Down, Left, UpRight, UpLeft, DownRight, DownLeft;

	public static Direction reflect(Direction dir, Edge e, Point p) {
		if (e == null)
			return dir;
		if (!e.start.equals(p) && !e.end.equals(p)) {
			switch(dir) {
				case UpLeft:
					return (e.horizontal() ? Direction.DownLeft : Direction.UpRight);
				case UpRight:
					return (e.horizontal() ? Direction.DownRight : Direction.UpLeft);
				case DownLeft:
					return (e.horizontal() ? Direction.UpLeft : Direction.DownRight);
				case DownRight:
					return (e.horizontal() ? Direction.UpRight : Direction.DownLeft);
				default: return dir; 
			}
		}
		else {
			switch(dir) {
				case UpLeft:
					return Direction.DownRight;
				case UpRight:
					return Direction.DownLeft;
				case DownLeft:
					return Direction.UpRight;
				case DownRight:
					return Direction.UpLeft;
				default: return dir; 
			}
		}
	}
}




