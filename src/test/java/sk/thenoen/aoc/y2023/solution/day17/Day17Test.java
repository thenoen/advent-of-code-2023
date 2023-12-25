package sk.thenoen.aoc.y2023.solution.day17;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day17Test {

	Day17 day17 = new Day17();


	@Test
	void solvePart1Sample() {
		final long solution = day17.solvePart1("day17/sample.txt");
		System.out.println("solution: " + solution);
		assertEquals(102, solution);
	}

	@Test
	void solvePart1() {
		final long solution = day17.solvePart1("day17/input.txt");
		System.out.println("solution: " + solution);
		assertTrue(solution < 1388);
		assertNotEquals(962, solution);
	}

}