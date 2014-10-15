import java.awt.Color;
import java.awt.Graphics;


public class Barrier extends Player {
	
	private int blockWidth;
	protected int pos_x;
	protected int pos_y;
	
	public Barrier(int size, int pos_x, int pos_y, int bw, int bh) {
		super(0, 0, pos_x, pos_y, size - 1, size - 1, bw, bh);
		blockWidth = size;
		this.pos_x = pos_x;
		this.pos_y = pos_y;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect(pos_x, pos_y, blockWidth, blockWidth);
	}
}
