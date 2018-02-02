package edu.cmu.cs.cs214.hw1.graph;

import edu.cmu.cs.cs214.hw1.staff.*;

public class Algorithms {

	/**
	 * *********************** Algorithms **************************** Please
	 * see the handout for a more detailed specification of the behavior of each
	 * method.
	 */

	/**
	 * Returns the index of vertex v in the vertex array G of length len or -1
	 * if v is not present in G
	 */
	private static int getIndex(Vertex[] G, Vertex v, int len) {
		for (int i = 0; i < len; i++) {
			if (v.equals(G[i])) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * This method finds the shortest distance between two vertices. It returns
	 * -1 if the two nodes are not connected.
	 * 
	 * @param graph
	 *            the friends graph
	 * @param a
	 *            the first Vertex
	 * @param b
	 *            the second Vertex
	 */
	public static int shortestDistance(Graph graph, Vertex a, Vertex b) {
		Vertex[] all = graph.getVertices();

		int count = 0;
		for (Vertex j : all) {
			count++;
		}

		int[] set = new int[count];  				/* keeps track of nodes already visited in BFS */
		Vertex root = a;
		Vertex t;
		Queue q = new Queue(count); 				/*Data structure used for implementing a BFS*/
		q.insert(root);

		// implementing a BFS
		while (!q.isEmpty()) {
			t = q.remove();
			if (t.equals(b)) {
				return set[getIndex(all, t, count)]; /*path between two people established*/
			}

			Vertex[] neigh = graph.getNeighbors(t);
			for (Vertex j : neigh) {
				int ind = getIndex(all, j, count);
				if (set[ind] == 0) {
					set[ind] = set[getIndex(all, t, count)] + 1;
					q.insert(j);
				}
			}
		}
		return -1; 									/* if the two people are not connected in the graph */
	}

	/**
	 * This method is used to find common friends between v1 and v2. The
	 * resulting array should not contain any duplicates, and should have length
	 * equal to the number of vertices. It should not contain any nulls. The
	 * vertices in the array can be ordered arbitrarily.
	 * 
	 * If there are no common friends, then return an array of size 0.
	 * 
	 * @param graph
	 *            the friends graph
	 * @param a
	 *            the first Vertex
	 * @param b
	 *            the second Vertex
	 */
	public static Vertex[] commonFriends(Graph graph, Vertex a, Vertex b) {
		Vertex[] neighA = graph.getNeighbors(a);
		Vertex[] neighB = graph.getNeighbors(b);

		int count = 0;
		for (Vertex i : neighA) {
			for (Vertex j : neighB) {
				if (i.equals(j)) {
					count++;
				}
			}
		}

		Vertex[] common = new Vertex[count];
		count = 0;

		for (Vertex i : neighA) {
			for (Vertex j : neighB) {
				if (i.equals(j)) {
					common[count] = i;
					count++;
				}
			}
		}
		return common;
	}

	/**
	 * This method is used to find the person who has the most common friends
	 * with a particular user. If there is a tie, you can return any of the
	 * people who are tied.
	 * 
	 * @param graph
	 *            the friends graph
	 * @param source
	 *            the Vertex(Person) in question
	 */
	public static Vertex suggestFriend(Graph graph, Vertex source) {
		Vertex[] all = graph.getVertices();
		Vertex person = all[0]; 
		int max = 0;
		for (Vertex i : all) {
			Vertex[] common = commonFriends(graph, source, i);

			int count = 0;
			for (Vertex j : common) {
				count++;
			}

			if (!graph.isAdjacent(source, i) && count >= max
					&& !i.equals(source)) {
				person = i;
				max = count;
			}
		}
		return person;
	}

}
