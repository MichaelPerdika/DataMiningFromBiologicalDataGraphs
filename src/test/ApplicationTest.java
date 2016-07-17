package test;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import main.jung.GraphQueriesAPI;
import main.jung.MyEdge;

public class ApplicationTest {

	public static void main(String[] args) {
		List<DirectedGraph<Integer, MyEdge>> graphTestSet = 
				fetchTestSet2();
		GraphQueriesAPI testGraphQueries = new GraphQueriesAPI(graphTestSet);
		testGraphQueries.findPatternsInGraphs(1);
		testGraphQueries.printApplicationOutput();
		testGraphQueries.visualizeGraphList();
		testGraphQueries.visualizeSubGraphList();
		testGraphQueries.visualizeComplementarySubGraphList();
		
	}
	
	public static List<DirectedGraph<Integer, MyEdge>> fetchTestSet1() {
		List<DirectedGraph<Integer, MyEdge>> graphList = 
				new ArrayList<DirectedGraph<Integer, MyEdge>>();
		/** Desired results
		g0 = Vertices:0,1,2,3
		Edges:C [2,3] D [3,0] A [0,1] B [1,2] 
		g1 = Vertices:4,5,6,7
		Edges:F [4,7] A [4,5] B [5,6] E [7,6] 
		g2 = Vertices:8,9,10,11,12,13
		Edges:D [8,9] F [11,12] A [9,10] E [12,13] C [10,11] 
		g3 = Vertices:16,14,15
		Edges:E [15,16] F [14,15] 
		
		p0 = Vertices:0,1,2
		Edges:A [0,1] B [1,2] 
		p1 = Vertices:2,3
		Edges:C [2,3] 
		p2 = Vertices:0,1,3
		Edges:D [3,0] A [0,1] 
		p3 = Vertices:4,6,7
		Edges:E [7,6] F [4,7] 
		p4 = Vertices:4,5
		Edges:A [4,5] 
		
		   g0 g1 g2 g3 
		p0 1  1  0  0  
		p1 1  0  1  0  
		p2 1  0  1  0  
		p3 0  1  1  1  
		p4 1  1  1  0 
		*/
		
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
		/** desired results
		g0 = Vertices:0,1,2,3
		Edges:4.4.4.4 [2,3] 2.3.4.1 [3,0] 1.2.3.4 [0,1] 3.3.3.3 [1,2] 
		g1 = Vertices:10,11,12,13
		Edges:2.3.4.5 [13,10] 1.2.3.4 [10,11] 3.3.1.1 [11,12] 5.5.5.5 [12,13] 
		g2 = Vertices:20,21,22,23
		Edges:3.3.3.3 [21,22] 3.3.1.1 [23,20] 1.2.3.4 [20,21] 1.2.3.4 [22,23] 

		p0 = Vertices:0,1,2,3
		Edges:2.3.4.* [3,0] 1.2.3.4 [0,1] 3.3.* [1,2] 
		p1 = Vertices:0,1,2
		Edges:1.2.3.4 [0,1] 3.3.3.3 [1,2] 
		p2 = Vertices:0,1,2
		Edges:1.2.3.4 [0,1] 3.3.* [1,2] 
		p3 = Vertices:10,11,12
		Edges:1.2.3.4 [10,11] 3.3.1.1 [11,12] 

		   g0 g1 g2 
		p0  1  1  0  
		p1  1  0  1  
		p2  1  1  2  
		p3  0  1  1 
		*/
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
		TestingUtils.addEdge(graph2, "1.2.3.4",10, 11);
		TestingUtils.addEdge(graph2, "3.3.1.1",11, 12);
		TestingUtils.addEdge(graph2, "5.5.5.5",12, 13);
		TestingUtils.addEdge(graph2, "2.3.4.5",13, 10);
		graphList.add(graph2);
		/*******graph3**********/
		DirectedGraph<Integer, MyEdge> graph3 = new
				DirectedSparseMultigraph<Integer, MyEdge>();
		TestingUtils.addEdge(graph3, "1.2.3.4",20, 21);
		TestingUtils.addEdge(graph3, "3.3.3.3",21, 22);
		TestingUtils.addEdge(graph3, "1.2.3.4",22, 23);
		TestingUtils.addEdge(graph3, "3.3.1.1",23, 20);
		graphList.add(graph3);
		
		return graphList;
	}
	
	public static List<DirectedGraph<Integer, MyEdge>> fetchTestSet3() {
		/* This is two graphs that are looped*/
		List<DirectedGraph<Integer, MyEdge>> graphList = 
				new ArrayList<DirectedGraph<Integer, MyEdge>>();
		/** desired results
		g0 = Vertices:0,1,2,3
		Edges:4.4.4.4 [2,3] 2.3.4.1 [3,0] 1.2.3.4 [0,1] 3.3.3.3 [1,2] 
		g1 = Vertices:0,1,2,3
		Edges:2.3.4.1 [3,0] 1.2.3.4 [0,1] 3.3.3.3 [1,2] 4.4.4.4 [2,3] 

		p0 = Vertices:0,1,2,3
		Edges:2.3.4.1 [3,0] 3.3.3.3 [1,2] 4.4.4.4 [2,3] 1.2.3.4 [0,1] 

		   g0 g1 
		p0 1  1  
		*/
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
		TestingUtils.addEdge(graph2, "1.2.3.4", 0, 1);
		TestingUtils.addEdge(graph2, "3.3.3.3", 1, 2);
		TestingUtils.addEdge(graph2, "4.4.4.4", 2, 3);
		TestingUtils.addEdge(graph2, "2.3.4.1", 3, 0);
		graphList.add(graph2);

		
		return graphList;
	}
	
}
