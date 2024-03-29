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
	private boolean movedToRoom;
	private boolean notBusy;
	

	protected Map<Card, Player> seen;
	
	public boolean isMovedToRoom() {
		return movedToRoom;
	}

	public void setMovedToRoom(boolean movedToRoom) {
		this.movedToRoom = movedToRoom;
	}
	
	public boolean isNotBusy() {
		return notBusy;
	}

	public void setNotBusy(boolean notBusy) {
		this.notBusy = notBusy;
	}

	public String getName () {
		return name;
	}
	
	public Set<Card> getHand() {
		return hand;
	}
	
	public void setHand(Set<Card> newHand) {
		this.hand = newHand;
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
		this.movedToRoom = false;
		this.hand = new HashSet<Card>();
		this.seen = new HashMap<Card, Player>();
		this.notBusy = true;
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
	
	public void forceMove(BoardCell cell) {
		Board board = Board.getInstance();
		if(board.getCell(this.getRow(), this.getColumn()).isRoom()) {
			board.getRoom(board.getCell(this.getRow(), this.getColumn())).removePlayer(this);
		}else {
			board.getCell(this.getRow(), this.getColumn()).setOccupied(false);
		}
		this.row = cell.getRow();
		this.column = cell.getCol();
		cell.setOccupied(true);
		if(cell.isRoom()) {
			board.getRoom(cell).addPlayer(this);
		}
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
