/**
 * 定义了蚂蚁两个属性，蚂蚁编号、蚂蚁选择的路径。
 * @author 65635
 */
public class Ant {

	private int number;					//蚂蚁编号
	private float[][] x_ij;				//蚂蚁选择的路径
	private float cost;
	private float time;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public float[][] getX_ij() {
		return x_ij;
	}
	public void setX_ij(float[][] x_ij) {
		this.x_ij = x_ij;
	}
	public float getCost() {
		return cost;
	}
	public void setCost(float cost) {
		this.cost = cost;
	}
	public float getTime() {
		return time;
	}
	public void setTime(float time) {
		this.time = time;
	}
	public Ant(){
		
	}
}
