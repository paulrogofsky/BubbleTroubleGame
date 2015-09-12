/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

// imports necessary libraries for Java swing
import java.awt.*;
//import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/** 
 * Game
 * Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    
    public void run(){
        // NOTE : recall that the 'final' keyword notes inmutability
		  // even for local variables. 

        // Top-level frame in which game components live
		  // Be sure to change "TOP LEVEL FRAME" to the name of your game
        //final ArrayList <Integer> highScores = new ArrayList <Integer> ();
        final JFrame frame = new JFrame("Bubble Trouble 3HUNNA");
        frame.setLocation(300,300);

		  // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Press play to play");
        status_panel.add(status);

        // Main playing area
        final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel(new GridLayout (2,4));
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset
        // button, we define it as an anonymous inner class that is 
        // an instance of ActionListener with its actionPerformed() 
        // method overridden. When the button is pressed,
        // actionPerformed() will be called.
        final JButton pause = new JButton ("Pause");
        final JButton quit = new JButton ("Exit");
        final JButton nextLevel = new JButton ("Next Level");
        final JButton instructions = new JButton ("Instructions");
        final JButton HighScores = new JButton ("High Scores");
        final JButton previousLevel = new JButton ("Previous Level");
        final JButton home = new JButton ("Home");
        final JButton play = new JButton ("Play");
        pause.addActionListener (new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                if (court.playing) {
                    court.playing = false; pause.setText("Continue");
                }
                else if (pause.getText().equals("Continue")){
                    pause.setText ("Pause");
                    court.cont();
                }
            }
        });      
        quit.addActionListener (new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                System.exit(0);
            }
        });      
        nextLevel.addActionListener (new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                if ((court.playing || pause.getText().equals("Continue") )
                        && court.level < court.totalLevels) {
                    court.nextLevel(true); pause.setText("Pause");
                }
            }
        }); 
        instructions.addActionListener (new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                if (!court.playing && pause.getText().equals("Pause") && 
                        instructions.getText().equals("Instructions")
                        &&!HighScores.getText().equals("Leave High Scores")) {
                    court.instructions = true;
                    instructions.setText("Leave Instructions");
                }
                else if (!court.playing && pause.getText().equals("Pause") && 
                        instructions.getText().equals("Leave Instructions")) {
                    court.instructions = false;
                    instructions.setText("Instructions");
                }
                else if (court.playing) court.requestFocusInWindow();
            }
        });        
        HighScores.addActionListener (new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                if (!court.playing && pause.getText().equals("Pause") && 
                        HighScores.getText().equals("High Scores") &&
                        !instructions.getText().equals("Leave Instructions")) {
                    court.highScores = true;
                    HighScores.setText("Leave High Scores");
                }
                else if (!court.playing && pause.getText().equals("Pause") && 
                        HighScores.getText().equals("Leave High Scores")) {
                    court.highScores = false;
                    HighScores.setText("High Scores");
                }   
                else if (court.playing) court.requestFocusInWindow();
            }
        });
        previousLevel.addActionListener (new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                if ((court.playing || pause.getText().equals("Continue"))
                        && court.level>1) {
                    court.level --; court.lives ++; 
                    court.nextLevel(false); pause.setText("Pause"); 
                }
            }
        });      
        home.addActionListener (new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                court.reset(); pause.setText("Pause");
                instructions.setText("Instructions");
                HighScores.setText("High Scores");
                status.setText("Press play to play");
            }
        });  
        play.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                if (!court.playing && pause.getText().equals("Pause") &&
                        instructions.getText().equals("Instructions") &&
                        HighScores.getText().equals("High Scores")) {
                    court.reset(); 
                    court.nextLevel(true);
                    pause.setText("Pause");
                    instructions.setText("Instructions");
                }
                else if (court.playing) court.requestFocusInWindow();
            }
        });
        control_panel.add(play);
        control_panel.add (pause);
        control_panel.add(quit);
        control_panel.add(instructions);
        control_panel.add(nextLevel);
        control_panel.add(previousLevel);
        control_panel.add(home);
        control_panel.add(HighScores);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Game());
    }
}
