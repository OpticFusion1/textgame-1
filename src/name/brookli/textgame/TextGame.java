package name.brookli.textgame;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;

/**
 * 
 * @author Absreim
 * 
 * Main class of TextGame.
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
		if(args.length == 1){
			try{
				map = new Map(new File(args[0]));
			}
			catch(Exception e){
				System.err.println("Error generating Map from XML file. Exiting.");
				System.exit(1);
			}
		}
		else{
			map = new Map();
		}
		Scanner s = new Scanner(System.in);
		while(!quitting){
			interpretCmd(s);
		}
		s.close();
		System.out.println("Thank you for playing BLI Text Game version 1.0.");
	}
	
	private static void interpretCmd(Scanner cmd){
		if(cmd.hasNext("move")){
			cmd.next();
			Map.Direction dir = null;
			if(cmd.hasNext("north")){
				dir = Map.Direction.NORTH;
			}
			else if(cmd.hasNext("south")){
				dir = Map.Direction.SOUTH;
			}
			else if(cmd.hasNext("east")){
				dir = Map.Direction.EAST;
			}
			else if(cmd.hasNext("west")){
				dir = Map.Direction.WEST;
			}
			if(dir != null){
				if(map.move(dir)) {
					System.out.println("Move successful.");
				}
				else {
					System.out.println("Invalid move.");
				}
			}
			else{
				if(cmd.hasNext()){
					System.out.println("Invalid direction specified with move commmand.");
				}
				else{
					System.out.println("No direction specified with move command."); 
				}
			}
		}
		else if(cmd.hasNext("take")){
			cmd.next();
			if(cmd.hasNext()){
				if(map.take(cmd.next())){
					System.out.println("Item taken.");
				}
				else {
					System.out.println("No such item.");
				}
			}
			else{
				System.out.println("No item specified with take command.");
			}
		}
		else if(cmd.hasNext("put")){
			cmd.next();
			if(cmd.hasNext()){
				if(map.put(cmd.next())){
					System.out.println("Item dropped.");
				}
				else {
					System.out.println("No such item.");
				}
			}
			else{
				System.out.println("No item specified with put command.");
			}
		}
		else if(cmd.hasNext("look")){
			cmd.next();
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
			if(floorItems != null && floorItems.size() > 0){
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
		else if(cmd.hasNext("quit|exit")){
			quitting = true;
		}
		else if(cmd.hasNext()){
			cmd.next();
			System.out.println("Unrecognized command.");
		}
	}
}