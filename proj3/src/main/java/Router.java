import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    private static final double inf = Double.MAX_VALUE;
    static HashMap<Long, Long> edgeFrom;
    static HashMap<Long, RoutingNode> id2RoutingNode;
    static HashMap<RoutingNode, Integer> RoutingNode2Index;
    static HashSet<RoutingNode> marked;
    static IndexMinPQ<RoutingNode> fringe;

    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        edgeFrom = new HashMap<>();
        fringe = new IndexMinPQ<>(g.numberOfNodes());
        id2RoutingNode = new HashMap<>();
        marked = new HashSet();
        RoutingNode2Index= new HashMap<>();
        //完成空fringe的构建 空marked 空edgeFrom的建构


        Node startNode = closetNode(g, stlon, stlat);
        Node destNode = closetNode(g, destlon, destlat);
        RoutingNode start = new RoutingNode(startNode.getId(), 0, g.distance(startNode.getId(), destNode.getId()));
        RoutingNode dest = new RoutingNode(destNode.getId(), inf, 0);
        fringe.insert(0, start);
        fringe.insert(1, dest);
        RoutingNode2Index.put(start, 0);
        RoutingNode2Index.put(dest, 1);
        //edgeFrom.put(start.getId(), Long.MIN_VALUE);
        //edgeFrom.put(dest.getId(), Long.MIN_VALUE);
        id2RoutingNode.put(start.getId(), start);
        id2RoutingNode.put(dest.getId(), dest);


        int i = 2;
        for (Long ids : g.vertices()) {
            if (ids != start.getId() && ids != dest.getId()) {
                RoutingNode temp = new RoutingNode(g.nodes.get(ids).getId(), inf, g.distance(ids, dest.getId()));
                fringe.insert(i, temp);
                RoutingNode2Index.put(temp, i);
                i++;
                //edgeFrom.put(temp.getId(), Long.MIN_VALUE);
                id2RoutingNode.put(temp.getId(), temp);
            }
        }
        //完成fringe的填充

        //System.out.println(dest.getId());
        //System.out.println(start.getId());
        //开始进行Dij寻找

        Dij(g, dest.getId());
        if (edgeFrom.isEmpty()) {
            return null;
        } else {
            LinkedList<Long> path = new LinkedList<>();
            Long temp = dest.getId();
            path.addFirst(temp);
            while (!edgeFrom.isEmpty()) {
                if (edgeFrom.get(temp) != start.getId()) {
                    temp = edgeFrom.get(temp);
                    path.addFirst(temp);
                } else {
                    temp = edgeFrom.get(temp);
                    path.addFirst(temp);
                    break;
                }
            }

            return path;
        }

    }

    private static void Dij(GraphDB g, long dest) {
        while (!fringe.isEmpty()) {
            RoutingNode parent = fringe.minKey();
            fringe.delMin();
            if (parent.getId() == dest) {
                return;
            }
            marked.add(parent);
            for (Node neighbor : g.nodes.get(parent.getId()).getNeighbor()) {
                if (!marked.contains(id2RoutingNode.get(neighbor.getId()))) {
                    double dis = parent.getDistanceToOrigin() + g.distance(parent.getId(), neighbor.getId());
                    RoutingNode k = id2RoutingNode.get(neighbor.getId());
                    if(k.getDistanceToOrigin() > dis) {
                        RoutingNode k1 = new RoutingNode(k.getId(), dis, k.getDistanceToDest());
                        RoutingNode2Index.put(k1, RoutingNode2Index.get(k));
                        fringe.decreaseKey(RoutingNode2Index.get(k1), k1); //sink!!
                        edgeFrom.put(neighbor.getId(), parent.getId());
                    }
                }
            }
        }


    }


    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        return null; // FIXME
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";

        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                        && way.equals(((NavigationDirection) o).way)
                        && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }

    }

    private static Node closetNode(GraphDB db, double lon, double lat) {
        int i = 0;
        Node closet = null;
        double distance = 0;
        for (Long id : db.vertices()) {
            if (i == 0) {
                closet = db.nodes.get(id);
                distance = GraphDB.distance(lon, lat, closet.getLon(), closet.getLat());
                i = 1;
            } else {
                double temp = GraphDB.distance(lon, lat, db.nodes.get(id).getLon(), db.nodes.get(id).getLat());
                if (temp < distance) {
                    closet = db.nodes.get(id);
                    distance = temp;
                }
            }
        }
        return closet;

    }

}
