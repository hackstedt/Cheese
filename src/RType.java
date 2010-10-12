import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Main program.
 * 
 * @author Martin und Helge Boeschen
 *
 */
public class RType extends JFrame {
	
	private static final String[] GAMETYPE = {"SINGLE", "TWOPLAYERLOCAL", "TWOPLAYERNETWORK"};
    public RType() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setTitle("Cheese");
    }

    public static void main(String[] args) {
    	// Choose game dialog
        RType rtype = new RType();
        String ret = (String)JOptionPane.showInputDialog(
    		      rtype,
    		      "Choose you game",
    		      "JOptionPane.showInputDialog",
    		      JOptionPane.QUESTION_MESSAGE,
    		      null,
    		      GAMETYPE,
    		      GAMETYPE[0]
    		    );
        rtype.add(new Board(ret));
        rtype.setResizable(false);
        rtype.setVisible(true);
    }
}
