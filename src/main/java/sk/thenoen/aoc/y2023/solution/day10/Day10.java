package sk.thenoen.aoc.y2023.solution.day10;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
			if (test.firstNeighbour == null) {
				test = findNextTile(test, map, path);
			}
		}

		return (path.size() / 2) + (path.size() % 2);
	}

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
			if (test.firstNeighbour == null) {
				test = findNextTile(test, map, path);
			}
		}

		Tile[][] tileCounterMap = new Tile[map[0].length][map.length];
		int insideCount = 0;

		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				final Tile counter = new Tile(x, y, map[y][x]);
				if (!tileAlreadyInPath(counter, path)) {
					counter.right = countBorders(x, y, map, path, i -> i + 1, i -> i);
					counter.down = countBorders(x, y, map, path, i -> i, i -> i + 1);
					counter.left = countBorders(x, y, map, path, i -> i - 1, i -> i);
					counter.up = countBorders(x, y, map, path, i -> i, i -> i - 1);
				}
				tileCounterMap[x][y] = counter;
				if (counter.isInside()) {
					insideCount++;
				}
			}
		}

		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				System.out.print(tileCounterMap[x][y] + " ");
			}
			System.out.println();
		}

		return insideCount;
	}

	private static int countBorders(int startX, int startY, char[][] map, List<Tile> path,
									Function<Integer, Integer> xDirection,
									Function<Integer, Integer> yDirection) {
		int x = startX;
		int y = startY;

		Tile test = new Tile(x, y, '?');
		boolean inPath = tileAlreadyInPath(test, path);
		if (inPath) {
			return 0;
		}

		boolean borderDetection = false;

		int count = 0;
		while (x < map[0].length && y < map.length && x > -1 && y > -1) {
			x = xDirection.apply(x);
			y = yDirection.apply(y);

			test = new Tile(x, y, '?');

			//			inPath = tileAlreadyInPath(test, path);
			//			if (inPath && borderDetection != inPath) {
			//				borderDetection = true;
			//				count++;
			//			}
			//			if (!inPath) {
			//				borderDetection = false;
			//			}

			if (tileAlreadyInPath(test, path)) {
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
		private final char type;

		private Tile firstNeighbour;
		private Tile secondNeighbour;

		private int right = 0;
		private int down = 0;
		private int left = 0;
		private int up = 0;

		private boolean isInside() {
			return (right % 2 == 1 &&
					left % 2 == 1) ||
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
				if (this.firstNeighbour == null && matches(this.getCoordinates(), other.getFirstNeighbour())) {
					this.firstNeighbour = other;
					return true;
				}
				if (this.secondNeighbour == null && matches(this.getCoordinates(), other.getSecondNeighbour())) {
					this.secondNeighbour = other;
					return true;
				}
				return false;
			}
			if (this.firstNeighbour == null && matches(this.getFirstNeighbour(), other.getCoordinates()) &&
				(matches(this.getCoordinates(), other.getFirstNeighbour()) || matches(this.getCoordinates(), other.getSecondNeighbour()))) {
				this.firstNeighbour = other;
				return true;
			}
			if (this.secondNeighbour == null && matches(this.getSecondNeighbour(), other.getCoordinates()) &&
				(matches(this.getCoordinates(), other.getFirstNeighbour()) || matches(this.getCoordinates(), other.getSecondNeighbour()))) {
				this.secondNeighbour = other;
				return true;
			}
			return false;
		}

		public int[] getFirstNeighbour() {
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

		public int[] getSecondNeighbour() {
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
			return type + "";
		}
	}

	private static boolean matches(int[] a, int[] b) {
		return a[0] == b[0] && a[1] == b[1];
	}
}
