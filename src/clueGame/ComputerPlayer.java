package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map.Entry;
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
		boolean weaponIsPicked = false, personIsPicked = false;
		
		while (!(weaponIsPicked && personIsPicked)) {
			int index = rand.nextInt(deck.size());
			if (((Card) deck.get(index)).getCardType() == CardType.WEAPON && weaponIsPicked == false && !this.seenOrHandContainsName(((Card) deck.get(index)).getCardName())) {
				weapon = (Card) deck.get(index);
				weaponIsPicked = true;
				deck.remove(index);
			} else if (((Card) deck.get(index)).getCardType() == CardType.CHARACTER && personIsPicked == false && !this.seenOrHandContainsName(((Card) deck.get(index)).getCardName())) {
				person = (Card) deck.get(index);
				personIsPicked = true;
				deck.remove(index);
			} else {
				deck.remove(index);
			}
		}
		
		Solution suggestion = new Solution(Board.getInstance().getCardFromName(room.getName()), person, weapon);
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
		Board board = Board.getInstance();
		Set<Card> tempDeck = board.getDeck();
		Solution solution = new Solution(new Card("", null), new Card("", null), new Card("", null));
		if (this.seen.size() + this.getHand().size() == tempDeck.size() - 3) {
			for (Entry<Card, Player> entry: this.seen.entrySet()) {
				tempDeck.remove(entry.getKey());
			}
			for (Card card: this.getHand()) {
				tempDeck.remove(card);
			}
			for (Card card: tempDeck) {
				if (card.getCardType() == CardType.CHARACTER) {
					solution.setPerson(card);
				} else if (card.getCardType() == CardType.ROOM) {
					solution.setRoom(card);
				} else if (card.getCardType() == CardType.WEAPON) {
					solution.setWeapon(card);
				}
			}
			
			// Handle the accusation
			boolean correct = board.checkAccusation(solution);
			ClueGame.getInstance().endGame(correct, false);
		} else {
			return;
		}
	}

}
