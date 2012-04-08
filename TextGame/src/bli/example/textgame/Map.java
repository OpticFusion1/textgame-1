package bli.example.textgame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * 
 * @author Absreim
 * 
 * Model class of TextGame in Model-View-Controller architecture. Keeps track of
 * state of the environment, processes actions received from the Interpreter (Controller) class,
 * and notifies the Interpreter (Controller) class about the result of an action. 
 * The TextGame (View) class queries Map for information on the state of the environment.
 *
 */
class Map {

	// x and y store location of player
	// x is first index of array (locations[x][y])
	// North is positive y direction
	// East is positive x direction
	private int x;
	private int y;
	
	// object containing current location in a MapLocation object
	// used to improve performance
	private MapLocation locationCache;
	
	// locations on the map
	// first index is x, second is y
	// true means location exists, false means it does not
	private final boolean[][] locations;
	
	// collection of items on the map
	// each location with an item is a key in the HashMap
	// each location can have multiple items, which are contained
	// in a HashSet of Strings for that location
	private final HashMap<MapLocation, HashSet<String>> items;
	
	// HashSet of Strings representing items in the player's inventory
	private final HashSet<String> inventory;
	
	/**
	 * Default constructor with hardcoded map used as an example.
	 */
	Map() {
		
		x = 2;
		y = 2;
		
		boolean[][] locations = {{false, false, true, false, false}, 
				{false, false, true, false, false}, 
				{true, true, true, true, true},
				{false, false, true, false, false},
				{false, false, true, false, false}};
		this.locations = locations;
		
		locationCache = null;
		
		items = new HashMap<MapLocation, HashSet<String>>();
		HashSet<String> northSquare = new HashSet<String>();
		northSquare.add("north flag");
		items.put(new MapLocation(2,4), northSquare);
		HashSet<String> southSquare = new HashSet<String>();
		southSquare.add("south flag");
		southSquare.add("compass");
		items.put(new MapLocation(2,0), southSquare);
		
		inventory = new HashSet<String>();
		inventory.add("transponder");
		
	}
	
	/**
	 * 
	 * @return MapLocation of current location
	 * 
	 * Creates new MapLocation object for locationCache if needed.
	 * Otherwise returns locationCache.
	 */
	private MapLocation getLocationCache(){
		
		if(locationCache == null){
			
			return new MapLocation(x,y);
			
		}
		else{
			
			return locationCache;
			
		}
		
	}
	
	/**
	 * 
	 * @param d Direction of move
	 * @return boolean value whether move was successful
	 * 
	 * Called by Interpreter class to attempt to move in a 
	 * Direction.
	 * 
	 */
	boolean move(Direction d){
		
		boolean result = false;
		switch(d){
		
			case NORTH:
				if(result = locations[x][y+1]){
					y++;
				}
				break;
			case SOUTH:
				if(result = locations[x][y-1]){
					y--;					
				}
				break;
			case EAST:
				if(result = locations[x+1][y]){
					x++;
				}
				break;
			case WEST:
				if(result = locations[x-1][y]){
					x--;
				}
				break;
				
		}
		return result;
		
	}
	
	/**
	 * 
	 * @param item - name of the item to try to put down
	 * @return boolean value describing whether action was successful
	 * 
	 * Called by Interpreter class to attempt to put item on the ground.
	 * 
	 * Any attempt to put down an item at a location where an item of the 
	 * same name already exists will be unsuccessful.
	 * 
	 */
	boolean put(String item){
		
		if(inventory.contains(item)){
			
			MapLocation current = getLocationCache();
			if(!items.containsKey(current)){
				
				items.put(current, new HashSet<String>());
				
			}

			return items.get(current).add(item);
			
		}
		else{
			
			return false;
			
		}
		
	}
	
	/**
	 * 
	 * @param item - name of item to try to pick up
	 * @return boolean value describing whether action was successful
	 * 
	 * Called by Interpreter class to attempt to pick up item off the ground.
	 * 
	 */
	boolean take(String item){
		
		MapLocation current = getLocationCache();
		if(items.containsKey(current)){
			
			HashSet<String> floor = items.get(current);
			if(floor.contains(item) && !inventory.contains(item)){
				
				return floor.remove(item) && inventory.add(item);
				
			}
			else{
				
				return false;
				
			}
			
		}
		else{
			
			return false;
			
		}
		
	}
	
	/**
	 * 
	 * @return coordinates of player
	 * 
	 * Called by TextGame class to get current location of player.
	 */
	MapLocation getLocation(){
		
		return getLocationCache();
		
	}
	
	/**
	 * 
	 * @return Iterator of Strings representing items
	 * 
	 * Called by TextGame class to get the player's inventory.
	 */
	Iterator<String> getInventory(){
		
		return inventory.iterator();
		
	}
	
	/**
	 * 
	 * @return Iterator of Strings representing items. Null if no
	 * items on the ground.
	 * 
	 * Called by TextGame class to get a list of items that are on the ground.
	 */
	Iterator<String> getGroundItems(){
		
		MapLocation current = getLocationCache();
		if(items.containsKey(current)){
			
			return items.get(current).iterator();
			
		}
		else{
			
			return null;
			
		}
		
	}
	
	/**
	 * 
	 * Container for coordinate pair of location on the map
	 *
	 */
	class MapLocation{
		
		private final int x;
		private final int y;
		
		public MapLocation(int x, int y){
			
			this.x = x;
			this.y = y;
		}
		
		public int getX(){
			
			return x;
			
		}
		
		public int getY(){
			
			return y;
			
		}
		
		@Override
		public int hashCode(){
		
			int result = 17;
			result = 31 * result + x;
			result = 31 * result + y;
			return result;
			
		}
		
		@Override
		public boolean equals(Object o){
			
			if(o == null){
				
				return false;
				
			}
			else if(!(o instanceof MapLocation)){
				
				return false;
				
			}
			else{
				
				MapLocation mapO = (MapLocation) o;
				return (this.x == mapO.x) && (this.y == mapO.y);
				
			}
			
		}
		
	}
	
	enum Direction {
		
		NORTH, SOUTH, EAST, WEST;
		
	}

}
