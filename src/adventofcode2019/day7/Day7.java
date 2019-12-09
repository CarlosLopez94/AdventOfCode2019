package adventofcode2019.day7;

import adventofcode2019.Util;
import adventofcode2019.day5.Day5;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Day7 {
    public void main() {
        System.out.println("Day 7 - Part 1");
        String input = Util.ReadFileOneLine("day7/input.txt");
        List<Integer> instructions = Util.stringToIntegerList(input, ",");

        List<String> names = new ArrayList<>();
        names.add("A");
        names.add("B");
        names.add("C");
        names.add("D");
        names.add("E");

        //Init all IntCodeComputers
        List<IntCodeComputer> intCodeComputers = initIntCodeComputers(instructions, names, false);

        //Calculate all possible PhaseSettingSequences (permutations)
        List<List<Integer>> allPhaseSettingSequences = getAllPhaseSettingSequence();

        int maxSignal = Integer.MIN_VALUE;
        for (List<Integer> phaseSettingSequence : allPhaseSettingSequences) {
            //Calculate signal for this PhaseSettingSequence
            int currentSignal;
            try {
                currentSignal = getSignal(intCodeComputers, phaseSettingSequence, 0);
                maxSignal = Math.max(maxSignal, currentSignal);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("The maximum signal is: " + maxSignal);
    }

    public List<IntCodeComputer> initIntCodeComputers(List<Integer> instructions, List<String> names, boolean feedback) {
        List<IntCodeComputer> intCodeComputers = new ArrayList<>();

        BlockingQueue<Integer> firstIn = new ArrayBlockingQueue<>(5);
        IntCodeComputer lastIntCodeComputer = null;
        for (String name : names) {
            if (lastIntCodeComputer == null) {
                lastIntCodeComputer = new IntCodeComputer(name, instructions, firstIn, new ArrayBlockingQueue<>(5));
            } else {
                lastIntCodeComputer = new IntCodeComputer(name, instructions, lastIntCodeComputer.outQueue, new ArrayBlockingQueue<>(5));
            }
            System.out.println("Create IntCodeComputer '" + name + "'");
            intCodeComputers.add(lastIntCodeComputer);
        }

        if (feedback) {
            intCodeComputers.get(0).inQueue = intCodeComputers.get(intCodeComputers.size() - 1).outQueue;
        }

        return intCodeComputers;
    }

    public int getSignal(List<IntCodeComputer> intCodeComputers, List<Integer> phaseSettingSequence, int initialInput) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for (IntCodeComputer currentIntCodeComputer : intCodeComputers) {
            int index = intCodeComputers.indexOf(currentIntCodeComputer);
            currentIntCodeComputer.cleanQueues();

            currentIntCodeComputer.inQueue.put(phaseSettingSequence.get(index));

            Thread currentThread = new Thread(currentIntCodeComputer);
            threads.add(currentThread);
            currentThread.start();
        }

        intCodeComputers.get(0).inQueue.put(initialInput);

        for (Thread thread : threads) {
            thread.join();
        }

        return intCodeComputers.get(intCodeComputers.size() - 1).signal;
        //Run Amplifier 'A'
       /* List<Integer> instructions = deepCloneList(input);
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
    */
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

    private class IntCodeComputer extends Day5 implements Runnable {
        private String name;
        private BlockingQueue<Integer> inQueue;
        private BlockingQueue<Integer> outQueue;
        private List<Integer> instructions;

        private boolean usedPhaseValue;
        private Integer signal;

        public IntCodeComputer(String name, List<Integer> instructions, BlockingQueue<Integer> inQueue, BlockingQueue<Integer> outQueue) {
            this.name = name;
            this.instructions = Util.deepCloneList(instructions);
            this.inQueue = inQueue;
            this.outQueue = outQueue;
        }

        @Override
        public int getNextInput(int input) {
            int newInput = input;
            try {
                newInput = inQueue.take(); // phase value
                if (usedPhaseValue) {
                    inQueue.put(newInput); //Feeds himself
                }else{
                    usedPhaseValue = true;
                }
                System.out.println("'" + name + "' uses his store value: " + newInput);
            } catch (
                    InterruptedException e) {
                e.printStackTrace();
            }
            return super.getNextInput(newInput);
        }

        public void cleanQueues() {
            inQueue.clear();
            outQueue.clear();
        }

        @Override
        public void run() {
            try {
                usedPhaseValue = false;
                signal = null;

                this.signal = super.evaluate(Util.deepCloneList(instructions), 0).get(0);

                System.out.println("'" + name + "' has finished!");
                System.out.println("'" + name + "' is going to output a new value " + signal);
                outQueue.put(signal);
                System.out.println("'" + name + "' has output value: " + signal);
            } catch (Exception e) {
                System.out.println("ERROR EN '" + name + "'");
                e.printStackTrace();
            }
        }
    }


}
