/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode2019.day6;

import adventofcode2019.Util;
import java.util.HashMap;
import java.util.HashSet;
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
