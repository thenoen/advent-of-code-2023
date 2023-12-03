package sk.thenoen.aoc.y2023.solution.day3;

import java.util.ArrayList;
import java.util.List;

public record Gear(int x,
				   int y) {

	public List<Tuple> getNeighbours() {
		List<Tuple> neighbours = new ArrayList<>();

		neighbours.add(new Tuple(x, y + 1));
		neighbours.add(new Tuple(x, y - 1));

		neighbours.add(new Tuple(x - 1, y));
		neighbours.add(new Tuple(x + 1, y));

		neighbours.add(new Tuple(x - 1, y - 1));
		neighbours.add(new Tuple(x + 1, y + 1));

		neighbours.add(new Tuple(x - 1, y + 1));
		neighbours.add(new Tuple(x + 1, y - 1));

		return neighbours;
	}
}

