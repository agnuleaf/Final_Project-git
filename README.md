# Grid Graph 

2D square grid graph representation and 'maze' game written in Java. 
Two separate modes are provided:
1. Demo Mode : User places two endpoints and optionally as many walls as possible with the mouse.  
The shortest path is determined by the `edu.princeton.cs.algs4` graph api.
2. Game Mode : Player is given a grid with random walls, and required to place two endpoints. The shortest path,   
if it exists, is determined and converted to walls (except for the start and end points). The Goal is to cover as much
as possible until no path exists and the player is forced to place disconnected endpoints. 
The endpoints must be: 
   - far enough apart (about 5 squares)
   - different quadrants of the grid.

## Details of DEMO

On Mouse press event, the location is checked against the `Grid` instance's `StackSet` :
 `Stack` for fast push/pop and `Set` fast lookup. If the check succeeds, `GridDraw` draws. 

Buttons :
- Run is available after the two endpoints are added, to perform and visualize the visited points of breadth first 
search in its expanding wavefront.
- Undo is available after the two endpoints and a wall are added. It can only undo wall placement.
**After the Visualization Thread completes**:
- Run is changed to Continue maintaining the `Set` of walls within the grid but popping the stack history of placement.
- Undo is changed to Reset to which discards the `Set` for a new one, refreshing the grid.

## Dependencies

- `edu.princeton.cs.algs4` library
- optionally Java23 for markdown doc-comments rendering
