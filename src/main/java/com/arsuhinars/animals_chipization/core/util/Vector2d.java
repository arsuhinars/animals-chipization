package com.arsuhinars.animals_chipization.core.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vector2d {
    private double x;
    private double y;

    public Vector2d(double value) {
        this.x = value;
        this.y = value;
    }

    public Vector2d() {
        this.x = 0;
        this.y = 0;
    }

    public boolean fuzzyEquals(Vector2d v) {
        return MathUtil.fuzzyEquals(x, v.x) && MathUtil.fuzzyEquals(y, v.y);
    }

    public Vector2d add(Vector2d v) {
        return new Vector2d(x + v.x, y + v.y);
    }

    public Vector2d subtract(Vector2d v) {
        return new Vector2d(x - v.x, y - v.y);
    }

    public Vector2d multiply(double s) {
        return new Vector2d(x * s, y * s);
    }

    public static double dot(Vector2d a, Vector2d b) {
        return (a.x * b.x) + (a.y * b.y);
    }

    public static double cross(Vector2d a, Vector2d b) {
        return (a.x * b.y) - (a.y * b.x);
    }
}
