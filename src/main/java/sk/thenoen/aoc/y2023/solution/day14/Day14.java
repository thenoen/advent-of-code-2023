package sk.thenoen.aoc.y2023.solution.day14;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.thenoen.aoc.y2023.solution.Utils;

public class Day14 {

	public static final int ITERATIONS = 1_000_000_000;

	public long solvePart1(String inputPath) {

		final ArrayList<String> inputLines = Utils.loadLines(inputPath);

		final char[][] dish = inputLines.stream()
										.map(String::toCharArray)
										.toList()
										.toArray(char[][]::new);

		printDish(dish);

		// tilt north
		for (int y = 1; y < dish.length; y++) {
			for (int x = 0; x < dish[y].length; x++) {
				if (dish[y][x] != 'O') {
					continue;
				}
				//find next empty space on the TOP
				final int emptySpaceY = findEmptySpaceNorth(y, x, dish);
				if (emptySpaceY == y) {
					continue; // stone can't be moved
				}
				dish[emptySpaceY][x] = 'O';
				dish[y][x] = '.';
			}
		}

		printDish(dish);

		final long sum = getNorthLoad(dish);

		return sum;
	}

	public long solvePart2(String inputPath) {

		final ArrayList<String> inputLines = Utils.loadLines(inputPath);

		final char[][] dish = inputLines.stream()
										.map(String::toCharArray)
										.toList()
										.toArray(char[][]::new);

		Map<Long, List<Long>> loadToIteration = new HashMap<>();
		Map<Long, List<char[][]>> loadToDish = new HashMap<>();
		long countOfConsecutiveKnownDishes = 0;
		long load = -1;
		Map<String, String> dishToDish = new HashMap<>();
		Map<String, Long> dishToIteration = new HashMap<>();

		//		printDish(dish);
		for (int i = 0; i < ITERATIONS; i++) {

			String originalDish = serializeDish(dish);
			// tilt north
			for (int y = 1; y < dish.length; y++) {
				for (int x = 0; x < dish[y].length; x++) {
					if (dish[y][x] != 'O') {
						continue;
					}
					final int emptySpaceNorth = findEmptySpaceNorth(y, x, dish);
					if (emptySpaceNorth == y) {
						continue; // stone can't be moved
					}
					dish[emptySpaceNorth][x] = 'O';
					dish[y][x] = '.';
				}
			}
			//			printDish(dish);

			// tilt west
			for (int y = 0; y < dish.length; y++) {
				for (int x = 1; x < dish[y].length; x++) {
					if (dish[y][x] != 'O') {
						continue;
					}
					final int emptySpaceWest = findEmptySpaceWest(y, x, dish);
					if (emptySpaceWest == x) {
						continue; // stone can't be moved
					}
					dish[y][emptySpaceWest] = 'O';
					dish[y][x] = '.';
				}
			}
			//			printDish(dish);

			// tilt south
			for (int y = dish.length - 1; y >= 0; y--) {
				for (int x = 0; x < dish[y].length; x++) {
					if (dish[y][x] != 'O') {
						continue;
					}
					final int emptySpaceSouth = findEmptySpaceSouth(y, x, dish);
					if (emptySpaceSouth == y) {
						continue; // stone can't be moved
					}
					dish[emptySpaceSouth][x] = 'O';
					dish[y][x] = '.';
				}
			}
			//			printDish(dish);

			// tilt east
			for (int y = 0; y < dish.length; y++) {
				for (int x = dish[y].length - 2; x >= 0; x--) {
					if (dish[y][x] != 'O') {
						continue;
					}
					final int emptySpaceEast = findEmptySpaceEast(y, x, dish);
					if (emptySpaceEast == x) {
						continue; // stone can't be moved
					}
					dish[y][emptySpaceEast] = 'O';
					dish[y][x] = '.';
				}
			}


			String updatedDish = serializeDish(dish);
			dishToIteration.put(originalDish, (long) i);

			if (updatedDish.equals(dishToDish.get(originalDish))) {
				final Long loop1 = dishToIteration.get(originalDish);
				final Long loop2 = dishToIteration.get(updatedDish);

				long remaining = ((ITERATIONS - loop2) % (i - loop2 + 1));
				i = (int) (ITERATIONS - remaining - 1);

				System.out.println("found loop: " + loop1);
			} else {
				dishToDish.put(originalDish, updatedDish);
			}
			load = getNorthLoad(dish);

		}

		final long sum = getNorthLoad(dish);

		return load;
	}

	private static String serializeDish(char[][] dish) {
		String result = "";
		for (int y = 0; y < dish.length; y++) {
			result += Arrays.toString(dish[y]) + "\n";
		}
		return result;
	}

	private static long getNorthLoad(char[][] dish) {
		long sum = 0;
		for (int y = 0; y < dish.length; y++) {
			for (int x = 0; x < dish[y].length; x++) {
				if (dish[y][x] == 'O') {
					sum += dish.length - y;
				}
			}
		}
		return sum;
	}

	private static char[][] cloneDish(char[][] dish) {
		char[][] newDish = new char[dish.length][dish[0].length];

		for (int y = 0; y < dish.length; y++) {
			for (int x = 0; x < dish[y].length; x++) {
				newDish[y][x] = dish[y][x];
			}
		}
		return newDish;
	}

	private static boolean compare(char[][] dish1, char[][] dish2) {
		for (int y = 0; y < dish1.length; y++) {
			for (int x = 0; x < dish1[y].length; x++) {
				if (dish1[y][x] != dish2[y][x]) {
					return false;
				}
			}
		}
		return true;
	}

	private static int findEmptySpaceNorth(int y, int x, char[][] dish) {
		for (int i = y - 1; i >= 0; i--) {
			if (dish[i][x] != '.') {
				return i + 1;
			}
		}
		return 0;
	}

	private static int findEmptySpaceWest(int y, int x, char[][] dish) {
		for (int i = x - 1; i >= 0; i--) {
			if (dish[y][i] != '.') {
				return i + 1;
			}
		}
		return 0;
	}

	private static int findEmptySpaceSouth(int y, int x, char[][] dish) {
		for (int i = y + 1; i < dish.length; i++) {
			if (dish[i][x] != '.') {
				return i - 1;
			}
		}
		return dish.length - 1;
	}

	private static int findEmptySpaceEast(int y, int x, char[][] dish) {
		for (int i = x + 1; i < dish[0].length; i++) {
			if (dish[y][i] != '.') {
				return i - 1;
			}
		}
		return dish[0].length - 1;
	}

	private static void printDish(char[][] dish) {
		System.out.println();
		for (int y = 0; y < dish.length; y++) {
			for (int x = 0; x < dish[y].length; x++) {
				System.out.print(dish[y][x] + " ");
			}
			System.out.println();
		}
	}

}
