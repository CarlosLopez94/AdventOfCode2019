package adventofcode2019.day11;

import adventofcode2019.Day;
import adventofcode2019.Util;

import java.math.BigInteger;
import java.util.*;

public class Day11 implements Day {
    @Override
    public void main() {
        System.out.println("Day 11 - Part 1");
        String input = Util.ReadFileOneLine("day11/input.txt");
        List<Long> instructions = Util.stringToLongList(input, ",");

        Map<Point, Integer> squares = new HashMap<>();

        Point initPoint = new Point(0, 0);
        Robot robot = new Robot(initPoint, instructions);
        squares.put(initPoint, 0);

        robot.start(squares);

        System.out.println("There are " + squares.size() + " visited points.");
    }

    private class Robot {
        private IntCodeComputer intCodeComputer;
        private Direction direction;
        private Point location;

        public Robot(Point initPoint, List<Long> instructions) {
            intCodeComputer = new IntCodeComputer(instructions);
            direction = Direction.UP;
            location = initPoint;
        }

        public void turnRight() {
            System.out.println("Turn Right");
            switch (direction) {
                case UP:
                    direction = Direction.RIGHT;
                    break;
                case RIGHT:
                    direction = Direction.DOWN;
                    break;
                case DOWN:
                    direction = Direction.LEFT;
                    break;
                case LEFT:
                    direction = Direction.UP;
                    break;
            }
        }

        public void turnLeft() {
            System.out.println("Turn Left");
            switch (direction) {
                case UP:
                    direction = Direction.LEFT;
                    break;
                case RIGHT:
                    direction = Direction.UP;
                    break;
                case DOWN:
                    direction = Direction.RIGHT;
                    break;
                case LEFT:
                    direction = Direction.DOWN;
                    break;
            }
        }

        public void move() {
            switch (direction) {
                case UP:
                    location = new Point(location.x, location.y - 1);
                    break;
                case RIGHT:
                    location = new Point(location.x + 1, location.y);
                    break;
                case DOWN:
                    location = new Point(location.x, location.y + 1);
                    break;
                case LEFT:
                    location = new Point(location.x - 1, location.y);
                    break;
            }
            System.out.println("Moved to " + location);
        }

        public void paint(Map<Point, Integer> squares, Integer colorToPaint) {
            squares.put(location, colorToPaint);
            System.out.println("painted " + location + " with color " + colorToPaint);
        }

        public void start(Map<Point, Integer> squares) {
            while (!intCodeComputer.isHaltProcessed()) {
                Integer currentColor = getColorCurrentSquare(squares);
                List<BigInteger> output = intCodeComputer.evaluateStep(currentColor);
                if (!intCodeComputer.isHaltProcessed()) {
                    Integer newColor = output.get(0).intValue();
                    Integer newDirection = intCodeComputer.evaluateStep(currentColor).get(0).intValue();
                    paint(squares, newColor);

                    if (newDirection == 0) {
                        turnLeft();
                    } else {
                        turnRight();
                    }
                    move();
                }
            }
        }

        public Integer getColorCurrentSquare(Map<Point, Integer> squares) {
            if (!squares.containsKey(location)) {
                squares.put(location, 0);
            }
            return squares.get(location);
        }
    }

    private enum Direction {
        UP, RIGHT, DOWN, LEFT
    }

    private class Point implements Comparable<Point> {
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
}
