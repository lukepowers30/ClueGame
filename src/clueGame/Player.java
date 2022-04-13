package clueGame;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public abstract class Player {
	private String name;
	private Color color;
	private int row, column;
	private Set<Card> hand;
	protected Map<Card, Player> seen;
	
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
		this.seen = new HashMap<Card, Player>();
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
		for (Map.Entry<Card, Player> entry: seen.entrySet()) {
			if (entry.getKey().getCardName().equals(name)) {
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
	
	public void drawPlayer(int cellWidth, int cellHeight, Graphics g) {
		g.setColor(color);
		g.fillOval(column * cellWidth, row * cellHeight, cellWidth, cellHeight);
		g.setColor(Color.black);
		g.drawOval(column * cellWidth, row * cellHeight, cellWidth, cellHeight);
	}
	
	public void drawPlayerInRoom(int cellWidth, int cellHeight, Graphics g, int offset) {
		g.setColor(color);
		g.fillOval((int)((column + offset * 0.5) * cellWidth), row * cellHeight, cellWidth, cellHeight);
		g.setColor(Color.black);
		g.drawOval((int)((column + offset * 0.5) * cellWidth), row * cellHeight, cellWidth, cellHeight);
	}
	
	public Color getColor() {
		return color;
	}

	public void updateSeen(Card card, Player player) {
		seen.put(card, player);
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

	public Map<Card, Player> getSeen() {
		return seen;
	}
	
	
}
