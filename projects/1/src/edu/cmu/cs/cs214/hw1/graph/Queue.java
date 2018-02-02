package edu.cmu.cs.cs214.hw1.graph;

import edu.cmu.cs.cs214.hw1.staff.*;

/**
 * <code>Queue</code> is a class representing a simple queue.
 */
public class Queue {
	private Vertex[] store;
	private int front;   				/*Keeps track of front of the queue*/
	private int back;					/*Keeps track of back of the queue*/
	
	/**
     * Class constructor for <code>Queue</code>.
     */
	public Queue(int size) {
		this.store = new Vertex[size];
	}

	/**
     * Enqueues an element at the end of <code>Queue</code>.
     * 
     * @param elem
     *            Element to be enqueued  
     */
	public void insert(Vertex elem) {
		store[back] = elem;
		back = back + 1;
	}

	/**
     * Dequeues an element from the beginning of <code>Queue</code>.
     * 
     */
	public Vertex remove() {
		Vertex elem = store[front];
		front = front + 1;
		return elem;
	}
 
	/**
     * Checks if the <code>Queue</code> is empty
     */
	public boolean isEmpty() {
		return front == back;
	}
}
