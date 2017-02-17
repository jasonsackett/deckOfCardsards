package tld.deckofcards;

// Dead simple 'poker' card class
public class Card {
	
	public enum CardValue {
		Ace, Two, Three, Four, Five,
		Six, Seven, Eight, Nine, Ten,
		Jack, Queen, King
	}

	public enum CardSuit {
		Clubs, Diamonds, Hearts, Spades
	}
	
	// member storage:
	CardValue value;
	CardSuit suit;
	
	// c'tor
	public Card(CardValue v, CardSuit s) { value = v; suit = s; }
	
	// static methods start with upper-case, like Classes, and best when functional (no side-effects)
	public static String CardtoString(Card card) {
		return String.format("%s of %s", card.value.toString(), card.suit.toString());
	}
}
