package sk.thenoen.aoc.y2023.solution.day2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sk.thenoen.aoc.y2023.solution.Utils;

public class Day2 {

	public long solvePart1(String inputPathString) {
		final ArrayList<String> lines = Utils.loadLines(inputPathString);

		final List<Game> games = lines.stream()
									  .map(Day2::parseGame)
									  .toList();

		Subset conditionSubset = new Subset(12, 13, 14);

		return games.stream()
					.filter(g -> g.isPossible(conditionSubset))
					.mapToLong(Game::id)
					.sum();
	}

	public long solvePart2(String inputPathString) {
		final ArrayList<String> lines = Utils.loadLines(inputPathString);

		final List<Game> games = lines.stream()
									  .map(Day2::parseGame)
									  .toList();

		return games.stream()
					.map(Game::minimalSubset)
					.mapToLong(Subset::power)
					.sum();
	}

	private static Game parseGame(String line) {
		final var gameScanner = new Scanner(line);

		gameScanner.next(); //skip 'Game'
		final long id = Long.parseLong(gameScanner.next().replace(":", ""));

		gameScanner.useDelimiter(";");

		List<Subset> subsets = new ArrayList<>();
		while (gameScanner.hasNext()) {
			final Subset subset = parseSubset(gameScanner.next());
			subsets.add(subset);
		}
		return new Game(id, subsets);
	}

	private static Subset parseSubset(String subsetString) {
		Scanner scanner = new Scanner("," + subsetString);
		scanner.useDelimiter(", ");

		Map<Color, Long> colorNumbers = new HashMap<>();

		final Pattern pattern = Pattern.compile(".*?(\\d+) (\\w+)");
		final Matcher matcher = pattern.matcher(subsetString);
		while (matcher.find()) {
			final String countGroup = matcher.group(1);
			final String colorGroup = matcher.group(2);
			colorNumbers.put(Color.fromString(colorGroup), Long.parseLong(countGroup));
		}

		return new Subset(colorNumbers.getOrDefault(Color.RED, 0L),
						  colorNumbers.getOrDefault(Color.GREEN, 0L),
						  colorNumbers.getOrDefault(Color.BLUE, 0L));
	}

	private static enum Color {
		RED, GREEN, BLUE;

		public static Color fromString(String string) {
			return Arrays.stream(values())
						 .filter(v -> v.name().toLowerCase().equals(string))
						 .findFirst()
						 .orElseThrow();
		}
	}

}
