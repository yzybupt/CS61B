public class RoutingNode implements Comparable<RoutingNode> {
    private long id;
    private double distanceToOrigin;
    private double distanceToDest;

    public RoutingNode(long id, double dis, double dis2) {
        this.id = id;
        this.distanceToDest = dis2;
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

    public double getDistanceToDest() {
        return distanceToDest;
    }

    public void setDistanceToDest(double a) {
        distanceToDest = a;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoutingNode that = (RoutingNode) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return 1;
    }
    
    @Override
    public int compareTo(RoutingNode o) {
        if (this.distanceToDest + this.distanceToOrigin > o.getDistanceToDest() + o.getDistanceToOrigin()) {
            return 1;
        } else if (this.distanceToDest + this.distanceToOrigin < o.getDistanceToDest() + o.getDistanceToOrigin()) {
            return -1;
        } else {
            return 0;
        }
    }
}

