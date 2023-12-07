package sk.thenoen.aoc.y2023.solution.day7;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.thenoen.aoc.y2023.solution.Utils;

public class Day7 {

	public long solvePart1(String inputPath) {

		final ArrayList<String> lines = Utils.loadLines(inputPath);

		final List<Hand> sortedHands = lines.stream()
											.map(line -> line.split(" "))
											.map(this::parseHand)
											.sorted()
											.toList();

		long totalWinnings = 0;
		for (int i = 1; i <= sortedHands.size(); i++) {
			totalWinnings += i * sortedHands.get(i - 1).bid;
		}

		return totalWinnings;
	}

	private Hand parseHand(String[] strings) {
		return new Hand(strings[0], Long.parseLong(strings[1]));
	}

	private class Hand implements Comparable<Hand> {

		private String cards;
		private String sortedCards;
		private String cardsAsComparableString;
		private long bid;
		private HandType type;

		public Hand(String cards, long bid) {
			this.cards = cards;
			this.sortedCards = sortCards(cards);
			this.bid = bid;
			this.type = HandType.findHandType(sortedCards);
			setCardsAsComparableString();
		}

		private static String sortCards(String cards) {
			final String sorted = cards.chars()
									   .sorted()
									   .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
									   .toString();
			final int[] uniqueCards = sorted.chars()
											.distinct()
											.toArray();

			Map<Character, Character> patternMap = new HashMap<>();
			for (int i = 0; i < uniqueCards.length; i++) {
				patternMap.put((char) uniqueCards[i], (char) ('a' + i));
			}

			return sorted.chars()
						 .map(c -> patternMap.get((char) c))
						 .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
						 .toString();
		}

		@Override
		public int compareTo(Hand o) {
			final long rankComparison = this.type.rank - o.type.rank;
			if (rankComparison != 0) {
				return (int) rankComparison;
			}
			return this.cardsAsComparableString.compareTo(o.cardsAsComparableString);
		}

		private void setCardsAsComparableString() {
			cardsAsComparableString = cards.chars()
										   .map(c -> switch (c) {
											   case 'A' -> 'n';
											   case 'K' -> 'm';
											   case 'Q' -> 'l';
											   case 'J' -> 'k';
											   case 'T' -> 'j';
											   case '9' -> 'i';
											   case '8' -> 'h';
											   case '7' -> 'g';
											   case '6' -> 'f';
											   case '5' -> 'e';
											   case '4' -> 'c';
											   case '3' -> 'b';
											   case '2' -> 'a';
											   default -> throw new RuntimeException("Unknown card: " + c);

										   })
										   .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
										   .toString();
		}

		@Override
		public String toString() {
			return cards + "("+ type.name() + ")";
		}
	}

	private enum HandType {
		HIGH_CARD(List.of("abcde"), 1),
		ONE_PAIR(List.of("aabcd", "abbcd", "abccd", "abcdd"), 2),
		TWO_PAIRS(List.of("aabbc", "aabcc", "abbcc"), 3),
		THREE_OF_A_KIND(List.of("aaabc", "abbbc", "abccc"), 4),
		FULL_HOUSE(List.of("aabbb", "aaabb"), 5),
		FOUR_OF_A_KIND(List.of("aaaab", "abbbb"), 6),
		FIVE_OF_A_KIND(List.of("aaaaa"), 7);

		private List<String> patterns;
		private long rank;

		HandType(List<String> patterns, long rank) {
			this.patterns = patterns;
			this.rank = rank;
		}

		public static HandType findHandType(String cards) {
			for (HandType handType : HandType.values()) {
				for (String pattern : handType.patterns) {
					if (cards.matches(pattern)) {
						return handType;
					}
				}
			}

			throw new RuntimeException("No hand type found for cards: " + cards);
		}
	}

}
