package beans;

import java.io.Serializable;

public class Skill implements Serializable{
	private int number=-2; //序号
	private int value;  //对应的技能编号

	
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
