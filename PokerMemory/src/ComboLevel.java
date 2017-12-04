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
public class ComboLevel extends GameLevel {

	long score=0;
	
	protected ComboLevel(TurnsTakenCounterLabel validTurnTime, JFrame mainFrame) {
		super(validTurnTime, 5, mainFrame);
		this.getTurnsTakenCounter().setDifficultyModeLabel("Combo Level");
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
			card.turnUp();
			
			if(this.getTurnedCardsBuffer().size() == getCardsToTurnUp()) {
				// Menu que permite seleccionar el Estilo de carta que deseas jugar.
				String[] buttons= {"Straight", "Flush","4-of-a-kind","PASS"};
				int handSelector= JOptionPane.showOptionDialog(null, "Select Hand", "Hand Selection", JOptionPane.INFORMATION_MESSAGE, 0, null, buttons, buttons[3]);
				System.out.println(handSelector);
				
				
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
					
					//Escoger eltipo de Mano seleccionada.
					if(handSelector==0) {
						if( sameSuit==false && sequential==true) {
							
							this.getTurnedCardsBuffer().clear();
							score = score + 1000 + (100*x[4]);
							this.getMainFrame().setScore(score);
							
							}
						// the cards do not match, so start the timer to turn them down
						else 
							{this.getTurnDownTimer().start();
							  if(score>=10) {
								 score -= 10;
								 this.getMainFrame().setScore(score);}
							 			
							}
					}
					//Escoger el tipo de Mano Seleccionada.
					if(handSelector==1) {
							//Verifica que las 5 Cartas levantadas tengan el mismo Suit.
							if( otherCard.getSuit().equals( card.getSuit()) && otherCard1.getSuit().equals( card.getSuit()) && otherCard2.getSuit().equals( card.getSuit()) && otherCard3.getSuit().equals( card.getSuit()) ) {
								this.getTurnedCardsBuffer().clear();
								score = score + 700 +x[0]+x[1]+x[2]+x[3]+x[4] ;
								this.getMainFrame().setScore(score);
								}
							// the cards do not match, so start the timer to turn them down
							else 
								{this.getTurnDownTimer().start();
								 if(score>=10) {
									 score -= 10;
									this.getMainFrame().setScore(score);
								 }
								}
					}
					if (handSelector == 2) {
						int [] setOfFour = Arrays.copyOfRange(x, 0, 4);
						int[] y = new int[4];
						Arrays.fill(y,  setOfFour[0]);
						
						if (Arrays.equals(y, setOfFour)) {
							this.getTurnedCardsBuffer().clear();
							score = score + 150 + y[0]*4;
							this.getMainFrame().setScore(score);
						} else {
							
							setOfFour = Arrays.copyOfRange(x, 1, 5);
					        Arrays.fill(y, setOfFour[0]);
						
						    if (Arrays.equals(y, setOfFour)) {
						    	this.getTurnedCardsBuffer().clear();
						    	score = score + 150 + y[1]*4;
						    	this.getMainFrame().setScore(score);
						    } else {
						    	this.getTurnDownTimer().start();
						    	if (score >= 10) {
						    		score -= 10;
						    		this.getMainFrame().setScore(score);
						    	}
						    }
					    }
					}
					if(handSelector==3){
						this.getTurnDownTimer().start();
						if (score >= 5) {
							score -= 5;
							this.getMainFrame().setScore(score);
						}
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
		return "Combo Level";
	}
	
	@Override
	protected boolean  isGameOver()
	{
		for (int i =0; i< this.getGrid().size();i++)
			if(!this.getGrid().get(i).isFaceUp()) return false;

		return true;
	}
}





