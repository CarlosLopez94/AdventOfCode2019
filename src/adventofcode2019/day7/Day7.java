package adventofcode2019.day7;

import adventofcode2019.Util;
import adventofcode2019.day5.Day5;

import javax.jnlp.SingleInstanceListener;
import java.util.*;

public class Day7 {
    public void main() {
        String input = Util.ReadFileOneLine("day7/input.txt");
        List<Integer> instructions = Util.stringToIntegerList(input, ",");
        System.out.println(instructions);

        Day5 intCodeComputer = new Day5();
        List<List<Integer>> allPhaseSettingSequences = getAllPhaseSettingSequence();

        int maxSignal = Integer.MIN_VALUE;
        for(List<Integer> phaseSettingSequence : allPhaseSettingSequences){
            int currentSignal = getSignal(intCodeComputer, instructions, phaseSettingSequence, 0);
            maxSignal = Math.max(maxSignal, currentSignal);
        }

        System.out.println("The maximum signal is: " + maxSignal);
    }

    public int getSignal(Day5 intCodeComputer, List<Integer> input, List<Integer> phaseSettingSequence, int initialInput) {
        //Run Amplifier 'A'
        List<Integer> instructions = deepCloneList(input);
        int signalA = intCodeComputer.evaluate(instructions, phaseSettingSequence.get(0), initialInput).get(0);

        //Run Amplifier 'B'
        instructions = deepCloneList(input);
        int signalB = intCodeComputer.evaluate(instructions, phaseSettingSequence.get(1), signalA).get(0);

        //Run Amplifier 'C'
        instructions = deepCloneList(input);
        int signalC = intCodeComputer.evaluate(instructions, phaseSettingSequence.get(2), signalB).get(0);

        //Run Amplifier 'D'
        instructions = deepCloneList(input);
        int signalD = intCodeComputer.evaluate(instructions, phaseSettingSequence.get(3), signalC).get(0);

        //Run Amplifier 'E'
        instructions = deepCloneList(input);
        int signalE = intCodeComputer.evaluate(instructions, phaseSettingSequence.get(4), signalD).get(0);

        return signalE;
    }

    private List<List<Integer>> getAllPhaseSettingSequence() {
        List<Integer> originList = new ArrayList<>();
        //Possible values are {0, 1, 2, 3, 4}
        originList.add(0);
        originList.add(1);
        originList.add(2);
        originList.add(3);
        originList.add(4);

        //Get all permutations
        return Util.getAllPermutations(originList);
    }

    private List<Integer> deepCloneList(List<Integer> list) {
        List<Integer> newList = new ArrayList<>();

        for (Integer el : list) {
            newList.add(el);
        }

        return newList;
    }

}
