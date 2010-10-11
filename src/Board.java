
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

    ///draws the entire setting
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D)g;
		for (Cheese c : world.getCheeses()) {
			if (!c.isAlive()) {
				g2d.setColor(Color.GRAY);
				g2d.fillPolygon(c.getPolygon());
			}
	        g2d.setColor(Color.BLUE);
	        for (Edge e : c.getVertices().edges)		
	    		g2d.drawLine(e.start.x, e.start.y, e.end.x, e.end.y);		
		}	        
		for (Craft c : world.getCrafts()) {
	        g2d.drawImage(c.getImage(), c.getX(), c.getY(), this);
	        // draw cuttingEdge
	        g2d.setColor(Color.MAGENTA);
	        for (Edge e : c.getCuttingEdge().edges)
	        	g2d.drawLine(e.start.x, e.start.y, e.end.x, e.end.y);
		}
		for (Ball b : world.getBalls())
			g2d.drawOval(b.getX(), b.getY(), 2, 2);

		int i = 1;
		for (Craft c : world.getCrafts()) {
			g2d.drawString("Player " + i + ": " + c.getPoints() + " Points", 600, 10 * i);
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
	        	case KeyEvent.VK_LEFT:  dir = Direction.Left; break;
	        	case KeyEvent.VK_UP:    dir = Direction.Up; break;
	        	case KeyEvent.VK_RIGHT: dir = Direction.Right; break;
	        	case KeyEvent.VK_DOWN:  dir = Direction.Down; break;
	        	case KeyEvent.VK_A: craft = 1; dir = Direction.Left; break;
	        	case KeyEvent.VK_W: craft = 1; dir = Direction.Up; break;
	        	case KeyEvent.VK_D: craft = 1; dir = Direction.Right; break;
	        	case KeyEvent.VK_S: craft = 1; dir = Direction.Down; break;
	        	default: dir = null; 
        	}
        	if (dir != null)
        		world.setCraftDirection(dir, craft);
        }
    }
}