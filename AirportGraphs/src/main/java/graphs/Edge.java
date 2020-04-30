package graphs;

public class Edge {

    public String from;
    public String to;
    public String airline = "";
    public Float distance;
    public Float time;

    public Edge(String from, String to, String airline, Float distance, Float time) {
        this.from = from;
        this.to = to;
        this.airline = airline;
        this.distance = distance;
        this.time = time;
    }

    public Edge(String from, String to, String airline) {
        this.from = from;
        this.to = to;
        this.airline = airline;
    }

    public Edge(String from, String to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        if (this.airline.length() > 0) {
            return "" + from + " -> " + to + " with : " + this.airline;
        } else {
            return "" + from + " -> " + to;
        }
    }
}
