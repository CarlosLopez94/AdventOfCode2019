/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode2019;

import adventofcode2019.day2.Day2;
import adventofcode2019.day3.Day3;
import adventofcode2019.day4.Day4;
import day1.Day1;
import java.util.Scanner;

/**
 *
 * @author Carlos
 */
public class AdventOfCode2019 {

    public static void main(String[] args) {
        System.out.println("Welcome to Advent of Code 2015! Please choose a day between 1 and 25:");
        int day;
        do {
            //day = ReadDay();
            day = 4; //DELETE
            switch (day) {
                case 1:
                    Day1 day1 = new Day1();
                    day1.main();
                    break;
                case 2:
                    Day2 day2 = new Day2();
                    day2.main();
                    break;
                case 3:
                    Day3 day3 = new Day3();
                    day3.main();
                    break;
                case 4:
                    Day4 day4 = new Day4();
                    day4.main();
                    break;
                default:
                    System.out.println("ups, this day isn't avaliable yet! Try again");
            }
        } while (day < 0 && day > 25);
    }

    public static int ReadDay() {
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }

}
