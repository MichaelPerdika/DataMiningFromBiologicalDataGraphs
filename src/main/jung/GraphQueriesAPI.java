package main.jung;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class GraphQueriesAPI {

	private List<DirectedGraph<Integer, MyEdge>> graphList;
	private List<DirectedGraph<Integer, MyEdge>> subGraphList;
	private List<DirectedGraph<Integer, MyEdge>> subGraphListComplementary;
	private List<List<Integer>> patternTable;
	private List<List<Integer>> patternTableComplementary;
	private List<List<Integer>> patternTableWhole;
	
	/**
	 * empty constructor initialize to null 
	 */
	public GraphQueriesAPI(){
		setGraphList(new ArrayList<DirectedGraph<Integer, MyEdge>>());
		setSubGraphList(new ArrayList<DirectedGraph<Integer, MyEdge>>());
		setSubGraphListComplementary(new ArrayList<DirectedGraph<Integer, MyEdge>>());
		setPatternTable(new ArrayList<List<Integer>>());
		setPatternTableComplementary(new ArrayList<List<Integer>>());
		setPatternTableWhole(new ArrayList<List<Integer>>());
	}
	
	/**
	 * Constructor that will create the grapSet the subGraphSet and the patternTable
	 * @param graphSet
	 */
	public GraphQueriesAPI(List<DirectedGraph<Integer, MyEdge>> graphSet) {
		//TODO
		setGraphList(graphSet);
		setSubGraphList(new ArrayList<DirectedGraph<Integer, MyEdge>>());
		setSubGraphListComplementary(new ArrayList<DirectedGraph<Integer, MyEdge>>());
		setPatternTable(new ArrayList<List<Integer>>());
		setPatternTableComplementary(new ArrayList<List<Integer>>());
		setPatternTableWhole(new ArrayList<List<Integer>>());
	}
	
	/**
	 * This is the main method of this API that finds all the 
	 * subgraphs and fills the patternTable and subGraphList
	 * @param threshold is a number between 0% and 100% and is the threshold to
	 * define if edge names (ECNumbers) are equal/similar
	 */
	public void findPatternsInGraphs(double threshold){
		//TODO add min_support sigma = {0, 1} used in pattern table
		//TODO and add tolerance for "graph matching" for example 2.2.2.2 = 2.2.1.1. with 50%
		if (this.graphList.size() < 2){
			System.out.println("ERROR!!! The size of graphSet must have at least"
					+ " two instances. You gave: "+this.graphList.size()+". Exiting");
			System.exit(0);
		}
		// check all graphs with each other.
		for (int i=0;i<graphList.size();i++){
			for (int j=i+1;j<graphList.size();j++){
				mergeSubGraphsToGlobalSubGraphList(
						findCommonSubGraphsBetweenTwoGraphs(
								graphList.get(i), graphList.get(j), threshold));
			}
		}
		fillTheCompletePatternTable(threshold);
		fillWholePatternTable();
		
		
	}

	/**
	 * this method concatinates pattern table and the complementary to a whole pattern table
	 */
	private void fillWholePatternTable() {
		setPatternTableWhole(DeepClone.deepClone(getPatternTable()));
		for (List<Integer> row : getPatternTableComplementary()){
			this.patternTableWhole.add(row);
		}
	}

	/**
	 * this method fills both the pattern table and the complementary one
	 * It basically calls the methods fillPatternTable(threshold) 
	 * and fillComplementaryPatternTable()
	 * @param threshold
	 */
	private void fillTheCompletePatternTable(double threshold) {
		
		// fill pattern table
		fillPatternTable(threshold);
		// fill complementary pattern table
		fillComplementaryPatternTable(threshold);
	}

	/**
	 * this method fills the complementary pattern table.
	 * This table has all the patterns that are unique.
	 * @param threshold 
	 */
	private void fillComplementaryPatternTable(double threshold) {
		
		// fill the subGraphListComplementary table and patternTableComplementary.
		// To do so do subGraphListComplementary = GraphList - subGraphList (more or less)
		for (int col=0; col< graphList.size();col++){
			DirectedGraph<Integer, MyEdge> newSubGraph = 
					DeepClone.deepClone(getGraphList().get(col));
			for (int row=0; row< subGraphList.size();row++){
				// if the cell has >0 value then it means that this pattern
				// is contained in the current graph so subtract it.
				if (getPatternTableCell(row, col) >= 0){ 
					// this could be >0 and not >=0. But if a graph has no 1's then
					// it won't be checked thus not filtering the empty ECNumbers
					// (that are erased in the eraseCommonEdgesBetweenTwoGraphs method)
					// TODO in the future >=0  to >0 and resolve the empty ECNumbers problem.
					DirectedGraph<Integer, MyEdge> curSubGraph = 
							DeepClone.deepClone(getSubGraphList().get(row));
					eraseCommonEdgesBetweenTwoGraphs(newSubGraph, curSubGraph, threshold);
				}
			}
			// now the newSubGraph is ready to append it.
			if (newSubGraph.getEdgeCount() > 0){
				this.subGraphListComplementary.add(newSubGraph);
				// append the new row in patternTableComplementary
				List<Integer> newRow = new ArrayList<Integer>(graphList.size()); 
				for (int index=0; index<graphList.size();index++){
					if (index == col){
						newRow.add(index, 1);
						//TODO maybe its not 1 here.
					}
					else{
						newRow.add(index, 0);
					}
				}
				this.patternTableComplementary.add(newRow);
			}
		}		
	}

	/**
	 * this method does the "destSubGraph = destSubGraph - sourceSubGraph" using threshold for the
	 * edge equality.
	 * @param destSubGraph this is mutated through the method. Is the graph that we want 
	 * to subtract the curSubGraph
	 * @param sourceSubGraph the pattern/subGraph which is the reference. We want to subtract
	 * all its matching edges to destSubGraph
	 * @param threshold
	 */
	private void eraseCommonEdgesBetweenTwoGraphs(
			DirectedGraph<Integer, MyEdge> destSubGraph,
			DirectedGraph<Integer, MyEdge> sourceSubGraph, double threshold) {
	
		// deep clone since we are going to erase things here
		for (MyEdge destEdge : DeepClone.deepClone(destSubGraph.getEdges())){
			// TODO for now ignore the edges with empty ECNumber. Resolve this in the future
			
			if (parseEdgeNames(destEdge).get(0).get(0).isEmpty()){
			//Alternative use 
			//if (destEdge.toString().equals(" ")){
				// remove the empty name edge
				destSubGraph.removeEdge(destEdge);
				// after the remove of the edge check if there are vertexes that are not
				// connected to any edge
				removeReduntantVertex(destSubGraph, destEdge);
				continue; // since it is removed no need to check further
			}
			for (MyEdge sourceEdge : sourceSubGraph.getEdges()){
				MyEdge commonEdge = getCommonEdgeFromThreshold(
								sourceEdge, destEdge, threshold);
				// we have a match therefore erase that edge from destSubGraph
				if (commonEdge != null){
					destSubGraph.removeEdge(destEdge);
					removeReduntantVertex(destSubGraph, destEdge);
					continue; // since it is removed no need to check further
				}
			}
		}
		
		
	}

	/**
	 * this method removes the vertexes of graph that are not connected to any edge.
	 * So graph is mutated (if there are "hanging" vertexes)
	 * @param graph
	 * @param edge 
	 */
	private void removeReduntantVertex(DirectedGraph<Integer, MyEdge> graph, MyEdge edge) {
		
		Integer startVertex = edge.getStartNode();
		Integer endVertex = edge.getEndNode();
		// check for the starting vertex of the edge
		if (graph.containsVertex(startVertex)){
			if (graph.getIncidentEdges(startVertex).isEmpty()){
				graph.removeVertex(startVertex);
			}
		}
		
		// check for the ending vertex of the edge
		if (graph.containsVertex(endVertex)){
			if (graph.getIncidentEdges(endVertex).isEmpty()){
				graph.removeVertex(endVertex);
			}
		}
	}

	/**
	 * Before this method call the subGraphList is full and we want to fill the pattern table
	 * with the correct numbers >=0 (eliminate all -1).
	 */
	private void fillPatternTable(double threshold) {
		// TODO Auto-generated method stub
		// we iterate through the rows (subGraphs) and columns (graphs) and check
		// the number of occurrences of every subGraph/pattern in every graph and fill the 
		// Corresponding cell on patternTable. 
		for (int row=0; row<getSubGraphList().size(); row++){	
			for (int col=0; col<getGraphList().size(); col++){
				if (getPatternTableCell(row,col) != -1){
					System.out.println("Error in fillTheCompletePatternTable cell "+row+", "+col+"is -1");
					System.exit(1);
				}
				int occurrences = getOccurrencesOfPatternInGraph(
						getSubGraphList().get(row),getGraphList().get(col), threshold);
				insertPatternTableCell(row, col, occurrences);
			}
		}
	}

	public int getOccurrencesOfPatternInGraph(
			DirectedGraph<Integer, MyEdge> subGraph,
			DirectedGraph<Integer, MyEdge> graph, double threshold) {
		// TODO Auto-generated method stub		
		// pattern must be smaller than the graph.
		if (subGraph.getEdgeCount()>graph.getEdgeCount() 
				|| subGraph.getVertexCount() > graph.getVertexCount())
			return 0;
		List<DirectedGraph<Integer, MyEdge>> commonGraphList = 
				new ArrayList<DirectedGraph<Integer, MyEdge>>();
		Collection<MyEdge> subEdges = subGraph.getEdges();
		Collection<MyEdge> graphEdges = graph.getEdges();
		for (MyEdge sEdge : subEdges){
			List<DirectedGraph<Integer, MyEdge>> tempGraphList = 
					new ArrayList<DirectedGraph<Integer, MyEdge>>();
			for (MyEdge gEdge : graphEdges){
				//maybe i have to pass the threshold as well. For now it is irrelevant
				MyEdge comEdge = getCommonEdgeFromThreshold(sEdge, gEdge, threshold);
				// if there is no comEdge (null) or it doesn't match the patEdge return
				if (comEdge== null || !comEdge.isIdentical(sEdge)) continue;//do nothing
				// there is a match. Create or append in graph list.
				List<DirectedGraph<Integer, MyEdge>> TBE = 
						getGraphListWithNewEdge(new ArrayList<DirectedGraph<Integer, MyEdge>>(commonGraphList), comEdge, gEdge);
				concat2GraphLists(tempGraphList, TBE);
			}
			commonGraphList = tempGraphList;
		}
		if (commonGraphList.isEmpty()) return 0;
		int occurences = 0;
		for (DirectedGraph<Integer, MyEdge> comGraph : commonGraphList){
			if (graphEquality(comGraph, subGraph)) occurences++;
		}
		return occurences;
	}

	/**
	 * this method is used in getOccurrencesOfPatternInGraph. this method joins two list
	 * of graphLists. It appends the sourceList to the existing distList. distList is 
	 * mutated with this function
	 * @param distList
	 * @param sourceList
	 */
	private void concat2GraphLists(List<DirectedGraph<Integer, MyEdge>> distList,
			List<DirectedGraph<Integer, MyEdge>> sourceList) {
		// TODO Auto-generated method stub
		for (DirectedGraph<Integer, MyEdge> graph : sourceList){
			distList.add(graph);
		}
	}

	/**
	 * this method is used in getOccurrencesOfPatternInGraph. It inserts the given edge to 
	 * every graph of the prevGraphList and return this new list of graphs.
	 * @param prevGraphList
	 * @param edge
	 * @param gEdge 
	 */
	private List<DirectedGraph<Integer, MyEdge>> getGraphListWithNewEdge(
			List<DirectedGraph<Integer, MyEdge>> prevGraphList, MyEdge edge, MyEdge gEdge) {
		// TODO Auto-generated method stub
		List<DirectedGraph<Integer, MyEdge>> nextGraphList = 
				new ArrayList<DirectedGraph<Integer, MyEdge>>();
		if (prevGraphList.isEmpty()) {
			DirectedGraph<Integer, MyEdge> tempGraph = 
					new DirectedSparseMultigraph<Integer, MyEdge>();
			//tempGraph.addEdge(edge, gEdge.getStartNode(), gEdge.getEndNode());
			tempGraph.addEdge(new MyEdge(edge, gEdge.getStartNode(), gEdge.getEndNode()),
					gEdge.getStartNode(), gEdge.getEndNode());
			nextGraphList.add(tempGraph);
		}
		else{
			for (DirectedGraph<Integer, MyEdge> prevGraph : prevGraphList){
				DirectedGraph<Integer, MyEdge> tempGraph =
						DeepClone.deepClone(prevGraph);
				tempGraph.addEdge(new MyEdge(edge, gEdge.getStartNode(), gEdge.getEndNode()),
						gEdge.getStartNode(), gEdge.getEndNode());
				nextGraphList.add(tempGraph);
			}
		}	
		return nextGraphList;
	}

	/**
	 * This method returns a List of common subGraphs between graph1, graph2
	 * @param graph1 the first graph to be checked
	 * @param graph2 the second graph to be checked
	 * @param threshold the threshold for the equality/similarity of edge names
	 * @return the common subGraphs as a list of DirectedGraphs.
	 */
	public List<DirectedGraph<Integer, MyEdge>> findCommonSubGraphsBetweenTwoGraphs(
			DirectedGraph<Integer, MyEdge> graph1, 
			DirectedGraph<Integer, MyEdge> graph2, double threshold){
		//TODO this method is useless for now. Either erase it or utilize it
		return findCommonEdges(graph1, graph2, threshold);
	}
	
	/**
	 * insert description
	 * @param graph1
	 * @param graph2
	 * @param threshold 
	 */
	 public List<DirectedGraph<Integer, MyEdge>> findCommonEdges(
			DirectedGraph<Integer, MyEdge> graph1, 
			DirectedGraph<Integer, MyEdge> graph2, double threshold) {

		List<DirectedGraph<Integer, MyEdge>> commonSubGraphList = 
				new ArrayList<DirectedGraph<Integer, MyEdge>>();
		Collection<MyEdge> colEdges1 = graph1.getEdges();
		Collection<MyEdge> colEdges2 = graph2.getEdges();
		for (MyEdge edge1 : colEdges1){
			// this is used to avoid calculating again the same subGraph
			if (!checkIfEdgeAlreadyFoundInCommonSubGraphList(commonSubGraphList, edge1)){
				for (MyEdge edge2 : colEdges2){
					MyEdge comEdge = getCommonEdgeFromThreshold(edge1, edge2, threshold);
					if(comEdge != null){
						DirectedGraph<Integer, MyEdge> commonSubGraph = 
								new DirectedSparseMultigraph<Integer, MyEdge>();
						appendNextEdgesToCommonSubGraph(commonSubGraph, 
								graph1, graph2, edge1, edge2, comEdge, threshold);
						appendPreviousEdgesToCommonSubGraph(commonSubGraph, 
								graph1, graph2, edge1, edge2, comEdge, threshold);
						//this might need rework
						commonSubGraphList.add(commonSubGraph);
					}
				}
			}		
		}
		return commonSubGraphList;
	}
	 
	 /**
	  * this method returns the common MyEdge from edge1, edge2 that "passes" the 
	  * threshold.
	  * @param edge1
	  * @param edge2
	  * @param threshold
	  * @return MyEdge if there is a common edge or null if there is none 
	  * (instead of true or false)
	  */
	 public static MyEdge getCommonEdgeFromThreshold(MyEdge edge1, MyEdge edge2, double threshold) {
		// TODO Auto-generated method stub
		MyEdge comEdge;
		List<List<String>> e1 = parseEdgeNames(edge1);
		List<List<String>> e2 = parseEdgeNames(edge2);
		if ((e1 == null && e2 == null) || 
				(e1.size()==0 && e2.size()==0) ){
			comEdge = new MyEdge(new String[] {edge1.toString()}, 
					edge1.getStartNode(), edge1.getEndNode());
	        return comEdge;
			
	    }

		if((e1 == null && e2 != null) 
			      || (e1 != null && e2 == null)
			      || (e1.size()==0 && e2.size() !=0)
			      || (e1.size()!=0 && e2.size() ==0)){
			        return null;
		}
		
		double maxScore = -1, avgScore = 0;
		String[] comName = new String[e1.size()];
		for (int i=0;i<e1.size();i++){
			maxScore = -1;
			String tempComName = "";
			for (int j=0;j<e2.size();j++){
				/*******this is the case of "" == ""
				 * it is unequal for now but in the future fix this.
				 */
				if (e1.get(i).get(0).isEmpty() && e2.get(j).get(0).isEmpty()) 
					return null;
				/******/
				double tempScore = getParsedEdgeScore(e1.get(i), e2.get(j));
				String tempName = getParsedEdgeName(e1.get(i), e2.get(j));
				if (maxScore < tempScore){
					maxScore = tempScore;
					tempComName = tempName;
				}
			}
			avgScore += maxScore;
			comName[i] = tempComName;
		}
		if (e1.size()==0 && e2.size() !=0)
			return null;
		else
			avgScore = avgScore/e1.size();
		// the bellow if returns >= threshold
		/*
		if (avgScore >= threshold){ 
			comEdge = new MyEdge(comName, edge1.getStartNode(), edge1.getEndNode());
	        return comEdge;
		}
		*/
		// the bellow if returns == threshold
		if (avgScore == threshold){ 
			comEdge = new MyEdge(comName, edge1.getStartNode(), edge1.getEndNode());
	        return comEdge;
		}
		else
			return null;
	}
	 
	 /**
	  * returns the common subString in the form 2.2.1.* or "" if empty
	  * but match or null if no match at all
	  * @param subStr1
	  * @param subStr2
	  * @return
	  */
	 public static String getParsedEdgeName(List<String> subStr1, List<String> subStr2) {
		// TODO Auto-generated method stub
		int length1 = subStr1.size(), length2 = subStr2.size();
		int maxLength = length1, minLength = length1;
		if (maxLength < length2) maxLength = length2;
		if (minLength > length2) minLength = length2;
		// they are equal to empty lists
		if (length1 == 0 && length2 == 0) return "";
		// one is empty the other not then they have nothing in common
		if ((length1 == 0 && length2!=0) || (length1 !=0 && length2==0)) return null;
		String parsedName ="";
		for (int i=0; i<minLength;i++){
			if (subStr1.get(i).equals(subStr2.get(i))){
				if (i==0){
					parsedName = subStr1.get(i);
				}
				else{
					//there might be problem with dot because is used in regular expression
					parsedName += "."+subStr1.get(i);
				}
			}
			else{
				if (parsedName.length()>0) parsedName += ".-";
				// else do nothing don't return ".*" just return ""
				break;
			}
		}
		return parsedName;
	}

	/**
	 * This method may have to be deleted. Replaced from getMyEdgeFromThreshold
	 * gets two parsed lists of eCnumbers and returns the similarity score
	 * @param subStr1
	 * @param subStr2
	 * @return
	 */
	public static double getParsedEdgeScore(List<String> subStr1, List<String> subStr2) {
		// TODO Auto-generated method stub
		int length1 = subStr1.size(), length2 = subStr2.size();
		int maxLength = length1, minLength = length1;
		if (maxLength < length2) maxLength = length2;
		if (minLength > length2) minLength = length2;
		// they are equal to empty lists
		if (length1 == 0 && length2 == 0) return 1;
		// one is empty the other not then they have nothing in common
		if ((length1 == 0 && length2!=0) || (length1 !=0 && length2==0)) return 0;
		double numOfMatches =0;
		for (int i=0; i<minLength;i++){
			if (subStr1.get(i).equals(subStr2.get(i))) numOfMatches++;
			else break;
		}
		return numOfMatches/maxLength;
	}

	/**
	 * checks if an edge with the same starting and ending vertices 
	 * is contained in the list of SubGraphs (not necessary the same edge name they
	 * can have a similar name).
	 * This is used to avoid calculating again the same subGraph in the commonSubGraphList
	 * @param commonSubGraphList
	 * @param edge
	 * @return true if the edge is found in at least one subGraph from the list
	 */
	private boolean checkIfEdgeAlreadyFoundInCommonSubGraphList(
			List<DirectedGraph<Integer, MyEdge>> commonSubGraphList, MyEdge edge) {
		// TODO Auto-generated method stub
		
		if (commonSubGraphList.isEmpty()) return false;

		for (DirectedGraph<Integer, MyEdge> curSubGraph : commonSubGraphList){
			for (MyEdge curEdge : curSubGraph.getEdges()){
				if(edge.getStartNode() == curEdge.getStartNode()
						&& edge.getEndNode() == curEdge.getEndNode()){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * This method tries to replace graph.containsEdge(MyEdge myEdge) because i Have found a bug.
	 * if edge "A"[0,1] already in graph if I do graph.containsEdge("A"[0,1]) it might return
	 * false (even though it exists). I think it is because it checks hashcode.
	 * @param graph
	 * @param edge
	 * @return true if graph "really" contains edge.
	 */
	public static boolean graphContainsEdge(DirectedGraph<Integer, MyEdge> graph, 
			MyEdge edge) {
		// TODO Auto-generated method stub
		if (graph == null || edge == null) return false;
		Collection<MyEdge> graphEdges = graph.getEdges();
		for (MyEdge curEdge : graphEdges){
			/*
			if(edge.equals(curEdge) && edge.getStartNode()==curEdge.getStartNode()
					&& edge.getEndNode() == curEdge.getEndNode()){
				return true;
			}
			*/
			if(edge.isIdentical(curEdge)) return true;
		}
		// if none found then return false
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
	 * @param comEdge 
	 * @param threshold 
	 */
	private static void appendNextEdgesToCommonSubGraph(DirectedGraph<Integer, MyEdge> commonSubGraph,
			DirectedGraph<Integer, MyEdge> graph1, DirectedGraph<Integer, MyEdge> graph2, 
			MyEdge edge1, MyEdge edge2, MyEdge comEdge, double threshold) {
		// Insert the common edge. It doesn't matter which vertices 
		// we add edge1 or edge2. Add edge1 vertices as convention
		if (!graphContainsEdge(commonSubGraph, comEdge)){
			commonSubGraph.addEdge(comEdge, edge1.getStartNode(), edge1.getEndNode());
			
			Collection<MyEdge> nextEdges1 = graph1.getOutEdges(edge1.getEndNode());
			Collection<MyEdge> nextEdges2 = graph2.getOutEdges(edge2.getEndNode());
			for (MyEdge next1 : nextEdges1){
				for (MyEdge next2: nextEdges2){
					MyEdge nextComEdge = getCommonEdgeFromThreshold(next1, next2, threshold);
					if(nextComEdge != null){
						appendNextEdgesToCommonSubGraph(commonSubGraph,
								graph1,graph2,next1,next2, nextComEdge,threshold);
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
	 * @param threshold 
	 * @param comEdge 
	 */
	private static void appendPreviousEdgesToCommonSubGraph(DirectedGraph<Integer, MyEdge> commonSubGraph,
			DirectedGraph<Integer, MyEdge> graph1, DirectedGraph<Integer, MyEdge> graph2, 
			MyEdge edge1, MyEdge edge2, MyEdge comEdge, double threshold) {
		// TODO Auto-generated method stub
		//this has to be here because when going backward to see previous edges these edges may
		//have more than 1 next edges. So need to search these branches first before going backwards
		//again. If the next edges are already in the commonSubGraph then "appendNextEdgesToCommonSubGraph"
		// will do nothing
		appendNextEdgesToCommonSubGraph(commonSubGraph, 
				graph1, graph2, edge1, edge2, comEdge, threshold);
		
		// Here we append only the previous ones.
		Collection<MyEdge> previousEdges1 = graph1.getInEdges(edge1.getStartNode());
		Collection<MyEdge> previousEdges2 = graph2.getInEdges(edge2.getStartNode());
		for (MyEdge previous1 : previousEdges1){
			for (MyEdge previous2: previousEdges2){
				//TODO the bellow line
				MyEdge prevComEdge = getCommonEdgeFromThreshold(previous1, previous2, threshold);
				if (!graphContainsEdge(commonSubGraph, prevComEdge)){
					if(prevComEdge !=null){
						appendPreviousEdgesToCommonSubGraph(commonSubGraph, graph1, graph2, 
								previous1, previous2, prevComEdge, threshold);
					}
				}
			}
		}
	}
	

	/**
	 * this method merges a list of subGraphs to the global subGraphList. 
	 * And updates the patternTable
	 * @param tempSubGraphs is the comonSubGraphs between the comparison of graph1ID and graph2ID
	 */
	public void mergeSubGraphsToGlobalSubGraphList(
			List<DirectedGraph<Integer, MyEdge>> tempSubGraphs) {
		
		for(DirectedGraph<Integer, MyEdge> prev : tempSubGraphs){

			int subGraphIndex = isSubGraphInPatternTable(prev);
			if (subGraphIndex < 0){
				this.subGraphList.add(prev);
				appendNewRowInPatternTable();
			}

		}
		
	}
	
	/**
	 * this method adds a new row in the patternTable with -1 which is undefined for now
	 */
	private void appendNewRowInPatternTable() {
		// TODO Auto-generated method stub
		List<Integer> newRow = new ArrayList<Integer>(graphList.size()); 
		for (int index=0; index<graphList.size();index++){
			newRow.add(index, -1);
		}
		this.patternTable.add(newRow);
		
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
			for(DirectedGraph<Integer, MyEdge> curSubGraph : getSubGraphList()){
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
	 * this method prints the pattern table as the complementary one.
	 * It calls the methods printGraphs(), printSubGraphs(), 
	 * printPatternTable() and printComplementaryPatternTable()
	 */
	public void printApplicationOutput(){
		printGraphs();
		printSubGraphs();
		printPatternTable();
		printComplementarySubGraphs();
		printComplementaryPatternTable();
		printWholePatternTable();
	}

	/**
	 * this method prints the graphList
	 */
	public void printGraphs() {
		System.out.println("\nThe Graphs");
		/**** print the graphs ***/
		for (int i=0; i<graphList.size();i++){
			System.out.println("g"+i+" = "+graphList.get(i));
		}
	}
	
	/**
	 * this method prints the subGraphList
	 */
	public void printSubGraphs() {
		System.out.println("");
		/**** print the patterns */
		System.out.println("The Patterns");
		for (int i=0; i<subGraphList.size();i++){
			System.out.println("p"+i+" = "+subGraphList.get(i));
		}
	}

	/**
	 * prints the pattern table
	 */
	public void printPatternTable(){
	
		System.out.println("");
		/**** print the grid******/
		System.out.println("The Pattern Table");
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
	 * this method prints the subGraphListComplementary
	 */
	public void printComplementarySubGraphs() {
		System.out.println("");
		/**** print the complementary patterns */
		System.out.println("The Complementary Patterns");
		for (int i=0; i<subGraphListComplementary.size();i++){
			System.out.println("s"+i+" = "+subGraphListComplementary.get(i));
		}
	}

	/**
	 * prints the complementary pattern table
	 */
	public void printComplementaryPatternTable(){
		
		System.out.println("");
		/**** print the grid******/
		System.out.println("The complementary Pattern Table");
		System.out.print("   ");
		for (int num=0; num<graphList.size();num++){
			System.out.print("g"+num+" ");
		}
		System.out.println("");
		for (int i=0;i<patternTableComplementary.size();i++){
			System.out.print("s"+i+" ");
			for (int j=0;j<patternTableComplementary.get(i).size();j++){
				System.out.print(patternTableComplementary.get(i).get(j)+"  ");
			}
			System.out.println("");
		}
	}
	
	/**
	 * prints the complementary pattern table
	 */
	public void printWholePatternTable(){
		
		System.out.println("");
		/**** print the grid******/
		System.out.println("The Whole Pattern Table");
		System.out.print("   ");
		for (int num=0; num<graphList.size();num++){
			System.out.print("g"+num+" ");
		}
		System.out.println("");
		for (int i=0;i<patternTableWhole.size();i++){
			System.out.print("p"+i+" ");
			for (int j=0;j<patternTableWhole.get(i).size();j++){
				System.out.print(patternTableWhole.get(i).get(j)+"  ");
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
			
			Map<Integer, Map<String, Map<Integer, List<String>>>> canLabAdjList1 = 
					getCanonicalLabelAdjList(graph1);
			Map<Integer, Map<String, Map<Integer, List<String>>>> canLabAdjList2 = 
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
	public Map<Integer, Map<String, Map<Integer, List<String>>>> getCanonicalLabelAdjList(
			DirectedGraph<Integer, MyEdge> graph){

		Map<Integer, Map<String, Map<Integer, List<String>>>> cLAdjList = 
				new HashMap<Integer, Map<String, Map<Integer, List<String>>>>();
		for (Integer vertex : graph.getVertices()){
			
			Map<String, Map<Integer, List<String>>> tempSubMap = 
					new HashMap<String, Map<Integer, List<String>>>();
			/*for 'next' edges*/
			Map<Integer, List<String>> tempNextMap = new HashMap<Integer, List<String>>();
			for (MyEdge outEdge : graph.getOutEdges(vertex)){
				Integer endNode = outEdge.getEndNode();
				String edgeName = outEdge.toString();
				if (tempNextMap.containsKey(endNode)){
					tempNextMap.get(endNode).add(edgeName);
				}
				else{
					ArrayList<String> tempArray = new ArrayList<String>();
					tempArray.add(edgeName);
					tempNextMap.put(endNode, tempArray);
				}
			}
			/*for 'previous' edges*/
			Map<Integer, List<String>> tempPreviousMap = new HashMap<Integer, List<String>>();
			for (MyEdge inEdge : graph.getInEdges(vertex)){
				Integer startNode = inEdge.getStartNode();
				String edgeName = inEdge.toString();
				if (tempPreviousMap.containsKey(startNode)){
					tempPreviousMap.get(startNode).add(edgeName);
				}
				else{
					ArrayList<String> tempArray = new ArrayList<String>();
					tempArray.add(edgeName);
					tempPreviousMap.put(startNode, tempArray);
				}
			}
			//add next Map
			tempSubMap.put("next", tempNextMap);
			//add previous Map
			tempSubMap.put("previous", tempPreviousMap);
			cLAdjList.put(vertex, tempSubMap);
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
	public boolean canonicalLabelEquality(
			Map<Integer, Map<String, Map<Integer, List<String>>>> canLabAdjList1,
			Map<Integer, Map<String, Map<Integer, List<String>>>> canLabAdjList2) {
		
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
		Map<Integer, Map<String, Map<Integer, List<String>>>> adjList1 = 
				DeepClone.deepClone(canLabAdjList1);
		Map<Integer, Map<String, Map<Integer, List<String>>>> adjList2 = 
				DeepClone.deepClone(canLabAdjList2);
		// if they haven't the same size or are empty return false.
		if (adjList1.size() != adjList2.size() || adjList2.isEmpty() || adjList1.isEmpty()){
			return false;
		}
		// as equality we denote two CL that have the same "form of vertices"
		// that have the same edge names.
		while (!adjList1.isEmpty() ){
			boolean nextMatchFound = false;
			boolean previousMatchFound = false;
			// get the next entry from adjList1
			Entry<Integer, Map<String, Map<Integer, List<String>>>> entry1 = 
					adjList1.entrySet().iterator().next();
			// iterate all the entries of the 2nd list to see if it match.
			for (Entry<Integer, Map<String, Map<Integer, List<String>>>> entry2 : adjList2.entrySet()){
				// check if they are empty if they come from the same edge.
				if (entry1.getValue().isEmpty() && entry2.getValue().isEmpty())
					System.out.println("Both Entries are empty");
				/** check if next Maps are equal **/
				if(canonicalLabelEntryEquality(
						entry1.getValue().get("next"), entry2.getValue().get("next"))){
					nextMatchFound = true;
				}
				/** check if previous Maps are equal **/
				if(canonicalLabelEntryEquality(
						entry1.getValue().get("previous"), entry2.getValue().get("previous"))){
					previousMatchFound = true;
				}
				if (nextMatchFound && previousMatchFound){
					adjList1.remove(entry1.getKey());
					adjList2.remove(entry2.getKey());
					break;// TODO there musn't be a break here. Check how many you find.
				}
				else{
					nextMatchFound = false;
					previousMatchFound = false;
				}
				
			}
			// if the break from the above for loop is not reached then 
			// entry1 dind't find a map from entry 2 to match
			if (!(previousMatchFound && nextMatchFound)) return false;
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
	
	/**
	 * there are still some errors when i give two empty strings
	 * @param myEdge
	 * @return
	 */
	public static List<List<String>> parseEdgeNames(MyEdge myEdge){
		String[] eCNumbers = myEdge.getECNumber();
		ArrayList<List<String>> listOfStrings = 
				new ArrayList<List<String>>();
		//iterate through the ECNumbers
		for ( int i=0 ;i<eCNumbers.length;i++){
			ArrayList<String> tempList = new ArrayList<String>();
			String[] subECNum = eCNumbers[i].split(Pattern.quote(".")); // Split on period.
			//iterate inside an ECNumber
			for (int j=0;j<subECNum.length;j++){
				tempList.add(subECNum[j]);
			}
			listOfStrings.add(tempList);
		}
		return listOfStrings;
	}
	
	/**
	 * static method that can visualize a List of Directed graphs
	 * @param graphList
	 */
	public static void visualizeListOfGraphs(List<DirectedGraph<Integer, MyEdge>> graphList) {

		for (DirectedGraph<Integer, MyEdge> temp : graphList){
			visualizeGraph(temp);
		}
	}
	
	/**
	 * method that visualizes the list of patterns subGraphList
	 * @param graphList
	 */
	public void visualizeSubGraphList() {
		visualizeListOfGraphs(getSubGraphList());
	}
	
	/**
	 * method that visualizes the list of graphs graphList
	 * @param graphList
	 */
	public void visualizeGraphList() {
		visualizeListOfGraphs(getGraphList());
	}
	
	/**
	 * method that visualizes the list of patterns subGraphList
	 * @param graphList
	 */
	public void visualizeComplementarySubGraphList() {
		visualizeListOfGraphs(getSubGraphListComplementary());
	}
	
	/**
	 * This method visualizes a graph
	 * @param graph
	 */
	public static void visualizeGraph(DirectedGraph<Integer, MyEdge> graph) {
		// The visualization. Code from JUNG
		Graph<Integer, String> covGraph = convertGraphForVisualization(graph);
		
		// Layout<V, E>, VisualizationComponent<V,E>
        Layout<Integer, String> layout = new CircleLayout(covGraph);
        layout.setSize(new Dimension(800,600));
        BasicVisualizationServer<Integer,String> vv = new BasicVisualizationServer<Integer,String>(layout);
        vv.setPreferredSize(new Dimension(850,650));       
        // Setup up a new vertex to paint transformer...
        Transformer<Integer,Paint> vertexPaint = new Transformer<Integer,Paint>() {
            public Paint transform(Integer i) {
                return Color.GREEN;
            }
        };  
        // Set up a new stroke Transformer for the edges
        float dash[] = {10.0f};
        final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
             BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        Transformer<String, Stroke> edgeStrokeTransformer = new Transformer<String, Stroke>() {
            public Stroke transform(String s) {
                return edgeStroke;
            }
        };
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);        
        
        JFrame frame = new JFrame("Simple Graph View 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true); 
		
	}
	
	/**
	 * used in method visualizeGraph. Converts a graph from <Integer, MyEdge> to
	 * Graph<Integer, String> for convenience. 
	 * @param graph
	 * @return The convenient Graph<Integer, String>
	 */
	private static Graph<Integer, String> convertGraphForVisualization(
			DirectedGraph<Integer, MyEdge> graph) {
		Graph<Integer, String> convGraph = new DirectedSparseMultigraph<Integer, String>();
		Collection<MyEdge> collection = graph.getEdges();
		String emptyEdgeNameIter = "";
		for (MyEdge myEdge : collection){
			//TODO
			//this is used for now because there might be edges with empty name "" but different start and 
			// end points. If it exists then add a "_". This has to be changed
			if (convGraph.containsEdge(myEdge.getEdgeName())){
				emptyEdgeNameIter += "_";
				convGraph.addEdge(emptyEdgeNameIter, myEdge.getStartNode(), myEdge.getEndNode());
			}
			else{
				// before it was only this line without the if/else
				convGraph.addEdge(myEdge.getEdgeName(), myEdge.getStartNode(), myEdge.getEndNode());
			}
		}
		return convGraph;
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
	
	public Integer getPatternTableCell(int row, int col) {
		return patternTable.get(row).get(col);
	}
	
	public Integer getPatternTableWholeCell(int row, int col) {
		return patternTableWhole.get(row).get(col);
	}

	public void setPatternTable(List<List<Integer>> patternTable) {
		this.patternTable = patternTable;
	}
	
	public void insertPatternTableCell(int row, int col, int value) {
		this.patternTable.get(row).set(col, value);
	}

	public List<List<Integer>> getPatternTableComplementary() {
		return patternTableComplementary;
	}

	public void setPatternTableComplementary(List<List<Integer>> patternTableComplementary) {
		this.patternTableComplementary = patternTableComplementary;
	}

	public List<DirectedGraph<Integer, MyEdge>> getSubGraphListComplementary() {
		return subGraphListComplementary;
	}

	public void setSubGraphListComplementary(List<DirectedGraph<Integer, MyEdge>> subGraphListComplementary) {
		this.subGraphListComplementary = subGraphListComplementary;
	}

	public List<List<Integer>> getPatternTableWhole() {
		return patternTableWhole;
	}

	public void setPatternTableWhole(List<List<Integer>> patternTableWhole) {
		this.patternTableWhole = patternTableWhole;
	}

}
