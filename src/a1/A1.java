package a1;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import javax.media.j3d.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.JOptionPane;

/**
 *
 * @author dfudger
 */
public class A1 //extends Thread 
{
    static Socket socket;
    static InputStream in;
    static OutputStream out;
    static int type = 0;    
    static String p2IP;
    static String server_host = "localhost";
    static String player = "";
    /*@Override
    public void run() 
    {
        System.out.println("Hello from a thread!");
        
        
    }*/
    
  //Custom button text

    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        int server_port = 7099;
        int inbyte;
        
        Object[] options = 
        {   
            "Exit",
            "Join Game",
            "New Game"
        };

        
        
        int n = JOptionPane.showOptionDialog(null,
            "Welcome to Tic Tac Toe. Choose an option:",
            "Tic Tac Toe",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[2]);
        
        if(n == 0)
            System.exit(1);
        else if(n == 2) 
        {
            player = "player1";
            (new Server()).start();
            
            //Save the IP address for the server player1
            try 
            {
                InetAddress p1IP =InetAddress.getLocalHost();
                server_host = p1IP.getHostAddress();
                System.out.println("IP:"+p1IP.getHostAddress());
            }
            catch(Exception e) 
            {
                e.printStackTrace();
            }
            
        }
        else 
        {
            player = "player2";
            String p1IP = (String)JOptionPane.showInputDialog(
                                null,
                                "Enter player ones IP address:\n",
                                "Players IP",
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                null,
                                null);

            
            
            //while(p1IP.length() == 0)
                
            
            if ((p2IP != null) && (p2IP.length() > 0)) {
              
                System.out.println(p2IP.length());
                System.out.print(p2IP);
                return;
            }
            //else

            
            
        }
        
        //Splash screen - choose option
        
        //If create game: 
        
        
        //else - connect to game. 
        
        System.out.println("\n_____ Tic Tac Toe Player ______\n");
        try
        {
            socket = new Socket (server_host, server_port);
            System.out.println("Connected to server.");
            
            
            in = socket.getInputStream ();
            //inbyte = in.read ();
            //System.out.print (inbyte);
            
            out =  socket.getOutputStream ();
            //out.write(6);
                
            
        } 
        catch (IOException e) 
        {
            System.err.println("Could not listen on port: 4444.");
            System.exit(-1);
        }
      
        //Set up game board.
    	Window gui = new Window();
        gui.setVisible(true);
        

    }

   /*public void playSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("beep.wav"));
            Clip clip = AudioSystem.getClip( );
            clip.open(audioInputStream);
            clip.start( );
        }
        catch(Exception e)  {
            e.printStackTrace( );
        }
    } */   
        
}
