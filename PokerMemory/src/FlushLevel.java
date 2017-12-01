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

public class FlushLevel extends GameLevel {
	long score=0;
	
	
	protected FlushLevel(TurnsTakenCounterLabel validTurnTime, JFrame mainFrame) {
		super(validTurnTime, 5, mainFrame);
		this.getTurnsTakenCounter().setDifficultyModeLabel("Flush Level");
		this.setCardsPerRow(10);
		this.setRowsPerGrid(5);
		this.setCardsToTurnUp(5);
		this.setTotalUniqueCards(this.getRowsPerGrid() * this.getCardsPerRow());
		this.getMainFrame().setScore(0);
		
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
				
				// get the other cards (which was already turned up)
				Card otherCard = (Card) this.getTurnedCardsBuffer().get(0);
				Card otherCard1 = (Card) this.getTurnedCardsBuffer().get(1);
				Card otherCard2 = (Card) this.getTurnedCardsBuffer().get(2);
				Card otherCard3 = (Card) this.getTurnedCardsBuffer().get(3);
				
				// the cards match, so remove them from the list (they will remain face up)
				String cardRank[] = {card.getRank(),otherCard.getRank(),otherCard1.getRank(),otherCard2.getRank(),otherCard3.getRank()};
			    int x[]=ScoreManager.setValues(cardRank);
				//Verifica que las 5 Cartas levantadas tengan el mismo Suit.
				if( otherCard.getSuit().equals( card.getSuit()) && otherCard1.getSuit().equals( card.getSuit()) && otherCard2.getSuit().equals( card.getSuit()) && otherCard3.getSuit().equals( card.getSuit()) ) {
					this.getTurnedCardsBuffer().clear();
					score = score + 700 +x[0]+x[1]+x[2]+x[3]+x[4] ;
					this.getMainFrame().setScore(score);
					}
				// the cards do not match, so start the timer to turn them down
				else 
					{this.getTurnDownTimer().start();
					
					}
			}
			return true;
		}
		// there are already the number of EasyMode (two face up cards) in the turnedCardsBuffer
		return false;
	}
	@Override
	public String getMode() {
		// TODO Auto-generated method stub
		return "Flush Level";
	}
	
	@Override
	protected boolean  isGameOver()
	{
		for (int i =0; i< this.getGrid().size();i++)
			if(!this.getGrid().get(i).isFaceUp()) return false;

		return true;
	}
}
