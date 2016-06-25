package main.jung;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class Application {

	public static void main(String[] args)  {
			
			System.out.println("hello World!");
			//http://www.biocyc.org/ECOLI/NEW-IMAGE?type=PATHWAY&object=TCA&detail-level=2
			String fileName1 = "src/data/TCA cycle I (prokaryotic).biopax";
			//http://www.biocyc.org/YEAST/NEW-IMAGE?type=PATHWAY&object=TCA-EUK-PWY-YEAST&detail-level=2
			String fileName2 = "src/data/TCA cycle, aerobic respiration.biopax";
			//http://www.biocyc.org/ARA/NEW-IMAGE?type=PATHWAY&object=PWY-5690&detail-level=2
			String fileName3 = "src/data/TCA cycle II (plants and fungi).biopax";
			//http://www.biocyc.org/LEISH/NEW-IMAGE?type=PATHWAY&object=TCA&detail-level=2
			String fileName4 = "src/data/TCA cycle.biopax";
			
			System.out.println("The TCA cycle I (prokaryotic) graph: "+fileName1);
			DirectedGraph<Integer, MyEdge> graph1 = createGraphFromBiopaxFile(fileName1);
			//visualizeGraph(graph1);
			
			System.out.println("The TCA cycle, aerobic respiration graph: "+fileName2);
			DirectedGraph<Integer, MyEdge> graph2 = createGraphFromBiopaxFile(fileName2);
			//visualizeGraph(graph2);
			//findMetrics(graph1, graph2);
			
			System.out.println("The TCA cycle II (plants and fungi) graph: "+fileName3);
			DirectedGraph<Integer, MyEdge> graph3 = createGraphFromBiopaxFile(fileName3);
			//visualizeGraph(graph3);
			
			System.out.println("The TCA cycle graph: "+fileName4);
			DirectedGraph<Integer, MyEdge> graph4 = createGraphFromBiopaxFile(fileName4);
			//visualizeGraph(graph4);
			
			
			//initialize the graphQueries API
			List<DirectedGraph<Integer, MyEdge>> graphSet = 
					new ArrayList<DirectedGraph<Integer, MyEdge>>();
			graphSet.add(graph1);
			graphSet.add(graph2);
			graphSet.add(graph3);
			graphSet.add(graph4);
			// initialize the API
			GraphQueriesAPI graphQueries = new GraphQueriesAPI(graphSet);
			// find patterns in graphs. The main algorithm.
			graphQueries.findPatternsInGraphs(0.75);
			// print the pattern table
			graphQueries.printPatternTable();
			// calculate the hierarchical clustering
			//TODO
			
			visualizeListOfSubGraphs(graphQueries.getSubGraphList());
			/**
			 * expected results:
	g0 = Vertices:0,1,2,3,4,5,6,7,8
	Edges:1.3.5.1 [3,6]  [1,7] 1.1.5.4 [4,5] 4.2.1.2 [6,4]  [0,1] 1.1.1.37 [4,5] 2.3.3.1 2.3.3.16 [5,0] 1.1.1.42 [7,8]  [8,2] 6.2.1.5 [2,3] 
	g1 = Vertices:0,1,2,3,4,5,6,7,8
	Edges: [6,7]  [7,3] 1.1.1.41 [3,2] 1.1.1.37 [1,5] 6.2.1.5 [3,4] 1.3.5.1 [4,0] 4.2.1.2 [0,1] 6.4.1.1 [8,5]  [2,3] 2.3.3.1 2.3.3.16 [5,6] 
	
	tolerance 100%:
	p0 = Vertices:0,2,3,4,5,6
	Edges:1.1.1.37 [4,5] 1.3.5.1 [3,6] 2.3.3.1 2.3.3.16 [5,0] 4.2.1.2 [6,4] 6.2.1.5 [2,3] 
	
	   g0 g1 
	p0 1  1 
	
	tolerance 75%:
	p0 = Vertices:0,2,3,4,5,6
	Edges:1.1.1.37 [4,5] 1.3.5.1 [3,6] 2.3.3.1 2.3.3.16 [5,0] 4.2.1.2 [6,4] 6.2.1.5 [2,3] 
	p1 = Vertices:7,8
	Edges:1.1.1.* [7,8] 
	
	   g0 g1 
	p0 1  1  
	p1 2  2
	 
	tolerance 50%:
	p0 = Vertices:0,2,3,4,5,6
	Edges:1.1.* [4,5] 6.2.1.5 [2,3] 1.3.5.1 [3,6] 2.3.3.1 2.3.3.16 [5,0] 4.2.1.2 [6,4] 1.1.1.37 [4,5] 
	p1 = Vertices:7,8
	Edges:1.1.1.* [7,8] 
	
	   g0 g1 
	p0 2  1  
	p1 2  2
	
	tolerance 25%:
	p0 = Vertices:3,6
	Edges:1.* [3,6] 
	p1 = Vertices:0,2,3,4,5,6
	Edges:6.2.1.5 [2,3] 1.3.5.1 [3,6] 2.3.3.1 2.3.3.16 [5,0] 4.2.1.2 [6,4] 1.1.1.37 [4,5] 1.1.* [4,5] 
	p2 = Vertices:7,8
	Edges:1.1.1.* [7,8] 
	
	   g0 g1 
	p0 4  3  
	p1 2  1  
	p2 2  2
	*/
			
			
			
			System.out.println("Goodbye");		
		}
	

	public static void printListOfSubGraphs(List<DirectedGraph<Integer, MyEdge>> graphList) {
		for (Object temp : graphList.toArray()){
			DirectedGraph<Integer, MyEdge> graph = 
					(DirectedGraph<Integer, MyEdge>) temp;
			System.out.println(graph.toString());
		}
		
	}


	public static void visualizeListOfSubGraphs(List<DirectedGraph<Integer, MyEdge>> graphList) {
		for (Object temp : graphList.toArray()){
			visualizeGraph((DirectedGraph<Integer, MyEdge>) temp);
		}
	}



	/**
	 * this is a primitive method that finds some metrics between graphs.
	 * @param graph1
	 * @param graph2
	 */
	private static void findMetrics(DirectedGraph<Integer, MyEdge> graph1, 
			DirectedGraph<Integer, MyEdge> graph2) {
		//TODO
		System.out.println("graph1 info:");
		printGraphInfo(graph1);
		System.out.println("graph2 info:");
		printGraphInfo(graph2);
		
		findCommonECNumbers(graph1, graph2);
		findCommonEdgesNames(graph1, graph2);
		findCommonEdges(graph1, graph2);
		

	}

	private static void findCommonECNumbers(DirectedGraph<Integer, MyEdge> graph1,
			DirectedGraph<Integer, MyEdge> graph2) {
		/******findCommonECNumbers*******/
		System.out.println("Common eCNumbers between graphs:");
		Collection<MyEdge> colEdges1 = graph1.getEdges();
		Collection<MyEdge> colEdges2 = graph2.getEdges();
		String[] ECN1, ECN2;
		for (MyEdge edge1 : colEdges1){
			ECN1 = edge1.getECNumber();
			for (MyEdge edge2 : colEdges2){
				ECN2 = edge2.getECNumber();
				for (String s1 : ECN1){
					for (String s2 : ECN2){
						if (s1.equals(s2) && s1.length()>0){
							System.out.println(s1);
						}
					}
				}
			}
		}
		/********************************/
		
	}

	private static void findCommonEdgesNames(DirectedGraph<Integer, MyEdge> graph1,
			DirectedGraph<Integer, MyEdge> graph2) {
		/******findCommonEdgeNames*******/
		System.out.println("Common edgeNames between graphs:");
		Collection<MyEdge> colEdges1 = graph1.getEdges();
		Collection<MyEdge> colEdges2 = graph2.getEdges();
		String edgeName1, edgeName2;
		for (MyEdge edge1 : colEdges1){
			edgeName1 = edge1.getEdgeName();
			for (MyEdge edge2 : colEdges2){
				edgeName2 = edge2.getEdgeName();
				if (edgeName1.equals(edgeName2) && edgeName1.length()>0){
					System.out.println(edgeName1);
				}
			}
		}
		/********************************/
		
	}
	
	private static void findCommonEdges(DirectedGraph<Integer, MyEdge> graph1, 
			DirectedGraph<Integer, MyEdge> graph2) {
		/******findCommonEdges***********/
		System.out.println("Common edges between graphs:");
		Collection<MyEdge> colEdges1 = graph1.getEdges();
		Collection<MyEdge> colEdges2 = graph2.getEdges();
		DirectedGraph<Integer, MyEdge> commonSubGraph = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		for (MyEdge edge1 : colEdges1){
			for (MyEdge edge2 : colEdges2){
				if (edge1.equals(edge2)){
					commonSubGraph.addEdge(edge1, edge1.getStartNode(),edge1.getEndNode());
				}
			}
		}
		visualizeGraph(commonSubGraph);
		/********************************/
	}
	
	/**
	 * dummy method that prints info of the graph
	 */
	private static void printGraphInfo(DirectedGraph<Integer, MyEdge> graph){
		System.out.println(graph.toString());
		System.out.println("Number of Vertices :"+graph.getVertexCount());
		System.out.println("Number of Edges :"+graph.getEdgeCount());
	}


	/**
	 * This method returns a DirectedGraph (jung) given the .biopax fileName
	 * This method will create the BioPAXGraphAdjList graph from the .biopax file 
	 * and then will convert it to DirectedSparseMultigraph<Integer, MyEdge> 
	 * @param fileName
	 * @return DirectedGraph<Integer, MyEdge> (which is DirectedSparseMultigraph<Integer, MyEdge>)
	 */
	private static DirectedGraph<Integer, MyEdge> createGraphFromBiopaxFile(String fileName) {
		// create the graph from the .biopax file
		BioPAXGraphAdjList graphBiopax = new BioPAXGraphAdjList(fileName);
		// convert the above graph to the jung graph model
		DirectedGraph<Integer, MyEdge> graph = biopax2jung(graphBiopax);
		return graph;
	}


	/**
	 * this function will convert a BioPAXGraphAdjList graph to DirectedGraph<Integer, MyEdge> (jung) graphs
	 * @param graphBioPax
	 * @return DirectedGraph<Integer, MyEdge> (which is DirectedSparseMultigraph<Integer, MyEdge>)
	 */
	private static DirectedGraph<Integer, MyEdge> biopax2jung(BioPAXGraphAdjList graphBioPax) {
        
        DirectedGraph<Integer, MyEdge> graph = new DirectedSparseMultigraph<Integer, MyEdge>();
        //MyNode startNode, endNode = null;
        Integer startNode, endNode = null;
        String edgeRDFid;
        String[] nextEdgesRDFids;
        int nodeID = 0;
        boolean thereIsStartNode;
        Map<String, Map<String, Integer>> tempMap = new HashMap<String, Map<String, Integer>>();
		for (Map.Entry<String, Map<String, String[]>> entry : 
			graphBioPax.getBioPathStepsGraph().entrySet()){
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
				//System.out.println(g.toString());
				//System.out.println(edgeRDFid);
				tempMap.get(edgeRDFid).put("complete", 1);	
			}
		}
		//printTempMap(tempMap);
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
	private static void addStartNodeToNextEdges(Map<String, 
			Map<String, Integer>> tempMap, String[] nextEdgesRDFids, Integer startNode) {
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
	private static Graph<Integer, String> convertGraphForVisualization(DirectedGraph<Integer, MyEdge> graph) {
		Graph<Integer, String> convGraph = new DirectedSparseMultigraph<Integer, String>();
		Collection<MyEdge> collection = graph.getEdges();
		String emptyEdgeNameIter = "";
		for (MyEdge myEdge : collection){
			//TODO
			//this is used for now because there might be edges with empty name "" but different start and 
			// end points. If it exists then add a "_". This has to be changed
			if (convGraph.containsEdge("")){
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

}

//[BiochemicalReaction] == Edge
//[SmallMolecule] == Node with standardName == the correct name.
//[BiochemicalPathwayStep] with stepDirection == LEFT_TO_RIGHT or RIGHT_TO_LEFT.
