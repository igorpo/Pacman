import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class Game implements Runnable {
	public void run() {
		final JFrame frame = new JFrame("PACMAN");
		frame.setLocation(300, 200);
		frame.setResizable(false);
		// Status panel
		final JPanel status_panel = new JPanel();
		frame.add(status_panel, BorderLayout.SOUTH);
		final JLabel status = new JLabel("Score: ");
		status_panel.add(status);

		// Main playing area
		final GameBoard board = new GameBoard(status);
		frame.add(board, BorderLayout.CENTER);

		// Reset button
		final JPanel control_panel = new JPanel();
		frame.add(control_panel, BorderLayout.NORTH);

		final JButton reset = new JButton("New Game");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				board.reset();
			}
		});
		control_panel.add(reset);

		final JButton instructions = new JButton("Instructions");
		instructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameBoard.timer.stop();
				JOptionPane.showMessageDialog(frame,
						"Welcome to Pacman!\n" 
						+ "Use the arrow keys to control Pacman and eat "
						+ "the pellets throughout the maze.\n"
						+ "However, if you collide with a ghost,"
						+ " you will lose a life!\n"
						+ "Collect the special powerups to get more points "
						+ "and eat the ghosts.\n"
						+ "\n"
						+ "Features:\n"
						+ "Ghost AI: ghosts will chase you in certain places, "
						+ "while also being able to randomly chose different "
						+ "directions at times.\n"
						+ "Beware: ghosts can teleport to the other side of the"
						+ " board at times!\n"
						+ "Power Ups: Grab the white fruits to gain points, "
						+ "make the ghosts temporarily slower and "
						+ "eat the ghosts!\n"
						+ "Grab the strawberry to gain a life\n"
						+ "Grab the cherry to make the ghosts stop temporarily");
				GameBoard.timer.start();
				board.grabFocus();
			}
		});
		control_panel.add(instructions);
		// Put the frame on the screen
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// Start game
		board.reset();
	}

	/*
	 * Main method run to start and run the game Initializes the GUI elements
	 * specified in Game and runs it IMPORTANT: Do NOT delete! You MUST include
	 * this in the final submission of your game.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}
}

