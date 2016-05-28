package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.*;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import main.jung.GraphQueriesAPI;
import main.jung.MyEdge;

public class CanonicalLabelEqualityTest {

	GraphQueriesAPI gQueris;
	Map<Integer, Map<Integer, List<String>>> canLabAdjList1; 
	Map<Integer, Map<Integer, List<String>>> canLabAdjList2;
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
		TestingUtils.addEdge(graph2, "A", 10, 11);
		TestingUtils.addEdge(graph2, "B", 11, 12);

	}
	
	/**
	 * Tears down the test fixture. 
	 * (Called after every test case method.)
	 */
	@After
	public void tearDown() {
		canLabAdjList1 = null;
		canLabAdjList2 = null;
		graph1 = null;
		graph2 = null;
		gQueris = null;
	}
	
	@Test
	public void sameAdjLists() {

		canLabAdjList1 = gQueris.getCanonicalLabelAdjList(graph1);
		canLabAdjList2 = gQueris.getCanonicalLabelAdjList(graph2);
		/** canLabAdjList1 = {0 ={1 =[[A ]]}, 1 ={2 =[[B ]]}, 2 ={}} 
		 *  canLabAdjList2 = {10={11=[[A ]]}, 11={12=[[B ]]}, 12={}}**/
		assertTrue(gQueris.canonicalLabelEquality(canLabAdjList1, canLabAdjList2));
	}
	
	@Test
	public void allNullOptions() {
		canLabAdjList1 = gQueris.getCanonicalLabelAdjList(graph1);
		assertFalse(gQueris.canonicalLabelEquality(canLabAdjList1, null));
		assertFalse(gQueris.canonicalLabelEquality(null, canLabAdjList1));
		assertTrue(gQueris.canonicalLabelEquality(null, null));

	}
	
	@Test
	public void diffSizeAdjLists() {
		TestingUtils.addEdge(graph1, "C", 2, 3);
		canLabAdjList1 = gQueris.getCanonicalLabelAdjList(graph1);
		canLabAdjList2 = gQueris.getCanonicalLabelAdjList(graph2);
		/** canLabAdjList1 = {0 ={1 =[[A ]]}, 1 ={2 =[[B ]]}, 2 ={3=[[C ]]}, 3={}}
		 *  canLabAdjList2 = {10={11=[[A ]]}, 11={12=[[B ]]}, 12={}}**/
		assertFalse(gQueris.canonicalLabelEquality(canLabAdjList1, canLabAdjList2));
	}
	
	@Test
	public void atLeastOneEdgeWithDiffName() {
		TestingUtils.addEdge(graph1, "C", 2, 0);
		TestingUtils.addEdge(graph2, "D", 12, 10);
		canLabAdjList1 = gQueris.getCanonicalLabelAdjList(graph1);
		canLabAdjList2 = gQueris.getCanonicalLabelAdjList(graph2);
		/** canLabAdjList1 = {0 ={1 =[[A ]]}, 1 ={2 =[[B ]]}, 2 ={0 =[[C ]]}} 
		 *  canLabAdjList2 = {10={11=[[A ]]}, 11={12=[[B ]]}, 12={10=[[D ]]}}**/
		assertFalse(gQueris.canonicalLabelEquality(canLabAdjList1, canLabAdjList2));
	}
	
	@Test
	public void sameSizeAtLeastOneDifferentContent() {
		TestingUtils.addEdge(graph1, "B2A", 1, 0);
		canLabAdjList1 = gQueris.getCanonicalLabelAdjList(graph1);
		canLabAdjList2 = gQueris.getCanonicalLabelAdjList(graph2);
		System.out.println(canLabAdjList1);
		System.out.println(canLabAdjList2);
		/** canLabAdjList1 = {0 ={1 =[[A ]]}, 1 ={0 =[[B2A ]] , 2 =[[B ]]}, 2 ={}} 
		 *  canLabAdjList2 = {10={11=[[A ]]}, 11={              12=[[B ]]}, 12={}}**/
		assertFalse(gQueris.canonicalLabelEquality(canLabAdjList1, canLabAdjList2));
	}
	
}
