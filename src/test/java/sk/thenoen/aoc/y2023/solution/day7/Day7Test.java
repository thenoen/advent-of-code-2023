package sk.thenoen.aoc.y2023.solution.day7;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day7Test {

	Day7 day7 = new Day7();

	@Test
	void solvePart1Sample() {
		final long solution = day7.solvePart1("day7/sample.txt");
		assertEquals(6440, solution);
	}

	@Test
	void solvePart1() {
		final long solution = day7.solvePart1("day7/input.txt");
		System.out.println("solution: " + solution);
		assertEquals(253205868, solution);
	}
}