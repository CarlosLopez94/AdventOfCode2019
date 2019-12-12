package adventofcode2019.day12;

import adventofcode2019.Day;
import adventofcode2019.Util;

import java.math.BigInteger;
import java.util.*;

public class Day12 implements Day {
    @Override
    public void main() {
        System.out.println("Day 12 - Part 1");
        List<String> input = Util.ReadFile("day12/input.txt");

        List<Moon> moons = createMoons(input);


        printMoons(moons, 0); //initial state
        System.out.println();
        long steps = 1000;
        for (long i = 1; i <= steps; i++) {
            applyAllGravity(moons);
            applyAllVelocity(moons);
            //printMoons(moons, i);
            //System.out.println();
        }

        System.out.println("Total energy in the system is after " + steps + " steps: " + getTotalEnergy(moons));
        System.out.println("Day 12 - Part 2");
        moons = createMoons(input);

        List<Long> loopsSteps = new ArrayList<>();

        //Calculate separately the steps requires for each Axis to repeat a state.
        loopsSteps.add(getLoopOfAxis(moons, "X").longValue());
        loopsSteps.add(getLoopOfAxis(moons, "Y").longValue());
        loopsSteps.add(getLoopOfAxis(moons, "Z").longValue());

        //Find the least common multiple of each number of steps to find a state which every axis is repeating
        Long repeatingAfterSteps = Util.lcm(loopsSteps);
        System.out.println("A position is repeated after " + repeatingAfterSteps + " steps.");
    }

    public BigInteger getLoopOfAxis(List<Moon> moons, String axisName) {
        Map<String, BigInteger> states = new HashMap<>();
        BigInteger step = BigInteger.ZERO;
        boolean found = false;
        BigInteger loopSteps = null;
        while (!found) {
            Moon firstMoon;
            Moon secondMoon;
            StringBuilder currentStateBuilder = new StringBuilder();
            for (int i = 0; i < moons.size(); i++) {
                firstMoon = moons.get(i);
                for (int j = i; j < moons.size(); j++) {
                    secondMoon = moons.get(j);
                    //Apply gravity on current axis
                    switch (axisName) {
                        case "X":
                            firstMoon.applyGravityX(secondMoon.position.x);
                            secondMoon.applyGravityX(firstMoon.position.x);
                            break;
                        case "Y":
                            firstMoon.applyGravityY(secondMoon.position.y);
                            secondMoon.applyGravityY(firstMoon.position.y);
                            break;
                        case "Z":
                            firstMoon.applyGravityZ(secondMoon.position.z);
                            secondMoon.applyGravityZ(firstMoon.position.z);
                            break;
                    }
                }
                //Apply velocity on currentAxis
                switch (axisName) {
                    case "X":
                        firstMoon.applyVelocityX();
                        currentStateBuilder.append(firstMoon.position.x).append("#").append(firstMoon.velocity.x);
                        break;
                    case "Y":
                        firstMoon.applyVelocityY();
                        currentStateBuilder.append(firstMoon.position.y).append("#").append(firstMoon.velocity.y);
                        break;
                    case "Z":
                        firstMoon.applyVelocityZ();
                        currentStateBuilder.append(firstMoon.position.z).append("#").append(firstMoon.velocity.z);
                        break;
                }
            }

            String currentState = currentStateBuilder.toString();
            if (!states.containsKey(currentState)) {
                states.put(currentState, step);
            } else {
                loopSteps = step.subtract(states.get(currentState));
                found = true;
            }
            step = step.add(BigInteger.ONE);
        }

        return loopSteps;
    }

    public List<Moon> createMoons(List<String> input) {
        List<Moon> moons = new ArrayList<>();
        for (String moonInstruction : input) {
            String[] tokens = moonInstruction.split("=");
            Integer x = Integer.valueOf(tokens[1].split(",")[0]);
            Integer y = Integer.valueOf(tokens[2].split(",")[0]);
            Integer z = Integer.valueOf(tokens[3].split(">")[0]);
            moons.add(new Moon(new Vector(x, y, z)));
        }
        return moons;
    }

