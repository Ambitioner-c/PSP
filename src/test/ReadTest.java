package test;
import init.ReadFile;

import java.util.ArrayList;

import beans.*;


public class ReadTest {
	
	private static  ArrayList<Task> tasks;				//任务集
	private static ArrayList<Employee> employees;		//员工集
	private static ArrayList<Arc> arcs;					//弧集
	private static int [] employeeNumber_Task;			//一项任务中员工个数
	
	public static void main(String[] args){
		
		//读文件
		ReadFile readFile = new ReadFile("E:\\Study\\project\\","1.txt");
		readFile.init();
		readFile.sort();
		
		tasks = readFile.getTasks();
		employees = readFile.getEmployees();
		arcs = readFile.getArcs();
		readFile.setEmployee_Task();
		employeeNumber_Task = readFile.getEmployeeNumber_Task();
	
		System.out.println("验证任务是否已经全部被正确存储！");
		for(Task task:tasks){
			System.out.println(task.getNumber()+"所需effort为："+task.getCost());
			System.out.println(task.getNumber()+"共需要技能"+task.getSkills().size());
			for(Skill ski:task.getSkills()){
				
				System.out.println(task.getNumber()+"所需技能"+ski.getNumber()+"的值为："+ski.getValue());
			}
			System.out.println(task.getNumber()+"共需要职工"+employeeNumber_Task[task.getNumber()]);
			for(Employee employee:task.getEmployees()){
				
				System.out.println(task.getNumber()+"所需员工的编号为"+employee.getNumber());
			}
			
			System.out.println("");
		}
		
		System.out.println("----------------------------------------------------------------------");
		
		
		System.out.println("----------------------------------------------------------------------");
		System.out.println("验证职工是否已经全部被正确存储！");
		for(Employee employee:employees){
			System.out.println(employee.getNumber()+"薪酬为："+employee.getSalary());
			System.out.println(employee.getNumber()+"所具备技能"+employee.getSkills().size());
			for(Skill ski:employee.getSkills()){
				
				System.out.println(employee.getNumber()+"具备技能"+ski.getNumber()+"的值为："+ski.getValue());
			}
			System.out.println("");
		}
		
		
		System.out.println("----------------------------------------------------------------------");
		System.out.println("验证弧是否已经全部被正确存储！");
		for(Arc arc:arcs){
			System.out.println("弧"+arc.getNumber()+"是由结点"+arc.getFirstNumber()+"指向"+arc.getLastNumber());
		}
		
		
		System.out.println("Test is over!");
	
	}
}
