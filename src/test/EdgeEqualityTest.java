package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.jung.GraphQueriesAPI;
import main.jung.MyEdge;

public class EdgeEqualityTest {
	GraphQueriesAPI gQueris;
	MyEdge edge1;
	MyEdge edge2;
	
	
	/**
	 * Sets up the test fixture. 
	 * (Called before every test case method.)
	 */
	@Before
	public void setUp() {
		gQueris = new GraphQueriesAPI();
	}
	
	/**
	 * Tears down the test fixture. 
	 * (Called after every test case method.)
	 */
	@After
	public void tearDown() {
		edge1 = null;
		edge2 = null;
		gQueris = null;
	}
	
	@Test
	public void oneNameEqual100() {
		edge1 = new MyEdge("2.3.3.1", 0, 1);
		edge2 = new MyEdge("2.3.3.1", 0, 1);
		assertTrue(gQueris.edgeEquality(edge1, edge2, 1));
		assertTrue(gQueris.edgeEquality(edge1, edge2, 0.5));
		assertTrue(gQueris.edgeEquality(edge1, edge2, 0.25));
		assertTrue(gQueris.edgeEquality(edge1, edge2, 0));
	}
	
	@Test
	public void oneNameEqual50() {
		edge1 = new MyEdge("2.3.3.1", 0, 1);
		edge2 = new MyEdge("2.3.5.5", 0, 1);
		assertFalse(gQueris.edgeEquality(edge1, edge2, 1));
		assertTrue(gQueris.edgeEquality(edge1, edge2, 0.5));
		assertTrue(gQueris.edgeEquality(edge1, edge2, 0.25));
		assertTrue(gQueris.edgeEquality(edge1, edge2, 0));
	}
	
	@Test
	public void twoNameEqual100() {
		edge1 = new MyEdge(new String[] {"2.3.3.1", "4.1.1.1"}, 0, 1);
		edge2 = new MyEdge(new String[] {"2.3.3.1", "4.1.1.1"}, 0, 1);
		assertTrue(gQueris.edgeEquality(edge1, edge2, 1));
		assertTrue(gQueris.edgeEquality(edge1, edge2, 0.5));
		assertTrue(gQueris.edgeEquality(edge1, edge2, 0));
	}
	
	@Test
	public void twoNameEqual50case1() {
		edge1 = new MyEdge(new String[] {"2.3.3.1", "4.1.1.1"}, 0, 1);
		edge2 = new MyEdge(new String[] {"2.3.3.1"}, 0, 1);
		assertFalse(gQueris.edgeEquality(edge1, edge2, 0.6));
		assertTrue(gQueris.edgeEquality(edge1, edge2, 0.5));
		assertTrue(gQueris.edgeEquality(edge1, edge2, 0));
	}
	
	@Test
	public void twoNameEqual50case2() {
		edge1 = new MyEdge(new String[] {"2.3.5.5", "2.3.4.4"}, 0, 1);
		edge2 = new MyEdge(new String[] {"2.3.3.1"}, 0, 1);
		assertFalse(gQueris.edgeEquality(edge1, edge2, 0.6));
		assertTrue(gQueris.edgeEquality(edge1, edge2, 0.5));
		assertTrue(gQueris.edgeEquality(edge1, edge2, 0));
	}
	
	@Test
	public void twoNameEqual50case3() {
		edge1 = new MyEdge(new String[] {"2.3.5.5", "4.4.4.4"}, 0, 1);
		edge2 = new MyEdge(new String[] {"2.3.3.1", "4.4.1.1"}, 0, 1);
		assertFalse(gQueris.edgeEquality(edge1, edge2, 0.6));
		assertTrue(gQueris.edgeEquality(edge1, edge2, 0.5));
		assertTrue(gQueris.edgeEquality(edge1, edge2, 0));
	}
}
