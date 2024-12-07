package com.example.comp3074_groupproject_bestulator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.LineBackgroundSpan;
import android.widget.CalendarView;

public class ProjectCalendarDecorator {
    // Color constants for different project categories
    public static final int COLOR_CONSTRUCTION = Color.parseColor("#FF9800");
    public static final int COLOR_RENOVATION = Color.parseColor("#4CAF50");
    public static final int COLOR_DESIGN = Color.parseColor("#2196F3");
    public static final int COLOR_MAINTENANCE = Color.parseColor("#9C27B0");
    public static final int COLOR_OTHER = Color.parseColor("#607D8B");

    public static int getCategoryColor(String category) {
        switch (category.toLowerCase()) {
            case "construction":
                return COLOR_CONSTRUCTION;
            case "renovation":
                return COLOR_RENOVATION;
            case "design":
                return COLOR_DESIGN;
            case "maintenance":
                return COLOR_MAINTENANCE;
            default:
                return COLOR_OTHER;
        }
    }

    public static class DotSpan implements LineBackgroundSpan {
        private final int color;
        private final int count;
        private final float radius;

        public DotSpan(int color, int count) {
            this.color = color;
            this.count = count;
            this.radius = 4f; // Dot size
        }

        @Override
        public void drawBackground(Canvas canvas, Paint paint,
                                   int left, int right, int top, int baseline, int bottom,
                                   CharSequence text, int start, int end, int lnum) {
            int oldColor = paint.getColor();
            paint.setColor(color);

            // Draw dots based on count (max 3 dots)
            int displayCount = Math.min(count, 3);
            float spacing = 12f; // Space between dots
            float startX = (left + right) / 2f - ((displayCount - 1) * spacing / 2f);
            float y = bottom + 10f; // Position below the date number

            for (int i = 0; i < displayCount; i++) {
                canvas.drawCircle(startX + (i * spacing), y, radius, paint);
            }

            paint.setColor(oldColor);
        }
    }
}