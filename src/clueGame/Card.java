package clueGame;

public class Card {
	private String cardName;
	private CardType cardType;
	
	public boolean isEquals (Card card) {
		if(this.cardName == card.cardName && this.cardType == card.cardType) {
			return true;
		}else {
			return false;
		}
	}
	
	public CardType getCardType() {
		return cardType;
	}

	public Card(String cardName, CardType cardType) {
		super();
		this.cardName = cardName;
		this.cardType = cardType;
	}
}
