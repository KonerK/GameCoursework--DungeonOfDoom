import java.util.Random;
import java.util.Scanner;

/**
 * Contains the main logic of the game
 */
public class GameLogic {
	 
	private Map map;
	private HumanPlayer humanPlayer;
	private BotPlayer botPlayer;
	private boolean gameRunning;
	private boolean isHumanTurn;
	
	/**
	* Constructor for GameLogic
	* @param : the file path to the map
	*/
	public GameLogic(String mapFilePath) {
		map = new Map(mapFilePath);
		humanPlayer = new HumanPlayer();
		botPlayer = new BotPlayer();
		gameRunning = true;
		isHumanTurn = true;
	}
	
	
	
    /**
	* Checks if the game is running
    * @return : if the game is running.
    */
    public boolean gameRunning() {
        return gameRunning;
    }
    
    /**
	* Returns the map being used in the game
    * @return : the map field of GameLogic
    */
    public Map getMap() {
    	return map;
    }
   
    /**
	* Returns whose turn it is (the player or the bot)
    * @return : true if it is the human's turn, false for the bot
    */
    public boolean getPlayerTurn() {
    	return isHumanTurn;
    }
    
    /**
	* Changes whoever players turn it is to the other player.
    */
    public void togglePlayerTurn() {
    	isHumanTurn = !isHumanTurn;
    }
    
    /**
	* Returns the bot player
    * @return : the boy player in the game
    */
    public BotPlayer getBotPlayer() {
    	return botPlayer;
    }
    
    /**
	* Returns the player based on whose turn it is
    * @return : humanPlayer if it is the user's turn, botPlayer otherwise.
    */
    public Player getPlayer() {
    	if (isHumanTurn) {
    		return humanPlayer;
    	}else {
    		return botPlayer;
    	}
    }
    

    /**
	* Adds a player to a random location on the map
    * @param playerType : the player we are adding to the map, true for HumanPlayer type, false for BotPlayer type
    */
    public void addPlayerToMap(boolean playerType) {
    	
    	Random randomRow = new Random();
    	Random randomColumn = new Random();
    	int rowPos = 0;
    	int columnPos = 0; 
    	char [][] mapRepresentation = map.getMap();
    	
    	/* Generate a random row and column number until we find a position that is not a wall, does not have gold on it, or has
    	   not already got a player standing on it (for when we add the bot player) */
    	do {
    		rowPos = randomRow.nextInt(map.getRowCount());
    		columnPos = randomColumn.nextInt(map.getColumnCount());
    	}while(mapRepresentation[rowPos][columnPos] == '#' || mapRepresentation[rowPos][columnPos] == 'G'|| mapRepresentation[rowPos][columnPos] == 'P');
    	
    	/* Write the players position on the map */
    	map.writePlayerPositionOnMap(playerType, rowPos, columnPos);

    	/* Record the players position on the map, so we can move them when they use a MOVE command */
    	if (playerType) {
    		humanPlayer.setPlayerPosition(rowPos, columnPos);
    	}else {
    		botPlayer.setPlayerPosition(rowPos, columnPos);
    	}
    }
    
    /**
	* Performs the action specified by the player
    * @param command : the command specified by the player to be executed.
    * @return : A string depending on the command specified For example, if 
    * 		    command is "HELLO", then we will return a string revealing 
    * 		    the amount of gold needed to win the game.
    */
    public String performAction(String command) {
    	
    	/* Compare the processed commands specified by the player with the available commands,
    	   in order to decide what method to execute */ 
    	String [] availableCommands = humanPlayer.getAvaliableCommands();
    	if (command.equals("Invalid")) {
    		return "Invalid Command"; 
    	} else if (command.equals(availableCommands[0])){
    		/* If the processed command is "HELLO" then call hello() */
    		return hello();
    	} else if (command.equals(availableCommands[1])) {
    		return gold();
    	} else if (command.equals(availableCommands[2]) || command.equals(availableCommands[3]) || command.equals(availableCommands[4]) || command.equals(availableCommands[5])) {
    		char direction = command.charAt(command.length()-1);
    		if (isHumanTurn) {
    			return move(direction, humanPlayer);
    		}else {
    			return move(direction, botPlayer);
    		}
    	} else if (command.equals(availableCommands[6])) {
    		return pickup();
    	}else if (command.equals(availableCommands[7])) {
    		if (isHumanTurn) {
    			return look(humanPlayer);
    		}else {
    			return look(botPlayer);
    		}
    	}else{
    		return quitGame();
    	}
    }
 
    /**
   	* Returns the gold required to win
    * @return : Gold required to win
    */
    public String hello() {
    	String goldToWinMessage = "Gold To win: " + map.getGoldRequired();
        return goldToWinMessage;
    }
	

	/**
	 * Returns the gold currently owned by the player
     * @return : Gold currently owned
     */
    public String gold() {
    	String goldCurrentlyOwnedMessage = "Gold owned: " + humanPlayer.getGoldOwned();
        return goldCurrentlyOwnedMessage;
    }

