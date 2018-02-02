package edu.cmu.cs.cs214.hw1.graph;

import edu.cmu.cs.cs214.hw1.staff.*;

/**
 * This class models a node of the linked list data structure
 */
public class Node {
	private Vertex elem;
	private Node next;

	public Node(Vertex v, Node n) {
		this.elem = v;
		this.next = n;
	}
	
	/**
	 * This method stores/replaces a vertex in a node
	 */
	public void setElem(Vertex v) {
		this.elem = v;
	}
	
	/**
	 * This method stores/replaces a the pointer to the next node
	 */
	public void setNext(Node n) {
		this.next = n;
	}

	/**
	 * This method returns the vertex stored in a node
	 */
	public Vertex getElem() {
		return this.elem;
	}

	/**
	 * This method returns the pointer to the next node
	 */
	public Node getNext() {
		return this.next;
	}
}
