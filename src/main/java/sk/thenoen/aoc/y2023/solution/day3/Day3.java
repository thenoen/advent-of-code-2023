package sk.thenoen.aoc.y2023.solution.day3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sk.thenoen.aoc.y2023.solution.Utils;

public class Day3 {

	public long solvePart1(String inputPath) {

		final ArrayList<String> lines = getLines(inputPath);

		final List<PartNumber> potentialPartNumbers = parsePartNumbers(lines);

		final List<PartNumber> partNumbers = extractPartNumbers(potentialPartNumbers, lines);

		return partNumbers.stream()
						  .map(PartNumber::number)
						  .mapToLong(Long::valueOf)
						  .sum();
	}

	public long solvePart2(String inputPath) {
		final ArrayList<String> lines = getLines(inputPath);
		final List<PartNumber> potentialPartNumbers = parsePartNumbers(lines);
		final List<PartNumber> partNumbers = extractPartNumbers(potentialPartNumbers, lines);

		final List<Gear> potentialGears = parseGears(lines);

		Long sumOfGearRatios = 0L;
		long noGearCount = 0;
		long gearCount = 0;

		for (Gear potentialGear : potentialGears) {
			final List<PartNumber> neighbourParts = partNumbers.stream()
															   .filter(pn -> pn.isNextToGear(potentialGear))
															   .toList();
			if (neighbourParts.size() == 2) {
				// IS A GEAR
				gearCount++;
				final long firstPartNumber = Long.parseLong(neighbourParts.get(0).number());
				final long secondPartNumber = Long.parseLong(neighbourParts.get(1).number());
				long newSum = sumOfGearRatios + (firstPartNumber * secondPartNumber);
				if (newSum < sumOfGearRatios) {
					System.out.println("overflow");
				}
				sumOfGearRatios = newSum;
			} else {
				// NOT A GEAR
				noGearCount++;
//				System.out.println("not a gear");
				if (neighbourParts.size() != 1) {
					System.out.println("unexpected during sample test");
				}
			}
		}

		return sumOfGearRatios;
	}

	private static List<PartNumber> extractPartNumbers(List<PartNumber> potentialPartNumbers, ArrayList<String> lines) {
		final List<PartNumber> partNumbers = new ArrayList<>(potentialPartNumbers.size());

		for (PartNumber potentialPartNumber : potentialPartNumbers) {
			//			System.out.println(potentialPartNumber.number());

			final String above = lines.get(potentialPartNumber.y() - 1)
									  .substring(potentialPartNumber.x() - 1,
												 potentialPartNumber.x() + potentialPartNumber.number().length() + 1);
			final String below = lines.get(potentialPartNumber.y() + 1)
									  .substring(potentialPartNumber.x() - 1,
												 potentialPartNumber.x() + potentialPartNumber.number().length() + 1);

			final String before = lines.get(potentialPartNumber.y())
									   .substring(potentialPartNumber.x() - 1, potentialPartNumber.x());

			final String after = lines.get(potentialPartNumber.y())
									  .substring(potentialPartNumber.number().length() + potentialPartNumber.x(),
												 potentialPartNumber.number().length() + potentialPartNumber.x() + 1);

			final String all = above + below + before + after;
			final String clean = all.replaceAll("\\.", "");
			if (!clean.isEmpty()) {
				partNumbers.add(potentialPartNumber);

			} else {
				//				System.out.println("not part number: " + potentialPartNumber.number());
			}
		}
		return partNumbers;
	}

	private static ArrayList<String> getLines(String inputPath) {
		final ArrayList<String> tmpLines = Utils.loadLines(inputPath);
		final ArrayList<String> lines = new ArrayList<>();

		tmpLines.forEach(line -> {
			lines.add("." + line + ".");
		});
		lines.add(0, ".".repeat(lines.get(0).length()));
		lines.add(".".repeat(lines.get(0).length()));
		return lines;
	}

	private static List<PartNumber> parsePartNumbers(ArrayList<String> lines) {
		final Pattern pattern = Pattern.compile("(\\d+)");
		List<PartNumber> partNumbers = new ArrayList<>();
		for (String line : lines) {

			final Map<String, Integer> previousLocations = new HashMap<>();
			final Matcher matcher = pattern.matcher(line);
			int prevIndex = 0;
			while (matcher.find()) {
				final String number = matcher.group(1);
				final int start = line.indexOf(number, previousLocations.getOrDefault(number, prevIndex));
				final int lineIndex = lines.indexOf(line);
				previousLocations.put(number, start + 1);
				prevIndex = start + number.length();
				partNumbers.add(new PartNumber(number, start, lineIndex));
			}

			//			final List<String> distinct = partNumbers.stream()
			//													 .map(PartNumber::number)
			//													 .sorted()
			//													 .distinct()
			//													 .toList();
			//			if (distinct.size() != partNumbers.size()) {
			//				System.out.println("extra case");
			//			}
			//			partNumbers.clear();//todo: temporary for investigation of extra cases
		}
		return partNumbers;
	}

	private static List<Gear> parseGears(ArrayList<String> lines) {
		final Pattern pattern = Pattern.compile("(\\*)");
		List<Gear> gears = new ArrayList<>();
		for (String line : lines) {

			final Map<String, Integer> previousLocations = new HashMap<>();
			final Matcher matcher = pattern.matcher(line);
			while (matcher.find()) {
				final String number = matcher.group(1);
				final int start = line.indexOf(number, previousLocations.getOrDefault(number, 0));
				final int lineIndex = lines.indexOf(line);
				previousLocations.put(number, start + 1);
				gears.add(new Gear(start, lineIndex));
			}

			//			final List<String> distinct = gears.stream()
			//													 .map(PartNumber::number)
			//													 .sorted()
			//													 .distinct()
			//													 .toList();
			//			if (distinct.size() != gears.size()) {
			//				System.out.println("extra case");
			//			}
			//			gears.clear();//todo: temporary for investigation of extra cases
		}
		return gears;
	}

}
