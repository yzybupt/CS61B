public class RoutingNode {
    private long id;
    private double distanceToOrigin;

    public RoutingNode (long id, double dis) {
        this.id = id;
        this.distanceToOrigin = dis;
    }

    public long getId() {
        return id;
    }

    public double getDistanceToOrigin() {
        return distanceToOrigin;
    }

    public void setDistanceToOrigin(double a) {
        distanceToOrigin = a;
    }



}
