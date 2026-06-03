package com.anxin.navigation.application.map;

import com.anxin.navigation.domain.graph.CampusGraph;
import com.anxin.navigation.domain.model.Edge;
import com.anxin.navigation.domain.model.PlaceType;
import com.anxin.navigation.domain.model.RoadType;
import com.anxin.navigation.domain.model.Vertex;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GraphConnectivityCheckerTest {

    @Test
    void shouldReturnConnectedForFullyConnectedGraph() {
        CampusGraph graph = new CampusGraph();
        Vertex a = new Vertex("A", "A", PlaceType.OTHER, 0, 0, "");
        Vertex b = new Vertex("B", "B", PlaceType.OTHER, 5, 0, "");
        Vertex c = new Vertex("C", "C", PlaceType.OTHER, 0, 5, "");
        graph.addVertex(a);
        graph.addVertex(b);
        graph.addVertex(c);
        graph.addEdge(new Edge(a, b, 5.0, false, false, RoadType.PATH));
        graph.addEdge(new Edge(b, c, 5.0, false, false, RoadType.PATH));

        GraphConnectivityChecker.ConnectivityReport report = GraphConnectivityChecker.check(graph);
        assertTrue(report.fullyConnected);
    }

    @Test
    void shouldDetectIsolatedVertex() {
        CampusGraph graph = new CampusGraph();
        Vertex a = new Vertex("A", "A", PlaceType.OTHER, 0, 0, "");
        Vertex b = new Vertex("B", "B", PlaceType.OTHER, 5, 0, "");
        Vertex isolated = new Vertex("ISO", "Isolated", PlaceType.OTHER, 10, 10, "");
        graph.addVertex(a);
        graph.addVertex(b);
        graph.addVertex(isolated);
        graph.addEdge(new Edge(a, b, 5.0, false, false, RoadType.PATH));

        GraphConnectivityChecker.ConnectivityReport report = GraphConnectivityChecker.check(graph);
        assertFalse(report.fullyConnected);
        assertTrue(report.toWarning().contains("Isolated"));
    }

    @Test
    void shouldHandleEmptyGraph() {
        CampusGraph graph = new CampusGraph();
        GraphConnectivityChecker.ConnectivityReport report = GraphConnectivityChecker.check(graph);
        assertTrue(report.fullyConnected);
    }

    @Test
    void shouldHandleNullGraph() {
        GraphConnectivityChecker.ConnectivityReport report = GraphConnectivityChecker.check(null);
        assertTrue(report.fullyConnected);
    }
}
