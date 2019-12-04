/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode2019.day4;

import adventofcode2019.Util;

/**
 *
 * @author carlos
 */
public class Day4 {

    public void main() {
        System.out.println("Day 4 - Part 1");
        String input = Util.ReadFileOneLine("day4/input.txt");
        String[] tokens = input.split("-");
        String initPass = tokens[0];
        String maximum = tokens[1];

        System.out.println(neverDecrease("111112"));

        int validPasswords = 0;
        String password = initPass;
        while (!password.equals(maximum)) {
            if (checkCriteria(password)) {
                validPasswords++;
                // System.out.println(password + " --> si");
            } else {

                //System.out.println(password + " --> no");
            }
            password = getNextPassword(password);
        }
        System.out.println("The number of valid passwords is: " + validPasswords);
    }

    public String getNextPassword(String password) {
        Integer passwordInteger = Integer.valueOf(password);
        passwordInteger++;
        return String.valueOf(passwordInteger);
    }

    public boolean checkCriteria(String password) {
        return hasPair(password) && neverDecrease(password);
    }

    public boolean hasPair(String password) {
        boolean hasSameAdjacent = false;
        char lastChar = ' ';
        char currentChar;
        int i = 0;
        while (!hasSameAdjacent && i < password.length()) {
            currentChar = password.charAt(i);
            hasSameAdjacent = currentChar == lastChar;
            lastChar = currentChar;
            i++;
        }
        return hasSameAdjacent;
    }

    private boolean neverDecrease(String password) {
        boolean neverDecrease = true;

        int lastDigit = Integer.MIN_VALUE;
        int currentDigit;
        int i = 0;
        while (neverDecrease && i < password.length()) {
            currentDigit = Integer.valueOf(String.valueOf(password.charAt(i)));
            neverDecrease = lastDigit <= currentDigit;

            lastDigit = currentDigit;
            i++;
        }

        return neverDecrease;
    }
}
