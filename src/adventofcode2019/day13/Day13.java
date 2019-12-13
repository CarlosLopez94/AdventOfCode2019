package adventofcode2019.day13;

import adventofcode2019.Day;
import adventofcode2019.GenericClasses.Point;
import adventofcode2019.Util;
import adventofcode2019.day9.Day9;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day13 implements Day {
    @Override
    public void main() {
        System.out.println("Day 13 - Part 1");
        String input = Util.ReadFileOneLine("day13/input.txt");

        List<Long> instructions = Util.stringToLongList(input, ",");
        IntCodeComputer intCodeComputer = new IntCodeComputer(instructions);

        List<BigInteger> output = intCodeComputer.evaluate(BigInteger.ZERO);

        Map<Point, Tile> squares = createSquares(output);
        System.out.println("The number of blocks is: " + countTilesOf(squares, Tile.BLOCK));
    }

    private Map<Point, Tile> createSquares(List<BigInteger> tilesInstructions) {
        Map<Point, Tile> tiles = new HashMap<>();

        for (int i = 0; i < tilesInstructions.size(); i += 3) {
            Integer x = tilesInstructions.get(i).intValue();
            Integer y = tilesInstructions.get(i + 1).intValue();
            Integer tileType = tilesInstructions.get(i + 2).intValue();
            Tile tile = parseTyle(tileType);
            tiles.put(new Point(x, y), tile);
        }

        return tiles;
    }

    private Integer countTilesOf(Map<Point, Tile> squares, Tile tileToCount) {
        Integer count = 0;

        for (Point point : squares.keySet()) {
            if (squares.get(point) == (tileToCount)) {
                count++;
            }
        }

        return count;
    }

    private Tile parseTyle(Integer tileType) {
        Tile tile = null;
        switch (tileType) {
            case 0:
                tile = Tile.EMPTY;
                break;
            case 1:
                tile = Tile.WALL;
                break;
            case 2:
                tile = Tile.BLOCK;
                break;
            case 3:
                tile = Tile.HORIZONTAL;
                break;
            case 4:
                tile = Tile.BALL;
                break;
        }
        return tile;
    }


    private enum Tile {
        EMPTY, WALL, BLOCK, HORIZONTAL, BALL
    }

    private class IntCodeComputer {
        private List<Long> instructions;
        private Map<BigInteger, BigInteger> memory;
        private BigInteger relativeBase;
        private Day9 intCodeComputer;
        private BigInteger pointer;

        public IntCodeComputer(List<Long> instructions) {
            this.intCodeComputer = new Day9();
            this.instructions = instructions;
            restart();
        }

        public void restart() {
            memory = intCodeComputer.initMemory(instructions);
            relativeBase = BigInteger.ZERO;
            pointer = BigInteger.ZERO;
        }

        public List<BigInteger> evaluate(BigInteger input) {
            return intCodeComputer.evaluate(memory, input, relativeBase);
        }
    }

}
