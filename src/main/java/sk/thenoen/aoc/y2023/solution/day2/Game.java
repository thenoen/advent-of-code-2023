package sk.thenoen.aoc.y2023.solution.day2;

import java.util.List;
import java.util.OptionalLong;
import java.util.function.ToLongFunction;

public record Game(long id,
				   List<Subset> subsets) {

	public boolean isPossible(Subset conditionSubset) {
		return !subsets.stream()
					   .anyMatch(s -> !s.isSmallerOrEqual(conditionSubset));
	}

	public Subset minimalSubset() {
		final Long minRed = getMin(Subset::red);
		final Long minGreen = getMin(Subset::green);
		final Long minBlue = getMin(Subset::blue);
		return new Subset(minRed, minGreen, minBlue);
	}

	private Long getMin(ToLongFunction<Subset> red) {
		return subsets.stream()
					  .mapToLong(red)
					  .max()
					  .orElseThrow(() -> new IllegalStateException("This can't happen"));
	}

}