    public void applyAllGravity(List<Moon> moons) {
        Moon firstMoon;
        Moon secondMoon;
        for (int i = 0; i < moons.size(); i++) {
            firstMoon = moons.get(i);
            for (int j = i; j < moons.size(); j++) {
                secondMoon = moons.get(j);
                firstMoon.applyGravity(secondMoon);
                secondMoon.applyGravity(firstMoon);
            }
        }
    }

    public void applyAllVelocity(List<Moon> moons) {
        for (Moon moon : moons) {
            moon.applyVelocity();
        }
    }

    private int getTotalEnergy(List<Moon> moons) {
        int total = 0;

        for (Moon moon : moons) {
            total += moon.getTotalEnergy();
        }
        return total;
    }

    public void printMoons(List<Moon> moons, int step) {
        System.out.println("After " + step + " step:");
        for (Moon moon : moons) {
            System.out.println(moon);
        }
    }

    private class Moon {
        private Vector position;
        private Vector velocity;

        public Moon(Vector position) {
            this.position = position;
            this.velocity = new Vector(0, 0, 0);
        }

        public void applyGravityX(Integer otherX) {
            if (position.getX() < otherX) {
                velocity.setX(velocity.getX() + 1);
            } else if (position.getX() > otherX) {
                velocity.setX(velocity.getX() - 1);
            }
        }

        public void applyVelocityX() {
            position.setX(position.getX() + velocity.getX());
        }


        public void applyGravityY(Integer otherY) {
            if (position.getY() < otherY) {
                velocity.setY(velocity.getY() + 1);
            } else if (position.getY() > otherY) {
                velocity.setY(velocity.getY() - 1);
            }
        }

        public void applyVelocityY() {
            position.setY(position.getY() + velocity.getY());
        }


        public void applyGravityZ(Integer otherZ) {
            if (position.getZ() < otherZ) {
                velocity.setZ(velocity.getZ() + 1);
            } else if (position.getZ() > otherZ) {
                velocity.setZ(velocity.getZ() - 1);
            }
        }

        public void applyVelocityZ() {
            position.setZ(position.getZ() + velocity.getZ());
        }

        public void applyGravity(Moon otherMoon) {
            Vector otherPosition = otherMoon.position;
            applyGravityX(otherPosition.getX());
            applyGravityY(otherPosition.getY());
            applyGravityZ(otherPosition.getZ());

        }

        public void applyVelocity() {
            applyVelocityX();
            applyVelocityY();
            applyVelocityZ();
        }

        public int getPotentialEnergy() {
            return Math.abs(position.getX()) + Math.abs(position.getY()) + Math.abs(position.getZ());
        }


        public int getKineticEnergy() {
            return Math.abs(velocity.getX()) + Math.abs(velocity.getY()) + Math.abs(velocity.getZ());
        }

        public int getTotalEnergy() {
            return getPotentialEnergy() * getKineticEnergy();
        }

        @Override
        public String toString() {
            return "{pos=" + position +
                    ",\t vel=" + velocity + "}";
        }
    }

    private class Vector {
        private Integer x;
        private Integer y;
        private Integer z;

        public Vector(Integer x, Integer y, Integer z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Integer getX() {
            return x;
        }

        public Integer getY() {
            return y;
        }

        public Integer getZ() {
            return z;
        }

        public void setX(Integer x) {
            this.x = x;
        }

        public void setY(Integer y) {
            this.y = y;
        }

        public void setZ(Integer z) {
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vector point = (Vector) o;
            return x.equals(point.x) &&
                    y.equals(point.y) &&
                    z.equals(point.z);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override
        public String toString() {
            return "<" + x + ", " + y + ", " + z + '>';
        }
    }


}
