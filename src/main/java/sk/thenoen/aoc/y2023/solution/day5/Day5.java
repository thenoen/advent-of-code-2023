package sk.thenoen.aoc.y2023.solution.day5;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day5 {

	public long solvePart1Naively(String inputPath) {

		final Scanner scanner = new Scanner(Day5.class.getClassLoader().getResourceAsStream(inputPath));

		final List<Long> seeds = parseSeeds(scanner);

		scanner.nextLine();//empty line

		String nextLine;

		scanner.nextLine();// seed-to-soil map:
		Map<Long, Long> seedToSoil = new LinkedHashMap<>();
		while (!(nextLine = scanner.nextLine()).contains(":")) {
			if (nextLine.isEmpty()) {
				continue;
			}
			parseMap(nextLine, seedToSoil);
		}

		scanner.nextLine();// soil-to-fertilizer map:
		Map<Long, Long> soilToFertilizer = new LinkedHashMap<>();
		while (!(nextLine = scanner.nextLine()).contains(":")) {
			if (nextLine.isEmpty()) {
				continue;
			}
			parseMap(nextLine, soilToFertilizer);
		}

		scanner.nextLine();// fertilizer-to-water map:
		Map<Long, Long> fertilizerToWater = new LinkedHashMap<>();
		while (!(nextLine = scanner.nextLine()).contains(":")) {
			if (nextLine.isEmpty()) {
				continue;
			}
			parseMap(nextLine, fertilizerToWater);
		}

		scanner.nextLine();// water-to-light map:
		Map<Long, Long> waterToLight = new LinkedHashMap<>();
		while (!(nextLine = scanner.nextLine()).contains(":")) {
			if (nextLine.isEmpty()) {
				continue;
			}
			parseMap(nextLine, waterToLight);
		}

		scanner.nextLine();// light-to-temperature map:
		Map<Long, Long> lightToTemperature = new LinkedHashMap<>();
		while (!(nextLine = scanner.nextLine()).contains(":")) {
			if (nextLine.isEmpty()) {
				continue;
			}
			parseMap(nextLine, lightToTemperature);
		}

		scanner.nextLine();// temperature-to-humidity map:
		Map<Long, Long> temperatureToHumidity = new LinkedHashMap<>();
		while (!(nextLine = scanner.nextLine()).contains(":")) {
			if (nextLine.isEmpty()) {
				continue;
			}
			parseMap(nextLine, temperatureToHumidity);
		}

		scanner.nextLine();// humidity-to-location map:
		Map<Long, Long> humidityToLocation = new LinkedHashMap<>();
		while (scanner.hasNextLine() && !(nextLine = scanner.nextLine()).contains(":")) {
			if (nextLine.isEmpty()) {
				continue;
			}
			parseMap(nextLine, humidityToLocation);
		}

		final Optional<Long> min = seeds.stream()
										.map(seed -> seedToSoil.getOrDefault(seed, seed))
										.map(soil -> soilToFertilizer.getOrDefault(soil, soil))
										.map(fertilizer -> fertilizerToWater.getOrDefault(fertilizer, fertilizer))
										.map(water -> waterToLight.getOrDefault(water, water))
										.map(light -> lightToTemperature.getOrDefault(light, light))
										.map(temperature -> temperatureToHumidity.getOrDefault(temperature, temperature))
										.map(humidity -> humidityToLocation.getOrDefault(humidity, humidity))
										.sorted()
										.min(Long::compareTo);

		return min.get();
	}

	public long solvePart1(String inputPath) {

		final Scanner scanner = new Scanner(Day5.class.getClassLoader().getResourceAsStream(inputPath));

		final List<Long> seeds = parseSeeds(scanner);

		scanner.nextLine();//empty line
		scanner.nextLine();//empty line

		final List<TripleMap> seedToSoil = getTripleMaps(scanner);
		List<TripleMap> soilToFertilizer = getTripleMaps(scanner);
		List<TripleMap> fertilizerToWater = getTripleMaps(scanner);
		List<TripleMap> waterToLight = getTripleMaps(scanner);
		List<TripleMap> lightToTemperature = getTripleMaps(scanner);
		List<TripleMap> temperatureToHumidity = getTripleMaps(scanner);
		List<TripleMap> humidityToLocation = getTripleMaps(scanner);

		final Optional<Long> min = seeds.stream()
										.map(seed -> map(seed, seedToSoil))
										.map(soil -> map(soil, soilToFertilizer))
										.map(fertilizer -> map(fertilizer, fertilizerToWater))
										.map(water -> map(water, waterToLight))
										.map(light -> map(light, lightToTemperature))
										.map(temperature -> map(temperature, temperatureToHumidity))
										.map(humidity -> map(humidity, humidityToLocation))
										.sorted()
										.min(Long::compareTo);

		return min.get();
	}

	public long solvePart2(String inputPath) {

		final Scanner scanner = new Scanner(Day5.class.getClassLoader().getResourceAsStream(inputPath));

		final List<SeedRange> seedRanges = parseSeedRanges(scanner);

		scanner.nextLine();//empty line
		scanner.nextLine();//empty line

		final List<TripleMap> seedToSoil = getTripleMaps(scanner);
		List<TripleMap> soilToFertilizer = getTripleMaps(scanner);
		List<TripleMap> fertilizerToWater = getTripleMaps(scanner);
		List<TripleMap> waterToLight = getTripleMaps(scanner);
		List<TripleMap> lightToTemperature = getTripleMaps(scanner);
		List<TripleMap> temperatureToHumidity = getTripleMaps(scanner);
		List<TripleMap> humidityToLocation = getTripleMaps(scanner);

		final Optional<Long> min = seedRanges.stream()
											 .map(seedRange -> LongStream.range(seedRange.start, seedRange.start + seedRange.length))
											 .flatMap(LongStream::boxed)
											 //											 .parallel()
											 .map(seed -> map(seed, seedToSoil))
											 .map(soil -> map(soil, soilToFertilizer))
											 .map(fertilizer -> map(fertilizer, fertilizerToWater))
											 .map(water -> map(water, waterToLight))
											 .map(light -> map(light, lightToTemperature))
											 .map(temperature -> map(temperature, temperatureToHumidity))
											 .map(humidity -> map(humidity, humidityToLocation))
											 .parallel()
											 .min(Long::compareTo);

		return min.get();
	}

	private static List<Long> parseSeeds(Scanner scanner) {
		final String seedsString = scanner.nextLine();
		List<Long> seeds = new ArrayList<>();
		final Scanner seedsScanner = new Scanner(seedsString);
		seedsScanner.useDelimiter(":");
		seedsScanner.next();
		seedsScanner.useDelimiter(" ");
		seedsScanner.next();
		while (seedsScanner.hasNextLong()) {
			seeds.add(seedsScanner.nextLong());
		}
		return seeds;
	}

	private static List<SeedRange> parseSeedRanges(Scanner scanner) {
		final String seedsString = scanner.nextLine();
		List<SeedRange> seedRanges = new ArrayList<>();
		final Scanner seedsScanner = new Scanner(seedsString);
		seedsScanner.useDelimiter(":");
		seedsScanner.next();
		seedsScanner.useDelimiter(" ");
		seedsScanner.next();
		while (seedsScanner.hasNextLong()) {
			final long start = seedsScanner.nextLong();
			final long length = seedsScanner.nextLong();
			seedRanges.add(new SeedRange(start, length));
		}
		return seedRanges;
	}

	private static List<TripleMap> getTripleMaps(Scanner scanner) {
		String nextLine;

		List<TripleMap> seedToSoil = new ArrayList<>();
		while (scanner.hasNextLine() && !(nextLine = scanner.nextLine()).contains(":")) {
			if (nextLine.isEmpty()) {
				continue;
			}
			parseTriple(nextLine, seedToSoil);
		}
		return seedToSoil;
	}

	private long map(long source, List<TripleMap> mapList) {
		for (TripleMap tripleMap : mapList) {
			if (tripleMap.source <= source && source < (tripleMap.source + tripleMap.range)) {
				return tripleMap.destination + source - tripleMap.source;
			}
		}
		return source;
	}

	private static void parseMap(String nextLine, Map<Long, Long> seedToSoil) {
		final Scanner lineScanner = new Scanner(nextLine);
		final long destination = lineScanner.nextLong();
		final long source = lineScanner.nextLong();
		final long rangeLength = lineScanner.nextLong();

		for (long i = 0; i < rangeLength; i++) {
			seedToSoil.put(source + i, destination + i);
		}
	}

	private static void parseTriple(String nextLine, List<TripleMap> tripleMaps) {
		final Scanner lineScanner = new Scanner(nextLine);
		final long destination = lineScanner.nextLong();
		final long source = lineScanner.nextLong();
		final long rangeLength = lineScanner.nextLong();
		tripleMaps.add(new TripleMap(source, destination, rangeLength));
	}

	private record TripleMap(long source,
							 long destination,
							 long range) {

	}

	private record SeedRange(long start,
							 long length) {

	}
}
