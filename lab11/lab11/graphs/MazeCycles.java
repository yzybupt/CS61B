package lab11.graphs;



public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    Maze maze;
    public MazeCycles(Maze m) {
        super(m);
        maze = m;
    }

    @Override
    public void solve() {

        for (int i = 0; i < maze.N() * maze.N(); i++) {
            if (!marked[i]) {
                edgeTo[i] = i;
                if (!dfs(i)) {
                    announce();
                    break;
                }
            }
        }
    }

    // Helper methods go here
    private boolean dfs(int v) {
        marked[v] = true;
        for (int w : maze.adj(v)) {
            if (!marked[w]) {
                marked[w] = true;
                edgeTo[w] = v;
                if (!dfs(w)) {
                    return false;
                }
            } else if (edgeTo[v] != w) {
                clear(w);
                edgeTo[w] = v;
                return false;
            }
        }
        return true;

    }

    private void clear(int v) {
        int child = v;
        int parent = edgeTo[v];
        while (edgeTo[parent] != parent) {
            edgeTo[child] = 2147483647;
            child = parent;
            parent = edgeTo[parent];
        }
        edgeTo[child] = 2147483647;
        edgeTo[parent] = 2147483647;
    }


}

