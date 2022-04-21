package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SuggestionPromptPanel extends JPanel {
	private JComboBox<String> room, weapon, person;
	private JButton submit, cancel;
	
	private boolean suggestion;
	private String roomName;
	private JFrame suggestionFrame;
	
	
	public SuggestionPromptPanel(boolean suggestion, String roomName) {
		suggestionFrame = new JFrame();
		suggestionFrame.setVisible(true);
		suggestionFrame.setSize(400, 250);
		suggestionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		suggestionFrame.setTitle("Make an Accusation");
		
		
		
		this.setLayout(new GridLayout(4, 2));
		this.suggestion = suggestion;
		this.roomName = roomName;
		if (suggestion) {
			JLabel roomText = new JLabel("Current Room");
			this.add(roomText);
			JTextField currentRoom = new JTextField();
			currentRoom.setEditable(false);
			currentRoom.setText(roomName);
			this.add(currentRoom);
		} else {
			JLabel roomText = new JLabel("Room");
			this.add(roomText);
			room = new JComboBox<String>();
			for (Card c: Board.getInstance().getDeck()) {
				if (c.getCardType() == CardType.ROOM) {
					room.addItem(c.getCardName());
				}
			}
			this.add(room);
		}
		
		JLabel personText = new JLabel("Person");
		this.add(personText);
		person = new JComboBox<String>();
		for (Card c: Board.getInstance().getDeck()) {
			if (c.getCardType() == CardType.CHARACTER) {
				person.addItem(c.getCardName());
			}
		}
		this.add(person);
		
		JLabel weaponText = new JLabel("Weapon");
		this.add(weaponText);
		weapon = new JComboBox<String>();
		for (Card c: Board.getInstance().getDeck()) {
			if (c.getCardType() == CardType.WEAPON) {
				weapon.addItem(c.getCardName());
			}
		}
		this.add(weapon);
		
		submit = new JButton();
		submit.setText("Submit");
		this.add(submit);
		
		cancel = new JButton();
		cancel.setText("Cancel");
		this.add(cancel);
		
		suggestionFrame.add(this);
		ButtonListener buttonListener = new ButtonListener();
		submit.addActionListener(buttonListener);
		cancel.addActionListener(buttonListener);
	}
	
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Board board = Board.getInstance();
			if (e.getSource() == submit) {
				Card weaponCard = board.getCardFromName((String) weapon.getSelectedItem());
				Card personCard = board.getCardFromName((String) person.getSelectedItem());
				
				if (suggestion) {
					Solution solution = new Solution(board.getCardFromName(roomName), personCard, weaponCard);
					board.handleSuggestion(solution, board.getPlayers().get(0));
				} else {
					Card roomCard = board.getCardFromName((String) room.getSelectedItem());
					Solution solution = new Solution(roomCard, personCard, weaponCard);
					boolean correct = board.checkAccusation(solution);
					ClueGame.getInstance().endGame(correct, true);
				}
				
			}
			suggestionFrame.dispose();
			
		}
		
	}
}
