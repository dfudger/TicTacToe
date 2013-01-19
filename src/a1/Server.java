/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a1;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author dfudger
 */
public class Server extends Thread{

    
    @Override
    public void run() 
    {
        System.out.println("Hello from a thread!");
        
        int port = 7099, p1in = 0, p2in = 0;
        ServerSocket serverSocket;
        Socket player1Socket, player2Socket;
        
        
        System.out.println("\n_____ Tic Tac Toe Server ______\n");
        
        try 
        {
            serverSocket = new ServerSocket (port);
            
            System.out.println("Waiting for player one.");
            player1Socket = serverSocket.accept ();
            System.out.println("Player one accepted.");
            
            System.out.println("Waiting for player two.");
            player2Socket = serverSocket.accept ();
            System.out.println("Player two accepted. ");
        
            InputStream in1 = player1Socket.getInputStream ();
            InputStream in2 = player2Socket.getInputStream ();
            OutputStream out1 =  player1Socket.getOutputStream ();
            OutputStream out2 =  player2Socket.getOutputStream ();
            System.out.println("Input streams set up. Let's play!");
            
            out1.write(1);
            out2.write(2);
            
            while (true) //Continue accepting until game over
            {
                //Wait for p1 response
                p1in = in1.read ();
                System.out.print ("P1 move: " + p1in);
                
                if(p1in == 9) 
                    break; //game is over and should break connection with the sockets
                    
                else //send p1 response to p2
                    out2.write(p1in);
                

                //Wait for p2 response
                p2in = in2.read ();
                System.out.print ("P2 move: " + p2in);
                
                //If computer sends a 0, then the game is over and should break connection with the sockets. . 
                if(p2in == 9)
                    break;
                  
                else //send p2 response to p1
                    out1.write(p2in);
                    
                
            }
            
            //Close before exiting. 
            serverSocket.close();
            player1Socket.close();
         //   player2Socket.close();
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
        
       
        
        
        
        
         
         
    }

