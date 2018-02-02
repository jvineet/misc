package edu.cmu.cs.cs214.hw1.graph;

import edu.cmu.cs.cs214.hw1.staff.*;

/**
 * This class models the linked list data structure
 */
public class LinkedList {
	private Node begin;

	public LinkedList() {
		begin = new Node(null, null);
	}

	/**
	 * This method adds a new node in the front of the linked list
	 */
	public void addNode(Vertex v) {
		Node temp = new Node(v, begin);
		begin = temp; 
	}

	/**
	 * This method returns the first node in the linked list
	 */
	public Node getFirst() {
		return begin;
	}

	/**
	 * This method checks if a vertex v is stored in any node of the linked list
	 */
	public boolean lookUp(Vertex v) {
		Node list = begin;
		while (list != null) {
			Vertex v2 = list.getElem();
			if (v.equals(v2)) {
				return true;
			}
			list = list.getNext();
		}
		return false;
	}
}
