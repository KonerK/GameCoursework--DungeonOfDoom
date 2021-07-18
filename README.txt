- Principles of Programming 2nd Coursework - Dungeon of Doom

This project has been created as my second piece of programming coursework, Dungeon of Doom, and has been programmed in Java. The game begins with the player being spawned on a rectangular grid - the map. Also on the map are gold tiles, exit tiles, empty tiles and walls. The goal of the player is to move around the grid to collect a certain amount of gold, then find their way to an exit tile in order to leave the dungeon and win the game.

- Notes for Users

1. Configuration Instructions
This game can be run through your command line. Navigate in the command line to where you extracted the zip file containing the code, and compile the java files, using 'javac *.java'. The game should be run from GameLogic, as this contains the main method of the program. Enter in 'java GameLogic' and the game should begin.

The code files can also be uploaded on to any IDE, as long as the code can be run using Java 11. Upon moving the folders into an IDE, under the same project, click run to play the game. 

2. Using Maps
Upon pressing play, the user is promoted to enter a map. The map text files should be placed at the root of the project (the same place as to where you unzipped the zip file), as the maps are accessed directly by their file name. Map file names should be of the form 'name.txt' and to find the map you only need to write 'name' into the console when asked for the map name.

Note that after three failed attempts to search for a suitable map file, a default map is used for the game, and the user plays using this map. 

3. How to Play

Upon successfully running the game you will be told to enter a map name. You can do this (described above) or just press enter until the default map is used. After a map is chosen a message describing which dungeon you are in appears and the game begins. You can start straight away by a command (the commands available to you are described below). 
The player is able to move around and inspect the map by typing one of several commands into the console: 

HELLO - Displays amount of gold needed for the player to collect to win the game.
GOLD - Displays the current gold owned by the player. 
MOVE <direction> - Moves the player in an indicated direction. Available directions include: N, S, E or W.
PICKUP - Allows the player to pickup the gold if they are standing on it.
LOOK - Shows the player a 5x5 grid of the map around the player, with the player at the centre.
QUIT - Quits the game. If the player is standing on an exit tile and has collected the gold required to win the game then the player has won the game. Otherwise, the player has lost. 

There is also a bot on the map, which aims to walk around the map and look for the player. If the bot moves into the same position on the map as the player (or vice versa), then the bot catches the player and the player loses.

On the map (as seen through the LOOK command):
'P' - refers to the player
'B' - refers to the bot
'.' - refers to an empty tile, which are free to walk over by the player
'G' - refers to gold, which can be picked up by the player
'E' - refers to an exit tile
'#' - refers to a wall. The player cannot move through a wall.

- Notes for Developers

The code has been written in Java, making use of IntelliJ. The project contains 5 classes:
GameLogic - This class contains the main method from which the game is run. Within GameLogic, we create a Map, a BotPlayer and a HumanPlayer. We also keep track of whether the game is running or not, and whose turn it is (the bot or the player). In GameLogic, we add the players to the map, through addPlayerToMap(), and perform the actions each player specifies by calling methods such as hello(), gold(), move(). 

Map - The map class holds the current state of the map. This includes the map stored as a 2D char array, the name of the map, the gold required to be collected on the map to win the game, and the amount of rows/columns of the map, which are used in representing the map as a 2D array. Within this class, we deal with reading in the map from a file and, if this is not possible, providing a default map. We also use this class to update the position of the player/bot on the map as well as the position of items on the map.

Player - This is an abstract class, and is inherited by both BotPlayer and HumanPlayer, and contains the essential fields and behaviours of player classes. This class contains the necessary fields a player needs: the commands available to the player, the command they want to execute, their row/column position on the map (in order to move the player when they execute a move command) and the item they are standing on. Suitable accessor and mutator methods are provided for these fields. Two abstract methods are created in Player: generateCommand() and getNextAction(). These methods are abstract as how each player generates a command and how each command is processed is different depending on the player type.

HumanPlayer - HumanPlayer represents the class for the user. Two more fields are created on top of the fields provided by Player: the amount of gold the player owns and a BufferedReader Object. Within generateCommand() we gather the command the user wants to execute through asking for input in the console. This input is then processed in getNextAction(), where we look to see if the command entered by the user corresponds to any of the commands available to them. If it does, we can return the command the user wants to execute to GameLogic.

BotPlayer - BotPlayer represents the computer controlled player. Within generateCommand(), a command is generated based on how many moves the bot has made, and whether the bot has seen the player through the look command. Every two directions the bot moves in, generateCommand returns the LOOK command. The boy player then stores information about the map that it can gather through this command - such as the position of the player relative to the bot. If the player can not be seen, the next two moves of the bot will be to move in a random direction, as such generateCommand() returns either MOVE N, MOVE S, MOVE E or MOVE W. If the bot sees the player, it decides which direction to move in depending on which direction will bring it closest to the player (this is decided through a separate method called getDirectionToMoveIn, which is called in generateCommand() on moves when the bot is not using the LOOK command.) In getNextAction(), we simply return the command specified by the bot from generateCommand() to GameLogic. 

