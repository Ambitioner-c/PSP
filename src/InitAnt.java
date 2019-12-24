import java.util.ArrayList;
import java.util.Random;

import beans.Employee;
import beans.Task;

/**
 * 该类生成基于技能约束的初始贡献度矩阵。<br>
 * 技能约束矩阵：满足技能约束的位置填充1，否则填充0。<br>
 * 该类返回结果是依据技能约束矩阵，随机生成初始贡献度矩阵。
 * @author 65635
 */
public class InitAnt {
	/**
	 * 粒度
	 */
	int k = 10;	
	
	//项目信息
	private ArrayList<Employee> employees;
	private ArrayList<Task> tasks;
	
	/**
	 * 各任务中符合技能约束的员工集合
	 */
	ArrayList<Employee> t_e;
	
	/**
	 * 技能约束矩阵（满足技能约束的位置填充1，否则填充0）
	 */
	int[][] s_con;
	
	/**
	 * 初始贡献度矩阵
	 */
	Ant ant;
	
	/**
	 * 依据技能约束，生成初始贡献度矩阵。
	 * @param employees_c 员工集
	 * @param tasks_c 技能集
	 * @return 初始贡献度矩阵
	 */
	public Ant getAnt(ArrayList<Employee> employees_c, ArrayList<Task> tasks_c) {
		this.employees = employees_c;
		this.tasks = tasks_c;
		
		//初始化技能约束矩阵
		s_con = new int[employees.size()][tasks.size()];
		for (int i = 0; i < employees.size(); i++) {
			for (int j = 0; j < tasks.size(); j++) {
				s_con[i][j] = 0;
			}
		}
		
		//填充技能约束矩阵
		//依据各任务中符合技能约束的员工集合t_e
		for (int j = 0; j < tasks.size(); j++) {
			t_e = tasks.get(j).getEmployees();
			for (int i = 0; i < employees.size(); i++) {
				for (int k = 0; k < t_e.size(); k++) {
					if (i==t_e.get(k).getNumber()) {
						s_con[i][j] = 1;
						break;
					}
				}
			}
		}
		
		//生成贡献度矩阵
		ant = new Ant();
		Random random = new Random();
		
		//生成蚂蚁选择的路径
		float[][] x_ij = new float[employees.size()][tasks.size()];
		for (int i = 0; i < employees.size() ; i++) {
			for (int j = 0; j < tasks.size(); j++) {
				x_ij[i][j] = (float)random.nextInt(k+1)/k;
				if (s_con[i][j]==0) {
					x_ij[i][j] = 0.f;
				}
			}
		}
		ant.setX_ij(x_ij);
		
		return ant;
	}
	
	
	public void show(){
		//输出技能约束矩阵
		for (int i = 0; i < employees.size(); i++) {
			for (int j = 0; j < tasks.size(); j++) {
				System.out.print(s_con[i][j]);
				System.out.print("|");
			}
			System.out.println("");
		}

		//输出贡献度矩阵
		System.out.println("**************************************************");
		for (int i = 0; i < employees.size(); i++) {
			for (int j = 0; j < tasks.size(); j++) {
				System.out.print(ant.getX_ij()[i][j]);
				System.out.print("|");
			}
			System.out.println("");
		}
	}
}
