package sk.thenoen.aoc.y2023.solution.day15;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day15Test {

	Day15 day15 = new Day15();

	@Test
	void solvePart1Sample() {
		final long solution = day15.solvePart1("day15/sample.txt");
		System.out.println("solution: " + solution);
		assertEquals(1320, solution);
	}

	@Test
	void solvePart1() {
		final long solution = day15.solvePart1("day15/input.txt");
		System.out.println("solution: " + solution);
		assertEquals(510792, solution);
	}

	@Test
	void solvePart2Sample() {
		final long solution = day15.solvePart2("day15/sample.txt");
		System.out.println("solution: " + solution);
		assertEquals(145, solution);
	}

	@Test
	void solvePart2() {
		final long solution = day15.solvePart2("day15/input.txt");
		System.out.println("solution: " + solution);
		assertEquals(269410, solution);
	}

}