package clueGame;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
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
		this.seen = new HashSet<Card>();
	}
	
	
	public Card disproveSuggestion(Solution suggestion) {
		ArrayList<Card> matches = new ArrayList<Card>();
		for(Card c: hand) {
			if(c == suggestion.getPerson()) {
				matches.add(c);
			}
			if(c == suggestion.getWeapon()) {
				matches.add(c);
			}
			if(c == suggestion.getRoom()) {
				matches.add(c);
			}
		}
		
		if(matches.size() == 0) {
			return null;
		}else if(matches.size() == 1) {
			return matches.get(0);
		}else {
			Random rand = new Random();
			return matches.get(rand.nextInt(matches.size()));
		}
	}
	
	public boolean seenOrHandContainsName(String name) {
		for (Card c: seen) {
			if (c.getCardName().equals(name)) {
				return true;
			}
		}
		for (Card c: hand) {
			if (c.getCardName().equals(name)) {
				return true;
			}
		}
		return false;
		
	}
	
	public Color getColor() {
		return color;
	}

	public void updateSeen(Card card) {
		seen.add(card);
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public Room getRoom() {
		Board board = Board.getInstance();
		return board.getRoom(board.getCell(row, column));
	}

	public Set<Card> getSeen() {
		return seen;
	}
	
	
}
