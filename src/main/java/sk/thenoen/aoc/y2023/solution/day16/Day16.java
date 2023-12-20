package sk.thenoen.aoc.y2023.solution.day16;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import sk.thenoen.aoc.y2023.solution.Utils;

public class Day16 {

	public long solvePart1(String inputPath) {

		final ArrayList<String> lines = Utils.loadLines(inputPath);

		final char[][] map = lines.stream()
								  .map(line -> line.toCharArray())
								  .toList()
								  .toArray(char[][]::new);

		final Tile[][] tiles = new Tile[map.length][map[0].length];

		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				tiles[y][x] = new Tile(map[y][x], x, y);
			}
		}

		final ArrayList<Beam> finishedBeams = new ArrayList<>();
		final ArrayList<Beam> beamsToProcess = new ArrayList<>();
		final Beam firstBeam = new Beam(1, 0);
		//		firstBeam.addTile(tiles[0][0]);
		beamsToProcess.add(firstBeam);

		long countOfEnergizedTiles = 0;

		while (!beamsToProcess.isEmpty()) {
			final Beam beam = beamsToProcess.removeLast();
			final List<Beam> newBeams = followBeam(beam, tiles);
			final List<Beam> newUniqueBeams = newBeams.stream()
													  .filter(b -> !finishedBeams.contains(b))
													  .toList();
			newUniqueBeams.forEach(beamsToProcess::addFirst);
			finishedBeams.add(beam);
			//			printMap(tiles);
			countOfEnergizedTiles = countEnergizedTiles(tiles);
		}

		final long count = countEnergizedTiles(tiles);

		return count;
	}

	private static long countEnergizedTiles(Tile[][] tiles) {
		long count = 0;
		for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[0].length; x++) {
				if (tiles[y][x].energizationCount > 0) {
					count++;
				}
			}
		}
		return count;
	}

	private static void printMap(Tile[][] tiles) {
		for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[0].length; x++) {
				if (tiles[y][x].energizationCount > 0 && tiles[y][x].type == '.') {
					System.out.print("#");
				} else {
					System.out.print(tiles[y][x].type);
				}
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println();
	}

	private static void printEnergizedMap(Tile[][] tiles) {
		for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[0].length; x++) {
				if (tiles[y][x].energizationCount > 0) {
					System.out.print("#");
				} else {
					System.out.print(".");
				}
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public long solvePart2(String inputPath) {
		return 0;
	}

	private static List<Beam> followBeam(Beam beam, Tile[][] tiles) {
		final List<Beam> newBeams = new ArrayList<>();

		Tile nextTile;
		while ((nextTile = beam.findNextTile(tiles)) != null) {

			if (beam.fromLeft(nextTile) || beam.fromRight(nextTile)) {
				if (nextTile.getType() == '|') {
					final Beam top = new Beam(0, -1);
					top.addTile(nextTile);
					newBeams.add(top);
					final Beam bottom = new Beam(0, 1);
					bottom.addTile(nextTile);
					newBeams.add(bottom);
					break;
				}
			}

			if (beam.fromTop(nextTile) || beam.fromBottom(nextTile)) {
				if (nextTile.getType() == '-') {
					final Beam left = new Beam(-1, 0);
					left.addTile(nextTile);
					newBeams.add(left);
					final Beam right = new Beam(1, 0);
					right.addTile(nextTile);
					newBeams.add(right);
					break;
				}
			}

			if ((beam.fromLeft(nextTile) && nextTile.type == '/') ||
				(beam.fromRight(nextTile) && nextTile.type == '\\')) {
				final Beam newBeam = new Beam(0, -1);
				newBeam.addTile(nextTile);
				newBeams.add(newBeam);
				break;
			}

			if ((beam.fromRight(nextTile) && nextTile.type == '/') ||
				(beam.fromLeft(nextTile) && nextTile.type == '\\')) {
				final Beam newBeam = new Beam(0, 1);
				newBeam.addTile(nextTile);
				newBeams.add(newBeam);
				break;
			}

			if ((beam.fromTop(nextTile) && nextTile.type == '/') ||
				(beam.fromBottom(nextTile) && nextTile.type == '\\')) {
				final Beam newBeam = new Beam(-1, 0);
				newBeam.addTile(nextTile);
				newBeams.add(newBeam);
				break;
			}

			if ((beam.fromBottom(nextTile) && nextTile.type == '/') ||
				(beam.fromTop(nextTile) && nextTile.type == '\\')) {
				final Beam newBeam = new Beam(1, 0);
				newBeam.addTile(nextTile);
				newBeams.add(newBeam);
				break;
			}

			beam.addTile(nextTile);
		}

		return newBeams;
	}

	private static class Tile {

		private final char type;
		private final int xPosition;
		private final int yPosition;
		private int energizationCount = 0;

		public Tile(char type, int xPosition, int yPosition) {
			this.type = type;
			this.xPosition = xPosition;
			this.yPosition = yPosition;
		}

		public void increaseEnergization() {
			energizationCount++;
		}

		public char getType() {
			return type;
		}

		@Override
		public String toString() {
			return type +
				   " (" + xPosition +
				   ", " + yPosition +
				   ')';
		}
	}

	private static class Beam {

		private int xDirection;
		private int yDirection;

		private final List<Tile> tilesPath = new ArrayList<>();

		public Beam(int xDirection, int yDirection) {
			this.xDirection = xDirection;
			this.yDirection = yDirection;
		}

		public Tile findNextTile(Tile[][] tiles) {

			final Tile currentTile;
			if (tilesPath.isEmpty()) {
				return tiles[0][0];
			} else {
				currentTile = tilesPath.getLast();
			}

			final int nextY = currentTile.yPosition + yDirection;
			final int nextX = currentTile.xPosition + xDirection;

			if (nextY < tiles.length &&
				nextY >= 0 &&
				nextX < tiles[0].length &&
				nextX >= 0) {
				return tiles[nextY][nextX];
			}
			return null;

		}

		public void addTile(Tile tile) {
			tilesPath.add(tile);
			tile.increaseEnergization();
		}

		public boolean fromLeft(Tile nextTile) {
			final Tile currentTile = getCurrentTile();
			return currentTile.yPosition == nextTile.yPosition && currentTile.xPosition < nextTile.xPosition;
		}

		public boolean fromRight(Tile nextTile) {
			final Tile currentTile = getCurrentTile();
			return currentTile.yPosition == nextTile.yPosition && currentTile.xPosition > nextTile.xPosition;
		}

		public boolean fromTop(Tile nextTile) {
			final Tile currentTile = getCurrentTile();
			return currentTile.yPosition < nextTile.yPosition && currentTile.xPosition == nextTile.xPosition;
		}

		public boolean fromBottom(Tile nextTile) {
			final Tile currentTile = getCurrentTile();
			return currentTile.yPosition > nextTile.yPosition && currentTile.xPosition == nextTile.xPosition;
		}

		private Tile getCurrentTile() {
			Tile currentTile;
			if (tilesPath.isEmpty()) {
				currentTile = new Tile('.', (-1) * this.xDirection, (-1) * this.yDirection);
			} else {
				currentTile = tilesPath.getLast();
			}
			return currentTile;
		}

		@Override
		public int hashCode() {
			if (tilesPath.isEmpty()) {
				return xDirection + yDirection;
			}
			return xDirection + yDirection + tilesPath.getFirst().xPosition + tilesPath.getFirst().yPosition;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Beam other) {
				final boolean sameX = this.xDirection == other.xDirection;
				final boolean sameY = this.yDirection == other.yDirection;
				if ((tilesPath.isEmpty() && !other.tilesPath.isEmpty()) ||
					(!tilesPath.isEmpty() && other.tilesPath.isEmpty())) {
					return false;
				}
				final boolean sameStart = this.tilesPath.getFirst() == other.tilesPath.getFirst();
				if (sameX && sameY && sameStart) {
					return true;
				}
			}
			return false;
		}

		@Override
		public String toString() {
			return tilesPath.toString();
		}
	}

}
