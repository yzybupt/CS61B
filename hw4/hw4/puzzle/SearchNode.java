package hw4.puzzle;

public class SearchNode {
    protected int moves;
    protected WorldState node;
    protected SearchNode prev;

    public SearchNode(int moves, WorldState node, SearchNode prev) {
        this.moves = moves;
        this.node = node;
        this.prev = prev;
    }

}
