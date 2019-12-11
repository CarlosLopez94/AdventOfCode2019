package adventofcode2019.day8;

import adventofcode2019.Day;
import adventofcode2019.Util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Day8 implements Day {
    @Override
    public void main() {
        System.out.println("Day 8 - Part 1");
        String input = Util.ReadFileOneLine("day8/input.txt");

        int rows = 6;
        int columns = 25;

        List<List<Integer>> layers = separateLayers(input, rows, columns);
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
        System.out.println("Day 8 - Part 2");
        List<Integer> overlayed = overlaysLayers(layers, rows, columns);
        printLayer(overlayed, rows, columns);
    }

    private void printLayer(List<Integer> layer, int rows, int columns) {
        int[][] layerArray = new int[rows][columns];
        int cont = 0;
        for (Integer currentDigit : layer) {
            String currentChar = "░░"; //WHITE
            if (currentDigit == 1) {
                currentChar = "██"; //BLACK
            }

            if ((cont+1) % columns == 0) {
                System.out.println(currentChar);
            } else {
                System.out.print(currentChar);
            }

            cont++;
        }
    }

    private List<List<Integer>> separateLayers(String input, int rows, int columns) {
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

    private int countDigits(List<Integer> layer, int digitToCount) {
        int ocurrences = 0;

        for (Integer digit : layer) {
            if (digit == digitToCount) {
                ocurrences++;
            }
        }

        return ocurrences;
    }

    private List<Integer> overlaysLayers(List<List<Integer>> layers, int rows, int columns) {
        List<Integer> overlayed = new ArrayList<>();
        boolean found;
        int layerCont;
        int currentPixel;
        for (int i = 0; i < (rows * columns); i++) {
            found = false;
            layerCont = 0;
            while (!found && layerCont < layers.size()) {
                currentPixel = layers.get(layerCont).get(i);
                if (currentPixel != 2) { //is not transparent
                    found = true;
                    overlayed.add(currentPixel);
                }
                layerCont++;
            }
        }

        return overlayed;
    }
}
