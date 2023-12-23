package sk.thenoen.aoc.y2023.solution.day17;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import sk.thenoen.aoc.y2023.solution.Utils;

public class Day17 {

	public long solvePart1(String inputPath) {

		final ArrayList<String> lines = Utils.loadLines(inputPath);

		final char[][] mapChars = lines.stream()
									   .map(String::toCharArray)
									   .toList()
									   .toArray(char[][]::new);

		final Tile[][] map = new Tile[mapChars.length][mapChars[0].length];
		List<Tile> tiles = new ArrayList<>();

		for (int y = 0; y < mapChars.length; y++) {
			for (int x = 0; x < mapChars[0].length; x++) {
				final Tile tile = new Tile(mapChars[y][x], x, y);
				map[y][x] = tile;
				tiles.add(tile);
			}
		}

		Tile startingTile = map[0][0];
		Tile endingTile = map[map.length - 1][map[0].length - 1];

		List<Traveller> travellers = new ArrayList<>();
		Traveller traveller = new Traveller();
		travellers.add(traveller);
		traveller.visitTile(startingTile);

		return findPath(map[0].length, map.length, map, tiles, endingTile, travellers);
	}

	private static Integer findPath(int X, int Y, Tile[][] map, List<Tile> tiles, Tile endingTile, List<Traveller> travellers) {

		int distanceAtTheEnd = Integer.MAX_VALUE;
		final Map<String, Integer> knownSubPaths = new HashMap<>(65535);

		long hits = 0;
		long misses = 0;
		long iteration = 0;

		while (true) {
			iteration++;
			// Visit all neighbour Tiles
			Set<Traveller> doneTravellers = new HashSet<>();
			List<Traveller> newTravellers = new ArrayList<>();

			//			for (Traveller t : travellers) {
			Traveller tf = travellers.getFirst();
			final List<Tile> possibleTiles = findNextPossibleSteps(tf.travelledPath, map);
			for (Tile possibleTile : possibleTiles) {
				if (!tf.alreadyVisited(possibleTile)) {
					final Traveller clonedTraveller = tf.clone();
					clonedTraveller.visitTile(possibleTile);
					newTravellers.add(clonedTraveller);
				}
			}
			//			}
			travellers.remove(tf);

			final List<Traveller> tmpAtTheEnd = newTravellers.stream()
															 .filter(t -> t.getGetCurrentTile() == endingTile)
															 .toList();

			newTravellers.removeAll(tmpAtTheEnd);
			newTravellers.forEach(travellers::addFirst);

			final Optional<Integer> potentialDistanceAtTheEnd = tmpAtTheEnd.stream()
																		   .map(Traveller::getTravelledDistance)
																		   .sorted()
																		   .findFirst();
			if (potentialDistanceAtTheEnd.isPresent()) {
				distanceAtTheEnd = Math.min(distanceAtTheEnd, potentialDistanceAtTheEnd.get());
				//				System.out.println("distanceAtTheEnd: " + distanceAtTheEnd);
			}

			for (Traveller traveller : newTravellers) {

				final Integer travelledSubDistance = traveller.getTravelledDistance();
				if (travelledSubDistance > distanceAtTheEnd) {
					doneTravellers.add(traveller);
					continue;
				}
				final Integer knownSubDistance = knownSubPaths.get(traveller.getLastSignificantSegment());

				//				if(knownSubDistance != null) {
				//					hits++;
				//				} else {
				//					misses++;
				//				}
				//				if(iteration % 100_000 == 0) {
				//					System.out.println("hit ratio: " + (double) hits / (hits + misses) * 100);
				//					System.out.println("min distance: " + distanceAtTheEnd);
				//				}

				if (knownSubDistance != null && knownSubDistance < travelledSubDistance) {
					doneTravellers.add(traveller);
				} else {
					knownSubPaths.put(traveller.getLastSignificantSegment(), travelledSubDistance);
				}
			}
			travellers.removeAll(doneTravellers);

			if (iteration % 1_000_000 == 0) {
				//					System.out.println("hit ratio: " + (double) hits / (hits + misses) * 100);
				System.out.println("min distance: " + distanceAtTheEnd);
			}

			if (travellers.isEmpty()) {
				System.out.println();
				return distanceAtTheEnd;
			}
		}
	}

