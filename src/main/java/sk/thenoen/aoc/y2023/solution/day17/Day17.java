package sk.thenoen.aoc.y2023.solution.day17;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

		findPath(map[0].length, map.length, map, tiles, endingTile, travellers);

		return 0;
	}

	private static void findPath(int X, int Y, Tile[][] map, List<Tile> tiles, Tile endingTile, List<Traveller> travellers) {

		final List<Traveller> travellersAtTheEnd = new ArrayList<>();

		while (true) {
			// Visit all neighbour Tiles
			Set<Traveller> doneTravellers = new HashSet<>();
			List<Traveller> newTravellers = new ArrayList<>();

			for (Traveller t : travellers) {
				final List<Tile> possibleTiles = findNextPossibleSteps(t.travelledPath, map);
				for (Tile possibleTile : possibleTiles) {
					if (!t.alreadyVisited(possibleTile)) {
						final Traveller clonedTraveller = t.clone();
						clonedTraveller.visitTile(possibleTile);
						newTravellers.add(clonedTraveller);
						// remove _traveller_ from _travellers_
					}
				}
				doneTravellers.add(t);
			}
			travellers.removeAll(doneTravellers);
			doneTravellers.forEach(t -> t.getGetCurrentTile().removeTraveller(t));
			travellers.addAll(newTravellers);

			//			System.out.println("---------------------------------------");
			//			printField(X, Y, field);
			//			try {
			//				Thread.sleep(400);
			//			} catch (InterruptedException e) {
			//				throw new RuntimeException(e);
			//			}

			// Evaluate travelled paths and keep only the shortest
			for (Tile tile : tiles) {
				final List<Traveller> presentTravellers = tile.getPresentTravellers();
				if (presentTravellers.size() < 2) {
					continue;
				}
				final Traveller first = presentTravellers
						.stream()
						.min(Comparator.comparing(Traveller::getTravelledDistance))
						.orElseThrow();
				travellers.removeAll(presentTravellers);
				travellers.add(first);
				tile.removeTravellers();
				tile.addTraveller(first);
			}

			final List<Traveller> tmpAtTheEnd = travellers.stream()
															.filter(t -> t.getGetCurrentTile() == endingTile)
															.toList();

			travellers.removeAll(tmpAtTheEnd);
			travellersAtTheEnd.addAll(tmpAtTheEnd);
			tmpAtTheEnd.forEach(t -> t.getGetCurrentTile().removeTraveller(t));

			if (travellers.isEmpty()) {
				final Map<Traveller, Integer> distances = travellersAtTheEnd.stream()
																		  .collect(Collectors.toMap(t -> t, Traveller::getTravelledDistance));
				break;
			}
		}
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
		if (path.size() > 2) {
			thirdPrevious = path.get(path.size() - 3); // C
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

		//		private List<Tile> neighbours = new ArrayList<>();
		private List<Traveller> presentTravellers = new ArrayList<>();
		private boolean visited;
		private int heatLoss;
		private char label;
		private final int x;
		private final int y;

		public Tile(char heatLoss, int x, int y) {
			this.heatLoss = Integer.parseInt(String.valueOf(heatLoss));
			this.x = x;
			this.y = y;
		}

		public int getHeatLoss() {
			return heatLoss;
		}

		public char getLabel() {
			return label;
		}

		//		public void addNeighbour(Tile neighbour) {
		//			neighbours.add(neighbour);
		//		}

		//		public List<Tile> getNeighbours() {
		//			return neighbours;
		//		}

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

		@Override
		public String toString() {
			return "T " + getGetCurrentTile().toString() + " - p: " + travelledPath.size();
		}

		private LinkedList<Tile> travelledPath = new LinkedList<>();

		private void visitTile(Tile tile) {
			travelledPath.addLast(tile);
			tile.setVisited(true);
			tile.addTraveller(this);
		}

		public boolean alreadyVisited(Tile tile) {
			return travelledPath.contains(tile);
		}

		public Tile getGetCurrentTile() {
			return travelledPath.getLast();
		}

		public int getTravelledDistance() {
			return travelledPath.subList(1, travelledPath.size()).stream()
								.mapToInt(Tile::getHeatLoss)
								.sum();
		}

		private void setTravelledPath(LinkedList<Tile> path) {
			this.travelledPath = path;
		}

		public Traveller clone() {
			LinkedList<Tile> path = new LinkedList<>();
			travelledPath.forEach(path::addLast);

			final Traveller traveller = new Traveller();
			traveller.setTravelledPath(path);
			return traveller;
		}

	}

}
