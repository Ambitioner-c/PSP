import init.ReadFile;

import java.util.ArrayList;

import beans.Arc;
import beans.Employee;
import beans.Task;


public class Why {

	private static ArrayList<Employee> employees;
	private static ArrayList<Task> tasks;
	private static ArrayList<Arc> arcs;
	
	public static void main(String[] args) {
		
		ReadFile readFile = new ReadFile("E:\\Study\\project\\","1.txt");
		readFile.init();
		readFile.sort();
		readFile.setEmployee_Task();
		
		tasks = readFile.getTasks();
		employees = readFile.getEmployees();
		arcs = readFile.getArcs();
		
		float[][] x_ij = {
		        {0.0f,1.0f,0.0f,1.0f,1.0f,1.0f,1.0f,0.0f,0.0f,1.0f},
				{0.7f,1.0f,0.0f,0.0f,0.0f,1.0f,1.0f,0.0f,0.0f,1.0f},
				{1.0f,0.6f,0.0f,1.0f,1.0f,0.9f,1.0f,0.0f,1.0f,0.0f},
				{0.8f,0.8f,1.0f,0.3f,1.0f,1.0f,1.0f,0.4f,0.0f,1.0f},
				{0.0f,0.4f,0.9f,0.3f,1.0f,0.6f,0.0f,0.7f,1.0f,1.0f}};
		
		/*float[][] x_ij = {
				{0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f},
				{0.4f,1.0f,0.0f,0.0f,0.0f,1.0f,1.0f,0.3f,0.0f,1.0f},
				{0.9f,0.0f,0.0f,1.0f,1.0f,0.2f,1.0f,0.8f,1.0f,0.0f},
				{0.2f,0.5f,0.9f,0.9f,1.0f,1.0f,1.0f,0.0f,0.0f,1.0f}};*/
		
		/*float[][] x_ij = {
		        {1.0f,0.0f,0.0f,1.0f,0.7f},
				{1.0f,0.8f,0.0f,1.0f,0.0f},
				{1.0f,0.0f,1.0f,0.0f,0.8f},
				{1.0f,0.4f,1.0f,0.0f,0.7f},
				{0.0f,0.2f,1.0f,1.0f,1.0f}};*/
		
		Ant ant = new Ant();
		ant.setX_ij(x_ij);
		
		Evaluate evaluate = new Evaluate();
		evaluate.getEvaluate(employees, tasks, arcs, ant);
	}
	
}
