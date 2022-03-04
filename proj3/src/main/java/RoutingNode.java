import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutingNode that = (RoutingNode) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, distanceToOrigin);
    }
}
