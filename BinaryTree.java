import java.util.Stack;

/**
 * @author Austyn Nguyen
 * @date 4/18/2024
 * @description A BST that uses the nodes of TVType. 
 * @version JAVASE-17
 */

public class BinaryTree {
	private Node root;
	private static boolean bFlag = false;
	
	public BinaryTree() {
		root = null;
	}
	
	public void setRoot(Node root) {
		this.root = root;
	}
	
	public Node getRoot() {
		return root;
	}
	
	public void resetFlag() {
		bFlag = false;
	}
	
	public void insertNode(TVType info) {
		Node newNode = new Node(info);
		bFlag = true;
		
		//check for empty tree
		if (root == null) {
			root = newNode;
			return;
		} else {
			//where do I put this new node?
			Node current = root;
			Node parent = null;
			
			while (bFlag) {
				//current node is about to change; set parent node to current
				parent = current;
				
				//less than, insert left
				if (info.getPrice() < current.getInfo().getPrice()) {
					current = current.getLeft();
					
					if (current == null) {
						parent.setLeft(newNode);
						resetFlag();
					}
				}
				
				//greater than, insert right
				else {
					current = current.getRight();
					
					if (current == null) {
						parent.setRight(newNode);
						resetFlag();
					}
				}
			}
		}
	}
	
	//inorder: left > root > right
	public void printAll() {
		bFlag = true;
		Node current = root;
		Stack<Node> stack = new Stack<Node>();
		int i = 0;
		
		System.out.println("TV Options: ");
		System.out.println(String.format("%-8s%-25s%-20s%-8s", "Item", "Brand", "Model", "Cost"));
		System.out.println(String.format("%-8s%-25s%-20s%-8s", "----", "-----", "-----", "----"));
		
		while (current != null || !stack.isEmpty()) {
			//traverse to left leaf or left child
			while (current != null) {
				stack.push(current);
				current = current.getLeft();
			}
			
			//go back to ancestors and their right child
			current = stack.pop();
			i++;
				
			System.out.printf("%-8s", i);
			System.out.println(current.getInfo().toString());
				
			current = current.getRight();	
		}
	}
	
	public TVType findTV(String brand, String model) {
		TVType iType;
		
		if (root == null) {
			System.out.println("Tree is empty");
		} else {
			Node current = root;
			Stack<Node> stack = new Stack<Node>();
			bFlag = true;
			
			while ((current != null || !stack.isEmpty()) && bFlag) {
				
				//get left-most until null
				while (current != null) {
					stack.push(current);
					current = current.getLeft();
				}
				
				//pop and check
				current = stack.pop();
				iType = current.getInfo();
				
				//if equal, then return type
				if (iType.getBrand().equals(brand) && iType.getModel().equals(model)) {
					resetFlag();
					return iType;
				}
				
				//parse right child
				current = current.getRight();	
			}
		}
		
		//returns null if not found
		return null;
	}
}
