package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import main.jung.GraphQueriesAPI;
import main.jung.MyEdge;

public class VisualizePatternInGraphTest {
	GraphQueriesAPI gQueris;
	DirectedGraph<Integer, MyEdge> pattern;
	DirectedGraph<Integer, MyEdge> graph;
	
	
	/**
	 * Sets up the test fixture. 
	 * (Called before every test case method.)
	 */
	@Before
	public void setUp() {
		gQueris = new GraphQueriesAPI();
		pattern = new DirectedSparseMultigraph<Integer, MyEdge>();
		graph = new DirectedSparseMultigraph<Integer, MyEdge>();
		


	}
	
	/**
	 * Tears down the test fixture. 
	 * (Called after every test case method.)
	 */
	@After
	public void tearDown() {
		pattern = null;
		graph = null;
		gQueris = null;
	}
	
	@Test
	public void example1() {
		/*********pattern*********/
		TestingUtils.addEdge(pattern, "1.1.2.3", 5, 6);
		/*********graph***********/
		TestingUtils.addEdge(graph, "1.1.2.3", 0, 1);
		TestingUtils.addEdge(graph, "2.3.4.5", 1, 2);
		TestingUtils.addEdge(graph, "3.3.3.-", 2, 3);
		
		gQueris.visualizePatternInGraph(pattern, graph);
		assertTrue(true);
		while(true);
	}
}
