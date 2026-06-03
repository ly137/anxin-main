package com.anxin.navigation.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RouteHistoryTest {
    @TempDir
    Path tempDir;

    @Test
    void shouldAddAndRetrieveEntries() {
        RouteHistory history = new RouteHistory(tempDir);
        history.addEntry("V001", "V002", "图书馆", "食堂");
        assertEquals(1, history.getEntries().size());
        RouteHistory.HistoryEntry e = history.getEntries().get(0);
        assertEquals("V001", e.startId);
        assertEquals("V002", e.endId);
        assertEquals("图书馆", e.startName);
        assertEquals("食堂", e.endName);
    }

    @Test
    void shouldLimitTo20Entries() {
        RouteHistory history = new RouteHistory(tempDir);
        for (int i = 0; i < 25; i++) {
            history.addEntry("S" + i, "E" + i, "start" + i, "end" + i);
        }
        assertEquals(20, history.getEntries().size());
        assertEquals("S24", history.getEntries().get(0).startId);
    }

    @Test
    void shouldDeduplicateEntries() {
        RouteHistory history = new RouteHistory(tempDir);
        history.addEntry("A", "B", "a", "b");
        history.addEntry("A", "B", "a", "b");
        assertEquals(1, history.getEntries().size());
    }

    @Test
    void shouldSaveAndLoadRoundTrip() {
        RouteHistory history = new RouteHistory(tempDir);
        history.addEntry("V1", "V2", "One", "Two");
        history.save();

        RouteHistory reloaded = new RouteHistory(tempDir);
        reloaded.load();
        assertEquals(1, reloaded.getEntries().size());
        assertEquals("One", reloaded.getEntries().get(0).startName);
    }

    @Test
    void shouldHandleEmptyLoad() {
        RouteHistory history = new RouteHistory(tempDir);
        history.load();
        assertTrue(history.getEntries().isEmpty());
    }
}
