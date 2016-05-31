package main.jung;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

public class GraphQueriesAPI {

	private List<DirectedGraph<Integer, MyEdge>> graphList;
	private List<DirectedGraph<Integer, MyEdge>> subGraphList;
	private List<List<Integer>> patternTable;
	
	/**
	 * empty constructor initialize to null
	 */
	public GraphQueriesAPI(){
		setGraphList(new ArrayList<DirectedGraph<Integer, MyEdge>>());
		setSubGraphList(new ArrayList<DirectedGraph<Integer, MyEdge>>());
		setPatternTable(new ArrayList<List<Integer>>());
	}
	
	/**
	 * Constructor that will create the grapSet the subGraphSet and the patternTable
	 * @param graphSet
	 */
	public GraphQueriesAPI(List<DirectedGraph<Integer, MyEdge>> graphSet) {
		//TODO
		setGraphList(graphSet);
		setSubGraphList(new ArrayList<DirectedGraph<Integer, MyEdge>>());
		setPatternTable(new ArrayList<List<Integer>>());
	}
	
	/**
	 * This is the main method of this API that finds all the 
	 * subgraphs and fills the patternTable and subGraphList
	 */
	public void findPatternsInGraphs(){
		//TODO add min_support sigma = {0, 1}
		if (this.graphList.size() < 2){
			System.out.println("ERROR!!! The size of graphSet must have at least"
					+ " two instances. You gave: "+this.graphList.size()+". Exiting");
			System.exit(0);
		}
		// check all graphs with each other.
		for (int i=0;i<graphList.size();i++){
			for (int j=i+1;j<graphList.size();j++){
				mergeSubGraphsToGlobalSubGraphList(
						findCommonSubGraphsBetweenTwoGraphs(graphList.get(i), graphList.get(j)), 
						i, j);
			}
		}
		fillTheCompletePatternTable();
	}


