public class Edge {
    private Node[] nodes = new Node[2];
    private String name = "";

    public Edge (Node n1, Node n2, String name) {
        this.nodes[0] = n1;
        this.nodes[1] = n2;
        this.name = name;
    }


}
