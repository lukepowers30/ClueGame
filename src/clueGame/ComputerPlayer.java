package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {

	public ComputerPlayer(String name, Color color, int row, int column) {
		super(name, color, row, column);
	}
	
	public Solution makeSuggestion(Room room) {
		ArrayList<Card> deck = new ArrayList<Card>(Board.getInstance().getDeck());
		Random rand = new Random();
		
		Card weapon = new Card("blank", CardType.ROOM);
		Card person = new Card("blank", CardType.ROOM);
		boolean w = false, p = false;
		
		while (!(w && p)) {
			int index = rand.nextInt(deck.size());
			if (((Card) deck.get(index)).getCardType() == CardType.WEAPON && w == false && !this.seenOrHandContainsName(((Card) deck.get(index)).getCardName())) {
				weapon = (Card) deck.get(index);
				w = true;
				deck.remove(index);
			} else if (((Card) deck.get(index)).getCardType() == CardType.CHARACTER && p == false && !this.seenOrHandContainsName(((Card) deck.get(index)).getCardName())) {
				person = (Card) deck.get(index);
				p = true;
				deck.remove(index);
			} else {
				deck.remove(index);
			}
		}
		
		Solution suggestion = new Solution(new Card(room.getName(), CardType.ROOM), person, weapon);
		return suggestion;
	}
	
	/*
	 * Maybe refactor to just be selectTarget
	 */
	public void move(int pathLength) {
		Board board = Board.getInstance();
		board.calcTargets(board.getCell(this.getRow(), this.getColumn()), pathLength);
		Set<BoardCell> targets = board.getTargets();
		if(board.getCell(this.getRow(), this.getColumn()).isRoom()) {
			board.getRoom(board.getCell(this.getRow(), this.getColumn())).removePlayer(this);
		}else {
			board.getCell(this.getRow(), this.getColumn()).setOccupied(false);
		}
		for(BoardCell c: targets) {
			if(c.isRoom() && !this.seenOrHandContainsName(board.getRoom(c).getName())) {
				this.setRow(c.getRow());
				this.setColumn(c.getCol());
				Board.getInstance().getTargets().clear();
				board.getRoom(c).addPlayer(this);
				return;
			}
		}
		Object[] arr = targets.toArray();
		Random rand = new Random();
		BoardCell destination = (BoardCell) arr[rand.nextInt(arr.length)];
		destination.setOccupied(true);
		this.setRow(destination.getRow());
		this.setColumn(destination.getCol());
		Board.getInstance().getTargets().clear();
	}
	
	public void makeAccusation() {
		
	}

}
