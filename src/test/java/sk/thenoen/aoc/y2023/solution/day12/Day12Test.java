package sk.thenoen.aoc.y2023.solution.day12;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day12Test {

	Day12 day12 = new Day12();

	@Test
	void solvePart1Sample() {
		final long solution = day12.solvePart1("day12/sample.txt");
		assertEquals(21, solution);
	}

	@Test
	void solvePart1() {
		final long solution = day12.solvePart1("day12/input.txt");
		System.out.println("Solution: " + solution);
		assertEquals(7090, solution);
	}

	@Test
	void solvePart2Sample() {
		final long solution = day12.solvePart2("day12/sample.txt");
		assertEquals(525152, solution);
	}

	@Test
	void solvePart2() {
		final long solution = day12.solvePart2("day12/input.txt");
		System.out.println("Solution: " + solution);
		assertEquals(0, solution);
	}
}