package com.anxin.navigation.infrastructure.persistence;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RouteHistory {
    private static final int MAX_ENTRIES = 20;
    private static final String HISTORY_FILE = "routes-history.json";

    private final List<HistoryEntry> entries;
    private final Path dataDir;

    public RouteHistory(Path dataDir) {
        this.dataDir = dataDir;
        this.entries = new ArrayList<HistoryEntry>();
    }

    public List<HistoryEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public void addEntry(String startId, String endId, String startName, String endName) {
        entries.removeIf(e -> e.startId.equals(startId) && e.endId.equals(endId));
        entries.add(0, new HistoryEntry(startId, endId, startName, endName, System.currentTimeMillis()));
        while (entries.size() > MAX_ENTRIES) {
            entries.remove(entries.size() - 1);
        }
    }

    public void load() {
        entries.clear();
        try {
            Path file = dataDir.resolve(HISTORY_FILE);
            if (!file.toFile().exists()) {
                return;
            }
            String content = new String(java.nio.file.Files.readAllBytes(file), java.nio.charset.StandardCharsets.UTF_8);
            List<Map<String, Object>> raw = SimpleJson.parseArrayOfObjects(content);
            for (Map<String, Object> item : raw) {
                String startId = str(item, "startId");
                String endId = str(item, "endId");
                String startName = str(item, "startName");
                String endName = str(item, "endName");
                long ts = num(item, "timestamp");
                if (startId != null && endId != null) {
                    entries.add(new HistoryEntry(startId, endId, startName, endName, ts));
                }
            }
        } catch (Exception ignore) {
            // corrupt or missing history file is non-critical
        }
    }

    public void save() {
        try {
            List<Map<String, Object>> raw = new ArrayList<Map<String, Object>>();
            for (HistoryEntry e : entries) {
                java.util.LinkedHashMap<String, Object> map = new java.util.LinkedHashMap<String, Object>();
                map.put("startId", e.startId);
                map.put("endId", e.endId);
                map.put("startName", e.startName != null ? e.startName : "");
                map.put("endName", e.endName != null ? e.endName : "");
                map.put("timestamp", e.timestamp);
                raw.add(map);
            }
            String json = SimpleJson.toJsonArray(raw);
            Path file = dataDir.resolve(HISTORY_FILE);
            Path tmp = dataDir.resolve(HISTORY_FILE + ".tmp");
            java.nio.file.Files.write(tmp, json.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            try {
                java.nio.file.Files.move(tmp, file, java.nio.file.StandardCopyOption.ATOMIC_MOVE);
            } catch (java.nio.file.AtomicMoveNotSupportedException e) {
                java.nio.file.Files.move(tmp, file, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ignore) {
            // non-critical
        }
    }

    private static String str(Map<String, Object> m, String key) {
        Object v = m.get(key);
        return v != null ? v.toString() : null;
    }

    private static long num(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v instanceof Number) return ((Number) v).longValue();
        if (v instanceof String) {
            try { return Long.parseLong((String) v); } catch (NumberFormatException e) {}
        }
        return 0L;
    }

    public static final class HistoryEntry {
        public final String startId;
        public final String endId;
        public final String startName;
        public final String endName;
        public final long timestamp;

        HistoryEntry(String startId, String endId, String startName, String endName, long timestamp) {
            this.startId = startId;
            this.endId = endId;
            this.startName = startName;
            this.endName = endName;
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            String from = startName != null && !startName.isEmpty() ? startName : startId;
            String to = endName != null && !endName.isEmpty() ? endName : endId;
            return from + " → " + to;
        }
    }
}
