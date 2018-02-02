package edu.cmu.cs.cs214.hw3;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AvlTreeSetTest {
	private AvlTreeSet mTestTree;

	/** Called before each test case method. */
	@Before
	public void setUp() throws Exception {
		// Start each test case method with a brand new AvlTreeSet object.
		mTestTree = new AvlTreeSet();
	}

	/** Called after each test case method. */
	@After
	public void tearDown() throws Exception {
		// Don't need to do anything here.
	}

	/** Tests that an empty tree has size 0. */
	@Test
	public void testEmptyTreeSize() {
		// First argument is the expected value.
		// Second argument is the actual returned value.
		assertEquals(0, mTestTree.size());
		assertEquals(true, mTestTree.isEmpty());
	}

	@Test
	public void testInsert() {
		assertFalse(mTestTree.contains(1));
		mTestTree.insert(2);
		
		assertEquals(1, mTestTree.size());		//checks for size method
		assertTrue(mTestTree.contains(2));		//checks if correct thing is inserted

		mTestTree.insert(1);
		assertEquals(2, mTestTree.size());
		assertFalse(mTestTree.isEmpty());
		assertTrue(mTestTree.contains(2));
		assertTrue(mTestTree.contains(1));

		mTestTree.insert(3);
		assertEquals(3, mTestTree.size());
		assertTrue(mTestTree.contains(3));
	}

	// TODO: Add more test case methods below!

	@Test(expected = IllegalStateException.class)
	public void testRemoveEmpty() {
		mTestTree.remove(3);
	}

	@Test
	public void testRemoveFull() {
		mTestTree.insert(2);
		mTestTree.insert(1);
		mTestTree.insert(3);
		mTestTree.insert(6);
		mTestTree.insert(5);

		mTestTree.remove(2);
		assertEquals(4, mTestTree.size());
		assertFalse(mTestTree.contains(2));

		mTestTree.remove(1);
		assertEquals(3, mTestTree.size());
		assertFalse(mTestTree.contains(1));

		mTestTree.remove(3);
		mTestTree.remove(6);

		mTestTree.insert(1);
		mTestTree.remove(5);
	}

	@Test(expected = IllegalStateException.class)
	public void testGetMaxEmpty() {
		mTestTree.getMax();
	}

	@Test
	public void testGetMaxFull() {
		mTestTree.insert(2);
		mTestTree.insert(1);
		mTestTree.insert(3);
		mTestTree.insert(6);
		mTestTree.insert(5);

		assertEquals(6, mTestTree.getMax());
	}

	@Test(expected = IllegalStateException.class)
	public void testGetMinEmpty() {
		mTestTree.getMin();
	}

	@Test
	public void testGetMinFull() {
		mTestTree.insert(5);
		mTestTree.insert(6);
		mTestTree.insert(3);
		mTestTree.insert(1);
		mTestTree.insert(2);
		mTestTree.insert(7);
		mTestTree.insert(9);

		assertEquals(1, mTestTree.getMin());

	}

	@Test
	public void testGetHeight() {
		assertEquals(-1, mTestTree.getHeight());
		mTestTree.insert(5);
		assertEquals(0, mTestTree.getHeight());
		mTestTree.insert(4);
		assertEquals(1, mTestTree.getHeight());
		mTestTree.insert(3);
		assertEquals(1, mTestTree.getHeight());
		mTestTree.insert(2);
		assertEquals(2, mTestTree.getHeight());
	}
}
