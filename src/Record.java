/**
 * this class represents a record of a human name and where it is located
 * @author Grace
 *
 */
public class Record {

	private String name;
	private double x0;
	private double x1;
	private double y0;
	private double y1;
	
	public Record(String newName, double i, double j, double k, double l){
		name = newName;
		x0 = i;
		y0 = j;
		x1 = k;
		y1 = l;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getX0() {
		return x0;
	}

	public void setX0(int x0) {
		this.x0 = x0;
	}

	public double getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public double getY0() {
		return y0;
	}

	public void setY0(int y0) {
		this.y0 = y0;
	}

	public double getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	
	
}
