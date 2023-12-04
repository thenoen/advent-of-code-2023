package sk.thenoen.aoc.y2023.solution.day4;

import jdk.jshell.execution.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import sk.thenoen.aoc.y2023.solution.Utils;

public class Day4 {

	public long solvePart1(String inputPath) {

		final ArrayList<String> lines = Utils.loadLines(inputPath);
		long sum = 0;

		for (String line : lines) {
			final Scanner scanner = new Scanner(line);
			scanner.useDelimiter(" ");
			scanner.next();
			scanner.useDelimiter(":");
			final String id = scanner.next();// Card #:
			scanner.useDelimiter(" ");
			scanner.next(); // :

			List<Long> winningNumbers = new ArrayList<>();
			String nextToken = null;
			while (!(nextToken = scanner.next()).equals("|")) {
				if (nextToken.isEmpty()) {
					continue;
				}
				winningNumbers.add(Long.valueOf(nextToken));
			}
			//			scanner.next(); // |

			List<Long> myNumbers = scanner.tokens()
										  .filter(s -> !s.isEmpty())
										  .map(Long::valueOf)
										  .toList();

			long matches = myNumbers.stream()
									.filter(winningNumbers::contains)
									.count();

			sum += (long) Math.pow(2, (matches - 1d));
		}
		return sum;
	}

	public long solvePart2(String inputPath) {
		final ArrayList<String> lines = Utils.loadLines(inputPath);
		long currentId = 0;
		Map<Long, Long> cardMatches = new HashMap<>();
		Map<Long, Long> cardCounts = new HashMap<>();

		for (String line : lines) {
			currentId++;
			final Scanner scanner = new Scanner(line);
			scanner.useDelimiter(" ");
			scanner.next();
			scanner.useDelimiter(":");
			scanner.next();// Card #:
			scanner.useDelimiter(" ");
			scanner.next(); // :

			List<Long> winningNumbers = new ArrayList<>();
			String nextToken = null;
			while (!(nextToken = scanner.next()).equals("|")) {
				if (nextToken.isEmpty()) {
					continue;
				}
				winningNumbers.add(Long.valueOf(nextToken));
			}
			//			scanner.next(); // |

			List<Long> myNumbers = scanner.tokens()
										  .filter(s -> !s.isEmpty())
										  .map(Long::valueOf)
										  .toList();

			long matches = myNumbers.stream()
									.filter(winningNumbers::contains)
									.count();

			cardMatches.put(currentId, matches);
			cardCounts.put(currentId, 1L);

		}

		for (Long cardId : cardCounts.keySet()) {
			final Long matches = cardMatches.get(cardId);
			final Long currentCardCount = cardCounts.get(cardId);
			for (long i = 1; i <= matches; i++) {
				final Long nextCardCount = cardCounts.get(cardId + i);
				cardCounts.put(cardId + i, nextCardCount + currentCardCount);
			}
		}

		return cardCounts.values()
						 .stream()
						 .mapToLong(Long::longValue)
						 .sum();
	}

}
