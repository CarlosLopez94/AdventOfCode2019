/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode2019;

import adventofcode2019.Day1.Day1;
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
            day = 1; //DELETE
            switch (day) {
                case 1:
                    Day1 day1 = new Day1();
                    day1.main();
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