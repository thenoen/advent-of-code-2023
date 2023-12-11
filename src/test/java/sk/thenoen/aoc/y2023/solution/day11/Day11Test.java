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



	@Test
	void solvePart2Sample_1x() {
		final long solution = day11.solvePart2("day11/sample.txt",2);
		assertEquals(374, solution);
	}

	@Test
	void solvePart2Sample_10x() {
		final long solution = day11.solvePart2("day11/sample.txt",10);
		assertEquals(1030, solution);
	}
	@Test
	void solvePart2Sample_100x() {
		final long solution = day11.solvePart2("day11/sample.txt",100);
		assertEquals(8410, solution);
	}

	@Test
	void solvePart2() {
		final long solution = day11.solvePart2("day11/input.txt", 1_000_000L);
		System.out.println("Solution: " + solution);
		assertEquals(-1, solution);
	}

}