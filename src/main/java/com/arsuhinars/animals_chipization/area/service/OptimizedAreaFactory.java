package com.arsuhinars.animals_chipization.area.service;

import com.arsuhinars.animals_chipization.area.util.*;
import com.arsuhinars.animals_chipization.core.exception.InvalidFormatException;
import com.arsuhinars.animals_chipization.core.util.MathUtil;
import com.arsuhinars.animals_chipization.core.util.Rect;
import com.arsuhinars.animals_chipization.core.util.Triangle;
import com.arsuhinars.animals_chipization.core.util.Vector2d;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

@Component
public class OptimizedAreaFactory {
    public OptimizedArea createArea(List<Vector2d> originPoints) throws InvalidFormatException {
        if (linesIntersects(originPoints)) {
            throw new InvalidFormatException("Area lines can't intersect with each other");
        }

        var points = new LinkedHashSet<>(originPoints);

        // Шаг 1. Определяем направление задания точек
        double area = 0.0;
        var it = points.iterator();
        var first = it.next();
        var prev = it.next();
        while (it.hasNext()) {
            var curr = it.next();
            area += Vector2d.cross(
                prev.subtract(first), curr.subtract(first)
            );
        }
        area /= 2;

        if (MathUtil.fuzzyEquals(area, 0.0)) {
            throw new InvalidFormatException("Area can't be a line");
        }

        boolean isClockwise = area < 0.0;

        // Шаг 2. ищем и отрезаем уши

        // Список полученых треугольников
        var triangles = new LinkedList<Triangle>();

        // Триангулируем многоугольник
        while (points.size() > 3) {
            it = points.iterator();
            first = it.next();
            prev = first;
            var curr = it.next();

            for (int i = 0; i < points.size(); ++i) {
                var next = it.hasNext() ? it.next() : first;

                // Определяем, является ли текущая вершина выпуклой
                var cross = Vector2d.cross(
                    next.subtract(curr), prev.subtract(curr)
                );
                // Пропускаем, если она не выпуклая
                if (isClockwise ? cross > 0.0 : cross < 0.0) {
                    continue;
                }

                // Проверяем, что вершина является ухом
                // т.е. ни одна друга точка не находится внутри данного треугольника
                boolean isEarTip = true;
                var triangle = new Triangle(List.of(prev, curr, next));
                for (var p : points) {
                    if (p.equals(first) || p.equals(curr) || p.equals(next)) {
                        continue;
                    }
                    if (triangle.containsPoint(p)) {
                        isEarTip = false;
                        break;
                    }
                }

                if (isEarTip) {
                    // "Отрезаем" ухо
                    triangles.addFirst(triangle);
                    points.remove(curr);
                    break;
                }

                prev = curr;
                curr = next;
            }
        }

        triangles.add(new Triangle(points.stream().toList()));

        return new OptimizedArea(
            Rect.inPointsRange(originPoints),
            Math.abs(area),
            originPoints,
            isClockwise,
            triangles
        );
    }

    private boolean linesIntersects(List<Vector2d> points) {
        for (int i = 0; i < points.size(); ++i) {
            for (int j = i + 1; j < points.size(); ++j) {
                if (MathUtil.segmentsIntersect(
                    points.get(i), points.get((i + 1) % points.size()),
                    points.get(j), points.get((j + 1) % points.size())
                )) {
                    return true;
                }
            }
        }
        return false;
    }
}
