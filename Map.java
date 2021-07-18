import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Reads in the map from the file, and stores important information about the map.
 */
public class Map {

	/* Representation of the map */
	private char[][] map;
	/* Map name */
	private String mapName;  
	/* Gold required for the human player to win */
	private int goldRequired; 
	/* Amount of rows and columns the 2D map array has */
	private int rowCount = 0;
	private int columnCount = 0;
	
	/**
	 * Constructor that accepts a map to read in from.
	 * @param fileName : the file path of the map file.
	 */
	public Map(String fileName){
		readMap(fileName);
	}
	
	/**
    * @return : Gold required to exit the current map
    */
    public int getGoldRequired() {
        return goldRequired;
    }

	/**
    * @return : The map stored as a 2D array.
    */
    public char[][] getMap() {
        return map;
    }

	/**
     * @return : The name of the current map.
     */
    public String getMapName() {
        return mapName;
    }
    
	/**
    * @return : The number of rows in the map.
    */
    public int getRowCount() {
    	return rowCount;
    }
    
	/**
    * @return : The number of columns in the map.
    */
    public int getColumnCount() {
    	return columnCount;
    }
    
    
    /**
     * Writes the player position on the map.
     * @param isHumanPlayer : Decides if we are representing the human or bot player on the map.  
     * @param rowPos, columnPos : The row/column number we want to set the player to on the map.
     */
    public void writePlayerPositionOnMap(boolean isHumanPlayer, int rowPos, int columnPos) {
    	if (isHumanPlayer) {
    		map[rowPos][columnPos] = 'P';
    	}else {
    		map[rowPos][columnPos] = 'B';
    	}
    	
    }
    
    /**
     * Sets the position of an item on the map
     * @param rowPos, columnPos : The row/column number we want to set the item to on the map.
     * @param item : The item we want to put on the map.
     */
    public void setItemPosition(int rowPos, int columnPos, char item) {
    	map[rowPos][columnPos] = item;
    }
    
    
    /**
     * Reads in the map from a file.
     * @param fileNames : Name of the map's file.
     */
    public void readMap(String fileName) {
    	
    	/*
    	 * Used in converting the map from a string to a 2D array.
    	 */
    	String mapRepresentation = "";
    	String nextLine = "";
    
    	boolean mapFoundSuccessfully;
    	boolean sensibleGoldRequired;
    	boolean mapFormattedCorrectly;
    	boolean usingDefaultMap = false;
    	int attemptCounter = 0;
    	String filePath = fileName;
    	BufferedReader fileReader = null;
    	BufferedReader inputReader = null;
    	
    	
    	do {
        	/*
        	 * Three booleans below are used in checking whether the map
        	 * has been formatted properly. If these are all true by the end of the loop 
        	 * the map the user input was read successfully and we can move on. 
        	 */
    		mapFoundSuccessfully = true;
        	sensibleGoldRequired = true;
        	mapFormattedCorrectly = true;
        	attemptCounter++;
        	if (attemptCounter == 4) {
        		/* After the third attempt to input a map, we use the default map, from setDefaultMap() and break
        		   out of the loop */
        		usingDefaultMap = true;
        		break;
        	}
    		try {
        		fileReader = new BufferedReader(new FileReader(filePath));
        		
        		String firstLine = fileReader.readLine();
        		String secondLine = fileReader.readLine();
        			
        		/* The first two lines of the map file should specify the name of the map on one line, 
        		  and the amount of gold needed to win on the other. If this is not the case 
        		  mapFormattedCorrectly is set to false*/
        		try {
        			goldRequired = Integer.parseInt(secondLine.replace("win", "").trim());
        			mapName = firstLine.replace("name", "").trim();
        		}catch (NumberFormatException e) {
        			try {
            			goldRequired = Integer.parseInt(firstLine.replace("win", "").trim());
                		mapName = secondLine.replace("name", "").trim();
        			}catch (NumberFormatException x) {
        				mapFormattedCorrectly = false;
        			}
        		}	
        			
        		/* If the gold required to win the game is lower than one,
        		   this is not considered a sensible value and sensibleGoldRequired is set to false*/
        	    if (goldRequired < 1) {
        	    	sensibleGoldRequired = false;
        	    }
        			
        	    /* If there are no formatting issues with the map and we successfully read the map,
        	       then we convert the map to be one long string. We also count how many rows the map has 
        	     	(to use when converting the string to a 2d array.*/
                if (mapFoundSuccessfully && sensibleGoldRequired && mapFormattedCorrectly) {
                	mapRepresentation = "";
            		while((nextLine = fileReader.readLine()) != null){
            			mapRepresentation += nextLine + "\n";
            			rowCount++;
            		}	
                }               
        
        	}catch (FileNotFoundException e) {
        		mapFoundSuccessfully = false;	
        	}catch (IOException e) {
        			e.printStackTrace();
        	}
    		
        	/* If any issues were found when dealing with the map, we raise this issue to the user,
        	   and let them enter the map again (we do this at most three times) */
        	if (!mapFoundSuccessfully || !sensibleGoldRequired || !mapFormattedCorrectly) {
        		if (!mapFoundSuccessfully) {
        			System.out.println("The map could not be found.");
        		}else if (!mapFormattedCorrectly) {
        			System.out.println("Incorrect Map Format.");
        		}else {
        			System.out.println("Unsuitable Gold required to Win.");
        		}
        		
        		inputReader = new BufferedReader(new InputStreamReader(System.in));
        		try {
        			if (attemptCounter != 3) {
        				filePath = inputReader.readLine() + ".txt";
        			}
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	
    	}while((!mapFoundSuccessfully || !sensibleGoldRequired || !mapFormattedCorrectly));
    	
    	try {
    		if (fileReader != null) {
    			fileReader.close();
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	/* Call setDefaultMap if the map the user entered could not be read. */
    	if (usingDefaultMap) {
    		System.out.println("Number of Attempts to Load Map Has Been Passed\n"
								+ "You Will Be Placed in a Default Map.");
    		setDefaultMap();
    		
    	}else {
    		/* If the user's map was successfully read as one long String, then we can convert this
    		   string, representing the map, to a 2D char array. */
        	columnCount = mapRepresentation.substring(0, mapRepresentation.indexOf("\n")).length();
        	map = new char[rowCount][columnCount];
        	int characterCounter = 0;
        	String mapAsOneLine = mapRepresentation.replaceAll("\\s+","");
        	for (int i = 0; i < rowCount; i++) {
        		for (int j = 0; j < columnCount; j++) {
        			map[i][j] = mapAsOneLine.charAt(characterCounter);
        			characterCounter++;
        	    }
    	     }	
    	}

    }
    
    /**
     * Sets default attributes of map
     * after the user has tried three times to input a map name
     */
    public void setDefaultMap() {
		mapName = "Very Small Labyrinth of Doom";
		goldRequired = 2;
		map = new char[][]{
		{'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','.','.','.','.','.','.','G','.','.','.','.','.','.','.','.','.','E','.','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','.','.','E','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','G','.','.','.','.','.','.','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'}
		};
		rowCount = 9;
		columnCount = 20;
    }
    
    
    
}
