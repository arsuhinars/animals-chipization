package com.arsuhinars.animals_chipization.area.util;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OptimizedArea {
    private Rect rect;
    private double squareArea;
    private List<Vector2d> points;
    private boolean arePointsClockwise;
    private List<Triangle> triangles;

    public boolean containsPoint(Vector2d point) {
        return triangles.stream().anyMatch(t -> t.containsPoint(point));
    }

    public boolean overlapsTriangle(Triangle triangle) {
        return triangles.stream().anyMatch(t -> t.overlapsTriangle(triangle));
    }

    public boolean overlapArea(OptimizedArea area) {
        return area.triangles.stream().anyMatch(this::overlapsTriangle);
    }

    public boolean equalsTo(OptimizedArea area) {
        if (this == area) {
            return true;
        }

        if (this.points.size() != area.points.size() || Double.compare(squareArea, area.squareArea) != 0) {
            return false;
        }

        int selfIdx = -1;
        int otherIdx = -1;
        for (int i = 0; i < points.size(); ++i) {
            var curr = points.get(
                arePointsClockwise ? (points.size() - i - 1) : i
            );

            for (int j = 0; j < area.points.size(); ++j) {
                var other = area.points.get(
                    area.arePointsClockwise ? (area.points.size() - j - 1) : j
                );
                if (curr.fuzzyEquals(other)) {
                    selfIdx = i;
                    otherIdx = j;
                    break;
                }
            }

            if (selfIdx != -1) {
                break;
            }
        }

        if (selfIdx == -1) {
            return false;
        }

        for (int i = 0; i < points.size(); ++i) {
            var curr = points.get(
                arePointsClockwise ?
                    ((2 * points.size() - i - selfIdx - 1) % points.size()) :
                    (i + selfIdx) % points.size()
            );
            var other = area.points.get(
                area.arePointsClockwise ?
                    ((2 * area.points.size() - i - otherIdx - 1) % area.points.size()) :
                    (i + otherIdx) % area.points.size()
            );

            if (!curr.fuzzyEquals(other)) {
                return false;
            }
        }

        return true;
    }
}
