import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * Initializes the MobileBoard.
 */
public class GameMidlet extends MIDlet {

    private MobileBoard gameCanvas;
    private Thread t;
    private Display d;

    public void startApp() {
        this.gameCanvas = new MobileBoard();
        this.t = new Thread(gameCanvas);
        t.start();
        d = Display.getDisplay(this);
        d.setCurrent(gameCanvas);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        this.gameCanvas.stop();
    }
}
