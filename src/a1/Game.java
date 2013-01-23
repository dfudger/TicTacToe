/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author dfudger
 */
public class Game 
{
    Window thisGUI;
    Socket socket;
    InputStream in;
    OutputStream out;
    String p2IP;
    String server_host = "localhost";
    String player = "";
    private int playerNum;
    int moveMade = -1;
    int didWin = 0;
    
    public Game(Window gameBoard)
    {
        
    }

    public void startGame()
    {
        int server_port = 7099;
        int inbyte;
        int n = -1;
        
        System.out.println("Opening game dialog");
        n = Window.gameDialog();
        
        if(n == 0) //Cancel
            System.exit(1);
        
        else if(n == 2) 
        {
            player = "player1";
            (new Server()).start();
            
            //Save the IP address for the server player1
            server_host = Server.getIP();
            System.out.println("Servers IP:"+ server_host);
            JOptionPane.showMessageDialog(null, "Please give this IP to player 2:\n\n" + server_host + "\n");
            //NOTE: Need to display in a window.
            
            
        }
        else 
        {
            player = "player2";
            server_host = (String)JOptionPane.showInputDialog(null, "Enter player ones IP address:\n", "Players IP", JOptionPane.PLAIN_MESSAGE, null,null,null);
            System.out.print("Entered address: " + server_host);
            if ((server_host != null) && (server_host.length() > 0)) 
            {
                //NOTE: Add error checking.
            }    
        }
       
        try
        {
            socket = new Socket (server_host, server_port);
            if(socket != null)
                System.out.println("Connected to server.");
            else 
            {
                System.out.println("ERROR: Could not connect to server.");
                System.exit(-1);
            }
                
            in = socket.getInputStream ();
            out =  socket.getOutputStream ();
        } 
        catch (IOException e) 
        {
            System.err.println("Could not listen on port: 7099.");
            System.exit(-1);
        }
        
        
        //Find out what your player number is
        try
        {
            in = socket.getInputStream ();
            playerNum = in.read ();
            System.out.print ("Player #" + playerNum);
            
            
        }
        catch (IOException e) 
        {
            System.err.println("Couldn't get player number.");
            System.exit(-1);
        }
        
        setPlayerNum(playerNum);
    }
    
    private void setPlayerNum(int player)
    {
        playerNum = player;
    }
    
    public int getPlayerNum()
    {
        return playerNum;
    }
    
    public Game setGame() 
    {
        return this;
    }
    
    public void playGame() 
    {
        boolean gameOver = false;
        boolean myTurn = false;
        
        System.out.println("\n\n_________________Tic Tac Toe Player____________\n\n");
        System.out.println("My Player Number: " + playerNum);
        
        if(playerNum == 1)
                myTurn = true; //X goes first
            else
                myTurn = false;
        
        
        while(gameOver == false)
        {
            if(myTurn == true)
            {
               System.out.println("My Turn.\n");
                //Enable buttons
               thisGUI.enableButtons();
               System.out.println("Buttons enabled");
               //thisGUI.setText("Your Turn");
            }
            //thisGUI.disableButtons();
            moveMade = getResponse(in);
            System.out.println("\nMove Sent from Server: " + moveMade);
            
            didWin = thisGUI.winCheck();
            
            if(didWin == 1 && myTurn == true)
            {
                //You win!
                System.out.println("You win!!");
                JOptionPane.showMessageDialog(null, "You Win!");
                System.exit(1);
            }
            
            if(didWin == 1 && myTurn == false)
            {
                System.out.println("You lose!!");
                JOptionPane.showMessageDialog(null, "You Lose!");
                System.exit(1);
            }
            
            
            if (moveMade > -1 && moveMade < 10)
            {
                //System.out.println("Move made: " + moveMade);
                thisGUI.buttonChange(moveMade, myTurn);
                
                if(myTurn == true)
                    myTurn = false;
                else
                    myTurn = true;

            }
            /*else if (moveMade == 11)
            {
                System.out.println("You win!!");
                JOptionPane.showMessageDialog(null, "You Win!");
                return;
            }
            else if (moveMade == 12)
            {
                System.out.println("You lose!!");
                JOptionPane.showMessageDialog(null, "You Lose!");
                return;
            }
            else if (moveMade == 13)
            {
                System.out.println("Stalemate!");
                JOptionPane.showMessageDialog(null, "Stalemate");
                return;
            }
            else
            {
                System.out.println("ERROR: Invalid move.");
                System.exit(-1);
            }
             
             */
                
            //Either not my turn or sent click
            //Listen for message from server about what button pressed
            //If not a error response:
                //Update GUI (method)
            
        }
    }


public void setGUI(Window currGUI)
    {
        thisGUI = currGUI;
    }


  private int getResponse(InputStream in) 
    {
        int inbyte = -1;
        
        try
        {
            //in =  socket.getInputStream ();
            inbyte = in.read(); //readint

            System.out.print ("Read from server: " + inbyte + "\n");
        }
        catch (IOException e) 
        {
            System.err.println("Couldn't read p2 move from server.");
            System.exit(-1);
        }
        
        return inbyte;
    }

}
