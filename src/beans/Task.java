package beans;

import java.io.Serializable;
import java.util.ArrayList;

public class Task implements Serializable{

		private int k = 10;				//粒度
		
		private int number = -2; 		//编号
		private double cost; 			//成本
		
		private ArrayList<Skill> skills = new ArrayList<Skill>();							//技能列表
		private ArrayList<Employee> employees = new ArrayList<Employee>();					//职工列表
		private boolean FINISHED = false;													//任务是否已经被完成，默认未完成
		
		private ArrayList<ArrayList<Double>> m_tau = new ArrayList<ArrayList<Double>>();	//信息素列表
		private ArrayList<Double> K0 = new ArrayList<Double>();
		private ArrayList<Double> K1 = new ArrayList<Double>();
		private ArrayList<Double> K2 = new ArrayList<Double>();
		private ArrayList<Double> K3 = new ArrayList<Double>();
		private ArrayList<Double> K4 = new ArrayList<Double>();
		private ArrayList<Double> K5 = new ArrayList<Double>();
		private ArrayList<Double> K6 = new ArrayList<Double>();
		private ArrayList<Double> K7 = new ArrayList<Double>();
		private ArrayList<Double> K8 = new ArrayList<Double>();
		private ArrayList<Double> K9 = new ArrayList<Double>();
		private ArrayList<Double> K10 = new ArrayList<Double>();
		
		public Task(){	
			m_tau.add(K0);
			m_tau.add(K1);
			m_tau.add(K2);
			m_tau.add(K3);
			m_tau.add(K4);
			m_tau.add(K5);
			m_tau.add(K6);
			m_tau.add(K7);
			m_tau.add(K8);
			m_tau.add(K9);
			m_tau.add(K10);
		}
		
		public void setNumber(int number){
			this.number=number;
		}
		public int getNumber(){
			return number;
		}
		
		public void setCost(double cost){
			this.cost=cost;
		}
		public double getCost(){
			return cost;
		}
		
		public void setSkills(ArrayList<Skill> skills){
			this.skills=skills;
		}
		public ArrayList<Skill> getSkills(){
			return skills;
		}
		
		public void setFinished(boolean finished){
			FINISHED=finished;
		}
		public boolean getFinished(){
			return FINISHED;
		}
		
		public void setEmployees(ArrayList<Employee> employees){
			this.employees=employees;
		}
		public ArrayList<Employee> getEmployees(){
			return employees;
		}

		//初始化每一个任务对应的信息素表
		public void initTau(double m_dTau0){
			
			for(int i=0;i<k+1;i++){
				for(int j=0;j<employees.size();j++){
					m_tau.get(i).add(m_dTau0);
				}
			}
			
		}
		
		public synchronized double getTau(int r,int s){
			return m_tau.get(r).get(s);
		}
		
		public synchronized double getHeristic(int r){
			return 1/employees.get(r).getSalary();
		}
		
		public synchronized void updateTau(int r, int s, double value){
			//正在更新信息素
			m_tau.get(r).remove(s);
			m_tau.get(r).add(s, value);
			//信息素更新完毕
	    }
		
		public void setK(int k){
			this.k=k;
		}
		
		public int getK(){
			return k;
		}
		
}
