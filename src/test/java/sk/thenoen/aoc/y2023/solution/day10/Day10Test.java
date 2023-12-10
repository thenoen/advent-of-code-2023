package sk.thenoen.aoc.y2023.solution.day10;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day10Test {

	Day10 day10 = new Day10();

	@Test
	void solvePart1Sample1() {
		final long solution = day10.solvePart1("day10/sample1.txt");
		assertEquals(4, solution);
	}

	@Test
	void solvePart1Sample2() {
		final long solution = day10.solvePart1("day10/sample2.txt");
		assertEquals(8, solution);
	}

	@Test
	void solvePart1() {
		final long solution = day10.solvePart1("day10/input.txt");
		System.out.println("Solution: " + solution);
		assertEquals(6690, solution);
	}

	@Test
	public void solvePart2Sample1() {
		final long solution = day10.solvePart2("day10/part2.sample1.txt");
		assertEquals(4, solution);
	}

	@Test
	public void solvePart2Sample2() {
		final long solution = day10.solvePart2("day10/part2.sample2.txt");
		assertEquals(4, solution);
	}

	@Test
	public void solvePart2Sample3() {
		final long solution = day10.solvePart2("day10/part2.sample3.txt");
		assertEquals(8, solution);
	}

	@Test
	public void solvePart2Sample4() {
		final long solution = day10.solvePart2("day10/part2.sample4.txt");
		assertEquals(10, solution);
	}

}