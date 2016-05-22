package main.jung;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

public class GraphQueriesAPI {

	private List<DirectedGraph<Integer, MyEdge>> graphList;
	private List<DirectedGraph<Integer, MyEdge>> subGraphList;
	private List<List<Integer>> patternTable;
	
	/**
	 * empty constructor never to be called
	 */
	public GraphQueriesAPI(){}
	
	/**
	 * Constructor that will create the grapSet the subGraphSet and the patternTable
	 * @param graphSet
	 */
	public GraphQueriesAPI(List<DirectedGraph<Integer, MyEdge>> graphSet) {
		//TODO
		if (graphSet.size() < 2){
			System.out.println("ERROR!!! The size of graphSet must have at least"
					+ " two instances. You gave: "+graphSet.size()+". Exiting");
			System.exit(0);
		}
		setGraphList(graphSet);
		setSubGraphList(new ArrayList<DirectedGraph<Integer, MyEdge>>());
		this.patternTable = new ArrayList<List<Integer>>();
		findPatternsInGraphs();
	}
	
	/**
	 * This method finds all the subgraphs and fills the patternTable and subGraphList
	 */
	public void findPatternsInGraphs(){
		//TODO
		for (int i=0;i<graphList.size();i++){
			for (int j=i+1;j<graphList.size();j++){
				List<DirectedGraph<Integer, MyEdge>> tempSubGraphs = 
						findCommonSubGraphs(graphList.get(i), graphList.get(j));
				mergeSubGraphToPatternTable(tempSubGraphs);
			}
		}
	}

	/**
	 * This method returns a List of common subGraphs between graph1, graph2
	 * @param graph1 the first graph to be checked
	 * @param graph2 the second graph to be checked
	 * @return the common subGraphs as a list of DirectedGraphs.
	 */
	public List<DirectedGraph<Integer, MyEdge>> findCommonSubGraphs(
			DirectedGraph<Integer, MyEdge> graph1, 
			DirectedGraph<Integer, MyEdge> graph2){
		//TODO
		return findCommonEdges(graph1, graph2);
	}
	
	/**
	 * insert description
	 * @param graph1
	 * @param graph2
	 */
	 public List<DirectedGraph<Integer, MyEdge>> findCommonEdges(
			DirectedGraph<Integer, MyEdge> graph1, 
			DirectedGraph<Integer, MyEdge> graph2) {

		List<DirectedGraph<Integer, MyEdge>> commonSubGraphList = 
				new ArrayList<DirectedGraph<Integer, MyEdge>>();
		Collection<MyEdge> colEdges1 = graph1.getEdges();
		Collection<MyEdge> colEdges2 = graph2.getEdges();
		//DirectedGraph<Integer, MyEdge> commonSubGraph = 
		//		new DirectedSparseMultigraph<Integer, MyEdge>();
		for (MyEdge edge1 : colEdges1){
			//if already found then skip checking. Go to another edge1.
			if(!checkIfAlreadyFoundInCommonSubGraphList(commonSubGraphList, edge1)){
				for (MyEdge edge2 : colEdges2){
					if (edge1.equals(edge2)){
						DirectedGraph<Integer, MyEdge> commonSubGraph = 
								new DirectedSparseMultigraph<Integer, MyEdge>();
						appendNextEdgesToCommonSubGraph(commonSubGraph, graph1, graph2, edge1, edge2);
						appendPreviousEdgesToCommonSubGraph(commonSubGraph, graph1, graph2, edge1, edge2);
						//this might need rework
						commonSubGraphList.add(commonSubGraph);
					}
				}
			}
			
		}
		return commonSubGraphList;
	}

	/**
	 * checks if an edge is contained in the list of SubGraphs.
	 * @param commonSubGraphList
	 * @param edge
	 * @return true if the edge is found in at least one subGraph from the list
	 */
	private boolean checkIfAlreadyFoundInCommonSubGraphList(
			List<DirectedGraph<Integer, MyEdge>> commonSubGraphList, MyEdge edge) {
		// TODO Auto-generated method stub
		
		for (Object curObj : commonSubGraphList.toArray()){
			DirectedGraph<Integer, MyEdge> curSubGraph = 
					(DirectedGraph<Integer, MyEdge>)curObj;
			if(curSubGraph.containsEdge(edge)){
				return true;
			}
		}
		return false;
	}

	/**
	 * recursive method that will add all the next edges to commonSubGraph.
	 * This method mutates "commonSubGraph"
	 * @param commonSubGraph
	 * @param graph1
	 * @param graph2
	 * @param edge1
	 * @param edge2
	 */
	private static void appendNextEdgesToCommonSubGraph(DirectedGraph<Integer, MyEdge> commonSubGraph,
			DirectedGraph<Integer, MyEdge> graph1, DirectedGraph<Integer, MyEdge> graph2, 
			MyEdge edge1, MyEdge edge2) {
		// TODO Auto-generated method stub
		// It doesn't matter if we add edge1 or edge2 since they are the same.
		if (!commonSubGraph.containsEdge(edge1)){
			commonSubGraph.addEdge(edge1, edge1.getStartNode(), edge1.getEndNode());
			
			Collection<MyEdge> nextEdges1 = graph1.getOutEdges(edge1.getEndNode());
			Collection<MyEdge> nextEdges2 = graph2.getOutEdges(edge2.getEndNode());
			for (MyEdge next1 : nextEdges1){
				for (MyEdge next2: nextEdges2){
					if(next1.equals(next2)){
						appendNextEdgesToCommonSubGraph(commonSubGraph,graph1,graph2,next1,next2);
					}
				}
			}
		}
		//else do nothing just return from the method
	}
	
