package bli.example.textgame;

import java.util.HashSet;
import java.util.Scanner;

/**
 * 
 * @author Absreim
 * 
 * Main class of TextGame. Also serves as View in Model-View-Controller architecture.
 * Contains main loop of execution and creates Scanner that receives user input. The
 * Scanner is used by Interpreter to interpret commands. Receives results of commands
 * from Interpreter in the form of Strings. Also contains commands called by Interpreter
 * for exiting the program and for viewing the current state of the player. TextGame
 * queries Map for state information.
 *
 */
public class TextGame {

	private static boolean quitting = false;
	private static Map map;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("Welcome to BLI Text Game version 1.0");
		map = new Map();
		Interpreter interpreter = new Interpreter(map);
		while(!quitting){
			System.out.println(interpreter.interpretCmd(new Scanner(System.in)));
		}
		System.out.println("Thank you for playing BLI Text Game version 1.0.");
		
	}
	
	/**
	 * Called by Interpreter to exit the program.
	 */
	public static void quit(){
		
		quitting = true;
		
	}
	
	/**
	 * Displays the items on the floor, the player's position, and inventory items.
	 */
	public static void look(){
		
		Map.MapLocation location = map.getLocation();
		System.out.println("Current location is: (" + location.getX() + ", " + location.getY() + ")");
		
		HashSet<String> inventory = map.getInventory();
		if(inventory.size() > 0){
			
			System.out.print("Inventory contains: ");
			for(String item : inventory){
				
				System.out.print(item + " ");
				
			}
			System.out.println();
			
		}
		else{
			
			System.out.println("Inventory is empty.");
			
		}
		
		HashSet<String> floorItems = map.getGroundItems();
		if(floorItems.size() > 0){
			
			System.out.print("Floor contains: ");
			for(String item : floorItems){
				
				System.out.print(item + " ");
				
			}
			System.out.println();
			
		}
		else{
			
			System.out.println("There are no items on the floor.");
			
		}
		
	}

}
