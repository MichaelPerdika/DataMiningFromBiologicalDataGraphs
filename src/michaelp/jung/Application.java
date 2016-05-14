package michaelp.jung;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;
import org.biopax.paxtools.model.Model;

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
			String fileName1 = "src/data/TCA cycle I (prokaryotic).biopax";
			String fileName2 = "src/data/TCA cycle, aerobic respiration.biopax";
			
			System.out.println("The TCA cycle I (prokaryotic) graph: "+fileName1);
			DirectedGraph<MyNode, MyEdge> graph1 = createGraphFromBiopaxFile(fileName1);
			
			visualizeGraph(graph1);
			//BioPAXGraphAdjList graph1 = new BioPAXGraphAdjList(fileName1);
			//graph1.printGraph();
			
			/*
			System.out.println("The TCA cycle, aerobic respiration graph: "+fileName2);
			BioPAXGraphAdjList graph2 = new BioPAXGraphAdjList(fileName2);
			graph2.printGraph();
			*/
			
			System.out.println("Goodbye");		
		}
	

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	private static DirectedGraph<MyNode, MyEdge> createGraphFromBiopaxFile(String fileName) {
		// create the graph from the .biopax file
		BioPAXGraphAdjList graphBiopax = new BioPAXGraphAdjList(fileName);
		graphBiopax.printGraph();
		// convert the above graph to the jung graph model
		//DirectedGraph<MyNode, MyEdge> graph = biopax2jung(graphBiopax);
		DirectedGraph<MyNode, MyEdge> graph = createTempGraph(graphBiopax);
		return graph;
	}

	/**
	 * 
	 * @param graph
	 * @return
	 */
	private static DirectedGraph<MyNode, MyEdge> biopax2jung(BioPAXGraphAdjList graphBiopax) {
		
		DirectedGraph<MyNode, MyEdge> graph = new DirectedSparseMultigraph<MyNode, MyEdge>();
		
		for (Map.Entry<String, Map<String, String[]>> entry : graphBiopax.getBioPathStepsGraph().entrySet()){
		    MyEdge myEdge = new MyEdge(entry);
		    graph.addEdge(myEdge, new MyNode(), new MyNode());
		}
		return graph;
	}

	private static DirectedGraph<MyNode, MyEdge> createTempGraph(BioPAXGraphAdjList graphBioPax) {
        
        DirectedGraph<MyNode, MyEdge> graph = new DirectedSparseMultigraph<MyNode, MyEdge>();
        MyNode startNode, endNode = null;
        String edgeRDFid;
        String[] nextEdgesRDFids;
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
					startNode = new MyNode(tempMap.get(edgeRDFid).get("startNode"));
				}
				// if it hasn't a node index create new.
				else{
					startNode = new MyNode();
					tempMap.get(edgeRDFid).put("startNode", startNode.getNodeId());
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
							endNode = new MyNode(tempMap.get(nextEdgeRDFid).get("startNode"));
							addStartNodeToNextEdges(tempMap, nextEdgesRDFids, endNode.getNodeId());
							thereIsStartNode = true;
							break;
						}
					}
					// if we didn't find any endNode that matches then create new 
					// and add it to the next edges as startNode
					if (!thereIsStartNode){
						endNode = new MyNode();
						addStartNodeToNextEdges(tempMap, nextEdgesRDFids, endNode.getNodeId());
					}
					
				}
				// if there aren't nextEdges
				else{
					endNode = new MyNode();
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

	private static void addStartNodeToNextEdges(Map<String, Map<String, Integer>> tempMap, String[] nextEdgesRDFids, Integer startNode) {
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

	private static void visualizeGraph(DirectedGraph<MyNode, MyEdge> graph) {
		// TODO Auto-generated method stub
		// The visualization. Code from JUNG
		Graph<Integer, String> sgv = convertGraphForVisualization(graph);
		
		// Layout<V, E>, VisualizationComponent<V,E>
        Layout<Integer, String> layout = new CircleLayout(sgv);
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


	private static Graph<Integer, String> convertGraphForVisualization(DirectedGraph<MyNode, MyEdge> graph) {
		Graph<Integer, String> sgv = new DirectedSparseMultigraph<Integer, String>();
		Collection<MyEdge> collection = graph.getEdges();
		for (MyEdge myEdge : collection){
			sgv.addEdge(myEdge.getEdgeName(), myEdge.getStartNode().getNodeId(), myEdge.getEndNode().getNodeId());
		}
		return sgv;
	}

}

//[BiochemicalReaction] == Edge
//[SmallMolecule] == Node with standardName == the correct name.
//[BiochemicalPathwayStep] with stepDirection == LEFT_TO_RIGHT or RIGHT_TO_LEFT.
