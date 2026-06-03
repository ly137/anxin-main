package com.anxin.navigation.gui.styles;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.JTableHeader;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;

/**
 * 安心导航设计系统 - Swiss Modernism 2.0 风格 + Soft UI Evolution
 * 设计理念：清晰层级、数学比例间距、高对比度可读性、微妙深度阴影
 */
public final class UiStyles {
    // ==================== 间距系统 (8px 基准网格) ====================
    public static final int SPACE_4 = 4;
    public static final int SPACE_8 = 8;
    public static final int SPACE_12 = 12;
    public static final int SPACE_16 = 16;
    public static final int SPACE_20 = 20;
    public static final int SPACE_24 = 24;
    public static final int SPACE_32 = 32;
    public static final int SPACE_48 = 48;

    // ==================== 圆角系统 ====================
    public static final int RADIUS_SM = 6;
    public static final int RADIUS_MD = 10;
    public static final int RADIUS_LG = 14;
    public static final int RADIUS_XL = 20;

    // ==================== 品牌色系 (Deep Blue - 专业稳重) ====================
    public static final Color BRAND_900 = new Color(15, 23, 42);   // #0F172A 最深蓝黑
    public static final Color BRAND_800 = new Color(30, 41, 59);   // #1E293B
    public static final Color BRAND_700 = new Color(51, 65, 85);   // #334155
    public static final Color BRAND_600 = new Color(71, 85, 105);  // #475569
    public static final Color BRAND_500 = new Color(100, 116, 139); // #64748B
    public static final Color BRAND_100 = new Color(241, 245, 249); // #F1F5F9 最浅灰蓝

    // ==================== 主色调 (Primary Blue) ====================
    public static final Color PRIMARY_700 = new Color(29, 78, 216);  // #1D4ED8
    public static final Color PRIMARY_600 = new Color(37, 99, 235);  // #2563EB
    public static final Color PRIMARY_500 = new Color(59, 130, 246); // #3B82F6
    public static final Color PRIMARY_400 = new Color(96, 165, 250); // #60A5FA
    public static final Color PRIMARY_100 = new Color(219, 234, 254); // #DBEAFE
    public static final Color PRIMARY_50 = new Color(239, 246, 255);  // #EFF6FF

    // ==================== 强调色 (Accent Orange) ====================
    public static final Color ACCENT_600 = new Color(234, 88, 12);  // #EA580C
    public static final Color ACCENT_500 = new Color(249, 115, 22); // #F97316
    public static final Color ACCENT_100 = new Color(255, 237, 213); // #FFEDD5

    // ==================== 背景色 ====================
    public static final Color BACKGROUND = new Color(248, 250, 252); // #F8FAFC
    public static final Color SURFACE = Color.WHITE;
    public static final Color SURFACE_ALT = new Color(250, 250, 250); // #FAFAFA
    public static final Color SURFACE_SUBTLE = new Color(245, 247, 250); // #F5F7FA

    // ==================== 边框色 ====================
    public static final Color BORDER = new Color(226, 232, 240);  // #E2E8F0
    public static final Color BORDER_STRONG = new Color(203, 213, 225); // #CBD5E1

    // ==================== 文本色 (高对比度 WCAG AA+) ====================
    public static final Color TEXT_PRIMARY = new Color(15, 23, 42);   // #0F172A 主文字
    public static final Color TEXT_SECONDARY = new Color(71, 85, 105); // #475569 辅助文字
    public static final Color TEXT_TERTIARY = new Color(148, 163, 184); // #94A3B8 三级文字
    public static final Color TEXT_ON_BRAND = new Color(255, 255, 255); // 品牌色上的白色文字

    // ==================== 语义色 ====================
    public static final Color SUCCESS = new Color(16, 185, 129);     // #10B981
    public static final Color SUCCESS_SOFT = new Color(236, 253, 245); // #ECFDF5
    public static final Color WARNING = new Color(245, 158, 11);     // #F59E0B
    public static final Color WARNING_SOFT = new Color(255, 251, 235); // #FFFBEB
    public static final Color ERROR = new Color(239, 68, 68);        // #EF4444
    public static final Color ERROR_SOFT = new Color(254, 242, 242); // #FEF2F2
    public static final Color INFO = new Color(37, 99, 235);         // #2563EB
    public static final Color INFO_SOFT = new Color(239, 246, 255);  // #EFF6FF

