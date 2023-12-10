package sk.thenoen.aoc.y2023.solution.day8;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day8Test {

	Day8 day8 = new Day8();

	@Test
	void solvePart1Sample1() {
		final long solution = day8.solvePart1("day8/part1sample1.txt");
		assertEquals(2, solution);
	}

	@Test
	void solvePart1Sample2() {
		final long solution = day8.solvePart1("day8/part1sample2.txt");
		assertEquals(6, solution);
	}

	@Test
	void solvePart1() {
		final long solution = day8.solvePart1("day8/part1input.txt");
		System.out.println("solution = " + solution);
		assertEquals(18827, solution);
	}

}