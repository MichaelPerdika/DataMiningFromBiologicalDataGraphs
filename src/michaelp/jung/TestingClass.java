package michaelp.jung;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

public class TestingClass {

	TestingClass(){}
	
	public static List<DirectedGraph<Integer, MyEdge>> fetchTestSet1() {
		List<DirectedGraph<Integer, MyEdge>> graphList = 
				new ArrayList<DirectedGraph<Integer, MyEdge>>();
		
		/*******graph1**********/
		DirectedGraph<Integer, MyEdge> graph1 = new
				DirectedSparseMultigraph<Integer, MyEdge>();
		addEdge(graph1, "A", 0, 1);
		addEdge(graph1, "B", 1, 2);
		addEdge(graph1, "C", 2, 3);
		addEdge(graph1, "D", 3, 0);
		graphList.add(graph1);
		/*******graph2**********/
		DirectedGraph<Integer, MyEdge> graph2 = new
				DirectedSparseMultigraph<Integer, MyEdge>();
		addEdge(graph2, "A",4, 5);
		addEdge(graph2, "B",5, 6);
		addEdge(graph2, "E",7, 6);
		addEdge(graph2, "F",4, 7);
		graphList.add(graph2);
		/*******graph3**********/
		DirectedGraph<Integer, MyEdge> graph3 = new
				DirectedSparseMultigraph<Integer, MyEdge>();
		addEdge(graph3, "A",9, 10);
		addEdge(graph3, "D",8, 9);
		addEdge(graph3, "C",10, 11);
		addEdge(graph3, "F",11, 12);
		addEdge(graph3, "E",12, 13);
		graphList.add(graph3);
		/*******graph4**********/
		DirectedGraph<Integer, MyEdge> graph4 = new
				DirectedSparseMultigraph<Integer, MyEdge>();
		addEdge(graph4, "E", 15, 16);
		addEdge(graph4, "F", 14, 15);
		graphList.add(graph4);
		
		return graphList;
	}
	
	private static void addEdge(DirectedGraph<Integer, MyEdge> graph, 
			String edge, Integer startNode, Integer endNode) {
		graph.addEdge(new MyEdge(edge, startNode, endNode), startNode, endNode);
		
	}

}
