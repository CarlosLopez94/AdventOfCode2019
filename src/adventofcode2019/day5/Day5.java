/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode2019.day5;

import adventofcode2019.Util;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author carlos
 */
public class Day5 {

    public void main() {
        System.out.println("Day 5 - Part 1");
        String input = Util.ReadFileOneLine("day5/input.txt");
        List<Integer> instructions = inputToIntegerList(input);
        evaluate(instructions, 1);
    }

    private void evaluate(List<Integer> instructions, int input) {
        //Replace position 1 with value '12' and position 2 with value '2'
//        list.set(1, noun);
        //      list.set(2, verb);
        List<Integer> output = new ArrayList<>();
        int pointer = 0;
        boolean finish = false;
        while (!finish) {
            int instruction = instructions.get(pointer);
            System.out.println(instruction);
            int opcode = instruction % 100;
            boolean firstParamImmediate = ((instruction / 100) % 10) == 1;
            boolean secondParamImmediate = ((instruction / 1000) % 10) == 1;
            boolean thirdParamImmediate = false;// ((instruction / 10000) % 10) == 1;

            int param1;
            int param2;
            int param3;
            switch (opcode) {
                case 1:
                    param1 = getNextInput(instructions, pointer + 1);
                    param1 = getValue(instructions, param1, firstParamImmediate);

                    param2 = getNextInput(instructions, pointer + 2);
                    param2 = getValue(instructions, param2, secondParamImmediate);

                    param3 = getNextInput(instructions, pointer + 3);

                    System.out.println(instruction + " --> (" + param1 + ", " + param2 + ", " + param3 + ") --> guarda " + (param1 + param2) + " en position " + param3);

                    instructions.set(param3, param1 + param2);

                    //increse pointer
                    pointer += 4;

                    break;
                case 2:
                    param1 = getNextInput(instructions, pointer + 1);
                    param1 = getValue(instructions, param1, firstParamImmediate);

                    param2 = getNextInput(instructions, pointer + 2);
                    param2 = getValue(instructions, param2, secondParamImmediate);

                    param3 = getNextInput(instructions, pointer + 3);

                    System.out.println(instruction + " --> (" + param1 + ", " + param2 + ", " + param3 + ")  --> guarda " + (param1 * param2) + " en position " + param3);

                    instructions.set(param3, param1 * param2);

                    //increse pointer
                    pointer += 4;
                    break;
                case 3:
                    param1 = getNextInput(instructions, pointer + 1);
                    instructions.set(param1, input);

                    System.out.println(instruction + " --> guarda '" + input + "' en (" + param1 + ")");

                    //increse pointer
                    pointer += 2;
                    break;
                case 4:
                    param1 = getNextInput(instructions, pointer + 1);
                    param1 = getValue(instructions, param1, firstParamImmediate);
                    output.add(param1);

                    System.out.println(instruction + " --> imprime (" + param1 + ")");

                    //increse pointer
                    pointer += 2;
                    break;
                case 99:
                    finish = true;
                    break;
            }
        }

        for (Integer code : output) {
            System.out.print(code);
        }
        System.out.println("\n" + output);
    }

    private int getNextInput(List<Integer> instructions, int pointer) {
        return instructions.get(pointer);
    }

    private int getValue(List<Integer> instructions, int input, boolean isImmediate) {
        int value;
        if (isImmediate) {
            value = input;
        } else {//memory mode
            value = instructions.get(input);
        }

        return value;
    }

    private List<Integer> inputToIntegerList(String input) {
        List<Integer> list = new ArrayList<>();

        for (String token : input.split(",")) {
            list.add(Integer.valueOf(token));
        }

        return list;
    }  
    
}
