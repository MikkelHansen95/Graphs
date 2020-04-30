package graphs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdjacencyGraph {

    private int V;
    private int E = 0;
    //private final EdgeNode[] vertices;
    private Map<String, EdgeNode> vertices;

    public AdjacencyGraph() {
        this.V = V;
        vertices = new HashMap<>();
    }

   class EdgeNode {
        String destination;
        String source;
        String airline;
        Float distance;
        Float time;
        EdgeNode next;

        EdgeNode(String destination, String source, String airline, Float distance, Float time, EdgeNode next) {
            this.destination = destination;
            this.source = source;
            this.airline = airline;
            this.distance = distance;
            this.time = time;
            this.next = next;
        }

        @Override
        public String toString() {
            return this.destination + "; " + this.airline + "; " + this.next;
        }
    }

    public int getV() {
        return V;
    }

    public int getE() {
        return E;
    }

    public Map<String, EdgeNode> getVertices() {
        return vertices;
    }

    public void addEdgeWithSameAirline(String airline, String source, String destination, Float distance, Float time) {
        EdgeNode node = null;
        if (vertices.get(source) == null) {
            node = new EdgeNode(destination, source, airline, distance, time, vertices.get(source));
        } else {

            if (vertices.get(source).airline == airline) {
                node = vertices.get(source);
            } else {
//                System.out.println("Not same airline : " + airline);
                return;
            }
        }
        vertices.put(source, node);
        E++;
    }

    public void addEdge(String airline, String source, String destination, Float distance, Float time) {
        EdgeNode node = new EdgeNode(destination, source, airline, distance, time, vertices.get(source));
        vertices.put(source, node);
        E++;
    }

    public Iterable<String> adjacents(String source) {
        List<String> adjacents = new ArrayList<>();
        EdgeNode node = vertices.get(source);
        while (node != null) {
            adjacents.add(node.destination);
            node = node.next;
        }
        return adjacents;
    }

    public Iterable<EdgeNode> adjacentsWithEdgeNode(String source) {
        List<EdgeNode> adjacents = new ArrayList<>();
        EdgeNode node = vertices.get(source);
        while (node != null) {
            adjacents.add(node);
            node = node.next;
        }
        return adjacents;
    }

    public String toString() {
        String text = "";
        for (Map.Entry<String, EdgeNode> entry : vertices.entrySet()) {
            if (text.length() == 0) {
                text += "Airline : " + entry.getValue().airline + " - ";
            }
            text += "" + entry.getKey() + ": " + adjacents(entry.getKey()) + "\n";
        }
        return text;
    }

    public static void main(String[] args) {
        AdjacencyGraph g = new AdjacencyGraph();
        Set<String> sourceAirportCodes = new HashSet<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "./src/main/java/data/routes.txt"));
            String headers = reader.readLine();

            String line = reader.readLine();
            int count = 0;
            while (line != null) {
                String[] arr = line.split(";");

                String airline = arr[0];
                String source = arr[1];
                String destination = arr[2];
                Float distance = Float.parseFloat(arr[3]);
                Float time = Float.parseFloat(arr[4]);
                g.addEdge(airline, source, destination, distance, time);
                //System.out.println(airline + " " +  source + " " +destination + " " + distance + " " + time);
                sourceAirportCodes.add(source);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        BreadthFirstSearch bfsearch = new BreadthFirstSearch(g, "Airlines1");
//      Run for all 
        bfsearch.searchFromSameAirline("SYD");
        bfsearch.printWithAirline(System.out);
        
        /*
//        RUN FOR ONLY SAME AIRLINE
//        for (String s : sourceAirportCodes) {
//            BFSearch bfsearch = new BFSearch(g);
//            bfsearch.searchFrom(s);
//            bfsearch.print(System.out);
//        }
            */  
        DepthFirstSearch dfsearch = new DepthFirstSearch(g, "Airlines2");
//      Run for all 
        dfsearch.searchFromSameAirline("SYD");
        dfsearch.printWithAirline(System.out);
        
        /*
        //RUN FOR ONLY SAME AIRLINE
        for (String s : sourceAirportCodes) {
            DepthFirstSearch dfsearch1 = new DepthFirstSearch(g);
            dfsearch1.searchFrom(s);
            dfsearch1.print(System.out);
       }
       */
    }

}
