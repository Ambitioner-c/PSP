import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

import beans.Arc;
import beans.Employee;
import beans.Task;

/**
 * 该类实现贡献度矩阵的迭代。
 * @author 65635
 *
 */
public class Iteration{
	
	//项目信息
	private ArrayList<Employee> employees;
	private ArrayList<Task> tasks;
	private ArrayList<Arc> arcs;
	
	/**
	 * 粒度
	 */
	private int k = 10;
	
	/**
	 * 增加系数
	 */
	private float b = 0.25f;
	
	/**
	 * 挥发系数
	 */
	private float r = 0.15f;
	
	/**
	 * 初始信息素值
	 */
	private float init_pher = 0.1f;
	/**
	 * 成本的权重
	 */
	private float w_cost = 0.01f;
	
	/**
	 * 时间的权重
	 */
	private float w_time = 20000f;
	
	/**
	 * 状态转移规则中探索的概率
	 */
	float q0 = 0.9f;
	
	/**
	 * 信息素数组，两个维度，第一维度为粒度、第二个维度为任务
	 */
	private float[][] pher_kj;
	/**
	 * 信息素列表，有employees.size()个元素
	 */
	private ArrayList<float[][]> pheromone;
	
	/**
	 * 概率数组
	 */
	private float[][] p_kj;
	/**
	 * 概率列表，结构与信息素列表相同
	 */
	private ArrayList<float[][]> p;
	
	/**
	 * 用来记录旧的贡献度矩阵
	 */
	private Ant oldAnt;
	
	/**
	 * 用来记录新的贡献度矩阵
	 */
	private Ant newAnt;
	
	/**
	 * 旧贡献度矩阵的评估结果
	 */
	float[] e_old;
	/**
	 * 新贡献度矩阵的评估结果
	 */
	float[] e_new;
	
	/**
	 * 初始化的贡献度矩阵
	 */
	private Ant initAnt;
	
	/**
	 * 全局最优适应度值
	 */
	private double optSum;
	
	/**
	 * 全局最优解
	 */
	private Ant optAnt;
	
	/**
	 * 全局最优解集
	 */
	private ArrayList<Ant> optAnts;
	
