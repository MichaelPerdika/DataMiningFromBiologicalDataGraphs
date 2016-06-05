package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.jung.GraphQueriesAPI;
import main.jung.MyEdge;

public class ParsedEdgeScoreTest {
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
	public void oneNameEqual50() {
		edge1 = new MyEdge("2.3.3.1", 0, 1);
		edge2 = new MyEdge("2.3.5.5", 0, 1);
		List<List<String>> e1 = gQueris.parseEdgeNames(edge1);
		List<List<String>> e2 = gQueris.parseEdgeNames(edge2);
		assertTrue(gQueris.parsedEdgeScore(e1.get(0), e2.get(0)) >= 0.5);
	}
}
