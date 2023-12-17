package sk.thenoen.aoc.y2023.solution;

import java.util.ArrayList;
import java.util.List;

public class Day13 {

	public long solvePart1(String inputPath) {

		final ArrayList<String> lines = Utils.loadLines(inputPath);

		List<List<String>> patterns = new ArrayList<>();

		long sum = 0;

		List<String> patternLines = new ArrayList<>();
		for (String line : lines) {
			if (!line.isBlank()) {
				patternLines.add(line);
			} else {
				patterns.add(patternLines);
				patternLines = new ArrayList<>();
			}
		}
		patterns.add(patternLines);

		for (List<String> pattern : patterns) {
			pattern.forEach(System.out::println);
			System.out.println();
			sum += calculateReflection(pattern);
		}

		return sum;
	}

	private static long calculateReflection(List<String> patternLines) {
		final char[][] mapPattern = patternLines.stream()
												.map(String::toCharArray)
												.toList()
												.toArray(char[][]::new);

		//find vertical reflection
		int verticalReflection = 0;
		for (int x = 0; x < mapPattern[0].length - 1; x++) {
			final boolean same = compareColumns(mapPattern, x, x + 1);
			if (same) {
				verticalReflection = x + 1;
				if (x + 1 < mapPattern[0].length - x) {
					// first half
					boolean allReflect = true;
					for (int i = 0; i < x; i++) {
						if (!compareColumns(mapPattern, i, 2 * x - i + 1)) {
							allReflect = false;
							verticalReflection = 0;
							break;
						}
					}

					if(allReflect) {
						break;
					}
				} else {
					// second half
					boolean allReflect = true;
					for (int i = mapPattern[0].length - 1; i > x + 1; i--) {
						if (!compareColumns(mapPattern, i, 2 * x - i + 1)) {
							allReflect = false;
							verticalReflection = 0;
							break;
						}
					}

					if(allReflect) {
						break;
					}
				}
				System.out.println("vertical reflection found at x: " + verticalReflection);
			}
		}

		//find horizontal reflection
		int horizontalReflection = 0;
		for (int y = 0; y < mapPattern.length - 1; y++) {
			final boolean same = compareRows(mapPattern, y, y + 1);

			if (same) {
				horizontalReflection = y + 1;
				if (y + 1 < mapPattern.length - y) {
					// first half
					boolean allReflect = true;
					for (int i = 0; i < y; i++) {
						if (!compareRows(mapPattern, i, 2 * y - i + 1)) {
							allReflect = false;
							horizontalReflection = 0;
							break;
						}
					}

					if(allReflect) {
						break;
					}
				} else {
					// second half
					boolean allReflect = true;
					for (int i = mapPattern.length - 1; i > y + 1; i--) {
						if (!compareRows(mapPattern, i, 2 * y - i + 1)) {
							allReflect = false;
							horizontalReflection = 0;
							break;
						}
					}

					if(allReflect) {
						break;
					}
				}
				System.out.println("horizontal reflection found at y: " + horizontalReflection);
			}
		}

		System.out.println();
		final int sum = verticalReflection + (horizontalReflection * 100);
		return sum;
	}

	private static boolean compareColumns(char[][] mapPattern, int x1, int x2) {
		boolean allSame = true;
		for (int y = 0; y < mapPattern.length; y++) {
			if (mapPattern[y][x1] != mapPattern[y][x2]) {
				allSame = false;
				break;
			}
		}
		return allSame;
	}

	private static boolean compareRows(char[][] mapPattern, int y1, int y2) {
		boolean allSame = true;
		for (int x = 0; x < mapPattern[0].length; x++) {
			if (mapPattern[y1][x] != mapPattern[y2][x]) {
				allSame = false;
				break; //return false;
			}
		}
		return allSame; //return true;
	}
}
