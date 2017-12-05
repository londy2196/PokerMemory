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
	int choices=0;
	int successfulTurns=0;
	
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
			
			
			if(this.getTurnedCardsBuffer().size() == getCardsToTurnUp())
			{
				//Forza a las cartas a levantarse una vez seleccionada.
				card.faceUp();
				
				// Menu que permite seleccionar el Estilo de carta que deseas jugar.
				
				String[] buttons= {"Flush Hand","Straight Hand","New Hand","Pass"};
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
				
				
				
				
				//Array que guarda los valores de los Ranks en forma de integers.
				int[] x = ScoreManager.setValues(cardRank);
						
				//Rearegla el array para que el numero  esten sorteados de menor a mayor.
				 Arrays.sort(x);
				 
				boolean sameSuit= true;
				
				//Verifica que las 5 Cartas levantadas no tengan el mismo Suit.
				//Si no tienen el mismo suit devuelve falso.
				for(int i=0;i<this.getCardsToTurnUp();i++) {
					for(int j=i+1;j<this.getCardsToTurnUp();j++) {
					if(j!=i && !(cardSuit[i].equals(cardSuit[j])))
						sameSuit=false;}
				}
				//Arrays que contienen los dos casos especiales de "Wining hand" para el straight level.
				int[] a = {10,11,12,13,20};
				int[] b = {2,3,4,5,20};
				boolean sequential=false;

				//Verifica que los parametros del juego se cumplan para otorgar puntuacion y dejar cartas levantadas.
					if(isSeq(x) || areEqual(x, a) || areEqual(x, b)) {

						sequential=true;
					}
					else
						sequential=false;
					
					//Escoger eltipo de Mano seleccionada.
					if(handSelector==0) {
						
					    
						//Verifica que las 5 Cartas levantadas tengan el mismo Suit.
						if( otherCard.getSuit().equals( card.getSuit()) && otherCard1.getSuit().equals( card.getSuit()) && otherCard2.getSuit().equals( card.getSuit()) && otherCard3.getSuit().equals( card.getSuit()) ) {
							this.getTurnedCardsBuffer().clear();
							score = score + 700 +x[0]+x[1]+x[2]+x[3]+x[4] ;
							this.getMainFrame().setScore(score);
							
							successfulTurns=+1;
						
							}
						// the cards do not match, so start the timer to turn them down
						else 
							{this.getTurnDownTimer().start();
							this.isGameOver();
							
							 if(score>=5) {
								 score = score -5;
								this.getMainFrame().setScore(score);
								
							 }
							}
				}
					//Si se escoge el boton numero 2 correr el programa para winning hand de Straight Hand.
					if(handSelector==1) {
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
								 this.getMainFrame().setScore(score);}
							 			
							}
					}
					// Corre el codigo para la mano nueva generada.
					if (handSelector == 2) {
						int [] setOfFour = Arrays.copyOfRange(x, 0, 4);
						int[] y = new int[4];
						Arrays.fill(y,  setOfFour[0]);
						
						if (Arrays.equals(y, setOfFour)) {
							System.out.println(handSelector);
							this.getTurnedCardsBuffer().clear();
							score = score + 150 + y[0]*4;
							this.getMainFrame().setScore(score);
							
							successfulTurns=+1;
						}
							
							
						 else {
							
							setOfFour = Arrays.copyOfRange(x, 1, 5);
					        Arrays.fill(y, setOfFour[0]);
						
						    if (Arrays.equals(y, setOfFour)) {
						    	System.out.println(handSelector);
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
					
					//Si selecciona pass, bajar las cartas.
					
					if(handSelector==3){
						this.getTurnDownTimer().start();
					}
			}
			// Si el jugador a obtenido 7 Winning hands, no quedan mas Wining hands, se acaba el juego
			if(successfulTurns==7) {
				String[] options= {"Exit"};
				int boxOptions= JOptionPane.showOptionDialog(null, "Congrats!! Your have beaten this level \n  ", "Game Over", JOptionPane.INFORMATION_MESSAGE, 0, null, options, options[0]);
				System.out.println(boxOptions);
				
					if(boxOptions==0) {
					System.exit(0);
					}
				}
			return true;
		}
		
		
		
		return false;
	}
	//metodo creado para verificar si las 5 cartas seleccionadas estan en secuencia.
	public static boolean isSeq(int[] a) {
		
		for(int i=0; i<a.length-1;i++) {
			if(!(a[i]==a[i+1]-1)) return false;
		}
		return true;
	}
	//Metodo creado para verificar si de las 5 cartas seleccionadas, existen repeticiones.
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





