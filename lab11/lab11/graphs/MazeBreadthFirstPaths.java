package lab11.graphs;
import java.util.LinkedList;
/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    private LinkedList<Integer> callQueue;
    private Maze maze;
    private int source;
    private int target;
    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        callQueue = new LinkedList<>();
        maze = m;
        source = maze.xyTo1D(sourceX, sourceY);
        target = maze.xyTo1D(targetX, targetY);
        distTo[source] = 0;
        edgeTo[source] = source;
        marked[source] = true;
        callQueue.offer(source);
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {

        int v = 0;
        if (!callQueue.isEmpty()) {
            v = callQueue.poll();
            if (v == target) {
                return;
            }
            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    marked[w] = true;
                    distTo[w] = distTo[v] + 1;
                    edgeTo[w] = v;
                    callQueue.offer(w);
                }
            }
        }
        announce();
        bfs();

    }


    @Override
    public void solve() {
        bfs();
    }
}

