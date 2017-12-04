/**
 * Stores currently turned cards
 * also handles turning cards back down after a delay.
 *
 * @author Michael Leonhard (Original Author)
 * @author Modified by Bienvenido Vélez (UPRM)
 * @version Sept 2017
 */

import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.Arrays;
public class StraightLevel extends GameLevel {

	long score=0;
	int successfulTurns= 0;
	
	protected StraightLevel(TurnsTakenCounterLabel validTurnTime, JFrame mainFrame) {
		super(validTurnTime, 5, mainFrame);
		this.getTurnsTakenCounter().setDifficultyModeLabel("Straight Level");
		this.setCardsPerRow(10);
		this.setRowsPerGrid(5);
		this.setCardsToTurnUp(5);
		this.setTotalUniqueCards(this.getRowsPerGrid() * this.getCardsPerRow());
		this.getMainFrame().setScore(score);
	}

	@Override
	protected void makeDeck() {
		// Creates a deck to fill the 5x10 grid with all cards different from each other
		ImageIcon backIcon = this.getCardIcons()[this.getTotalCardsPerDeck()];

		// make an array of card numbers: 0, 0, 1, 1, 2, 2, ..., 7, 7
		// duplicate the image in as many cards as the input imageClones
		int totalCardsInGrid = getRowsPerGrid() * getCardsPerRow();
		int totalUniqueCards = totalCardsInGrid;

		// Generate one distinct random card number for each unique card	
		int cardsToAdd[] = new int[totalCardsInGrid];
		boolean cardChosen[] = new boolean[getTotalCardsPerDeck()];

		int chosenCount = 0;
		Random rand = new Random();
		while (chosenCount < totalUniqueCards)
		{
			int nextCardNo = rand.nextInt(getTotalCardsPerDeck());
			if (!cardChosen[nextCardNo]) {
				cardChosen[nextCardNo] = true;
				cardsToAdd[chosenCount] = nextCardNo;
				chosenCount++;
			}
		}

		// randomize the order of the cards
		this.randomizeIntArray(cardsToAdd);

		// make each card object and add it to the game grid
		for(int i = 0; i < cardsToAdd.length; i++)
		{
			// number of the card, randomized
			int num = cardsToAdd[i];
			// make the card object and add it to the panel
			String rank = cardNames[num].substring(0, 1);
			String suit = cardNames[num].substring(1, 2);
			this.getGrid().add( new Card(this, this.getCardIcons()[num], backIcon, num, rank, suit));
		}
	}

	@Override
	protected boolean turnUp(Card card) {
		
		// Turn up any card until all are turned up.
		if(this.getTurnedCardsBuffer().size() < getCardsToTurnUp()) 
		{
			this.getTurnedCardsBuffer().add(card);
			
			
			if(this.getTurnedCardsBuffer().size() == getCardsToTurnUp())
			{
				// there are five cards faced up
				// record the player's turn
				this.getTurnsTakenCounter().increment();
				
				// get the other cards that are face up.
				Card otherCard = (Card) this.getTurnedCardsBuffer().get(0);
				Card otherCard1 = (Card) this.getTurnedCardsBuffer().get(1);
				Card otherCard2 = (Card) this.getTurnedCardsBuffer().get(2);
				Card otherCard3 = (Card) this.getTurnedCardsBuffer().get(3);
				
				
				//Guarda los "Suits" de las cartas seleccionadas.
				String cardSuit[] = {card.getSuit(),otherCard.getSuit(),otherCard1.getSuit(),otherCard2.getSuit(),otherCard3.getSuit()};
				//Guarda los "Ranks" de las cartas seleccionadas.
				String cardRank[] = {card.getRank(),otherCard.getRank(),otherCard1.getRank(),otherCard2.getRank(),otherCard3.getRank()};
				
				int[] x = ScoreManager.setValues(cardRank);
						
				//Rearegla el array para que el numero  esten sorteadas
				 Arrays.sort(x);
				 
				boolean sameSuit= true;
				//Verifica que las 5 Cartas levantadas no tengan el mismo Suit.
				for(int i=0;i<this.getCardsToTurnUp();i++) {
					for(int j=i+1;j<this.getCardsToTurnUp();j++) {
					if(j!=i && !(cardSuit[i].equals(cardSuit[j])))
						sameSuit=false;}
				}
				int[] a = {10,11,12,13,20};
				int[] b = {2,3,4,5,20};
				boolean sequential=false;

					if(isSeq(x) || areEqual(x, a) || areEqual(x, b)) {

						sequential=true;
					}
					else
						sequential=false;
					
					// Verifica si las cartas cumple con el criterio del juego, de ser asi Las mantiene levantadas
				if( sameSuit==false && sequential==true) {
					
					this.getTurnedCardsBuffer().clear();
					score = score + 1000 + (100*x[4]);
					this.getMainFrame().setScore(score);
					
					successfulTurns=+1;
					}
				// the cards do not match, so start the timer to turn them down
				else 
					{this.getTurnDownTimer().start();
					if(score>=5) {
						 score = score -5;
						this.getMainFrame().setScore(score); }
					
					}
			}
			if(successfulTurns==7) {
				String[] options= {"Exit"};
				int boxOptions= JOptionPane.showOptionDialog(null, "No Winning Hands Left \n ", "Game Over", JOptionPane.INFORMATION_MESSAGE, 0, null, options, options[0]);
				System.out.println(boxOptions);
				
					if(boxOptions==0) {
					System.exit(0);
					}
				}
			
			return true;
			
			
		
		}
		
		return false;
	}
	
	public static boolean isSeq(int[] a) {
		
		for(int i=0; i<a.length-1;i++) {
			if(!(a[i]==a[i+1]-1)) return false;
		}
		return true;
	}
	
	public static boolean areEqual(int[] a, int[] b) {
		for(int i=0; i<a.length;i++) {
			if(a[i]!=b[i]) return false;
		}
		return true;
	}
	
	
	@Override
	public String getMode() {
		// TODO Auto-generated method stub
		return "Straight Level";
	}
	
	@Override
	protected boolean  isGameOver()
	{
		for (int i =0; i< this.getGrid().size();i++)
			if(!this.getGrid().get(i).isFaceUp()) return false;

		return true;
	}
}





