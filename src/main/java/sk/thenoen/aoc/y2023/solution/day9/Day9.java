package sk.thenoen.aoc.y2023.solution.day9;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import sk.thenoen.aoc.y2023.solution.Utils;

public class Day9 {

	public long solvePart1(String inputPath) {
		final ArrayList<String> lines = Utils.loadLines(inputPath);

		List<List<Long>> valueHistories = new ArrayList<>();

		for (String line : lines) {
			List<Long> values = new ArrayList<>();
			Scanner s = new Scanner(line);
			while (s.hasNextLong()) {
				values.add(s.nextLong());
			}
			valueHistories.add(values);
		}

		List<List<Long>> interpolatedValues = new ArrayList<>();

		long sum = 0;

		for (List<Long> valueHistory : valueHistories) {

			List<List<Long>> interpolatedValuesForHistory = new ArrayList<>();
			interpolatedValuesForHistory.add(valueHistory);

			List<Long> longs = valueHistory;
			do {
				//calculate new values
				longs = newValues(longs);
				interpolatedValuesForHistory.add(longs);
			} while (!allZeroes(longs));

			System.out.println();
			interpolatedValuesForHistory.forEach(System.out::println);

			final long interpolatedValue = interpolatedValuesForHistory.stream()
																	   .filter(list -> list.size() > 1)
																	   .map(List::getLast)
																	   .mapToLong(Long::longValue)
																	   .sum();
			sum += interpolatedValue;

		}

		return sum;
	}

	public long solvePart2(String inputPath) {
		final ArrayList<String> lines = Utils.loadLines(inputPath);

		List<List<Long>> valueHistories = new ArrayList<>();

		for (String line : lines) {
			List<Long> values = new ArrayList<>();
			Scanner s = new Scanner(line);
			while (s.hasNextLong()) {
				values.add(s.nextLong());
			}
			valueHistories.add(values);
		}

		List<List<Long>> interpolatedValues = new ArrayList<>();

		long sum = 0;

		for (List<Long> valueHistory : valueHistories) {

			List<List<Long>> interpolatedValuesForHistory = new ArrayList<>();
			interpolatedValuesForHistory.add(valueHistory);

			List<Long> longs = valueHistory;
			do {
				//calculate new values
				longs = newValues(longs);
				interpolatedValuesForHistory.add(longs);
			} while (!allZeroes(longs));

			System.out.println();
			interpolatedValuesForHistory.forEach(System.out::println);

			List<Long> firstValues = interpolatedValuesForHistory.stream()
																 .filter(list -> list.size() > 1)
																 .map(List::getFirst)
																 .toList();

			long tmp = 0;
			//			List<Long> newValues = new ArrayList<>();
			for (int i = firstValues.size() - 1; i > -1; i--) {
				tmp = firstValues.get(i) - tmp;
			}
			sum += tmp;

		}

		return sum;
	}

	private List<Long> newValues(List<Long> prevValues) {
		List<Long> newValues = new ArrayList<>();
		for (int i = 0; i < prevValues.size() - 1; i++) {
			//			newValues.add(Math.abs(prevValues.get(i) - prevValues.get(i + 1)));
			newValues.add(prevValues.get(i + 1) - prevValues.get(i));
		}
		return newValues;
	}

	private boolean allZeroes(List<Long> values) {
		for (Long value : values) {
			if (value != 0) {
				return false;
			}
		}
		return true;
	}
}
