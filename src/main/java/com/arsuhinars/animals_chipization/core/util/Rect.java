package com.arsuhinars.animals_chipization.core.util;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.List;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Rect {
    @Column(nullable = false)
    private double minX;
    @Column(nullable = false)
    private double minY;
    @Column(nullable = false)
    private double maxX;
    @Column(nullable = false)
    private double maxY;

    public boolean containsPoint(Vector2d p) {
        return
            p.getX() > minX + MathUtil.EPSILON && p.getX() < maxX - MathUtil.EPSILON &&
            p.getY() > minY + MathUtil.EPSILON && p.getY() < maxY - MathUtil.EPSILON;
    }

    public boolean overlapsRect(Rect r) {
        return (
            (minX >= r.minX && minX <= r.maxX) ||
            (maxX >= r.minX && maxX <= r.maxX) ||
            (r.minX >= minX && r.minX <= maxX) ||
            (r.maxX >= minX && r.maxX <= maxX)
        ) && (
            (minY >= r.minY && minY <= r.maxY) ||
            (maxY >= r.minY && maxY <= r.maxY) ||
            (r.minY >= minY && r.minY <= maxY) ||
            (r.maxY >= minY && r.maxY <= maxY)
        );
    }

    public static Rect inPointsRange(List<Vector2d> points) {
        var rect = new Rect(
            Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE
        );
        for (var p : points) {
            rect.setMinX(Math.min(p.getX(), rect.getMinX()));
            rect.setMinY(Math.min(p.getY(), rect.getMinY()));
            rect.setMaxX(Math.max(p.getX(), rect.getMaxX()));
            rect.setMaxY(Math.max(p.getY(), rect.getMaxY()));
        }

        return rect;
    }
}
