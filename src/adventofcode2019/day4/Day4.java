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

        int validPasswords = 0;
        String password = initPass;
        while (!password.equals(maximum)) {
            if (checkCriteria(password)) {
                validPasswords++;
            }
            password = getNextPassword(password);
        }
        System.out.println("The number of valid passwords is: " + validPasswords);
        System.out.println("Day 4 - Part 2");
        validPasswords = 0;
        password = initPass;
        while (!password.equals(maximum)) {
            if (checkComplexCriteria(password)) {
                validPasswords++;
            }
            password = getNextPassword(password);
        }
        System.out.println("The number of valid passwords with complex criteria is: " + validPasswords);

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

    public boolean checkComplexCriteria(String password) {
        return hasJustAPair(password) && neverDecrease(password);
    }

    public boolean hasJustAPair(String password) {
        boolean hasValidPair = false;

        int i = 0;
        int ocurrences = 0;
        char lastDigit = '*';
        char currentDigit;
        while (!hasValidPair && i < password.length()) {
            if (i == 0) {
                lastDigit = password.charAt(i);
            } else {
                currentDigit = password.charAt(i);
                if (lastDigit == currentDigit) {
                    ocurrences++;
                } else {
                    hasValidPair = ocurrences == 1;
                    ocurrences = 0;
                }
                lastDigit = currentDigit;
            }
            i++;
        }
        //Check for the last digits!
        if(!hasValidPair){
            hasValidPair = ocurrences == 1;
        }
        
        return hasValidPair;
    }

}
