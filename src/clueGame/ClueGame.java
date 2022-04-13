package clueGame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame {
	
	private CardPanel cardPanel;
	private GameControlPanel gcPanel;
	
	

	public CardPanel getCardPanel() {
		return cardPanel;
	}

	public GameControlPanel getGcPanel() {
		return gcPanel;
	}

	public ClueGame(Board boardPanel) throws HeadlessException {
		super();
		this.setSize(1000, 800);  // size the frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		this.setVisible(true); // make it visible
		this.setTitle("Clue Game");
		this.setLayout(new BorderLayout());
		//boardPanel.getPlayers().get(0).updateSeen(new Card("Really long name card", CardType.CHARACTER), boardPanel.getPlayers().get(1));
		cardPanel = new CardPanel();
		gcPanel = new GameControlPanel();
		this.add(boardPanel, BorderLayout.CENTER);
		this.add(cardPanel, BorderLayout.EAST);
		this.add(gcPanel, BorderLayout.SOUTH);
		boardPanel.repaint();
		this.revalidate();
	}

	public static void main(String[] args) {
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		((ComputerPlayer)board.getPlayers().get(1)).setRow(17);
		((ComputerPlayer)board.getPlayers().get(1)).setColumn(1);
		board.getCell(17, 1).setOccupied(true);
		ClueGame clueGame = new ClueGame(board);
		JOptionPane startMessage = new JOptionPane();
		startMessage.showMessageDialog(clueGame, "You are George! \n Can you find the Solution before the Computer players?");
		clueGame.getGcPanel().updatePanel();
		
	}

}
