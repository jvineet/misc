package edu.cmu.cs.cs214.hw1.graph;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.cmu.cs.cs214.hw1.staff.Graph;
import edu.cmu.cs.cs214.hw1.staff.Vertex;

public class AlgorithmsTest {
	private static final int MAX_VERTICES = 20;
	private Graph sampleGraph;    		//tests implementation of AdjacencyMatrixGraph
	private Graph sampleGraph1;			//tests implementation of AdjacencyListGraph
	private Vertex anne, ben, claire, don, paul, mary, philip, tom, jessie,
			steven, emily, kate, robert, lisa;

	@Before
	public void setUp() throws Exception {
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

		sampleGraph = new AdjacencyMatrixGraph(MAX_VERTICES);
		sampleGraph1 = new AdjacencyListGraph(MAX_VERTICES);

		buildSampleGraph(sampleGraph);
		buildSampleGraph(sampleGraph1);
	}

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
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testShortestDistance() {
		System.out.println("Testing 'shortest distance'...");
		
		//samplegraph
		int dist = Algorithms.shortestDistance(sampleGraph, claire, anne);
		assertTrue(dist == 2);

		dist = Algorithms.shortestDistance(sampleGraph, claire, don);
		assertTrue(dist == -1);

		dist = Algorithms.shortestDistance(sampleGraph, paul, paul);
		assertTrue(dist == 0);

		dist = Algorithms.shortestDistance(sampleGraph, robert, lisa);
		assertTrue(dist == 4);
		
		////samplegraph1
		dist = Algorithms.shortestDistance(sampleGraph1, claire, anne);
		assertTrue(dist == 2);

		dist = Algorithms.shortestDistance(sampleGraph1, claire, don);
		assertTrue(dist == -1);

		dist = Algorithms.shortestDistance(sampleGraph1, paul, paul);
		assertTrue(dist == 0);

		dist = Algorithms.shortestDistance(sampleGraph1, robert, lisa);
		assertTrue(dist == 4);
	}

	@Test
	public void testCommonFriends() {
		System.out.println("Testing 'commonFriends'...");

		//samplegraph
		Vertex[] friends;
		friends = Algorithms.commonFriends(sampleGraph, tom, paul);
		assertTrue(friends.length == 0);

		friends = Algorithms.commonFriends(sampleGraph, philip, emily);
		assertTrue(contains(friends, jessie, mary));

		friends = Algorithms.commonFriends(sampleGraph, kate, paul);
		assertTrue(contains(friends, robert));

		friends = Algorithms.commonFriends(sampleGraph, mary, lisa);
		assertTrue(contains(friends, emily));
		
		//samplegraph1
		friends = Algorithms.commonFriends(sampleGraph1, tom, paul);
		assertTrue(friends.length == 0);

		friends = Algorithms.commonFriends(sampleGraph1, philip, emily);
		assertTrue(contains(friends, jessie, mary));

		friends = Algorithms.commonFriends(sampleGraph1, kate, paul);
		assertTrue(contains(friends, robert));

		friends = Algorithms.commonFriends(sampleGraph1, mary, lisa);
		assertTrue(contains(friends, emily));
	}

	@Test
	public void testSuggestFriend() {
		System.out.println("Testing 'suggestions'...");

		//samplegraph
		Vertex friend = Algorithms.suggestFriend(sampleGraph, mary);
		assertTrue(friend.equals(jessie));

		friend = Algorithms.suggestFriend(sampleGraph, anne);
		assertTrue(friend.equals(claire));

		friend = Algorithms.suggestFriend(sampleGraph, philip);
		assertTrue(friend.equals(emily));
		
		//samplegraph1
		friend = Algorithms.suggestFriend(sampleGraph1, mary);
		assertTrue(friend.equals(jessie));

		friend = Algorithms.suggestFriend(sampleGraph1, anne);
		assertTrue(friend.equals(claire));

		friend = Algorithms.suggestFriend(sampleGraph1, philip);
		assertTrue(friend.equals(emily));
	}

}
