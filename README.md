# Tetris

##Game Description:
Tetris is a game in which the player moves and rotates dropping pieces so that they form solid lines. 
Completely filled lines are cleared, giving points to the player. 
The Wikipedia page for Tetris is a very good source of information about the various incarnations of the game, if you have questions. 
There are many free versions of Tetris online that you could look at such as http://www.tetrislive.com/

##Game Pieces
![Alt text](https://github.com/tuanhuynh52/Tetris/blob/master/1.png?raw=true "Optional 1")
#####Each standard piece is composed of four blocks. The “L” and “J”, and “S” and “Z”, pieces are mirror images of each other, but we'll just think of them as similar but distinct pieces.
Our abstraction will be that a Piece object represents a single Tetris piece with position and rotational state.A piece can be rotated 90° clockwise.
Enough rotations get you back to the original orientation, Each Tetris piece has one (the O), or four (the L, J, T, I, S and Z) 
distinct rotational states as shown on the next page. At the top of the diagram notice the increasing series of numbers 0 – 3 which represent the various rotational states. 
New pieces are initialized to state zero. Clockwise rotations increment the rotational state.

####Rotation: 
![Alt text](https://github.com/tuanhuynh52/Tetris/blob/master/2.png?raw=true "Optional 2")
##Duties and Reponsiblities:

* A graphical representation of a 10 x 20 Tetris game board, showing its filled squares and its piece-in-progress
* A graphical display of the "next" piece that will come after the current piece-in-progress. 
 This piece preview must show the "next" piece in the same rotational state in which it will appear on the board.
* The ability for the user to move the piece in progress left, right, down 1 line, and to rotate it clockwise using keyboard keys
* the ability to instantly drop a piece downward to the bottom by pressing an appropriate key
* some visual indication (initial greeting dialog, help window, display in the main window, etc.) of the keys that control the game
* an animation timer that makes the game board update at least once per second, causing the current piece to fall
* some message or indication that the game is over, displayed when the game ends (that is, when frozen pieces extend above the top of the board)
* the GUI MUST implement Observer to complete the observer design pattern for communication between the GUI and the board.
