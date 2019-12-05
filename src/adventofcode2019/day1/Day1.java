
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode2019.day1;

import adventofcode2019.Util;
import java.util.List;

/**
 *
 * @author carlos
 */
public class Day1 {

    public void main() {
        //Get input
        List<String> input = Util.ReadFile("/day1/input.txt");
        System.out.println("Day 1 - Part 1");

        int totalFuel = 0;
        for (String in : input) {
            int mass = Integer.valueOf(in);
            int fuel = getFuel(mass);
            totalFuel += fuel;
        }

        System.out.println("Total fuel is: " + totalFuel);

        System.out.println("Day 1 - Part 2");
        totalFuel = 0;
        for (String in : input) {
            int mass = Integer.valueOf(in);
            int fuel = calculateFuelModule(mass);
            totalFuel += fuel;
        }

        System.out.println("Total fuel (taking into account the extra fuel) is: " + totalFuel);
    }

    private Integer getFuel(int mass) {
        double divided = mass / 3;
        int roundedDown = (int) Math.floor(divided);

        return roundedDown - 2;
    }

    private Integer calculateFuelModule(int mass) {
        if (mass <= 0) {
            return 0;
        } else {
            int fuelNeeded = Math.max(getFuel(mass), 0);
            return fuelNeeded + calculateFuelModule(fuelNeeded);
        }
    }

}
