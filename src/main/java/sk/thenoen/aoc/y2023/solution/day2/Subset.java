package sk.thenoen.aoc.y2023.solution.day2;

public record Subset(long red,
					 long green,
					 long blue) {

	public boolean isSmallerOrEqual(Subset otherSubset) {
		return red <= otherSubset.red &&
			   green <= otherSubset.green &&
			   blue <= otherSubset.blue;
	}

	public long power() {
		return red * green * blue;
	}
}
