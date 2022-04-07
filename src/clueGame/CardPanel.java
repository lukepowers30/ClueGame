package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class CardPanel extends JPanel {
	
	JPanel peoplePanel;
	JPanel roomPanel;
	JPanel weaponPanel;

	public CardPanel() {
		super();
		setLayout(new GridLayout(3,1));
		creatPeoplePanel();
		creatRoomPanel();
		creatWeaponPanel();
	}

	private void creatWeaponPanel() {
		weaponPanel = new JPanel();
		JPanel hand = createHandPanel(peoplePanel, CardType.WEAPON);
		JPanel seen = createSeenPanel(peoplePanel, CardType.WEAPON);
		weaponPanel.setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));
		weaponPanel.setLayout(new GridLayout(2,1));
		weaponPanel.add(hand);
		weaponPanel.add(seen);
		weaponPanel.getComponent(0);
		this.add(weaponPanel);
	}

	private void creatRoomPanel() {
		roomPanel = new JPanel();
		JPanel hand = createHandPanel(peoplePanel, CardType.ROOM);
		JPanel seen = createSeenPanel(peoplePanel, CardType.ROOM);
		roomPanel.setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));
		roomPanel.setLayout(new GridLayout(2,1));
		roomPanel.add(hand);
		roomPanel.add(seen);
		roomPanel.getComponent(0);
		this.add(roomPanel);
	}

	private void creatPeoplePanel() {
		peoplePanel = new JPanel();
		JPanel hand = createHandPanel(peoplePanel, CardType.CHARACTER);
		JPanel seen = createSeenPanel(peoplePanel, CardType.CHARACTER);
		peoplePanel.setBorder(new TitledBorder (new EtchedBorder(), "People"));
		peoplePanel.setLayout(new GridLayout(2,1));
		peoplePanel.add(hand);
		peoplePanel.add(seen);
		peoplePanel.getComponent(0);
		this.add(peoplePanel);
	}

	private JPanel createSeenPanel(JPanel panel, CardType type) {
		JPanel seen = new JPanel();
		seen.setLayout(new GridLayout(0,1));
		JLabel label = new JLabel("Seen:");
		seen.add(label);
		for(Map.Entry<Card, Player> entry: Board.getInstance().getPlayers().get(0).getSeen().entrySet()) {
			if(entry.getKey().getCardType() == type) {
				JTextField card = new JTextField();
				card.setText(entry.getKey().getCardName());
				card.setBackground(entry.getValue().getColor());
				seen.add(card);
			}
		}
		return seen;
	}

	private JPanel createHandPanel(JPanel panel, CardType type) {
		JPanel hand = new JPanel();
		hand.setLayout(new GridLayout(0,1));
		JLabel label = new JLabel("In Hand:");
		hand.add(label);
		for(Card c: Board.getInstance().getPlayers().get(0).getHand()) {
			if(c.getCardType() == type) {
				JTextField card = new JTextField();
				card.setText(c.getCardName());
				card.setBackground(Board.getInstance().getPlayers().get(0).getColor());
				hand.add(card);
			}
		}
		return hand;
	}
	
	public void updatePanel() {
		this.removeAll();
		setLayout(new GridLayout(3,1));
		creatPeoplePanel();
		creatRoomPanel();
		creatWeaponPanel();
	}

	public static void main(String[] args) {
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		
		CardPanel panel = new CardPanel();
		JFrame frame = new JFrame();  // create the frame 
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(180, 750);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible
		
		board.getPlayers().get(0).updateSeen(new Card("Jane", CardType.CHARACTER), board.getPlayers().get(1));
		board.getPlayers().get(0).updateSeen(new Card("Michael", CardType.CHARACTER), board.getPlayers().get(2));
		board.getPlayers().get(0).updateSeen(new Card("Rope", CardType.WEAPON), board.getPlayers().get(3));
		board.getPlayers().get(0).updateSeen(new Card("Kitchen", CardType.ROOM), board.getPlayers().get(4));
		board.getPlayers().get(0).updateSeen(new Card("Patio", CardType.ROOM), board.getPlayers().get(5));
		
		panel.updatePanel();
	}

}
