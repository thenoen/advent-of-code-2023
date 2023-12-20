package sk.thenoen.aoc.y2023.solution.day16;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day16Test {

	Day16 day16 = new Day16();

	@Test
	void solvePart1Sample() {
		final long solution = day16.solvePart1("day16/sample.txt");
		System.out.println("solution: " + solution);
		assertEquals(46, solution);
	}

	@Test
	void solvePart1() {
		final long solution = day16.solvePart1("day16/input.txt");
		System.out.println("solution: " + solution);
		assertEquals(7482, solution);
	}

}