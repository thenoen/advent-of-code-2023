package sk.thenoen.aoc.y2023.solution.day12;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import sk.thenoen.aoc.y2023.solution.Utils;

public class Day12 {

	public long solvePart1(String inputPath) {

		final ArrayList<String> lines = Utils.loadLines(inputPath);

		long sum = 0;
		for (String line : lines) {

			final String[] split = line.split(" ");
			String springs = split[0];
			String groupNumbersString = split[1];

			final String regex = buildRegex(groupNumbersString);

			final Pattern pattern = Pattern.compile(regex);

			List<Integer> wildcardIndexes = new ArrayList<>();
			for (int i = 0; i < springs.toCharArray().length; i++) {
				if (springs.toCharArray()[i] == '?') {
					wildcardIndexes.add(i);
				}
			}

			final List<String> combinations = calculateCombinations(wildcardIndexes);

			long partialSum = 0;
			for (String combination : combinations) {
				String springsCandidate = springs;
				for (int i = 0; i < wildcardIndexes.size(); i++) {
					springsCandidate = springsCandidate.replaceFirst("\\?", combination.toCharArray()[i] + "");
				}
				if (pattern.matcher(springsCandidate).matches()) {
					partialSum++;
				}
			}
			sum += partialSum;
		}

		return sum;
	}

	public long solvePart2(String inputPath) { // just for curiosity, too CPU and memory intensive

		final ArrayList<String> lines = Utils.loadLines(inputPath);

		long sum = 0;
		for (String line : lines) {

			final String[] split = line.split(" ");
			String springsPart = split[0];
			final String springs = unfold(springsPart, "?");
			String groupNumbersString = unfold(split[1], ",");

			final String regex = buildRegex(groupNumbersString);

			final Pattern pattern = Pattern.compile(regex);

			List<Integer> wildcardIndexes = new ArrayList<>();
			for (int i = 0; i < springs.toCharArray().length; i++) {
				if (springs.toCharArray()[i] == '?') {
					wildcardIndexes.add(i);
				}
			}

			final List<String> combinations = calculateCombinations(wildcardIndexes);

			long partialSum = 0;
			for (String combination : combinations) {
				String springsCandidate = springs;
				for (int i = 0; i < wildcardIndexes.size(); i++) {
					springsCandidate = springsCandidate.replaceFirst("\\?", combination.toCharArray()[i] + "");
				}
				if (pattern.matcher(springsCandidate).matches()) {
					partialSum++;
				}
			}
			sum += partialSum;
		}

		return sum;
	}

	private static String unfold(String springsPart, String delimiter) {
		List<String> springsParts = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			springsParts.add(springsPart);
		}
		String springs = String.join(delimiter, springsParts);
		return springs;
	}

	private static List<String> calculateCombinations(List<Integer> wildcardIndexes) {
		List<String> combinations = new ArrayList<>();
		combinations.add("");
		for (long i = 0; i < wildcardIndexes.size(); i++) {
			List<String> newCombinations = new ArrayList<>();
			for (String combination : combinations) {
				newCombinations.add(combination + ".");
				newCombinations.add(combination + "#");
			}
			combinations = newCombinations;
		}
		return combinations;
	}

	private static String buildRegex(String groupNumbersString) {
		Scanner groupScanner = new Scanner(groupNumbersString);
		groupScanner.useDelimiter(",");
		List<String> regexParts = new ArrayList<>();
		while (groupScanner.hasNextLong()) {
			long group = groupScanner.nextLong();
			regexParts.add("#{" + group + "}");
		}
		final String regex = "\\.*?" + String.join("\\.+?", regexParts) + "\\.*?";
		return regex;
	}
}
