import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {
	
	private World world;
	private int worldWidth = 500;
	private int worldHeight = 500;
    private Timer timer;

    public Board() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        world = new World(worldWidth, worldHeight, 3, 3);
        timer = new Timer(5, this);
        timer.start();
    }
    
    public Board(String str) {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        if (str.equals("SINGLE"))
			world = new World(worldWidth, worldHeight, 1, 1);
        else if (str.equals("TWOPLAYERLOCAL"))
			world = new World(worldWidth, worldHeight, 2, 1);
        else
        	System.out.println("Error");
        timer = new Timer(5, this);
        timer.start();
    }

	private int convertXToViewPort(int x) {
		return 5+ (x*(getWidth()-10) / worldWidth);
	}
	private int convertYToViewPort(int y) {
		return 5+ (y*(getHeight()-10) / worldHeight);
	}

	private void drawLineViewport(int xs, int ys, int xe, int ye, Graphics g) {
		g.drawLine(convertXToViewPort(xs), convertYToViewPort(ys),
				   convertXToViewPort(xe), convertYToViewPort(ye));
	}

	private void drawArcViewport(int x, int y, int w, int h, int startAngle, int endAngle, Graphics g) {
		g.drawArc(convertXToViewPort(x), convertYToViewPort(y), w, h, startAngle, endAngle);
	}

	private void fillPolygonViewport(Polygon p, Graphics g) {
		for (int i = 0; i < p.npoints; ++i) {
			p.xpoints[i] = convertXToViewPort(p.xpoints[i]);
			p.ypoints[i] = convertYToViewPort(p.ypoints[i]);
		}
		g.fillPolygon(p);
	}

    ///draws the entire setting
	@Override
    public void paint(Graphics g) {
        super.paint(g);

		// draw cheeses
		for (int i = 0; i < world.getCheeseCount(); ++i) {
			// draw dead cheese in gray
			Cheese c = world.getCheeses()[i];
			if (!c.isAlive()) {
				g.setColor(Color.GRAY);
				fillPolygonViewport(world.getCheeses()[i].getPolygon(), g);
			}
			// draw surround edges in blue
			g.setColor(Color.BLUE);
			for (int j = 0; j < c.getVertices().edgeSize; ++j) {
				Edge e = c.getVertices().edges[j];
				drawLineViewport(e.start.x, e.start.y, e.end.x, e.end.y, g);
			}
		}

		// draw crafts
		for (int i = 0; i < world.getPlayerCount(); ++i) {
			Craft c = world.getCrafts()[i];
			//g2d.drawImage(c.getImage(), c.getX(), c.getY(), this);
			g.setColor(Color.GREEN);
			drawArcViewport(c.getPosition().x, c.getPosition().y, 3, 3, 0, 360, g);
			// draw cuttingEdge
			g.setColor(Color.RED);
			for (int j = 0; j < c.getCuttingEdge().edgeSize; ++j) {
				Edge e = c.getCuttingEdge().edges[j];
				drawLineViewport(e.start.x, e.start.y, e.end.x, e.end.y, g);
			}
		}

		//draw balls
		for (int i = 0; i < world.getLevel(); ++i)
			drawArcViewport(world.getBalls()[i].getPosition().x,
					  world.getBalls()[i].getPosition().y, 2, 2, 0 , 360, g);

		// draw Scores
		//int i = 1;
		//for (Craft c : world.getCrafts()) {
		//	g.drawString("Player " + i + ": " + c.toString(), 600, 10 * i);
		//	++i;
		//}

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    /// called by the timer
    public void actionPerformed(ActionEvent e) {
        world.move();
        repaint();
    }

    private class TAdapter extends KeyAdapter {

		@Override
        public void keyPressed(KeyEvent e) {
            Direction dir;
            int craft = 0;
        	int key = e.getKeyCode();
        	switch(key) {
	        	case KeyEvent.VK_LEFT:  dir = Direction.LEFT; break;
	        	case KeyEvent.VK_UP:    dir = Direction.UP; break;
	        	case KeyEvent.VK_RIGHT: dir = Direction.RIGHT; break;
	        	case KeyEvent.VK_DOWN:  dir = Direction.DOWN; break;
	        	case KeyEvent.VK_A: craft = 1; dir = Direction.LEFT; break;
	        	case KeyEvent.VK_W: craft = 1; dir = Direction.UP; break;
	        	case KeyEvent.VK_D: craft = 1; dir = Direction.RIGHT; break;
	        	case KeyEvent.VK_S: craft = 1; dir = Direction.DOWN; break;
	        	default: dir = null; 
        	}
        	if (dir != null)
        		world.setCraftDirection(dir, craft);
        }
    }
}