package sk.thenoen.aoc.y2023.solution.day2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day2Test {

	Day2 day2 = new Day2();

	@Test
	void solvePart1Sample() {
		final long solution = day2.solvePart1("day2/part1.sample.txt");
		assertEquals(8, solution);
	}

	@Test
	void solvePart1() {
		final long solution = day2.solvePart1("day2/part1.txt");
		System.out.println("solution: " + solution);
		assertEquals(2207, solution);
	}

	@Test
	void solvePart2Sample() {
		final long solution = day2.solvePart2("day2/part1.sample.txt");
		assertEquals(2286, solution);
	}

	@Test
	void solvePart2() {
		final long solution = day2.solvePart2("day2/part1.txt");
		System.out.println("solution: " + solution);
		assertEquals(62241, solution);
	}

}