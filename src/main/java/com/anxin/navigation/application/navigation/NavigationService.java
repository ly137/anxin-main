package com.anxin.navigation.application.navigation;

import com.anxin.navigation.domain.graph.CampusGraph;
import com.anxin.navigation.domain.model.PathResult;
import com.anxin.navigation.domain.model.Vertex;
import com.anxin.navigation.domain.planning.PathPlanningStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NavigationService {
    public static final double WALKING_SPEED_METERS_PER_MIN = 75.0;
    public static final double SHUTTLE_BUS_SPEED_METERS_PER_MIN = 300.0;

    private volatile PathPlanningStrategy strategy;

    public NavigationService(PathPlanningStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy, "strategy must not be null");
    }

    public PathResult navigate(CampusGraph graph, String startId, String endId) {
        return navigate(graph, startId, endId, WALKING_SPEED_METERS_PER_MIN);
    }

    public PathResult navigate(CampusGraph graph, String startId, String endId, double speedMetersPerMinute) {
        PathResult rawPath = strategy.plan(graph, startId, endId);
        return enrich(rawPath, speedMetersPerMinute);
    }

    public PathResult enrich(PathResult rawPath, double speedMetersPerMinute) {
        Objects.requireNonNull(rawPath, "rawPath must not be null");
        double estimatedTime = rawPath.getTotalDistance() / speedMetersPerMinute;
        List<String> instructions = buildInstructions(rawPath, speedMetersPerMinute);
        return new PathResult(
                rawPath.getStartVertex(),
                rawPath.getEndVertex(),
                rawPath.getPathList(),
                rawPath.getTotalDistance(),
                estimatedTime,
                rawPath.getSegmentDistances(),
                instructions
        );
    }

    public void setStrategy(PathPlanningStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy, "strategy must not be null");
    }

    public String getStrategyName() {
        return strategy.getClass().getSimpleName();
    }

    public List<String> buildInstructions(PathResult pathResult, double speedMetersPerMinute) {
        Objects.requireNonNull(pathResult, "pathResult must not be null");
        List<Vertex> vertices = pathResult.getPathList();
        List<Double> segmentDistances = pathResult.getSegmentDistances();

        String modeLabel = speedMetersPerMinute >= SHUTTLE_BUS_SPEED_METERS_PER_MIN ? "乘车" : "步行";

        if (vertices.size() == 1) {
            List<String> single = new ArrayList<String>(1);
            single.add("已在目的地，无需移动。");
            return single;
        }

        List<String> instructions = new ArrayList<String>();
        for (int i = 1; i < vertices.size(); i++) {
            Vertex from = vertices.get(i - 1);
            Vertex to = vertices.get(i);
            double distance = segmentDistances.get(i - 1);
            instructions.add("第" + i + "步：从 " + from.getName() + " 前往 " + to.getName()
                    + "，" + modeLabel + "约 " + Math.round(distance) + " 米。");
        }
        instructions.add("已到达目的地：" + pathResult.getEndVertex().getName() + "。");
        return instructions;
    }
}

