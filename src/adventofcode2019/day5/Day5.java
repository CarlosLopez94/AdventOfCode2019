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
        //Read input
        String input = Util.ReadFileOneLine("day5/input.txt");
        List<Integer> instructions = inputToIntegerList(input);

        System.out.println("Evaluation with a input of '1': ");
        List<Integer> output = evaluate(instructions, 1);
        for (Integer out : output) {
            if (out != 0) {
                System.out.println(out);
            }
        }

        System.out.println("Day 5 - Part 2");
        // Reset input
        instructions = inputToIntegerList(input);
        
        System.out.println("Evaluation with a input of '5': ");
        output = evaluate(instructions, 5);
        System.out.println(output.get(0));
    }

    private List<Integer> evaluate(List<Integer> instructions, int input) {
        List<Integer> output = new ArrayList<>();
        int pointer = 0;
        boolean finish = false;
        while (!finish) {
            int instruction = instructions.get(pointer);
            int opcode = instruction % 100;
            boolean firstParamImmediate = ((instruction / 100) % 10) == 1;
            boolean secondParamImmediate = ((instruction / 1000) % 10) == 1;

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

                    instructions.set(param3, param1 * param2);

                    //increse pointer
                    pointer += 4;
                    break;
                case 3:
                    param1 = getNextInput(instructions, pointer + 1);
                    instructions.set(param1, input);

                    //increse pointer
                    pointer += 2;
                    break;
                case 4:
                    param1 = getNextInput(instructions, pointer + 1);
                    param1 = getValue(instructions, param1, firstParamImmediate);
                    output.add(param1);

                    //increse pointer
                    pointer += 2;
                    break;
                case 5:
                    param1 = getNextInput(instructions, pointer + 1);
                    param1 = getValue(instructions, param1, firstParamImmediate);

                    if (param1 != 0) {
                        param2 = getNextInput(instructions, pointer + 2);
                        param2 = getValue(instructions, param2, secondParamImmediate);

                        pointer = param2;
                    } else {
                        pointer += 3;
                    }
                    break;
                case 6:
                    param1 = getNextInput(instructions, pointer + 1);
                    param1 = getValue(instructions, param1, firstParamImmediate);

                    if (param1 == 0) {
                        param2 = getNextInput(instructions, pointer + 2);
                        param2 = getValue(instructions, param2, secondParamImmediate);

                        pointer = param2;
                    } else {
                        pointer += 3;
                    }
                    break;
                case 7:
                    param1 = getNextInput(instructions, pointer + 1);
                    param1 = getValue(instructions, param1, firstParamImmediate);

                    param2 = getNextInput(instructions, pointer + 2);
                    param2 = getValue(instructions, param2, secondParamImmediate);

                    param3 = getNextInput(instructions, pointer + 3);

                    if (param1 < param2) {
                        instructions.set(param3, 1);
                    } else {
                        instructions.set(param3, 0);
                    }
                    pointer += 4;
                    break;
                case 8:
                    param1 = getNextInput(instructions, pointer + 1);
                    param1 = getValue(instructions, param1, firstParamImmediate);

                    param2 = getNextInput(instructions, pointer + 2);
                    param2 = getValue(instructions, param2, secondParamImmediate);

                    param3 = getNextInput(instructions, pointer + 3);

                    if (param1 == param2) {
                        instructions.set(param3, 1);
                    } else {
                        instructions.set(param3, 0);
                    }
                    pointer += 4;
                    break;
                case 99:
                    finish = true;
                    break;
            }
        }

        return output;
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
