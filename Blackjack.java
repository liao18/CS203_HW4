import java.io.*;
import java.util.*;

/**
 * Blackjack
 *
 * This text-based game plays a simplified version of Blackjack with the user.
 *
 * @author Andrew Nuxoll
 * @author <Your name goes here>
 *
 * @version <the date goes here>
 */
public class Blackjack
{
    /** this constant defines how many cards are in the deck.  Always use this
     * constant in lieu of a literal 52. */
    public static final int NUM_CARDS = 52;
    
    /** This array will contain the names of each card in the deck */
    public String[] cardNames = new String[NUM_CARDS];

    /** This array specifies whether each card has been dealt or not */
    public boolean[] dealt = new boolean[NUM_CARDS];

    /** This array specifies the value of each card in the deck.  Arrays are
     * initialized to a value of '11' but that value may be treated as a '1'
     * during play. */
    public int[] cardValues = new int[NUM_CARDS];

    /**
     * initNames
     * this method initializes the card names
     */
    public void initNames()
    {
        String[] ranks = {"Ace", "King", "Queen", "Jack", "Ten",
                          "Nine", "Eight", "Seven", "Six", "Five",
                          "Four", "Trey", "Deuce"};
        String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};

        int index = 0;
        for(int i = 0; i < suits.length; ++i)
        {
            for(int j = 0; j < ranks.length; ++j)
            {
                cardNames[index] = ranks[j] + " of " + suits[i];
                index++;
            }
        }
    }//initNames

    /**
     * shuffle
     *
     * this method "shuffles" the deck by setting each entry in the dealt array
     * to 'false'
     */
    public void shuffle()
    {
        //YOU WILL IMPLEMENT THIS METHOD
        
    }//shuffle

    /**
     * initValues
     * 
     * This method initializes the entries in the cardValues array.  A value of
     * '11' is assigned for the aces.  The getHandValue() method may later treat
     * this as a '1' to prevent the player from exceeding 21.
     */
    public void initValues()
    {
        //YOU WILL IMPLEMENT THIS METHOD
        
    }//initValues

    /**
     * cardsDealt
     *
     * This method returns the total number of cards in the deck that have been
     * dealt so far
     */
    private int cardsDealt()
    {
        int total = 0;
        for(int i = 0; i < dealt.length; ++i)
        {
            if (dealt[i])
            {
                total ++;
            }
        }

        return total;
    }//cardsDealt
    
    /**
     * drawCard
     * 
     * draws a card from the deck.  It selects randomly from cards that have not
     * been dealt yet.  The selcted card is marked as 'dealt' and its index is
     * returned to the caller.
     */
    private int drawCard()
    {
        //YOU WILL IMPLEMENT THIS METHOD
        return 0;  //invalid value
        
    }//drawCard

    /**
     * getHandValue
     *
     * calculates the value of a player's current hand.  If the hand contains
     * any aces, the total calculated by this method may be adjusted to treat
     * the ace as having a value of 1 instead of 11.  This should be done only
     * if necessary and only the minimum necessary number of aces should get
     * this treatment.
     * 
     * Under no circumstances should the cardValues array be altered by this method.
     *
     * @param hand      an array of indexes indicating which cards are in the hand
     *
     * @return the hand's value
     */
    private int getHandValue(int[] hand)
    {
        //YOU WILL IMPLEMENT THIS METHOD
        return 21; //invalid value
        
    }//getHandValue
    
     

    /**
     * announceWinner
     *
     * given arrays representing the player's hand and the dealer's hand, this
     * method announces the outcome to the player by printing a message to the
     * console.  The following outcomes are possible: player busts, dealer
     * busts, dealer wins, player wins and a push (tie).
     *
     * @param playersHand    an array of indexes indicating which cards are in
     *                       the player's hand
     * @param dealersHand    an array of indexes indicating which cards are in
     *                       the dealer's hand
     */
    private void announceWinner(int[] playersHand, int[] dealersHand)
    {
        //YOU WILL IMPLEMENT THIS METHOD
        System.out.println("I don't know who won?!");
    }//announceWinner
    

    /**
     * mainLoop
     *
     * this method contains the main loop that plays Blackjack with the player
     */
    public void mainLoop()
    {
        Scanner kbd = new Scanner(System.in); //for getting user input

        //Invite the player to play a hand
        System.out.println("Would you like to play a hand of Blackjack?");
        String answer = kbd.nextLine();
        answer = answer.trim().toLowerCase();
        //Get the user's answer to (would you like to play?)
        if ( (answer.length() == 0) || (answer.charAt(0) != 'y') )
        {
            return;
        }
        boolean quit = false; //the user isn't ready to quit yet

        while(!quit)
        {
            //Shuffle the deck if needed
            if (cardsDealt() > NUM_CARDS / 2)
            {
                shuffle();
            }
            
            //initialize the hands
            int[] playerHand = new int[13]; //hand will never have more than 13 cards
            playerHand[0] = drawCard();
            playerHand[1] = drawCard();
            int playerNumCards = 2;
            int playerTotal = getHandValue(Arrays.copyOf(playerHand, playerNumCards));
            int[] dealerHand = new int[13];
            dealerHand[0] = drawCard();
            int dealerNumCards = 1;
            int dealerTotal = getHandValue(Arrays.copyOf(dealerHand, dealerNumCards));

            //Tell the user about the hands
            System.out.println("Your hand contains these cards:");
            System.out.println("\t" + cardNames[playerHand[0]]);
            System.out.println("\t" + cardNames[playerHand[1]]);
            System.out.println("for a total value of: " 
                               + getHandValue(Arrays.copyOf(playerHand, playerNumCards)) + ".");
            System.out.println("The dealer has drawn the " +
                               cardNames[dealerHand[0]]);

            //Allow the player to add cards to his hand
            System.out.println("Would you like a hit?");
            answer = kbd.nextLine();
            answer = answer.trim().toLowerCase();
            while ( (answer.length() > 0) && (answer.charAt(0) == 'y') )
            {
                //Add a new card to player's hand
                int newCard = drawCard();
                playerHand[playerNumCards] = newCard;
                playerNumCards++;
                System.out.println("You have drawn the " + cardNames[newCard]);
                playerTotal = getHandValue(Arrays.copyOf(playerHand, playerNumCards));
                System.out.println("Your hand's value is now " + playerTotal);

                //Did player bust?
                if (playerTotal > 21)
                {
                    break;
                }

                //Another hit?
                System.out.println("Would you like another hit?");
                answer = kbd.nextLine();
                answer = answer.trim().toLowerCase();
            }//while

            //Dealer only plays if player didn't bust
            if (playerTotal <= 21)
            {
                //Dealer must draw a second card
                dealerHand[1] = drawCard();
                dealerNumCards = 2;
                dealerTotal = getHandValue(Arrays.copyOf(dealerHand, dealerNumCards));
                System.out.println("The dealer draws the " + cardNames[dealerHand[1]]);
                System.out.println("Dealer's current total is: " + dealerTotal);

                //Dealer draws up to 17
                while(dealerTotal < 17)
                {
                    int newCard = drawCard();
                    dealerHand[dealerNumCards] = newCard;
                    dealerNumCards++;
                    dealerTotal = getHandValue(Arrays.copyOf(dealerHand, dealerNumCards));
                    System.out.println("The dealer draws the " + cardNames[newCard]);
                    System.out.println("Dealer's current total is: " + dealerTotal);
                }
            }//if

            //Announce the outcome
            announceWinner(Arrays.copyOf(playerHand, playerNumCards),
                           Arrays.copyOf(dealerHand, dealerNumCards));

            //Play again?
            System.out.println("Would you like to play again?");
            answer = kbd.nextLine();
            answer = answer.trim().toLowerCase();
            if ( (answer.length() == 0) || (answer.charAt(0) != 'y') )
            {
                quit = true;
            }
    
        }//while
    }//mainLoop


    /** kicks off the game */
    public static void main(String[] args)
    {
        Blackjack myGame = new Blackjack();
        myGame.initNames();
        myGame.shuffle();
        myGame.initValues();
        myGame.mainLoop();
    }//main
    
    

}//class Blackjack
