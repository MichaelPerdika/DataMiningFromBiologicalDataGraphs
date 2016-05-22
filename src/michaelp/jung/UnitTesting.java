package michaelp.jung;

import java.util.List;

import edu.uci.ics.jung.graph.DirectedGraph;

public class UnitTesting {

	public static void main(String[] args) {
		List<DirectedGraph<Integer, MyEdge>> graphTestSet = 
				TestingClass.fetchTestSet1();
		GraphQueriesAPI testGraphQueries = new GraphQueriesAPI(graphTestSet);
		Application.visualizeListOfSubGraphs(testGraphQueries.getSubGraphList());
		Application.printListOfSubGraphs(testGraphQueries.getSubGraphList());

	}
	
}
