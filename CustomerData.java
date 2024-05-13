/**
 * @author Austyn Nguyen
 * @date 4/18/2024
 * @description Stores Customer objects and their data for iteration purposes
 * @version JAVASE-17
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CustomerData implements Iterable<Customer>, Serializable {
	private List<Customer> customerList;
	
	public CustomerData() {
		customerList = new LinkedList<Customer>();
	}
	
	public Iterator<Customer> iterator() {
		return customerList.iterator();
	}
	
	public void addCustomer(Customer c) {
		customerList.add(c);
	}
	
	public void removeCustomer(Customer c) {
		customerList.remove(c);
	}
	
	public Customer findCustomer(String accNum) {
		Iterator<Customer> itr = customerList.iterator();
		boolean found = false;
		Customer foundGuy = null;
		
		while (itr.hasNext() && (!found)) {
			foundGuy = itr.next();
			if (foundGuy.getAccNum().equals(accNum)) {
				found = true;
			}
		}
		
		if (!found) {
			foundGuy = null;
		}
		
		return foundGuy;
	}
	
	public int getSize() {
		return customerList.size();
	}
	
	public void displayAll() {
		int index = 1;
		for (Customer c : customerList) {
			System.out.printf("Customer");
			System.out.printf("%3s", index);
			System.out.printf("%-2s", ":");
			System.out.printf("%-25s", c.getName());
			System.out.printf("Account Number:");
			System.out.printf("%11s", c.getAccNum());
			System.out.printf("%n", "");
			index++;
		}
	}
	
	public Customer[] convert() {
		Object[] obj = new Object[customerList.size()];
		obj = customerList.toArray();
		Customer[] newList = new Customer[obj.length];
		
		for (int i = 0; i < obj.length; i++) {
			newList[i] = (Customer)(obj[i]);
		}
		
		return newList;
	}
	
}
