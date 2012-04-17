package bli.example.textgame;

import java.util.Scanner;

/**
 * 
 * @author Absreim
 *
 * Controller class of TextGame in Model-View-Controller architecture.
 * Receives command strings from TextGame class, interprets
 * the strings as known commands, passes the commands onto the Map,
 * and returns output describing the result of the command back to TextGame. 
 */
class Interpreter {
	
	private final Map map;
	
	Interpreter(Map map){
		
		this.map = map;
		
	}
	
	String interpretCmd(Scanner cmd){
		
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
				
				boolean result = map.move(dir);
				return result ? "Move successful." : "Invalid move.";
				
			}
			else{
				
				if(cmd.hasNext()){
					
					return "Invalid direction specified with move commmand.";
					
				}
				else{
					
					return "No direction specified with move command."; 
					
				}
				
			}
			
		}
		else if(cmd.hasNext("take")){
			
			cmd.next();
			if(cmd.hasNext()){
				
				boolean result = map.take(cmd.next());
				return result ? "Item taken." : "No such item.";
				
			}
			else{
				
				return "No item specified with take command.";
				
			}
			
		}
		else if(cmd.hasNext("put")){
			
			cmd.next();
			if(cmd.hasNext()){
				
				boolean result = map.put(cmd.next());
				return result ? "Item dropped." : "No such item.";
				
			}
			else{
				
				return "No item specified with put command.";
			
			}
			
		}
		else if(cmd.hasNext("look")){
			
			TextGame.look();
			return "Looking at player's current status...";
			
		}
		else if(cmd.hasNext("quit|exit")){
			
			TextGame.quit();
			return "Exiting...";
			
		}
		else if(cmd.hasNext()){
			
			return "Unrecognized command.";
			
		}
		else{
			
			return "No command entered.";
			
		}
		
	}

}
