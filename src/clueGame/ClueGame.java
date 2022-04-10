package clueGame;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;

public class ClueGame extends JFrame {

	public ClueGame(Board boardPanel) throws HeadlessException {
		super();
		this.setSize(1000, 800);  // size the frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		this.setVisible(true); // make it visible
		this.setTitle("Clue Game");
		this.setLayout(new BorderLayout());
		CardPanel cardPanel = new CardPanel();
		GameControlPanel gcPanel = new GameControlPanel();
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
		ClueGame clueGame = new ClueGame(board);
		
	}

}
