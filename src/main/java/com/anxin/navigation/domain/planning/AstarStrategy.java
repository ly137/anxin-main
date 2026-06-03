package com.anxin.navigation.domain.planning;

import com.anxin.navigation.domain.graph.CampusGraph;
import com.anxin.navigation.domain.model.Edge;
import com.anxin.navigation.domain.model.PathResult;
import com.anxin.navigation.domain.model.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

public class AstarStrategy implements PathPlanningStrategy {
    private final double scaleFactor;

    public AstarStrategy() {
        this(1.0);
    }

    public AstarStrategy(double scaleFactor) {
        if (scaleFactor <= 0 || Double.isNaN(scaleFactor) || Double.isInfinite(scaleFactor)) {
            throw new IllegalArgumentException("scaleFactor must be finite and > 0, was: " + scaleFactor);
        }
        this.scaleFactor = scaleFactor;
    }

    @Override
    public PathResult plan(CampusGraph graph, String startId, String endId) {
        Objects.requireNonNull(graph, "graph must not be null");

        String start = requireId(startId, "startId");
        String end = requireId(endId, "endId");
        ensureVertexExists(graph, start, "start");
        ensureVertexExists(graph, end, "end");

        Vertex startVertex = graph.getVertex(start);
        Vertex endVertex = graph.getVertex(end);

        if (start.equals(end)) {
            List<Vertex> single = Collections.singletonList(startVertex);
            return new PathResult(
                    startVertex, endVertex, single,
                    0.0, 0.0,
                    Collections.<Double>emptyList(),
                    Collections.emptyList()
            );
        }

        Map<String, Double> gScores = new HashMap<String, Double>();
        Map<String, String> previous = new HashMap<String, String>();
        Map<String, Edge> previousEdge = new HashMap<String, Edge>();

        for (Vertex vertex : graph.getAllVertices()) {
            gScores.put(vertex.getId(), Double.POSITIVE_INFINITY);
        }
        gScores.put(start, 0.0);

        PriorityQueue<AstarNode> openSet = new PriorityQueue<AstarNode>();
        openSet.add(new AstarNode(start, 0.0, 0.0));

        while (!openSet.isEmpty()) {
            AstarNode current = openSet.poll();
            double knownG = gScores.get(current.vertexId);
            if (current.gScore > knownG) {
                continue;
            }
            if (current.vertexId.equals(end)) {
                break;
            }

            for (Edge edge : graph.getNeighbors(current.vertexId)) {
                if (!edge.isAvailable()) {
                    continue;
                }
                String nextId = edge.getToVertex().getId();
                double tentativeG = knownG + edge.getWeight();
                if (tentativeG < gScores.get(nextId)) {
                    gScores.put(nextId, tentativeG);
                    previous.put(nextId, current.vertexId);
                    previousEdge.put(nextId, edge);
                    double heuristic = euclidean(graph.getVertex(nextId), endVertex);
                    openSet.add(new AstarNode(nextId, tentativeG, tentativeG + scaleFactor * heuristic));
                }
            }
        }

        if (!previous.containsKey(end)) {
            throw new NoRouteFoundException("No reachable path from " + start + " to " + end + ".");
        }

        return buildPathResult(graph, start, end, previous, previousEdge, gScores.get(end));
    }

    private double euclidean(Vertex a, Vertex b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }

    private PathResult buildPathResult(
            CampusGraph graph, String start, String end,
            Map<String, String> previous, Map<String, Edge> previousEdge, double totalDistance) {

        List<String> orderedIds = new ArrayList<String>();
        String cursor = end;
        orderedIds.add(cursor);
        while (!cursor.equals(start)) {
            cursor = previous.get(cursor);
            if (cursor == null) {
                throw new NoRouteFoundException("Path reconstruction failed from " + start + " to " + end + ".");
            }
            orderedIds.add(cursor);
        }
        Collections.reverse(orderedIds);

        List<Vertex> pathVertices = new ArrayList<Vertex>(orderedIds.size());
        for (String id : orderedIds) {
            pathVertices.add(graph.getVertex(id));
        }

        List<Double> segmentDistances = new ArrayList<Double>();
        for (int i = 1; i < orderedIds.size(); i++) {
            String currentId = orderedIds.get(i);
            String prevId = orderedIds.get(i - 1);
            Edge segment = previousEdge.get(currentId);
            if (segment == null || !segment.getFromVertex().getId().equals(prevId)) {
                throw new NoRouteFoundException("Path segment missing between " + prevId + " and " + currentId + ".");
            }
            segmentDistances.add(segment.getWeight());
        }

        return new PathResult(
                graph.getVertex(start), graph.getVertex(end),
                pathVertices, totalDistance, 0.0,
                segmentDistances, Collections.emptyList()
        );
    }

    private static void ensureVertexExists(CampusGraph graph, String id, String label) {
        if (!graph.containsVertex(id)) {
            throw new IllegalArgumentException(label + " vertex not found: " + id);
        }
    }

    private static String requireId(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value.trim();
    }

    private static final class AstarNode implements Comparable<AstarNode> {
        private final String vertexId;
        private final double gScore;
        private final double fScore;

        private AstarNode(String vertexId, double gScore, double fScore) {
            this.vertexId = vertexId;
            this.gScore = gScore;
            this.fScore = fScore;
        }

        @Override
        public int compareTo(AstarNode other) {
            return Double.compare(this.fScore, other.fScore);
        }
    }
}
