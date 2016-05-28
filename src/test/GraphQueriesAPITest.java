package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.*;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import main.jung.GraphQueriesAPI;
import main.jung.MyEdge;

public class GraphQueriesAPITest {

	@Test
	public void testGraphEquality1() {
		DirectedGraph<Integer, MyEdge> graph1 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		DirectedGraph<Integer, MyEdge> graph2 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		/*********graph1***********/
		TestingClass.addEdge(graph1, "A", 0, 1);
		TestingClass.addEdge(graph1, "B", 1, 2);
		/*********graph2***********/
		TestingClass.addEdge(graph2, "A", 0, 1);
		TestingClass.addEdge(graph2, "B", 1, 2);
		/***create empty object****/
		GraphQueriesAPI gQueris = new GraphQueriesAPI();
		assertTrue("graph equality",gQueris.graphEquality(graph1, graph2));
	}
	
	@Test
	public void testGraphEquality2() {
		DirectedGraph<Integer, MyEdge> graph1 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		DirectedGraph<Integer, MyEdge> graph2 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		/*********graph1***********/
		TestingClass.addEdge(graph1, "A", 0, 1);
		TestingClass.addEdge(graph1, "B", 1, 2);
		/*********graph2***********/
		TestingClass.addEdge(graph2, "B", 0, 1);
		TestingClass.addEdge(graph2, "A", 1, 2);
		/***create empty object****/
		GraphQueriesAPI gQueris = new GraphQueriesAPI();
		assertFalse("graph equality",gQueris.graphEquality(graph1, graph2));
	}


	private List<String> quickArray(String ... strings) {
		List<String> tempList = new ArrayList<String>();
		for (String s : strings){
			tempList.add(s);
		}
		return tempList;
	}

	
}
