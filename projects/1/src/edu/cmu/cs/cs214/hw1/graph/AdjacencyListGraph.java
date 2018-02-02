package edu.cmu.cs.cs214.hw1.graph;

import edu.cmu.cs.cs214.hw1.staff.*;

// TODO: Implement an AdjacencyListGraph class
public class AdjacencyListGraph implements Graph {
	private Vertex[] vertices;
	private LinkedList[] edges;
	private int len;
	private int max;

	public AdjacencyListGraph(int maxVertices) {
		this.vertices = new Vertex[maxVertices];
		this.edges = new LinkedList[maxVertices];
		this.max = maxVertices;
	}

	/**
	 * Returns the index of vertex v in the 'vertices' or -1 if v is not present
	 * in 'vertices'
	 */
	private int getIndex(Vertex v) {
		for (int i = 0; i < len; i++) {
			if (v.equals(vertices[i])) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void addVertex(Vertex v) {
		vertices[len] = v;
		edges[len] = new LinkedList();
		len++;
	}

	@Override
	public void addEdge(Vertex v1, Vertex v2) {
		int ind1 = getIndex(v1);
		int ind2 = getIndex(v2);

		edges[ind1].addNode(v2);
		edges[ind2].addNode(v1);
	}

	@Override
	public boolean isAdjacent(Vertex v1, Vertex v2) {
		int ind1 = getIndex(v1);
		return edges[ind1].lookUp(v2);
	}

	@Override
	public Vertex[] getNeighbors(Vertex v) {
		int ind = getIndex(v);
		Node list1 = edges[ind].getFirst();
		int count = 0;

		while (list1.getNext() != null) {
			count++;
			list1 = list1.getNext();
		}

		Node list = edges[ind].getFirst();
		Vertex[] neighbor = new Vertex[count];
		count = 0;

		while (list.getNext() != null) {
			neighbor[count] = list.getElem();
			count++;
			list = list.getNext();
		}
		return neighbor;
	}

	@Override
	public Vertex[] getVertices() {
		Vertex[] total = new Vertex[len];
		for (int i = 0; i < len; i++) {
			total[i] = vertices[i];
		}
		return total;
	}

}