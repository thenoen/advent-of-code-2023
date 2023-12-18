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

	public long solvePart2(String inputPath) {

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
			sum += calculateAlternativeReflection(pattern);
		}

		return sum;
	}

	private static long calculateAlternativeReflection(List<String> patternLines) {
		final char[][] mapPattern = patternLines.stream()
												.map(String::toCharArray)
												.toList()
												.toArray(char[][]::new);

		int verticalReflection = findVerticalReflection(mapPattern);
		int horizontalReflection = findHorizontalReflection(mapPattern);

		int alternativeVerticalReflection = findAlternativeVerticalReflection(mapPattern, verticalReflection);
		int alternativeHorizontalReflection = findAlternativeHorizontalReflection(mapPattern, horizontalReflection);

		System.out.println();
		final int sum = alternativeVerticalReflection + (alternativeHorizontalReflection * 100);
		return sum;
	}

	private static long calculateReflection(List<String> patternLines) {
		final char[][] mapPattern = patternLines.stream()
												.map(String::toCharArray)
												.toList()
												.toArray(char[][]::new);

		//find vertical reflection
		int verticalReflection = findVerticalReflection(mapPattern);

		//find horizontal reflection
		int horizontalReflection = findHorizontalReflection(mapPattern);

		System.out.println();
		final int sum = verticalReflection + (horizontalReflection * 100);
		return sum;
	}

	private static int findVerticalReflection(char[][] mapPattern) {
		int verticalReflection = 0;
		for (int x = 0; x < mapPattern[0].length - 1; x++) {
			final int differencesCount = compareColumns(mapPattern, x, x + 1);
			if (differencesCount == 0) {
				verticalReflection = x + 1;
				boolean allReflect = true;
				if (x + 1 < mapPattern[0].length - x) {
					// first half
					for (int i = 0; i < x; i++) {
						if (compareColumns(mapPattern, i, 2 * x - i + 1) != 0) {
							allReflect = false;
							verticalReflection = 0;
							break;
						}
					}

				} else {
					// second half
					for (int i = mapPattern[0].length - 1; i > x + 1; i--) {
						if (compareColumns(mapPattern, i, 2 * x - i + 1) != 0) {
							allReflect = false;
							verticalReflection = 0;
							break;
						}
					}

				}
				System.out.println("vertical reflection found at x: " + verticalReflection);
				if (allReflect) {
					break;
				}
			}
		}
		return verticalReflection;
	}

	private static int findAlternativeVerticalReflection(char[][] mapPattern, int originalVerticalReflection) {
		int verticalReflection = 0;
		for (int x = 0; x < mapPattern[0].length - 1; x++) {
			final int differencesCount = compareColumns(mapPattern, x, x + 1);
			verticalReflection = x + 1;
			if (differencesCount < 2  && verticalReflection != originalVerticalReflection) {
				int reflectionDefectCount = 0;
				if (x + 1 < mapPattern[0].length - x) {
					// first half
					for (int i = 0; i <= x; i++) {
						reflectionDefectCount += compareColumns(mapPattern, i, 2 * x - i + 1);
						if (reflectionDefectCount > 1) {
							verticalReflection = 0;
							break;
						}
					}

				} else {
					// second half
					for (int i = mapPattern[0].length - 1; i >= x + 1; i--) {
						reflectionDefectCount += compareColumns(mapPattern, i, 2 * x - i + 1);
						if (reflectionDefectCount > 1) {
							verticalReflection = 0;
							break;
						}
					}

				}
				System.out.println("alternative vertical reflection found at x: " + verticalReflection);
				if (reflectionDefectCount == 1 && verticalReflection != originalVerticalReflection) {
					return verticalReflection;
				}
			}
		}
		return 0;
	}

	private static int findHorizontalReflection(char[][] mapPattern) {
		int horizontalReflection = 0;
		for (int y = 0; y < mapPattern.length - 1; y++) {
			final int differencesCount = compareRows(mapPattern, y, y + 1);

			if (differencesCount == 0) {
				horizontalReflection = y + 1;
				boolean allReflect = true;
				if (y + 1 < mapPattern.length - y) {
					// first half
					for (int i = 0; i < y; i++) {
						if (compareRows(mapPattern, i, 2 * y - i + 1) != 0) {
							allReflect = false;
							horizontalReflection = 0;
							break;
						}
					}

				} else {
					// second half
					for (int i = mapPattern.length - 1; i > y + 1; i--) {
						if (compareRows(mapPattern, i, 2 * y - i + 1) != 0) {
							allReflect = false;
							horizontalReflection = 0;
							break;
						}
					}

				}
				System.out.println("horizontal reflection found at y: " + horizontalReflection);
				if (allReflect) {
					break;
				}
			}
		}
		return horizontalReflection;
	}

	private static int findAlternativeHorizontalReflection(char[][] mapPattern, int originalHorizontalReflection) {
		int horizontalReflection = 0;
		for (int y = 0; y < mapPattern.length - 1; y++) {
			final int differencesCount = compareRows(mapPattern, y, y + 1);

			horizontalReflection = y + 1;
			if (differencesCount < 2 && horizontalReflection != originalHorizontalReflection) {
				int intReflectionDefectCount = 0;
				if (y + 1 < mapPattern.length - y) {
					// first half
					for (int i = 0; i <= y; i++) {
						intReflectionDefectCount += compareRows(mapPattern, i, 2 * y - i + 1);
						if (intReflectionDefectCount > 1) {
							horizontalReflection = 0;
							break;
						}
					}

				} else {
					// second half
					for (int i = mapPattern.length - 1; i >= y + 1; i--) {
						intReflectionDefectCount += compareRows(mapPattern, i, 2 * y - i + 1);
						if (intReflectionDefectCount > 1) {
							horizontalReflection = 0;
							break;
						}
					}

				}
				System.out.println("alternative horizontal reflection found at y: " + horizontalReflection);
				if (intReflectionDefectCount == 1 && horizontalReflection != originalHorizontalReflection) {
					return horizontalReflection;
				}
			}
		}
		return 0;
	}

	private static int compareColumns(char[][] mapPattern, int x1, int x2) {
		int differencesCount = 0;
		for (int y = 0; y < mapPattern.length; y++) {
			if (mapPattern[y][x1] != mapPattern[y][x2]) {
				differencesCount++;
			}
		}
		return differencesCount;
	}

	private static int compareRows(char[][] mapPattern, int y1, int y2) {
		int differencesCount = 0;
		for (int x = 0; x < mapPattern[0].length; x++) {
			if (mapPattern[y1][x] != mapPattern[y2][x]) {
				differencesCount++;
			}
		}
		return differencesCount;
	}
}
