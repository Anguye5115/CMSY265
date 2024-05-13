/**
 * @author Austyn Nguyen
 * @date 4/18/2024
 * @description An abstract data type for a TV object. 
 * @version JAVASE-17
 */

public class TVType {
	private String brand;
	private String model;
	private double price;
	
	public TVType() {
		brand = "none";
		model = "none";
		price = 0;
	}
	
	public TVType(String brand, String model, double price) {
		this.brand = brand;
		this.model = model;
		this.price = price;
	}
	
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String getBrand() {
		return brand;
	}
	
	public String getModel() {
		return model;
	}
	
	public double getPrice() {
		return price;
	}
	
	//25 width
	//20 width
	public String toString() {
	    return String.format("%-25s" + "%-20s" + "$%.2f", brand, model, price);
	}

}