	private static void printMap(Tile[][] map, Traveller traveller) {
		traveller.travelledPath.forEach(tile -> tile.setVisited(true));
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				System.out.print(map[y][x].getVisited() ? "X" : map[y][x].getHeatLoss());
			}
			System.out.println();
		}
		traveller.travelledPath.forEach(tile -> tile.setVisited(false));
		System.out.println();
	}

	private static List<Tile> findNextPossibleSteps(List<Tile> path, Tile[][] tiles) {
		List<Tile> nextPossibleSteps = new ArrayList<>();

		Tile current = path.getLast(); // A

		if (path.size() == 1) {
			if (current.x + 1 < tiles[0].length) {
				nextPossibleSteps.add(tiles[current.y][current.x + 1]); // right
			}
			if (current.y + 1 < tiles.length) {
				nextPossibleSteps.add(tiles[current.y + 1][current.x]); // down
			}
			if (current.x - 1 >= 0) {
				nextPossibleSteps.add(tiles[current.y][current.x - 1]); // left
			}
			if (current.y - 1 >= 0) {
				nextPossibleSteps.add(tiles[current.y - 1][current.x]); // up
			}
			return nextPossibleSteps;
		}

		Tile previous = path.get(path.size() - 2); // B
		Tile thirdPrevious = null;
		if (path.size() > 3) {
			thirdPrevious = path.get(path.size() - 4); // C
		}

		int dX = current.x - previous.x;
		int dY = current.y - previous.y;

		if ((dX == -1 && dY == 0) || (dX == 1 && dY == 0)) { // right or left

			if (current.y - 1 >= 0) {
				nextPossibleSteps.add(tiles[current.y - 1][current.x]); // up
			}
			if (current.y + 1 < tiles[0].length) {
				nextPossibleSteps.add(tiles[current.y + 1][current.x]); // down
			}

			int theoreticalHorizontal = current.x + dX;
			if (thirdPrevious == null ||
				(current.y != thirdPrevious.y && (0 <= theoreticalHorizontal && theoreticalHorizontal < tiles[0].length))) {
				nextPossibleSteps.add(tiles[current.y][theoreticalHorizontal]);
			}

		} else if ((dX == 0 && dY == -1) || (dX == 0 && dY == 1)) { // down or up

			if (current.x - 1 >= 0) {
				nextPossibleSteps.add(tiles[current.y][current.x - 1]); // left
			}
			if (current.x + 1 < tiles.length) {
				nextPossibleSteps.add(tiles[current.y][current.x + 1]); // right
			}

			int theoreticalVertical = current.y + dY;
			if (thirdPrevious == null ||
				(current.x != thirdPrevious.x && (0 <= theoreticalVertical && theoreticalVertical < tiles.length))) {
				nextPossibleSteps.add(tiles[theoreticalVertical][current.x]);
			}

		} else {
			throw new RuntimeException("Invalid path");
		}

		return nextPossibleSteps;
	}

	private static class Tile {

		private List<Traveller> presentTravellers = new ArrayList<>();
		private boolean visited;
		private int heatLoss;
		private String label;
		private final int x;
		private final int y;

		public Tile(char heatLoss, int x, int y) {
			this.heatLoss = Integer.parseInt(String.valueOf(heatLoss));
			this.x = x;
			this.y = y;
			this.label = x + "," + y;
		}

		public int getHeatLoss() {
			return heatLoss;
		}

		public String getLabel() {
			return label;
		}

		public List<Traveller> getPresentTravellers() {
			return presentTravellers;
		}

		public void addTraveller(Traveller traveller) {
			presentTravellers.add(traveller);
		}

		public void removeTraveller(Traveller traveller) {
			presentTravellers.remove(traveller);
		}

		public void setVisited(boolean visited) {
			this.visited = visited;
		}

		public boolean getVisited() {
			return visited;
		}

		public void removeTravellers() {
			presentTravellers.clear();
		}

		@Override
		public String toString() {
			return heatLoss + " (" + x + ", " + y + ")";
		}
	}

	private class Traveller {

		private String lastSignificantSegment;
		private int travelledDistance;
		private List<Tile> travelledPath = new ArrayList<>();

		@Override
		public String toString() {
			return "T " + getGetCurrentTile().toString() + " - p: " + travelledPath.size();
		}

		private void visitTile(Tile tile) {
			travelledPath.addLast(tile);
			lastSignificantSegment = calculateLastSignificantSegment();
			travelledDistance = calculateTravelledDistance();

			//			tile.setVisited(true);
			//			tile.addTraveller(this);
		}

		public String getLastSignificantSegment() {
			return lastSignificantSegment;
		}

		public int getTravelledDistance() {
			return travelledDistance;
		}

		public boolean alreadyVisited(Tile tile) {
			return travelledPath.contains(tile);
		}

		public Tile getGetCurrentTile() {
			return travelledPath.getLast();
		}

		public Integer calculateTravelledDistance() {
			return travelledPath.subList(1, travelledPath.size()).stream()
								.mapToInt(Tile::getHeatLoss)
								.sum();
		}

		private void setTravelledPath(List<Tile> path) {
			this.travelledPath = path;
		}

		public Traveller clone() {
//			LinkedList<Tile> path = new LinkedList<>();
//			travelledPath.forEach(path::addLast);

			final Traveller traveller = new Traveller();
			traveller.setTravelledPath(new ArrayList<>(travelledPath));
			traveller.travelledDistance = travelledDistance;
			traveller.lastSignificantSegment = lastSignificantSegment;
			return traveller;
		}

		public String calculateLastSignificantSegment() {
			String result = "";
			for (int i = 0; i < 4 && i < travelledPath.size(); i++) {
				result += travelledPath.get(travelledPath.size() - 1 - i).getLabel() + "_";
			}
			return result;
		}

	}

}
