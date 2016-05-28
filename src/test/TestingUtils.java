package test;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.jung.graph.DirectedGraph;
import main.jung.MyEdge;

public class TestingUtils {

	public static void addEdge(DirectedGraph<Integer, MyEdge> graph, 
			String edge, Integer startNode, Integer endNode) {
		graph.addEdge(new MyEdge(edge, startNode, endNode), startNode, endNode);
		
	}
	
	public static List<String> quickArray(String ... strings) {
		List<String> tempList = new ArrayList<String>();
		for (String s : strings){
			tempList.add(s);
		}
		return tempList;
	}
}
