package sk.thenoen.aoc.y2023.solution.day8;

import jdk.jshell.execution.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sk.thenoen.aoc.y2023.solution.Utils;

public class Day8 {

	public long solvePart1(String inputPath) {

		final ArrayList<String> lines = Utils.loadLines(inputPath);

		final String turnsString = lines.remove(0);
		lines.remove(0); // empty line

		//		List<String> nodeLines = new ArrayList<>();

		final Map<String, Node> graph = parseGraph(lines);

		long count = 0;

		final Node startNode = graph.get("AAA");
		final Node endNode = graph.get("ZZZ");
		Node currentNode = startNode;

		final char[] turnsArray = turnsString.toCharArray();

		int index = 0;
		while (currentNode != endNode) {
			if (turnsArray[index] == 'L') {
				currentNode = currentNode.leftChild;
			} else {
				currentNode = currentNode.rightChild;
			}
			index = (index + 1) % turnsArray.length;
			count++;
		}

		return count;
	}

	public long solvePart2(String inputPath) {
		final ArrayList<String> lines = Utils.loadLines(inputPath);

		final String turnsString = lines.remove(0);
		lines.remove(0); // empty line

		final Map<String, Node> graph = parseGraph(lines);

		List<Node> startingNodes = graph.keySet().stream()
										.filter(k -> k.endsWith("A"))
										.map(graph::get)
										.toList();
		List<Node> originalStartingNodes = new ArrayList<>(startingNodes);
//		originalStartingNodes.addAll(startingNodes);

		Map<Node, Long> nodeToFirstEnd = new HashMap<>();
		Map<Node, List<Long>> nodeToEveryEnd = new HashMap<>();

		final char[] turnsArray = turnsString.toCharArray();
		long stepCount = 1;
		int index = 0;
		do {
			List<Node> nextNodes = new ArrayList<>();

			for (int i = 0; i < startingNodes.size(); i++) {
				Node currentNode = startingNodes.get(i);
				if (turnsArray[index] == 'L') {
					nextNodes.add(currentNode.leftChild);
				} else {
					nextNodes.add(currentNode.rightChild);
				}
				if (nextNodes.getLast().name.endsWith("Z")) {
					nodeToFirstEnd.put(originalStartingNodes.get(i), stepCount);
					nodeToEveryEnd.putIfAbsent(originalStartingNodes.get(i), new ArrayList<>());
					nodeToEveryEnd.get(originalStartingNodes.get(i)).add(stepCount);
				}
			}

			index = (index + 1) % turnsArray.length;
			startingNodes = nextNodes;
			stepCount++;

		} while (nodeToFirstEnd.size() != startingNodes.size());
//		} while (nodeToEveryEnd.values().stream().allMatch(v -> v.size() > 9));

		long lcm = 0;
		boolean anyMatch = true;

		// LCM calculation for those who have time and/or are lazy
		do {
			lcm++;
			long x = lcm;
			anyMatch = nodeToFirstEnd.values().stream().anyMatch(v -> x % v != 0);
		} while (anyMatch);

		return lcm; //correct: 20_220_305_520_997L; //calculated in LibreOffice Calc
	}

	private static Map<String, Node> parseGraph(ArrayList<String> lines) {
		Map<String, Node> graph = new HashMap<>();
		final Pattern pattern = Pattern.compile("(\\w\\w\\w)\s=\s\\((\\w\\w\\w),\\s(\\w\\w\\w)\\)");
		for (String line : lines) {

			final Matcher matcher = pattern.matcher(line);
			final boolean found = matcher.find();
			Node newNode = graph.putIfAbsent(matcher.group(1), new Node(matcher.group(1)));
			if (newNode == null) {
				newNode = graph.get(matcher.group(1));
			}

			Node leftNode = graph.putIfAbsent(matcher.group(2), new Node(matcher.group(2)));
			Node rightNode = graph.putIfAbsent(matcher.group(3), new Node(matcher.group(3)));
			if (leftNode == null) {
				leftNode = graph.get(matcher.group(2));
			}
			if (rightNode == null) {
				rightNode = graph.get(matcher.group(3));
			}
			newNode.leftChild = leftNode;
			newNode.rightChild = rightNode;

		}
		return graph;
	}

	private static class Node {

		private final String name;
		private Node leftChild;
		private Node rightChild;

		public Node(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "Node{" +
				   "name='" + name + '\'' +
				   '}';
		}
	}

}
