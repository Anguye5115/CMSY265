/**
 * @author Austyn Nguyen
 * @date 4/18/2024
 * @description A node class for a BST. 
 * @version JAVASE-17
 */

public class Node {
	private TVType info;
	private Node left;
	private Node right;
	
	public Node(TVType newInfo) {
		this.info = newInfo;
		left = null;
		right = null;
	}
	
	public void setInfo(TVType info) {
		this.info = info;
	}
	
	public void setLeft(Node left) {
		this.left = left;
	}
	
	public void setRight(Node right) {
		this.right = right;
	}
	
	public TVType getInfo() {
		return info;
	}
	
	public Node getLeft() {
		return left;
	}
	
	public Node getRight() {
		return right;
	}
}
