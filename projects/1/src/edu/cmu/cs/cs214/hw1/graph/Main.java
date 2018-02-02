package edu.cmu.cs.cs214.hw1.graph;

import edu.cmu.cs.cs214.hw1.staff.Graph;
import edu.cmu.cs.cs214.hw1.staff.Vertex;
import edu.cmu.cs.cs214.hw1.graph.*;

/**
 * This class should contain some test to help ensure the correctness of your
 * implementations.
 * 
 * Run this class to perform tests. A couple of sample tests have been provided.
 * You may are encouraged to add additional tests to ensure the correctness of
 * your graph/algorithm implementations.
 */
public class Main {

	private static final int MAX_VERTICES = 20;
	private Graph sampleGraph;
	private Vertex anne, ben, claire, don, paul, mary, philip, tom, jessie,
			steven, emily, kate, robert, lisa;

	public static void main(String[] args) {
		new Main().runTests();
	}

	public Main() {
		paul = new Vertex("Paul");
		mary = new Vertex("Mary");
		philip = new Vertex("Philip");
		tom = new Vertex("Tom");
		jessie = new Vertex("Jessie");
		steven = new Vertex("Steven");
		emily = new Vertex("Emily");
		kate = new Vertex("Kate");
		robert = new Vertex("Robert");
		lisa = new Vertex("Lisa");
		anne = new Vertex("Anne");
		ben = new Vertex("Ben");
		claire = new Vertex("Claire");
		don = new Vertex("Don");

		// TODO: uncomment one of these two lines
		//sampleGraph = new AdjacencyMatrixGraph(MAX_VERTICES);
		sampleGraph = new AdjacencyListGraph(MAX_VERTICES);

		buildSampleGraph(sampleGraph);
	}

	/**
	 * Builds the sample graph illustrated in hw1.pdf Appendix A.
	 * 
	 * @param source
	 *            an initially empty {@link Graph}. This method requires that
	 *            source must has MAX_VERTICES set to at least 14.
	 */
	private void buildSampleGraph(Graph source) {
		source.addVertex(paul);
		source.addVertex(mary);
		source.addVertex(philip);
		source.addVertex(tom);
		source.addVertex(jessie);
		source.addVertex(steven);
		source.addVertex(robert);
		source.addVertex(emily);
		source.addVertex(kate);
		source.addVertex(lisa);
		source.addEdge(paul, mary);
		source.addEdge(mary, philip);
		source.addEdge(philip, tom);
		source.addEdge(philip, jessie);
		source.addEdge(tom, jessie);
		source.addEdge(jessie, steven);
		source.addEdge(jessie, emily);
		source.addEdge(emily, mary);
		source.addEdge(emily, lisa);
		source.addEdge(paul, robert);
		source.addEdge(paul, kate);
		source.addEdge(robert, kate);
		source.addVertex(anne);
		source.addVertex(ben);
		source.addVertex(claire);
		source.addVertex(don);
		source.addEdge(anne, ben);
		source.addEdge(ben, claire);
	}

	/**
	 * Performs tests on the {@code sampleGraph} (see hw0.pdf) and prints the
	 * results to the screen.
	 * 
	 * TODO: We won't grade how thoroughly you test your code, but we strongly
	 * recommend that you write some tests of your own to ensure the correctness
	 * of your graph and algorithm implementations!
	 */
	private void runTests() {
		testGetNeighbors();
		testCommonFriends();
		testSuggestFriend();
		testShortestDistance();
		// TODO: add more tests for the rest of your implementations here!
	}

	/**
	 * Perform tests for {@link Graph#getNeighbors(Vertex)}.
	 */
	private void testGetNeighbors() {
		System.out.println("Testing 'getNeighbors'...");

		Vertex[] neighbors;
		neighbors = sampleGraph.getNeighbors(jessie);
		printResult(neighbors.length == 4);
		printResult(contains(neighbors, philip, tom, steven, emily));

		neighbors = sampleGraph.getNeighbors(don);
		printResult(neighbors.length == 0);
		printResult(contains(neighbors));

		neighbors = sampleGraph.getNeighbors(mary);
		printResult(neighbors.length == 3);
		printResult(contains(neighbors, philip, emily, paul));
	}

	/**
	 * Perform tests for {@link Algorithms#commonFriends(Graph, Vertex, Vertex)}
	 * .
	 */
	private void testCommonFriends() {
		System.out.println("Testing 'commonFriends'...");

		Vertex[] friends;
		friends = Algorithms.commonFriends(sampleGraph, tom, paul);
		printResult(friends.length == 0);

		friends = Algorithms.commonFriends(sampleGraph, philip, emily);
		printResult(contains(friends, jessie, mary));

		friends = Algorithms.commonFriends(sampleGraph, kate, paul);
		printResult(contains(friends, robert));

		friends = Algorithms.commonFriends(sampleGraph, mary, lisa);
		printResult(contains(friends, emily));
	}

	/**
	 * Helper testing method which returns false iff one or more specified
	 * 'checkVertices' do not exist in the Vertices array 'vertices'.
	 * 
	 * @param vertices
	 *            an array of vertices
	 * @param checkVertices
	 *            the vertices to check (google search "java varargs" if you
	 *            don't know what the "..." means)
	 */
	private static boolean contains(Vertex[] vertices, Vertex... checkVertices) {
		for (Vertex check : checkVertices) {
			boolean found = false;
			for (Vertex v : vertices) {
				if (v.equals(check)) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Perform tests for suggestFriend method in class Algorithms.
	 */
	private void testSuggestFriend() {
		System.out.println("Testing 'suggestions'...");

		Vertex friend = Algorithms.suggestFriend(sampleGraph, mary);
		printResult(friend.equals(jessie));

		friend = Algorithms.suggestFriend(sampleGraph, anne);
		printResult(friend.equals(claire));

		friend = Algorithms.suggestFriend(sampleGraph, philip);
		printResult(friend.equals(emily));
	}

	/**
	 * Perform tests for shortestDistance method in class Algorithms.
	 */
	private void testShortestDistance() {
		System.out.println("Testing 'shortest distance'...");

		int dist = Algorithms.shortestDistance(sampleGraph, claire, anne);
		printResult(dist == 2);

		dist = Algorithms.shortestDistance(sampleGraph, claire, don);
		printResult(dist == -1);

		dist = Algorithms.shortestDistance(sampleGraph, paul, paul);
		printResult(dist == 0);

		dist = Algorithms.shortestDistance(sampleGraph, robert, lisa);
		printResult(dist == 4);
	}

	/**
	 * Helper testing method which prints the test's result to the screen.
	 */
	private static void printResult(boolean result) {
		if (result) {
			System.out.println("PASS");
		} else {
			System.out.println("FAIL");
		}
	}
}
