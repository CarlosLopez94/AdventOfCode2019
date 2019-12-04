/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode2019.day3;

import adventofcode2019.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author carlos
 */
public class Day3 {

    public void main() {
        System.out.println("Day 3 - Part 1");
        List<String> wiresInput = Util.ReadFile("/day3/input.txt");

        //Create origin point
        Point origin = new Point(0, 0);

        //Init list
        Bounds limitsBounds = new Bounds();
        List<Wire> wires = new ArrayList<>();
        for (String wireInstruction : wiresInput) {
            Wire newWire = createWire(wireInstruction, origin, limitsBounds);
            wires.add(newWire);
            System.out.println(newWire);
        }
        //Calculate representation bounds and offset
        Point originOffset = new Point(Math.abs(limitsBounds.minX), Math.abs(limitsBounds.minY));
        int[][] wiresRepresentation = new int[Math.abs(limitsBounds.minX) + limitsBounds.maxX + 1][Math.abs(limitsBounds.minY) + limitsBounds.maxY + 1];
        // Merge all wires
        mergeWires(wiresRepresentation, originOffset, wires);
        //Get all intersections points
        List<Point> intersections = getIntersections(wiresRepresentation);

        // Get closest point
        Point closest = null;
        int minManhattan = Integer.MAX_VALUE;

        for (Point intersection : intersections) {
            int manhattan = manhattanDistance(originOffset, intersection);
            System.out.println(intersection + " --> " + manhattan);
            if (manhattan < minManhattan) {
                minManhattan = manhattan;
                closest = intersection;
            }
        }
        System.out.println("Intersections: " + intersections);
        System.out.println("The closest point to origin " + originOffset + " is " + closest + " with a Manhattan distance of " + minManhattan);
        // printMap(wiresRepresentation, originOffset);

    }

    public void mergeWires(int[][] wiresMap, Point offset, List<Wire> wires) {
        for (Wire wire : wires) {
            Point last = null;
            List<Point> visitedPoints = new ArrayList<>();
            for (Point current : wire.getPoints()) {
                if (last == null) {
                    last = current;
                } else {
                    int direction = (current.x < last.x) ? -1 : 1; //if current x is less than before then is moving to the left
                    int difference = Math.abs(current.x - last.x);
                    for (int i = 1; i <= difference; i++) {
                        int offsetX = offset.x + last.x + (i * direction);
                        int offsetY = offset.y + last.y;

                        Point newPoint = new Point(offsetX, offsetY);
                        if (!visitedPoints.contains(newPoint)) {
                            wiresMap[offsetX][offsetY]++;
                            visitedPoints.add(newPoint);
                        }
                    }

                    direction = (current.y < last.y) ? -1 : 1; //if current x is less than before then is moving upp
                    difference = Math.abs(current.y - last.y);
                    for (int i = 1; i <= difference; i++) {
                        int offsetX = offset.x + last.x;
                        int offsetY = offset.y + last.y + (i * direction);

                        Point newPoint = new Point(offsetX, offsetY);
                        if (!visitedPoints.contains(newPoint)) {
                            wiresMap[offsetX][offsetY]++;
                            visitedPoints.add(newPoint);
                        }
                    }
                    last = current;
                }
            }
        }
    }

    public List<Point> getIntersections(int[][] wiresMap) {
        List<Point> intersections = new ArrayList<>();
        for (int i = 0; i < wiresMap.length; i++) {
            for (int j = 0; j < wiresMap[0].length; j++) {
                if (wiresMap[i][j] > 1) {
                    intersections.add(new Point(i, j));
                }
            }
        }

        return intersections;
    }

    public void printMap(int[][] map, Point origin) {
        for (int i = 0; i < map[0].length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (origin.y == i && origin.x == j) {
                    System.out.print("*");
                } else if (map[j][i] == 0) {
                    System.out.print("·");
                } else {
                    System.out.print(map[j][i]);
                }
            }
            System.out.println();
        }

    }

    public Wire createWire(String wireInstructions, Point origin, Bounds bounds) {
        Wire wire = new Wire();
        wire.addPoint(origin);

        Point nextPoint = origin;
        for (String instruction : wireInstructions.split(",")) {
            nextPoint = calculateNewPoint(nextPoint, instruction);
            wire.addPoint(nextPoint);
            bounds.updateBounds(nextPoint);
        }
        return wire;
    }

    private Point calculateNewPoint(Point currentPoint, String instruction) {
        String direction = instruction.substring(0, 1);
        int number = Integer.valueOf(instruction.substring(1));

        Point nextPoint = null;

        switch (direction) {
            case "U": //upp
                nextPoint = new Point(currentPoint.getX(), currentPoint.getY() - number);
                break;
            case "R": //right
                nextPoint = new Point(currentPoint.getX() + number, currentPoint.getY());
                break;
            case "D": //down
                nextPoint = new Point(currentPoint.getX(), currentPoint.getY() + number);
                break;
            case "L": //left
                nextPoint = new Point(currentPoint.getX() - number, currentPoint.getY());
                break;
        }

        return nextPoint;
    }

    public int manhattanDistance(Point origin, Point destiny) {
        return Math.abs(origin.x - destiny.x) + Math.abs(origin.y - destiny.y);

    }

    private class Point {

        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Point other = (Point) obj;
            if (this.x != other.x) {
                return false;
            }
            if (this.y != other.y) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }

    }

    private class Bounds {

        private int minX;
        private int maxX;
        private int minY;
        private int maxY;

        public Bounds() {
            this.minX = Integer.MAX_VALUE;
            this.maxX = Integer.MIN_VALUE;
            this.minY = Integer.MAX_VALUE;
            this.maxY = Integer.MIN_VALUE;
        }

        public void updateBounds(Point point) {
            minX = Math.min(minX, point.getX());
            maxX = Math.max(maxX, point.getX());
            minY = Math.min(minY, point.getY());
            maxY = Math.max(maxY, point.getY());
        }

        public String toString() {
            return "X=(" + minX + "," + maxX + ")/Y=(" + minY + "," + maxY + ")";
        }
    }

    private class Wire {

        private List<Point> points;

        public Wire() {
            this.points = new ArrayList<>();
        }

        public void addPoint(Point nextPoint) {
            points.add(nextPoint);
        }

        public List<Point> getPoints() {
            return points;
        }

        @Override
        public String toString() {
            return points.toString();
        }
    }
}
