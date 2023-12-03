package sk.thenoen.aoc.y2023.solution.day3;

import java.util.ArrayList;
import java.util.List;

public record PartNumber(String number,
						 int x,
						 int y) {

	public boolean isNextToGear(Gear g) {
		final List<Tuple> neighbours = getNeighbours();
		final boolean contains = neighbours.contains(new Tuple(g.x(), g.y()));
		return contains;
	}

	private List<Tuple> getNeighbours() {

		List<Tuple> neighbours = new ArrayList<>();


		for (int i = x - 1; i < (x + number.length() + 1); i++) {
			neighbours.add(new Tuple(i, y - 1));
			neighbours.add(new Tuple(i, y + 1));
		}

		neighbours.add(new Tuple(x - 1, y));
		neighbours.add(new Tuple(x + number.length(), y));

		return neighbours;
	}


}
