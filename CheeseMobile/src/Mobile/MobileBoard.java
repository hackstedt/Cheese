package Mobile;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

import cheesemobilecheese.Cheese;
import cheesemobilecheese.Craft;
import cheesemobilecheese.Direction;
import cheesemobilecheese.Edge;
import cheesemobilecheese.World;

/**
 *
 */
public class MobileBoard extends GameCanvas implements Runnable {

    private World world;
	private boolean interrupted;
    
    public MobileBoard() {
        super(true);
        try {
            this.setFullScreenMode(true);
            this.init();
        } catch (IOException ex) {}
    }

    /**
     * Initialize the Game Design, then load all layers and start animation threads.
     */
    private void init() throws IOException {
        this.world = new World(0, 300, 0, 300, 1, 1);
		this.interrupted = false;

		//this.timer.scheduleAtFixedRate(this.waterAnimator, 0, gameDesign.AnimWaterSeq001Delay);
    }

	private int convertX(int x) {
		return (getWidth()-1) * x / 300;
	}

	private int convertY(int y) {
		return (getHeight()-1) * y / 300;
	}
    /**
     * The main game loop that checks for user input and repaints canvas.
     */
    public void run() {
        Graphics g = getGraphics();
        while (!this.interrupted) {

            //check for user input
            int keyState = getKeyStates();

            //if user is pressing the left button
            if ((keyState & LEFT_PRESSED) != 0) {
                world.setCraftDirection(Direction.LEFT, 0);
            } else if ((keyState & RIGHT_PRESSED) != 0) {
                world.setCraftDirection(Direction.RIGHT, 0);
            } else if ((keyState & UP_PRESSED) != 0) {
                world.setCraftDirection(Direction.UP, 0);
            } else if ((keyState & DOWN_PRESSED) != 0) {
                world.setCraftDirection(Direction.DOWN, 0);
            }

			world.move();

			g.setColor(0,0,0);
			g.fillRect(0,0,getWidth(), getHeight());

			// draw cheeses
			for (int i = 0; i < world.getCheeseCount(); ++i) {
				// draw dead cheese in gray
				Cheese c = world.getCheeses()[i];
				if (!c.isAlive()) {
					g.setColor(200,200,200);
					//g.fillPolygon(world.getCheeses()[i].getPolygon());
				}
				// draw surround edges in blue
				g.setColor(0,0,255);
				for (int j = 0; j < c.getVertices().edgeSize; ++j) {
					Edge e = c.getVertices().edges[j];
					g.drawLine(convertX(e.start.x), convertY(e.start.y), convertX(e.end.x), convertY(e.end.y));
				}
			}

			// draw crafts
			for (int i = 0; i < world.getPlayerCount(); ++i) {
				Craft c = world.getCrafts()[i];
				//g2d.drawImage(c.getImage(), c.getX(), c.getY(), this);
				g.setColor(0,255,0);
				g.drawArc(convertX(c.getPosition().x), convertY(c.getPosition().y), 3, 3, 0, 360);
				// draw cuttingEdge
				g.setColor(255,0,0);
				for (int j = 0; j < c.getCuttingEdge().edgeSize; ++j) {
					Edge e = c.getCuttingEdge().edges[j];
					g.drawLine(convertX(e.start.x), convertY(e.start.y), convertX(e.end.x), convertY(e.end.y));
				}
			}

			//draw balls
			for (int i = 0; i < world.getLevel(); ++i)
				g.drawArc(convertX(world.getBalls()[i].getPosition().x),
						  convertY(world.getBalls()[i].getPosition().y), 2, 2, 0 , 360);
			
			repaint();
			
            try {
                Thread.sleep(15);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Stops the main game loop.
     */
    public void stop() {
        this.interrupted = true;
    }
}
