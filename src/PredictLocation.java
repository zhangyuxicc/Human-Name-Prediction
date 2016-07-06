/**
 * this class represents a predict location
 * @author Grace
 *
 */
public class PredictLocation {
	
	private int x0;
	private int x1;
	private int y0;
	private int y1;
	private double rate;
	private int count;
	
	public PredictLocation(int i, int j, int k, int l){
		
		x0 = i;
		y0 = j;
		x1 = k;
		y1 = l;
		count = 0;
		rate = 0;
	}
	
	public int getX0() {
		return x0;
	}

	public void setX0(int x0) {
		this.x0 = x0;
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getY0() {
		return y0;
	}

	public void setY0(int y0) {
		this.y0 = y0;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
	
	public void increment(){
		count++;
	}

	
}
