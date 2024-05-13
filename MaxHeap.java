import java.util.*;

public class MaxHeap {
	private DelInfo[] arrDel;
	private int max_size;
	private int size;
	
	//1 instead of 0 because of false root
	private static final int FRONT = 1;
	
	public MaxHeap(int max_size) {
		this.max_size = max_size;
		this.size = 0;
		
		//make room for a false root
		arrDel = new DelInfo[max_size + 1];
		
		//make false root largest possible number
		arrDel[0] = new DelInfo();
		arrDel[0].setnumTV(Integer.MAX_VALUE);
	}
	
	public int getsize() {
		return size;
	}
	
	private int getParentPosition(int position) {
		return position / 2;
	}
	
	private int getLeftChildPosition(int position) {
		return (2 * position);
	}
	
	private int getRightChildPosition(int position) {
		return (2 * position) + 1;
	}
	
	private boolean checkLeaf(int position) {
		if (position >= (size / 2) && position <= size) {
			return true;
		} else {
			return false;
		}
	}
	
	private void swap(int currentIndex, int parentIndex) {
		DelInfo temp = arrDel[currentIndex];
		arrDel[currentIndex] = arrDel[parentIndex];
		arrDel[parentIndex] = temp;
	}
	
	public void insertNode(DelInfo info) {
		if (size >= max_size) {
			System.out.println("Max size reached. Cannot add anymore.\n");
			return;
		} else {
			arrDel[++size] = info;
			int current = size;
			
			while (arrDel[current].getnumTV() > arrDel[getParentPosition(current)].getnumTV()) {
				swap(current, getParentPosition(current));
				current = getParentPosition(current);
			}
		}
	}
	
	public void designMaxHeap() {
		for (int position = (size / 2); position >= 1; position--) {
			maxHeapify(position);
		}
	}
	
	private void maxHeapify(int position) {
		//make children selected within range
		boolean leftChild = getLeftChildPosition(position) <= size;
		boolean rightChild = getRightChildPosition(position) <= size;
		
		//check if the given node is a leaf
		if (!checkLeaf(position) && (leftChild || rightChild)) {
			//check if the given node is less than its children
			//if it is, must max heapify
				if (arrDel[position].getnumTV() < arrDel[getLeftChildPosition(position)].getnumTV() ||
					arrDel[position].getnumTV() < arrDel[getRightChildPosition(position)].getnumTV()) {
				
				//swap with left child, the recursively heapify left child
				if (arrDel[getLeftChildPosition(position)].getnumTV() > arrDel[getRightChildPosition(position)].getnumTV()) {
					swap(position, getLeftChildPosition(position));
					maxHeapify(getLeftChildPosition(position));
				} else {
					//swap with right child ad heapify right child
					swap(position, getRightChildPosition(position));
					maxHeapify(getRightChildPosition(position));
				}
			}
		}
	}
	
	//remove maximum element from heap
	public DelInfo removeRoot() {
		DelInfo popElement = arrDel[FRONT];
		//take the last leaf to the root
		arrDel[FRONT] = arrDel[size--];
		//heapify after moving last leaf to root
		maxHeapify(FRONT);
		return popElement;
	}
}
