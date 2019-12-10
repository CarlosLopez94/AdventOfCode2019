package adventofcode2019.day7;

import adventofcode2019.Day;
import adventofcode2019.Util;
import adventofcode2019.day5.Day5;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Day7 implements Day {
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
        List<List<Integer>> allPhaseSettingSequences = getAllPhaseSettingSequence(0, 4);

        int maxSignal = Integer.MIN_VALUE;
        for (List<Integer> phaseSettingSequence : allPhaseSettingSequences) {
            //Calculate signal for this PhaseSettingSequence
            cleanAllQueues(intCodeComputers);
            int currentSignal;
            try {
                currentSignal = getSignal(intCodeComputers, phaseSettingSequence, 0);
                maxSignal = Math.max(maxSignal, currentSignal);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("The maximum signal is: " + maxSignal);
        System.out.println("Day 7 - Part 2");

        intCodeComputers = initIntCodeComputers(instructions, names, true);

        //Calculate all possible PhaseSettingSequences (permutations)
        allPhaseSettingSequences = getAllPhaseSettingSequence(5, 9);

        maxSignal = Integer.MIN_VALUE;
        int currentSignal;
        for (List<Integer> phaseSettingSequence : allPhaseSettingSequences) {
            cleanAllQueues(intCodeComputers);
            //Calculate signal for this PhaseSettingSequence
            try {
                currentSignal = getSignal(intCodeComputers, phaseSettingSequence, 0);
                maxSignal = Math.max(maxSignal, currentSignal);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("The maximum signal with feedback is: " + maxSignal);

    }

    public List<IntCodeComputer> initIntCodeComputers(List<Integer> instructions, List<String> names, boolean feedback) {
        List<IntCodeComputer> intCodeComputers = new ArrayList<>();

        BlockingQueue<Integer> firstIn = new ArrayBlockingQueue<>(5);
        IntCodeComputer lastIntCodeComputer = null;
        for (String name : names) {
            if (lastIntCodeComputer == null) {
                lastIntCodeComputer = new IntCodeComputer(name, instructions, firstIn, new ArrayBlockingQueue<>(5), feedback);
            } else {
                lastIntCodeComputer = new IntCodeComputer(name, instructions, lastIntCodeComputer.outQueue, new ArrayBlockingQueue<>(5), feedback);
            }
            System.out.println("Create IntCodeComputer '" + name + "'" + (feedback ? " in feedback mode" : ""));
            intCodeComputers.add(lastIntCodeComputer);
        }

        if (feedback) {
            intCodeComputers.get(0).inQueue = intCodeComputers.get(intCodeComputers.size() - 1).outQueue;
        }

        return intCodeComputers;
    }

    public void cleanAllQueues(List<IntCodeComputer> intCodeComputers) {
        for (IntCodeComputer currentIntCodeComputer : intCodeComputers) {
            currentIntCodeComputer.cleanQueues();
        }
    }

    public int getSignal(List<IntCodeComputer> intCodeComputers, List<Integer> phaseSettingSequence, int initialInput) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for (IntCodeComputer currentIntCodeComputer : intCodeComputers) {
            int index = intCodeComputers.indexOf(currentIntCodeComputer);

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
    }


    private List<List<Integer>> getAllPhaseSettingSequence(int minValue, int maxValue) {
        List<Integer> permutationsList = new ArrayList<>();
        //Possible values are {0, 1, 2, 3, 4}

        for (int i = minValue; i <= maxValue; i++) {
            permutationsList.add(i);
        }

        //Get all permutations
        return Util.getAllPermutations(permutationsList);
    }

    private class IntCodeComputer extends Day5 implements Runnable {
        private String name;
        private BlockingQueue<Integer> inQueue;
        private BlockingQueue<Integer> outQueue;
        private List<Integer> instructions;
        private boolean feedback;

        private boolean usedPhaseValue;
        private Integer signal;

        public IntCodeComputer(String name, List<Integer> instructions, BlockingQueue<Integer> inQueue, BlockingQueue<Integer> outQueue, boolean feedback) {
            this.name = name;
            this.instructions = Util.deepCloneList(instructions);
            this.inQueue = inQueue;
            this.outQueue = outQueue;
            this.feedback = feedback;
        }

        @Override
        public int getNextInput(int input) {
            int newInput = input;
            try {
                newInput = inQueue.take(); // phase value
                if (usedPhaseValue && !feedback) {
                    inQueue.put(newInput); //Feeds himself
                } else {
                    usedPhaseValue = true;
                }
            } catch (
                    InterruptedException e) {
                e.printStackTrace();
            }
            return super.getNextInput(newInput);
        }

        @Override
        public void processNewOutput(List<Integer> outputList, int newOutput) {
            if (feedback) {
                try {
                    outQueue.put(newOutput);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            signal = newOutput;
            super.processNewOutput(outputList, newOutput);
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

                super.evaluate(Util.deepCloneList(instructions), 0);

                if (!feedback) {
                    outQueue.put(signal);
                }
            } catch (Exception e) {
                System.out.println("ERROR EN '" + name + "'");
                e.printStackTrace();
            }
        }
    }


}
