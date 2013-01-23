package a1;


import java.io.*;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author dfudger
 */
public class A1 //extends Thread 
{
    
    /**
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
    
