/*
 * 
 * TicTacToe - A1.java
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

/**
 * A1 is the main class for this project. A1 game sets up the gui and begins the game play.
 * @author dfudger
 */
public class A1 //extends Thread 
{
    
    /**
     * Main runs the tictactoe game.
     * The program creates the gui to be used by each player.
     * It then creates a game object that holds the game logic and the server that will connect the two players.
     * The game is set up (setting up the two players), followed by the first players turn.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Window gui = new Window();
        Game currentGame = new Game(gui);
        gui.setGame(currentGame);
        currentGame.setGUI(gui);
        currentGame.startGame();
        gui.setVisible(true);
        currentGame.playGame();
    } 
}
    
