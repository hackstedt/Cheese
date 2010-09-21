
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
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
    private Timer timer;
    private Craft craft;
    private Cheese cheese;

    public Board() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        world = new World();

        craft = new Craft();
        cheese = new Cheese();
        
        timer = new Timer(5, this);
        timer.start();
    }


    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D)g;
        Polygon p = world.cheese.getPolygon();
        // draw cheese
        g2d.fillPolygon(p);
        // draw craft
        g2d.drawImage(world.craft.getImage(), world.craft.getX(), world.craft.getY(), this);
        // Point p1 = world.cheese.getEntryOrExitPoint(new Point(world.craft.getX(),world.craft.getY()),world.craft.getDirection());
        g2d.setColor(Color.RED);
        // g2d.drawOval((int)p1.getX(), (int) p1.getY(), 10, 10);
       
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
    
    


    public void actionPerformed(ActionEvent e) {
        world.move();
        repaint();  
    }


    private class TAdapter extends KeyAdapter {


        public void keyPressed(KeyEvent e) {

            Direction direction;
            
        	int key = e.getKeyCode();
        	
        	switch(key) {
        	case KeyEvent.VK_LEFT: direction = Direction.Left; break;
        	case KeyEvent.VK_UP: direction = Direction.Up; break;
        	case KeyEvent.VK_RIGHT: direction = Direction.Right; break;
        	case KeyEvent.VK_DOWN: direction = Direction.Down; break;
        	default: direction = Direction.Down; 
        	}
        	world.changeDirection(direction);
        }
    }

}

