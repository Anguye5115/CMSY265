/**
 * @author Austyn Nguyen
 * @date 4/18/2024
 * @description An abstract data type for a TV object. Stores specific ID numbers.
 * @version JAVASE-17
 */

public class TV implements Comparable<TV> {

	private String idNum;
	private TVType info;
	
	public TV() {
		idNum = null;
	}
	
	public TV(String idNum) {
		this.idNum = idNum;
		this.info = null;
	}
	
	public TV(String idNum, TVType info) {
		this.idNum = idNum;
		this.info = info;
	}
	
	public String getId() {
		return idNum;
	}
	
	public TVType getInfo() {
		return info;
	}
	
	public void setId(String idNum) {
		this.idNum = idNum;
	}
	
	public void setInfo(TVType info) {
		this.info = info;
	}
	
	public String toString() {
		if (info != null) {
			return ("The TV id number is: " + getId() + "\n" + info.toString());	
		} else {
			return ("The TV id number is: " + getId());
		}
	}

	public int compareTo(TV newTv) {
		return idNum.compareTo(newTv.getId());
	}
	
}
