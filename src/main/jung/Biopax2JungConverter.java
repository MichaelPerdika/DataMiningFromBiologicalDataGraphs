package main.jung;

import java.util.HashMap;
import java.util.Map;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

public class Biopax2JungConverter {
	private BioPAXGraphAdjList biopaxGraph;
	private DirectedGraph<Integer, MyEdge> jungGraph;

	/**
	 * never to be called
	 */
	@SuppressWarnings("unused")
	private Biopax2JungConverter(){
		
	}
	public Biopax2JungConverter(BioPAXGraphAdjList graphBioPax) {
		// TODO Auto-generated constructor stub
		this.biopaxGraph = graphBioPax;
		this.jungGraph = biopax2jung();
	}
	/**
	 * this method will convert a BioPAXGraphAdjList graph to DirectedGraph<Integer, MyEdge> (jung) graphs
	 * @param graphBioPax
	 * @return DirectedGraph<Integer, MyEdge> (which is DirectedSparseMultigraph<Integer, MyEdge>)
	 */
	private DirectedGraph<Integer, MyEdge> biopax2jung() {
        
        DirectedGraph<Integer, MyEdge> graph = 
        		new DirectedSparseMultigraph<Integer, MyEdge>();
        //MyNode startNode, endNode = null;
        Integer startNode, endNode = null;
        String edgeRDFid;
        String[] nextEdgesRDFids;
        int nodeID = 0;
        boolean thereIsStartNode;
        Map<String, Map<String, Integer>> tempMap = 
        		new HashMap<String, Map<String, Integer>>();
		for (Map.Entry<String, Map<String, String[]>> entry : 
			this.biopaxGraph.getBioPathStepsGraph().entrySet()){
			MyEdge myEdge = new MyEdge(entry);
			//TODO this needs rework because stepConversion is [BiochemicalReaction]
			// while nextStep is [BiochemicalPathwayStep]
			edgeRDFid = myEdge.getEdgeRDFid();
			// the edgeRDFid doesn't exist in tempMap then add it with null
			if (!tempMap.containsKey(edgeRDFid)){
				tempMap.put(edgeRDFid, new HashMap<String, Integer>());
			}
			// Now the edgeRDFid is surely in the tempMap. Check if has already been visualized
			if (!tempMap.get(edgeRDFid).containsKey("complete")){
				// this edge hasn't been visualized yet.
				// if it has already a starting node index
				if (tempMap.get(edgeRDFid).containsKey("startNode")){
					startNode = tempMap.get(edgeRDFid).get("startNode");
				}
				// if it hasn't a node index create new.
				else{
					
					startNode = nodeID++;
					tempMap.get(edgeRDFid).put("startNode", startNode);
				}
				nextEdgesRDFids = myEdge.getNextStepRDFids();
				// if there are nextEdges
				if (nextEdgesRDFids.length > 0){
					thereIsStartNode = false;
					// check if the endNode of the current edgeRDFid
					// is already the starting Node of one or more nextEdgeRDFid 
					for (String nextEdgeRDFid : nextEdgesRDFids){
						// make sure that if one edge RDFid is not in tempMap to add it
						if (!tempMap.containsKey(nextEdgeRDFid)){
							tempMap.put(nextEdgeRDFid, new HashMap<String, Integer>());
						}
						if (tempMap.get(nextEdgeRDFid).containsKey("startNode")){
							endNode = tempMap.get(nextEdgeRDFid).get("startNode");
							addStartNodeToNextEdges(tempMap, nextEdgesRDFids, endNode);
							thereIsStartNode = true;
							break;
						}
					}
					// if we didn't find any endNode that matches then create new 
					// and add it to the next edges as startNode
					if (!thereIsStartNode){
						endNode = nodeID++;
						addStartNodeToNextEdges(tempMap, nextEdgesRDFids, endNode);
					}
					
				}
				// if there aren't nextEdges
				else{
					endNode = nodeID++;
				}
				//finally add the edge
				myEdge.setEdgeNodes(startNode, endNode);
				graph.addEdge(myEdge, startNode, endNode);
				tempMap.get(edgeRDFid).put("complete", 1);	
			}
		}
		return graph;
	}

	/**
	 * Used in biopax2jung method. Nothing to see here.
	 * When a current edge is stored in the graph it will set its ending
	 * node as the starting node of the next edges of his.
	 * @param tempMap Is a temporary map that stores which edges are already in the graph 
	 * @param nextEdgesRDFids the next edges RDFids 
	 * @param startNode the ID of the ending node of the current Edge 
	 * (which is the starting node for the next nodes)
	 */
	private void addStartNodeToNextEdges(Map<String, Map<String, Integer>> tempMap,
			String[] nextEdgesRDFids, Integer startNode) {
		for (String nextEdgeRDFid : nextEdgesRDFids){
			if (!tempMap.containsKey(nextEdgeRDFid)){
				tempMap.put(nextEdgeRDFid, new HashMap<String, Integer>());
			}
			// now tempMap has the edge. check if it already has startNode
			if (tempMap.get(nextEdgeRDFid).containsKey("startNode")){
				//this is redundant but check it anyway
				if (tempMap.get(nextEdgeRDFid).get("startNode") != startNode){
					System.out.println("Error in addStartNodeToNextEdges");
				}
			}
			// else: it doesn't have so add it
			else{
				tempMap.get(nextEdgeRDFid).put("startNode", startNode);
			}
		}
	}
	
	public BioPAXGraphAdjList getBiopaxGraph(){
		return this.biopaxGraph;
	}

	public DirectedGraph<Integer, MyEdge> getJungGraph(){
		return this.jungGraph;
	}
}