	/**
	 * 全局最优解更新次数
	 */
	private int modCount = 0;
	
	
	/**
	 * 该方法对贡献度矩阵进行迭代优化。<br>
	 * @param employees_c 员工集
	 * @param tasks_c 任务集
	 * @param arcs_c 弧集
	 * @param optAnts_c 全局最优解集
	 * @return ArrayList<Ant> 返回全局最优解集
	 */
	public ArrayList<Ant> getInteration(ArrayList<Employee> employees_c, ArrayList<Task> tasks_c, 
			ArrayList<Arc> arcs_c, ArrayList<Ant> optAnts_c) {
		this.employees = employees_c;
		this.tasks = tasks_c;
		
		//初始化弧集
		arcs = new ArrayList<Arc>();
		for (int i = 0; i < arcs_c.size(); i++) {
			arcs.add(arcs_c.get(i));
		}
		
		//将optAnts_c列表中第31个ant赋值为当前初始贡献度矩阵
		//其中，第31个ant为上一代评估结果最优的矩阵
		float[][] x_ij = new float[employees.size()][tasks.size()];
		for (int i = 0; i < employees.size(); i++) {
			for (int j = 0; j < tasks.size(); j++) {
				x_ij[i][j] = optAnts_c.get(30).getX_ij()[i][j]; 
			}
		}
		newAnt = new Ant();
		oldAnt = new Ant();
		initAnt = new Ant();
		//初始化新贡献度矩阵、旧贡献度矩阵、初始贡献度矩阵
		newAnt.setX_ij(x_ij);
		oldAnt.setX_ij(x_ij);
		initAnt.setX_ij(x_ij);
		
		//初始化新旧贡献度矩阵的评估结果
		e_old = new float[2];
		e_new = new float[2];
		e_old[0] = 10000000.f;
		e_old[1] = 10000000.f;
		e_new[0] = 10000000.f;
		e_new[1] = 10000000.f;
		
		//初始化全局最优解（赋值为初始贡献度表）
		optSum = 10000000.f;
		optAnt = new Ant();
		optAnt.setX_ij(initAnt.getX_ij());
		optAnt.setCost(e_new[0]);
		optAnt.setTime(e_new[1]);
		
		//初始化全局最优解集（赋值方法中获得的参数optAnts_c）
		optAnts = new ArrayList<Ant>();
		for (int i = 0; i < optAnts_c.size(); i++) {
			Ant ant = new Ant();
			ant = optAnts_c.get(i);
			optAnts.add(ant);
		}
		
		//****************************************核心代码↓****************************************
		//初始化信息素矩阵
		init(employees.size(), tasks.size());
		
		//表示有alpha代蚂蚁
		for (int alpha = 0; alpha < 10; alpha++) {
			//表示每代蚂蚁有beta个群体
			for (int beta = 0; beta < 100; beta++) {
				//num只蚂蚁同步求解
				for (int num = 0; num < 10; num++) {
					//根据比对新的与旧的贡献度表的评估结果，更新信息素矩阵
					update(employees.size(), tasks.size(), arcs_c);
					
					//根据信息素矩阵，更新贡献度矩阵
					next(employees.size(), tasks.size());
				}
				//挥发信息素
				volatilize(employees.size(), tasks.size());
			}
		}
		
		//输出全局最优解情况
		//show();
		
		//用全局最优解，替换全局最优解集中第31个元素
		optAnts.remove(30);
		optAnts.add(optAnt);
		//****************************************核心代码↑****************************************
		
		return optAnts;
	}
	
	
	/**
	 * 初始化信息素矩阵，根据员工数量生成与员工数量相同的信息素矩阵。<br>
	 * 换句话说，每一个员工对于一张信息素矩阵。
	 * @param employee 员工数量
	 * @param task 技能数量
	 */
	public void init(int employee,int task){
		pheromone = new ArrayList<float[][]>();
		for (int i = 0; i < employee; i++) {
			pher_kj = new float[k+1][task];
			for (int m = 0; m < k+1; m++) {
				for (int j = 0; j < task; j++) {
					pher_kj[m][j] = init_pher;
				}
			}
			pheromone.add(pher_kj);
		}
	}
	
	/**
	 * 根据挥发系数r进行信息素的挥发。
	 * @param employee 员工数量
	 * @param task 技能数量
	 */
	public void volatilize(int employee,int task){
		for (int i = 0; i < employee; i++) {
			for (int m = 0; m < k+1; m++) {
				for (int j = 0; j < task; j++) {
					float a = pheromone.get(i)[m][j];
					float b = 1-r;
					BigDecimal aBigDecimal = new BigDecimal(a);
					BigDecimal bBigDecimal = new BigDecimal(b);
					BigDecimal result = aBigDecimal.multiply(bBigDecimal).setScale(3, java.math.BigDecimal.ROUND_HALF_UP);
					
					pheromone.get(i)[m][j] = result.floatValue();
				}
			}
		}
	}
	
