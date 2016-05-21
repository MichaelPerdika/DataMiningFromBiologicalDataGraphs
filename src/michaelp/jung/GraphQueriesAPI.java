package michaelp.jung;

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
	@SuppressWarnings("unused")
	private GraphQueriesAPI(){}
	
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
	 * To Be erased. Dummy function that returns common subgraphs
	 * @param graph1
	 * @param graph2
	 */
	private static List<DirectedGraph<Integer, MyEdge>> findCommonEdges(
			DirectedGraph<Integer, MyEdge> graph1, 
			DirectedGraph<Integer, MyEdge> graph2) {

		List<DirectedGraph<Integer, MyEdge>> commonSubGraphList = 
				new ArrayList<DirectedGraph<Integer, MyEdge>>();
		Collection<MyEdge> colEdges1 = graph1.getEdges();
		Collection<MyEdge> colEdges2 = graph2.getEdges();
		//DirectedGraph<Integer, MyEdge> commonSubGraph = 
		//		new DirectedSparseMultigraph<Integer, MyEdge>();
		for (MyEdge edge1 : colEdges1){
			for (MyEdge edge2 : colEdges2){
				if (edge1.equals(edge2)){
					appendEdgeToSubGraph(commonSubGraphList, edge1, 
							edge1.getStartNode(), edge1.getEndNode());
				}
			}
		}
		/*
		//TBE
		for (Object temp : 
			commonSubGraphList.toArray()){
			System.out.print(temp.toString());
		}
		//TBE
		*/
		return commonSubGraphList;
	}

	/**
	 * 
	 * @param graphList
	 * @param edge
	 * @param startNode
	 * @param endNode
	 */
	private static void appendEdgeToSubGraph(
			List<DirectedGraph<Integer, MyEdge>> graphList, 
			MyEdge edge, Integer startNode, Integer endNode) {
		// TODO Auto-generated method stub
		if (graphList.isEmpty()){
			DirectedGraph<Integer, MyEdge> tempGraph = 
					new DirectedSparseMultigraph<Integer, MyEdge>();
			tempGraph.addEdge(edge, startNode, endNode);
			graphList.add(tempGraph);
		}
		else{
			// iterate through the existing graphs to see if this edge
			// should be an extension of a subGraph or a part of a new subGraph
			boolean neverFound = true;
			for (int i=0; i<graphList.size();i++){
				DirectedGraph<Integer, MyEdge> curSubGraph = graphList.get(i);
				// if the vertexes exist in the current subgraph then the 
				// edge must be added here
				if(curSubGraph.containsVertex(startNode) || 
						curSubGraph.containsVertex(endNode)){
					curSubGraph.addEdge(edge, startNode, endNode);
					neverFound = false;
					// NOTE! there could be a "break" here but we want 
					// to see if this edge could be added in other subGraphs as well 
				}
			}
			// now the edge is supposed to be appended wherever it fitted. If
			// not found then create a new subGraph with only this edge
			if (neverFound){
				DirectedGraph<Integer, MyEdge> tempGraph = 
						new DirectedSparseMultigraph<Integer, MyEdge>();
				tempGraph.addEdge(edge, startNode, endNode);
				graphList.add(tempGraph);
			}
		}
		
	}

	/**
	 * this method merges a list of subGraphs to the global subGraphList
	 * @param tempSubGraphs
	 */
	private void mergeSubGraphToPatternTable(
			List<DirectedGraph<Integer, MyEdge>> tempSubGraphs) {
		// TODO Auto-generated method stub
		
		for(Object curTempSubGraph : tempSubGraphs.toArray()){
			DirectedGraph<Integer, MyEdge> prev = (DirectedGraph<Integer, MyEdge>)curTempSubGraph;
			boolean alreadyExists = false;
			//line!=null && !line.isEmpty()
			if (getSubGraphList().isEmpty()){
				this.subGraphList.add(prev);
			}
			else{
				for(Object curSubGraph : getSubGraphList().toArray()){
					DirectedGraph<Integer, MyEdge> next = (DirectedGraph<Integer, MyEdge>)curSubGraph;
					if(prev.equals(next)){
						alreadyExists = true;
					}
				}
			}
			if(!alreadyExists){
				this.subGraphList.add(prev);
			}
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
