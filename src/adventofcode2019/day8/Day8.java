package adventofcode2019.day8;

import adventofcode2019.Day;
import adventofcode2019.Util;

import java.util.ArrayList;
import java.util.List;

public class Day8 implements Day {
    @Override
    public void main() {
        System.out.println("Day 8 - Part 1");
        String input = Util.ReadFileOneLine("day8/input.txt");

        List<List<Integer>> layers = separateLayers(input, 6, 25);
        System.out.println(layers);

        int minZeros = Integer.MAX_VALUE;
        List<Integer> minLayer = null;
        for (List<Integer> currentLayer : layers) {
            int currentMin = countDigits(currentLayer, 0);
            if (currentMin < minZeros) {
                minZeros = currentMin;
                minLayer = currentLayer;
            }
        }

        int productOnesTwos = countDigits(minLayer, 1) * countDigits(minLayer, 2);
        System.out.println("The product of '1' and '2' of the layer with greatest ocurrences of '0' is: " + productOnesTwos);
    }

    public List<List<Integer>> separateLayers(String input, int rows, int columns) {
        List<List<Integer>> layers = new ArrayList<>();
        int charsPerLayer = rows * columns;
        List<Integer> currentLayer = new ArrayList<>();
        for (char character : input.toCharArray()) {
            if (currentLayer.size() >= charsPerLayer) {
                layers.add(currentLayer);
                currentLayer = new ArrayList<>();
            }
            currentLayer.add(Integer.valueOf("" + character));
        }

        if (currentLayer.size() > 0) {
            layers.add(currentLayer);
        }

        return layers;
    }

    public int countDigits(List<Integer> layer, int digitToCount) {
        int ocurrences = 0;

        for (Integer digit : layer) {
            if (digit == digitToCount) {
                ocurrences++;
            }
        }

        return ocurrences;
    }
}
