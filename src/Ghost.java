import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Ghost extends Player {


	public String img_file = "";

	public static final int SIZE = 14;
	private BufferedImage img;
	
	public Ghost(int cWidth, int cHeight, int color, int x, int y, 
			int v_x, int v_y) {
		super(v_x, v_y, x, y, SIZE, SIZE,
				cWidth, cHeight);
		switch (color) {
		case 0:
			img_file = "redGhostRight2.png";
			break;
		case 1:
			img_file = "blueGhostRight.png";
			break;
		case 2: 
			img_file = "orangeGhostRight.png";
			break;
		case 3: 
			img_file = "ghostPinkRight.png";
			break;
		default:
			break;
		}
		
		
	}
	
	@Override
	public void draw(Graphics g) {
		try {
			img = ImageIO.read(new File(img_file));	
		} catch (IOException e) {
		}
		g.drawImage(img, pos_x, pos_y, width, height, null);
	}
}
