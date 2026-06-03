package com.anxin.navigation.gui.workbench;

import com.anxin.navigation.domain.model.PlaceType;
import com.anxin.navigation.domain.model.RoadType;

import java.awt.Color;

final class MapConfig {
    static final double MIN_ZOOM = 0.50;
    static final double MAX_ZOOM = 4.0;
    static final double ZOOM_STEP = 1.12;
    static final int VIEW_PADDING = 40;
    static final int WORLD_PADDING = 20;
    static final double GRID_SNAP_SIZE = 20.0;
    static final int GRID_SNAP_PIXELS = 10;
    static final int SNAP_VERTEX_PIXELS = 14;
    static final int AXIS_ALIGN_PIXELS = 10;
    static final double MOVE_HIT_THRESHOLD_PIXELS = 20.0;
    static final int PAN_VISIBLE_MIN_PIXELS = 36;
    static final double PAN_OVERSCROLL_FACTOR = 0.35;
    static final int FLASH_CYCLE_LIMIT = 8;
    static final int FLASH_INTERVAL_MS = 180;

    private MapConfig() {
    }

    static Color withOpacity(Color color, float opacity) {
        int alpha = Math.round(255 * Math.max(0.0f, Math.min(1.0f, opacity)));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    // 道路颜色 - 基于品牌色系，增强对比度与可读性
    static Color roadColor(RoadType roadType) {
        if (roadType == RoadType.MAIN_ROAD) {
            return new Color(37, 99, 235);   // #2563EB 主蓝色
        }
        if (roadType == RoadType.STAIRS) {
            return new Color(100, 116, 139); // #64748B 蓝灰色
        }
        return new Color(148, 163, 184);     // #94A3B8 浅蓝灰
    }

    static float roadWidth(RoadType roadType) {
        if (roadType == RoadType.MAIN_ROAD) {
            return 5.2f;
        }
        if (roadType == RoadType.STAIRS) {
            return 3.6f;
        }
        return 3.0f;
    }

    // 地点颜色 - 使用语义化现代色彩，增强类型辨识度
    static Color placeColor(PlaceType placeType) {
        if (placeType == PlaceType.GATE) {
            return new Color(37, 99, 235);   // #2563EB 蓝 - 入口
        }
        if (placeType == PlaceType.LIBRARY) {
            return new Color(16, 185, 129);  // #10B981 绿 - 知识
        }
        if (placeType == PlaceType.CANTEEN) {
            return new Color(245, 158, 11);  // #F59E0B 琥珀 - 饮食
        }
        if (placeType == PlaceType.TEACHING_BUILDING) {
            return new Color(139, 92, 246);  // #8B5CF6 紫 - 学术
        }
        if (placeType == PlaceType.DORMITORY) {
            return new Color(6, 182, 212);   // #06B6D4 青 - 居住
        }
        if (placeType == PlaceType.OFFICE) {
            return new Color(239, 68, 68);   // #EF4444 红 - 行政
        }
        if (placeType == PlaceType.SPORTS_CENTER) {
            return new Color(34, 197, 94);   // #22C55E 绿 - 运动
        }
        return new Color(100, 116, 139);     // #64748B 灰蓝 - 其他
    }
}
