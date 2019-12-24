package test;
import init.ReadFile;

import java.util.ArrayList;

import beans.*;


public class ReadTest {
	
	private static  ArrayList<Task> tasks;				//����
	private static ArrayList<Employee> employees;		//Ա����
	private static ArrayList<Arc> arcs;					//����
	private static int [] employeeNumber_Task;			//һ��������Ա������
	
	public static void main(String[] args){
		
		//���ļ�
		ReadFile readFile = new ReadFile("E:\\Study\\project\\","1.txt");
		readFile.init();
		readFile.sort();
		
		tasks = readFile.getTasks();
		employees = readFile.getEmployees();
		arcs = readFile.getArcs();
		readFile.setEmployee_Task();
		employeeNumber_Task = readFile.getEmployeeNumber_Task();
	
		System.out.println("��֤�����Ƿ��Ѿ�ȫ������ȷ�洢��");
		for(Task task:tasks){
			System.out.println(task.getNumber()+"����effortΪ��"+task.getCost());
			System.out.println(task.getNumber()+"����Ҫ����"+task.getSkills().size());
			for(Skill ski:task.getSkills()){
				
				System.out.println(task.getNumber()+"���輼��"+ski.getNumber()+"��ֵΪ��"+ski.getValue());
			}
			System.out.println(task.getNumber()+"����Ҫְ��"+employeeNumber_Task[task.getNumber()]);
			for(Employee employee:task.getEmployees()){
				
				System.out.println(task.getNumber()+"����Ա���ı��Ϊ"+employee.getNumber());
			}
			
			System.out.println("");
		}
		
		System.out.println("----------------------------------------------------------------------");
		
		
		System.out.println("----------------------------------------------------------------------");
		System.out.println("��ְ֤���Ƿ��Ѿ�ȫ������ȷ�洢��");
		for(Employee employee:employees){
			System.out.println(employee.getNumber()+"н��Ϊ��"+employee.getSalary());
			System.out.println(employee.getNumber()+"���߱�����"+employee.getSkills().size());
			for(Skill ski:employee.getSkills()){
				
				System.out.println(employee.getNumber()+"�߱�����"+ski.getNumber()+"��ֵΪ��"+ski.getValue());
			}
			System.out.println("");
		}
		
		
		System.out.println("----------------------------------------------------------------------");
		System.out.println("��֤���Ƿ��Ѿ�ȫ������ȷ�洢��");
		for(Arc arc:arcs){
			System.out.println("��"+arc.getNumber()+"���ɽ��"+arc.getFirstNumber()+"ָ��"+arc.getLastNumber());
		}
		
		
		System.out.println("Test is over!");
	
	}
}
