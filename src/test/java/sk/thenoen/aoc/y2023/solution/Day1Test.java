package sk.thenoen.aoc.y2023.solution;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day1Test {

	Day1 day1 = new Day1();

	@Test
	void solvePart1Sample() {
		final long solution = day1.solvePart1("day1/part1.sample.txt");
		assertEquals(142, solution);
	}

	@Test
	void solvePart1() {
		final long solution = day1.solvePart1("day1/part1.txt");
		System.out.println("solution: " + solution);
		assertEquals(55017, solution);
	}

	@Test
	void solvePart2Sample() {
		final long solution = day1.solvePart2("day1/part2.sample.txt");
		assertEquals(281, solution);
	}


	@Test
	void solvePart2() {
		final long solution = day1.solvePart2("day1/part2.txt");
		System.out.println("solution: " + solution);
		assertEquals(53543, solution);
	}


	@Test
	void solvePart2_alternative() {
		final long solution = day1.solvePart2_alternative("day1/part2.txt");
		System.out.println("solution: " + solution);
		assertTrue(53543 > solution);
		assertEquals(53539, solution);
	}

}