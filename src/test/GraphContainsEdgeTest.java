package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import main.jung.GraphQueriesAPI;
import main.jung.MyEdge;

public class GraphContainsEdgeTest {
	GraphQueriesAPI gQueris;
	DirectedGraph<Integer, MyEdge> graph;
	MyEdge edge;
	
	
	/**
	 * Sets up the test fixture. 
	 * (Called before every test case method.)
	 */
	@Before
	public void setUp() {
		gQueris = new GraphQueriesAPI();
		graph = new DirectedSparseMultigraph<Integer, MyEdge>();
		/*********graph1***********/
		TestingUtils.addEdge(graph, "1.1.2.3", 0, 1);
		TestingUtils.addEdge(graph, "2.3.4.5", 1, 2);
		TestingUtils.addEdge(graph, "", 2, 0);


	}
	
	/**
	 * Tears down the test fixture. 
	 * (Called after every test case method.)
	 */
	@After
	public void tearDown() {
		graph = null;
		gQueris = null;
	}
	
	@Test
	public void edgeExists() {
		edge = new MyEdge("1.1.2.3", 0, 1);
		assertTrue(GraphQueriesAPI.graphContainsEdge(graph, edge));
		edge = new MyEdge("", 2, 0);
		assertTrue(GraphQueriesAPI.graphContainsEdge(graph, edge));
	}
	
	@Test
	public void edgeNameEqualDifferentNodes() {
		edge = new MyEdge("1.1.2.3", 0, 2);
		assertFalse(GraphQueriesAPI.graphContainsEdge(graph, edge));
		edge = new MyEdge("1.1.2.3", 2, 1);
		assertFalse(GraphQueriesAPI.graphContainsEdge(graph, edge));
	}
	
	@Test
	public void edgeNodesCorrectDiffName() {
		edge = new MyEdge("1.1.2.5", 0, 1);
		assertFalse(GraphQueriesAPI.graphContainsEdge(graph, edge));
		edge = new MyEdge("1.1.2.*", 0, 1);
		assertFalse(GraphQueriesAPI.graphContainsEdge(graph, edge));
	}
}
