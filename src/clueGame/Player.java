package clueGame;
import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public abstract class Player {
	private String name;
	private Color color;
	private int row, column;
	private Set<Card> hand;
	protected Set<Card> seen;
	
	public String getName () {
		return name;
	}
	
	public Set<Card> getHand() {
		return hand;
	}

	public void updateHand (Card card) {
		hand.add(card);
	}

	public Player(String name, Color color, int row, int column) {
		super();
		this.name = name;
		this.color = color;
		this.row = row;
		this.column = column;
		this.hand = new HashSet<Card>();
	}
	
	
	public Card disproveSuggestion(Solution suggestion) {
		return null;
	}
	
	public void updateSeen(Card card) {
		seen.add(card);
	}
}
