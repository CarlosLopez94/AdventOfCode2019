package adventofcode2019.GenericClasses;

import java.util.Objects;

public class Point implements Comparable<Point> {
    private Integer x;
    private Integer y;

    public Point(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Point o) {
        int compareYAxis = y.compareTo(o.y);
        if (compareYAxis != 0) {
            return compareYAxis;
        } else {
            return x.compareTo(o.x);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x.equals(point.x) &&
                y.equals(point.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}