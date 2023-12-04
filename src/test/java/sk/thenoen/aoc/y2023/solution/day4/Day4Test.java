package sk.thenoen.aoc.y2023.solution.day4;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day4Test {

	Day4 day4 = new Day4();

	@Test
	void solvePart1Sample() {
		final long solution = day4.solvePart1("day4/sample.txt");
		assertEquals(13, solution);
	}

	@Test
	void solvePart1() {
		final long solution = day4.solvePart1("day4/input.txt");
		System.out.println("solution: " + solution);
		assertEquals(22897, solution);
	}

	@Test
	void solvePart2Sample() {
		final long solution = day4.solvePart2("day4/sample.txt");
		assertEquals(30, solution);
	}

	@Test
	void solvePart2() {
		final long solution = day4.solvePart2("day4/input.txt");
		System.out.println("solution: " + solution);
		assertEquals(5095824, solution);
	}

}