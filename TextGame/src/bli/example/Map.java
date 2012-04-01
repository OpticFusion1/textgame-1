package bli.example;

import java.util.HashMap;
import java.util.HashSet;

public class Map {

	// x and y store location of player
	// x is first index of array (locations[x][y])
	private int x;
	private int y;
	
	// locations on the map
	// true means location exists, false means it does not
	private final boolean[][] locations;
	
	// collection of items on the map
	// each location with an item is a key in the HashMap
	// each location can have multiple items, which are contained
	// in a HashSet of Strings for that location
	private final HashMap<MapLocation, HashSet<String>> items;
	
	/**
	 * Default constructor with hardcoded map used as an example.
	 */
	public Map() {
		
		x = 2;
		y = 2;
		
		boolean[][] locations = {{false, false, true, false, false}, 
				{false, false, true, false, false}, 
				{true, true, true, true, true},
				{false, false, true, false, false},
				{false, false, true, false, false}};
		this.locations = locations;
		
		items = new HashMap<MapLocation, HashSet<String>>();
		HashSet<String> northSquare = new HashSet<String>();
		northSquare.add("north flag");
		items.put(new MapLocation(0,2), northSquare);
		HashSet<String> southSquare = new HashSet<String>();
		southSquare.add("south flag");
		southSquare.add("compass");
		items.put(new MapLocation(4,2), southSquare);
		
	}
	
	/**
	 * 
	 * Container for coordinate pair of location on the map
	 *
	 */
	private class MapLocation{
		
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

}
