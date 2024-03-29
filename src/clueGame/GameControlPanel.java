package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel {
	private JTextField turnName;
	private JTextField rollValue;
	private JTextField guessText;
	private JTextField guessResultText;
	
	private JButton accusationButton;
	private JButton nextButton;
	
	private static int diceRoll;
	

	public static int getDiceRoll() {
		return diceRoll;
	}



	public static void setDiceRoll(int roll) {
		diceRoll = roll;
	}



	/**
	 * Constructor for the panel, it does 90% of the work
	 */
	public GameControlPanel()  {
		super();
		setLayout(new GridLayout(2, 0));
		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new GridLayout(1, 4));
		JPanel turnPanel = new JPanel();
		turnPanel.setLayout(new BorderLayout());
		JPanel rollPanel = new JPanel();
		rollPanel.setLayout(new BorderLayout());
		accusationButton = new JButton("Make Accusation");
		nextButton = new JButton("NEXT");
		
		JLabel turnQuestion = new JLabel("Whose turn?");
		turnName = new JTextField();
		turnName.setEditable(false);
		JLabel rollLabel = new JLabel("Roll:");
		rollValue = new JTextField();
		rollValue.setEditable(false);
		
		turnPanel.add(turnQuestion, BorderLayout.NORTH);
		turnPanel.add(turnName, BorderLayout.CENTER);
		rollPanel.add(rollLabel, BorderLayout.WEST);
		rollPanel.add(rollValue, BorderLayout.EAST);
		
		
		
		
		upperPanel.add(turnPanel);
		upperPanel.add(rollPanel);
		upperPanel.add(accusationButton);
		upperPanel.add(nextButton);
		
		add(upperPanel);
		
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new GridLayout(0, 2));
		JPanel guessPanel = new JPanel();
		guessPanel.setBorder(new TitledBorder (new EtchedBorder(), "Guess:"));
		guessText = new JTextField();
		guessText.setEditable(false);
		guessPanel.setLayout(new BorderLayout());
		guessPanel.add(guessText, BorderLayout.CENTER);
		
		JPanel resultPanel = new JPanel();
		resultPanel.setBorder(new TitledBorder (new EtchedBorder(), "Guess Result:"));
		guessResultText = new JTextField();
		guessResultText.setEditable(false);
		resultPanel.setLayout(new BorderLayout());
		
		resultPanel.add(guessResultText, BorderLayout.CENTER);
		
		lowerPanel.add(guessPanel);
		lowerPanel.add(resultPanel);
		add(lowerPanel);
		ButtonListener buttonListener = new ButtonListener();
		nextButton.addActionListener(buttonListener);
		accusationButton.addActionListener(buttonListener);
		
	}
	
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Board board = Board.getInstance();
			Player currentPlayer = board.getPlayers().get(board.getCurrentPlayerIndex());
			if (e.getSource() == accusationButton && currentPlayer.isNotBusy()) {
				if (currentPlayer instanceof HumanPlayer) {
					if(!((HumanPlayer) currentPlayer).isHasMoved()) {
						SuggestionPromptPanel suggestionPanel = new SuggestionPromptPanel(false, null);
						
						JOptionPane c = new JOptionPane();
					}else {
						JOptionPane notTurn = new JOptionPane();
						notTurn.showMessageDialog(ClueGame.getInstance(), "You can only make an accusation at the start of your turn.");
					}
				} else {
					JOptionPane notTurn = new JOptionPane();
					notTurn.showMessageDialog(ClueGame.getInstance(), "It is not your turn.");
				}
				
			} else if (e.getSource() == nextButton && currentPlayer.isNotBusy()) {
				if (currentPlayer instanceof HumanPlayer) {
					if (((HumanPlayer) currentPlayer).isHasMoved()) {
						board.goToNextPlayer();
					} else {
						JOptionPane error = new JOptionPane();
						error.showMessageDialog(board, "Cannot go to next player. Finish your turn.");
					}
				} else {
					board.goToNextPlayer();
				}
				
			}
			updatePanel();
		}
		
	}
	
	
	
	/**
	 * Main to test the panel
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		GameControlPanel panel = new GameControlPanel();  // create the panel
		JFrame frame = new JFrame();  // create the frame 
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(750, 180);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible
		
		// test filling in the data
		panel.setTurn(new ComputerPlayer( "Col. Mustard", Color.green, 0, 0), 5);
		panel.setGuess( "I have no guess!", null);
		panel.setGuessResult( "So you have nothing?", null);
	}



	public void setGuessResult(String string, Player disprover) {
		guessResultText.setText(string);
		if (disprover == null) {
			guessResultText.setOpaque(false);
		} else {
			guessResultText.setOpaque(true);
			guessResultText.setBackground(disprover.getColor());
		}
	}



	public void setGuess(String string, Player caller) {
		guessText.setText(string);
		if (caller != null) {
			guessText.setOpaque(true);
			guessText.setBackground(caller.getColor());
		}else {
			guessText.setOpaque(false);
		}
	}


	// Function only used for testing
	public void setTurn(Player computerPlayer, int i) {
		turnName.setText(computerPlayer.getName());
		turnName.setBackground(computerPlayer.getColor());
		rollValue.setText(Integer.toString(i));
		
	}
	
	public void updatePanel() {
		Board board = Board.getInstance();
		turnName.setText(board.getPlayers().get(board.getCurrentPlayerIndex()).getName());
		turnName.setBackground(board.getPlayers().get(board.getCurrentPlayerIndex()).getColor());
		rollValue.setText(Integer.toString(diceRoll));
		revalidate();
	}
	
}