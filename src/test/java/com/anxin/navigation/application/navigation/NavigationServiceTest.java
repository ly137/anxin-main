package com.anxin.navigation.application.navigation;

import com.anxin.navigation.domain.graph.CampusGraph;
import com.anxin.navigation.domain.model.Edge;
import com.anxin.navigation.domain.model.PathResult;
import com.anxin.navigation.domain.model.PlaceType;
import com.anxin.navigation.domain.model.RoadType;
import com.anxin.navigation.domain.model.Vertex;
import com.anxin.navigation.domain.planning.DijkstraStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NavigationServiceTest {

    @Test
    void shouldGenerateInstructionsAndEstimatedTime() {
        CampusGraph graph = createGraph();
        NavigationService service = new NavigationService(new DijkstraStrategy());

        PathResult result = service.navigate(graph, "A", "C", 100.0);

        Assertions.assertEquals(12.0, result.getTotalDistance());
        Assertions.assertEquals(0.12, result.getEstimatedTime(), 0.000001);
        Assertions.assertEquals(2, result.getSegmentDistances().size());
        Assertions.assertEquals(3, result.getNaviInstructions().size());
        Assertions.assertTrue(result.getNaviInstructions().get(0).contains("第1步"));
        Assertions.assertTrue(result.getNaviInstructions().get(2).contains("已到达目的地"));
    }

    @Test
    void shouldHandleStartEqualsEnd() {
        CampusGraph graph = createGraph();
        NavigationService service = new NavigationService(new DijkstraStrategy());

        PathResult result = service.navigate(graph, "A", "A");

        Assertions.assertEquals(0.0, result.getTotalDistance());
        Assertions.assertEquals(0.0, result.getEstimatedTime(), 0.000001);
        Assertions.assertTrue(result.getSegmentDistances().isEmpty());
        Assertions.assertEquals(1, result.getNaviInstructions().size());
    }

    @Test
    void shouldUseShuttleBusSpeedForInstructions() {
        CampusGraph graph = createGraph();
        NavigationService service = new NavigationService(new DijkstraStrategy());

        PathResult result = service.navigate(graph, "A", "C", NavigationService.SHUTTLE_BUS_SPEED_METERS_PER_MIN);

        Assertions.assertTrue(result.getNaviInstructions().get(0).contains("乘车"));
        Assertions.assertEquals(12.0 / NavigationService.SHUTTLE_BUS_SPEED_METERS_PER_MIN, result.getEstimatedTime(), 0.000001);
    }

    @Test
    void shouldUseWalkingLabelForWalkingSpeed() {
        CampusGraph graph = createGraph();
        NavigationService service = new NavigationService(new DijkstraStrategy());

        PathResult result = service.navigate(graph, "A", "C", NavigationService.WALKING_SPEED_METERS_PER_MIN);

        Assertions.assertTrue(result.getNaviInstructions().get(0).contains("步行"));
    }

    private CampusGraph createGraph() {
        CampusGraph graph = new CampusGraph();
        Vertex a = new Vertex("A", "East Gate", PlaceType.GATE, 0, 0, "");
        Vertex b = new Vertex("B", "Library", PlaceType.LIBRARY, 1, 1, "");
        Vertex c = new Vertex("C", "Teaching Building A", PlaceType.TEACHING_BUILDING, 2, 2, "");

        graph.addVertex(a);
        graph.addVertex(b);
        graph.addVertex(c);
        graph.addEdge(new Edge(a, b, 5, false, false, RoadType.MAIN_ROAD));
        graph.addEdge(new Edge(b, c, 7, false, false, RoadType.PATH));
        return graph;
    }
}
