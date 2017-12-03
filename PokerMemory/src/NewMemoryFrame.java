import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.ActionListener;

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
                    
                    
                    else if(e.getActionCommand().equals("Combo Level")) newGame("combolevel");
                    
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
        
        JMenuItem comboLevellMenuItem = new JMenuItem("Combo Level");
        comboLevellMenuItem.addActionListener(menuHandler);
        memoryMenu.add(comboLevellMenuItem);
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
        

        	BorderLayout b1 = (BorderLayout) this.getContentPane().getLayout();
        	this.getContentPane().remove(b1.getLayoutComponent(BorderLayout.CENTER));
        	this.getContentPane().add(showCardDeck(), BorderLayout.CENTER);
        	
        	this.setVisible(true);
        	}
        else if(difficultyMode.equalsIgnoreCase("straightlevel")) {
          this.setGameLevel(new StraightLevel(this.getTurnCounterLabel(), this));
          this.getLevelDescriptionLabel().setText("Straight Level");
          this.getTurnCounterLabel().reset();
        

      	BorderLayout b1 = (BorderLayout) this.getContentPane().getLayout();
      	this.getContentPane().remove(b1.getLayoutComponent(BorderLayout.CENTER));
      	this.getContentPane().add(showCardDeck(), BorderLayout.CENTER);
      	
      	this.setVisible(true);
       
        }
        else if(difficultyMode.equalsIgnoreCase("combolevel")) {
            this.setGameLevel(new ComboLevel(this.getTurnCounterLabel(), this));
            this.getLevelDescriptionLabel().setText("Combo Level");
            this.getTurnCounterLabel().reset();
          

        	BorderLayout b1 = (BorderLayout) this.getContentPane().getLayout();
        	this.getContentPane().remove(b1.getLayoutComponent(BorderLayout.CENTER));
        	this.getContentPane().add(showCardDeck(), BorderLayout.CENTER);
        	
        	this.setVisible(true);
          }
        else if(difficultyMode.equalsIgnoreCase("equalpair")) {
            this.setGameLevel(new EqualPairScore(this.getTurnCounterLabel(), this));
            this.getLevelDescriptionLabel().setText("Equal Pair");
            this.getTurnCounterLabel().reset();
          

        	BorderLayout b1 = (BorderLayout) this.getContentPane().getLayout();
        	this.getContentPane().remove(b1.getLayoutComponent(BorderLayout.CENTER));
        	this.getContentPane().add(showCardDeck(), BorderLayout.CENTER);
        	
        	this.setVisible(true);
         
          }
   
        else {
            super.newGame(difficultyMode);
        }
      
    }

}