	/**
	 * recursive method that will add all the previous edges to commonSubGraph.
	 * The previous edges are added indirectly because they are added in the call 
	 * of appendNextEdgesToCommonSubGraph.
	 * This method mutates "commonSubGraph"
	 * @param commonSubGraph
	 * @param graph1
	 * @param graph2
	 * @param edge1
	 * @param edge2
	 */
	private static void appendPreviousEdgesToCommonSubGraph(DirectedGraph<Integer, MyEdge> commonSubGraph,
			DirectedGraph<Integer, MyEdge> graph1, DirectedGraph<Integer, MyEdge> graph2, MyEdge edge1, MyEdge edge2) {
		// TODO Auto-generated method stub
		//this has to be here because when going backward to see previous edges these edges may
		//have more than 1 next edges. So need to search these branches first before going backwards
		//again. If the next edges are already in the commonSubGraph then "appendNextEdgesToCommonSubGraph"
		// will do nothing
		appendNextEdgesToCommonSubGraph(commonSubGraph, graph1, graph2, edge1, edge2);

		// Here we append only the previous ones.
		Collection<MyEdge> previousEdges1 = graph1.getInEdges(edge1.getStartNode());
		Collection<MyEdge> previousEdges2 = graph2.getInEdges(edge2.getStartNode());
		for (MyEdge previous1 : previousEdges1){
			for (MyEdge previous2: previousEdges2){
				if(previous1.equals(previous2)){
					appendPreviousEdgesToCommonSubGraph(commonSubGraph, graph1, graph2, 
							previous1, previous2);
				}
			}
		}
	}
	

	
	
	/**
	 * this method merges a list of subGraphs to the global subGraphList
	 * @param tempSubGraphs
	 */
	public void mergeSubGraphToPatternTable(
			List<DirectedGraph<Integer, MyEdge>> tempSubGraphs) {
		// TODO Auto-generated method stub
		
		for(Object curTempSubGraph : tempSubGraphs.toArray()){
			DirectedGraph<Integer, MyEdge> prev = (DirectedGraph<Integer, MyEdge>)curTempSubGraph;
			boolean alreadyExists = false;
			//line!=null && !line.isEmpty()
			if (getSubGraphList().isEmpty()){
				this.subGraphList.add(prev);
				continue;
			}
			else{
				for(Object curSubGraph : getSubGraphList().toArray()){
					DirectedGraph<Integer, MyEdge> next = (DirectedGraph<Integer, MyEdge>)curSubGraph;
					if(graphEquality(prev,next)){
						alreadyExists = true;
					}
				}
			}
			if(!alreadyExists){
				this.subGraphList.add(prev);
			}
		}
		
	}
	
	/**
	 * this function checks if two graphs are equal (have the same edges, vertices are irrelevant)
	 * @param graph1
	 * @param graph2
	 * @return true if equal or else false
	 */
	public boolean graphEquality(DirectedGraph<Integer, MyEdge> graph1, 
			DirectedGraph<Integer, MyEdge> graph2) {
		// TODO This method needs better implementation. For example A->B is not same as B->A
		// but this method will return True. Do this in the future.
		
		if (graph1.getEdgeCount()!=graph2.getEdgeCount() || 
				graph1.getVertexCount()!=graph2.getVertexCount()){
			return false;
		}
		else{
			Collection<MyEdge> edgesCollection1 = graph1.getEdges();
			Collection<MyEdge> edgesCollection2 = graph2.getEdges();
			for ( MyEdge edge1 : edgesCollection1){
				boolean noEqual = true;
				for ( MyEdge edge2 : edgesCollection2){
					if (edge1.equals(edge2)){
						noEqual = false;
						break;
					}
				}
				if(noEqual){
					return false;
				}
			}
			return true;
		}
		
	}

	public List<DirectedGraph<Integer, MyEdge>> getGraphList() {
		return graphList;
	}

	public void setGraphList(List<DirectedGraph<Integer, MyEdge>> graphSet) {
		//create a copy of it
		this.graphList = new ArrayList<DirectedGraph<Integer, MyEdge>> (graphSet);
	}

	public List<DirectedGraph<Integer, MyEdge>> getSubGraphList() {
		return subGraphList;
	}

	public void setSubGraphList(List<DirectedGraph<Integer, MyEdge>> subGraphList) {
		this.subGraphList = subGraphList;
	}

	public List<List<Integer>> getPatternTable() {
		return patternTable;
	}

	public void setPatternTable(List<List<Integer>> patternTable) {
		this.patternTable = patternTable;
	}

}
