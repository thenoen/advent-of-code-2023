package sk.thenoen.aoc.y2023.solution.day11;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import sk.thenoen.aoc.y2023.solution.Utils;

public class Day11 {

	public long solvePart1(String inputPath) {
		final ArrayList<String> lines = Utils.loadLines(inputPath);

		final Pattern pattern = Pattern.compile("^\\.+$");

		List<String> expandedRows = new ArrayList<>();
		for (String line : lines) {
			if (pattern.matcher(line).matches()) {
				expandedRows.add(line);
			}
			expandedRows.add(line);
		}

		List<String> rotatedRows = new ArrayList<>();
		for (int i = 0; i < expandedRows.get(0).length(); i++) {
			String newRow = "";
			for (String expandedRow : expandedRows) {
				newRow += expandedRow.charAt(i);
			}
			if (pattern.matcher(newRow).matches()) {
				//				for (long l = 0; l < 1_000_000L; l++) {
				rotatedRows.add(newRow);
				//				}
			}
			rotatedRows.add(newRow);
		}

		List<String> universeStrings = new ArrayList<>();
		for (int i = 0; i < rotatedRows.get(0).length(); i++) {
			String newRow = "";
			for (String rotatedRow : rotatedRows) {
				newRow += rotatedRow.charAt(i);
			}
			universeStrings.add(newRow);
		}

		//		universeStrings.forEach(System.out::println);

		List<Star> stars = new ArrayList<>();

		for (int y = 0; y < universeStrings.size(); y++) {
			String universeRow = universeStrings.get(y);
			for (int x = 0; x < universeRow.length(); x++) {
				if (universeRow.charAt(x) == '#') {
					stars.add(new Star(x, y));
				}
			}
		}

		long sum = 0;

		for (int i = 0; i < stars.size(); i++) {
			Star star = stars.get(i);
			for (int j = i + 1; j < stars.size(); j++) {
				Star other = stars.get(j);
				final long distance = star.distance(other);
				sum += distance;
			}
		}

		return sum;
	}

	public long solvePart2(String inputPath, long expansionConstant) {
		final ArrayList<String> lines = Utils.loadLines(inputPath);

		final Pattern pattern = Pattern.compile("^\\.+$");

		List<String> expandedRows = new ArrayList<>();
		List<Long> indexesOfExpandedRows = new ArrayList<>();
		for (int i = 0; i < lines.size(); i++) {

			if (pattern.matcher(lines.get(i)).matches()) {
				indexesOfExpandedRows.add((long) i);
				for (long l = 0; l < expansionConstant - 1; l++) {
					expandedRows.add(lines.get(i));
				}
			}
			expandedRows.add(lines.get(i));
		}

		List<String> rotatedRows = new ArrayList<>();
		List<Long> indexesOfExpandedColumns = new ArrayList<>();
		for (int i = 0; i < expandedRows.get(0).length(); i++) {
			String newRow = "";
			for (String expandedRow : expandedRows) {
				newRow += expandedRow.charAt(i);
			}
			if (pattern.matcher(newRow).matches()) {
				indexesOfExpandedColumns.add((long) i);
				for (long l = 0; l < expansionConstant - 1; l++) {
					rotatedRows.add(newRow);
				}
			}
			rotatedRows.add(newRow);
		}

		//		List<String> universeStrings = new ArrayList<>();
		//		for (int i = 0; i < rotatedRows.get(0).length(); i++) {
		//			String newRow = "";
		//			for (int j = 0; j < rotatedRows.size(); j++) {
		//				newRow += rotatedRows.get(j).charAt(i);
		//			}
		//			universeStrings.add(newRow);
		//			System.out.println("processed row " + i + " of " + rotatedRows.get(0).length());
		//		}

		List<Star> stars = new ArrayList<>();

		for (int y = 0; y < lines.size(); y++) {
			String universeRow = lines.get(y);
			for (int x = 0; x < universeRow.length(); x++) {
				if (universeRow.charAt(x) == '#') {
					stars.add(new Star(x, y));
				}
			}
		}

		long sum = 0;

		for (int i = 0; i < stars.size(); i++) {
			Star star = stars.get(i);
			for (int j = i + 1; j < stars.size(); j++) {
				Star other = stars.get(j);
				final long distance = star.distance(other, indexesOfExpandedColumns, indexesOfExpandedRows, expansionConstant);
				sum += distance;
			}
		}

		return sum;
	}

	private static class Star {

		int x;
		int y;

		public Star(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public long distance(Star other) {
			return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
		}

		public long distance(Star other,
							 List<Long> indexesOfExpandedColumns,
							 List<Long> indexesOfExpandedRows,
							 long expansionConstant) {
			long xDistance = Math.abs(this.x - other.x);
			if (this.x < other.x) {
				final long count = indexesOfExpandedColumns.stream()
														   .filter(index -> index > this.x && index < other.x)
														   .count();
				xDistance = (xDistance - count) + (count * expansionConstant);
			} else {
				final long count = indexesOfExpandedColumns.stream()
														   .filter(index -> index > other.x && index < this.x)
														   .count();
				xDistance = (xDistance - count) + (count * expansionConstant);
			}

			long yDistance = Math.abs(this.y - other.y);
			if (this.y < other.y) {
				final long count = indexesOfExpandedRows.stream()
														.filter(index -> index > this.y && index < other.y)
														.count();
				yDistance = (yDistance - count) + (count * expansionConstant);
			} else {
				final long count = indexesOfExpandedRows.stream()
														.filter(index -> index > other.y && index < this.y)
														.count();
				yDistance = (yDistance - count) + (count * expansionConstant);
			}

			return xDistance + yDistance;
		}
	}

}
