/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode2019;

import adventofcode2019.day1.Day1;
import adventofcode2019.day2.Day2;
import adventofcode2019.day3.Day3;
import adventofcode2019.day4.Day4;
import adventofcode2019.day5.Day5;
import adventofcode2019.day6.Day6;
import adventofcode2019.day7.Day7;

import java.util.Scanner;

/**
 * @author Carlos
 */
public class AdventOfCode2019 {

    public static void main(String[] args) {
        System.out.println("Welcome to Advent of Code 2015! Please choose a day between 1 and 25:");
        int dayNumber;
        do {
            //dayNumber = ReadDay();
            dayNumber = 7; //DELETE
            Day day = null;
            switch (dayNumber) {
                case 1:
                    day = new Day1();
                    break;
                case 2:
                    day = new Day2();
                    break;
                case 3:
                    day = new Day3();
                    break;
                case 4:
                    day = new Day4();
                    break;
                case 5:
                    day = new Day5();
                    break;
                case 6:
                    day = new Day6();
                    break;
                case 7:
                    day = new Day7();
                    b areak;
                default:
                    day = null;
                    System.out.println("ups, this day isn't avaliable yet! Try again");
            }
            if (day != null) {
                day.main();
            }
        } while (dayNumber < 0 && dayNumber > 25);
    }

    public static int ReadDay() {
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }

}
