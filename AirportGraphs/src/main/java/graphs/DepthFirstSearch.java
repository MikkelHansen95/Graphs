package graphs;

import basics.ArrayStack;
import basics.Stack;
import graphs.Edge;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DepthFirstSearch {

    private final AdjacencyGraph graph;
    private Map<String, String> visitedFrom;
    private Map<String, Edge> visitedFromWithAirline;
    private Stack<Edge> edges;

    public DepthFirstSearch(AdjacencyGraph graph) {
        ArrayList<String> keys = new ArrayList<String>(graph.getVertices().keySet());
        this.graph = graph;
        visitedFrom = new HashMap<>();
        for (int v = 0; v < keys.size(); v++) {
            visitedFrom.put(keys.get(v), "");
        }
        edges = new ArrayStack<>(2_000);
    }
    
    public DepthFirstSearch(AdjacencyGraph graph, String name) {
        ArrayList<String> keys = new ArrayList<>(graph.getVertices().keySet());
        this.graph = graph;
        visitedFromWithAirline = new HashMap<>();
        for (int v = 0; v < keys.size(); v++) {
            visitedFromWithAirline.put(keys.get(v), new Edge("", "", "", 0F, 0F));
        }
        edges = new ArrayStack<>(5_000);
    }

    private void register(Edge edge) {
        if (visitedFrom.get(edge.to) == null || !visitedFrom.get(edge.to).equals("")) {
            return;
        }
        // only register if 'to' has not been registered already
        edges.push(edge);
        visitedFrom.put(edge.to, edge.from);
    }
    
    private void registerWithSameAirline(Edge edge) {
        if (visitedFromWithAirline.get(edge.to) == null || !visitedFromWithAirline.get(edge.to).to.equals("")) {
            return;
        }
        // only register if 'to' has not been registered already
        edges.push(edge);
        visitedFromWithAirline.put(edge.to, edge);
    }

    public void searchFrom(String v) {
        register(new Edge(v, v));
        while (!edges.isEmpty()) {
            Edge step = edges.pop();
            for (String w : graph.adjacents(step.to)) {
                register(new Edge(step.to, w));
            }
        }
    }
    
    public void searchFromSameAirline(String v) {
        registerWithSameAirline(new Edge(v, v, "", 0F, 0F));
        while (!edges.isEmpty()) {
            Edge step = edges.pop();
            for (AdjacencyGraph.EdgeNode node : graph.adjacentsWithEdgeNode(step.to)) {
                registerWithSameAirline(new Edge(step.to, node.destination, node.airline, node.distance, node.time));
            }
        }
    }

    public String showPathTo(String w) {
        String path = w;
        while (!visitedFrom.get(w).equals(w) && !visitedFrom.get(w).equals("")) {
            w = visitedFrom.get(w);
            path = "" + w + " -> " + path;
        }
        return path;
    }
    
    public String showPathToWithSameAirline(String w, String airline) {
        String path = w + " ||| Airline company: " + airline;
        while (visitedFromWithAirline.get(w) != null
                && !visitedFromWithAirline.get(w).to.equals(w) 
                && !visitedFromWithAirline.get(w).to.equals("")
                && visitedFromWithAirline.get(w).airline.equals(airline)) {
            
            String currAirline = visitedFromWithAirline.get(w).airline;
            w = visitedFromWithAirline.get(w).to;
            path = "" + w + " (" + currAirline + ") -> " + path;
        }
        return path;
    }

    public void print(PrintStream out) {
        int count = 0;
        for (Map.Entry<String, AdjacencyGraph.EdgeNode> entry : graph.getVertices().entrySet()) {
            String key = entry.getKey();
            String keyPath = showPathTo(entry.getKey());
            if (!key.equals(keyPath)) {
                //out.println("Airline : " + entry.getValue().airline + " - " + key + ": " + keyPath);
                count++;
            }
        }
        System.out.println("count df:  " + count);
    }

    public void printWithAirline(PrintStream out) {
        int count = 0;
        for (Map.Entry<String, AdjacencyGraph.EdgeNode> entry : graph.getVertices().entrySet()) {
            String key = entry.getKey();
            String keyPath = showPathToWithSameAirline(key, entry.getValue().airline);
            if (!key.equals(keyPath.substring(0, 3))) {
                out.println("" + key + ": " + keyPath);
            }
            count++;
        }
        System.out.println("Depth Search: " + count);
    }

}
