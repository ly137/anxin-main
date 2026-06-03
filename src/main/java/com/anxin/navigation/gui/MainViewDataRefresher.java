package com.anxin.navigation.gui;

import com.anxin.navigation.application.auth.AuthService;
import com.anxin.navigation.application.map.MapService;
import com.anxin.navigation.application.navigation.ConsolePathFormatter;
import com.anxin.navigation.application.navigation.NavigationService;
import com.anxin.navigation.domain.graph.CampusGraph;
import com.anxin.navigation.domain.model.Admin;
import com.anxin.navigation.domain.model.Edge;
import com.anxin.navigation.domain.model.PathResult;
import com.anxin.navigation.domain.model.PlaceType;
import com.anxin.navigation.domain.model.RoadType;
import com.anxin.navigation.domain.model.Vertex;
import com.anxin.navigation.domain.planning.DijkstraStrategy;
import com.anxin.navigation.gui.components.AdminLoginDialog;
import com.anxin.navigation.gui.components.LoadingOverlay;
import com.anxin.navigation.gui.components.ViewportFillPanel;
import com.anxin.navigation.gui.controller.AuthController;
import com.anxin.navigation.gui.controller.MapController;
import com.anxin.navigation.gui.controller.NavigationController;
import com.anxin.navigation.gui.model.OverviewData;
import com.anxin.navigation.gui.model.RoadOption;
import com.anxin.navigation.gui.model.RouteVisualizationDto;
import com.anxin.navigation.gui.model.VertexOption;
import com.anxin.navigation.gui.routing.AppRoute;
import com.anxin.navigation.gui.styles.UiStyles;
import com.anxin.navigation.gui.view.ForbiddenManageView;
import com.anxin.navigation.gui.view.OverviewDashboardView;
import com.anxin.navigation.gui.view.PathQueryView;
import com.anxin.navigation.gui.view.PlaceBrowseView;
import com.anxin.navigation.gui.view.RoadManageView;
import com.anxin.navigation.gui.view.VertexManageView;
import com.anxin.navigation.gui.workbench.EditToolMode;
import com.anxin.navigation.gui.workbench.MapCanvas;
import com.anxin.navigation.gui.workbench.MapWorkbenchView;
import com.anxin.navigation.gui.workbench.WorkbenchFeedback;
import com.anxin.navigation.gui.workbench.command.CommandBus;
import com.anxin.navigation.gui.workbench.state.MapViewState;
import com.anxin.navigation.infrastructure.persistence.PersistenceService;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JLayeredPane;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.imageio.ImageIO;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


final class MainViewDataRefresher {
    private final MainView view;

    MainViewDataRefresher(MainView view) {
        this.view = view;
    }

    void handlePathQuery(String startId, String endId, PathQueryView.TransportMode transportMode) {
        if (MainView.isBlank(startId) || MainView.isBlank(endId)) {
            view.feedback.showErrorDialog("参数缺失", "请先选择起点与终点。");
            return;
        }
        try {
            boolean isWalking = transportMode == PathQueryView.TransportMode.WALKING;
            double speed = isWalking ? NavigationService.WALKING_SPEED_METERS_PER_MIN : NavigationService.SHUTTLE_BUS_SPEED_METERS_PER_MIN;
            String transportLabel = transportMode.getLabel();

            view.feedback.showLoading("正在计算最短路径...");
            NavigationController.NavigationVisualResult result = view.navigationController.queryPathVisual(startId, endId, speed);
            PathResult pathResult = result.getPathResult();
            view.pathQueryView.setResultContent(view.navigationController.format(pathResult, transportLabel));
            view.pathQueryView.setInstructions(pathResult.getNaviInstructions());
            if (view.viewState.getCurrentPathResult() == null) {
                view.viewState.setPreviousRouteVisualization(null);
            } else {
                view.viewState.setPreviousRouteVisualization(view.navigationController.toTraceRouteVisualization(view.viewState.getCurrentPathResult()));
            }
            view.viewState.setCurrentPathResult(pathResult);
            view.viewState.setCurrentRouteVisualization(result.getRouteVisualization());
            view.mapCanvas.setRouteComparison(view.viewState.getCurrentRouteVisualization(), view.viewState.getPreviousRouteVisualization());
            view.feedback.success("路径查询成功。");
            view.feedback.setStatus("用户模式: 路径查询完成");
        } catch (RuntimeException ex) {
            view.feedback.showOperationError("查询失败", ex);
            view.feedback.setStatus("用户模式: 路径查询失败");
        } finally {
            view.feedback.hideLoading();
        }
    }

    void refreshAllData() {
        refreshMapCanvas();
        refreshPathOptions();
        refreshPlaceData(view.placeBrowseView.selectedType());
        refreshVertexData();
        refreshRoadData();
        refreshForbiddenData();
        refreshOverviewData();
        view.updateAdminAccessUi();
        view.refreshUndoRedoButtons();
        if (view.activeRoute == AppRoute.ADMIN_MODE) {
            view.handleAdminMapSelectionContext();
        }
    }

    void refreshPathOptions() {
        String selectedStart = view.pathQueryView.selectedStartId();
        String selectedEnd = view.pathQueryView.selectedEndId();
        List<VertexOption> options = view.mapController.listVertexOptions();
        view.pathQueryView.setVertexOptions(options, selectedStart, selectedEnd);
    }

    void refreshMapCanvas() {
        List<Vertex> vertices = view.mapController.listVertices();
        List<Edge> roads = view.mapController.listRoads();
        view.mapCanvas.setGraphData(vertices, roads);
        view.mapCanvas.setRouteComparison(view.viewState.getCurrentRouteVisualization(), view.viewState.getPreviousRouteVisualization());
    }

    void refreshPlaceData(String selectedType) {
        List<com.anxin.navigation.domain.model.Vertex> vertices;
        if (PlaceBrowseView.ALL_PLACE_TYPES.equals(selectedType)) {
            vertices = view.mapController.listVertices();
        } else {
            vertices = view.mapController.listVerticesByType(PlaceType.valueOf(selectedType));
        }
        view.placeBrowseView.setPlaces(vertices);
    }

    void refreshVertexData() {
        view.vertexManageView.setVertices(view.mapController.listVertices());
    }

    void refreshRoadData() {
        String selectedFrom = view.roadManageView.selectedFromId();
        String selectedTo = view.roadManageView.selectedToId();
        view.roadManageView.setVertexOptions(view.mapController.listVertexOptions(), selectedFrom, selectedTo);
        view.roadManageView.setRoads(view.mapController.listRoads());
    }

    void refreshForbiddenData() {
        String selectedKey = view.forbiddenManageView.selectedRoadKey();
        view.forbiddenManageView.setRoadOptions(view.mapController.listRoadOptions(), selectedKey);
    }

    void refreshOverviewData() {
        OverviewData overview = view.mapController.loadOverview();
        view.overviewDashboardView.setOverviewData(overview);
    }

}