    // ==================== 特殊色 ====================
    public static final Color ROUTE_HIGHLIGHT = new Color(249, 115, 22); // #F97316 路线高亮橙色
    public static final Color TOP_BAR_SUBTLE_TEXT = new Color(203, 213, 225); // 顶栏副文字
    public static final Color OVERLAY_PANEL_BACKGROUND = new Color(255, 255, 255, 245);
    public static final Color PALETTE_BUTTON_BACKGROUND = new Color(248, 250, 252);
    public static final Color PALETTE_BUTTON_BORDER = new Color(226, 232, 240);
    public static final Color PALETTE_BUTTON_TEXT = new Color(30, 41, 59);
    public static final Color PALETTE_BUTTON_ACTIVE_BACKGROUND = new Color(219, 234, 254);
    public static final Color PALETTE_BUTTON_ACTIVE = new Color(29, 78, 216);

    // ==================== 兼容别名 ====================
    public static final Color PRIMARY = PRIMARY_600;
    public static final Color PRIMARY_SOFT = PRIMARY_100;
    public static final Color PAGE_BACKGROUND = BACKGROUND;
    public static final Color PANEL_BACKGROUND = SURFACE;

    // ==================== 字体系统 (现代专业) ====================
    private static final String FONT_FAMILY = resolveFontFamily();
    public static final Font DISPLAY_FONT = new Font(FONT_FAMILY, Font.BOLD, 24);
    public static final Font TITLE_FONT = new Font(FONT_FAMILY, Font.BOLD, 18);
    public static final Font SUBTITLE_FONT = new Font(FONT_FAMILY, Font.BOLD, 13);
    public static final Font BODY_FONT = new Font(FONT_FAMILY, Font.PLAIN, 13);
    public static final Font BODY_BOLD_FONT = new Font(FONT_FAMILY, Font.BOLD, 13);
    public static final Font CAPTION_FONT = new Font(FONT_FAMILY, Font.PLAIN, 11);
    public static final Font METRIC_FONT = new Font(FONT_FAMILY, Font.BOLD, 24);
    public static final Font SMALL_METRIC_FONT = new Font(FONT_FAMILY, Font.BOLD, 16);
    public static final Font PALETTE_BUTTON_FONT = new Font(FONT_FAMILY, Font.BOLD, 14);
    public static final Font PALETTE_BUTTON_ACTIVE_FONT = new Font(FONT_FAMILY, Font.BOLD, 14);

    // ==================== 组件尺寸常量 ====================
    public static final int PALETTE_BUTTON_SIZE = 42;
    public static final int ADMIN_SECTION_BUTTON_HEIGHT = 40;
    public static final int ADMIN_HEADER_BUTTON_WIDTH = 128;
    public static final int ADMIN_HEADER_BUTTON_HEIGHT = 36;
    public static final int MAP_CANVAS_BACKGROUND_RED = 248;
    public static final int MAP_CANVAS_BACKGROUND_GREEN = 250;
    public static final int MAP_CANVAS_BACKGROUND_BLUE = 252;

    // ==================== 阴影颜色常量 (用于模拟 shadow-clay 效果) ====================
    public static final Color SHADOW_BORDER = new Color(226, 232, 240);
    public static final Color CARD_HEADER_BG = new Color(249, 250, 251);

    /**
     * 选择最佳可用字体，优先使用系统现代无衬线字体
     */
    private static String resolveFontFamily() {
        String[] candidates = {
            "Microsoft YaHei UI",
            "PingFang SC",
            "Noto Sans SC",
            "Microsoft YaHei",
            "Segoe UI",
            "Inter",
            Font.SANS_SERIF
        };
        for (String candidate : candidates) {
            Font testFont = new Font(candidate, Font.PLAIN, 13);
            if (testFont.getFamily().equals(candidate) || testFont.canDisplay('中')) {
                return candidate;
            }
        }
        return Font.SANS_SERIF;
    }

