package graphs;

import basics.ArrayQueue;
import basics.Queue;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;

public class BreadthFirstSearch {

    private AdjacencyGraph graph;
    private Map<String, String> visitedFrom;
    private Map<String, Pair<String, String>> visitedFromWithAirline;
    private Queue<Edge> edges;

    public BreadthFirstSearch(AdjacencyGraph graph) {
        ArrayList<String> keys = new ArrayList<>(graph.getVertices().keySet());
        this.graph = graph;
        visitedFrom = new HashMap<>();
        for (int v = 0; v < keys.size(); v++) {
            visitedFrom.put(keys.get(v), "");
        }
        edges = new ArrayQueue<>(5_000);
    }

    public BreadthFirstSearch(AdjacencyGraph graph, String name) {
        ArrayList<String> keys = new ArrayList<>(graph.getVertices().keySet());
        this.graph = graph;
        visitedFromWithAirline = new HashMap<>();
        for (int v = 0; v < keys.size(); v++) {
            visitedFromWithAirline.put(keys.get(v), null);
        }
        edges = new ArrayQueue<>(5_000);
    }

    private class Edge {

        String from;
        String to;
        String airline = "";
        Float distance;
        Float time;

        Edge(String from, String to, String airline, Float distance, Float time) {
            this.from = from;
            this.to = to;
            this.airline = airline;
            this.distance = distance;
            this.time = time;
        }

        Edge(String from, String to, String airline) {
            this.from = from;
            this.to = to;
            this.airline = airline;
        }

        Edge(String from, String to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            return "" + from + " -> " + to;
        }
    }

    private void register(Edge edge) {
        if (visitedFrom.get(edge.to) == null || !visitedFrom.get(edge.to).equals("")) {
            return;
        }
        // only register if 'to' has not been registered already
        edges.enqueue(edge);
        visitedFrom.put(edge.to, edge.from);
    }

    private void registerWithSameAirline(Edge edge) {
        if (visitedFromWithAirline.get(edge.to) != null) {
            return;
        }
        // only register if 'to' has not been registered already
        edges.enqueue(edge);
        visitedFromWithAirline.put(edge.to, new Pair(edge.from, edge.airline));
    }

    public void searchFrom(String v) {
        register(new Edge(v, v));
        while (!edges.isEmpty()) {
            Edge step = edges.dequeue();
            for (String w : graph.adjacents(step.to)) {
                register(new Edge(step.to, w));
            }
        }
    }

    public void searchFromSameAirline(String v) {
        registerWithSameAirline(new Edge(v, v, ""));
        while (!edges.isEmpty()) {
            Edge step = edges.dequeue();
            for (AdjacencyGraph.EdgeNode node : graph.adjacentsWithEdgeNode(step.to)) {
                registerWithSameAirline(new Edge(step.to, node.destination, node.airline));
            }
        }
    }

    public String showPathToWithSameAirline(String w, String airline) {
        String path = w + " ||| Airline company: " + airline;
        while (visitedFromWithAirline.get(w) != null
                && !visitedFromWithAirline.get(w).getKey().equals(w)
                && !visitedFromWithAirline.get(w).getKey().equals("")
                && visitedFromWithAirline.get(w).getValue().equals(airline)) {

            String currAirline = visitedFromWithAirline.get(w).getValue();
            w = visitedFromWithAirline.get(w).getKey();
            path = "" + w + " (" + currAirline + ") -> " + path;
        }
        return path;
    }

    public String showPathTo(String w) {
        String path = w;
        while (!visitedFrom.get(w).equals(w) && !visitedFrom.get(w).equals("")) {
            w = visitedFrom.get(w);
            path = "" + w + " -> " + path;
        }
        return path;
    }

    public void print(PrintStream out) {
        int count = 0;
        for (Map.Entry<String, AdjacencyGraph.EdgeNode> entry : graph.getVertices().entrySet()) {
            String key = entry.getKey();
            String keyPath = showPathTo(entry.getKey());
            if (!key.equals(keyPath)) {
                out.println("Airline : " + entry.getValue().airline + " - " + key + ": " + keyPath);
                count++;
            }
        }
        System.out.println(count);
    }

    public void printWithAirline(PrintStream out) {
        int count = 0;
        for (Map.Entry<String, AdjacencyGraph.EdgeNode> entry : graph.getVertices().entrySet()) {
            String key = entry.getKey();
            String keyPath = showPathToWithSameAirline(key, entry.getValue().airline);
            if (!key.equals(keyPath.substring(0, 3))) {
                //out.println("" + key + ": " + keyPath);
                count++;
            }

        }
        System.out.println("Breadth Search: " + count);
    }

}
