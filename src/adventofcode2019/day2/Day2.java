/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode2019.day2;

import adventofcode2019.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author carlos
 */
public class Day2 {

    public void main() {
        System.out.println("Day 2 - Part 1");
        String input = Util.ReadFileOneLine("day2/input.txt");
        List<Integer> inputList = inputToIntegerList(input);
        System.out.println(inputList);

        //Make a copy of input
        List<Integer> firstPartList = deepCloneList(inputList);

        //Evaluate List (noun are verb are 12 and 2 on this part)
        evaluate(firstPartList, 12, 2);
        System.out.println("The first position is " + firstPartList.get(0));

        System.out.println("Day 2 - Part 2");
        int expectedValue = 19690720;
        boolean found = false;
        int noun = 0;
        int verb = 0;
        List<Integer> secondPartList;
        while (!found && noun <= 99) {
            verb = 0;
            while (!found && verb <= 99) {
                secondPartList = deepCloneList(inputList);

                evaluate(secondPartList, noun, verb);

                if (secondPartList.get(0) == expectedValue) {
                    found = true;
                    System.out.println(secondPartList);
                    System.out.println("Correct combinacion!! --> noun='" + noun + "'   verb='" + verb + "'");
                } else {
                    //Increase verb
                    verb++;
                }
            }
            if (!found) {
                noun++;
            }
        }

        int finalCalculation = (100 * noun) + verb;
        System.out.println("The final result is: " + finalCalculation);
    }

    private void evaluate(List<Integer> list, int noun, int verb) {
        //Replace position 1 with value '12' and position 2 with value '2'
        list.set(1, noun);
        list.set(2, verb);

        int currentPos = 0;
        boolean finish = false;
        while (!finish) {
            int currentValue = list.get(currentPos);
            switch (currentValue) {
                case 1:
                    int firstValue = list.get(list.get(currentPos + 1));
                    int secondValue = list.get(list.get(currentPos + 2));
                    int storeAtPosition = list.get(currentPos + 3);
                    list.set(storeAtPosition, firstValue + secondValue);
                    break;
                case 2:
                    firstValue = list.get(list.get(currentPos + 1));
                    secondValue = list.get(list.get(currentPos + 2));
                    storeAtPosition = list.get(currentPos + 3);
                    list.set(storeAtPosition, firstValue * secondValue);
                    break;
                case 99:
                    finish = true;
                    System.out.println(" 99 - Se acaba");
                    break;
                default:
                    break;
            }
            currentPos += 4;
        }
    }

    private List<Integer> deepCloneList(List<Integer> list) {
        List<Integer> newList = new ArrayList<>();

        for (Integer el : list) {
            newList.add(el);
        }

        return newList;
    }

    private List<Integer> inputToIntegerList(String input) {
        List<Integer> list = new ArrayList<>();

        for (String token : input.split(",")) {
            list.add(Integer.valueOf(token));
        }

        return list;
    }

}
