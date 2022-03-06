import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *  Parses OSM XML files using an XML SAX parser. Used to construct the graph of roads for
 *  pathfinding, under some constraints.
 *  See OSM documentation on
 *  <a href="http://wiki.openstreetmap.org/wiki/Key:highway">the highway tag</a>,
 *  <a href="http://wiki.openstreetmap.org/wiki/Way">the way XML element</a>,
 *  <a href="http://wiki.openstreetmap.org/wiki/Node">the node XML element</a>,
 *  and the java
 *  <a href="https://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html">SAX parser tutorial</a>.
 *
 *  You may find the CSCourseGraphDB and CSCourseGraphDBHandler examples useful.
 *
 *  The idea here is that some external library is going to walk through the XML
 *  file, and your override method tells Java what to do every time it gets to the next
 *  element in the file. This is a very common but strange-when-you-first-see it pattern.
 *  It is similar to the Visitor pattern we discussed for graphs.
 *
 *  @author Alan Yao, Maurice Lee
 */
public class GraphBuildingHandler extends DefaultHandler {
    /**
     * Only allow for non-service roads; this prevents going on pedestrian streets as much as
     * possible. Note that in Berkeley, many of the campus roads are tagged as motor vehicle
     * roads, but in practice we walk all over them with such impunity that we forget cars can
     * actually drive on them.
     */

    private static final String regEx = "[^0-9]";
    private Pattern p = Pattern.compile(regEx);//训练的模式匹配用来提取字符串中的数字

    private static final Set<String> ALLOWED_HIGHWAY_TYPES = new HashSet<>(Arrays.asList
            ("motorway", "trunk", "primary", "secondary", "tertiary", "unclassified",
                    "residential", "living_street", "motorway_link", "trunk_link", "primary_link",
                    "secondary_link", "tertiary_link"));
    private String activeState = "";
    private final GraphDB g;

    /**
     * Create a new GraphBuildingHandler.
     * @param g The graph to populate with the XML data.
     */
    public GraphBuildingHandler(GraphDB g) {
        this.g = g;
    }

    /**
     * Called at the beginning of an element. Typically, you will want to handle each element in
     * here, and you may want to track the parent element.
     * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
     *            if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if qualified names are
     *              not available. This tells us which element we're looking at.
     * @param attributes The attributes attached to the element. If there are no attributes, it
     *                   shall be an empty Attributes object.
     * @throws SAXException Any SAX exception, possibly wrapping another exception.
     * @see Attributes
     */

    HashMap<Integer,Long> nodesOnWay;
    int index;
    String wayName = "";
    int flag = 0; // default is that type of way is not eligible
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("node")) {
            activeState = "node";
            long id = Long.valueOf(attributes.getValue("id"));
            double lon = Double.valueOf(attributes.getValue("lon"));
            double lat = Double.valueOf(attributes.getValue("lat"));
            g.nodes.put(id, new Node(id, lon, lat));

        } else if (qName.equals("way")) {

            activeState = "way";
            nodesOnWay = new HashMap<>();
            index = 0;

        } else if (activeState.equals("way") && qName.equals("nd")) {

            index++;
            nodesOnWay.put(index, Long.valueOf(attributes.getValue("ref")));

        } else if (activeState.equals("way") && qName.equals("tag")) {
            String k = attributes.getValue("k");
            String v = attributes.getValue("v");
            if (k.equals("maxspeed")) {
                //waySpeed = Integer.valueOf(p.matcher(v).replaceAll("").trim());
            } else if (k.equals("highway")) {
                if (ALLOWED_HIGHWAY_TYPES.contains(v)) {
                    flag = 1;
                }
            } else if (k.equals("name")) {
                wayName = v;
            }
        } else if (activeState.equals("node") && qName.equals("tag") && attributes.getValue("k")
                .equals("name")) {
            /* While looking at a node, we found a <tag...> with k="name". */
            /* TODO Create a location. */
            /* Hint: Since we found this <tag...> INSIDE a node, we should probably remember which
            node this tag belongs to. Remember XML is parsed top-to-bottom, so probably it's the
            last node that you looked at (check the first if-case). */
//            System.out.println("Node's name: " + attributes.getValue("v"));
        }
    }

    /**
     * Receive notification of the end of an element. You may want to take specific terminating
     * actions here, like finalizing vertices or edges found.
     * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
     *            if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if qualified names are
     *              not available.
     * @throws SAXException  Any SAX exception, possibly wrapping another exception.
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("way")) {
            if (flag == 1) {
                int k = 1;
                for (Integer indices : nodesOnWay.keySet()) {
                    if (k == nodesOnWay.size()) {
                        break;
                    }
                    k++;
                    double dis = GraphDB.distance(g.nodes.get(nodesOnWay.get(indices)).getLon(),g.nodes.get(nodesOnWay.get(indices)).getLon()
                    , g.nodes.get(nodesOnWay.get(indices + 1)).getLon(), g.nodes.get(nodesOnWay.get(indices + 1)).getLat());

                    g.nodes.get(nodesOnWay.get(indices)).addNeighbor(g.nodes.get(nodesOnWay.get(indices + 1)),dis);
                    g.nodes.get(nodesOnWay.get(indices + 1)).addNeighbor(g.nodes.get(nodesOnWay.get(indices)),dis);
                }
                wayName = "";
                flag = 0;
            }
        }
    }

}
