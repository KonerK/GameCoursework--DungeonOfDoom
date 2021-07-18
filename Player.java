/**
 * Represents the essential fields and behaviours that are inherited by all player type objects - BotPlayer & HumanPlayer
 */

public abstract class Player {
    
	/* The commands available to the player */
	private String [] availableCommands = {"HELLO", "GOLD", "MOVE N", "MOVE E", "MOVE S", "MOVE W", "PICKUP", "LOOK", "QUIT"} ;
	/* The command input by the player */
	private String command;
    /* The row and column positions of the players on the map */
	private int rowPosition;
    private int columnPosition; 
    /* The item (character) the player is standing on */
    private char itemPlayerIsOn;
    
   
	/**
	 * Constructor for a Player,
	 * Every player starts on a '.' square, as such itemPlayerIsOn is set to '.'
	 */
    public Player() {
    	itemPlayerIsOn = '.';
    }
    
	/**
    * @return : the row the player is on in the map 
    */
    public int getPlayerRowPosition() {
     	return rowPosition;
    }
    
	/**
    * @return : the column the player is on in the map 
    */
    public int getPlayerColumnPosition() {
     	return columnPosition;
    }

	/**
    * @return : the commands the player can use
    */
    public String[] getAvaliableCommands() {
     	return availableCommands;
    }
     
	/**
    * @return : the item the player is currently standing on ('G', '.', 'E' etc)
    */
    public char getItemPlayerIsOn() {
        return itemPlayerIsOn; 
    }
     
	/**
    * @return : the command the player wants to execute 
    *           (E.g. what the user enters in the console)
    */          
    public String getCommand() {
    	return command;
    }
     
    /**
    * Sets the command the player wants to execute
    * @param command: The command entered/generated by the player
    */
    public void setCommand(String command) {
    	this.command = command;
    }
     
    /**
    * Sets the item the player is on
    * @param item: The item (character) the player is on
    */
    public void setItemPlayerIsOn(char item) {
     	itemPlayerIsOn = item;
    }

    /**
    * Sets the position of the player
    * @param rowPosition: the row the player should be in
    * @param columnPosition: the column the player should be in
    */
    public void setPlayerPosition(int rowPosition, int columnPosition) {
    	this.rowPosition = rowPosition;
    	this.columnPosition = columnPosition;
    }
     
    /**
    * Dictates how the command is gathered from the player 
    * (such as by input from the console)
    */
    public abstract void generateCommand();
    
    /**
     * Processes the command. 
     * For example, if the user enters 'MOVE N' we return availableCommands[2],the command the user specified.
     * @return : Processed output or Invalid if the command entered/generated is wrong
     */
    public abstract String getNextAction();

}
