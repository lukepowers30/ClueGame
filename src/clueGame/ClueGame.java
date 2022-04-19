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
	
	private static ClueGame theInstance;
	
	public static ClueGame getInstance() {
		return theInstance;
	}
	
	

	public CardPanel getCardPanel() {
		return cardPanel;
	}

	public GameControlPanel getGcPanel() {
		return gcPanel;
	}

	public ClueGame(Board boardPanel) throws HeadlessException {
		super();
		// Setup frame
		this.setSize(1000, 800);  // size the frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		this.setVisible(true); // make it visible
		this.setTitle("Clue Game");
		
		this.setLayout(new BorderLayout());
		// Add panels to Frame
		cardPanel = new CardPanel();
		gcPanel = new GameControlPanel();
		this.add(boardPanel, BorderLayout.CENTER);
		this.add(cardPanel, BorderLayout.EAST);
		this.add(gcPanel, BorderLayout.SOUTH);
		// Finish display
		boardPanel.repaint();
		this.revalidate();
	}

	public static void main(String[] args) {
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		
		theInstance = new ClueGame(Board.getInstance());
		ClueGame clueGame = ClueGame.getInstance();
		Board.getInstance().repaint();
		clueGame.revalidate();
		JOptionPane startMessage = new JOptionPane();
		startMessage.showMessageDialog(clueGame, "You are George! \n Can you find the Solution before the Computer players?");
		clueGame.getGcPanel().updatePanel();

	}


}
