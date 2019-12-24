import java.util.ArrayList;
import java.util.Iterator;

import beans.Arc;
import beans.Employee;
import beans.Task;

/**
 * 该类实现贡献度矩阵的评估，考虑了员工不加班的情况。
 * @author 65635
 * 
 */
public class Evaluate {

	private ArrayList<Task> tasks;				//任务集
	private ArrayList<Employee> employees;		//员工集
	private ArrayList<Arc> arcs;				//弧集
	
	/**
	 * 被评估的贡献度矩阵
	 */
	private Ant ant;
	
	/**
	 * 变化后的贡献度矩阵
	 */
	private Ant d_ant;
	
	/**
	 * 当前可执行的任务集
	 */
	private ArrayList<Integer> startNumbers;
	
	/**
	 * 调整后的贡献度矩阵。<br>
	 * 该变量用来记录规范化后的贡献度矩阵。
	 */
	private float[][] d_ij;
	
	/**
	 * 各任务当前的总贡献度
	 */
	private float[] d_j;
	
	/**
	 * 各任务的当前任务量（剩余任务量）
	 */
	private float[] eff_j;
	
	/**
	 * 当前刚完成任务
	 */
	private int finished;
	
	/**
	 * 当前已完成任务集
	 */
	private ArrayList<Task> finished_j;
	
	/**
	 * 各员工在当前可执行任务的总贡献度
	 */
	private float[] sum;
	
	/**
	 * 当前任务集完成的最短时间
	 */
	private float t;
	
	/**
	 * 总成本
	 */
	private float cost;
	
	/**
	 * 总时间
	 */
	private float time;
	
	/**
	 * 该方法内置show()方法（默认不显示），可以print各阶段任务完成次序（finished_j）、累计成本（cost）、累计时间（time）和调整后的贡献度矩阵（d_ij）。
	 * @param employees_c 员工集
	 * @param tasks_c 任务集
	 * @param arcs_c 弧集
	 * @param ant_c 需要被评估的贡献度矩阵
	 * @return 返回一个数组，result[0]表示总成本，result[1]表示总时间
	 */
	public float[] getEvaluate(ArrayList<Employee> employees_c, ArrayList<Task> tasks_c, 
			ArrayList<Arc> arcs_c, Ant ant_c){
		this.employees = employees_c;
		this.tasks = tasks_c;
		this.arcs = arcs_c;
		this.ant = ant_c;
		
		//将各任务的成本定义为任务量（以“天”为单位）
		eff_j = new float[tasks.size()];
		for (int j = 0; j < tasks.size(); j++) {
			eff_j[j] = (float) tasks.get(j).getCost();
		}
		
		//当前已完成任务集
		finished_j = new ArrayList<Task>();
		//总成本初始化
		cost = 0.f;
		//总时间初始化
		time = 0.f;
		
		//调整后的贡献度矩阵初始化
		//将被评估的贡献度矩阵设置为初始的值
		d_ij = new float[employees.size()][tasks.size()];
		for (int i = 0; i < employees.size(); i++) {
			for (int j = 0; j < tasks.size(); j++) {
				d_ij[i][j] = ant.getX_ij()[i][j];
			}
		}
		
		d_ant = new Ant();
		d_ant.setX_ij(d_ij);
		
		//****************************************核心代码↓****************************************
		for (int n = 0; n < tasks.size(); n++) {
			
			//获得当前可执行的任务集
			setStartNumbers(tasks, arcs, finished_j);	
			
			//求当前可执行任务中各员工总的贡献度，总贡献度>1.0，则进行规范化
			setSum(employees.size(), startNumbers, d_ant);
			
			//规范化
			setD_ij(employees.size(), tasks.size(), startNumbers, d_ant, sum);
			
			//任务的总贡献度，用于比较当前可执行任务完成的先后顺序
			setD_j(startNumbers, employees.size(), d_ij);

			//最短工作时间，根据所有员工在在可执行任务的总贡献度，计算当前任务集中最快完成的任务
			setT(eff_j, d_j, startNumbers);		
			
			//任务量，结果为最短工作时间结束后，各任务的剩余任务量
			setEff_j(startNumbers, d_j, t);
			
			//更新已完成的任务集
			setFinished_j(tasks, finished);
			//更新总成本
			setCost(employees, startNumbers, d_ij, t);
			//更新总时间
			setTime(t);
			
			//显示
			//show();
			
			//删除以已完成任务为起点的弧
			setArcs(finished);
		}
		//****************************************核心代码↑****************************************
		
		//返回结果
		float[] result = new float[2];
		result[0] = cost;
		result[1] = time;
		
		return result;
	}
	
	
	//获取贡献度表
	public Ant getAnt(){
		return ant;
	}
	public void setAnt(Ant ant){
		this.ant = ant;
	}
	
