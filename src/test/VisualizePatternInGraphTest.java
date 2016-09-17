package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import main.jung.GraphQueriesAPI;
import main.jung.MyEdge;

public class VisualizePatternInGraphTest {
	GraphQueriesAPI gQueris;
	DirectedGraph<Integer, MyEdge> graph;
	
	
	/**
	 * Sets up the test fixture. 
	 * (Called before every test case method.)
	 */
	@Before
	public void setUp() {
		gQueris = new GraphQueriesAPI();
		graph = new DirectedSparseMultigraph<Integer, MyEdge>();
		


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
	public void example1() {
		/*********graph***********/
		TestingUtils.addEdge(graph, "1.1.2.3", 0, 1);
		TestingUtils.addEdge(graph, "2.3.4.5", 1, 2);
		TestingUtils.addEdge(graph, "3.3.3.-", 2, 3);
		
		List<Integer> vertices = new ArrayList<Integer>();
		List<MyEdge> edges = new ArrayList<MyEdge>();
		vertices.add(0); vertices.add(1);
		edges.add(new MyEdge("1.1.2.3", 0, 1));
		GraphQueriesAPI.visualizePatternInGraph(graph,vertices, edges, "example1");
		assertTrue(true);
		while(true);
	}
}
