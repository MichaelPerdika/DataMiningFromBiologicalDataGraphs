package test;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import main.jung.Application;
import main.jung.GraphQueriesAPI;
import main.jung.MyEdge;

public class ApplicationTest {

	public static void main(String[] args) {
		List<DirectedGraph<Integer, MyEdge>> graphTestSet = 
				fetchTestSet2();
		GraphQueriesAPI testGraphQueries = new GraphQueriesAPI(graphTestSet);
		testGraphQueries.findPatternsInGraphs(0.25);
		//Application.visualizeListOfSubGraphs(testGraphQueries.getSubGraphList());
		//Application.printListOfSubGraphs(testGraphQueries.getSubGraphList());
		testGraphQueries.printPatternTable();
		
	}
	
	public static List<DirectedGraph<Integer, MyEdge>> fetchTestSet1() {
		List<DirectedGraph<Integer, MyEdge>> graphList = 
				new ArrayList<DirectedGraph<Integer, MyEdge>>();
		
		/*******graph1**********/
		DirectedGraph<Integer, MyEdge> graph1 = new
				DirectedSparseMultigraph<Integer, MyEdge>();
		TestingUtils.addEdge(graph1, "A", 0, 1);
		TestingUtils.addEdge(graph1, "B", 1, 2);
		TestingUtils.addEdge(graph1, "C", 2, 3);
		TestingUtils.addEdge(graph1, "D", 3, 0);
		graphList.add(graph1);
		/*******graph2**********/
		DirectedGraph<Integer, MyEdge> graph2 = new
				DirectedSparseMultigraph<Integer, MyEdge>();
		TestingUtils.addEdge(graph2, "A",4, 5);
		TestingUtils.addEdge(graph2, "B",5, 6);
		TestingUtils.addEdge(graph2, "E",7, 6);
		TestingUtils.addEdge(graph2, "F",4, 7);
		graphList.add(graph2);
		/*******graph3**********/
		DirectedGraph<Integer, MyEdge> graph3 = new
				DirectedSparseMultigraph<Integer, MyEdge>();
		TestingUtils.addEdge(graph3, "A",9, 10);
		TestingUtils.addEdge(graph3, "D",8, 9);
		TestingUtils.addEdge(graph3, "C",10, 11);
		TestingUtils.addEdge(graph3, "F",11, 12);
		TestingUtils.addEdge(graph3, "E",12, 13);
		graphList.add(graph3);
		/*******graph4**********/
		DirectedGraph<Integer, MyEdge> graph4 = new
				DirectedSparseMultigraph<Integer, MyEdge>();
		TestingUtils.addEdge(graph4, "E", 15, 16);
		TestingUtils.addEdge(graph4, "F", 14, 15);
		graphList.add(graph4);
		
		return graphList;
	}
	
	public static List<DirectedGraph<Integer, MyEdge>> fetchTestSet2() {
		List<DirectedGraph<Integer, MyEdge>> graphList = 
				new ArrayList<DirectedGraph<Integer, MyEdge>>();
		
		/*******graph1**********/
		DirectedGraph<Integer, MyEdge> graph1 = new
				DirectedSparseMultigraph<Integer, MyEdge>();
		TestingUtils.addEdge(graph1, "1.2.3.4", 0, 1);
		TestingUtils.addEdge(graph1, "3.3.3.3", 1, 2);
		TestingUtils.addEdge(graph1, "4.4.4.4", 2, 3);
		TestingUtils.addEdge(graph1, "2.3.4.1", 3, 0);
		graphList.add(graph1);
		/*******graph2**********/
		DirectedGraph<Integer, MyEdge> graph2 = new
				DirectedSparseMultigraph<Integer, MyEdge>();
		TestingUtils.addEdge(graph2, "1.2.3.4",0, 1);
		TestingUtils.addEdge(graph2, "3.3.1.1",1, 2);
		TestingUtils.addEdge(graph2, "5.5.5.5",2, 3);
		TestingUtils.addEdge(graph2, "2.3.4.5",3, 0);
		graphList.add(graph2);
		/*******graph3**********
		DirectedGraph<Integer, MyEdge> graph3 = new
				DirectedSparseMultigraph<Integer, MyEdge>();
		TestingUtils.addEdge(graph3, "1.2.3.4",0, 1);
		TestingUtils.addEdge(graph3, "3.3.3.3",1, 2);
		TestingUtils.addEdge(graph3, "2.3.5.5",3, 0);
		graphList.add(graph3);
		*/
		
		return graphList;
	}
}