	//获得当前可执行的任务集
	public ArrayList<Integer> getStartNumbers(){
		return startNumbers;
	}
	/**
	 * 调用了Start类的getStartNumbers()方法。获取当前可执行的任务集startNumber。
	 * @param tasks 任务集
	 * @param arcs 弧集
	 * @param finished_j 已完成任务集
	 */
	public void setStartNumbers(ArrayList<Task> tasks, ArrayList<Arc> arcs, ArrayList<Task> finished_j){
		Start start = new Start();
		startNumbers = start.getStartNumbers(tasks, arcs, finished_j);
	}
	
	//获取各员工在可执行任务集中的总贡献度
	public float[] getSum() {
		return sum;
	}
	/**
	 * 循环遍历当前可执行任务集，计算求和各员工在可执行任务集中的总贡献度（如，员工0在可执行任务0、1中总贡献度为1.2）。
	 * @param employess 员工集
	 * @param startNumbers 当前可执行任务集
	 * @param d_ant 变化后的贡献度矩阵
	 */
	public void setSum(int employess,ArrayList<Integer> startNumbers, Ant d_ant) {
		sum = new float[employess];
		for (int i = 0; i < employess; i++) {
			for (int j = 0; j < startNumbers.size(); j++) {
				sum[i] += d_ant.getX_ij()[i][startNumbers.get(j)];
			}
		}
	}
	
	//规范化
	public float[][] getD_ij() {
		return d_ij;
	}
	/**
	 * 规范化每一个员工在当前可执行任务中的总贡献度。
	 * @param employess 员工集
	 * @param tasks 任务集
	 * @param startNumbers 可执行任务集
	 * @param d_ant 变化后的贡献度矩阵
	 * @param sum 当前各员工在可执行任务的总贡献度
	 */
	public void setD_ij(int employess,int tasks,ArrayList<Integer> startNumbers,Ant d_ant,float[] sum) {
		//修改d_ij[][]表
		for (int i = 0; i < employess; i++) {
			for (int j = 0; j < startNumbers.size(); j++) {
				d_ij[i][startNumbers.get(j)] = (ant.getX_ij()[i][startNumbers.get(j)])/(float) Math.max(1.0, sum[i]);
			}
		}
	}
	
	//获取各任务的总贡献度
	public float[] getD_j() {
		return d_j;
	}
	/**
	 * 获取每个当前可执行任务的总贡献度（如，对所有员工在任务0的贡献度求和）。
	 * @param startNumbers 可执行任务集
	 * @param employees 员工集
	 * @param d_ij 调整后的贡献度矩阵
	 */
	public void setD_j(ArrayList<Integer> startNumbers,int employees,float[][] d_ij) {
		d_j = new float[startNumbers.size()];
		for (int j = 0; j < startNumbers.size(); j++) {
			float d = 0.f;
			for (int i = 0; i < employees ; i++) {
				d =d + d_ij[i][startNumbers.get(j)];
			}
			d_j[j] = d;
		}
	}
	
	//获取当前任务集完成的最短时间
	public float getT() {
		return t;
	}
	/**
	 * 根据各任务的当前任务剩余量，以及各任务当前员工的总贡献度，计算快最快完成任务的最短时间。
	 * @param eff_j 各任务的任务量
	 * @param d_j 当前可执行任务的各自的总贡献度
	 * @param startNumbers 当前可执行任务集
	 */
	public void setT(float[] eff_j,float[] d_j,ArrayList<Integer> startNumbers) {
		float min = eff_j[startNumbers.get(0)]/d_j[0];
		for (int j = 0; j < startNumbers.size(); j++) {
			min = Math.min(min, eff_j[startNumbers.get(j)]/d_j[j]);
			if(min==eff_j[startNumbers.get(j)]/d_j[j]){
				setFinished(startNumbers.get(j));
			}
		}
		t = min;
	}

	//获取剩余任务量
	public float[] getEff_j() {
		return eff_j;
	}
	/**
	 * 根据setT()得到的最短时间，计算各任务的剩余工作量。
	 * @param startNumbers 当前可执行任务集
	 * @param d_j 当前可执行任务的各自的总贡献度
	 * @param t 当前可执行任务中最快完成任务的完成最短时间
	 */
	public void setEff_j(ArrayList<Integer> startNumbers,float[] d_j,float t) {
		for (int j = 0; j < startNumbers.size(); j++) {
			if ((eff_j[startNumbers.get(j)] - t*d_j[j])<0.000001) {
				eff_j[startNumbers.get(j)] = 0;
			}else {
				eff_j[startNumbers.get(j)] = eff_j[startNumbers.get(j)] - t*d_j[j];
			}
		}
	}

