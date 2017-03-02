package projektooneu;

public class Mass {
	private int x0;
	private int y0;
	private int x1;
	private int y1;

	public Mass(int x, int y, int width, int height) {
		this.x0 = x;
		this.y0 = y;
		this.x1 = x + width;
		this.y1 = y + height;
	}

	public Mass(Rectangle rec) {
		this.x0 = rec.getX();
		this.y0 = rec.getY();
		this.x1 = rec.getX() + rec.getWidth();
		this.y1 = rec.getY() + rec.getHeight();
	}

	public boolean collide(Mass m) {
		return (((this.x0 < m.x0 && this.x1 > m.x0) || (this.x0 < m.x1 && this.x1 > m.x1))
				&& ((this.y0 < m.y0 && this.y1 > m.y0) || (this.y0 < m.y1 && this.y1 > m.y1)))
				|| (((m.x0 < this.x0 && m.x0 > this.x1) || (m.x1 < this.x0 && m.x1 > this.x1))
						&& ((m.y0 < this.y0 && m.y0 > this.y1) || (m.y1 < this.y0 && m.y1 > this.y1)));

	}

}
