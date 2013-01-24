/*
 * 
 * TicTacToe - Window.java
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.OutputStream;

/**
* The class creates and controls the graphical user interface. 
* @author dfudger
*/
public class Window extends JFrame implements ActionListener 
{
    
    /*****************************  Variables ****************************/
    Game thisGame;
    private static final long serialVersionUID = 1L;
    JFrame box; //Where the game is created
    public static int theWIDTH = 300;
    public static int theHEIGHT = 400;
    JLabel greeting = new JLabel();
    JPanel gamepanel = new JPanel();
    static JButton button[] = new JButton[9];
    JButton clicked;
    int didWin = 0, didTie = 0;
    
    private int[][] winCombinations = new int[][]{
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, //horizontal wins
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, //vertical wins
        {0, 4, 8}, {2, 4, 6} //diagonal wins
    };
    
    
    /*****************************  Create Game ****************************/
    /**
     * Creates the gui window that becomes the tictactoe board.
     * Constructor does not take any parameters or returns anything.
     * Once created, the gui will be displayed by the program.
     */
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
            button[i] = new JButton();
            button[i].setActionCommand(""); //Will be filled with an x or o at gameplay
            button[i].setIcon(new ImageIcon(getClass().getResource("/blank.jpg")));
            button[i].addActionListener(this);
            gamepanel.add(button[i]);
        }
        //System.out.println("Button Length: " + button.length + "\n");
        box.setVisible(true);
    }

    /**
     * This function is called when a button on the game board is clicked.
     * This clicking action, sets off an action listener that records which button was clicked
     * and passes that onto another function to send the response to the server. 
     * @param ae
     */
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        int btnValue = -1; //Result should be at least 0
        
        //Get the button that was clicked from the GUI
        Object source = ae.getSource();
        clicked = (JButton) source;
        
        //Find the location of the button clicked on the board
        for (int i = 0; i < button.length; i++) 
        {
            if(button[i] == clicked) 
            {
                btnValue = i; //Save the location of the button within it's array
                //System.out.println("btnValue: " + btnValue + "\n");
                sendResponse(thisGame.out, btnValue); //Send the location of the button to the server
                break;
            }     
        }   
    }

    /**
     * This function uses the static button array located within this class.
     * For each button, an action listener is created. Once the action listener is
     * enabled, the user can click on a button in the game board and get a response.
     * If that button has already been clicked (has an X or O) then the button is not enables.
     */
    public void enableButtons()
    {
        //For every button in array, set an action listener 
        for (int i = 0; i < button.length; i++) 
        {
            //If the action command is ""
            if(button[i].getActionCommand().equals(""))
                button[i].addActionListener(this);    
        }   
    }

    /**
     * This function uses the static button array located within this class.
     * Each button on the game board is disabled so it cannot receive input once clicked by a user.
     */
    public void disableButtons()
    {
        //For every button in array, remove its action listener 
        for (int i = 0; i < button.length; i++) 
        {
            button[i].removeActionListener(this);
        }
    }
    
    /**
     * This function is passed the current game object and sets it to the declared object created within this class.
     * @param currGame
     */
    public void setGame(Game currGame)
    {
        thisGame = currGame;
    }
    
    /**
     * This function is passed a string from a game object. It is set in the JLabel and the string is displayed on the game board.
     * @param s
     */
    public void setGreeting(String s)
    {
        greeting.setText(s);
        
    }
    
    /**
     * This function is responsible for updating the buttons on the game board during the game.
     * It is passed an integer that describes the location of the button within the button array.
     * This specific button is given an action command and icon image depend on whose turn it is (X or O).
     * Once the button has been set, the function validates that the game is not over then updates the players screen.
     * 
     * @param move
     * @param turn
     */
    public void buttonChange(int move, boolean turn)
    {
        //System.out.println("buttonChange called for player: " + thisGame.getPlayerNum() + "\n\n");
        if(turn == true)
        {
            button[move].setActionCommand("X");
            button[move].setIcon(new ImageIcon(getClass().getResource("/xMove.jpg")));
        }
        else if(turn == false)
        {
            button[move].setActionCommand("O");
            button[move].setIcon(new ImageIcon(getClass().getResource("/oMove.jpg")));
        }
        else
        {
            System.err.println("ERROR: Not player one or two.");
        }
        
        //Validate is a win or stalemate
        didWin = winCheck();
        didTie = stalemateCheck();
        validateMove(didWin);
        validateMove(didTie);
        
        disableButtons(); //Turn is over, don't want the player to click anymore        this.repaint();
    }
    
    /**
     * This function checks the status of the tictactoe game after every move. 
     * If the integer moveMade equals 1 or 2, then one of the players have won.
     * If moveMade is 3, then there was no winner. Otherwise, the game continues.
     * @param moveMade
     */
    public void validateMove(int moveMade)
    {
        if(moveMade == 1)
        {
            if (thisGame.getPlayerNum() == 1)
            {
                //System.out.println("You win!!");
                JOptionPane.showMessageDialog(null, "You Win!");
                System.exit(0);
            }
            else if (thisGame.getPlayerNum() == 2)
            {
                //System.out.println("You lose!!");
                JOptionPane.showMessageDialog(null, "You Lose!");
                System.exit(0);
            }
        }
        
        else if (moveMade == 3)
        {
            //System.out.println("Stalemate!");
            JOptionPane.showMessageDialog(null, "Stalemate");
            System.exit(0);
        }
        else
        {
            //System.out.println("No win yet, keep going...\n");
            return;
        }
    }
    
    /**
     * This function loops through every winning combination possible on the game board for the player. If one of the winning combinations matches
     * the current state of the game board, then a 1 is returned, indicating that the game has been won.
     * @return game state
     */
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
                //System.out.println("Winner is true");
            }
        }

        if (winner) 
            return 1;
        else
            return 0;
    }    
    
    /*
     * This function loops through every button within the button array.
     * If all of these buttons have been clicked (their action commands are not empty,
     * then the game is a stalement. 
     */
    private int stalemateCheck() 
    {   
        for (int i = 0; i < button.length; i++) 
        {
            if(button[i].getActionCommand().equals("")) 
                return 0;
        }   
        return 3; //3 indicates stalemate in the validateMove function
    }

    /*
     * This function sends the location of the button on the game board to the server.
     * 
     * @param out
     * @param btnValue 
     */
    private void sendResponse(OutputStream out, int btnValue)
    {
        try 
        {
            //System.out.print("Double Check btnValue: " + btnValue + "\n");
            out = thisGame.socket.getOutputStream ();
            out.write(btnValue);
            //System.out.print("Sent btnValue\n\n");
        } 
        catch (IOException e) 
        {
            System.err.println("Couldn't write to server.");
            System.exit(-1);
        }
    }
    
    /**
     * This function creates the initial dialog box used to begin the game. 
     * Depending on the users response, they will either join a game, create a game, or exit the program.
     * 
     * The users response is sent back to the game.
     * @return the users response
     */
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




