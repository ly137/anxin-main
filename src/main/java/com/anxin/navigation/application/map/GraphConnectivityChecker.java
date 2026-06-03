package com.anxin.navigation.application.map;

import com.anxin.navigation.domain.graph.CampusGraph;
import com.anxin.navigation.domain.model.Edge;
import com.anxin.navigation.domain.model.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class GraphConnectivityChecker {

    private GraphConnectivityChecker() {
    }

    public static ConnectivityReport check(CampusGraph graph) {
        if (graph == null) {
            return new ConnectivityReport(true, Collections.<Set<Vertex>>emptyList());
        }

        List<Vertex> allVertices = new ArrayList<Vertex>(graph.getAllVertices());
        if (allVertices.isEmpty()) {
            return new ConnectivityReport(true, Collections.<Set<Vertex>>emptyList());
        }

        Set<String> visited = new HashSet<String>();
        List<Set<Vertex>> components = new ArrayList<Set<Vertex>>();

        for (Vertex v : allVertices) {
            if (!visited.contains(v.getId())) {
                Set<Vertex> component = new LinkedHashSet<Vertex>();
                dfsUndirected(graph, v.getId(), visited, component);
                components.add(component);
            }
        }

        boolean fullyConnected = components.size() <= 1;
        return new ConnectivityReport(fullyConnected, components);
    }

    private static void dfsUndirected(CampusGraph graph, String vertexId, Set<String> visited, Set<Vertex> component) {
        visited.add(vertexId);
        component.add(graph.getVertex(vertexId));

        for (Edge edge : graph.getNeighbors(vertexId)) {
            String neighborId = edge.getToVertex().getId();
            if (!visited.contains(neighborId)) {
                dfsUndirected(graph, neighborId, visited, component);
            }
        }
    }

    public static final class ConnectivityReport {
        public final boolean fullyConnected;
        public final List<Set<Vertex>> components;

        ConnectivityReport(boolean fullyConnected, List<Set<Vertex>> components) {
            this.fullyConnected = fullyConnected;
            this.components = components != null ? Collections.unmodifiableList(components) : Collections.<Set<Vertex>>emptyList();
        }

        public String toWarning() {
            if (fullyConnected) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("[数据完整性警告] 校园地图不是全连通的！共 ").append(components.size()).append(" 个孤立区域:\n");
            for (int i = 0; i < components.size(); i++) {
                sb.append("  区域").append(i + 1).append(" (").append(components.get(i).size()).append(" 个地点): ");
                List<String> names = new ArrayList<String>();
                for (Vertex v : components.get(i)) {
                    names.add(v.getName());
                }
                sb.append(String.join(", ", names));
                sb.append('\n');
            }
            sb.append("请检查道路数据确保所有地点可达。");
            return sb.toString();
        }
    }
}
