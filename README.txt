Igor Pogorelskiy
README Pacman

I made pacman for my final project game. The Maze class stores all of the 
items within the maze that pacman travels around. The Food class creates each
food element that the pacman eats. Each Barrier indicates one maze block 
that the pacman cannot go through. PowerUps class is in charge of the 
pacman's white balls that he can eats to gobble up the ghosts. The extra
class contains the other fruit that spawn for pacman to eat. The Ghost
class contains the spawning logic for the ghosts who chase pacman. The Pacman
class contains the logic for the pacman's drawing and spawning. The game class 
creates the gameboard and runs the game. The Player class stems from the Game
object class given to us renamed. The GameBoard contains the logic for the game,
the size of the blocks, the creation of the maze, collision detections and 
effects, pads the pixels so that the turning for the ghosts and pacman are
easier. The GameBoard class also controls the logic behind the power up bonuses
and the turning at intersections and when the ghosts hit the blocks of the maze.
The ghosts follow pacman with a simple AI when they hit the blocks, and
then they turn randomly at junctions. The timers and animations are also
controlled within the GameBoard.