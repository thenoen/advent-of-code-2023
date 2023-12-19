package sk.thenoen.aoc.y2023.solution.day15;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

import sk.thenoen.aoc.y2023.solution.Utils;

public class Day15 {

	public long solvePart1(String inputPath) {

		final String inputString = Utils.loadLines(inputPath).get(0);

		Scanner scanner = new Scanner(inputString);
		scanner.useDelimiter(",");

		long sum = 0;

		while (scanner.hasNext()) {
			final String step = scanner.next();
			System.out.println(step);

			final long currentValue = calculateHash(step);
			System.out.println(step + " -> " + currentValue);
			sum += currentValue;

		}

		return sum;
	}

	public long solvePart2(String inputPath) {
		final Pattern stepPattern = Pattern.compile("(\\w+)([=-])(\\d?)");

		final String inputString = Utils.loadLines(inputPath).get(0);

		Scanner scanner = new Scanner(inputString);
		scanner.useDelimiter(",");

		Map<Integer, List<String>> boxes = new HashMap<>(256);
		for (int i = 0; i < 256; i++) {
			boxes.put(i, new ArrayList<>());
		}

		while (scanner.hasNext()) {
			final String step = scanner.next();
			final Matcher matcher = stepPattern.matcher(step);
			final boolean matches = matcher.matches();
			if (!matches) {
				throw new RuntimeException("invalid step: " + step);
			}

			String label = matcher.group(1);
			String operation = matcher.group(2);
			String focalLength = matcher.group(3);

			final Integer boxIndex = calculateHash(label);

			System.out.println(step + " -> " + boxIndex);

			final List<String> box = boxes.get(boxIndex);
			if (operation.equals("=")) {
				final Optional<String> lensToRemove = box.stream()
														 .filter(lens -> lens.startsWith(label))
														 .findFirst();
				if (lensToRemove.isPresent()) {
					final int index = box.indexOf(lensToRemove.get());
					box.remove(lensToRemove.get());
					box.add(index, label + " " + focalLength);
				} else {
					box.add(label + " " + focalLength);
				}
			}

			if (operation.equals("-")) {
				box.removeIf(lens -> lens.startsWith(label)); // remove all lens?
			}

			boxes.keySet().stream().forEach(key -> {
				if (!boxes.get(key).isEmpty()) {
					System.out.println(key + " -> " + boxes.get(key));
				}
			});

			System.out.println();

		}

		long totalFocusingPower = 0;
		for (Integer i : boxes.keySet()) {
			final List<String> box = boxes.get(i);
			if (box.isEmpty()) {
				continue;
			}
			for (int lensIndex = 1; lensIndex <= box.size(); lensIndex++) {
				final Long lensFocalLength = Long.valueOf(box.get(lensIndex - 1).split(" ")[1]);
				totalFocusingPower += (i + 1) * lensIndex * lensFocalLength;
			}
		}

		return totalFocusingPower;
	}

	private static int calculateHash(String step) {
		int currentValue = 0;
		final char[] chars = step.toCharArray();
		for (char aChar : chars) {
			//				System.out.println(aChar + " -> " + (int) aChar);
			currentValue += (int) aChar;
			currentValue *= 17;
			currentValue %= 256;
		}
		return currentValue;
	}

}
