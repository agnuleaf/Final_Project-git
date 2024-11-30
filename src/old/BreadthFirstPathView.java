package old;

import grid.GridDraw;
import grid.Grid;
import grid.GridPoint;
import edu.princeton.cs.algs4.*;

import edu.princeton.cs.algs4.BreadthFirstPaths;

import java.awt.*;

import static java.lang.Math.abs;

/// # 2D Grid Breadth First Search Visualization
///
/// Uses [BreadthFirstPaths] to find the shortest path from a given source to a target in an //TODO weighted
/// grid graph. Animating the expanding wavefront until the target is found.
/// @author Wesley Miller
public class BreadthFirstPathView implements Runnable {
    private Grid grid;
    private GridPoint p;
    private GridPoint q;
    private int tPause;
    private Draw pane;
    private GridDraw gridDraw;
    Thread t; // Run Path view after wavefront thread to demonstrate true operation

        @Override
    public void run() {
//        t = new Thread(this, "BFS Path Taken View" );
//        t.start();
//            System.out.println(Thread.currentThread().getThreadGroup());
            BreadthFirstPaths bfp = new BreadthFirstPaths(grid.graph(), grid.indexOf(p));
        if(bfp.hasPathTo(grid.indexOf(q))){
            GridPoint prev = p;
            for(var step  : bfp.pathTo(grid.indexOf(q))){
                gridDraw.path(prev, grid.pointAt(step), Color.PINK);
                prev = grid.pointAt(step);
                pane.disableDoubleBuffering();
                pane.pause(gridDraw.getPause());
//                pane.show();
//                try{
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
            }
        }
    }

    public BreadthFirstPathView(GridPoint p, GridPoint q, Grid grid, GridDraw gridDraw){
        this.p = p; this.q = q; this.grid = grid; this.gridDraw = gridDraw; this.pane = gridDraw.getDraw();
    }
    /// View the algorithm on a test set using internal `algs4.Draw`
    public static void main(String[] args) {

    }
}
