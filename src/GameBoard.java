import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {

	// the state of the game logic
	private Pacman pacman; 
	private Ghost ghostRed;
	private Ghost ghostBlue;
	private Ghost ghostPink;
	private Ghost ghostOrange;
	private Extra strawberry;
	private Extra cherry;
	private Maze maze;
	private static final int SQUARE_WIDTH = 15;
	private static final int RADIUS = 4;
	private static final int RADIUS_POWERUP = 12;
	private static final double PERCENT_CHANGE_DIRECTION = 0.38;
	private int ghostVel = 3;
	private int lives = 3;
	private int score = 0;
	protected static Timer timer;
	private int counter = 0;
	private ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
	private ArrayList<Extra> extra = new ArrayList<Extra>();

	public boolean playing = false; // whether the game is running
	private boolean won = false;
	private boolean canEatGhosts = false;
	private boolean seeStrawberry = false;
	private boolean seeCherry = false;
	private JLabel status; 

	// Game constants
	public static final int BOARDWIDTH = 35*SQUARE_WIDTH;
	public static final int BOARDHEIGHT = 20*SQUARE_WIDTH;
	public static int PACMAN_VEL = 3;
	
	// Update interval for timer, in milliseconds
	public static final int INTERVAL = 45;

	public GameBoard(JLabel status) {
		// creates border around the court area, JComponent method
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setBackground(Color.black);
		

		timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		timer.start();
		setFocusable(true);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				checkDir(pacman);
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					pacman.img_file = "Untitled.png";
					pacman.v_x = -PACMAN_VEL;
					pacman.v_y = 0;
				}
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					pacman.img_file = "Untitled_Right.png";
					pacman.v_x = PACMAN_VEL; 
					pacman.v_y = 0;
				}
				else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					pacman.img_file = "Pacman_Down.png";
					pacman.v_y = PACMAN_VEL; 
					pacman.v_x = 0;
				}
				else if (e.getKeyCode() == KeyEvent.VK_UP) {
					pacman.img_file = "Pacman_Up.png";
					pacman.v_y = -PACMAN_VEL; 
					pacman.v_x = 0;
				}
			}

			public void keyReleased(KeyEvent e) {
			}
		});

		this.status = status;
	}

	/**
	 * (Re-)set the game to its initial state.
	 */
	public void reset() {
		counter = 0;
		seeStrawberry = false;
		seeCherry = false;
		ghosts.clear();
		initMaze();
		pacman = new Pacman(BOARDWIDTH, BOARDHEIGHT);
		ghostRed = new Ghost(BOARDWIDTH, BOARDHEIGHT,
				0, 17*SQUARE_WIDTH, 8*SQUARE_WIDTH, ghostVel, 0);
		ghostPink = new Ghost(BOARDWIDTH, BOARDHEIGHT,
				3, 6*SQUARE_WIDTH, 8*SQUARE_WIDTH, 0, -ghostVel);
		ghostBlue = new Ghost(BOARDWIDTH, BOARDHEIGHT, 
				1, 33*SQUARE_WIDTH, 8*SQUARE_WIDTH, -ghostVel, 0);
		ghostOrange = new Ghost(BOARDWIDTH, BOARDHEIGHT, 
				2, 28*SQUARE_WIDTH, 2*SQUARE_WIDTH, 0, ghostVel);
		addGhosts();
		strawberry = new Extra(BOARDWIDTH, BOARDHEIGHT,
				4*SQUARE_WIDTH, 6*SQUARE_WIDTH, 0);
		cherry = new Extra(BOARDWIDTH, BOARDHEIGHT,
				30*SQUARE_WIDTH, 4*SQUARE_WIDTH, 1);
		playing = true;
		score = 0;
		lives = 3;
		requestFocusInWindow();
	}

	private void addGhosts() {
		ghosts.add(ghostBlue);
		ghosts.add(ghostOrange);
		ghosts.add(ghostRed);
		ghosts.add(ghostPink);
		
	}

	// create a maze
	private void initMaze() {
		maze = new Maze(SQUARE_WIDTH, BOARDWIDTH, BOARDHEIGHT, RADIUS,
				        RADIUS_POWERUP);
	}

	/**
	 * This method is called every time the timer defined in the constructor
	 * triggers.
	 */
	void tick() {
		if (playing) {
			if (maze.f.isEmpty()) {
				playing = false;
				won = true;
			}

			if (hitMazeBlock(pacman) != null) {
				int save_x = pacman.pos_x;
				int save_y = pacman.pos_y;
				if (pacman.v_x != 0)
				{
					switch (pacman.pos_y % SQUARE_WIDTH)
					{
					case 1:
					case 2:
					case 3:
						pacman.pos_y = (pacman.pos_y / SQUARE_WIDTH)
						                * SQUARE_WIDTH;
						break;
					case 12:
					case 13:
					case 14:
						pacman.pos_y = ((pacman.pos_y / SQUARE_WIDTH)
								         * SQUARE_WIDTH) + SQUARE_WIDTH;
						break;
					}
				}
				else if (pacman.v_y != 0)
				{
					switch (pacman.pos_x % SQUARE_WIDTH)
					{
					case 1:
					case 2:
					case 3:
						pacman.pos_x = (pacman.pos_x / SQUARE_WIDTH)
						                * SQUARE_WIDTH;
						break;
					case 12:
					case 13:
					case 14:
						pacman.pos_x = ((pacman.pos_x / SQUARE_WIDTH)
								         * SQUARE_WIDTH) + SQUARE_WIDTH;
						break;
					}
				}
				if (hitMazeBlock(pacman) != null)
				{
					pacman.pos_x = save_x;
					pacman.pos_y = save_y;
					pacman.v_x = 0;
					pacman.v_y = 0;
				}
			}

			// randomly move ghosts at intersections
			for (Ghost ghost: ghosts)
			{
				turn(ghost.hitWall(), ghost);
			
				int save_v_x = ghost.v_x;
				int save_v_y = ghost.v_y;
				boolean changed_direction = false;
				// check to see if we are moving to the right or left
				if (ghost.v_x != 0 && ghost.pos_x % SQUARE_WIDTH == 0 &&
						ghost.pos_y % SQUARE_WIDTH == 0)
				{
					// check to see if we should move up
					ghost.v_x = 0;
					ghost.v_y = -ghostVel;
					if (hitMazeBlock(ghost) == null && 
							Math.random() < PERCENT_CHANGE_DIRECTION)
						changed_direction = true;
					
					// check to see if we should move down
					if (!changed_direction)
					{
						ghost.v_x = 0;
						ghost.v_y = ghostVel;
						if (hitMazeBlock(ghost) == null &&
								Math.random() < PERCENT_CHANGE_DIRECTION)
							changed_direction = true;
					}
				} else if (ghost.v_y != 0 && 
						ghost.pos_x % SQUARE_WIDTH == 0 && 
						ghost.pos_y % SQUARE_WIDTH == 0){
					// we are moving up or down
					
					// check to see if we should move to the right
					ghost.v_x = ghostVel;
					ghost.v_y = 0;
					if (hitMazeBlock(ghost) == null &&
							Math.random() < PERCENT_CHANGE_DIRECTION)
						changed_direction = true;
					
					// check to see if we should move to the left
					if (!changed_direction)
					{
						ghost.v_x = -ghostVel;
						ghost.v_y = 0;
						if (hitMazeBlock(ghost) == null &&
								Math.random() < PERCENT_CHANGE_DIRECTION)
							changed_direction = true;
					}
				}
				if (!changed_direction)
				{
					// We didn't change direction, so restore original direction
					ghost.v_x = save_v_x;
					ghost.v_y = save_v_y;
					
					// We are going to hit a wall so change direction
					if (hitMazeBlock(ghost) != null) {
						Direction d = ghost.hitObj(hitMazeBlock(ghost));		
						turn(d, ghost);
					}
				}
			}
						
			for (int i = 0; i < maze.f.size(); i++) {
				if (pacman.intersects(maze.f.get(i))) {
					maze.f.remove(i);
					score += 10;
				}
			}
			
			for (int i = 0; i < maze.p.size(); i++) {
				if (pacman.intersects(maze.p.get(i))) {
					maze.p.remove(i);
					ghostVel = 1;
					canEatGhosts = true;
					score += 100;
					
				}
			}
			pacman.move();
			
			if (seeCherry && !extra.contains(cherry)) {
				counter++;
				checkCounter();
			}
			
			if (canEatGhosts) {
				counter++;
				checkCounter();
				ghostRed.img_file = "ChasedGhost.png"; 
			}
			else if (ghostRed.v_x > 0)
				ghostRed.img_file = "redGhostRight2.png";
			else if (ghostRed.v_x < 0)
				ghostRed.img_file = "redGhostLeft2.png";
			else if (ghostRed.v_y > 0)
				ghostRed.img_file = "redGhostDown2.png";
			else if (ghostRed.v_y < 0)
				ghostRed.img_file = "redGhostUp2.png";
		
			ghostRed.move();
			
			if (canEatGhosts) {
				checkCounter();
				ghostPink.img_file = "ChasedGhost.png"; 
				
			}
			else if (ghostPink.v_x > 0)
				ghostPink.img_file = "ghostPinkRight.png";
			else if (ghostPink.v_x < 0)
				ghostPink.img_file = "ghostPinkLeft.png";
			else if (ghostPink.v_y > 0)
				ghostPink.img_file = "ghostPinkDown.png";
			else if (ghostPink.v_y < 0)
				ghostPink.img_file = "ghostPinkUp.png";
			ghostPink.move();
			
			if (canEatGhosts) {
				checkCounter();
				ghostBlue.img_file = "ChasedGhost.png";
				
			}
			else if (ghostBlue.v_x > 0)
				ghostBlue.img_file = "blueGhostRight.png";
			else if (ghostBlue.v_x < 0)
				ghostBlue.img_file = "blueGhostLeft.png";
			else if (ghostBlue.v_y > 0)
				ghostBlue.img_file = "blueGhostDown.png";
			else if (ghostBlue.v_y < 0)
				ghostBlue.img_file = "blueGhostUp.png";		
			ghostBlue.move();
			
			if (canEatGhosts) {
				checkCounter();
				ghostOrange.img_file = "ChasedGhost.png"; 
				
			}
			else if (ghostOrange.v_x > 0)
				ghostOrange.img_file = "orangeGhostRight.png";
			else if (ghostOrange.v_x < 0)
				ghostOrange.img_file = "orangeGhostLeft.png";
			else if (ghostOrange.v_y > 0)
				ghostOrange.img_file = "orangeGhostDown.png";
			else if (ghostOrange.v_y < 0)
				ghostOrange.img_file = "orangeGhostUp.png";
			ghostOrange.move();
			
			repaint();
			
			//update score
			status.setText("Score: " + score
									 + "                                      " 
									 + "                                      " 
									 + "Lives Remaining: " 
									 + (lives - 1));
		}
	}

	
	
	private void checkCounter() {
		if (counter == 300) {
			canEatGhosts = false;
			counter = 0;
			PACMAN_VEL = 3;
			addGhosts();
			ghostVel = 3;
			for (Ghost ghost : ghosts) {
				ghost.v_x = ghostVel;
				ghost.v_y = ghostVel;
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Extra fruit : extra) {
			fruit.draw(g);
		}
		if (score > 4000 && !seeStrawberry) {
			seeStrawberry = true;
			extra.add(strawberry);
		} else if (score > 1000 && !seeCherry) {
			seeCherry = true;
			extra.add(cherry);
		}
		if (pacman.intersects(strawberry) && seeStrawberry 
			&& extra.contains(strawberry)) {	
			extra.remove(strawberry);
			score += 1500;
			lives++;
		}
		if (pacman.intersects(cherry) && seeCherry && extra.contains(cherry)) {	
			extra.remove(cherry);
			score += 500;
	
			for (Ghost ghost : ghosts) {
				ghost.v_x = 0;
				ghost.v_y = 0;
			}
			
		}
		if ((pacman.intersects(ghostRed) 
			|| pacman.intersects(ghostPink)
			|| pacman.intersects(ghostBlue)
			|| pacman.intersects(ghostOrange))
			&& !canEatGhosts) {
			lives--; 
			if (lives == 0) {
				won = false;
				playing = false;
			}
			timer.stop();
			timer.setInitialDelay(2000);
			pacman.pos_x = 270;
			pacman.pos_y = 270;
			timer.start();
			
		} else  if (canEatGhosts) {
			if (pacman.intersects(ghostRed)) {
				score += 200;
				ghosts.remove(ghostRed);		
			} else if (pacman.intersects(ghostBlue)) {
				score += 200;
				ghosts.remove(ghostBlue);
			} else if (pacman.intersects(ghostOrange)) {
				score += 200;
				ghosts.remove(ghostOrange);	
			} else if (pacman.intersects(ghostPink)) {
				score += 200;
				ghosts.remove(ghostPink);
			}
		}
		if (!playing)
			gameOver(g, won);
		else {
			pacman.draw(g);
			
			for (Ghost gst : ghosts) {
				gst.draw(g);
			}
			
			for (Barrier x : maze.b) {
				x.draw(g);
			}
			for (Food food : maze.f) {
				food.draw(g);
			}
			for (PowerUp pu : maze.p) {
				pu.draw(g);
			}
		}		
	}

	/**
	 * turn the ghosts at barriers
	 */
	private void turn(Direction d, Ghost ghost) {
		if (d == null)
			return;
		
		int save_ghost_v_x = ghost.v_x;
		int save_ghost_v_y = ghost.v_y;
		ghost.v_x = 0;
		ghost.v_y = 0;
		int dx = pacman.pos_x - ghost.pos_x;
		int dy = pacman.pos_y - ghost.pos_y;
		
		switch (d) {
			case UP:
				// repeat the code for down without break
			case DOWN:
				if (ghost.v_x % 15 == 0)
				{
					if (dx > 0) {
						// pacman is to the right, try to go right
						ghost.v_x = ghostVel;
						if (hitMazeBlock(ghost) == null)
							return;
	
						// can't go right, try left
						ghost.v_x = -ghostVel;
						if (hitMazeBlock(ghost) == null)
							return;
					} else {
						// pacman is to the left, try to go left
						ghost.v_x = -ghostVel;
						if (hitMazeBlock(ghost) == null)
							return;
						
						// can't go left, try right
						ghost.v_x = ghostVel;
						if (hitMazeBlock(ghost) == null)
							return;		
					}
				}
				// can't go right or left, turn back around
				ghost.v_x = 0;
				ghost.v_y = -save_ghost_v_y;
				return;
			case LEFT:
				// drop to right case
			case RIGHT: 
				if (ghost.v_y % 15 == 0)
				{
					if (dy > 0) {
						// pacman is below us, try to go down
						ghost.v_y = ghostVel;
						if (hitMazeBlock(ghost) == null)
							return;
	
						// can't go down, try to go up
						ghost.v_y = -ghostVel;
						if (hitMazeBlock(ghost) == null)
							return;
					} else {
						// pacman is above us, try to go up
						ghost.v_y = -ghostVel;
						if (hitMazeBlock(ghost) == null)
							return;
	
						// can't up, try to go down
						ghost.v_y = ghostVel;
						if (hitMazeBlock(ghost) == null)
							return;
					}
				}
				// can't go up or down, turn back around
				ghost.v_x = -save_ghost_v_x;
				ghost.v_y = 0;
				return;
			default:
				break;
		}
	}
	
	public Barrier hitMazeBlock(Player obj) {
		for (Barrier z : maze.b) {
			if (obj.willIntersect(z)) {
				return z;
			}
		}
		return null;
	}
	
	private void checkDir(Player p) {
		if (p.v_x != 0) p.v_y = 0;
		else if (p.v_y != 0) p.v_x = 0;
	}

	
	private void gameOver(Graphics g, boolean gameWon) {
		String msg;
		if (gameWon)
			msg = "Congratulations! You win!";
		else 
			msg = "Game Over";
	    Font font = new Font("Helvetica", Font.BOLD, 24);
	    FontMetrics f = getFontMetrics(font);
	    g.setColor(Color.white);
	    g.setFont(font);
	    g.drawString(msg, (BOARDWIDTH - f.stringWidth(msg))/2, BOARDHEIGHT/2);
	    g.drawString(("Score: " + score), 
	    		(BOARDWIDTH - f.stringWidth(msg))/2, BOARDHEIGHT/2 + 50);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(BOARDWIDTH, BOARDHEIGHT);
	}
}