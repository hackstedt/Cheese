import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {
	
	private World world;
    private Timer timer;

    public Board() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        world = new World(5, 505, 5, 505, 3, 3);
        timer = new Timer(5, this);
        timer.start();
    }
    
    public Board(String str) {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        if (str.equals("SINGLE"))
			world = new World(5, 505, 5, 505, 1, 1);
        else if (str.equals("TWOPLAYERLOCAL"))
			world = new World(5, 505, 5, 505, 2, 1);
        else
        	System.out.println("Error");
        timer = new Timer(5, this);
        timer.start();
    }

    ///draws the entire setting
    public void paint(Graphics g) {
        super.paint(g);

		// draw cheeses
		for (int i = 0; i < world.getCheeseCount(); ++i) {
			// draw dead cheese in gray
			Cheese c = world.getCheeses()[i];
			if (!c.isAlive()) {
				g.setColor(Color.GRAY);
				g.fillPolygon(world.getCheeses()[i].getPolygon());
			}
			// draw surround edges in blue
			g.setColor(Color.BLUE);
			for (int j = 0; j < c.getVertices().edgeSize; ++j) {
				Edge e = c.getVertices().edges[j];
				g.drawLine(e.start.x, e.start.y, e.end.x, e.end.y);
			}
		}

		// draw crafts
		for (int i = 0; i < world.getPlayerCount(); ++i) {
			Craft c = world.getCrafts()[i];
			//g2d.drawImage(c.getImage(), c.getX(), c.getY(), this);
			g.setColor(Color.GREEN);
			g.drawArc(c.getPosition().x, c.getPosition().y, 3, 3, 0, 360);
			// draw cuttingEdge
			g.setColor(Color.RED);
			for (int j = 0; j < c.getCuttingEdge().edgeSize; ++j) {
				Edge e = c.getCuttingEdge().edges[j];
				g.drawLine(e.start.x, e.start.y, e.end.x, e.end.y);
			}
		}

		//draw balls
		for (int i = 0; i < world.getLevel(); ++i)
			g.drawArc(world.getBalls()[i].getPosition().x,
					  world.getBalls()[i].getPosition().y, 2, 2, 0 , 360);

		// draw Scores
		int i = 1;
		for (Craft c : world.getCrafts()) {
			g.drawString("Player " + i + ": " + c.toString(), 600, 10 * i);
			++i;
		}

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    /// called by the timer
    public void actionPerformed(ActionEvent e) {
        world.move();
        repaint();
    }

    private class TAdapter extends KeyAdapter {

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