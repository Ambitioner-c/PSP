package beans;

import java.io.Serializable;

public class Skill implements Serializable{
	private int number=-2; //���
	private int value;  //��Ӧ�ļ��ܱ��

	
	public void setNumber(int number){
		this.number=number;
	}
	public int getNumber(){
		return number;
	}
	
	public void setValue(int value){
		this.value=value;
	}
	public int getValue(){
		return value;
	}
	
	
}
