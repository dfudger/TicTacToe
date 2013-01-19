/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a1;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;

/**
*
* @author dfudger
*/
public class Window extends JFrame implements ActionListener {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /*****************************  Variables ****************************/
    JFrame box;
    public static final int theWIDTH = 300;
    public static final int theHEIGHT = 400;
    JLabel greeting = new JLabel();
    JPanel gamepanel = new JPanel();
    JButton button[] = new JButton[9];
    
    
    private boolean turn; //X's turn first
    private boolean winner = false;
    private int[][] winCombinations = new int[][]{
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, //horizontal wins
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, //vertical wins
        {0, 4, 8}, {2, 4, 6} //diagonal wins
    };
    
    private String playerOneImg = "images/xMove.jpg";
    private String playerTwoImg = "images/oMove.jpg";
    int playerNum;
    
    /*****************************  Create Game ****************************/
    public Window() 
    {
        try
        {
            A1.in = A1.socket.getInputStream ();
            playerNum = A1.in.read ();
            System.out.print ("Player #" + playerNum);
            
            if(playerNum == 1)
                turn = true; //X goes first
            else
                turn = false;
        }
        catch (IOException e) 
        {
            System.err.println("Couldn't get player number.");
            System.exit(-1);
        }
        
        
        //Create window
        box = new JFrame();
        box.setSize(theWIDTH, theHEIGHT);

        box.setDefaultCloseOperation(EXIT_ON_CLOSE); //Use X to close
        box.setLayout(new FlowLayout()); //Set the type of layout for window

        greeting.setText("Welcome to Tic Tac Toe");  
        box.add(greeting);

        gamepanel.setLayout(new GridLayout(3, 3));
        gamepanel.setBackground(new Color(0, 0, 0));

        Dimension d1 = new Dimension(300, 300);
        gamepanel.setPreferredSize(d1);
        box.add(gamepanel);


        //Each grid location holds a button, this button is blank
        for (int i = 0; i < button.length; i++) 
        {
            //System.out.println("This is i: " + i);
            button[i] = new JButton();
            button[i].setActionCommand(""); //Will be filled with an x or o
            button[i].setIcon(new ImageIcon("images/blank.jpg")); //Start off with nothing in the button. 
            button[i].addActionListener(this);
            gamepanel.add(button[i]);
        }

        box.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        
        int inbyte;
        int btnValue = 0;
        boolean winner = false;
        String emptyButton = " ";
        String[] toFile = new String[9];
        String currentTurn;
        
        Object source = ae.getSource();

        JButton clicked = (JButton) source;
        //System.out.println("Clicked: " + clicked);
        
        for(int i=0; i < button.length; i++)
        {
            if(button[i] == clicked) 
            {
                btnValue = i;
                System.out.println(btnValue);
                break;
            }
                
        }
        
        if (turn == true)  //It's player 1's turn
        {
            clicked.setActionCommand("X");
            clicked.setIcon(new ImageIcon(playerOneImg));
            
            //System.out.println(clicked.getIcon());
            
            clicked.removeActionListener(this);
            
            try 
            {
                A1.out =  A1.socket.getOutputStream ();
                A1.out.write(btnValue);

            } 
            catch (IOException e) 
            {
                System.err.println("Couldn't write to server.");
                System.exit(-1);
            }
            
            turn = false;
            greeting.setText("Player 2's Turn");
        }
        /*************************** Player Two ********************************/
        else //if (turn == false) { //It's player 2's turn
        {
            try
            {
                A1.in = A1.socket.getInputStream ();
                inbyte = A1.in.read ();
                System.out.print (inbyte + "\n");
                
            }
            catch (IOException e) 
            {
                System.err.println("Couldn't read p2 move from server.");
                System.exit(-1);
            }
            
            
            clicked.setActionCommand("O");
            clicked.setIcon(new ImageIcon(playerTwoImg));
        
            //System.out.println(clicked.getIcon());
            clicked.removeActionListener(this);
            turn = true;
            greeting.setText("Player 1's Turn");
        }

            
        
        for (int i = 0; i <= 7; i++) {
            if (button[winCombinations[i][0]].getActionCommand().equals(button[winCombinations[i][1]].getActionCommand())
                    && button[winCombinations[i][1]].getActionCommand().equals(button[winCombinations[i][2]].getActionCommand())
                    && !button[winCombinations[i][0]].getActionCommand().equals("")) {
                winner = true;
                System.out.println("Winner is true");
            }
        }

        if (winner) 
        {
            greeting.setText("Winner!");
        }
            
            //If all disabled, stalemate. 
    }

   	
}

/*if listening for other player, when message comes in, find the locatino on the board, disable the button and add an icon. Then check if win.*/


