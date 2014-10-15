import java.awt.Color;
import java.awt.Graphics;


public class PowerUp extends Player {
	
	private int r;
	protected int pos_x;
	protected int pos_y;
	
	public PowerUp(int size, int pos_x, int pos_y, int r, int bw, int bh) {
		super(0, 0, pos_x, pos_y, size, size, bw, bh);
		this.r = r;
		this.pos_x = pos_x;
		this.pos_y = pos_y;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillOval(pos_x, pos_y, r, r);
	}
}
