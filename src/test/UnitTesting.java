package test;

import java.util.List;

import edu.uci.ics.jung.graph.DirectedGraph;
import main.jung.Application;
import main.jung.GraphQueriesAPI;
import main.jung.MyEdge;

public class UnitTesting {

	public static void main(String[] args) {
		List<DirectedGraph<Integer, MyEdge>> graphTestSet = 
				TestingClass.fetchTestSet1();
		GraphQueriesAPI testGraphQueries = new GraphQueriesAPI(graphTestSet);
		Application.visualizeListOfSubGraphs(testGraphQueries.getSubGraphList());
		Application.printListOfSubGraphs(testGraphQueries.getSubGraphList());

	}
	
}
