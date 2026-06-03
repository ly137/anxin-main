package com.anxin.navigation.domain.planning;

import com.anxin.navigation.domain.graph.CampusGraph;
import com.anxin.navigation.domain.model.Edge;
import com.anxin.navigation.domain.model.PathResult;
import com.anxin.navigation.domain.model.PlaceType;
import com.anxin.navigation.domain.model.RoadType;
import com.anxin.navigation.domain.model.Vertex;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AstarStrategyTest {
    private final AstarStrategy strategy = new AstarStrategy();
    private final AstarStrategy weighted = new AstarStrategy(2.0);

    @Test
    void shouldFindShortestPathSameAsDijkstra() {
        CampusGraph graph = buildSimpleGraph();
        PathResult result = strategy.plan(graph, "A", "D");
        assertEquals(10.0, result.getTotalDistance(), 0.001);
        assertEquals(3, result.getPathList().size());
        assertEquals("A", result.getPathList().get(0).getId());
        assertEquals("B", result.getPathList().get(1).getId());
        assertEquals("D", result.getPathList().get(2).getId());
    }

    @Test
    void shouldSkipForbiddenEdges() {
        CampusGraph graph = buildSimpleGraph();
        graph.addEdge(new Edge(graph.getVertex("B"), graph.getVertex("D"), 5.0, false, true, RoadType.PATH));
        PathResult result = strategy.plan(graph, "A", "D");
        assertTrue(result.getTotalDistance() > 10.0);
    }

    @Test
    void shouldThrowWhenNoRouteFound() {
        CampusGraph graph = buildSimpleGraph();
        graph.addEdge(new Edge(graph.getVertex("A"), graph.getVertex("B"), 5.0, false, true, RoadType.PATH));
        graph.addEdge(new Edge(graph.getVertex("B"), graph.getVertex("D"), 5.0, false, true, RoadType.PATH));
        graph.addEdge(new Edge(graph.getVertex("A"), graph.getVertex("C"), 12.0, false, true, RoadType.PATH));
        graph.addEdge(new Edge(graph.getVertex("C"), graph.getVertex("D"), 11.0, false, true, RoadType.PATH));
        assertThrows(NoRouteFoundException.class, () -> strategy.plan(graph, "A", "D"));
    }

    @Test
    void shouldHandleStartEqualsEnd() {
        CampusGraph graph = buildSimpleGraph();
        PathResult result = strategy.plan(graph, "A", "A");
        assertEquals(0.0, result.getTotalDistance(), 0.001);
        assertEquals(1, result.getPathList().size());
    }

    @Test
    void weightedShouldStillFindPath() {
        CampusGraph graph = buildSimpleGraph();
        PathResult result = weighted.plan(graph, "A", "D");
        assertTrue(result.getTotalDistance() > 0);
        assertTrue(result.getPathList().size() >= 2);
    }

    @Test
    void shouldRejectInvalidScaleFactor() {
        assertThrows(IllegalArgumentException.class, () -> new AstarStrategy(0));
        assertThrows(IllegalArgumentException.class, () -> new AstarStrategy(-1));
        assertThrows(IllegalArgumentException.class, () -> new AstarStrategy(Double.NaN));
    }

    private CampusGraph buildSimpleGraph() {
        CampusGraph graph = new CampusGraph();
        Vertex a = new Vertex("A", "A", PlaceType.OTHER, 0, 0, "");
        Vertex b = new Vertex("B", "B", PlaceType.OTHER, 5, 0, "");
        Vertex c = new Vertex("C", "C", PlaceType.OTHER, 0, 12, "");
        Vertex d = new Vertex("D", "D", PlaceType.OTHER, 5, 10, "");
        for (Vertex v : new Vertex[]{a, b, c, d}) graph.addVertex(v);
        graph.addEdge(new Edge(a, b, 5.0, false, false, RoadType.PATH));
        graph.addEdge(new Edge(b, d, 5.0, false, false, RoadType.PATH));
        graph.addEdge(new Edge(a, c, 12.0, false, false, RoadType.PATH));
        graph.addEdge(new Edge(c, d, 11.0, false, false, RoadType.PATH));
        return graph;
    }
}
