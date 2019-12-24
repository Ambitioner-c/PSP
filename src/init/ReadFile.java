package init;

import beans.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ReadFile implements Serializable{
	
	private File file;
	private FileReader fileReader;
	private BufferedReader bufferedReader;
	
	private String absoluteRoute = null;				//绝对路径
	private String fileName = null;						//文件名
	
	private ArrayList<Task> tasks = new ArrayList<Task>();				//任务集
	private ArrayList<Employee> employees = new ArrayList<Employee>();	//员工集
	private ArrayList<Arc> arcs = new ArrayList<Arc>();					//弧集
	private int [ ] employeeNumber_Task;								//一个任务的员工数
	
	//读取文件时将每条数据按特征剥离开
	private String regex1 = "=";
	private String regex2 = "[.]";
	private String regex3 = "->";
	
	/**用于存放每条字符串
	 * @param String s
	 */
	private String s = null;				//用于存放每条字符串
	
	/**任务编号
	 * @param Int number1
	 */
	private int number1 = -2;				//任务编号
	
	/**员工编号
	 * @param Int number2
	 */
	private int number2 = -2;				//员工编号
	
	private Task task = null;				//任务
	private Employee employee = null;		//员工
	
	//设置绝对路径、文件名，用于读取文件
	public ReadFile(String absoluteRoute,String fileName){
		
		this.absoluteRoute = absoluteRoute;
		this.fileName = fileName;
	}
	
	//通过init（）方法将文件中的数据全部存到相应的数组
	public void init(){
		
		// 创建文本输入流，读取文件   
		try{
		
			file = new File(absoluteRoute+fileName);
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			
			//按行读取文件
			while((s=bufferedReader.readLine())!= null){
				
				if(s.startsWith("#")){continue;}//忽略起始的两行
				
				String [] w1 = s.split(regex1);
				String [] w2 = w1[0].split(regex2);
				String [] w3 = w1[1].split(regex3);
				
				String [] words = new String[5];
				System.arraycopy(w2, 0, words, 0, w2.length);
				System.arraycopy(w3, 0, words, w2.length,w3.length);
				
				
				//-------------------------------------------------------------------------------------------------------------------------
				//将所有的任务及其相应的全部属性添加到名为“tasks”的动态数组中
				//忽略了任务数量的解析，但可通过获取tasks的长度获取
				//注意在tasks中并没有按照任务的编号进行排序，只是按照生成文件中的顺序存放
				if(words[0].equals("task")){
					
					if(words[1].equals("number")){/*跳出本次循环*/continue;}//任务的总数量
					else{
						number1 = Integer.parseInt(words[1]);
						
						//判断该任务是否是一个新的任务（通过任务编号属性即可得知）
						//如果不是新任务，就从数组中取出相应的已经存在的任务
						//如果是新任务，就直接new一个任务Task对象
						for(Task task:tasks){
							if(task.getNumber()==number1){
								this.task = task;
								break;
							}
						}
						if(task==null){
							task = new Task();
						}

						task.setNumber(number1);//任务编码
					
						//对该任务的技能属性进行操作
						if(words[2].equals("skill")){
							
							if(words[3].equals("number")){/*跳出本次循环*/task=null;number1=-2;continue;}//该任务所需要的技能数
							else{
								Skill skill = new Skill();
								skill.setNumber(Integer.parseInt(words[3]));
								skill.setValue(Integer.parseInt(words[4]));

								task.getSkills().add(skill);
								
							}
						}
						
						//对该任务的花费属性进行操作
						else{
							task.setCost(Double.parseDouble(words[3]));
						}
					}
					
					//如果任务已在数组中存在，那么替换原任务
					//如果任务不在数组中，那么就将该任务添加到数组中
					int logo = -1;
					for(Task task:tasks){
						if(task.getNumber()==number1){
							task = this.task;
							logo = number1;
							break;
						}
					}
					if(logo==-1){tasks.add(task);}
					else{logo=-1;}
					
					task = null;
					number1 = -2;	
				}
				
				
				//------------------------------------------------------------------------------------------------------------------------------
				//将所有的职工极其相应的全部属性添加到名为“employees”的动态数组中
				//忽略了职工数量的解析，但可通过获取employees的长度获取
				//注意在employees中并没有按照职工的编号进行排序，只是按照生成文件中的顺序存放
				else if(words[0].equals("employee")){
					if(words[1].equals("number")){/*跳出本次循环;*/continue;}//人员的总数量
					else{
						number2 = Integer.parseInt(words[1]);
						
						//判断该职工是否是一个新的职工（通过职工编号属性即可得知）
						//如果不是新职工，就从数组中取出相应的已经存在的职工
						//如果是新职工，就直接new一个职工Employee对象
						for(Employee employee:employees){
							if(employee.getNumber()==number2){
								this.employee = employee;
								break;
							}
						}
						if(employee==null){
							employee = new Employee();
						}
						
						employee.setNumber(number2);//职工编码
						
						//对该职工的技能属性进行操作
						if(words[2].equals("skill")){
							if(words[3].equals("number")){/*跳出本次循环*/employee=null;number2=-2;continue;}//该职工所具备的技能数
							else{
								Skill skill = new Skill();
								skill.setNumber(Integer.parseInt(words[3]));
								skill.setValue(Integer.parseInt(words[4]));
								employee.getSkills().add(skill);
							}
						}
						
						//对该职工的薪酬属性进行操作
						else{
							employee.setSalary(Double.parseDouble(words[3]));
						}
						
						//如果职工已在数组中存在，那么替换原职工
						//如果职工不在数组中，那么就将该职工添加到数组中
						int logo=-1;
						for(Employee employee:employees){
							if(employee.getNumber()==number2){
								employee=this.employee;
								logo=number2;
								break;
							}
						}
						if(logo==-1){employees.add(employee);}
						else{logo=-1;}
					}
					
					employee=null;
					number2=-2;
				}
				
				
				//------------------------------------------------------------------------------------------------------------------------------			
				//将所有的弧极其相应的全部属性添加到名为“tpg”的动态数组中
				//忽略了弧数量的解析，但可通过获取tpg的长度获取
				//注意在tpg中并没有按照弧的编号进行排序，只是按照生成文件中的顺序存放
				else if(words[0].equals("graph")){
					if(words[2].equals("number")){System.out.println("文件读取成功：");continue;}
					else{
						Arc arc  =new Arc();
						arc.setNumber(Integer.parseInt(words[2]));
						arc.setFirstNumber(Integer.parseInt(words[3]));
						arc.setLastNumber(Integer.parseInt(words[4]));
						arcs.add(arc);
					}
				}
			}
		}catch(IOException e){
			
			System.out.println("文件中有空行，无法进行读写操作!");
			System.out.println("Reading file is fail!");
		}	
	}
	
	public ArrayList<Task> getTasks(){
		return tasks;
	}
	
	public ArrayList<Employee> getEmployees(){
		return employees;
	}
	
	public ArrayList<Arc> getArcs(){
		return arcs;
	}
	
	//对获得三个动态数组进行排序
	public void sort(){
		Collections.sort(tasks, new Comparator<Task>(){
			public int compare(Task t1,Task t2){
				return t1.getNumber()-t2.getNumber();
			}
		});//按照Task的number属性对tasks进行从小到大排序
		
		Collections.sort(employees, new Comparator<Employee>(){
			public int compare(Employee e1,Employee e2){
				return e1.getNumber()-e2.getNumber();
			}
		});//按照Employee的number属性对employees进行从小到大排序
		
		Collections.sort(arcs, new Comparator<Arc>(){
			public int compare(Arc a1,Arc a2){
				return a1.getNumber()-a2.getNumber();
			}
		});//按照Arc的number属性对arcs进行从小到大排序
		
		for(Task task:tasks){
			Collections.sort(task.getSkills(), new Comparator<Skill>(){
				public int compare(Skill s1,Skill s2){
					return s1.getNumber()-s2.getNumber();
				}
			});//按照Skill的number属性对每一个Task和Employee中的skills数组进行从小到大排序
		}
		
		for(Employee employee:employees){
			Collections.sort(employee.getSkills(), new Comparator<Skill>(){
				public int compare(Skill s1,Skill s2){
					return s1.getNumber()-s2.getNumber();
				}
			});//按照Skill的number属性对每一个Task和Employee中的skills数组进行从小到大排序
		}
	}

	//任务和员工的对应
	public void setEmployee_Task(){
		
		int logo = -1;
		
		// 将对某任务有贡献的员工挑选出来
		for(int i=0;i<tasks.size();i++){
			for(int j=0;j<employees.size();j++){
				for(int k=0;k<employees.get(j).getSkills().size();k++){
					for(int m=0;m<tasks.get(i).getSkills().size();m++){
						if(tasks.get(i).getSkills().get(m).getValue()==employees.get(j).getSkills().get(k).getValue()){
							tasks.get(i).getEmployees().add(employees.get(j));
							logo = 1;
							break;
						}
					}
					if(logo>0){logo = -1;break;}
				}
			}
		}
		
		//对每个任务挑选后的员工按编号进行排序
		for(int i=0;i<tasks.size();i++){
			Collections.sort(tasks.get(i).getEmployees(), new Comparator<Employee>(){
				public int compare(Employee e1,Employee e2){
					return e1.getNumber()-e2.getNumber();
				}
			});
		}
		
	}
	
	//获取每个任务所对应员工的数量
	public int[] getEmployeeNumber_Task(){
		employeeNumber_Task=new int[tasks.size()];
		for(int i=0;i<tasks.size();i++){
			employeeNumber_Task[i]=tasks.get(i).getEmployees().size();
		}
		return employeeNumber_Task;
	}
	
}
