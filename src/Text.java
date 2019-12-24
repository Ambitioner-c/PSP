import java.util.ArrayList;

import beans.Arc;
import beans.Employee;
import beans.Task;
import init.ReadFile;

/**
 * 主文件
 * @author 65635
 */
public class Text {
	
	private static ArrayList<Employee> employees;
	private static ArrayList<Task> tasks;
	private static ArrayList<Arc> arcs;
	private static Ant ant;
	
	/**
	 * 全局最优解集
	 */
	private static ArrayList<Ant> optAnts;
	
	/**
	 * 全局最优解集更新次数
	 */
	private static int modCount = 0;
	
	public static void main(String[] args) {
		//读文件
		ReadFile readFile = new ReadFile("E:\\Study\\project\\","1.txt");
		readFile.init();
		readFile.sort();
		readFile.setEmployee_Task();
		
		tasks = readFile.getTasks();
		employees = readFile.getEmployees();
		arcs = readFile.getArcs();
		
		//生成初始贡献度矩阵
		InitAnt initAnt = new InitAnt();
		ant = initAnt.getAnt(employees,tasks);
		
		//初始化全局最优解集
		optAnts = new ArrayList<Ant>();
		float[][] x_ij = new float[employees.size()][tasks.size()];
		for (int i = 0; i < employees.size(); i++) {
			for (int j = 0; j < tasks.size(); j++) {
				x_ij[i][j] = ant.getX_ij()[i][j]; 
			}
		}
		for (int m = 0; m < 31; m++) {
			float opt_f = 1000000.f;
			Ant ant = new Ant();
			ant.setX_ij(x_ij);
			ant.setCost(opt_f);
			ant.setTime(opt_f);
			optAnts.add(ant);
		}
		
		//****************************************核心代码↓****************************************
		//迭代优化
		for (int iter = 0; iter < 3; iter++) {
			Iteration iteration = new Iteration();
			optAnts = iteration.getInteration(employees, tasks, arcs, optAnts);
			modCount += iteration.getModCount();
			
			System.out.print("进度：");
			for (int i = 0; i < iter+1; i++) {
				System.out.print("#####");
			}
			System.out.println((iter+1)*10 + "%");
		}
		show();
		//****************************************核心代码↑****************************************
	}
	
	public static void show(){
		Evaluate evaluate = new Evaluate();
		float[] opt = evaluate.getEvaluate(employees, tasks, arcs, optAnts.get(30));
		System.out.println("全局最优解：");
		System.out.println("cost:" + opt[0] + " " + "time:" + opt[1]);
		for (int i = 0; i < employees.size() ; i++) {
			for (int j = 0; j < tasks.size(); j++) {
				System.out.print(optAnts.get(30).getX_ij()[i][j]);
				System.out.print("|");	
			}
			System.out.print("\n");
		}
		System.out.println("************************************");
		System.out.println("全局最优解集（31个）：");
		System.out.println("全局最优解集更新次数为：" + modCount);
		for (int m = 0; m < optAnts.size(); m++) {
			System.out.println(optAnts.get(m).getCost() + " " + optAnts.get(m).getTime());
			/*for (int i = 0; i < employees.size(); i++) {
				for (int j = 0; j < tasks.size(); j++) {
					System.out.print(optAnts.get(m).getX_ij()[i][j]);
					System.out.print("|");
				}
				System.out.print("\n");
			}*/
		}	
	}
}
