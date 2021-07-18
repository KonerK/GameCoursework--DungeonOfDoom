import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Represents the fields and behaviours needed by the human player, 
 * containing the code needed to read inputs from the console
 */

public class HumanPlayer extends Player {
	
	 /* The amount of gold owned by the player */
     private int goldOwned; 
     /* Buffered reader used to read text entered by the user from the console */
     private BufferedReader userInput;
     
 	/**
 	 * Constructor for HumanPlayer
 	 * goldOwned is initially set to zero
 	 */
     public HumanPlayer() {
    	 super();
    	 goldOwned = 0;
    	 userInput = new BufferedReader(new InputStreamReader(System.in));
     }
     
   	/**
   	 * Increments goldOwned by one
   	 */
     public void incrementGoldOwned() {
     	goldOwned++;
     }
     
 	/**
      * @return : the amount of gold the player owns
      */
     public int getGoldOwned() {
       	return goldOwned;
       }
    

  	/**
  	 * Reads in the user's input from the console
  	 */
     @Override
     public void generateCommand() {
     	try {
 			this.setCommand(userInput.readLine());
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
    }
     
     /**
   	 * Uses the user input to return the command the user wanted to execute from the availableCommands field
   	 */
     @Override
     public String getNextAction() {
    	
    	String strippedCommand = this.getCommand().toUpperCase().trim();
    	String [] availableCommands = this.getAvaliableCommands();
    	
    	/* Loop through availableCommands and see if what the user entered matches any of its elements.
    	   If it does, then return this element as it is the command the user wants to execute */
    	for (int i = 0; i < availableCommands.length; i++) {
    		if (availableCommands[i].equals(strippedCommand)){
    			return availableCommands[i];
    		}
    	}
    	/* If there is no match between what the user entered and the elements availableCommands, the user
    	   entered an invalid command, so return "Invalid" */
    	return "Invalid";  
     } 
    
}