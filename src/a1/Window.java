/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
*
* @author dfudger
*/
public class Window extends JFrame implements ActionListener {
    
    /*****************************  Variables ****************************/
    Game thisGame;
    private static final long serialVersionUID = 1L;
    JFrame box;
    public static final int theWIDTH = 300;
    public static final int theHEIGHT = 400;
    JLabel greeting = new JLabel();
    JPanel gamepanel = new JPanel();
    static JButton button[] = new JButton[9];
    JButton clicked;
    int didWin = 0, didTie = 0;
    int turn; //X's turn first
    //private boolean winner = false;
    
    
    private String playerOneImg = "images/xMove.jpg";
    private String playerTwoImg = "images/oMove.jpg";
    
    private int[][] winCombinations = new int[][]{
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, //horizontal wins
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, //vertical wins
        {0, 4, 8}, {2, 4, 6} //diagonal wins
    };
    
    /*****************************  Create Game ****************************/
    public Window() 
    {
        
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
            button[i].setIcon(new ImageIcon(getClass().getResource("/blank.jpg")));
            button[i].addActionListener(this);
            gamepanel.add(button[i]);
        }
        System.out.println("Button Length: " + button.length + "\n");
        box.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        
        
        int btnValue = -1;
        
        //Get the button that was clicked from the GUI
        Object source = ae.getSource();
        clicked = (JButton) source;
        
        //Find the location of the button clicked on the board
        for (int i = 0; i < button.length; i++) 
        {
            
            if(button[i] == clicked) 
            {
                btnValue = i;
                System.out.println("btnValue: " + btnValue + "\n");
                sendResponse(thisGame.out, btnValue);
                
                break;
            }     
        }   
    }

    public void enableButtons()
    {
        //For every button in array
        for (int i = 0; i < button.length; i++) 
        {
            //If the action command is ""
            if(button[i].getActionCommand().equals(""))
            {
                button[i].addActionListener(this);
            }    
                //enable button
        }
        
    }

    public void disableButtons()
    {
        //For every button in array
        for (int i = 0; i < button.length; i++) 
        {
            //If the action command is ""
            //if(button[i].getActionCommand().equals(""))
            //{
                button[i].removeActionListener(this);
            //}    
                //enable button
        }
        
    }
    
    public void setGame(Game currGame)
    {
        thisGame = currGame;
    }
    
    /*
     * On players turn, take the button clicked and add the letter image, set the button type, and disable so it can't be clicked. 
     */
    
    public void setGreeting(String s)
    {
        greeting.setText(s);
        
    }
    
    public void buttonChange(int move, boolean turn)
    {
        System.out.println("buttonChange called for player: " + thisGame.getPlayerNum() + "\n\n");
        if(turn == true)
        {
            button[move].setActionCommand("X");
            button[move].setIcon(new ImageIcon(getClass().getResource("/xMove.jpg")));
        }
        else if(turn == false)
        {
            button[move].setActionCommand("O");
            
            //URL url = 
            
            button[move].setIcon(new ImageIcon(getClass().getResource("/oMove.jpg")));
        }
        else
        {
            System.err.println("ERROR: Not player one or two.");
        }
        
        didWin = winCheck();
        didTie = stalemateCheck();
        validateMove(didWin);
        validateMove(didTie);
        //this.validate();
        disableButtons();
        
        
        this.repaint();
        
        
        //button.removeActionListener(this);
        
    }
    
    public void validateMove(int moveMade)
    {
        System.out.append("VALIDATE MOVE: " + moveMade + "\n");
        
        if(moveMade == 1)
        {
            if (thisGame.getPlayerNum() == 1)
            {
                System.out.println("You win!!");
                JOptionPane.showMessageDialog(null, "You Win!");
                System.exit(1);
            }
            else if (thisGame.getPlayerNum() == 2)
            {
                System.out.println("You lose!!");
                JOptionPane.showMessageDialog(null, "You Lose!");
                System.exit(1);
            }
        }
        
        else if (moveMade == 3)
        {
            System.out.println("Stalemate!");
            JOptionPane.showMessageDialog(null, "Stalemate");
            System.exit(1);
        }
        else
        {
            System.out.println("No win yet, keep going...\n");
            return;
        }
    }
    
    public int winCheck()
    {
        boolean winner = false;
        
        for (int i = 0; i <= 7; i++) 
        {
            if (button[winCombinations[i][0]].getActionCommand().equals(button[winCombinations[i][1]].getActionCommand())
                    && button[winCombinations[i][1]].getActionCommand().equals(button[winCombinations[i][2]].getActionCommand())
                    && !button[winCombinations[i][0]].getActionCommand().equals("")) 
            {
                winner = true;
                System.out.println("Winner is true");
                //break;
            }
        }

        if (winner) 
            return 1;
        
        else
            return 0;
    }    
    
   
    private int stalemateCheck() 
    {
        boolean tie = false;
        
        for (int i = 0; i < button.length; i++) 
        {
            
            if(button[i].getActionCommand().equals("")) 
            {
               tie = false; 
               return 0;
               //break;
            }
            
        }   
        tie = true;
        return 3;
    }

    private void sendResponse(OutputStream out, int btnValue)
    {
        try 
        {
            System.out.print("Double Check btnValue: " + btnValue + "\n");
            out = thisGame.socket.getOutputStream ();
            out.write(btnValue);
            System.out.print("Sent btnValue\n\n");
        } 
        catch (IOException e) 
        {
            System.err.println("Couldn't write to server.");
            System.exit(-1);
        }
    }
    
    public static int gameDialog()
    {
        Object[] options = 
        {   
            "Exit", "Join Game","New Game"
        };

        
        
        int n = JOptionPane.showOptionDialog(null,
            "Welcome to Tic Tac Toe. Choose an option:",
            "Tic Tac Toe",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[2]);
        
        return n;
    }
    
}




