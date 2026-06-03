package com.anxin.navigation.app;

import com.anxin.navigation.application.map.GraphConnectivityChecker;
import com.anxin.navigation.domain.graph.CampusGraph;
import com.anxin.navigation.gui.MainView;
import com.anxin.navigation.infrastructure.persistence.PersistenceService;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.nio.file.Paths;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setupLookAndFeel();
                launchGui();
            }
        });
    }

    private static void launchGui() {
        String dataDir = resolveDataDir();
        PersistenceService persistenceService = new PersistenceService(Paths.get(dataDir));
        CampusGraph graph = persistenceService.loadGraphOrDefault();
        persistenceService.loadUsersOrDefault();

        String warning = GraphConnectivityChecker.check(graph).toWarning();
        if (!warning.isEmpty()) {
            System.err.println(warning);
        }

        MainView mainView = new MainView(graph, persistenceService);
        mainView.setVisible(true);
    }

    private static String resolveDataDir() {
        String envValue = System.getenv("ANXIN_DATA_DIR");
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue.trim();
        }
        return "data";
    }

    private static void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignore) {
            // Keep default look-and-feel when system theme is unavailable.
        }
    }
}
