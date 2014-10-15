import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Extra extends Player {
	
	public String img_file = "";
	public static final int SIZE = 14;
	private static BufferedImage img;
	
	public Extra(int cWidth, int cHeight, int x, int y, int fruitCount) {
		super(0, 0, x, y, SIZE, SIZE, cWidth, cHeight);
		switch (fruitCount) {
		case 0:
			img_file = "strawberry.png";
			break;
		case 1:
			img_file = "cherry.png";
		default:
			break;
		}
	}
	
	@Override
	public void draw(Graphics g) {
		try {
			img = ImageIO.read(new File(img_file));	
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		g.drawImage(img, pos_x, pos_y, width, height, null);
	}
}
