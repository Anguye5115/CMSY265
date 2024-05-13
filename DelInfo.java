
public class DelInfo {
	private String name;
	private String accNum;
	private String address;
	private int numTV;
	
	public DelInfo() {
		name = "none";
		accNum = "none";
		address = "none";
		numTV = 0;
	}
	
	public DelInfo(String name, String accNum, String address, int numTV) {
		this.name = name;
		this.accNum = accNum;
		this.address = address;
		this.numTV = numTV;
	}
	
	public String getname() {
		return name;
	}
	
	public String getaccNum() {
		return accNum;
	}
	
	public String getaddress() {
		return address;
	}
	
	public int getnumTV() {
		return numTV;
	}
	
	public void setname(String name) {
		this.name = name;
	}
	
	public void setaccNum(String accNum) {
		this.accNum = accNum;
	}
	
	public void setaddress(String address) {
		this.address = address;
	}
	
	public void setnumTV(int numTV) {
		this.numTV = numTV;
	}
	
	public String toString(){
		return String.format(
				"Name: %-25s" + "Account Number: %s%n"
				+ "Address: %s%n"
				+ "Number of TVs: %d%n%n", name, accNum, address, numTV);
	}
}
