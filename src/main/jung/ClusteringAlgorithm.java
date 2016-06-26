package main.jung;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.jung.graph.DirectedGraph;

public class ClusteringAlgorithm {

	private List<DirectedGraph<Integer, MyEdge>> graphList;
	private List<DirectedGraph<Integer, MyEdge>> subGraphList;
	private List<List<Integer>> patternTable;
	
	public ClusteringAlgorithm() {
		// TODO Auto-generated constructor stub
		graphList = new ArrayList<DirectedGraph<Integer, MyEdge>>();
		subGraphList = new ArrayList<DirectedGraph<Integer, MyEdge>>();
		patternTable = new ArrayList<List<Integer>>();
	}
}
