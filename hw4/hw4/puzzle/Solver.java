package hw4.puzzle;
import edu.princeton.cs.algs4.MinPQ;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.ArrayList;
public class Solver {
    private MinPQ<SearchNode> PQ;
    private LinkedList<WorldState> results;
    private HashMap<WorldState, Integer> distance;
    private ArrayList<WorldState> finalr;
    public Solver(WorldState initial) {
        finalr = new ArrayList<>();
        distance = new HashMap<>();
        results = new LinkedList<>();
        PQ = new MinPQ<>(cmp);
        PQ.insert(new SearchNode(0, initial, null));
        solve();
    }

    public int moves() {
        return finalr.size() - 1;
    }

    public Iterable<WorldState> solution() {
        return finalr;
    }

    private void solve() {
        SearchNode current;
        while (true) {
            current = PQ.min();
            PQ.delMin();
            if (current.node.isGoal()) {
                break;
            } else {
                for (WorldState s : current.node.neighbors()) {
                    if (current.prev == null) {
                        PQ.insert(new SearchNode(current.moves + 1, s, current));
                    } else if (!current.prev.node.equals(s)) {
                        PQ.insert(new SearchNode(current.moves + 1, s, current));
                    }

                }
            }
        }

        SearchNode temp = current;
        while (temp != null) {
            results.add(temp.node);
            temp = temp.prev;
        }

        while (!results.isEmpty()) {
            finalr.add(results.removeLast());
        }

    }


    private Comparator<SearchNode> cmp = new Comparator<SearchNode>() {
        public int compare(SearchNode e1, SearchNode e2) {
            return (e1.moves + getDistance(e1.node)) - (e2.moves + getDistance(e2.node));
        }
    };

    private int getDistance(WorldState e) {
        if (distance.containsKey(e)) {
            return distance.get(e);
        } else {
            int a = e.estimatedDistanceToGoal();
            distance.put(e, a);
            return a;
        }
    }

}
