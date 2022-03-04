import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Node {
    private int neighborNumber = 0;
    private long id = 0;
    private double lon = 0;
    private double lat = 0;
    private String name = "";
    private ConcurrentHashMap<Node, Double> neighborss = new ConcurrentHashMap<>();

    public Node(long id, double lon, double lat) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
    }


    public void addNeighbor(Node n, double distance) {
        if (!neighborss.contains(n)) {
            neighborss.put(n, distance);
            neighborNumber++;
        }
    }

    public void deleteNeighbor(Node n) {
        if (neighborss.contains(n)) {
            neighborss.remove(n);
            neighborNumber--;
        }
    }
    public ArrayList<Node> getNeighbor() {
        ArrayList<Node> temp = new ArrayList<>();
        for (Node n : neighborss.keySet()) {
            temp.add(n);
        }
        return temp;
    }


    public long getId() {
        return this.id;
    }

    public int getNeighborNumber() {
        return neighborNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLon() {
        return this.lon;
    }

    public double getLat() {
        return this.lat;
    }
}
