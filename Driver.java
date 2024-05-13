/**
 * @author Austyn Nguyen
 * @date 4/18/2024
 * @description Driver for inventory control software. Uses Stack and Queue collections.
 * @version JAVASE-17
 */

import java.util.*;
import java.io.*;
import java.util.Stack;

public class Driver {
	
	//main menu
	private static final int MENU1 = 1;
	private static final int MENU2 = 2;
	private static final int MENU3 = 3;
	private static final int MENU4 = 4;
	private static final int MENU5 = 5;
	private static final int MENU6 = 6;
	private static final int MENU7 = 7;
	private static final int MENU8 = 8;
	private static final int MENU9 = 9;
	private static final int MENU10 = 10;
	
	private static final int NUMTV = 14;
	private static final int STOCKTV = 5;
	private static final int MINBUY = 1;
	private static TV top;
	
	//used to read customer file
	private static final int FILE_MODULO = 2;
	private static final int NAME_MODULO = 1;
	private static final int ID_MODULO = 0;
	
	//used to read tv file
	private static final int TVFILE_MODULO = 3;
	private static final int BRAND_MODULO = 1;
	private static final int MODEL_MODULO = 2;
	private static final int PRICE_MODULO = 0;
	
	//customer update menu
	private static final int CUST_MENU1 = 1;
	private static final int CUST_MENU2 = 2;
	private static final int CUST_MENU3 = 3;
	private static final int CUST_MENU4 = 4;
	private static final int CUST_MENU5 = 5;
	private static final int CUST_MENU6 = 6;
	private static boolean CUSTOMER_MENU = false;
	private static boolean CHANGED = false;
	private static boolean SAVED = false;
	private static final String YES = "Y";
	private static final String NO = "N";
	
	private static final String NONE = "NONE";
	private static final String MAIN = "MAIN";
	private static final String CUSTOMER = "CUST";
	
	private static Customer[] customerList;
	private static final int BASE_CASE = 1;
	
	private static final int MAX_DELIVERIES = 25;
	
	private static final double SALES_TAX = 1.06;
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int userInput = 0;
		boolean validMenu = false;
		boolean quit = false;
		File tvFile = new File("src/stack.txt");
		File custFile = new File("src/CustFile.txt");
		Stack<TV> ids = new Stack<>();
		Queue<Customer> queue = new LinkedList<Customer>();
		CustomerData customerData = new CustomerData();
		MaxHeap heap = null;
		
		System.out.println("Lab 7: Sets and Maps");
		System.out.println("Copyright @2022 - Howard Community College All rights reserved; Unauthorized duplication prohibited.\n");
		System.out.println("CMSY 265 TV Inventory Control Program\n");
		
		readData(ids, tvFile);
		readData(customerData, custFile);
		
		customerData = callSort(customerData);
		top = ids.peek();
		
