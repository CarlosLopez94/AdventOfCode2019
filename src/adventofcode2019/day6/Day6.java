/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode2019.day6;

import adventofcode2019.Util;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author carlos
 */
public class Day6 {

    public void main() {
        System.out.println("Day 6 - Part 1");
        List<String> input = Util.ReadFile("day6/input.txt");

        //Get relations
        Map<String, Set<String>> relations = getPlanetsRelations(input);
        System.out.println(relations);

        //Parse to planets structure
        Planet root = parseToPlanets(relations, "COM");
        System.out.println(root);
        int total = getTotalOrbitting(root);
        System.out.println("Total orbitting is: " + total);
        System.out.println("Day 6 - Part 2");

        // Get planets 'YOU' and 'SAN'
        Planet origin = findPlanetByName(root, "YOU");
        Planet destiny = findPlanetByName(root, "SAN");

        //Get planets where 'YOU' and 'SAN' are 
        Planet originParent = origin.getParentPlanet();
        Planet destinyParent = destiny.getParentPlanet();

        //Find closest Parent to both parent planets.
        Planet closestParentToBoth = findClosestParent(root, originParent, destinyParent);
        System.out.println(closestParentToBoth);
        int jumps = getJumpsfromOriginToDestiny(closestParentToBoth, origin) + getJumpsfromOriginToDestiny(closestParentToBoth, destiny);
        System.out.println("Jumps: " + jumps);
    }

    private Map<String, Set<String>> getPlanetsRelations(List<String> input) {
        Map<String, Set<String>> relations = new HashMap<>();

        for (String in : input) {
            String[] tokens = in.replace(')', '#').split("#");
            String orbitCenter = tokens[0];
            String orbits = tokens[1];
            if (relations.containsKey(orbitCenter)) {
                relations.get(orbitCenter).add(orbits);
            } else {
                Set<String> relationsOfCenter = new TreeSet<>();
                relationsOfCenter.add(orbits);
                relations.put(orbitCenter, relationsOfCenter);
            }
        }

        return relations;
    }

    private Planet parseToPlanets(Map<String, Set<String>> relations, String name) {
        Planet currentPlanet = new Planet(name);

        //If currentPlanet doesnt have a relation then it doesnt have any child
        if (relations.containsKey(name)) {
            //For each child
            for (String currentChild : relations.get(name)) {
                currentPlanet.addOrbittingPlanet(parseToPlanets(relations, currentChild));
            }
        }
        return currentPlanet;
    }

    private int getTotalOrbitting(Planet currentPlanet) {
        int currentTotal = currentPlanet.getTotalOrbitting();
        if (currentPlanet.getChildPlanets().size() > 0) {
            for (Planet child : currentPlanet.getChildPlanets()) {
                currentTotal += getTotalOrbitting(child);
            }
        }
        return currentTotal;
    }

    private Planet findPlanetByName(Planet root, String nameToFind) {
        Planet foundPlanet = null;
        if (root.getName().equals(nameToFind)) {
            foundPlanet = root;
        } else {
            Iterator<Planet> it = root.childPlanets.iterator();
            while (foundPlanet == null && it.hasNext()) {
                foundPlanet = findPlanetByName(it.next(), nameToFind);
            }
        }
        return foundPlanet;
    }

    private Planet findClosestParent(Planet root, Planet child1, Planet child2) {
        Planet closest = null;
        if (root.isParent(child1) && root.isParent(child2)) {
            Iterator<Planet> it = root.getChildPlanets().iterator();
            boolean founded = false;
            Planet candidate = null;

            while (!founded && it.hasNext()) {
                candidate = it.next();
                founded = candidate.isParent(child1) && candidate.isParent(child2);
            }

            if (founded) {
                closest = findClosestParent(candidate, child1, child2);
            } else {
                closest = root;
            }

        }
        return closest;
    }

    private int getJumpsfromOriginToDestiny(Planet origin, Planet destiny) {
        return getJumpsfromOriginToDestinyAux(origin, destiny, 0);
    }

    /**
     * DONT CALL THIS METHOD DIRECTLY, CALL 'getJumpsfromOriginToDestiny'
     * INSTEAD Returns number of jump necessary to reach destiny planet from
     * origin. planet
     *
     * @param origin
     * @param destiny
     * @param currentDepth
     * @return
     */
    private int getJumpsfromOriginToDestinyAux(Planet origin, Planet destiny, int currentDepth) {
        int depth = currentDepth;
        //If origin is parent of destiny its already orbitting, so there is no jump
        if (destiny.getParentPlanet().equals(origin)) {
            return depth;
        } else {
            depth++;
            Iterator<Planet> it = origin.getChildPlanets().iterator();
            boolean founded = false;
            while (!founded && it.hasNext()) {
                Planet candidate = it.next();
                if (candidate.isParent(destiny)) {
                    depth = getJumpsfromOriginToDestinyAux(candidate, destiny, depth);
                }
            }

            return depth;
        }
    }

    private class Planet {

        private String name;
        private Planet parentPlanet;
        private Set<Planet> childPlanets;

        public String getName() {
            return name;
        }

        public Planet getParentPlanet() {
            return parentPlanet;
        }

        public Set<Planet> getChildPlanets() {
            return childPlanets;
        }

        public Planet(String name) {
            this.name = name;
            this.parentPlanet = null;
            this.childPlanets = new HashSet<>();
        }

        private void orbits(Planet parentPlanet) {
            this.parentPlanet = parentPlanet;
        }

        public void addOrbittingPlanet(Planet planet) {
            planet.orbits(this);
            childPlanets.add(planet);
        }

        public int getTotalOrbitting() {
            if (parentPlanet == null) {
                return 0;
            } else {
                return 1 + parentPlanet.getTotalOrbitting();
            }
        }

        /**
         * Returns true if 'childCandidate' is a diret or indirect child this
         * Planet
         *
         * @param childCandidate
         * @return
         */
        public boolean isParent(Planet childCandidate) {
            boolean isChild = childPlanets.contains(childCandidate);
            if (!isChild) {
                Iterator<Planet> it = childPlanets.iterator();
                while (!isChild && it.hasNext()) {
                    isChild = it.next().isParent(childCandidate);
                }
            }
            return isChild;
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
            final Planet other = (Planet) obj;
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            return true;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("(").append(name).append(") --> {");

            if (childPlanets.size() > 0) {
                Set<String> names = new TreeSet<>();
                for (Planet p : childPlanets) {
                    names.add(p.toString());
                }
                sb.append(String.join(",", names));
            }
            sb.append("}");

            return sb.toString();
        }

    }
}
