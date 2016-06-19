package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import main.jung.GraphQueriesAPI;
import main.jung.MyEdge;

public class GetOccurrencesOfPatternInGraphTest {
	
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
	public void patternSizeGreaterThanGraph() {
		/**********graph************/
		TestingUtils.addEdge(graph, "1.2.3.4", 0, 1);
		/*********pattern***********/
		TestingUtils.addEdge(pattern, "1.2.3.4", 10, 11);
		TestingUtils.addEdge(pattern, "1.2.3.3", 10, 12);
		
		assertTrue(gQueris.getOccurrencesOfPatternInGraph(pattern, graph, 1)==0);
	}
	
	@Test
	public void TS2G1P0() {;
		TestingUtils.addEdge(graph, "1.2.3.4", 10, 11);
		TestingUtils.addEdge(graph, "3.3.1.1", 11, 12);
		TestingUtils.addEdge(graph, "5.5.5.5", 12, 13);
		TestingUtils.addEdge(graph, "2.3.4.5", 13, 10);

		/*********pattern***********/
		TestingUtils.addEdge(pattern, "1.2.3.4", 0, 1);
		TestingUtils.addEdge(pattern, "3.3.*", 1, 2);
		TestingUtils.addEdge(pattern, "2.3.4.*", 3, 0);

		
		System.out.println(gQueris.getOccurrencesOfPatternInGraph(pattern, graph,1 ));
		assertTrue(gQueris.getOccurrencesOfPatternInGraph(pattern, graph,1)==1);
	}
	
	@Test
	public void TS2G2P1() {;
		TestingUtils.addEdge(graph, "1.2.3.4", 0, 1);
		TestingUtils.addEdge(graph, "3.3.3.3", 1, 2);
		TestingUtils.addEdge(graph, "1.2.3.4", 2, 3);
		TestingUtils.addEdge(graph, "3.3.1.1", 3, 0);

		/*********pattern***********/
		TestingUtils.addEdge(pattern, "1.2.3.4", 10, 11);
		TestingUtils.addEdge(pattern, "3.3.3.3", 11, 12);
		
		System.out.println(gQueris.getOccurrencesOfPatternInGraph(pattern, graph,1 ));
		assertTrue(gQueris.getOccurrencesOfPatternInGraph(pattern, graph,1 )==1);
	}
	
	@Test
	public void TS2G2P2() {;
		TestingUtils.addEdge(graph, "1.2.3.4", 0, 1);
		TestingUtils.addEdge(graph, "3.3.3.3", 1, 2);
		TestingUtils.addEdge(graph, "1.2.3.4", 2, 3);
		TestingUtils.addEdge(graph, "3.3.1.1", 3, 0);

		/*********pattern***********/
		TestingUtils.addEdge(pattern, "1.2.3.4", 10, 11);
		TestingUtils.addEdge(pattern, "3.3.*", 11, 12);
		
		System.out.println(gQueris.getOccurrencesOfPatternInGraph(pattern, graph,1 ));
		assertTrue(gQueris.getOccurrencesOfPatternInGraph(pattern, graph,1)==2);
	}
	
	@Test
	public void TS2G2P3() {;
		TestingUtils.addEdge(graph, "1.2.3.4", 0, 1);
		TestingUtils.addEdge(graph, "3.3.3.3", 1, 2);
		TestingUtils.addEdge(graph, "1.2.3.4", 2, 3);
		TestingUtils.addEdge(graph, "3.3.1.1", 3, 0);

		/*********pattern***********/
		TestingUtils.addEdge(pattern, "1.2.3.4", 10, 11);
		TestingUtils.addEdge(pattern, "3.3.1.1", 11, 12);
		
		System.out.println(gQueris.getOccurrencesOfPatternInGraph(pattern, graph,1));
		assertTrue(gQueris.getOccurrencesOfPatternInGraph(pattern, graph,1)==1);
	}
	
	@Test
	public void test1() {
		/**********graph************/
		TestingUtils.addEdge(graph, "1.2.3.4", 0, 1);
		TestingUtils.addEdge(graph, "3.3.3.3", 1, 2);
		TestingUtils.addEdge(graph, "4.3.5.3", 2, 3);
		TestingUtils.addEdge(graph, "2.3.1.3", 3, 1);
		TestingUtils.addEdge(graph, "1.2.3.4", 3, 4);
		/*********pattern***********/
		TestingUtils.addEdge(pattern, "1.2.3.4", 10, 11);
		
		
		assertTrue(gQueris.getOccurrencesOfPatternInGraph(pattern, graph,1)==2);
	}
	
	@Test
	public void test2() {
		/**********graph************/
		TestingUtils.addEdge(graph, "5.5.5.5", 0, 2);
		TestingUtils.addEdge(graph, "5.5.5.5", 1, 2);
		TestingUtils.addEdge(graph, "1.2.3.4", 2, 3);
		TestingUtils.addEdge(graph, "3.3.3.3", 3, 4);
		TestingUtils.addEdge(graph, "3.3.3.3", 3, 5);
		/*********pattern***********/
		TestingUtils.addEdge(pattern, "5.5.5.5", 0, 1);
		TestingUtils.addEdge(pattern, "1.2.3.4", 1, 2);
		TestingUtils.addEdge(pattern, "3.3.3.3", 2, 3);
		System.out.println(gQueris.getOccurrencesOfPatternInGraph(pattern, graph,1));
		assertTrue(gQueris.getOccurrencesOfPatternInGraph(pattern, graph,1)==4);
	}
}
