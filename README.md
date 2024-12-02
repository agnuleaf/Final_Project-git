# Grid Graph 

Basic 2D square grid graph representation written in Java. 
Grid stores walls placed by user and source and target endpoints for a shortest path search, provided by the `edu.princeton.cs.algs4` graph api.

## Details
On Mouse press event, the location is checked against the `Grid` instance's `StackSet` :
 `Stack` for fast push/pop and `Set` fast lookup. If the check succeeds, `GridDraw` draws.
To Undo a wall placement just pop the Stack to ID it and remove it from the set. NO attempt at Removal by mouse click.

Buttons :
 Run is available after the two endpoints are added.
 Undo is available after the two endpoints and a wall are added.

 ## TODO:

[x] Reset the maze after each run.
[] Test for bad input from user, dead end states, etc.
[] Change instructions on `JLabel`. eg. "Place endpoints" -> "Place walls or press run"
[] Cleanup unused or redundant logic & docComments on all public methods/classes except getters/setters.

[] try a few larger grid sizes with faster speeds. Could just parse cli args to set them, instead of gui.

## Maze
 User creates start/end points and obstacles in a maze. The shortest path is visualized as a breadth first search expanding wavefront over the grid and ...
 TODO alternate targeted search algorithm 

## Dependencies

- `edu.princeton.cs.algs4` library
- optional Java23 for markdown doc-comments
