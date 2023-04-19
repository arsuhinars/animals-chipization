package com.arsuhinars.animals_chipization.core.util;

import lombok.Getter;
import lombok.ToString;

import java.util.*;

@Getter
@ToString(onlyExplicitlyIncluded = true)
public class Triangle {
    @ToString.Include
    private final Vector2d[] points;

    private final Rect rect;

    private final double area;

    public Triangle(List<Vector2d> points) {
        if (points == null || points.size() != 3) {
            throw new IllegalArgumentException("Points must contain only 3 elements");
        }

        this.points = points.toArray(Vector2d[]::new);
        this.rect = Rect.inPointsRange(points);
        this.area = Math.abs(Vector2d.cross(
            this.points[1].subtract(this.points[0]),
            this.points[2].subtract(this.points[0])
        )) / 2.0;
    }

    public double getArea() {
        return area;
    }

    public boolean containsPoint(Vector2d point) {
        return containsPoint(point, true);
    }

    public boolean containsPoint(Vector2d point, boolean withBorders) {
        var s1 = Math.abs(Vector2d.cross(
            points[0].subtract(point), points[1].subtract(point)
        ));
        var s2 = Math.abs(Vector2d.cross(
           points[1].subtract(point), points[2].subtract(point)
        ));
        var s3 = Math.abs(Vector2d.cross(
            points[2].subtract(point), points[0].subtract(point)
        ));

        if (!withBorders && (
            MathUtil.fuzzyEquals(s1, 0.0) || MathUtil.fuzzyEquals(s2, 0.0) || MathUtil.fuzzyEquals(s3, 0.0)
        )) {
            return false;
        }

        return MathUtil.fuzzyEquals(s1 + s2 + s3, this.area * 2.0);
    }

    public boolean overlapsTriangle(Triangle triangle) {
        if (!rect.overlapsRect(triangle.rect)) {
            return false;
        }

        if (containsPoint(triangle.points[0], false) ||
            containsPoint(triangle.points[1], false) ||
            containsPoint(triangle.points[2], false)
        ) {
            return true;
        }

        if (triangle.containsPoint(points[0], false) ||
            triangle.containsPoint(points[1], false) ||
            triangle.containsPoint(points[2], false)
        ) {
            return true;
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (MathUtil.segmentsIntersect(
                    points[i],          points[(i + 1) % 3],
                    triangle.points[j], triangle.points[(j + 1) % 3]
                )) {
                    return true;
                }
            }
        }

        return false;
    }
}
