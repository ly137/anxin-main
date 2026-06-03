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


final class MainViewLayoutBuilder {
    private final MainView view;

    MainViewLayoutBuilder(MainView view) {
        this.view = view;
    }

    void initializeFrame() {
        view.setTitle("安心导航 - 校园智能路径规划系统");
        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        view.setMinimumSize(new Dimension(1440, 820));
        view.setSize(1600, 920);
        view.setLocationRelativeTo(null);
        view.setGlassPane(view.loadingOverlay);
    }

    void initializeLayout() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiStyles.PAGE_BACKGROUND);
        view.setContentPane(root);

        root.add(createTopBar(), BorderLayout.NORTH);

        JSplitPane shellSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createLeftNavigation(), createCenterWorkspace());
        shellSplitPane.setResizeWeight(0.0);
        shellSplitPane.setContinuousLayout(true);
        UiStyles.applySplitPaneStyle(shellSplitPane, 6);
        SwingUtilities.invokeLater(() -> shellSplitPane.setDividerLocation(MainView.LEFT_NAV_WIDTH + 12));

        root.add(shellSplitPane, BorderLayout.CENTER);
    }

    JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout(24, 0));
        topBar.setBackground(UiStyles.BRAND_900);
        topBar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        brandPanel.setOpaque(false);

        // Logo 徽章 - 更精致的品牌标识
        JPanel logoBadge = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        logoBadge.setOpaque(true);
        logoBadge.setBackground(UiStyles.PRIMARY_600);
        logoBadge.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        JLabel logoText = new JLabel("安");
        logoText.setFont(new Font(UiStyles.BODY_FONT.getFamily(), Font.BOLD, 16));
        logoText.setForeground(Color.WHITE);
        logoBadge.add(logoText);

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("安心导航");
        title.setFont(UiStyles.DISPLAY_FONT);
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("校园智能路径规划系统 · Campus Smart Routing");
        subtitle.setFont(UiStyles.CAPTION_FONT);
        subtitle.setForeground(UiStyles.TOP_BAR_SUBTLE_TEXT);

        titleBlock.add(title);
        titleBlock.add(Box.createVerticalStrut(4));
        titleBlock.add(subtitle);

        brandPanel.add(logoBadge);
        brandPanel.add(titleBlock);

        view.topModeBadgeLabel = new JLabel();
        view.topModeBadgeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        view.topModeDescriptionLabel = new JLabel();
        view.topModeDescriptionLabel.setFont(UiStyles.CAPTION_FONT);
        view.topModeDescriptionLabel.setForeground(UiStyles.TOP_BAR_SUBTLE_TEXT);
        view.topModeDescriptionLabel.setAlignmentX(java.awt.Component.RIGHT_ALIGNMENT);
        view.topModeDescriptionLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        view.topSessionBadgeLabel = new JLabel();
        view.topSessionBadgeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel timeLabel = new JLabel("启动 " + view.startupTimeText, SwingConstants.RIGHT);
        timeLabel.setFont(UiStyles.CAPTION_FONT);
        timeLabel.setForeground(UiStyles.TOP_BAR_SUBTLE_TEXT);
        timeLabel.setAlignmentX(java.awt.Component.RIGHT_ALIGNMENT);

        JPanel badgeRow = new JPanel();
        badgeRow.setOpaque(false);
        badgeRow.setLayout(new BoxLayout(badgeRow, BoxLayout.X_AXIS));
        badgeRow.setAlignmentX(java.awt.Component.RIGHT_ALIGNMENT);
        badgeRow.add(Box.createHorizontalGlue());
        badgeRow.add(view.topModeBadgeLabel);
        badgeRow.add(Box.createHorizontalStrut(8));
        badgeRow.add(view.topSessionBadgeLabel);

        view.topStatusPanel = new JPanel();
        view.topStatusPanel.setOpaque(false);
        view.topStatusPanel.setLayout(new BoxLayout(view.topStatusPanel, BoxLayout.Y_AXIS));
        view.topStatusPanel.add(badgeRow);
        view.topStatusPanel.add(Box.createVerticalStrut(6));
        view.topStatusPanel.add(view.topModeDescriptionLabel);
        view.topStatusPanel.add(Box.createVerticalStrut(2));
        view.topStatusPanel.add(timeLabel);

        topBar.add(brandPanel, BorderLayout.WEST);
        topBar.add(view.topStatusPanel, BorderLayout.EAST);
        view.updateShellHeaderState();
        return topBar;
    }

    JPanel createLeftNavigation() {
        JPanel shell = new JPanel(new BorderLayout());
        shell.setPreferredSize(new Dimension(MainView.LEFT_NAV_WIDTH, 0));
        shell.setMinimumSize(new Dimension(MainView.LEFT_NAV_MIN_WIDTH, 0));
        shell.setBackground(UiStyles.PAGE_BACKGROUND);
        shell.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 0));

        JPanel navigation = new ViewportFillPanel();
        navigation.setLayout(new BoxLayout(navigation, BoxLayout.Y_AXIS));
        navigation.setBackground(UiStyles.PAGE_BACKGROUND);
        navigation.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel modeRail = UiStyles.cardPanel();
        modeRail.setLayout(new BoxLayout(modeRail, BoxLayout.Y_AXIS));
        modeRail.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        modeRail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
        modeRail.add(view.createRailHeader("导航模式", "切换用户 / 管理员 / 系统视图"));
        modeRail.add(Box.createVerticalStrut(UiStyles.SPACE_12));

        for (AppRoute route : AppRoute.values()) {
            JButton navButton = new JButton("  " + route.getTitle());
            navButton.setFont(UiStyles.BODY_FONT);
            navButton.setFocusPainted(false);
            navButton.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
            navButton.setHorizontalAlignment(SwingConstants.LEFT);
            navButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            navButton.setPreferredSize(new Dimension(0, 42));
            navButton.addActionListener(e -> view.navigateTo(route));
            view.navButtons.put(route, navButton);
            view.applyNavigationButtonStyle(navButton, false);
            modeRail.add(navButton);
            modeRail.add(Box.createVerticalStrut(6));
        }

        navigation.add(modeRail);
        navigation.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(navigation);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        scrollPane.getViewport().setBackground(UiStyles.PAGE_BACKGROUND);

        shell.add(scrollPane, BorderLayout.CENTER);
        return shell;
    }

    JPanel createCenterWorkspace() {
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(UiStyles.PAGE_BACKGROUND);
        center.setMinimumSize(new Dimension(980, 0));

        view.mapWorkbenchView.registerRoutePanel(AppRoute.USER_MODE, createUserModeView());
        view.mapWorkbenchView.registerRoutePanel(AppRoute.ADMIN_MODE, createAdminModeView());
        view.mapWorkbenchView.registerRoutePanel(AppRoute.SYSTEM_SETTINGS, view.createSystemSettingsView());
        view.mapWorkbenchView.setMapContent(createMapOverlayContent());

        center.add(view.mapWorkbenchView, BorderLayout.CENTER);
        return center;
    }

    JLayeredPane createMapOverlayContent() {
        view.mapLayeredPane = new JLayeredPane();
        view.mapLayeredPane.setOpaque(true);
        view.mapLayeredPane.setLayout(null);
        view.mapLayeredPane.add(view.mapCanvas, JLayeredPane.DEFAULT_LAYER);

        view.mapOverlayToolbar = view.createMapOverlayToolbar();
        view.mapLayeredPane.add(view.mapOverlayToolbar, JLayeredPane.PALETTE_LAYER);
        view.mapLayeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                layoutMapOverlayComponents();
            }
        });
        SwingUtilities.invokeLater(view::layoutMapOverlayComponents);
        return view.mapLayeredPane;
    }

    void layoutMapOverlayComponents() {
        if (view.mapLayeredPane == null) {
            return;
        }
        int width = Math.max(0, view.mapLayeredPane.getWidth());
        int height = Math.max(0, view.mapLayeredPane.getHeight());
        view.mapCanvas.setBounds(0, 0, width, height);
        if (view.mapOverlayToolbar == null) {
            return;
        }
        Dimension preferred = view.mapOverlayToolbar.getPreferredSize();
        int availableHeight = Math.max(0, height - (MainView.MAP_OVERLAY_MARGIN * 2));
        int overlayWidth = preferred.width;
        int overlayHeight = Math.min(preferred.height, availableHeight);
        int x = Math.max(12, width - overlayWidth - MainView.MAP_OVERLAY_MARGIN);
        int y = MainView.MAP_OVERLAY_MARGIN;
        view.mapOverlayToolbar.setBounds(x, y, overlayWidth, overlayHeight);
        view.mapOverlayToolbar.revalidate();
        view.mapOverlayToolbar.repaint();
    }

    JPanel createUserModeView() {
        JPanel page = new JPanel(new GridBagLayout());
        page.setBackground(UiStyles.PAGE_BACKGROUND);
        page.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        view.pathQueryView = new PathQueryView();
        view.placeBrowseView = new PlaceBrowseView();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        gbc.weighty = 0;
        gbc.insets = new Insets(0, 0, 12, 0);
        page.add(view.pathQueryView, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        page.add(view.placeBrowseView, gbc);

        gbc.gridy = 2;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        page.add(filler, gbc);
        return page;
    }

    JPanel createAdminModeView() {
        JPanel page = new JPanel(new BorderLayout(12, 12));
        page.setBackground(UiStyles.PAGE_BACKGROUND);

        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actionBar.setOpaque(false);
        JButton loginButton = UiStyles.primaryButton("管理员登录");
        view.styleAdminHeaderButton(loginButton);
        loginButton.addActionListener(e -> {
            if (view.showAdminLoginDialog()) {
                view.refreshAllData();
            }
        });
        JButton logoutButton = UiStyles.secondaryButton("退出登录");
        view.styleAdminHeaderButton(logoutButton);
        logoutButton.addActionListener(e -> view.handleAdminLogout());
        actionBar.add(loginButton);
        actionBar.add(logoutButton);

        view.adminCardLayout = new CardLayout();
        view.adminCardPanel = new JPanel(view.adminCardLayout);
        view.adminCardPanel.setBackground(UiStyles.PAGE_BACKGROUND);
        view.adminCardPanel.add(createAdminLockedPanel(), "LOCKED");
        view.adminCardPanel.add(createAdminWorkspace(), "UNLOCKED");

        page.add(actionBar, BorderLayout.NORTH);
        page.add(view.adminCardPanel, BorderLayout.CENTER);
        return page;
    }

    JPanel createAdminLockedPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UiStyles.SURFACE_ALT);
        panel.setBorder(UiStyles.cardBorder());
        JLabel label = new JLabel("请先完成管理员登录，再进行地点/道路/禁行管理。", SwingConstants.CENTER);
        label.setFont(UiStyles.SUBTITLE_FONT);
        label.setForeground(UiStyles.TEXT_SECONDARY);
        panel.add(label);
        return panel;
    }

    JPanel createAdminWorkspace() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(UiStyles.PAGE_BACKGROUND);

        view.vertexManageView = new VertexManageView();
        view.roadManageView = new RoadManageView();
        view.forbiddenManageView = new ForbiddenManageView();
        view.overviewDashboardView = new OverviewDashboardView();

        JPanel sectionBar = UiStyles.cardPanel(new GridLayout(0, 2, 12, 12));
        sectionBar.setBackground(UiStyles.SURFACE_ALT);
        sectionBar.add(view.createAdminSectionButton(MainView.ADMIN_SECTION_VERTEX, "地点管理"));
        sectionBar.add(view.createAdminSectionButton(MainView.ADMIN_SECTION_ROAD, "道路管理"));
        sectionBar.add(view.createAdminSectionButton(MainView.ADMIN_SECTION_FORBIDDEN, "禁行管理"));
        sectionBar.add(view.createAdminSectionButton(MainView.ADMIN_SECTION_OVERVIEW, "地图概览"));

        JPanel workspaceHeader = new JPanel(new BorderLayout(0, 8));
        workspaceHeader.setBackground(UiStyles.PAGE_BACKGROUND);
        workspaceHeader.add(sectionBar, BorderLayout.NORTH);

        view.adminWorkspaceLayout = new CardLayout();
        view.adminWorkspacePanel = new JPanel(view.adminWorkspaceLayout);
        view.adminWorkspacePanel.setBackground(UiStyles.PAGE_BACKGROUND);
        view.adminWorkspacePanel.add(view.vertexManageView, MainView.ADMIN_SECTION_VERTEX);
        view.adminWorkspacePanel.add(view.roadManageView, MainView.ADMIN_SECTION_ROAD);
        view.adminWorkspacePanel.add(view.forbiddenManageView, MainView.ADMIN_SECTION_FORBIDDEN);
        view.adminWorkspacePanel.add(view.overviewDashboardView, MainView.ADMIN_SECTION_OVERVIEW);

        panel.add(workspaceHeader, BorderLayout.NORTH);
        panel.add(view.adminWorkspacePanel, BorderLayout.CENTER);

        view.showAdminSection(MainView.ADMIN_SECTION_VERTEX);
        view.setAdminEditMode(EditToolMode.SELECT);
        view.refreshUndoRedoButtons();
        return panel;
    }

}
