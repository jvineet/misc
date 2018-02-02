package edu.cmu.cs.cs214.hw1.graph;

import edu.cmu.cs.cs214.hw1.staff.Graph;
import edu.cmu.cs.cs214.hw1.staff.Vertex;

// TODO: Implement an AdjacencyMatrixGraph class
public class AdjacencyMatrixGraph implements Graph {
	private Vertex[] vertices;
	private int[][] edges;
	private int len;
	private int max;
	
	public AdjacencyMatrixGraph (int maxVertices) {
		this.vertices = new Vertex[maxVertices];
		this.edges = new int[maxVertices][maxVertices];
		this.max = maxVertices; 
	}
	
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
		len++;		
	}

	@Override
	public void addEdge(Vertex v1, Vertex v2) {
		int ind1 = getIndex(v1);
		int ind2 = getIndex(v2);
		
		edges[ind1][ind2] = 1;
		edges[ind2][ind1] = 1;	
	}

	@Override
	public boolean isAdjacent(Vertex v1, Vertex v2) {
		int ind1 = getIndex(v1);
		int ind2 = getIndex(v2);
		return (edges[ind1][ind2] == 1);
	}

	@Override
	public Vertex[] getNeighbors(Vertex v) {
		int ind = getIndex(v);
		int count = 0;
		for (int i = 0; i < len; i++) {
			count = count + edges[ind][i];
		}
		
		int incr = 0;
		Vertex[] neighbor = new Vertex[count];
		for (int i = 0; i < len; i++) {
			if (edges[ind][i] == 1) {
				neighbor[incr] = vertices[i];
				incr++;
			}
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