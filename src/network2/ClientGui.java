package network2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;


public class ClientGui extends JFrame implements ActionListener
{
  private static final int PORT = 1235;     // server details
  private static final String HOST = "localhost";


  private JTextArea jtaMesgs;
  private JButton jLeft;
  private JButton jUp;
  private JButton jConnect;
  Client client;


  public ClientGui()
  {
     super( "High Score Client" );

     initializeGUI();
     client=new Client();

     addWindowListener( new WindowAdapter() {
       public void windowClosing(WindowEvent e)
       { ; }
     });

     setSize(300,450);
     setVisible(true);
  } // end of ScoreClient();


  private void initializeGUI()
  // text area in center, and controls in south
  {
    Container c = getContentPane();
    c.setLayout( new BorderLayout() );

    jtaMesgs = new JTextArea(7, 7);
    jtaMesgs.setEditable(false);
    JScrollPane jsp = new JScrollPane( jtaMesgs);
    c.add( jsp, "Center");

    
    jLeft = new JButton("Left");
    jLeft.addActionListener(this);
    
    jUp = new JButton("Up");
    jUp.addActionListener(this);
    
    jConnect= new JButton("Connect");
    jConnect.addActionListener(this);

    JPanel p1 = new JPanel( new FlowLayout() );
    p1.add(jLeft); p1.add(jUp); p1.add(jConnect);
    
    c.add(p1, "South");

  }  // end of initializeGUI()


  

    


   public void actionPerformed(ActionEvent e)
   // Either a name/score is to be sent or the "Get Scores"
   // button has been pressed
   {
     if (e.getSource() == jLeft) {
    	 jtaMesgs.append("Left gedrückt \n");
    	 try {
    	 client.connect();
    	 }
    	 catch (Throwable ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
     }
     else if (e.getSource() == jUp)
    	 jtaMesgs.append("Right gedrückt \n");
   }

  


  // ------------------------------------

  public static void main(String args[]) 
  {  new ClientGui();  }

} // end of ScoreClient class