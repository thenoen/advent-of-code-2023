package sk.thenoen.aoc.y2023.solution.day10;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import sk.thenoen.aoc.y2023.solution.Utils;

public class Day10 {

	public long solvePart1(String inputPath) {

		final ArrayList<String> lines = Utils.loadLines(inputPath);
		lines.addFirst(".".repeat(lines.get(0).length()));
		lines.addLast(".".repeat(lines.get(0).length()));
		final char[][] map = lines.stream()
								  .map(line -> ("." + line + ".").toCharArray())
								  .toList()
								  .toArray(char[][]::new);

		Tile start = null;
		for (int y = 0; y < map[0].length; y++) {
			for (int x = 0; x < map.length; x++) {
				if (map[y][x] == 'S') {
					start = new Tile(x, y, map[y][x]);
				}
			}
		}

		System.out.println(start);

		List<Tile> path = new ArrayList<>();
		path.add(start);

		findNextTile(start, map, path);
		Tile test;

		test = path.getLast();
		while (test != null) {
			if (test.nextNeighbour == null) {
				test = findNextTile(test, map, path);
			}
		}

		return (path.size() / 2) + (path.size() % 2);
	}

	// terribly slow and ugly code
	public long solvePart2(String inputPath) {

		final ArrayList<String> lines = Utils.loadLines(inputPath);
		lines.addFirst(".".repeat(lines.get(0).length()));
		lines.addLast(".".repeat(lines.get(0).length()));
		final char[][] map = lines.stream()
								  .map(line -> ("." + line + ".").toCharArray())
								  .toList()
								  .toArray(char[][]::new);

		Tile start = null;
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				if (map[y][x] == 'S') {
					start = new Tile(x, y, map[y][x]);
				}
			}
		}

		List<Tile> path = new ArrayList<>();
		path.add(start);

		findNextTile(start, map, path);
		Tile test;

		test = path.getLast();
		while (test != null) {
			//			if (test.firstNeighbour == null) {
			test = findNextTile(test, map, path);
			//			}
		}

		//fix previous and next tiles
		for (int i = 0; i < path.size(); i++) {
			path.get(i).nextNeighbour = path.get((i + 1) % path.size());
			path.get((i + 1) % path.size()).previousNeighbour = path.get(i);
		}

		Map<Tile, Tile> edges = new LinkedHashMap<>();

		Tile edgeStart = start;
		for (int i = 1; i < path.size(); i++) {
			if (path.get(i).isCorner()) {
				edges.put(edgeStart, path.get(i));
				edgeStart.nextCorner = path.get(i);
				path.get(i).previousCorner = edgeStart;
				edgeStart = path.get(i);
			}
		}
		edges.put(edgeStart, path.getLast().nextNeighbour);
		edgeStart.nextCorner = path.getLast().nextNeighbour;
		path.getLast().nextNeighbour.previousCorner = edgeStart;

		final CountInsideTiles result = getCountInsideTiles(map, path, edges);

		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				final Tile tile = result.tileCounterMap()[x][y];
				String type;
				if (path.contains(tile)) {
					type = tile.toString();
				} else if (tile.isInside()) {
					type = "x";
				} else {
					type = ".";
				}
				System.out.print(type + " ");
			}
			System.out.println();
		}

		return result.insideCount();
	}

	private static CountInsideTiles getCountInsideTiles(char[][] map, List<Tile> path, Map<Tile, Tile> edges) {
		final int xDimension = map[0].length;
		final int yDimension = map.length;
		Tile[][] tileCounterMap = new Tile[xDimension][yDimension];
		int insideCount = 0;

		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				final Tile testTile = new Tile(x, y, map[y][x]);
				tileCounterMap[x][y] = testTile;
				if (tileAlreadyInPath(testTile, path)) {
					continue;
				}

				//check crossed edges

				Set<Tile> crossedEdges = new HashSet<>();
				for (int testX = 0; testX < testTile.x; testX++) {
					int testY = testTile.y;
					crossedEdges.addAll(countCrossedEdges(edges, testX, testY));
				}
				Set<Tile> edgeStartsToRemove = findFullyCrossedEdges(crossedEdges, edges);
				crossedEdges.removeAll(edgeStartsToRemove);
				testTile.left = crossedEdges.size();
				crossedEdges.clear();

				for (int testX = testTile.x; testX < xDimension; testX++) {
					int testY = testTile.y;
					crossedEdges.addAll(countCrossedEdges(edges, testX, testY));
				}
				edgeStartsToRemove = findFullyCrossedEdges(crossedEdges, edges);
				crossedEdges.removeAll(edgeStartsToRemove);
				testTile.right = crossedEdges.size();
				crossedEdges.clear();

				for (int testY = 0; testY < testTile.y; testY++) {
					int testX = testTile.x;
					crossedEdges.addAll(countCrossedEdges(edges, testX, testY));
				}
				// remove ALL full crossed  with U-shaped siblings
				edgeStartsToRemove = findFullyCrossedEdges(crossedEdges, edges);
				crossedEdges.removeAll(edgeStartsToRemove);
				testTile.up = crossedEdges.size();
				crossedEdges.clear();

				for (int testY = testTile.y; testY < yDimension; testY++) {
					int testX = testTile.x;
					crossedEdges.addAll(countCrossedEdges(edges, testX, testY));
				}
				edgeStartsToRemove = findFullyCrossedEdges(crossedEdges, edges);
				crossedEdges.removeAll(edgeStartsToRemove);
				testTile.down = crossedEdges.size();
				crossedEdges.clear();

				if (testTile.isInside()) {
					insideCount++;
				}
			}
		}
		CountInsideTiles result = new CountInsideTiles(tileCounterMap, insideCount);
		return result;
	}

	private record CountInsideTiles(Tile[][] tileCounterMap, int insideCount) {

	}

	private static Set<Tile> countCrossedEdges(Map<Tile, Tile> edges, int testX, int testY) {
		Set<Tile> crossedEdgeStarts = new HashSet<>();
		for (Tile eStart : edges.keySet()) {
			final Tile eEnd = eStart.nextCorner; //edges.get(eStart);
			if (crossesEdge(eStart, eEnd, testX, testY)) {
				crossedEdgeStarts.add(eStart);
			}
		}

		return crossedEdgeStarts;
	}

	private static Set<Tile> findFullyCrossedEdges(Set<Tile> crossedEdgeStarts, Map<Tile, Tile> edges) {
		Set<Tile> edgeStartsToRemove = new HashSet<>();

		for (Tile crossedEdgeStart : crossedEdgeStarts) {
			final Tile crossedEdgeEnd = edges.get(crossedEdgeStart);
			Tile w1Start = crossedEdgeStart.previousCorner;//findStartForEnd(crossedEdgeStart, edges);
			if (crossedEdgeStarts.contains(crossedEdgeEnd)
				&& crossedEdgeStarts.contains(w1Start)) {
				// is fully crossed
				// find orientation fo wings
				; // needs start of edge that ends with crossedEdgeStart
				Tile w1End = crossedEdgeStart;
				Tile w2Start = crossedEdgeEnd;
				Tile w2End = edges.get(w2Start);
				if (w1End.x == w2Start.x) { //horizontally parallel
					//vertical
					if ((w1Start.x > w1End.x && w2End.x > w1End.x) ||
						(w1Start.x < w1End.x && w2End.x < w1End.x)) {
						//same orientation
						edgeStartsToRemove.add(crossedEdgeStart);
					} else {
						//opposite orientation
					}
				} else if (w1End.y == w2Start.y) { //vertically parallel
					//horizontal
					if ((w1Start.y > w1End.y && w2End.y > w1End.y) ||
						(w1Start.y < w1End.y && w2End.y < w1End.y)) {
						//same orientation
						edgeStartsToRemove.add(crossedEdgeStart);
					} else {
						//opposite orientation
					}
				} else {
					throw new IllegalStateException("should not happen");
				}
			}
		}
		return edgeStartsToRemove;
	}

	private static Tile findStartForEnd(Tile start, Map<Tile, Tile> edges) {
		for (Map.Entry<Tile, Tile> edge : edges.entrySet()) {
			if (edge.getValue() == start) {
				return edge.getKey();
			}
		}
		throw new IllegalStateException("can't happen");
	}

	private static boolean crossesEdge(Tile eStart, Tile eEnd, int testX, int testY) {
		if (Math.min(eStart.x, eEnd.x) <= testX && testX <= Math.max(eStart.x, eEnd.x) &&
			Math.min(eStart.y, eEnd.y) <= testY && testY <= Math.max(eStart.y, eEnd.y)) {
			return true;
		}
		return false;
	}

	private static int countBorders(int startX, int startY, char[][] map, List<Tile> path,
									Function<Integer, Integer> xDirection,
									Function<Integer, Integer> yDirection,
									Predicate<Tile> edgeTest) {
		int x = startX;
		int y = startY;

		Tile test = new Tile(x, y, map[y][x]);
		boolean inPath = tileAlreadyInPath(test, path);
		if (inPath) {
			return 0;
		}

		boolean borderDetection = false;

		int count = 0;
		while (x < map[0].length - 1 && y < map.length - 1 && x > 0 && y > 0) {
			x = xDirection.apply(x);
			y = yDirection.apply(y);

			test = new Tile(x, y, map[y][x]);

			//			inPath = tileAlreadyInPath(test, path);
			//			if (/*edgeTest.test(test) && */inPath && borderDetection != inPath) {
			//				borderDetection = true;
			//				count++;
			//			}
			//			if (!inPath) {
			//				borderDetection = false;
			//			}

			final boolean edgeTypeTest = edgeTest.test(test);
			if (tileAlreadyInPath(test, path) && edgeTypeTest) {
				count++;
			}
		}
		return count;
	}

	private static class Counter {

		private int right = 0;
		private int down = 0;
		private int left = 0;
		private int up = 0;

		private boolean isInside() {
			return right % 2 == 1 &&
				   down % 2 == 1 &&
				   left % 2 == 1 &&
				   up % 2 == 1;
		}
	}

	private static Tile findNextTile(Tile start, char[][] map, List<Tile> path) {

		Tile test = new Tile(start.x + 1, start.y, map[start.y][start.x + 1]);
		if (!tileAlreadyInPath(test, path) && start.canConnectWith(test)) {
			path.add(test);
			return test;
		}

		test = new Tile(start.x, start.y - 1, map[start.y - 1][start.x]);
		if (!tileAlreadyInPath(test, path) && start.canConnectWith(test)) {
			path.add(test);
			return test;
		}

		test = new Tile(start.x - 1, start.y, map[start.y][start.x - 1]);
		if (!tileAlreadyInPath(test, path) && start.canConnectWith(test)) {
			path.add(test);
			return test;
		}

		test = new Tile(start.x, start.y + 1, map[start.y + 1][start.x]);
		if (!tileAlreadyInPath(test, path) && start.canConnectWith(test)) {
			path.add(test);
			return test;
		}
		return null;
	}

	private static boolean tileAlreadyInPath(Tile tile, List<Tile> path) {
		for (Tile pathTile : path) {
			if (matches(tile.getCoordinates(), pathTile.getCoordinates())) {
				return true;
			}
		}
		return false;
	}

	private List<Tile> findNeighbours(Tile tile, char[][] map) {
		switch (tile.type) {
			case 'S':
				return List.of();
			case 'E':
				return List.of();
			case 'N':
				return List.of();
			case 'W':
				return List.of();
			case 'X':
				return List.of();
			default:
				throw new IllegalArgumentException("Unknown tile type: " + tile.type);
		}
	}

	private static class Tile {

		private final int x;
		private final int y;
		private char type;

		private Tile nextNeighbour;
		private Tile previousNeighbour;
		private Tile nextCorner;
		private Tile previousCorner;

		private int right = 0;
		private int down = 0;
		private int left = 0;
		private int up = 0;

		private boolean isCorner() {
			// S may not be always a Corner
			return List.of('F', '7', 'L', 'J', 'S').contains(this.type);
		}

		private boolean isInside() {
			return (right % 2 == 1 &&
					left % 2 == 1) &&
				   (down % 2 == 1 &&
					up % 2 == 1);
		}

		public Tile(int x, int y, char type) {
			this.x = x;
			this.y = y;
			this.type = type;
		}

		private int[] getCoordinates() {
			return new int[]{x, y};
		}

		public boolean canConnectWith(Tile other) {
			if (other.type == '.') {
				return false;
			}
			if (this.type == 'S') {
				if (this.nextNeighbour == null && matches(this.getCoordinates(), other.getNextNeighbourCoordinates())) {
					this.previousNeighbour = other;
					//					other.nextNeighbour = this;
					return true;
				}
				if (this.previousNeighbour == null && matches(this.getCoordinates(), other.getPreviousNeighbourCoordinates())) {
					this.nextNeighbour = other;
					//					other.previousNeighbour = this;
					return true;
				}
				return false;
			}
			if (this.nextNeighbour == null && matches(this.getNextNeighbourCoordinates(), other.getCoordinates()) &&
				(matches(this.getCoordinates(), other.getNextNeighbourCoordinates()) ||
				 matches(this.getCoordinates(), other.getPreviousNeighbourCoordinates()))) {
				this.nextNeighbour = other;
				//				other.previousNeighbour = this;
				return true;
			}
			if (this.previousNeighbour == null && matches(this.getPreviousNeighbourCoordinates(), other.getCoordinates()) &&
				(matches(this.getCoordinates(), other.getNextNeighbourCoordinates()) ||
				 matches(this.getCoordinates(), other.getPreviousNeighbourCoordinates()))) {
				this.previousNeighbour = other;
				//				other.nextNeighbour = this;
				return true;
			}
			return false;
		}

		public int[] getNextNeighbourCoordinates() { //next
			return switch (type) {
				case '7' -> new int[]{x, y + 1};
				case '|' -> new int[]{x, y + 1};
				case 'J' -> new int[]{x - 1, y};
				case '-' -> new int[]{x + 1, y};
				case 'L' -> new int[]{x + 1, y};
				case 'F' -> new int[]{x + 1, y};

				default -> throw new RuntimeException("can't happen");
			};
		}

		public int[] getPreviousNeighbourCoordinates() { //previous
			return switch (type) {
				case '7' -> new int[]{x - 1, y};
				case '|' -> new int[]{x, y - 1};
				case 'J' -> new int[]{x, y - 1};
				case '-' -> new int[]{x - 1, y};
				case 'L' -> new int[]{x, y - 1};
				case 'F' -> new int[]{x, y + 1};

				default -> throw new RuntimeException("can't happen");
			};
		}

		@Override
		public String toString() {
			if (isInside()) {
				return "x";
			}
			return switch (type) {
				case 'F' -> "╔";
				case '7' -> "╗";
				case '|' -> "║";
				case 'J' -> "╝";
				case '-' -> "═";
				case 'L' -> "╚";
				default -> type + "";
			};
		}
	}

	private static boolean matches(int[] a, int[] b) {
		return a[0] == b[0] && a[1] == b[1];
	}
}
