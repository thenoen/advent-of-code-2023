package sk.thenoen.aoc.y2023.solution;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day13Test {


	Day13 day13 = new Day13();

	@Test
	void solvePart1Sample() {
		final long solution = day13.solvePart1("day13/sample.txt");
		System.out.println("solution: " + solution);
		assertEquals(405, solution);
	}

	@Test
	void solvePart1SampleCustom1() {
		final long solution = day13.solvePart1("day13/sample_custom1.txt");
		System.out.println("solution: " + solution);
		assertEquals(3, solution);
	}

//	@Disabled
	@Test
	void solvePart1() {
		final long solution = day13.solvePart1("day13/input.txt");
		System.out.println("solution: " + solution);
		assertTrue(solution > 29950);
		assertEquals(31877, solution);
	}

}