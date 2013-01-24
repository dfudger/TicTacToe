/*
 * 
 * TicTacToe - Server.java
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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The server class is responsible for establishing a network between the server
 * and two client games of tictactoe. The server checks that the button on the game board
 * has not been used and sends the move to both clients so they can update their game board.
 * @author dfudger
 */
public class Server extends Thread
{
    /**
     * This function keeps the server running while a game is in session. 
     * It creates the sockets and listens for each players move.
     */
    @Override
    public void run() 
    {
        boolean gameOver = false;
        int turn = 0, port = 7099, in = -1;
        ServerSocket serverSocket;
        Socket player1Socket, player2Socket;
        int listOfSpots[] = new int[] {0,0,0,0,0,0,0,0,0};
        
        try 
        {
            serverSocket = new ServerSocket (port);
            
            
            player1Socket = serverSocket.accept ();
            player2Socket = serverSocket.accept ();
            
            InputStream in1 = player1Socket.getInputStream ();
            InputStream in2 = player2Socket.getInputStream ();
            
            OutputStream out1 =  player1Socket.getOutputStream ();
            OutputStream out2 =  player2Socket.getOutputStream ();
            
            out1.write(1);
            out2.write(2);
            
            while (gameOver == false) //Continue accepting until game over
            {
                if(turn == 0) //Player One
                {
                    //Wait for p1 response
                    in = in1.read ();
                }
                else if(turn == 1)
                {   
                    in = in2.read ();
                }
                else
                {
                    System.out.append("Error: no ones turn!");
                    System.exit(-1);
                }
         
                //Make sure not trying to click on a spot taken.
                if(listOfSpots[in] == 0 )//&& didWin == 0) 
                {
                    out1.write(in);
                    out2.write(in);
                    listOfSpots[in] = 1; //Mark is "used"
                    
                    if (turn == 0)
                        turn = 1;
                    else
                        turn = 0;    
                }
                
            }
            
            //Close before exiting. 
            serverSocket.close();
            player1Socket.close();
            player2Socket.close();
            System.exit(1);
        } 
        catch (IOException e) 
        {
            System.err.println("Could not listen on port: 7099.");
            System.exit(-1);
        }
}    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        
        
        }
        
       
    /**
     * This function gets the players IP address that the server will be hosted
     * and send it back to the user.
     * @return IP address
     */
    public static String getIP()
    {
        InetAddress p1IP = null;
        try 
        {
            p1IP =InetAddress.getLocalHost();
        }
        catch(Exception e) 
        {
            System.err.println(e);
        }
        
        return (p1IP.getHostAddress());
    }     
 }

