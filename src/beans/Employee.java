package beans;

import java.io.Serializable;
import java.util.ArrayList;

public class Employee implements Serializable{
	
	private int number=-2;  //���
	private double salary;  //н��
	private ArrayList<Skill> skills=new ArrayList<Skill>();  //���ܼ���
	
	public void setNumber(int number){
		this.number=number;
	}
	public int getNumber(){
		return number;
	}
	
	public void setSalary(double salary){
		this.salary=salary;
	}
	public double getSalary(){
		return salary;
	}
	
	public void setSkills(ArrayList<Skill> skills){
		this.skills=skills;
	}
	public ArrayList<Skill> getSkills(){
		return skills;
	}
}