    private UiStyles() {
    }

    /**
     * 安装全局 UI 默认值，覆盖系统 L&F 以保持视觉一致性
     */
    public static void installDefaults() {
        UIManager.put("Panel.background", PAGE_BACKGROUND);
        UIManager.put("Label.font", BODY_FONT);
        UIManager.put("Label.foreground", TEXT_PRIMARY);
        UIManager.put("Button.font", BODY_FONT);
        UIManager.put("Button.background", SURFACE);
        UIManager.put("Button.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.font", BODY_FONT);
        UIManager.put("TextField.background", SURFACE);
        UIManager.put("TextField.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground", PRIMARY_600);
        UIManager.put("TextField.border", inputBorder());
        UIManager.put("ComboBox.font", BODY_FONT);
        UIManager.put("ComboBox.background", SURFACE);
        UIManager.put("ComboBox.foreground", TEXT_PRIMARY);
        UIManager.put("ComboBox.border", inputBorder());
        UIManager.put("CheckBox.font", BODY_FONT);
        UIManager.put("CheckBox.foreground", TEXT_PRIMARY);
        UIManager.put("Table.font", BODY_FONT);
        UIManager.put("Table.background", SURFACE);
        UIManager.put("Table.foreground", TEXT_PRIMARY);
        UIManager.put("Table.gridColor", BORDER);
        UIManager.put("Table.selectionBackground", PRIMARY_50);
        UIManager.put("Table.selectionForeground", TEXT_PRIMARY);
        UIManager.put("TableHeader.font", SUBTITLE_FONT);
        UIManager.put("TableHeader.background", SURFACE_SUBTLE);
        UIManager.put("TableHeader.foreground", TEXT_PRIMARY);
    }

