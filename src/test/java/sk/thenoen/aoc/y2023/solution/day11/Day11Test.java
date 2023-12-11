package sk.thenoen.aoc.y2023.solution.day11;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day11Test {

	Day11 day11 = new Day11();

	@Test
	void solvePart1Sample1() {
		final long solution = day11.solvePart1("day11/sample.txt");
		assertEquals(374, solution);
	}

	@Test
	void solvePart1() {
		final long solution = day11.solvePart1("day11/input.txt");
		System.out.println("Solution: " + solution);
		assertEquals(9521550, solution);
	}

}