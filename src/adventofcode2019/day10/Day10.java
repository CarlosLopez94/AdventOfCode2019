package adventofcode2019.day10;

import adventofcode2019.Day;
import adventofcode2019.Util;

import java.util.*;
import java.util.List;

public class Day10 implements Day {
    @Override
    public void main() {
        System.out.println("Day 10 - Part 1");
        List<String> input = Util.ReadFile("day10/input.txt");

        List<Asteroid> asteroids = createAsteroids(input);

        for (Asteroid asteroid : asteroids) {
            asteroid.addAllSights(asteroids);
        }

        int maxViewed = Integer.MIN_VALUE;
        Asteroid best = null;
        for (Asteroid asteroid : asteroids) {
            int viewed = asteroid.getAllSight();
            if (viewed > maxViewed) {
                maxViewed = viewed;
                best = asteroid;
            }
        }
        System.out.println("The best asteroid is " + best.coordinate.toString() + " with " + maxViewed);
    }

    private List<Asteroid> createAsteroids(List<String> asteroidsCoordinates) {
        List<Asteroid> asteroids = new ArrayList<>();
        int x;
        int y = 0;
        for (String line : asteroidsCoordinates) {
            x = 0;
            for (char c : line.toCharArray()) {
                if (c == '#') {
                    asteroids.add(new Asteroid(x, y));
                }
                x++;
            }
            y++;
        }
        return asteroids;
    }

    private class Asteroid {
        private Point coordinate;
        private Map<Double, List<Asteroid>> viewRectSlopes;

        public Asteroid(int x, int y) {
            this.coordinate = new Point(x, y);
            this.viewRectSlopes = new TreeMap<>();
        }

        public void addAllSights(List<Asteroid> asteroids) {
            for (Asteroid asteroid : asteroids) {
                if (!this.equals(asteroid) && !isBlocked(asteroid)) {
                    //If this asteroid can be reached from any sight line, it means that nothing is blocking sight (t should be added)
                    addSightLine(asteroid);
                }
            }
        }

        private boolean isBlocked(Asteroid asteroid) {
            //Check if current asteroid can be sight by one of the sight lines already added. If its true it means it exists another asteroid
            // that is blocking sight (so it shouldnt be added).
            boolean found = false;
            Iterator<Double> it = viewRectSlopes.keySet().iterator();
            Double currentSlope;
            while (!found && it.hasNext()) {
                currentSlope = it.next();
                found = isInSightLine(currentSlope, asteroid);
                if (found) {
                    viewRectSlopes.get(currentSlope).add(asteroid);
                }
            }
            if (!found) {
                addSightLine(asteroid);
            }
            return found;
        }

        private int getAllSight() {
            int inSight = 0;
            for (Double currentSlope : viewRectSlopes.keySet()) {
                List<Asteroid> viewedAsteroids = viewRectSlopes.get(currentSlope);
                viewedAsteroids.add(this); //add this asteroid
                //Sort asteroids
                Collections.sort(viewedAsteroids, new Comparator<Asteroid>() {
                    @Override
                    public int compare(Asteroid o1, Asteroid o2) {
                        return o1.coordinate.compareTo(o2.coordinate);
                    }
                });

                int thisPosition = viewedAsteroids.indexOf(this);
                //sum before this
                for (int i = 0; i < thisPosition; i++) {
                    inSight++;
                    i = thisPosition + 1; //finish loop
                }

                //sum after this
                for (int i = thisPosition + 1; i < viewedAsteroids.size(); i++) {
                    inSight++;
                    i = viewedAsteroids.size() + 1; //finish loop
                }

                viewedAsteroids.remove(this); //Remove this asteroid
            }

            return inSight;
        }

        private boolean isInSightLine(Double slope, Asteroid candidate) {
            if (slope.isInfinite()) {
                return this.coordinate.x.equals(candidate.coordinate.x);
            } else {
                Double differenceY = (double) (candidate.coordinate.y - this.coordinate.y);
                return differenceY.equals(slope * (candidate.coordinate.x - this.coordinate.x));
            }
        }

        private void addSightLine(Asteroid viewedAsteroid) {
            Double slope = (double) (viewedAsteroid.coordinate.y - this.coordinate.y) / (viewedAsteroid.coordinate.x - this.coordinate.x);
            viewRectSlopes.put(slope, new ArrayList<>());
            viewRectSlopes.get(slope).add(viewedAsteroid);
        }

        //Two asteroids are the same asteroid if they are at they share coordinates
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Asteroid) {

                return coordinate.equals(((Asteroid) obj).coordinate);
            }
            return false;
        }

        @Override
        public String toString() {
            return coordinate.toString() + " --> " + viewRectSlopes.keySet().toString();
        }
    }

    private class Point implements Comparable<Point> {
        private Integer x;
        private Integer y;

        public Point(Integer x, Integer y) {
            this.x = x;
            this.y = y;
        }

        public Integer getX() {
            return x;
        }

        public Integer getY() {
            return y;
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
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }


}
