package name.brookli.textgame;

import java.util.HashMap;
import java.util.HashSet;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

/**
 * 
 * @author Absreim
 * 
 * Keeps track of the state of the environment.
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
	
	// Item names cannot contain whitespace.
	// There should not be more than one item with the same name on a map.
	
	// collection of items on the map
	// each location with an item is a key in the HashMap
	// each location can have multiple items, which are contained
	// in a HashSet of Strings for that location
	private final HashMap<MapLocation, HashSet<String>> items;
	
	// HashSet of Strings representing items in the player's inventory
	private final HashSet<String> inventory;
	
	/**
	 * Default constructor with hard coded map used as an example.
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
		northSquare.add("north_flag");
		items.put(new MapLocation(2,4), northSquare);
		HashSet<String> southSquare = new HashSet<String>();
		southSquare.add("south_flag");
		southSquare.add("compass");
		items.put(new MapLocation(2,0), southSquare);
		
		inventory = new HashSet<String>();
		inventory.add("transponder");
		
	}
	
	/**
	 * 
	 * Creates a Map instance based on the contents of an XML file.
	 * 
	 * @param xmlFile File object representing XML file of map to load.
	 */
	Map(File xmlFile) throws Exception{
		
		// No way to recover for exceptions loading the parser or file.
		// Exceptions will be thrown up the chain.
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(true);
		dbFactory.setNamespaceAware(true);
		dbFactory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", 
			      "http://www.w3.org/2001/XMLSchema");
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);
		
		Element root = doc.getDocumentElement();
		
		// create locations 2d array with proper dimensions
		// ----------------------------------------------
		int width = Integer.parseInt(root.getAttribute("width"));
		int height = Integer.parseInt(root.getAttribute("height"));
		locations = new boolean[width][];
		for(int i=0;i<width;i++){
			
			locations[i] = new boolean[height];
			
		}
		//------------------------------------------------
		
		// initialize starting location
		//------------------------------------------------
		// first child node might be a text node, in which case the Element
		// we want is the next node
		Node startNode = root.getFirstChild();
		Element startElement;
		if(startNode.getNodeName().equals("#text")){
			
			startElement = (Element) startNode.getNextSibling();
			
		}
		else{
			
			startElement = (Element) startNode;
			
		}
		
		x = Integer.parseInt(startElement.getAttribute("x"));
		y = Integer.parseInt(startElement.getAttribute("y"));
		//------------------------------------------------
		
		// initialize inventory
		//------------------------------------------------
		Node inventoryNode = startElement.getNextSibling();
		Element inventoryElement;
		if(inventoryNode.getNodeName().equals("#text")){
			
			inventoryElement = (Element) inventoryNode.getNextSibling();
			
		}
		else{
			
			inventoryElement = (Element) inventoryNode;
			
		}
		NodeList inventoryItemNodes = inventoryElement.getChildNodes();
		inventory = new HashSet<String>();
		
		for(int i=0;i<inventoryItemNodes.getLength();i++){
			
			Node currentItemNode = inventoryItemNodes.item(i);
			if(!currentItemNode.getNodeName().equals("#text")){
				
				String itemName = ((Element) currentItemNode).getFirstChild().getNodeValue();
				inventory.add(itemName);
				
			}
			
		}
		//------------------------------------------------
		
		// fill in map locations
		//------------------------------------------------
		items = new HashMap<MapLocation, HashSet<String>>();
		Node locationNode = inventoryElement.getNextSibling();
		Element locationElement;
		if(locationNode.getNodeName().equals("#text")){
			
			locationElement = (Element) locationNode.getNextSibling();
			
		}
		else{
			
			locationElement = (Element) locationNode;
			
		}
		while(locationElement != null){
			
			// fill in spot in locations array
			int locationX = Integer.parseInt(locationElement.getAttribute("x"));
			int locationY = Integer.parseInt(locationElement.getAttribute("y"));
			locations[locationX][locationY] = true;
			
			// fill items hash with items at the location
			NodeList locationItemNodes = locationElement.getChildNodes();
			int numItems = locationItemNodes.getLength();
			if(numItems > 0){
				
				HashSet<String> itemSet = new HashSet<String>();
				items.put(new MapLocation(locationX,locationY), itemSet);
				
				for(int i=0;i<numItems;i++){
					
					Node currentItemNode = locationItemNodes.item(i);
					if(!currentItemNode.getNodeName().equals("#text")){
					
						String itemName = ((Element) currentItemNode).getFirstChild().getNodeValue();
						itemSet.add(itemName);
					
					}
					
				}
				
			}
			
			Node nextLocationNode = locationElement.getNextSibling();
			if(nextLocationNode.getNodeName().equals("#text")){
				
				locationElement = (Element) nextLocationNode.getNextSibling();
				
			}
			else{
				
				locationElement = (Element) nextLocationNode;
				
			}
			
		}
		//------------------------------------------------
		
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
				if(result = y + 1 < locations[x].length && locations[x][y+1]){
					y++;
				}
				break;
			case SOUTH:
				if(result = y > 0 && locations[x][y-1]){
					y--;					
				}
				break;
			case EAST:
				if(result = x + 1 < locations.length && locations[x+1][y]){
					x++;
				}
				break;
			case WEST:
				if(result = x > 0 && locations[x-1][y]){
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

			return inventory.remove(item) && items.get(current).add(item);
			
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
	HashSet<String> getInventory(){
		
		return inventory;
		
	}
	
	/**
	 * 
	 * @return Iterator of Strings representing items. Null if no
	 * items on the ground.
	 * 
	 * Called by TextGame class to get a list of items that are on the ground.
	 */
	HashSet<String> getGroundItems(){
		
		MapLocation current = getLocationCache();
		if(items.containsKey(current)){
			
			return items.get(current);
			
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