	/**
	 * 信息素的更新。<br>
	 * 该方法包含三大主要功能：<br>
	 * 1、比较新的贡献度矩阵与当前全局最优解矩阵，更新全局最优解；<br>
	 * 2、更新全局最优解集；<br>
	 * 3、比较新的贡献度矩阵与旧的贡献度矩阵，用较好的矩阵更新信息素矩阵。
	 * @param employee 员工数量
	 * @param task 任务数量
	 * @param arcs_c 弧集
	 */
	public void update(int employee, int task, ArrayList<Arc> arcs_c){
		Ant ant = new Ant();
		float[][] x_ij = new float[employee][task];
		for (int i = 0; i < employee; i++) {
			for (int j = 0; j < task; j++) {
				x_ij[i][j] = 0.f; 
			}
		}
		ant.setX_ij(x_ij);
		
		//计算新的贡献度矩阵的适应度函数
		Evaluate evaluate = new Evaluate();
		e_new = evaluate.getEvaluate(employees, tasks, arcs, newAnt);
		
		//重新填补被删除的弧集
		for (int i = 0; i < arcs_c.size(); i++) {
			arcs.add(arcs_c.get(i));
		}
		
		//扩大时间的数量级，同时缩小成本的数量级，经过调整，实现时间和成本的同时减少。
		//这样的方法起到了很好的作用。
		double old_sum = e_old[0]*w_cost + e_old[1]*w_time;
		double new_sum = e_new[0]*w_cost + e_new[1]*w_time;
		
		//输出每次新贡献度矩阵的适应度值
		System.out.println(e_new[0] + "   " + e_new[1]);
		
		//更新全局最优解
		optimal(e_new, new_sum, newAnt);
		//更新全局最优解集
		optimal2(e_new, newAnt);
		
		//时间和成本加权求和较好的解赋值给ant。
		//ant将用于更新信息素矩阵。
		if (old_sum-new_sum>0.000001f) {
			ant.setX_ij(newAnt.getX_ij());
		}else {
			ant.setX_ij(oldAnt.getX_ij());
		}
		
		//更新成本和时间较好的解
		for (int i = 0; i < employee; i++) {
			for (int j = 0; j < task; j++) {
				int x_ij2 = (int) ((ant.getX_ij()[i][j])*10);
				
				float a = pheromone.get(i)[x_ij2][j];
				BigDecimal aBigDecimal = new BigDecimal(a);
				BigDecimal bBigDecimal = new BigDecimal(b);
				BigDecimal result = aBigDecimal.add(bBigDecimal).setScale(3, java.math.BigDecimal.ROUND_HALF_UP);
				
				pheromone.get(i)[x_ij2][j] = result.floatValue();
			}
		}
	}
	
	/**
	 * 按照状态转移规则生成下一个贡献度矩阵。<br>
	 * 生成过程分为两种情况：①信息素浓度最高的路径为下一个路径；②轮盘赌概率选择下一个路径。
	 * @param employee 员工数量
	 * @param task 技能数量
	 */
	public void next(int employee,int task){
		//将当前蚂蚁newAnt克隆为旧蚂蚁oldAnt
		oldAnt.setX_ij(newAnt.getX_ij());
		
		//计算分母部分
		float[][] sum = new float[employee][task];
		for (int i = 0; i < employee; i++) {
			for (int j = 0; j < task; j++) {
				sum[i][j] = 0.0f;
			}
		}
		for (int i = 0; i < employee; i++) {
			for (int j = 0; j < task; j++) {
				for (int m = 0; m < k+1; m++) {
					sum[i][j] += pheromone.get(i)[m][j];
				}
			}
		}
		
		//概率矩阵
		p = new ArrayList<float[][]>();
		for (int i = 0; i < employee; i++) {
			p_kj = new float[k+1][task];
			for (int m = 0; m < k+1; m++) {
				for (int j = 0; j < task; j++) {
					p_kj[m][j] = pheromone.get(i)[m][j]/sum[i][j];
				}
			}
			p.add(p_kj);
		}
		
		//状态转移规则
		Random random = new Random();
		float q = random.nextFloat();
		float[][] x_ij = new float[employee][task];
		
		if (q-q0>=0.000001) {
			//最大信息素选择下一个贡献度表
			for (int i = 0; i < employee; i++) {
				for (int j = 0; j < task; j++) {
					float max = 0.f;
					for (int m = 0; m < k+1; m++) {
						if (pheromone.get(i)[m][j]-max>=0.000001) {
							max = pheromone.get(i)[m][j];
						}
					}
					for (int m = 0; m < k+1; m++) {
						if (Math.abs(pheromone.get(i)[m][j]-max)<0.000001) {
							x_ij[i][j] = (float)m/k;
						}
					}
				}
			}
		} else {
			//轮盘赌选择下一个贡献度表
			for (int i = 0; i < employee; i++) {
				for (int j = 0; j < task; j++) {
					float sleectP = random.nextFloat();
					float sum1 = 0.f;
					for (int m = 0; m < k+1; m++) {
						sum1 += p.get(i)[m][j];
						if (sum1 >= sleectP) {
							x_ij[i][j] = (float)m/10;
							//这里考虑了技能约束
							if (initAnt.getX_ij()[i][j]<0.000001) {
								x_ij[i][j] = 0.f;
							}
							break;
						}
					}
				}
			}
		}
		newAnt.setX_ij(x_ij);
		
		for (int i = 0; i < e_new.length; i++) {
			e_old[i] = e_new[i];
		}
	}
	
