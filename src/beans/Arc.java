package beans;

import java.io.Serializable;

public class Arc implements Serializable{
	
	private int number;  //序号
	private int firstNumber;  //头结点序号
	private int lastNumber;  //尾结点序号

	
	public void setNumber(int number){
		this.number = number;
	}
	public int getNumber(){
		return number;
	}
	
	public void setFirstNumber(int firstNumber){
		this.firstNumber = firstNumber;
	}
	public int getFirstNumber(){
		return firstNumber;
	}
	
	public void setLastNumber(int lastNumber){
		this.lastNumber = lastNumber;
	}
	public int getLastNumber(){
		return lastNumber;
	}
	
}
