package sk.thenoen.aoc.y2023.solution.day9;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day9Test {

	Day9 day9 = new Day9();

	@Test
	void solvePart1Sample() {
		long result = day9.solvePart1("day9/sample.txt");
		assertEquals(114, result);
	}

	@Test
	void solvePart1() {
		long solution = day9.solvePart1("day9/input.txt");
		System.out.println("solution = " + solution);
		assertEquals(1995001648, solution);
	}

	@Test
	void solvePart2Sample() {
		long result = day9.solvePart2("day9/sample.txt");
		assertEquals(2, result);
	}

	@Test
	void solvePart2() {
		long solution = day9.solvePart2("day9/input.txt");
		System.out.println("solution = " + solution);
		assertEquals(988, solution);
	}

}