		do {
			System.out.println("Menu Options");
			System.out.println(MENU1 + " - Stock Shelves");
			System.out.println(MENU2 + " - Fill Web Order");
			System.out.println(MENU3 + " - Restock Return");
			System.out.println(MENU4 + " - Restock Inventory");
			System.out.println(MENU5 + " - Customer Update");
			System.out.println(MENU6 + " - Customer Purchase");
			System.out.println(MENU7 + " - Customer Checkout");
			System.out.println(MENU8 + " - Display Delivery List");
			System.out.println(MENU9 + " - Display Inventory");
			System.out.println(MENU10 + " - End Program");
			
			System.out.print("Please enter the menu choice: ");
			
			do {
				try {
					userInput = input.nextInt();
					validateLimit(userInput, MAIN);
				} catch(InputMismatchException e) {
					System.out.println("Error - input must be an integer");
					input.nextLine();
				} catch (ArithmeticException e){
					System.out.println(e);
				} finally {
					if ((userInput >= MENU1) && (userInput <= MENU10)) {
						validMenu = true;
					} else {
						System.out.print("Please enter a valid option: ");
						validMenu = false;
					}
				}
			} while (!validMenu);	
			
			switch (userInput) {
			case MENU1:
				stockShelves(ids);
				break;
			case MENU2:
				webOrder(ids);
				break;
			case MENU3:
				restockReturn(ids);
				break;
			case MENU4:
				restockInv(ids);
				break;
			case MENU5:
				CUSTOMER_MENU = true;
				CHANGED = false;
				SAVED = false;
				while(CUSTOMER_MENU) {
					customerUpdate(customerData);
					customerData = callSort(customerData);
				}
				break;
			case MENU6:
				customerData = callSort(customerData);
				customerPurch(ids, queue, customerData);
				customerData = callSort(customerData);
				if (CHANGED) {
					System.out.println("Customer Data has been updated. You must save now.\n");
					save2File(customerData);	
				}
				break;
			case MENU7:
				customerCO(queue);
				break;
			case MENU8:
				heap = readData();
				int iterator = 1;
				int size = heap.getsize();
				
				System.out.println();
				System.out.println("Delivery Report");
				System.out.println("---------------");
				while (iterator <= size) {
					System.out.println("Delivery Stop #" + iterator);
					System.out.print(heap.removeRoot().toString());
					iterator++;
				}
				break;
			case MENU9:
				displayAll(ids);
				break;
			case MENU10:
				input.nextLine(); //input buffer
				if (!queue.isEmpty()) {
					System.out.println("There are still customers waiting to check out!");
					quit = false;
				} else if (CHANGED && !SAVED) { //extra credit: checking save
					String choice;
					boolean validSaveQuit = false;
					
					do {
						System.out.print("WARNING - Data has not been saved. Do you wish to proceed to exit (Y/N)?: ");
						choice = input.nextLine();
						
						if(choice.toUpperCase().equals(YES)) {
							System.out.println("Final Inventory:");
							displayAll(ids);
							System.out.println("Thank you for using the program");
							quit = true;	
							validSaveQuit = true;
						} else if(choice.toUpperCase().equals(NO)) {
							quit = false;
							validSaveQuit = true;
						} else {
							System.out.println("Error - Invalid entry, please re-enter");
							validSaveQuit = false;
						}
					} while (!validSaveQuit);
				} else {
					System.out.println("Final Inventory:");
					displayAll(ids);
					System.out.println("Thank you for using the program");
					quit = true;	
				}
				break;
			}
			System.out.println();
 		} while (!quit);
		
		
	}
	
	public static void readData(Stack<TV> ids, File file) {
		try (Scanner fRead = new Scanner(file)) {
			while (fRead.hasNext()) {
				TV tv = new TV(fRead.nextLine());
				ids.push(tv);
			}
			fRead.close();
		} catch (IOException | NoSuchElementException | IllegalStateException e) {
			System.out.println("The TV Data file could not be opened.");
		}
	}
	
	public static void readData(CustomerData customerData, File file) {
		Customer newGuy;
		String name = "";
		String accNum = "";
		int i = 1;
		try (Scanner fRead = new Scanner(file)) {
			while (fRead.hasNext()) {
				if((i % FILE_MODULO) == NAME_MODULO) {
					name = fRead.nextLine();
				} else if ((i % FILE_MODULO) == ID_MODULO) {
					accNum = fRead.nextLine();
					newGuy = new Customer(name, accNum);
					customerData.addCustomer(newGuy);
				}
				i++;
			}
			fRead.close();
		} catch (IOException | NoSuchElementException | IllegalStateException e) {
			System.out.println("The Customer Data file could not be opened.");
		}
	}
	
	public static void readData(BinaryTree tree) {
		Scanner input = new Scanner(System.in);
		String fileName;
		File newFile = null;
		String brand = null;
		String model = null;
		double price = 0;
		
		//reading file and validating file existence
		boolean flag = true;
		do {
			try {
				System.out.print("Please enter in the TV Types file path: ");
				fileName = input.nextLine();
				
				if (!fileName.endsWith(".txt")) {
					throw new IllegalArgumentException("The file must be a .txt file, please re-enter");
				} else if (!fileName.startsWith("C:") && !fileName.startsWith("c:")) {
					throw new IllegalArgumentException("The file must be in the local directory \'C:\', please re-enter");
				}
				
				newFile = new File(fileName);
				
				if (!newFile.exists()) {
					throw new IllegalArgumentException("The file could not be found, please re-enter");
				}
				
				flag = false;
			} catch (IllegalArgumentException e) {
				System.out.println(e);
				flag = true;
			}	
		} while (flag);
		
		try (Scanner fRead = new Scanner(newFile)) {
			boolean bBrand = false, bModel = false;
			
			while (fRead.hasNext()) {
				if (!bBrand) {
					brand = fRead.nextLine();
					bBrand = true;
				} else if (!bModel) {
					model = fRead.nextLine();
					bModel = true;
				} else {
					price = Double.parseDouble(fRead.nextLine());
					TVType newInfo = new TVType(brand, model, price);
					tree.insertNode(newInfo);
					
					bBrand = false;
					bModel = false;
				}
			}
			fRead.close();
		} catch (IOException | NoSuchElementException | IllegalStateException e) {
			System.out.println("The TV File could not be opened.");
		}
		
	}
	
	public static MaxHeap readData() {
		Scanner input = new Scanner(System.in);
		String fileName;
		File newFile = null;
		MaxHeap heap = new MaxHeap(MAX_DELIVERIES);
		
		String name = null, address = null, accNum = null;
		int numTV = 0;
		
		//reading file and validating file existence
		boolean flag = true;
		do {
			try {
				System.out.print("Please enter in the delivery info file path: ");
				fileName = input.nextLine();

				if (!fileName.endsWith(".txt")) {
					throw new IllegalArgumentException("The file must be a .txt file, please re-enter");
				} else if (!fileName.startsWith("C:") && !fileName.startsWith("c:")) {
					throw new IllegalArgumentException("The file must be in the local directory \'C:\', please re-enter");
				}

				newFile = new File(fileName);

				if (!newFile.exists()) {
					throw new IllegalArgumentException("The file could not be found, please re-enter");
				}

				flag = false;
			} catch (IllegalArgumentException e) {
				System.out.println(e);
				flag = true;
			}	
		} while (flag);
		
		try (Scanner fRead = new Scanner(newFile)) {
			boolean bname = false, baddress = false, baccNum = false;
			
			while (fRead.hasNext()) {
				if (!bname) {
					name = fRead.nextLine();
					bname = true;
				} else if (!baddress) {
					address = fRead.nextLine();
					baddress = true;
				} else if (!baccNum) {
					accNum = fRead.nextLine();
					baccNum = true;
				} else {
					numTV = Integer.parseInt(fRead.nextLine());
					DelInfo newDel = new DelInfo(name, accNum, address, numTV);
					heap.insertNode(newDel);
					heap.designMaxHeap();
					
					bname = false;
					baddress = false;
					baccNum = false;
				}
			}
			fRead.close();
		} catch (IOException | NoSuchElementException | IllegalStateException e) {
			System.out.println("The Delivery Information File could not be opened.");
		}
		
		return heap;
	}
	
	public static void appendDelFile(String name, String address, String accNum, int numTV) {
		Scanner input = new Scanner(System.in);
		String fileName;
		File newFile = null;
		FileWriter writer;
		//reading file and validating file existence
		boolean flag = true;
		do {
			try {
				System.out.print("Please enter in the delivery info file path: ");
				fileName = input.nextLine();

				if (!fileName.endsWith(".txt")) {
					throw new IllegalArgumentException("The file must be a .txt file, please re-enter");
				} else if (!fileName.startsWith("C:") && !fileName.startsWith("c:")) {
					throw new IllegalArgumentException("The file must be in the local directory \'C:\', please re-enter");
				}

				newFile = new File(fileName);

				if (!newFile.exists()) {
					throw new IllegalArgumentException("The file could not be found, please re-enter");
				} else {
					System.out.println("File already exists.");
					System.out.println("Successfully wrote to file.");
				}
				
				flag = false;
			} catch (IllegalArgumentException e) {
				System.out.println(e);
				flag = true;
			}	
		} while (flag);
	
		try {
			writer = new FileWriter(newFile, true);
			writer.write(name + "\n");
			writer.write(address + "\n");
			writer.write(accNum + "\n");
			writer.write(numTV + "\n");
			writer.close();
		} catch (IOException e) {
			System.out.println("Error in writing to the Delivery File.\n");
		}
	}
	
	public static void writeFile(CustomerData customerData, String fileName) {
		try {
			if (!fileName.endsWith(".txt")) {
				throw new IllegalArgumentException("The file must be a .txt file, please re-enter");
			} else if (!fileName.startsWith("C:") && !fileName.startsWith("c:")) {
				throw new IllegalArgumentException("The file must be in the local directory \'C:\', please re-enter");
			}
			File newFile = new File(fileName);
			FileWriter writer = new FileWriter(newFile);
			System.out.println("File created: " + newFile.getName());
			for (Customer c : customerData) {
				writer.write(c.getName());
				writer.write("\n");
				writer.write(c.getAccNum());
				writer.write("\n");
			}
			System.out.println("Successfully wrote to the file.");
			writer.close();
		} catch (IOException | NoSuchElementException | IllegalStateException e) {
			System.out.println("The data could not be saved, please enter another file name");
			save2File(customerData);
		} catch (IllegalArgumentException e) {
			System.out.println(e);
			save2File(customerData);
		}
	}
	
	public static void validateLimit(int num, String str) {
		if (str.equals(MAIN)) {
			if ((num < MENU1) || (num > MENU10)) {
				throw new ArithmeticException("Error - Not within option range.");
			}	
		} else if (str.equals(CUSTOMER)) {
			if ((num < CUST_MENU1) || (num > CUST_MENU6)) {
				throw new ArithmeticException("Error - Not within option range.");
			}	
		}
	}
	
	public static void validateNumTV(Stack<TV> ids, int num) {
		if (num > ids.size()) {
			throw new ArrayIndexOutOfBoundsException("Error - There are only " + ids.size() + " TVs available.");
		} else if (num < MINBUY) {
			throw new ArrayIndexOutOfBoundsException("Error - Must buy a positive number of TVs.");
		}
	}
	
	public static void stockShelves(Stack<TV> ids) {
		if (ids.size() < STOCKTV) {
			System.out.println("There are insufficient TVs available.");
		} else {
			System.out.println("The following TV's have been placed on the floor for sale:");
			for (int i = 0; i < STOCKTV; i++) {
				System.out.println(ids.pop().toString());
			}
			System.out.println("There are " + ids.size() + " TV's left in inventory");
		}
	}
	
	public static void webOrder(Stack<TV> ids) {
		if (!ids.isEmpty()) {
			System.out.println("The following TV's has been shipped: ");
			System.out.println(ids.pop().toString());
			System.out.println("There are " + ids.size() + " TV's left in inventory");
		} else {
			System.out.println("There are insufficient TVs available.");
		}
	}
	
	public static void restockReturn(Stack<TV> ids) {
		Scanner input = new Scanner(System.in);
		String topID = top.getId();
		int iHyphen = topID.indexOf("-");
		String Snum = topID.substring(iHyphen + 1, topID.length());
		int Inum = Integer.parseInt(Snum);
		TVReturn returns = new TVReturn();
		boolean bFlag = true;
		String idNum;
		
		while (bFlag) {
			System.out.print("Please enter the ID number of the returned TV: ");
			idNum = input.nextLine();
			
			TV tv = returns.contains(idNum);
			
			if (tv != null) {
				double dPrice = tv.getInfo().getPrice() * SALES_TAX;
				String price = String.format("%.2f", dPrice);
				System.out.println("The customer should receive credit for: $" + price);
				bFlag = false;
			}
		}

		TV newTV = new TV(topID.substring(0, iHyphen + 1) + (Inum + 1));
		ids.push(newTV);
		top = ids.peek();
		System.out.println("There are " + ids.size() + " TV's left in inventory");	
	}
	
	public static void restockInv(Stack<TV> ids) {
		String topID = top.getId();
		int iHyphen = topID.indexOf("-");
		String Snum = topID.substring(iHyphen + 1, topID.length());
		int Inum = Integer.parseInt(Snum);
		
		for (int i = 0; i < STOCKTV; i++) {
			TV newTV = new TV(topID.substring(0, iHyphen + 1) + (Inum + 1 + i));
			ids.push(newTV);
		}
		
		top = ids.peek();
		
		System.out.println("There are " + ids.size() + " TV's left in inventory");
	}
	
	public static void customerUpdate(CustomerData customerData) {
		Scanner input = new Scanner(System.in);
		int userInput = 0;
		boolean validMenu = false;
		
		System.out.println("Customer Update Menu Options");
		System.out.println(CUST_MENU1 + " - Add a Customer");
		System.out.println(CUST_MENU2 + " - Delete a Customer");
		System.out.println(CUST_MENU3 + " - Change Customer Name");
		System.out.println(CUST_MENU4 + " - Save Changes");
		System.out.println(CUST_MENU5 + " - Display Customer List");
		System.out.println(CUST_MENU6 + " - Return to Main");
		
		System.out.print("Please enter the menu choice: ");
		
		do {
			try {
				userInput = input.nextInt();
				validateLimit(userInput, CUSTOMER);
			} catch(InputMismatchException e) {
				System.out.println("Error - input must be an integer");
				input.nextLine();
			} catch (ArithmeticException e){
				System.out.println(e);
			} finally {
				if ((userInput >= CUST_MENU1) && (userInput <= CUST_MENU6)) {
					validMenu = true;
				} else {
					System.out.print("Please enter a valid option: ");
					validMenu = false;
				}
			}
		} while (!validMenu);	
		
		switch (userInput) {
		case CUST_MENU1:
			addCustomer(customerData);
			CHANGED = true;
			break;
		case CUST_MENU2:
			removeCustomer(customerData);
			CHANGED = true;
			break;
		case CUST_MENU3:
			updateName(customerData);
			CHANGED = true;
			break;
		case CUST_MENU4:
			save2File(customerData);
			SAVED = true;
			break;
		case CUST_MENU5:
			displayCustomer(customerData);
			break;
		case CUST_MENU6:
			displayCustomer(customerData);
			back2Menu(customerData);
			break;
		}

	}
	
	public static void addCustomer(CustomerData customerData) {
		Scanner input = new Scanner(System.in);
		String name, accNum;
		Iterator<Customer> itr = customerData.iterator();
		boolean duplicate = false;
		Customer newGuy;
		
		System.out.println("Add a new customer to the system");
		System.out.print("Please enter the customer name: ");
		name = input.nextLine();
		
		do {
			duplicate = false;
			System.out.print("Please enter the customer ID number: ");
			accNum = input.nextLine();
			
			//check for uniqueness
			while(itr.hasNext() && (!duplicate)) {
				if (itr.next().getAccNum().equals(accNum)) {
					duplicate = true;
				}
			}
			
			if (duplicate) {
				System.out.println("Error - This account already exists, please re-enter\n");
			}
		} while (duplicate);
		
		newGuy = new Customer(name, accNum);
		customerData.addCustomer(newGuy);
	}
	
	public static void removeCustomer(CustomerData customerData) {
		Scanner input = new Scanner(System.in);
		String accNum;
		Customer oldGuy;
		
		System.out.println("Remove a customer from the system");
		
		do {
			System.out.print("Please enter the customer ID number: ");
			accNum = input.nextLine();
			oldGuy = customerData.findCustomer(accNum);
			
			if (oldGuy != null) {
				customerData.removeCustomer(oldGuy);
			} else {
				System.out.println("Error - This account does not exist, please re-enter\n");
			}	
		} while (oldGuy == null);
	}
	
	public static void updateName(CustomerData customerData) {
		Scanner input = new Scanner(System.in);
		String accNum, name;
		Customer oldGuy;
		
		System.out.println("Change the name of a customer in the system");
		do {
			System.out.print("Please enter the customer ID number: ");
			accNum = input.nextLine();
			oldGuy = customerData.findCustomer(accNum);
			
			if (oldGuy != null) {
				System.out.print("Please enter the new name: ");
				name = input.nextLine();
				oldGuy.setName(name);
			} else {
				System.out.println("Error - This account does not exist, please re-enter\n");
			}	
		} while (oldGuy == null);
	}
	
	public static void save2File(CustomerData customerData) {
		Scanner input = new Scanner(System.in);
		String fileName;
		System.out.print("Please enter the name of the file to save: ");
		fileName = input.nextLine();
		writeFile(customerData, fileName);
	}
	
	public static void displayCustomer(CustomerData customerData) {
		System.out.println("Customer List:");
		System.out.println("---------------");
		customerData.displayAll();
	}

	public static void back2Menu(CustomerData customerData) {
		if (CHANGED && !SAVED) {
			Scanner input = new Scanner(System.in);
			String choice;
			System.out.print("WARNING - Data has not been saved. Do you wish to proceed to the main menu (Y/N)?: ");
			choice = input.nextLine();
			
			if(choice.toUpperCase().equals(YES)) {
				CUSTOMER_MENU = false;
			} else if(!choice.toUpperCase().equals(NO)) {
				System.out.println("Error - Invalid entry, please re-enter");
				back2Menu(customerData);
			}
		} else {
			CUSTOMER_MENU = false;
		}
	}
	
	public static void customerPurch(Stack<TV> ids, Queue<Customer> queue, CustomerData customerData) {
		if (!ids.isEmpty()) {
			Scanner input = new Scanner(System.in);
			String name, accNum, address;
			int numTV = 0;
			boolean validNum = false;
			boolean newCustomer = false;
			ArrayList<String> idNums = new ArrayList<String>();
			Customer oldGuy;
			String brand, model;
			boolean bFlag = true;
			TVType info;
			BinaryTree tree = new BinaryTree();
			String delivery_choice;
			
			//TESTING
			readData(tree);
			displayCustomer(customerData);

			do {
				System.out.print("Please enter the customer account number or none: ");
				accNum = input.nextLine();
				oldGuy = customerData.findCustomer(accNum);
				
				if (accNum.toUpperCase().equals(NONE)){ //new customer
					Iterator<Customer> itr = customerData.iterator();
					boolean duplicate = false;
					newCustomer = true;
					CHANGED = true;
					System.out.println("Customer not found\n");
					System.out.print("Please enter the customer name: ");
					name = input.nextLine();
					do { //check for uniqueness
						duplicate = false;
						System.out.print("Please enter a new customer account number for " + name + ": ");
						accNum = input.nextLine();
						
						while(itr.hasNext() && (!duplicate)) {
							if (itr.next().getAccNum().equals(accNum)) {
								duplicate = true;
							}
						}

						if (duplicate) {
							System.out.println("Error - This account already exists, please re-enter\n");
						}
					} while (duplicate);
					
					oldGuy = new Customer(name, accNum);
					customerData.addCustomer(oldGuy);
				} else {
					if (oldGuy != null) { //found old account
						name = oldGuy.getName();
						CHANGED = false;
					} else { //creating new account
						newCustomer = true;
						CHANGED = true;
						System.out.println("Account could not be found, creating a new one:");
						System.out.print("Please enter the customer name: ");
						name = input.nextLine();
						oldGuy = new Customer(name, accNum);
						customerData.addCustomer(oldGuy);
					}		
				}
			} while ((oldGuy == null) && (!newCustomer));
			
			System.out.println("Customer is: " + name + "\n");
			tree.printAll();
			
			do {
				System.out.print("Please enter in the brand: ");
				brand = input.nextLine();
				System.out.print("Please enter in the model: ");
				model = input.nextLine();
				
				info = tree.findTV(brand, model);
				if (info == null) {
					System.out.println("TV not found, please re-enter!\n");
					bFlag = true;
				} else {
					bFlag = false;
				}
			} while (bFlag);
			
			System.out.println("Here is what you selected: ");
			System.out.println(info.toString());
			System.out.println();
			
			do {
				try {
					System.out.print("Please enter the number of TVs purchased: ");
					numTV = input.nextInt();	
					validateNumTV(ids, numTV);
					System.out.println();
				} catch (InputMismatchException e) {
					System.out.println("Error - input must be an integer");
					input.nextLine();
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println(e);
				} finally {
					if ((numTV >= MINBUY) && (numTV <= ids.size())) {
						validNum = true;
					} else {
						validNum = false;
					}
				}
				
			} while (!validNum);
			
			System.out.println("Customer " + name + " purchased the following TVs:");
			for (int iterator = 0; iterator < numTV; iterator++) {
				TV currentTV = ids.pop();
				idNums.add(currentTV.getId());
				System.out.println(idNums.get(iterator));
			}
			
			oldGuy.setNumTV(numTV);
			oldGuy.setIdNums(idNums);
			oldGuy.setInfo(info);
			queue.add(oldGuy);
			
			input = new Scanner(System.in); //buffer
			
			bFlag = true;
			do {
				System.out.print("Do the customer want the TVs delivered (Y or N)?: ");
				delivery_choice = input.nextLine();
				delivery_choice = delivery_choice.toUpperCase();
				
				if (delivery_choice.equals(YES)) {
					System.out.print("Please enter customer address: ");
					address = input.nextLine();
					appendDelFile(name, address, accNum, numTV);
					bFlag = false;
				} else if (!delivery_choice.equals(NO)) {
					System.out.println("Error: Enter either Y or N.\n");
					bFlag = true;
				}
			} while (bFlag);
			
			System.out.println("There are " + ids.size() + " TVs left in stock.\n");
		} else {
			System.out.println("There are no more TVs in stock!");
		}
	}
	
	public static void customerCO(Queue<Customer> queue) {
		if (!queue.isEmpty()) {
			Customer currentCustomer = queue.remove();
			currentCustomer.toString();
			System.out.printf(currentCustomer.toString());
			System.out.println();
			
			for (String id : currentCustomer.getIdNums()) {
				System.out.println("TV ID Purchased is: " + id);
			}
			
			System.out.println("There are " + queue.size() + " customers left.");
		} else {
			System.out.println("There are no customers in line to check out.");
		}
	}
	
	public static void displayAll(Stack<TV> ids) {
		System.out.println("The following " + ids.size() + " TV's are left in inventory: ");
		for (TV tv : ids) {
			System.out.println(tv);
		}
	}
	
	public static CustomerData callSort(CustomerData customerData) {
		CustomerData sortedList = new CustomerData();
		customerList = customerData.convert();
		
		insertionSort(customerList, customerList.length);
		
		for (int index = 0; index < customerList.length; index++) {
			sortedList.addCustomer(customerList[index]);
		}		
		
		return sortedList;
	}
	
	//[1]
	//start from end to beginning
	public static void insertionSort(Customer[] list, int size) {	
		if (size <= BASE_CASE) {
			return;
		}
		
		insertionSort(list, size - 1);
		
		Customer last = list[size - 1];
		int leftIndex = size - 2;
		
		while ((leftIndex >= 0) && (list[leftIndex].getAccNum().compareTo(last.getAccNum()) > 0)) {
			list[leftIndex + 1] = list[leftIndex];
			leftIndex--;
		}
		list[leftIndex + 1] = last;
	}
	
}

/* Index
 * [1] https://www.geeksforgeeks.org/recursive-insertion-sort/
 * */