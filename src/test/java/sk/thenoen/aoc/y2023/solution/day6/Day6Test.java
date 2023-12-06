package sk.thenoen.aoc.y2023.solution.day6;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day6Test {

	Day6 day6 = new Day6();

	@Test
	void solvePart1Sample() {
		final long solution = day6.solvePart1("day6/sample.txt");
		assertEquals(288, solution);
	}

	@Test
	void solvePart1() {
		final long solution = day6.solvePart1("day6/input.txt");
		System.out.println("solution: " + solution);
		assertEquals(1108800, solution);
	}

	@Test
	void solvePart2Sample() {
		final long solution = day6.solvePart2("day6/sample.txt");
		assertEquals(71503, solution);
	}

	@Test
	void solvePart2() {
		final long solution = day6.solvePart2("day6/input.txt");
		System.out.println("solution: " + solution);
		assertEquals(36919753, solution);
	}

}