import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*; 
/* In this activity, since we are dealing with 1 unique key (the ID number),
 * it is more feasible with a map, as accessing the specific TV object with
 * the given ID number is much simpler than with a set. Removing the object
 * from the map and locating it is efficient and capable with just the class
 * methods.
 * */

public class TVReturn {
	private TreeMap<String, TV> myMap = new TreeMap<String, TV>();
	private static String file_path = null;
	
	TVReturn() {
		boolean flag = true;
		Scanner input = new Scanner(System.in);
		File file = null;
		
		if (file_path == null) {
			while (flag) {
				try {
					System.out.print("Please enter in the previous TV Sales file path: ");
					file_path = input.nextLine();

					if (!file_path.endsWith(".txt")) {
						throw new IllegalArgumentException("The file must be a .txt file, please re-enter");
					} else if (!file_path.startsWith("C:") && !file_path.startsWith("c:")) {
						throw new IllegalArgumentException("The file must be in the local directory \'C:\', please re-enter");
					}

					file = new File(file_path);

					if (!file.exists()) {
						throw new IllegalArgumentException("The file could not be found, please re-enter");
					}
					
					flag = false;
				} catch (IllegalArgumentException e) {
					System.out.println(e);
				}		
			}
		} else {
			file = new File(file_path);
		}
		
		try (Scanner fRead = new Scanner(file)) {
			String idNum = null;
			String brand = null;
			String model = null;
			double price;
			
			while (fRead.hasNext()) {
				if (idNum == null) {
					idNum = fRead.nextLine();
				} else if (brand == null) {
					brand = fRead.nextLine();
				} else if (model == null) {
					model = fRead.nextLine();
				} else {
					price = Double.parseDouble(fRead.nextLine());
					
					TVType info = new TVType(brand, model, price);
					TV tv = new TV(idNum, info);
					myMap.put(idNum, tv);
					
					idNum = null;
					brand = null;
					model = null;
				}
			}
			fRead.close();
		} catch (IOException | NoSuchElementException | IllegalStateException e) {
			System.out.println("The previous TV Sales file could not open.");
		}
	}
	
	public TV contains (String idNum) {
		TV returnTV = null;
		if (myMap.containsKey(idNum)) {
			ArrayList<String> lines = new ArrayList<String>();
			returnTV = myMap.get(idNum);
			
			System.out.println("Found item:");
			System.out.println(myMap.get(idNum).toString());
			
			try (Scanner fRead = new Scanner(new File(file_path))) {
				int index;
				
				while (fRead.hasNext()) {
					lines.add(fRead.nextLine());
				}
				fRead.close();
				
				index = lines.indexOf(idNum);
				for (int i = 0; i < 4; i++) {
					lines.remove(index);
				}
				
				myMap.remove(idNum);
				
				FileWriter writer = new FileWriter(file_path);
				for (String str : lines) {
					writer.write(str + "\n");
				}
				writer.close();
			} catch (IOException |NoSuchElementException | IllegalStateException e) {
				System.out.println("Error occurred while trying to overwrite file.");
			}		
		} else {
			System.out.println("Item not found in the TV sold list. Please re-enter!");
		}
		return returnTV;
	}
}
