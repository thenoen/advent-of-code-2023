package sk.thenoen.aoc.y2023.solution.day12;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class Day12Test {

	Day12 day12 = new Day12();

	@Test
	void solvePart1Sample() {
		final long solution = day12.solvePart1("day12/sample.txt");
		assertEquals(21, solution);
	}

	@Test
	void solvePart1() {
		final long solution = day12.solvePart1("day12/input.txt");
		System.out.println("Solution: " + solution);
		assertEquals(7090, solution);
	}

	@Disabled
	@Test
	void solvePart2SampleBad() {
		final long solution = day12.solvePart2Bad("day12/sample.txt");
		assertEquals(525152, solution);
	}

	@Disabled
	@Test
	void solvePart2Bad() {
		final long solution = day12.solvePart2Bad("day12/input.txt");
		System.out.println("Solution: " + solution);
		assertEquals(0, solution);
	}

// ===========================================================================

	@Test
	void solvePart1SampleSolution2() throws ExecutionException, InterruptedException {
		final long solution = day12.solvePart2("day12/sample.txt", 1);
		assertEquals(21, solution);
	}

	@Test
	void solvePart1Solution2() throws ExecutionException, InterruptedException {
		final long solution = day12.solvePart2("day12/input.txt", 1);
		System.out.println("Solution: " + solution);
		assertEquals(7090, solution);
	}

	@Test
	void solvePart2Sample() throws ExecutionException, InterruptedException {
		final long solution = day12.solvePart2("day12/sample.txt", 5);
		assertEquals(525152, solution);
	}

	@Disabled //slow
	@Test
	void solvePart2() throws ExecutionException, InterruptedException {
		final long solution = day12.solvePart2("day12/input.txt", 5);
		System.out.println("Solution: " + solution);
		assertEquals(0, solution);
	}

// ===========================================================================

	@Test
	void solvePart1SampleSolution2NextAttempt() throws ExecutionException, InterruptedException {
		final long solution = day12.solvePart2NextAttempt("day12/sample.txt", 1);
		assertEquals(21, solution);
	}

	@Test
	void solvePart1Solution2NextAttempt() throws ExecutionException, InterruptedException {
		final long solution = day12.solvePart2NextAttempt("day12/input.txt", 1);
		System.out.println("Solution: " + solution);
		assertEquals(7090, solution);
	}

	@Test
	void solvePart2SampleNextAttempt() throws ExecutionException, InterruptedException {
		final long solution = day12.solvePart2NextAttempt("day12/sample.txt", 5);
		assertEquals(525152, solution);
	}

//	@Disabled //slow
	@Test
	void solvePart2NextAttempt() throws ExecutionException, InterruptedException {
		final long solution = day12.solvePart2NextAttempt("day12/input.txt", 5);
		System.out.println("Solution: " + solution);
		assertEquals(0, solution);
	}
}