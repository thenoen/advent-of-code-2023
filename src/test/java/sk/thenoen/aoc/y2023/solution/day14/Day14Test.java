package sk.thenoen.aoc.y2023.solution.day14;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day14Test {

	Day14 day14 = new Day14();

	@Test
	void solvePart1Sample() {
		final long solution = day14.solvePart1("day14/sample.txt");
		System.out.println("solution: " + solution);
		assertEquals(136, solution);
	}

	@Test
	void solvePart1() {
		final long solution = day14.solvePart1("day14/input.txt");
		System.out.println("solution: " + solution);
		assertEquals(110677, solution);
	}

	@Test
	void solvePart2Sample() {
		final long solution = day14.solvePart2("day14/sample.txt");
		System.out.println("solution: " + solution);
		assertEquals(64, solution);
	}

	@Test
	void solvePart2() {
		final long solution = day14.solvePart2("day14/input.txt");
		System.out.println("solution: " + solution);
		assertEquals(90551, solution);
	}

}