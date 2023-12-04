package sk.thenoen.aoc.y2023.solution.day4;

import java.util.List;

public record Card(int id,
				   List<Long> winningNumbers,
				   List<Long> matchingNumbers) {

}
