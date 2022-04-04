package clueGame;

public class Solution {
	private Card room;
	private Card person;
	private Card weapon;
	
	public Solution(Card room, Card person, Card weapon) {
		super();
		this.room = room;
		this.person = person;
		this.weapon = weapon;
	}
	
	public boolean isEquals(Solution sol) {
		if(this.room == sol.room && this.person == sol.person && this.weapon == sol.weapon) {
			return true;
		}else {
			return false;
		}
	}
	
	public Card getRoom() {
		return room;
	}
	public void setRoom(Card room) {
		this.room = room;
	}
	public Card getPerson() {
		return person;
	}
	public void setPerson(Card person) {
		this.person = person;
	}
	public Card getWeapon() {
		return weapon;
	}
	public void setWeapon(Card weapon) {
		this.weapon = weapon;
	}
	
	
	
}
