package sk.thenoen.aoc.y2023.solution.day7;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day7Part2Test {

	Day7Part2 day7 = new Day7Part2();

	@Test
	void solvePart2Sample() {
		final long solution = day7.solvePart2("day7/sample.txt");
		assertEquals(5905, solution);
	}

	@Test
	void solvePart2() {
		final long solution = day7.solvePart2("day7/input.txt");
		System.out.println("solution = " + solution);
		assertEquals(253907829, solution);
	}
}