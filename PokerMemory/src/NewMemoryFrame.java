import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class NewMemoryFrame extends MemoryFrame {

    public NewMemoryFrame() {
        super();
        JMenuBar menuBar = this.getJMenuBar();
        JMenu memoryMenu = null ;
        
        ;
    
    

       for (int i = 0; i < menuBar.getMenuCount(); i++) {
            if (menuBar.getMenu(i).getText().equals("Memory")) {
                memoryMenu = menuBar.getMenu(i);
               break;
            }
        }
    
 
        
        ActionListener menuHandler = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	dprintln("actionPerformed " + e.getActionCommand());
                try {
                    if(e.getActionCommand().equals("Flush Level")) newGame("flushlevel");
                    
                    else if(e.getActionCommand().equals("Straight Level")) newGame("straightlevel");
               } catch (IOException e2) {
                    e2.printStackTrace(); throw new RuntimeException("IO ERROR");
                }
            }
        };
        
// Agrega la opcion de escoger el nivel Flush Level al Menu "Memory"
        JMenuItem flushLevelMenuItem = new JMenuItem("Flush Level");
        flushLevelMenuItem.addActionListener(menuHandler);
        memoryMenu.add(flushLevelMenuItem);
     // Agrega la opcion de escoger el nivel straight Level al Menu "Memory"
        JMenuItem straightLevelMenuItem = new JMenuItem("Straight Level");
        straightLevelMenuItem.addActionListener(menuHandler);
        memoryMenu.add(straightLevelMenuItem);
    }
    

    /**
     * Prepares a new game (first game or non-first game)
     * @throws IOException 
     */
    public void newGame(String difficultyMode) throws IOException
    {
       // Reset the turn counter label
        this.getTurnCounterLabel().reset();

        // make a new card field with cards, and add it to the window
        
// Comienza un "New Game" del Difficulty mode seleccionado en el menu.
        if(difficultyMode.equalsIgnoreCase("flushlevel")) {
            this.setGameLevel(new FlushLevel(this.getTurnCounterLabel(), this));
            this.getLevelDescriptionLabel().setText("Flush Level");
          this.getTurnCounterLabel().reset();
        
          
           // clear out the content pane (removes turn counter label and card field)
            BorderLayout bl  = (BorderLayout) this.getContentPane().getLayout();
            this.getContentPane().remove(bl.getLayoutComponent(BorderLayout.CENTER));
            this.getContentPane().add(showCardDeck(), BorderLayout.CENTER);

            // show the window (in case this is the first game)
            this.setVisible(true);
        }
        else if(difficultyMode.equalsIgnoreCase("straightlevel")) {
          this.setGameLevel(new StraightLevel(this.getTurnCounterLabel(), this));
          this.getLevelDescriptionLabel().setText("Straight Level");
        this.getTurnCounterLabel().reset();
        BorderLayout bl  = (BorderLayout) this.getContentPane().getLayout();
        this.getContentPane().remove(bl.getLayoutComponent(BorderLayout.CENTER));
        this.getContentPane().add(showCardDeck(), BorderLayout.CENTER);
        }
        
        
        else {
            super.newGame(difficultyMode);
        }
    }

}
