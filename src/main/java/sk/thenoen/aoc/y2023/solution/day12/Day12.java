package sk.thenoen.aoc.y2023.solution.day12;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

	public long solvePart2Bad(String inputPath) { // just for curiosity, too CPU and memory intensive

		final ArrayList<String> lines = Utils.loadLines(inputPath);

		long sum = 0;
		for (String line : lines) {

			final String[] split = line.split(" ");
			String springsPart = split[0];
			final String springs = unfold(springsPart, "?", 5);
			String groupNumbersString = unfold(split[1], ",", 5);

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

	public long solvePart2(String inputPath, int unfoldingMultiplier) throws ExecutionException, InterruptedException {

		final Map<Long, Long> longLongMap = parseGroupSizes("##..###.####?#...");

		final ArrayList<String> lines = Utils.loadLines(inputPath);

		long sum = 0;
		List<Future<Long>> futures = new ArrayList<>();

		try (ExecutorService executorService = Executors.newFixedThreadPool(11)) {
			for (String line : lines) {

				final String[] split = line.split(" ");
				String springsPart = split[0];
				final String springs = unfold(springsPart, "?", unfoldingMultiplier);
				String groupNumbersString = unfold(split[1], ",", unfoldingMultiplier);

				final Condition condition = buildCondition(groupNumbersString);

				long index = lines.indexOf(line) + 1;

				//				final long lineCount = verifyPermutations(springs, condition);

				final Future<Long> future = executorService.submit(() -> {
					System.out.println("Line: " + index + " of " + lines.size() + " ... ");
					return verifyPermutations(springs, condition);
				});
				futures.add(future);

				//				System.out.println("\tcount: " + lineCount);
				//				sum += lineCount;
			}

		}

		for (Future<Long> future : futures) {
			sum += future.get();
		}

		return sum;
	}

	private static long verifyPermutations(String springs, Condition condition) {

		final Map<Character, Long> typeCounts = getTypeCounts(springs);

		// more gears than needed
		final Long currentGearCount = typeCounts.getOrDefault('#', 0L);
		if (currentGearCount > condition.gearsCount) {
			return 0;
		}

		// not enough placeholders
		final long availablePlaceholdersCount = typeCounts.getOrDefault('?', 0L);
		if ((availablePlaceholdersCount + currentGearCount) < condition.gearsCount) {
			return 0;
		}

		final Map<Long, Long> currentGroupSizes = parseGroupSizes(springs);
		//		for (int i = 0; i < condition.groupSizes.size(); i++) {
		//			if (currentGroupSizes.get((long) i) == null) {
		//				break; //continue for next recursion (possible optimization)
		//			}
		//			if (currentGroupSizes.get((long) i) > condition.groupSizes.get(i)) {
		//				return 0;
		//			}
		//		}

		for (Long l : currentGroupSizes.keySet()) {
			if (!currentGroupSizes.get(l).equals(condition.groupSizes.get(l.intValue()))) {
				return 0;
			}
		}

		if (springs.contains("?")) {
			long sum = 0;
			sum += verifyPermutations(springs.replaceFirst("\\?", "."), condition);
			sum += verifyPermutations(springs.replaceFirst("\\?", "#"), condition);
			//			System.out.println("\t... sum: " + sum);
			return sum;
		} else {
			if (condition.pattern.matcher(springs).matches()) {
				return 1;
			}
		}
		return 0;
	}

	private static long getTypeCount(String springs, char type) {
		final long currentGearCount = springs.chars()
											 .filter(c -> c == type)
											 .count();
		return currentGearCount;
	}

	private static Map<Character, Long> getTypeCounts(String springs) {
		final Map<Character, Long> countGroups = springs.chars()
														.mapToObj(i -> (char) i)
														.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		return countGroups;
	}

	private static String unfold(String springsPart, String delimiter, int times) {
		List<String> springsParts = new ArrayList<>();
		for (int i = 0; i < times; i++) {
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
		long countOfGears = 0;
		while (groupScanner.hasNextLong()) {
			long group = groupScanner.nextLong();
			countOfGears += group;
			regexParts.add("#{" + group + "}");
		}
		final String regex = "\\.*?" + String.join("\\.+?", regexParts) + "\\.*?";
		return regex;
	}

	private static Condition buildCondition(String groupNumbersString) {
		Scanner groupScanner = new Scanner(groupNumbersString);
		groupScanner.useDelimiter(",");
		List<String> regexParts = new ArrayList<>();
		long countOfGears = 0;
		List<Long> groupSizes = new ArrayList<>();
		while (groupScanner.hasNextLong()) {
			long group = groupScanner.nextLong();
			countOfGears += group;
			groupSizes.add(group);
			regexParts.add("#{" + group + "}");
		}
		final String regex = "\\.*?" + String.join("\\.+?", regexParts) + "\\.*?";
		return new Condition(countOfGears, Pattern.compile(regex), groupSizes);
	}

	private record Condition(long gearsCount,
							 Pattern pattern,
							 List<Long> groupSizes) {

	}

	private static Map<Long, Long> parseGroupSizes(String springs) {
		Map<Long, Long> groupSizes = new HashMap<>(100);
		long groupIndex = -1;
		long currentGroupSize = 0;

		boolean isGear = false;

		final char[] springsAsChars = springs.toCharArray();

		for (int i = 0; i < springsAsChars.length; i++) {

			final char curentChar = springsAsChars[i];
			if (!isGear && curentChar == '#') {
				isGear = true;
				groupIndex++;
				currentGroupSize++;
				//				groupSizes.put(groupIndex, currentGroupSize);
			} else if (curentChar == '#') {
				currentGroupSize++;
				//				groupSizes.put(groupIndex, currentGroupSize);
			} else if (curentChar == '.') {
				if (isGear) {
					groupSizes.put(groupIndex, currentGroupSize);
				}
				isGear = false;
				currentGroupSize = 0;
			} else if (curentChar == '?') {
				//				groupSizes.put(groupIndex, currentGroupSize);
				if (isGear) {
					groupSizes.remove(groupIndex);
				}
				return groupSizes;
			}

		}

		return groupSizes;
	}
}
