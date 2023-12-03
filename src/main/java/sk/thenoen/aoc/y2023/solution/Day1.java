package sk.thenoen.aoc.y2023.solution;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import com.sun.jdi.InvalidLineNumberException;

public class Day1 {

	public static final String C_1 = "one";
	public static final String C_2 = "two";
	public static final String C_3 = "three";
	public static final String C_4 = "four";
	public static final String C_5 = "five";
	public static final String C_6 = "six";
	public static final String C_7 = "seven";
	public static final String C_8 = "eight";
	public static final String C_9 = "nine";

	public long solvePart1(String inputPath) {

		final ArrayList<String> lines = loadLines(inputPath);

		long calibrationValue = 0;
		for (String line : lines) {

			List<Character> characters = line.chars()
											 .mapToObj(i -> (char) i)
											 .filter(Character::isDigit)
											 .toList();

			final Long number = Long.valueOf(String.valueOf(characters.getFirst()) + String.valueOf(characters.getLast()));
			calibrationValue += number;
		}

		return calibrationValue;
	}

	public long solvePart2(String inputPath) {

		List<String> lines = loadLines(inputPath);

		//		final String one = "one23".replace("one", "1");

		UnaryOperator<String> one = s -> s.replace(C_1, "1");
		UnaryOperator<String> two = s -> s.replace(C_2, "2");
		UnaryOperator<String> three = s -> s.replace(C_3, "3");
		UnaryOperator<String> four = s -> s.replace(C_4, "4");
		UnaryOperator<String> five = s -> s.replace(C_5, "5");
		UnaryOperator<String> six = s -> s.replace(C_6, "6");
		UnaryOperator<String> seven = s -> s.replace(C_7, "7");
		UnaryOperator<String> eight = s -> s.replace(C_8, "8");
		UnaryOperator<String> nine = s -> s.replace(C_9, "9");

		Map<UnaryOperator<String>, String> cToF = Map.of(one, C_1,
														 two, C_2,
														 three, C_3,
														 four, C_4,
														 five, C_5,
														 six, C_6,
														 seven, C_7,
														 eight, C_8,
														 nine, C_9);

		List<UnaryOperator<String>> fncts = List.of(one, two, three, four, five, six, seven, eight, nine);

		List<String> replacedLines = new ArrayList<>();
		boolean replaceOccured = true;
		while (replaceOccured) {
			replaceOccured = replaceDigitStrings(lines, fncts, cToF, replacedLines);
			lines.clear();
			lines.addAll(replacedLines);
			replacedLines.clear();
		}

		final List<String> lineNumbers = new ArrayList<>();
		long calibrationValue = 0;
		for (String line : lines) {

			List<Character> characters = line.chars()
											 .mapToObj(i -> (char) i)
											 .filter(Character::isDigit)
											 .toList();

			final String numberString = String.valueOf(characters.getFirst()) + String.valueOf(characters.getLast());
//			lineNumbers.add(numberString);
//			writeToFile(lineNumbers, "/tmp/day1-part2.txt");
			final Long number = Long.valueOf(numberString);
			calibrationValue += number;
		}

		return calibrationValue;
	}

	private static boolean replaceDigitStrings(List<String> inputLines,
											   List<UnaryOperator<String>> fncts, Map<UnaryOperator<String>, String> cToF,
											   List<String> replacedLines) {
		boolean replaceOccurred = false;

		for (String line : inputLines) {
			final Map<Integer, UnaryOperator<String>> firstOccurrences = fncts.stream()
																			  .filter(fn -> line.contains(cToF.get(fn)))
																			  .collect(Collectors.toMap(fn -> line.indexOf(cToF.get(fn)),
																										fn -> fn));

			final Optional<Integer> min = firstOccurrences.keySet()
														  .stream()
														  .min(Integer::compareTo);

			if (min.isPresent()) {
				if (min.get() < 0) {
					throw new IllegalStateException();
				}
				final String result = firstOccurrences.get(min.get()).apply(line);
				replacedLines.add(result);
				replaceOccurred = true;
			} else {
				replacedLines.add(line);
			}
		}
		return replaceOccurred;
	}

	private static ArrayList<String> loadLines(String inputPath) {
		final Scanner scanner = new Scanner(Day1.class.getClassLoader().getResourceAsStream(inputPath));
		final ArrayList<String> lines = new ArrayList<>();
		while (scanner.hasNextLine()) {
			lines.add(scanner.nextLine());
		}
		return lines;
	}

	public long solvePart2_alternative(String inputPath) {
		final ArrayList<String> lines = loadLines(inputPath);

		final ArrayList<String> lineNumbers = new ArrayList<>();

		long calibrationValue = 0;
		for (String line : lines) {

			final List<String> digits = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9",
												C_1, C_2, C_3, C_4, C_5, C_6, C_7, C_8, C_9);

			final Map<Integer, String> firstOccurrences = digits.stream()
																.filter(line::contains)
																.collect(Collectors.toMap(line::indexOf, d -> d));

			final Map<Integer, String> lastOccurrences = digits.stream()
															   .filter(line::contains)
															   .collect(Collectors.toMap(line::lastIndexOf, d -> d));

			final Optional<Integer> min = firstOccurrences.keySet()
														  .stream()
														  .min(Integer::compareTo);

			final Optional<Integer> max = lastOccurrences.keySet()
														 .stream()
														 .max(Integer::compareTo);

			final String firstDigit = firstOccurrences.get(min.get());
			final String lastDigit = lastOccurrences.get(max.get());

			final String numberString = toStringNumber(firstDigit) + toStringNumber(lastDigit);
//			lineNumbers.add(numberString);
//			writeToFile(lineNumbers, "/tmp/day1-part2-alternative.txt");

			final long number = Long.parseLong(numberString);

			calibrationValue += number;
		}

		return calibrationValue;
	}

	private String toStringNumber(String s) {
		if (s.length() > 1) {
			return switch (s) {
				case C_1 -> "1";
				case C_2 -> "2";
				case C_3 -> "3";
				case C_4 -> "4";
				case C_5 -> "5";
				case C_6 -> "6";
				case C_7 -> "7";
				case C_8 -> "8";
				case C_9 -> "9";
				default -> throw new IllegalStateException();
			};
		} else {
			return s;
		}
	}

	private void writeToFile(List<String> lines, String pathString) {

		try (final FileWriter fileWriter = new FileWriter(pathString)) {
			for (String line : lines) {
				fileWriter.write(line + "\n");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