	/**
	 * 更新全局最优解。<br>
	 * 如果新贡献度矩阵的适应度优于全局最优解，则替换全局最优解。
	 * @param e_newc 新贡献度矩阵的评估结果
	 * @param new_sum 新贡献度矩阵的适应度
	 * @param newAntc 新贡献度矩阵
	 */
	public void optimal(float[] e_newc, double new_sum, Ant newAntc){
		if (new_sum-optSum<=0.000001) {
			optSum = new_sum;
			float cost = e_newc[0];
			float time = e_newc[1];
			optAnt.setX_ij(newAntc.getX_ij());
			optAnt.setCost(cost);
			optAnt.setTime(time);
		}
	}
	
	/**
	 * 更新全局最优解集。<br>
	 * 全局最优解集共有31个，其中第31个用来记录全局最优解。<br>
	 * @param e_newc
	 * @param newAntc
	 */
	public void optimal2(float[] e_newc, Ant newAntc){
		
		//0-14倾向去对时间进行优化
		for (int m = 0; m < 15; m++) {
			//循环遍历，如果新的贡献度矩阵与全局最优解集中首先出现的时间和成本近似，则跳出循环
			if ((Math.abs(optAnts.get(m).getCost()-e_newc[0])<=0.001f)&&(Math.abs(optAnts.get(m).getTime()-e_newc[1])<=0.001f)) {
				break;
			}else if ((optAnts.get(m).getTime()-e_newc[1]>=0.01f)||(optAnts.get(m).getCost()-e_newc[0]>=200.f)) {
				optAnts.get(m).setX_ij(newAntc.getX_ij());
				optAnts.get(m).setCost(e_newc[0]);
				optAnts.get(m).setTime(e_newc[1]);
				modCount++;
				break;	
			}
		}
		//15-29倾向去对成本进行优化
		for (int m = 15; m < 30; m++) {
			if ((Math.abs(optAnts.get(m).getCost()-e_newc[0])<=0.001f)&&(Math.abs(optAnts.get(m).getTime()-e_newc[1])<=0.001f)) {
				break;
			}else if ((optAnts.get(m).getCost()-e_newc[0]>=200.f)||(optAnts.get(m).getTime()-e_newc[1]>=0.01f)) {
				optAnts.get(m).setX_ij(newAntc.getX_ij());
				optAnts.get(m).setCost(e_newc[0]);
				optAnts.get(m).setTime(e_newc[1]);
				modCount++;
				break;	
			}
		}
	}
	
	/**
	 * 获得全局最优解集更新次数
	 * @return
	 */
	public int getModCount() {
		return modCount;
	}
	
	
	public void show(){
		System.out.println("全局最优解的适应度值为：" + optSum);
		Evaluate evaluate = new Evaluate();
		float[] opt = evaluate.getEvaluate(employees, tasks, arcs, optAnt);
		System.out.println("cost:" + opt[0] + " " + "time:" + opt[1]);
		for (int i = 0; i < employees.size() ; i++) {
			for (int j = 0; j < tasks.size(); j++) {
				System.out.print(optAnt.getX_ij()[i][j]);
				System.out.print("|");	
			}
			System.out.print("\n");
		}
	}
}