	/**
	 * This method returns a List of common subGraphs between graph1, graph2
	 * @param graph1 the first graph to be checked
	 * @param graph2 the second graph to be checked
	 * @return the common subGraphs as a list of DirectedGraphs.
	 */
	public List<DirectedGraph<Integer, MyEdge>> findCommonSubGraphsBetweenTwoGraphs(
			DirectedGraph<Integer, MyEdge> graph1, 
			DirectedGraph<Integer, MyEdge> graph2){
		//TODO this method is useless for now. Either erase it or utilize it
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
		for (MyEdge edge1 : colEdges1){
			//if already found then skip checking. Go to another edge1.
			if(!checkIfEdgeAlreadyFoundInCommonSubGraphList(commonSubGraphList, edge1)){
				for (MyEdge edge2 : colEdges2){
					//TODO instead of equals use something like "similarity" that takes
					// as argument min_support sigma = 0 - 100 %
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
	private boolean checkIfEdgeAlreadyFoundInCommonSubGraphList(
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
	 * this method merges a list of subGraphs to the global subGraphList. 
	 * And updated the patternTable
	 * @param tempSubGraphs is the comonSubGraphs between the comparison of graph1ID and graph2ID
	 * @param graph1ID first graph ID. Is going to needed to fill the patternTable
	 * @param graph2ID second graph ID. Is going to needed to fill the patternTable
	 */
	public void mergeSubGraphsToGlobalSubGraphList(
			List<DirectedGraph<Integer, MyEdge>> tempSubGraphs, int graph1ID, int graph2ID) {
		// TODO maybe use IsSubGraphInPatternTable here???
		// TODO do the above
		
		for(Object curTempSubGraph : tempSubGraphs.toArray()){
			DirectedGraph<Integer, MyEdge> prev = (DirectedGraph<Integer, MyEdge>)curTempSubGraph;
			boolean alreadyExists = false;
			if (getSubGraphList().isEmpty()){
				this.subGraphList.add(prev);
				appendNewRowInPatternTable(graph1ID, graph2ID);
				continue;
			}
			else{
				int subGraphId = 0;
				for(Object curSubGraph : getSubGraphList().toArray()){
					DirectedGraph<Integer, MyEdge> next = (DirectedGraph<Integer, MyEdge>)curSubGraph;
					if(graphEquality(prev,next)){
						alreadyExists = true;
						updateExistingRowInPatternTable(subGraphId, graph1ID, graph2ID);
					}
					subGraphId++;
				}
			}
			if(!alreadyExists){
				this.subGraphList.add(prev);
				appendNewRowInPatternTable(graph1ID, graph2ID);
			}
		}
		
	}

	/**
	 * this method adds a new row in the patternTable with 1 for the graphs that 
	 * "match" with the two graph id's and -1 elsewhere
	 * @param graph1id
	 * @param graph2id
	 */
	private void appendNewRowInPatternTable(int graph1id, int graph2id) {
		// TODO Auto-generated method stub
		List<Integer> newRow = new ArrayList<Integer>(graphList.size()); 
		for (int index=0; index<graphList.size();index++){
			if (index==graph1id || index==graph2id) newRow.add(index, 1);
			else newRow.add(index, -1);
		}
		this.patternTable.add(newRow);
		
	}
	
	/**
	 * this method updates the row subGraphId and columns graph1ID and graph2ID.
	 * If these cells have -1 then convert them to 1. If these cells have >=1 then 
	 * increment them by 1.
	 * @param subGraphId
	 * @param graph1id
	 * @param graph2id
	 */
	private void updateExistingRowInPatternTable(int subGraphId, int graph1id, int graph2id) {
		int cellValue1 = this.patternTable.get(subGraphId).get(graph1id);
		int cellValue2 = this.patternTable.get(subGraphId).get(graph2id);
		// TODO I only set values to 1. How to make it increment???
		// TODO maybe outside of this method or pass more arguments

		/********* graph1id ********/
		if ( cellValue1 != 0){
			insertPatternTableCell(subGraphId, graph1id, 1);}
		else {//it should never enter here
			System.out.println("error in updateExistingRowInPatternTable, cell ("
					+ subGraphId +", "+graph1id+") is 0");
			System.exit(0);}
		/********* graph2id ********/
		if ( cellValue2 != 0){
			insertPatternTableCell(subGraphId, graph2id, 1);}
		else {//it should never enter here
			System.out.println("error in updateExistingRowInPatternTable, cell ("
					+ subGraphId +", "+graph2id+") is 0");
			System.exit(0);}
		
	}
	
	/**
	 * In this method the patternTable has created all possible rows.
	 * But there are still -1 so search for all -1 and alter them to 0 or 1.
	 */
	private void fillTheCompletePatternTable() {
		// TODO Auto-generated method stub
		// we iterate through the columns (graphs) and check the patterns with
		// -1 if they match with any other pattern of graph(col)
		for (int col=0; col<getGraphList().size(); col++){
			for (int row=0; row<getSubGraphList().size(); row++){
				if (getPatternTable().get(row).get(col) >= 1){
					continue; // go to the next row (pattern)
				}
				// need to check this -1 and do it 1 or 0.
				else if (getPatternTable().get(row).get(col) == -1 ){
					// need to check if pattern (row,col) match any from (*,col).
					updateColumnOfPatternTable(row, col);
				}
				else{//it should never enter here (value ==0)
					System.out.println("error in fillTheCompletePatternTable, cell ("
							+ row +", "+col+") is 0");
					System.exit(0);
				}
			}
		}
		
	}
	
	/**
	 * we want to check the testingRow (with value -1) of the pattern table
	 * given the specific column (graph id) check if the pattern testingRow match
	 * any other pattern of the same column. If yes then testingRow = 1 else 0.
	 * @param testingRow
	 * @param columnGraph
	 */
	private void updateColumnOfPatternTable(int testingRow, int columnGraph) {
		// TODO Auto-generated method stub
		for (int row=0; row<getSubGraphList().size(); row++){
			if (row == testingRow || getPatternTable().get(row).get(columnGraph)<=0){
				continue;
			}
			else{
				List<DirectedGraph<Integer, MyEdge>> commonSubGraph = 
						findCommonSubGraphsBetweenTwoGraphs(
								getSubGraphList().get(testingRow), 
								getSubGraphList().get(row));
				// no common patterns
				if (commonSubGraph.size() == 0){
					continue;
				}
				else{
					for (Object tempObject : commonSubGraph.toArray()){
						DirectedGraph<Integer, MyEdge> tempSubGraph = 
								(DirectedGraph<Integer, MyEdge>)tempObject;
						// all the commonSubGraphs that are found must be in the pattern table.
						// if not it is an error thats why it must never enter the if statement
						int foundIndex = isSubGraphInPatternTable(tempSubGraph);
						if (foundIndex <= 0){
							System.out.println("error in updateColumnOfPatternTable."
									+ "This pattern was found and it wasn't on the patternTable");
							System.out.println(tempObject);
							System.exit(0);
						}
						// if found then update -1 to 1
						if (foundIndex == testingRow){
							insertPatternTableCell(testingRow, columnGraph, 1);
							break;
						}
						else{
							if(getPatternTable().get(foundIndex).get(columnGraph) == -1){
								System.out.println("Warning 1 in updateColumnOfPatternTable. check this out");
							}
							else if(getPatternTable().get(foundIndex).get(columnGraph) == 0){
								System.out.println("error in updateColumnOfPatternTable."
										+ "the pattern that was found has value 0");
								System.exit(0);
							}
							// else if it has >=1 do nothing
						}
					}
				}
			}
		}
		// if it was never found and the change -1 to 1 hasn't happen then set to 0
		if (getPatternTable().get(testingRow).get(columnGraph)== -1){
			insertPatternTableCell(testingRow, columnGraph, 0);
		}
	}

	/**
	 * this method checks if subGraph exists in the patternTable and returns its index 
	 * (instead of true or false). If found return the index 0 to n. If not found return -1
	 * and if the subGraphList is empty return -2.
	 * @param subGraph
	 * @return -2, -1 or 0 to n if subGraphList is empty, graph not found or index if found 
	 * correspondingly
	 */
	private int isSubGraphInPatternTable(DirectedGraph<Integer, MyEdge> subGraph) {
		// TODO Auto-generated method stub
		if (getSubGraphList().isEmpty()){
			return -2;
		}
		else{
			int index = 0;
			for(Object curObj : getSubGraphList().toArray()){
				DirectedGraph<Integer, MyEdge> curSubGraph = (DirectedGraph<Integer, MyEdge>)curObj;
				if(graphEquality(subGraph,curSubGraph)){
					return index;
				}
				index++;
			}
		}
		// if not found the return false
		return -1;
	}

	/**
	 * prints the pattern table
	 */
	public void printPatternTable(){
		
		/**** print the graphs ***/
		for (int i=0; i<graphList.size();i++){
			System.out.println("g"+i+" = "+graphList.get(i));
		}
		System.out.println("");
		/**** print the patterns */
		for (int i=0; i<subGraphList.size();i++){
			System.out.println("p"+i+" = "+subGraphList.get(i));
		}
		System.out.println("");
		/**** print the grid******/
		System.out.print("   ");
		for (int num=0; num<graphList.size();num++){
			System.out.print("g"+num+" ");
		}
		System.out.println("");
		for (int i=0;i<patternTable.size();i++){
			System.out.print("p"+i+" ");
			for (int j=0;j<patternTable.get(i).size();j++){
				System.out.print(patternTable.get(i).get(j)+"  ");
			}
			System.out.println("");
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
		// TODO This method needs better implementation. 
		// For example A->B is not same as B->A
		// but this method will return True. Do this in the future.
		if (graph1 == null && graph2 == null){
	        return true;
	    }
		if((graph1 == null && graph2 != null) 
			      || graph1 != null && graph2 == null){
			        return false;
		}
		if (graph1.getEdgeCount()!=graph2.getEdgeCount() || 
				graph1.getVertexCount()!=graph2.getVertexCount()){
			return false;
		}
		else{
			
			Map<Integer, Map<Integer, List<String>>> canLabAdjList1 = 
					getCanonicalLabelAdjList(graph1);
			Map<Integer, Map<Integer, List<String>>> canLabAdjList2 = 
					getCanonicalLabelAdjList(graph2);
			return canonicalLabelEquality(canLabAdjList1, canLabAdjList2);
		}
	}

	/**
	 * this method returns the "canonical label" adjacent list of a graph.
	 * This list shows which vertex goes to which vertices with which edges
	 * @param graph
	 * @return the "canonical label" adjacent list.
	 */
	public Map<Integer, Map<Integer, List<String>>> getCanonicalLabelAdjList(
			DirectedGraph<Integer, MyEdge> graph){

		Map<Integer, Map<Integer, List<String>>> cLAdjList = 
				new HashMap<Integer, Map<Integer, List<String>>>();
		for (Integer vertex : graph.getVertices()){
			Map<Integer, List<String>> tempMap = new HashMap<Integer, List<String>>();
			for (MyEdge edge : graph.getOutEdges(vertex)){
				Integer endNode = edge.getEndNode();
				String edgeName = edge.toString();
				if (tempMap.containsKey(endNode)){
					tempMap.get(endNode).add(edgeName);
				}
				else{
					ArrayList<String> tempArray = new ArrayList<String>();
					tempArray.add(edgeName);
					tempMap.put(endNode, tempArray);
				}
			}
			cLAdjList.put(vertex, tempMap);
		}
		
		return cLAdjList;
	}

	/**
	 * this method gets two canonical label adjacent lists and checks if
	 * they are equal.
	 * @param canLabAdjList1 
	 * @param canLabAdjList2
	 * @return
	 */
	public boolean canonicalLabelEquality(Map<Integer, Map<Integer, List<String>>> canLabAdjList1,
			Map<Integer, Map<Integer, List<String>>> canLabAdjList2) {
		
		if (canLabAdjList1 == null && canLabAdjList2 == null){
	        return true;
	    }
		if((canLabAdjList1 == null && canLabAdjList2 != null) 
			      || canLabAdjList1 != null && canLabAdjList2 == null
			      || canLabAdjList1.size() != canLabAdjList2.size()){
			        return false;
		}
		// create a deep copy of both of them because we are going 
		// to mutate them
		Map<Integer, Map<Integer, List<String>>> adjList1 = DeepClone.deepClone(canLabAdjList1);
		Map<Integer, Map<Integer, List<String>>> adjList2 = DeepClone.deepClone(canLabAdjList2);
		// if they haven't the same size or are empty return false.
		if (adjList1.size() != adjList2.size() || adjList2.isEmpty() || adjList1.isEmpty()){
			return false;
		}
		// as equality we denote two CL that have the same "form of vertices"
		// that have the same edge names. For example: 
		// {1 : {2:['a', 'm'],3:['b']}, 2: { 3:['c']}} is equal to
		// {4 : {5:['a', 'm'],6:['b']}, 5: { 6:['c']}}
		while (!adjList1.isEmpty() ){
			boolean matchFound = false;
			// get the next entry from adjList1
			Entry<Integer, Map<Integer, List<String>>> entry1 = adjList1.entrySet().iterator().next();
			// iterate all the entries of the 2nd list to see if it match.
			for (Entry<Integer, Map<Integer, List<String>>> entry2 : adjList2.entrySet()){
				if(canonicalLabelEntryEquality(entry1.getValue(), entry2.getValue())){
					adjList1.remove(entry1.getKey());
					adjList2.remove(entry2.getKey());
					matchFound = true;
					break;
				}
			}
			// if the break from the above for loop is not reached then 
			// entry1 dind't find a map from entry 2 to match
			if (!matchFound) return false;
		}
		// if it exits normally the while then the two lists are the same so return true
		return true;
	}
	
	/**
	 * this method gets two entries of canonical label rows 
	 * and will return true if they are equal. By equal we denote same number 
	 * of vertices (not same indexes in particular) that have the same edge names
	 * @param map1
	 * @param map2
	 * @return true if they are "equal" else false.
	 */
	public boolean canonicalLabelEntryEquality(Map<Integer, List<String>> map1,
			Map<Integer, List<String>> map2) {
		
		if (map1 == null && map2 == null){
	        return true;
	    }
		if((map1 == null && map2 != null) 
			      || map1 != null && map2 == null
			      || map1.size() != map2.size()){
			        return false;
		}
		// we are going to mutate the map2 so copy it
		Map<Integer, List<String>> m2 = DeepClone.deepClone(map2);

		for (Entry<Integer, List<String>> entry1 : map1.entrySet()){
			boolean matchFound = false;
			for (Entry<Integer, List<String>> entry2 : m2.entrySet()){
				
				if (equalLists(entry1.getValue(), entry2.getValue())){
					//go the the next iteration of entry1 and remove
					// the entry of m2. We don't won't to check again
					m2.remove(entry2.getKey());
					matchFound = true;
					break;
				}
			}
			// if no match then return false
			if (!matchFound)	return false;
		}
		// if it exits the double for loop then it means that the two
		// maps indeed match
		return true;
	}

	public  boolean equalLists(List<String> one, List<String> two){     
	    if (one == null && two == null){
	        return true;
	    }

	    if((one == null && two != null) 
	      || one != null && two == null
	      || one.size() != two.size()){
	        return false;
	    }

	    //to avoid messing the order of the lists we will use a copy
	    //as noted in comments by A. R. S.
	    one = new ArrayList<String>(one); 
	    two = new ArrayList<String>(two);   

	    Collections.sort(one);
	    Collections.sort(two);      
	    return one.equals(two);
	}
	
	public List<DirectedGraph<Integer, MyEdge>> getGraphList() {
		return graphList;
	}

	public void addGraphToGraphSet(DirectedGraph<Integer, MyEdge> graph){
		this.graphList.add(graph);
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
	
	public void insertPatternTableCell(int row, int col, int value) {
		this.patternTable.get(row).set(col, value);
	}

}
