package com.arsuhinars.animals_chipization.area.util;

import java.util.List;

public class MathUtil {
    public static final double EPSILON = 1e-8;

    /**
     * Функция проверки на пересечение двух отрезков
     * @param a1 точка начала первого отрезка
     * @param a2 точка конца первого отрезка
     * @param b1 точка начала второго отрезка
     * @param b2 точка конца второго отрезка
     */
    public static boolean segmentsIntersect(
        Vector2d a1, Vector2d a2, Vector2d b1, Vector2d b2
    ) {
        // Вектор направления прямой, лежащей на первом отрезке
        var d = a2.subtract(a1);
        // Вектор направления прямой, лежащей на втором
        var k = b2.subtract(b1);

        double t = Vector2d.cross(k, a1.subtract(b1)) / Vector2d.cross(d, k);

        // Если прямые параллельны
        if (Double.isInfinite(t)) {
            return false;
        }

        // Находим точку пересечения прямых
        var point = d.multiply(t).add(a1);

        // Находится ли внутри первого отрезка
        boolean inFirstSegment = Rect.inPointsRange(List.of(a1, a2)).containsPoint(point);

        // Находится ли внутри второго отрезка
        boolean inSecondSegment = Rect.inPointsRange(List.of(b1, b2)).containsPoint(point);

        return inFirstSegment && inSecondSegment;
    }

    public static boolean fuzzyEquals(double a, double b) {
        return Math.abs(a - b) <= EPSILON;
    }
}
