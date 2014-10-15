import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Pacman extends Player {

		public String img_file = "Pacman_Still.png";
		public boolean openMouth = false;
		public static final int SIZE = 13;
		public static final int INIT_POS_X = 270;
		public static final int INIT_POS_Y = 270;
		public static final int INIT_VEL_X = 0;
		public static final int INIT_VEL_Y = 0;
		private static BufferedImage img;


		public Pacman(int courtWidth, int courtHeight) {
			super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE,
					courtWidth, courtHeight);
		}

		@Override
		public void draw(Graphics g) {
			try {
				if (openMouth)
					img = ImageIO.read(new File(img_file));	
				else 
					img = ImageIO.read(new File("Pacman_Still.png"));
			} catch (IOException e) {
				System.out.println("Internal Error:" + e.getMessage());
			}
			g.drawImage(img, pos_x + 1, pos_y + 1, width, height, null);
			openMouth = !openMouth;
		}
}
