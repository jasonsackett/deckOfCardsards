/**
 * 
 */
package tld.deckofcards;

import java.util.LinkedList;
import java.util.Random;

import tld.deckofcards.Card.CardSuit;
import tld.deckofcards.Card.CardValue;

/**
 * @author jason
 * 
 * Code in Java a set of classes that represent a deck of poker-style
 * playing cards. (Fifty-two playing cards in four suits: hearts, spades, clubs, diamonds, with
 * face values of Ace, 2-10, Jack, Queen, and King.)
 * Within one of your classes, you must provide two operations:
 * 
 * 1. shuffle() Shuffle returns no value, but results in the cards in the deck being
 * randomly permuted. Please do not use library-provided “shuffle” operations to
 * implement this function. You may use library provided random number generators
 * in your solution if needed.
 * 
 * 2. dealOneCard() This function should return one card from the deck to the caller.
 * Specifically, a call to shuffle followed by 52 calls to dealOneCard() should result in
 * the caller being provided all 52 cards of the deck in a random order. If the caller
 * then makes a 53rd call dealOneCard(), no card is dealt.
 * 
 * 
 * Comments:
 * This is a fun exercise, obviously more about 'style',
 * (past following directions, and code that runs)
 * than applying algorithms.
 *
 * Could be done by selecting a random card from the remaining source deck, but a riffle shuffle is much more interesting.
 * Functional style, pseudo-physical rough shuffle simulation, instead of just a manual random reorder
 *
 * Also, something I did a long time ago (the easy way) in a friend's original language:
 *  https://github.com/stevedekorte/io/blob/master/samples/misc/Cards.io
 *
 * Could use some test cases, and automated test code
 *
 */
public class Deck {
	private final static Random random = new Random(System.currentTimeMillis());
	private final static int deckRoughSplitRange = 5; // at 5 this will be effectively -2 -1 0 1 2; see use below
	private final static int riffleShuffleChunkSizeRange = 3; // 0 to 2
	private final static int riffleShuffleCount = 7; //http://www.nytimes.com/1990/01/09/science/in-shuffling-cards-7-is-winning-number.html

	// ***member storage:

	// LinkedList is super simple to understand,
	// and this is a small list, by design, so no performance issues to be prematurely concerned about
	// and no synchronization issues on the horizon, and this can be easily made thread-safe if ever needed
	private LinkedList<Card> cards = null;
	
	// ***static methods 
	// (start with upper-case, like Classes, and best when functional (no side-effects))
	
	private static LinkedList<Card> CreateDeck() {
		LinkedList<Card> _cards = new LinkedList<>();
		for (CardSuit suit : CardSuit.values()) {
			for (CardValue value : CardValue.values()) {
				_cards.add(new Card(value, suit));}}
		return _cards;
	}
	
	// non-internal state change side-effect: printing to console
	public static void PrintDeckToConsole(Deck deck) {
		Card card = null;
		do {
			card = deck.dealOneCard();
			if (null == card) System.out.println("Deck is empty."); 
			else System.out.println(Card.CardtoString(card));
		} while (null != card);
	}

	// push the chunk from the hand to a return stack (as long as there are cards in the hand)
	// side-effect: hand may be changed
	private static LinkedList<Card> AddChunk(int chunkSize, LinkedList<Card> hand) {
		LinkedList<Card> stack = new LinkedList<>();
		while (null != hand && !hand.isEmpty() && chunkSize-- > 0) stack.push(hand.pop());
		return stack;
	}

	// pseudo-physical shuffle simulation
	// side-effect: mixes the cards in the deck a bit
	public static LinkedList<Card> shuffleOnce(LinkedList<Card> cards) {
		if (cards == null) cards = CreateDeck();
		LinkedList<Card> shuffled = new LinkedList<>();
		int deckSize = cards.size();
		if (deckSize == 0) return shuffled; // no source, empty shuffle result
		// first split deck (roughly)
		// split in middle +/- 0, 1, or 2 if deckRoughSplitRange is e.g.: 5
		// e.g.: at 5, it's 0 to 4 cards, 2 == even split (if deck size is even, also OK if not)
		int splitLocation = deckSize/2 - deckRoughSplitRange/2 + random.nextInt(deckRoughSplitRange);
		if (splitLocation < 0) splitLocation = 0;
		else if (splitLocation >= deckSize) splitLocation = deckSize - 1;
		LinkedList<Card> leftHand = new LinkedList<>(cards.subList(0,splitLocation));
		LinkedList<Card> rightHand = new LinkedList<>(cards.subList(splitLocation,deckSize));
		// interleave into output stack (roughly)
		while (!leftHand.isEmpty() || !rightHand.isEmpty()) {
			int chunkLeftSize = random.nextInt(riffleShuffleChunkSizeRange); // at 3: 0, 1, or 2 cards in a chunk
			int chunkRightSize = random.nextInt(riffleShuffleChunkSizeRange);
			if (chunkLeftSize == 0 && chunkRightSize == 0) continue; // minor optimization
			boolean leftFirst = random.nextInt(2) == 0; // which hand hits first this round
			if (leftFirst) {
				shuffled.addAll(AddChunk(chunkLeftSize, leftHand));
				shuffled.addAll(AddChunk(chunkRightSize, rightHand));
			} else {
				shuffled.addAll(AddChunk(chunkRightSize, rightHand));
				shuffled.addAll(AddChunk(chunkLeftSize, leftHand));
			}
		}
		return shuffled;
	}
	
	// instance methods
	
	// instance method, as per specification
	// side-effect: likely alters the deck
	public Card dealOneCard() {
		if (cards == null) cards = CreateDeck();
		if (cards.isEmpty()) return null;
		return cards.pop();
	}

	// instance method, as per specification
	// seven standard rough shuffles, see link at top of class
	public void shuffle() {
		for (int i = 0; i < riffleShuffleCount; i++) cards = shuffleOnce(cards);
	}
	
	// Main, for testing Deck and Card classes
	public static void main(String[] args) {
		Deck deck = new Deck();
		deck.shuffle();
		Deck.PrintDeckToConsole(deck);
	}
}