    /**
     * Checks if movement is legal and updates player's location on the map
     * @param direction : The direction of the movement
     * @param player : The player we are moving
     * @return : If movement was a success or not
     */
    public String move(char direction, Player player) {
    	
    	/* Get player's current position, then change this accordingly depending on the direction */
    	int rowToMoveTo = player.getPlayerRowPosition();
    	int columnToMoveTo = player.getPlayerColumnPosition();
    	
        if (direction == 'N') {
        	rowToMoveTo -= 1;
        }else if (direction == 'S') {
        	rowToMoveTo += 1;
        }else if (direction == 'E') {
            columnToMoveTo += 1;
        }else if (direction == 'W') {
            columnToMoveTo -= 1;
        }
        
        if (map.getMap()[rowToMoveTo][columnToMoveTo] == '#') {
        	return "Fail";
        }else {
        	/*If the player is not moving into a wall: 
        	   Replace the 'P'/'B' with the item the player should be standing on, as the player is about to move */ 
        	map.setItemPosition(player.getPlayerRowPosition(), player.getPlayerColumnPosition(), player.getItemPlayerIsOn());
        	/* Set the new item the player will be standing on when they move */
        	player.setItemPlayerIsOn(map.getMap()[rowToMoveTo][columnToMoveTo]);
        	/* Write the player's position */
        	map.writePlayerPositionOnMap(isHumanTurn, rowToMoveTo, columnToMoveTo);
        	player.setPlayerPosition(rowToMoveTo, columnToMoveTo);
        	return "Success";
        }
    }
    
    
    /**
    * Converts the map from a 2D char array to a single string.
    * @param player : The player who we are performing the look command on 
    * @return : A String representation of the game map.
    */
    public String look(Player player) {
        String playerSurroundings = "";
        int rowNum =  player.getPlayerRowPosition()-2; 
    	int columnNum = player.getPlayerColumnPosition()-2;
    	int currentRowNum = rowNum;
    	int initialColumnNum = columnNum;

    	/* Convert the map to a 5x5 grid as String with the player at the centre */ 
    	for (int i = 0; i < 25; i++) {
        	if (i%5 == 0 && i != 0) {
        		playerSurroundings = playerSurroundings + "\n";
        		columnNum = initialColumnNum;
        		currentRowNum++;
        		rowNum = currentRowNum;
        	}
        	try {
        		playerSurroundings = playerSurroundings + map.getMap()[rowNum][columnNum];
        	}catch (ArrayIndexOutOfBoundsException e) {
        		/* If we are trying to show an area outside of the map, we represent this as
        		   a '#' (a wall)  */ 
        		playerSurroundings = playerSurroundings + "#";
        	}
        	columnNum++;
        }
    	
    	return playerSurroundings;
    }

    
    /**
    * Processes the player's pickup command, updating the map and the player's gold amount.
    * @return If the player successfully picked-up gold or not.
    */
    public String pickup() {
    	/* If the player is standing on a gold piece, incremend their gold score and replace the gold piece with a '.' */
    	if (humanPlayer.getItemPlayerIsOn() == 'G') {
    		humanPlayer.setItemPlayerIsOn('.');
    		humanPlayer.incrementGoldOwned();
    		return "Sucess. Gold Owned: " + humanPlayer.getGoldOwned();
    	}else {
    		return "Fail";
    	}    
    }
    

    /**
    * Quits the game, shutting down the application.
    * @return : A message saying whether the player has won or not
    */
    public String quitGame() {
    	String winningMessage = "WIN - You Escape the Dungeon Feeling Rich";
    	String losingMessage = "LOSE";
    	/* If the player is on an exit tile, and has the amount of gold required to win, then print "WIN..." */
    	if (humanPlayer.getItemPlayerIsOn() == 'E' && humanPlayer.getGoldOwned() == map.getGoldRequired()) {
    		gameRunning = false;
    		return winningMessage; 
    	}else {
			gameRunning = false;
			return losingMessage; 
    	}
    } 
    
    /**
    * Checks whether the bot is standing on the same tile as the human and vice versa. 
    * @return : true if bot and player are standing on each other, false otherwise.
    */
    public boolean isGameLost(){
    	if (humanPlayer.getItemPlayerIsOn() == 'B' || botPlayer.getItemPlayerIsOn() == 'P') {
    		gameRunning = false;
    		return true;
    	}else {
    		return false;
    	}
    }
    

	public static void main(String[] args){
    	
		/* Welcome message to the user */
		System.out.println("Enter the Name of a Map below to play:");					
		/* Gather the user input and use this to create a gamelogic object */
    	Scanner scanner = new Scanner(System.in);
    	String mapName = scanner.nextLine().trim();
    	GameLogic logic = new GameLogic(mapName + ".txt");
    	/* Add both players to the map and print beginning message to the user */
    	logic.addPlayerToMap(true);
		logic.addPlayerToMap(false);
		System.out.println("You find yourself in the " + logic.getMap().getMapName()
							+ ". Find enough gold to exit the map and win. Do not let the "
							+ "bot catch you!");
	
		
		while (logic.gameRunning()) {
			/* get a command from the player (bot or human) */
			logic.getPlayer().generateCommand();
			
			/* If it's the players turn, we ouput the result of their command to the console */
			if (logic.getPlayerTurn()) {
				System.out.println(logic.performAction(logic.getPlayer().getNextAction()));
			}else {
				/* If it's the bots turn and they choose the look command we need to use
				   setMapAsLastSeen() to make sure they this information is stored 
				   by the bot and can be used to find the best direction to move in. */
				if (logic.getPlayer().getCommand() == logic.getPlayer().getAvaliableCommands()[7]) {
					logic.getBotPlayer().setMapAsLastSeen(logic.performAction(logic.getPlayer().getNextAction()));
				}else {
					/* If the bot is not using the look command, we can simply perform the action it specifies */
					logic.performAction(logic.getPlayer().getNextAction());
				}
			}
			
			/* Check whether the bot and player are standing on the same square, if they are the game is lost. */
			if(logic.isGameLost()) {
				System.out.println("The Bot Caught You! You Lose!");
			}

			logic.togglePlayerTurn();
		}
		
		scanner.close();
    }
}