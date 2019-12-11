package adventofcode2019.day11;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntCodeComputer {
    private List<Long> instructions;
    private Map<BigInteger, BigInteger> memory;
    private BigInteger relativeBase;
    private BigInteger pointer;
    private boolean haltProcessed;

    public IntCodeComputer(List<Long> instructions) {
        this.instructions = instructions;
        restart(instructions);
    }

    public Map<BigInteger, BigInteger> restart(List<Long> instructions) {
        relativeBase = BigInteger.ZERO;
        pointer = BigInteger.ZERO;
        haltProcessed = false;
        this.memory = new HashMap<>();

        for (int i = 0; i < instructions.size(); i++) {
            memory.put(BigInteger.valueOf(i), BigInteger.valueOf(instructions.get(i)));
        }

        return memory;
    }

    public List<BigInteger> evaluateStep(int initInput) {
        BigInteger input = BigInteger.valueOf(initInput);
        haltProcessed = false;
        List<BigInteger> output = new ArrayList<>();
        boolean finish = false;
        BigInteger currentInput;
        while (!finish) {
            BigInteger instruction = memory.get(pointer);
            int opcode = instruction.mod(BigInteger.valueOf(100)).intValue();
            int firstParamMode = instruction.divide(BigInteger.valueOf(100)).mod(BigInteger.TEN).intValue();
            int secondParamMode = instruction.divide(BigInteger.valueOf(1000)).mod(BigInteger.TEN).intValue();
            int thirdParamMode = instruction.divide(BigInteger.valueOf(10000)).mod(BigInteger.TEN).intValue();

            BigInteger param1;
            BigInteger param2;
            BigInteger param3;
            switch (opcode) {
                case 1:
                    param1 = getNextParam(memory, pointer.add(BigInteger.ONE));
                    param1 = getValue(memory, param1, firstParamMode, relativeBase, false);

                    param2 = getNextParam(memory, pointer.add(BigInteger.valueOf(2)));
                    param2 = getValue(memory, param2, secondParamMode, relativeBase, false);

                    param3 = getNextParam(memory, pointer.add(BigInteger.valueOf(3)));
                    param3 = getValue(memory, param3, thirdParamMode, relativeBase, true);

                    memory.put(param3, param1.add(param2));
                    //increase pointer
                    pointer = pointer.add(BigInteger.valueOf(4));
                    break;
                case 2:
                    param1 = getNextParam(memory, pointer.add(BigInteger.valueOf(1)));
                    param1 = getValue(memory, param1, firstParamMode, relativeBase, false);

                    param2 = getNextParam(memory, pointer.add(BigInteger.valueOf(2)));
                    param2 = getValue(memory, param2, secondParamMode, relativeBase, false);

                    param3 = getNextParam(memory, pointer.add(BigInteger.valueOf(3)));
                    param3 = getValue(memory, param3, thirdParamMode, relativeBase, true);


                    memory.put(param3, param1.multiply(param2));
                    //increse pointer
                    pointer = pointer.add(BigInteger.valueOf(4));
                    break;
                case 3:
                    param1 = getNextParam(memory, pointer.add(BigInteger.valueOf(1)));
                    param1 = getValue(memory, param1, firstParamMode, relativeBase, true);

                    currentInput = getNextInput(input);

                    memory.put(param1, currentInput);

                    //increse pointer
                    pointer = pointer.add(BigInteger.valueOf(2));
                    break;
                case 4:
                    param1 = getNextParam(memory, pointer.add(BigInteger.valueOf(1)));
                    param1 = getValue(memory, param1, firstParamMode, relativeBase, false);

                    processNewOutput(output, param1);

                    //increse pointer
                    pointer = pointer.add(BigInteger.valueOf(2));
                    finish = true;
                    break;
                case 5:
                    param1 = getNextParam(memory, pointer.add(BigInteger.valueOf(1)));
                    param1 = getValue(memory, param1, firstParamMode, relativeBase, false);

                    if (param1.compareTo(BigInteger.ZERO) != 0) {
                        param2 = getNextParam(memory, pointer.add(BigInteger.valueOf(2)));
                        param2 = getValue(memory, param2, secondParamMode, relativeBase, false);

                        pointer = param2;
                    } else {
                        pointer = pointer.add(BigInteger.valueOf(3));
                    }
                    break;
                case 6:
                    param1 = getNextParam(memory, pointer.add(BigInteger.valueOf(1)));
                    param1 = getValue(memory, param1, firstParamMode, relativeBase, false);

                    if (param1.compareTo(BigInteger.ZERO) == 0) {
                        param2 = getNextParam(memory, pointer.add(BigInteger.valueOf(2)));
                        param2 = getValue(memory, param2, secondParamMode, relativeBase, false);

                        pointer = param2;
                    } else {
                        pointer = pointer.add(BigInteger.valueOf(3));
                    }
                    break;
                case 7:
                    param1 = getNextParam(memory, pointer.add(BigInteger.valueOf(1)));
                    param1 = getValue(memory, param1, firstParamMode, relativeBase, false);

                    param2 = getNextParam(memory, pointer.add(BigInteger.valueOf(2)));
                    param2 = getValue(memory, param2, secondParamMode, relativeBase, false);

                    param3 = getNextParam(memory, pointer.add(BigInteger.valueOf(3)));
                    param3 = getValue(memory, param3, thirdParamMode, relativeBase, true);

                    if (param1.compareTo(param2) < 0) {
                        memory.put(param3, BigInteger.ONE);
                    } else {
                        memory.put(param3, BigInteger.ZERO);
                    }
                    pointer = pointer.add(BigInteger.valueOf(4));
                    break;
                case 8:
                    param1 = getNextParam(memory, pointer.add(BigInteger.valueOf(1)));
                    param1 = getValue(memory, param1, firstParamMode, relativeBase, false);

                    param2 = getNextParam(memory, pointer.add(BigInteger.valueOf(2)));
                    param2 = getValue(memory, param2, secondParamMode, relativeBase, false);

                    param3 = getNextParam(memory, pointer.add(BigInteger.valueOf(3)));
                    param3 = getValue(memory, param3, thirdParamMode, relativeBase, true);

                    if (param1.equals(param2)) {
                        memory.put(param3, BigInteger.ONE);
                    } else {
                        memory.put(param3, BigInteger.ZERO);
                    }
                    pointer = pointer.add(BigInteger.valueOf(4));
                    break;
                case 9:
                    param1 = getNextParam(memory, pointer.add(BigInteger.valueOf(1)));
                    param1 = getValue(memory, param1, firstParamMode, relativeBase, false);

                    relativeBase = relativeBase.add(param1);
                    pointer = pointer.add(BigInteger.valueOf(2));
                    break;
                case 99:
                    finish = true;
                    haltProcessed = true;
                    break;
            }
        }

        return output;
    }

    public BigInteger getNextInput(BigInteger input) {
        return input;
    }

    public void processNewOutput(List<BigInteger> outputList, BigInteger newOutput) {
        outputList.add(newOutput);
    }

    private BigInteger getNextParam(Map<BigInteger, BigInteger> memory, BigInteger pointer) {
        return memory.get(pointer);
    }

    private BigInteger getValue(Map<BigInteger, BigInteger> memory, BigInteger paramValue, int mode, BigInteger relativeBase, boolean isSaveOperation) {
        BigInteger value = BigInteger.valueOf(0);
        if (!isSaveOperation) {
            if (mode == 0) {
                value = memory.getOrDefault(paramValue, BigInteger.ZERO);
            } else if (mode == 1) {
                value = paramValue;
            } else if (mode == 2) {
                value = memory.getOrDefault((paramValue.add(relativeBase)), BigInteger.ZERO);
            }
        } else {
            if (mode == 0 || mode == 1) {
                value = paramValue;
            } else if (mode == 2) {
                value = paramValue.add(relativeBase);
            }

        }
        return value;
    }

    public boolean isHaltProcessed() {
        return haltProcessed;
    }
}
