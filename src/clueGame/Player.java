package clueGame;
import java.awt.Color;
import java.util.Set;

public abstract class Player {
	private String name;
	private Color color;
	private int row, column;
	private Set<Card> hand;
	
	public String getName () {
		return name;
	}
	
	public void updateHand (Card card) {
		
	}
}