	//当前刚完成的任务
	public int getFinished() {
		return finished;
	}
	/**
	 * 设置当前刚完成任务。
	 * @param j 当前刚完成任务编号
	 */
	public void setFinished(int j) {
		finished = j;
	}

	//当前已完成的任务集
	public ArrayList<Task> getFinished_j() {
		return finished_j;
	}
	/**
	 * 添加刚完成任务至已完成任务集中。
	 * @param tasks 任务集
	 * @param finished 刚完成任务编号
	 */
	public void setFinished_j(ArrayList<Task> tasks,int finished) {
		finished_j.add(tasks.get(finished));
	}

	//更新弧集
	public ArrayList<Arc> getArcs() {
		return arcs;
	}
	/**
	 * 删除以已完成任务为起点的弧。
	 * @param finished 刚完成任务编号
	 */
	public void setArcs(int finished) {
		for (Iterator<Arc> iterator = arcs.iterator(); iterator.hasNext();) {
			Arc arc = (Arc) iterator.next();
			if (arc.getFirstNumber()==finished) {
				iterator.remove();
			}
		}
	}
	
	//总成本
	public float getCost() {
		return cost;
	}
	/**
	 * 根据任务完成的时间和贡献度计算当前累计成本。
	 * @param employees 员工集
	 * @param startNumbers 可执行任务集
	 * @param d_ij 改变后的贡献度矩阵
	 * @param t 最小完成时间
	 */
	public void setCost(ArrayList<Employee> employees,ArrayList<Integer> startNumbers,float[][] d_ij,float t) {
		for (int j = 0; j < startNumbers.size(); j++) {
			for (int i = 0; i < employees.size(); i++) {
				cost = (float) (cost + t*(employees.get(i).getSalary()/30)*d_ij[i][startNumbers.get(j)]);
			}
		}
	}

	//总时间
	public float getTime() {
		return time;
	}
	/**
	 * 计算累计时间。
	 * @param t 最小完成时间
	 */
	public void setTime(float t) {
		time = time + t;
	}


	public void show(){
		//输出规范化之前的蚂蚁选择的路径
		System.out.println("************************************************************");
		System.out.println("第"+ant.getNumber()+"只蚂蚁！");
		for (int i = 0; i < employees.size() ; i++) {
			for (int j = 0; j < tasks.size(); j++) {
				System.out.print(ant.getX_ij()[i][j]);
				System.out.print("|");	
			}
			System.out.print("\r\n");
		}
		System.out.print("\r\n");
		
		//输出当前可执行任务
		System.out.println("************************************************************");
		System.out.println("当前可执行任务：");
		for(Integer integer:startNumbers){
			System.out.print(integer);
			System.out.print("|");	
		}
		System.out.print("\r\n");
		
		//输出总贡献度
		System.out.println("************************************************************");
		System.out.print("总贡献度：");
		for (int i = 0; i < sum.length; i++) {
			System.out.print(sum[i]);
			System.out.print("|");	
		}
		System.out.print("\r\n");
		
		//输出规范化后的蚂蚁选择的路径
		System.out.println("************************************************************");
		System.out.println("规范化后的贡献度：");
		for (int i = 0; i < employees.size() ; i++) {
			for (int j = 0; j < tasks.size(); j++) {
				System.out.print(d_ij[i][j]);
				System.out.print("|");	
			}
			System.out.print("\r\n");
		}
		System.out.print("\r\n");
		
		//输出任务的总贡献度
		System.out.println("************************************************************");
		for (int j = 0; j < startNumbers.size(); j++) {
			System.out.println("第"+startNumbers.get(j)+"个任务的总贡献度:"+d_j[j]);
		}
		System.out.print("\r\n");
		
		//输出任务量
		System.out.println("************************************************************");
		System.out.println("各任务的工作量：");
		for (int j = 0; j < tasks.size(); j++) {
			System.out.print(eff_j[j]);
			System.out.print("|");
		}
		System.out.println("\r\n");
		
		//输出最短工作时间
		System.out.println("************************************************************");
		System.out.println("第 "+finished+" 个任务最先完成"+"完成时间为：");
		System.out.print(t);
		System.out.println("\r\n");
		
		//输出已完成的任务集
		System.out.println("************************************************************");
		System.out.println("已完成的任务有：");
		for(Task task:finished_j){
			System.out.print(task.getNumber());
			System.out.print("|");
		}
		System.out.println("\r\n");
		
		//输出当前累计总成本
		System.out.println("************************************************************");
		System.out.println("当前累计总成本：");
		System.out.println(cost);
		
		//输出当前累计总时间
		System.out.println("当前累计总时间：");
		System.out.print(time);
		System.out.println("\r\n");
	}
}
