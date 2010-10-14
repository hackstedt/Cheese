public class Direction {

    public static final Direction UP = new Direction("Up");
    public static final Direction RIGHT = new Direction("Right");
    public static final Direction DOWN = new Direction("Down");
    public static final Direction LEFT = new Direction("Left");
    public static final Direction UPRIGHT = new Direction("UpRight");
    public static final Direction UPLEFT = new Direction("UpLeft");
    public static final Direction DOWNRIGHT = new Direction("DownRight");
    public static final Direction DOWNLEFT = new Direction("DownLeft");

    private final String name;

    private Direction(final String name) {
        this.name = name;
    }

public static Direction reflect(Direction dir, Edge e, Point p) {
    if (e == null)
        return dir;
    if (!e.start.equals(p) && !e.end.equals(p)) {
        if (dir == UPLEFT)
            return (e.horizontal() ? DOWNLEFT : UPRIGHT);
        if (dir == UPRIGHT)
            return (e.horizontal() ? DOWNRIGHT : UPLEFT);
        if (dir == DOWNLEFT)
            return (e.horizontal() ? UPLEFT : DOWNRIGHT);
        if (dir == DOWNRIGHT)
            return (e.horizontal() ? UPRIGHT : DOWNLEFT);
        else
            return dir;
    }
    else {
        if (dir == UPLEFT)
            return DOWNRIGHT;
        if (dir == UPRIGHT)
            return DOWNLEFT;
        if (dir == DOWNLEFT)
            return UPRIGHT;
        if (dir == DOWNRIGHT)
            return UPLEFT;
        else
            return dir;
    }
}
}




