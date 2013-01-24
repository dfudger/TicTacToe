/*
 * 
 * TicTacToe - Game.java
 * CIS*3760 Assignment One
 * University of Guelph
 * 
 * Author: Danielle Fudger 0621496
 * Contact: dfudger@uoguelph.ca
 * Date: Januray 23, 2013
 * 
 * This is a java based TicTacToe game for two players. For more information, please see the README. 
 * 
 */
package a1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 * The Game class contains the client side logic for the tictactoe game.
 * It controls whose turn it is, if the player should make a move, or wait for the other player.
 * @author dfudger
 */
public class Game 
{
    Window thisGUI;
    Socket socket;
    InputStream in;
    OutputStream out;
    String p2IP, server_host = "localhost", player = "";
    private int playerNum;
    int moveMade = -1, didWin = 0;
    
    /**
     * Within the constructor, the gui is passed in as a parameter so the game object can send data to their specific Window object.
     * @param gameBoard
     */
    public Game(Window gameBoard)
    {
        
    }

    /**
     * This function calls gameDialog() that asks the user to choose an option, 
     * based on that option, the player either created a server, or is asked to give them IP and join one.
     * 
     * Once the user had connected to the server and is assigned a player number (1 or 2) the game is ready to begin.
     */
    public void startGame()
    {
        int server_port = 7099, n = -1;
        
        System.out.println("Opening game dialog");
        n = Window.gameDialog();
        
        if(n == 0) //Cancel
            System.exit(1);
        
        else if(n == 2) //Create a game
        {
            player = "player1";
            (new Server()).start();
            
            //Save the IP address for the server player1
            server_host = Server.getIP();
            System.out.println("Servers IP:"+ server_host);
            JOptionPane.showMessageDialog(null, "Please give this IP to player 2:\n\n" + server_host + "\n");            
        }
        
        else //Join a game, ask the user to input their IP address for the game and save it to the server_host to connect with the other player
        {
            player = "player2";
            server_host = (String)JOptionPane.showInputDialog(null, "Enter player ones IP address:\n", "Players IP", JOptionPane.PLAIN_MESSAGE, null,null,null);
            //System.out.print("Entered address: " + server_host); 
        }
       
        try
        {
            socket = new Socket (server_host, server_port);
            if(socket != null)
            {
                //System.out.println("Connected to server.");
            }
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
        
        //Find out what your player number is, first read from the server
        try
        {
            in = socket.getInputStream ();
            playerNum = in.read ();
            //System.out.print ("Player #" + playerNum);
        }
        catch (IOException e) 
        {
            System.err.println("Couldn't get player number.");
            System.exit(-1);
        }
        
        setPlayerNum(playerNum); //Sent to the Window class, informs the user of what player they are.
    }
    
    /*
     * Sets the player number based on the player parameter
     */
    private void setPlayerNum(int player)
    {
        playerNum = player;
    }
    
    /**
     * This functions returns the playerNumber saved for their game.
     * @return playerNum
     */
    public int getPlayerNum()
    {
        return playerNum;
    }
    
    /**
     * This function passes the game object back to the gui (Window class)
     * @return Game object
     */
    public Game setGame() 
    {
        return this;
    }
    
    /**
     * This function controls the game play for the client. If it is their turn to move
     * then they can click on a button on the game board, that response is sent to the server
     * and the game board waits for a response from the server, after which it will add an X to that 
     * position on the board. If it is not their turn, the game waits for a response from the server
     * telling them what move the other player made.
     */
    public void playGame() 
    {
        boolean gameOver = false;
        boolean myTurn = false;
        
        thisGUI.setGreeting("Player " + playerNum);
        if(playerNum == 1)
                myTurn = true; //Player one goes first
            else
                myTurn = false;
        
        
        while(gameOver == false)
        {
            if(myTurn == true)
            {
               System.out.println("My Turn.\n");
               thisGUI.enableButtons();
               System.out.println("Buttons enabled");
            }
            
            moveMade = getResponse(in);
            
            if (moveMade > -1 && moveMade < 10)
            {
                thisGUI.buttonChange(moveMade, myTurn);
                
                if(myTurn == true)
                    myTurn = false;
                else
                    myTurn = true;
            }
        }
    }

    /**
     * This function is passed the Window object created in the main and sets the object to the one within this class.
     * @param currGUI
     */
    public void setGUI(Window currGUI)
    {
        thisGUI = currGUI;
    }

    /*
     * This function listens for a message from the server. This integer read
     * is then returned to the calling function.
     * 
     */
    private int getResponse(InputStream in) 
    {
        int inbyte = -1;
        
        try
        {
            inbyte = in.read(); //read integer from server
            //System.out.print ("Read from server: " + inbyte + "\n");
        }
        catch (IOException e) 
        {
            System.err.println("Couldn't read p2 move from server.");
            System.exit(-1);
        }
        
        return inbyte;
    }

}
