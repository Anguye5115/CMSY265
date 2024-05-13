/**
 * @author Austyn Nguyen
 * @date 4/18/2024
 * @description An abstract data type for a Customer object. 
 * @version JAVASE-17
 */

import java.io.Serializable;
import java.util.*;

public class Customer implements Serializable{
	
	private String name;
	private String accNum;
	private int numTV;
	private double costTV;
	private ArrayList<String> idNums;
	private TVType info;
	
	private static final double TAX = 1.06;
	
	public Customer() {
		name = new String();
		accNum = new String();
		numTV = 0;
		costTV = 0;
		idNums = new ArrayList<String>();
		info = null;
	}
	
	public Customer(String name, String accNum, int numTV, ArrayList<String> idNums, TVType info) {
		this.name = name;
		this.accNum = accNum;
		this.numTV = numTV;
		this.idNums = idNums;
		this.info = info;
	}
	
	public Customer(String name, String accNum) {
		this.name = name;
		this.accNum = accNum;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}
	
	public String getAccNum() {
		return accNum;
	}
	
	public void setNumTV(int numTV) {
		this.numTV = numTV;
	}
	
	public int getNumTV() {
		return numTV;
	}
	
	public void setCostTV(double costTV) {
		this.costTV = costTV;
	}
	
	public double getCostTV() {
		return costTV;
	}
	
	public void setIdNums(ArrayList<String> idNums) {
		this.idNums = idNums;
	}
	
	public TVType getInfo() {
		return info;
	}
	
	public void setInfo(TVType info) {
		this.info = info;
	}
	
	public ArrayList<String> getIdNums() {
		return idNums;
	}
	
	public String toString(){
		return String.format("Checkout Receipt:%n"
				+ "Customer: %s%n"
				+ "Account Number: %s%n"
				+ "Purchased %d TVs for $%.2f%n", name, accNum, numTV, due());
	}
	
//	public String toString() {
//		return String.format("%.2f", due());
//	}
	
	public double due() {
		double preTax = numTV * info.getPrice();
		double postTax = preTax * TAX;
		return postTax;
	}
}
