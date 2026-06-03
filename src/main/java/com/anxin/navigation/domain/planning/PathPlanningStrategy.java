package com.anxin.navigation.domain.planning;

import com.anxin.navigation.domain.graph.CampusGraph;
import com.anxin.navigation.domain.model.PathResult;

public interface PathPlanningStrategy {
    PathResult plan(CampusGraph graph, String startId, String endId);
}

