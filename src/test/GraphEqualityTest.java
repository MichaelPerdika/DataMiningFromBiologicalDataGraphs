package test;

import static org.junit.Assert.*;

import org.junit.*;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import main.jung.GraphQueriesAPI;
import main.jung.MyEdge;

public class GraphEqualityTest {

	GraphQueriesAPI gQueris;
	DirectedGraph<Integer, MyEdge> graph1;
	DirectedGraph<Integer, MyEdge> graph2;
	
	
	/**
	 * Sets up the test fixture. 
	 * (Called before every test case method.)
	 */
	@Before
	public void setUp() {
		gQueris = new GraphQueriesAPI();
		graph1 = new DirectedSparseMultigraph<Integer, MyEdge>();
		graph2 = new DirectedSparseMultigraph<Integer, MyEdge>();
		/*********graph1***********/
		TestingUtils.addEdge(graph1, "A", 0, 1);
		TestingUtils.addEdge(graph1, "B", 1, 2);
		/*********graph2***********/
		TestingUtils.addEdge(graph2, "A", 5, 6);
		TestingUtils.addEdge(graph2, "B", 6, 7);
	}
	
	/**
	 * Tears down the test fixture. 
	 * (Called after every test case method.)
	 */
	@After
	public void tearDown() {
		graph1 = null;
		graph2 = null;
		gQueris = null;
	}
	
	@Test
	public void sameGraphssameDirection() {
		/** graph1Edges = [A ][0,1] [B ][1,2] 
		 *  graph2Edges = [A ][5,6] [B ][6,7] **/
		assertTrue(gQueris.graphEquality(graph1, graph2));
	}
	
	@Test
	public void sameGraphsDiffDirection() {
		/** graph1Edges = [A ][0,1] [B ][1,2] 
		 *  graph2Edges = [A ][6,7] [B ][5,6] **/
		graph1 = new DirectedSparseMultigraph<Integer, MyEdge>();
		graph2 = new DirectedSparseMultigraph<Integer, MyEdge>();
		/*********graph1***********/
		TestingUtils.addEdge(graph1, "1.2.3.4", 0, 1);
		TestingUtils.addEdge(graph1, "3.3.3.3", 1, 2);
		/*********graph2***********/
		TestingUtils.addEdge(graph2, "1.2.3.4", 2, 3);
		TestingUtils.addEdge(graph2, "3.3.3.3", 1, 2);
		assertFalse(gQueris.graphEquality(graph1, graph2));
	}
	
	@Test
	public void sameGraphsAtLeastOneDifferentDirection() {
		TestingUtils.addEdge(graph1, "c", 2, 3);
		TestingUtils.addEdge(graph2, "c", 7, 8); // this is A-->d
		TestingUtils.addEdge(graph1, "d", 0, 3);
		TestingUtils.addEdge(graph2, "d", 8, 5); // this is d-->A
		/** graph1Edges = [A ][0,1] [B ][1,2] [c ][2,3] [d ][0,3]  
		 *  graph2Edges = [A ][5,6] [B ][6,7] [c ][7,8] [d ][8,5]**/
		assertFalse(gQueris.graphEquality(graph1, graph2));
	}
	
	@Test
	public void allNullOptions() {
		assertFalse(gQueris.graphEquality(graph1, null));
		assertFalse(gQueris.graphEquality(null, graph2));
		assertTrue(gQueris.graphEquality(null, null));

	}
	
}
