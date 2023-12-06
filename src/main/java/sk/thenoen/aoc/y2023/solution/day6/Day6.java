package sk.thenoen.aoc.y2023.solution.day6;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sk.thenoen.aoc.y2023.solution.Utils;

public class Day6 {

	public long solvePart1(String inputPath) {
		final ArrayList<String> lines = Utils.loadLines(inputPath);

		final Pattern pattern = Pattern.compile("(\\d+)");

		final Matcher timesMatcher = pattern.matcher(lines.get(0));
		final Matcher distancesMatcher = pattern.matcher(lines.get(1));

		List<Long> times = new ArrayList<>();
		List<Long> distances = new ArrayList<>();

		while (timesMatcher.find()) {
			final String timeString = timesMatcher.group();
			times.add(Long.parseLong(timeString));
			distancesMatcher.find();
			final String distanceString = distancesMatcher.group();
			distances.add(Long.parseLong(distanceString));
		}

		long waysOfBeating = 1;
		for (int i = 0; i < times.size(); i++) {
			long raceTime = times.get(i);
			long prevDistance = distances.get(i);

			long countOfOptions = 0;
			for (long holdTime = 1; holdTime < raceTime; holdTime++) {
				long distance = holdTime * (raceTime - holdTime);
				if (distance > prevDistance) {
					countOfOptions++;
				}
			}

			waysOfBeating *= countOfOptions;
		}

		return waysOfBeating;
	}

	public long solvePart2(String inputPath) {
		final ArrayList<String> lines = Utils.loadLines(inputPath);

		final Pattern pattern = Pattern.compile("(\\d+)");

		final Matcher timesMatcher = pattern.matcher(lines.get(0));
		final Matcher distancesMatcher = pattern.matcher(lines.get(1));

		String timeString = "";
		String distanceString = "";

		while (timesMatcher.find()) {
			timeString += timesMatcher.group();
			distancesMatcher.find();
			distanceString += distancesMatcher.group();
		}

		final long raceTime = Long.parseLong(timeString);
		final long prevDistance = Long.parseLong(distanceString);

		long countOfOptions = 0;
		for (long holdTime = 1; holdTime < raceTime; holdTime++) {
			long distance = holdTime * (raceTime - holdTime);
			if (distance > prevDistance) {
				countOfOptions++;
			}
		}

		return countOfOptions;
	}

}
