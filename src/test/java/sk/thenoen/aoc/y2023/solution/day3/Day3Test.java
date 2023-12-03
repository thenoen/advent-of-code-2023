package sk.thenoen.aoc.y2023.solution.day3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day3Test {

	Day3 day3 = new Day3();

	@Test
	void solvePart1Sample() {
		final long solution = day3.solvePart1("day3/sample.txt");
		assertEquals(4361, solution);
	}

	@Test
	void solvePart1() {
		final long solution = day3.solvePart1("day3/input.txt");
		System.out.println("solution: " + solution);
		assertTrue(532207 < solution, "too low");
		assertTrue(1170560 > solution, "loo high");
		assertEquals(532331, solution);
	}

	@Test
	void solvePart2Sample() {
		final long solution = day3.solvePart2("day3/sample.txt");
		assertEquals(467835, solution);
	}

	@Test
	void solvePart2Sample_custom1() {
		final long solution = day3.solvePart2("day3/sample_custom1.txt");
		assertEquals(0, solution);
	}

	@Test
	void solvePart2Sample_custom2() {
		final long solution = day3.solvePart2("day3/sample_custom2.txt");
		assertEquals(0, solution);
	}

	@Test
	void solvePart2Sample_custom3() {
		final long solution = day3.solvePart2("day3/sample_custom3.txt");
		assertEquals(779450, solution);
	}

	@Test
	void solvePart2() {
		final long solution = day3.solvePart2("day3/input.txt");
		System.out.println("solution: " + solution);
		assertNotEquals(70240207, solution);
		assertNotEquals(81401120, solution);
		assertTrue(81401120 < solution, "too low");
//		assertTrue( > solution, "loo high");
		assertEquals(82301120, solution);
	}

}