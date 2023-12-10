package sk.thenoen.aoc.y2023.solution.day8;

import jdk.jshell.execution.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sk.thenoen.aoc.y2023.solution.Utils;

public class Day8 {

	public long solvePart1(String inputPath) {

		final ArrayList<String> lines = Utils.loadLines(inputPath);

		final String turnsString = lines.remove(0);
		lines.remove(0); // empty line

		List<String> nodeLines = new ArrayList<>();

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
