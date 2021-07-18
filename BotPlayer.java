import java.util.ArrayList;
import java.util.Random;
/**
 * Represents the fields and behaviours needed by the bot player
 * containing the code needed to generate an informed command 
 */

public class BotPlayer extends Player {
	
	/* How many moves the bot has taken */
	private int moveNumber;
	
	/* The map as last seen through the LOOK command */
	private char[][] mapAsLastSeen;
	
	/* The row and column position of the human player,
	   as last seen through the LOOK command */
	private int humanPlayerRowPos;
	private int humanPlayerColumnPos;
	
	/* The bots row and column position on the map,
	   as last seen through the LOOK command */
	private int botRowPosOnLookMap;
	private int botColumnPosOnLookMap;
	
	/* Holds whether the bot has seen the human player through the look command*/
	private boolean humanPlayerSeen;
	
	
 	/**
 	 * Constructor for BotPlayer
 	 */
	public BotPlayer() {
		super();
		moveNumber = 0;
		mapAsLastSeen = new char[5][5];
		humanPlayerSeen = false;
	}
	
	
 	/**
 	 * Generates a command for the bot player, 
 	 * depending on how moves have been taken. 
 	 */
	@Override
	public void generateCommand() {
		/*The bot will always look at the map first, move two directions, then look at the map again, 
		  in order to see whether it can see the player and note its position  */
		if (moveNumber%3 == 0) {
			this.setCommand(this.getAvaliableCommands()[7]);
		}else {
			this.setCommand(getDirectionToMoveIn());
		}
		moveNumber++;
	}
	
	
    /**
  	 * Returns the command to be executed (found by using generateCommand() and getDirection())
  	 */
	@Override
	public String getNextAction() {
		return this.getCommand();
	}
	
	
 	/**
 	 * Processes the map as seen when using the LOOK command
 	 * @param mapInsight : the string returned when using the LOOK command. 
 	 */
	public void setMapAsLastSeen(String mapInsight) {
		/* Look() from gameLogic returns the map with spaces, remove these spaces to process the map as one long string */
		mapInsight = mapInsight.replace("\n", "");
		int charIndex = 0;
		boolean foundHuman = false;
		
		/* Convert the string returned by the look command into a 2D array. 
		   This way, we can take note if we see the player, as well as it's position on the map, 
		   and use this to calculate what move will bring the bot closest to the player */ 
		for (int i = 0; i < 5; i++){
			for (int j = 0; j < 5; j++) {
				mapAsLastSeen[i][j] = mapInsight.charAt(charIndex);
				charIndex++;
				if (mapAsLastSeen[i][j] == 'P') {
					humanPlayerRowPos = i;
					humanPlayerColumnPos = j;
					foundHuman = true;
				}
				if (mapAsLastSeen[i][j] == 'B') {
					botRowPosOnLookMap = i;
					botColumnPosOnLookMap = j;
				}
			}
		}
		humanPlayerSeen = foundHuman;
	}
	
	
 	/**
 	 * Generates a sensible direction for the bot to move in 
 	 * @return : an element of availableCommands that corresponds to moving in a certain direction
 	 */
	public String getDirectionToMoveIn() {
		
		
		/* To decide which direction to move in, 
		   we score each direction we could move in and keep this in an array, 
		   the lower the score the better */
		double [] directionScores = new double [4];
		/*DirectionScores[0] represents score for moving north
		  DirectionScores[1] represents score for moving south
		  DirectionScores[2] represents score for moving East
		  DirectionScores[3] represents score for moving West*/
		
		/* If multiple directions have the same, lowest score, we will need to choose randomly 
		   between them */
		Random random = new Random();
		
		
		
		/* If any of the possible directions result in the bot moving into a wall,
		   the the directionScore for this move is set to positive infinity as the bot should
		   not take this move */
		if (mapAsLastSeen[botRowPosOnLookMap-1][botColumnPosOnLookMap] == '#') {
			directionScores[0] = Double.POSITIVE_INFINITY;
		}
		if (mapAsLastSeen[botRowPosOnLookMap+1][botColumnPosOnLookMap] == '#') {
			directionScores[1] = Double.POSITIVE_INFINITY;
		}
		if (mapAsLastSeen[botRowPosOnLookMap][botColumnPosOnLookMap+1] == '#') {
			directionScores[2] = Double.POSITIVE_INFINITY;
		}
		if (mapAsLastSeen[botRowPosOnLookMap][botColumnPosOnLookMap-1] == '#') {
			directionScores[3] = Double.POSITIVE_INFINITY;
		}
		
		
		/* If the player has been seen when the bot uses the LOOK command, then
		   we calculate the manhattan distance between the player (positions known from using the LOOK command)
		   and the bot depending on the directions it could take. */
		if (humanPlayerSeen) {
			directionScores[0] += Math.abs(humanPlayerRowPos-(botRowPosOnLookMap-1)) + Math.abs(humanPlayerColumnPos-botColumnPosOnLookMap);
			directionScores[1] += Math.abs(humanPlayerRowPos-(botRowPosOnLookMap+1)) + Math.abs(humanPlayerColumnPos-botColumnPosOnLookMap);
			directionScores[2] += Math.abs(humanPlayerRowPos-botRowPosOnLookMap) + Math.abs(humanPlayerColumnPos-(botColumnPosOnLookMap+1));
			directionScores[3] += Math.abs(humanPlayerRowPos-botRowPosOnLookMap) + Math.abs(humanPlayerColumnPos-(botColumnPosOnLookMap-1));
		}
		
		/* Sometimes, if the bot is not seen and there are no walls around, 
		   each move we could make will have the same score. In this case,
		   make a random move (N, S, W or E) */
		
		boolean scoresTheSame = true;
		/* Check if the scores are all the same*/
		for (int i = 0; i < directionScores.length; i++) {
			if (directionScores[0] != directionScores[i]) {
				scoresTheSame = false;
				break;
			}
		}
		/* If scores are the same, the bot returns a random direction to go in */
		if (scoresTheSame) {
			int randomMove = random.nextInt(4)+2;
			return this.getAvaliableCommands()[randomMove];
		}
		
		/* calculate minimum score for each direction */
		double minScore = Math.min(Math.min(directionScores[0], directionScores[1]), Math.min(directionScores[2], directionScores[3]));
		
		/* Multiple elements in directionScores may have the same score which is also the minimum score. We add the indexes 
		   of these elements to the arraylist directionsWithLowestScore*/
		ArrayList<Integer> directionsWithLowestScore = new ArrayList<>();
		for (int i = 0; i < directionScores.length; i++ ) {
			if (directionScores[i] == minScore) {
				directionsWithLowestScore.add(i);
			}
		}
		
		/* directionsWithLowestScore holds the indexes of the elements in directionScores that contain the lowest score.
		   We randomly select one of these indexes from directionWithLowestScore to decide what move to make*/ 
		int randomDirectionIndex = directionsWithLowestScore.get(random.nextInt(directionsWithLowestScore.size()));

		/* return the direction with the lowest score */ 
		if (randomDirectionIndex == 0) {
			/* If randomDirectionIndex is 0, the moving north had the lowest score as
			   the score for moving north was kept in directionScores[0]. */
			
			/* Bot updates its position as it's moving north */ 
			botRowPosOnLookMap -= 1;
			
			/* return availableCommands[2], which is 'MOVE N' */
			return this.getAvaliableCommands()[2];
		}else if (randomDirectionIndex == 1) {
			botRowPosOnLookMap += 1;
			return this.getAvaliableCommands()[4];
		}else if (randomDirectionIndex == 2) {
			botColumnPosOnLookMap += 1;
			return this.getAvaliableCommands()[3];
		}else {
			botColumnPosOnLookMap -= 1;
			return this.getAvaliableCommands()[5];
		}
		
	}
}

