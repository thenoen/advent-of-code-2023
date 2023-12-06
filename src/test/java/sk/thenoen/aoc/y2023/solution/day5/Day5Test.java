package sk.thenoen.aoc.y2023.solution.day5;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day5Test {

	private Day5 day5 = new Day5();

	@Test
	void solvePart1SampleNaively() {
		final long solution = day5.solvePart1Naively("day5/sample.txt");
		assertEquals(35, solution);
	}

	@Test
	void solvePart1Sample() {
		final long solution = day5.solvePart1("day5/sample.txt");
		assertEquals(35, solution);
	}

	@Test
	void solvePart1() {
		final long solution = day5.solvePart1("day5/input.txt");
		System.out.println("solution: " + solution);
		assertEquals(324724204, solution);
	}

	@Test
	void solvePart2Sample() {
		final long solution = day5.solvePart2("day5/sample.txt");
		assertEquals(46, solution);
	}

//	@Disabled
	@Test
	void solvePart2() {
		final long solution = day5.solvePart2("day5/input.txt");
		System.out.println("solution: " + solution);
		assertTrue(104070863 > solution);
		assertEquals(104070862, solution);
	}

}