    /**
     * 分组边框 - 用于表单分区
     */
    public static Border sectionBorder(String title) {
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                title
        );
        titledBorder.setTitleFont(SUBTITLE_FONT);
        titledBorder.setTitleColor(TEXT_SECONDARY);
        titledBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        return BorderFactory.createCompoundBorder(
                titledBorder,
                BorderFactory.createEmptyBorder(SPACE_12, SPACE_12, SPACE_12, SPACE_12)
        );
    }

    /**
     * 标准卡片边框 (1px 细线 + 内边距)
     */
    public static Border cardBorder() {
        return cardBorder(SPACE_16);
    }

    public static Border cardBorder(int padding) {
        return cardBorder(padding, padding, padding, padding);
    }

    public static Border cardBorder(int top, int left, int bottom, int right) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(top, left, bottom, right)
        );
    }

    /**
     * 柔和卡片边框 - 更浅的边框色
     */
    public static Border softCardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SURFACE_SUBTLE),
                BorderFactory.createEmptyBorder(SPACE_12, SPACE_12, SPACE_12, SPACE_12)
        );
    }

    /**
     * 高亮卡片边框 - 带阴影效果模拟的卡片
     */
    public static Border elevatedCardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SHADOW_BORDER),
                BorderFactory.createEmptyBorder(SPACE_16, SPACE_16, SPACE_16, SPACE_16)
        );
    }

    public static JPanel cardPanel() {
        return createCardPanel(null, cardBorder());
    }

    public static JPanel cardPanel(LayoutManager layout) {
        return createCardPanel(layout, cardBorder());
    }

    public static JPanel softCardPanel(LayoutManager layout) {
        JPanel panel = createCardPanel(layout, softCardBorder());
        panel.setBackground(SURFACE_ALT);
        return panel;
    }

    public static JButton primaryButton(String text) {
        JButton button = new JButton(text);
        applyPrimaryButtonStyle(button);
        return button;
    }

    public static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        applySecondaryButtonStyle(button);
        return button;
    }

    public static JButton dangerButton(String text) {
        JButton button = new JButton(text);
        applyDangerButtonStyle(button);
        return button;
    }

    public static JButton successButton(String text) {
        JButton button = new JButton(text);
        applySuccessButtonStyle(button);
        return button;
    }

    public static JButton ghostButton(String text) {
        JButton button = new JButton(text);
        applyGhostButtonStyle(button);
        return button;
    }

    /**
     * 主要按钮 (Filled Blue)
     */
    public static void applyPrimaryButtonStyle(JButton button) {
        applyFilledButtonStyle(button, PRIMARY_600, Color.WHITE);
    }

    /**
     * 次要按钮 (Outlined)
     */
    public static void applySecondaryButtonStyle(JButton button) {
        applyOutlinedButtonStyle(button, TEXT_PRIMARY, SURFACE, BORDER);
    }

    /**
     * 危险操作按钮 (Filled Red)
     */
    public static void applyDangerButtonStyle(JButton button) {
        applyFilledButtonStyle(button, ERROR, Color.WHITE);
    }

    /**
     * 成功操作按钮 (Filled Green)
     */
    public static void applySuccessButtonStyle(JButton button) {
        applyFilledButtonStyle(button, SUCCESS, Color.WHITE);
    }

    /**
     * 幽灵按钮 (透明背景 + 主色文字)
     */
    public static void applyGhostButtonStyle(JButton button) {
        applyOutlinedButtonStyle(button, PRIMARY_600, SURFACE_ALT, SURFACE_ALT);
        button.setBorder(BorderFactory.createEmptyBorder(SPACE_8, SPACE_16, SPACE_8, SPACE_16));
    }

    /**
     * 强调按钮 (Filled Orange Accent)
     */
    public static void applyAccentButtonStyle(JButton button) {
        applyFilledButtonStyle(button, ACCENT_500, Color.WHITE);
    }

    public static JTextField formField(int columns) {
        JTextField field = new JTextField(columns);
        applyTextFieldStyle(field);
        return field;
    }

    public static void applyTextFieldStyle(JTextField field) {
        field.setFont(BODY_FONT);
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(SURFACE);
        field.setCaretColor(TEXT_PRIMARY);
        field.setBorder(inputBorder());
    }

    public static <T> JComboBox<T> formComboBox() {
        JComboBox<T> comboBox = new JComboBox<T>();
        applyComboBoxStyle(comboBox);
        return comboBox;
    }

    public static <T> JComboBox<T> formComboBox(T[] items) {
        JComboBox<T> comboBox = new JComboBox<T>(items);
        applyComboBoxStyle(comboBox);
        return comboBox;
    }

    public static void applyComboBoxStyle(JComboBox<?> comboBox) {
        comboBox.setFont(BODY_FONT);
        comboBox.setForeground(TEXT_PRIMARY);
        comboBox.setBackground(SURFACE);
        comboBox.setBorder(inputBorder());
        comboBox.setMaximumRowCount(10);
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                String text = value == null ? "" : value.toString();
                label.setText(text);
                label.setToolTipText(text);
                label.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
                return label;
            }
        });
        installComboPopupWidthBehavior(comboBox);
    }

    public static JCheckBox formCheckBox(String text) {
        JCheckBox checkBox = new JCheckBox(text);
        applyCheckBoxStyle(checkBox);
        return checkBox;
    }

    public static void applyCheckBoxStyle(AbstractButton checkBox) {
        checkBox.setFont(BODY_FONT);
        checkBox.setForeground(TEXT_PRIMARY);
        checkBox.setFocusPainted(false);
        checkBox.setOpaque(false);
        checkBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkBox.setIconTextGap(SPACE_8);
    }

    public static JLabel formLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(BODY_FONT);
        label.setForeground(TEXT_PRIMARY);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    public static JLabel captionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(CAPTION_FONT);
        label.setForeground(TEXT_SECONDARY);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    public static JLabel metricLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(METRIC_FONT);
        label.setForeground(TEXT_PRIMARY);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    public static JTable standardTable(Object[][] data, Object[] headers) {
        JTable table = new JTable(data, headers);
        applyTableStyle(table);
        return table;
    }

    public static void applyTableStyle(JTable table) {
        table.setFillsViewportHeight(true);
        table.setRowHeight(36);
        table.setGridColor(BORDER);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(PRIMARY_50);
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setBackground(SURFACE);
        table.setForeground(TEXT_PRIMARY);
        table.setFont(BODY_FONT);
        table.setBorder(BorderFactory.createEmptyBorder());

        JTableHeader header = table.getTableHeader();
        if (header != null) {
            header.setFont(SUBTITLE_FONT);
            header.setBackground(SURFACE_SUBTLE);
            header.setForeground(TEXT_PRIMARY);
            header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_STRONG));
            header.setPreferredSize(new Dimension(header.getPreferredSize().width, 36));
            header.setReorderingAllowed(false);
        }
    }

    public static void applyTableScrollPaneStyle(JScrollPane scrollPane) {
        if (scrollPane == null) {
            return;
        }
        scrollPane.setBackground(SURFACE);
        if (scrollPane.getViewport() != null) {
            scrollPane.getViewport().setBackground(SURFACE);
        }
    }

    public static void applySplitPaneStyle(JSplitPane splitPane, int dividerSize) {
        if (splitPane == null) {
            return;
        }
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setOpaque(false);
        splitPane.setOneTouchExpandable(false);
        splitPane.setDividerSize(dividerSize);
        splitPane.setBackground(PAGE_BACKGROUND);
        splitPane.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                BasicSplitPaneDivider divider = new BasicSplitPaneDivider(this) {
                    @Override
                    public void setBorder(Border border) {
                    }
                };
                divider.setBackground(PAGE_BACKGROUND);
                return divider;
            }
        });
    }

    private static JPanel createCardPanel(LayoutManager layout, Border border) {
        JPanel panel = layout == null ? new JPanel() : new JPanel(layout);
        panel.setOpaque(true);
        panel.setBackground(PANEL_BACKGROUND);
        panel.setBorder(border);
        return panel;
    }

    private static void installComboPopupWidthBehavior(JComboBox<?> comboBox) {
        if (Boolean.TRUE.equals(comboBox.getClientProperty("anxin.combo.popup.sized"))) {
            return;
        }
        comboBox.putClientProperty("anxin.combo.popup.sized", Boolean.TRUE);
        comboBox.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                Object child = comboBox.getUI().getAccessibleChild(comboBox, 0);
                if (!(child instanceof BasicComboPopup)) {
                    return;
                }
                BasicComboPopup popup = (BasicComboPopup) child;
                int popupWidth = Math.max(comboBox.getWidth(), 220);
                Dimension preferredSize = popup.getPreferredSize();
                popup.setPreferredSize(new Dimension(popupWidth, preferredSize.height));
                popup.setPopupSize(popupWidth, preferredSize.height);
                if (popup.getComponentCount() <= 0 || !(popup.getComponent(0) instanceof JScrollPane)) {
                    return;
                }
                JScrollPane scrollPane = (JScrollPane) popup.getComponent(0);
                scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setPreferredSize(new Dimension(popupWidth, preferredSize.height));
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
    }

    private static void applyFilledButtonStyle(JButton button, Color background, Color foreground) {
        applyBaseButtonStyle(button);
        button.setForeground(foreground);
        button.setBackground(background);
        button.setBorder(BorderFactory.createEmptyBorder(SPACE_8, SPACE_20, SPACE_8, SPACE_20));
        button.setBorderPainted(false);
    }

    private static void applyOutlinedButtonStyle(JButton button, Color foreground, Color background, Color borderColor) {
        applyBaseButtonStyle(button);
        button.setForeground(foreground);
        button.setBackground(background);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                BorderFactory.createEmptyBorder(SPACE_8, SPACE_20, SPACE_8, SPACE_20)
        ));
        button.setBorderPainted(true);
    }

    private static void applyBaseButtonStyle(JButton button) {
        button.setUI(new BasicButtonUI());
        button.setFont(BODY_BOLD_FONT);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }

    /**
     * 自定义圆角输入边框 - 模拟 rounded-lg 效果
     */
    public static Border inputBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        );
    }

    /**
     * 聚焦状态的输入边框 - 蓝色高亮
     */
    public static Border inputFocusBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_500, 2),
                BorderFactory.createEmptyBorder(7, 11, 7, 11)
        );
    }
}
