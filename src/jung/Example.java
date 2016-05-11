package jung;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import michaelp.BioPAXGraphAdjList;
import michaelp.BiopaxAPI;

public class Example {

	public static void main(String[] args)  {
		//creatingGraphAndAddingVerticesAndEdges();
		//constructingADirectedGraphWithCustomEdges();
		String fileName1 = "src/data/TCA cycle I (prokaryotic).biopax";
		biopax2jung(fileName1);
		
		String fileName2 = "src/data/TCA cycle, aerobic respiration.biopax";
		biopax2jung(fileName2);
	}

	/**
	 * this method converts the XML/OWL .biopax file to JUNG-friendly data
	 * @param fileName the name of the .biopax file to be loaded as graph
	 */
	private static void biopax2jung(String fileName) {
		// create the graph from the biopax file using BioPAXGraphAdjList
		System.out.println("Loading the TCA cycle I (prokaryotic) graph: "+fileName);
		BioPAXGraphAdjList graph = new BioPAXGraphAdjList(fileName);
		//graph.printGraph();
		//System.out.println("Edge standardName is: "+graph.getPropertyInfo("http://http://BioCyc.org//META/pathway-biopax?type=3%38object=TCABiochemicalReaction47212", "standardName"));
		
		
		// The visualization. Code form JUNG
		Graph<Integer, String> sgv = createTempGraph(graph); // This builds the graph
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

	private static DirectedGraph<Integer, String> createTempGraph(BioPAXGraphAdjList graphBioPax) {
		
	    DirectedGraph<Integer, String> g;
		// Graph<V, E> where V is the type of the vertices and E is the type of the edges
        // Note showing the use of a SparseGraph rather than a SparseMultigraph
        g = new DirectedSparseMultigraph<Integer, String>();
        /*
        // Add some vertices. From above we defined these to be type Integer.
        String s1 = graphBioPax.getPropertyInfo("http://http://BioCyc.org//META/pathway-biopax?type=3%38object=TCABiochemicalReaction47212", "standardName");
        String s2 = graphBioPax.getPropertyInfo("http://http://BioCyc.org//META/pathway-biopax?type=3%38object=TCABiochemicalReaction48103", "standardName");
        String s3 = graphBioPax.getPropertyInfo("http://http://BioCyc.org//META/pathway-biopax?type=3%38object=TCABiochemicalReaction46376", "standardName");
        g.addVertex(s1);
        g.addVertex(s2);
        g.addVertex(s3); 
        // g.addVertex((Integer)1);  // note if you add the same object again nothing changes
        // Add some edges. From above we defined these to be of type String
        // Note that the default is for undirected edges.
        g.addEdge("Edge-A", s1, s2); // Note that Java 1.5 auto-boxes primitives
        g.addEdge("Edge-B", s2, s3);
        g.addEdge("Edge-C1", s3, s1);
        g.addEdge("Edge-C2", "s3", s1);
        */
        Integer nodeID = 0, startNode, endNode = null;
        String edgeRDFid, edgeName, edgeRDFid2;
        String[] nextEdgesRDFids;
        boolean thereIsStartNode;
        Map<String, Map<String, Integer>> tempMap = new HashMap<String, Map<String, Integer>>();
		for (Map.Entry<String, Map<String, String[]>> entry : graphBioPax.getBioPathStepsGraph().entrySet()){
			//TODO this needs rework because stepConversion is [BiochemicalReaction]
			// while nextStep is [BiochemicalPathwayStep]
			edgeRDFid2 = entry.getValue().get("stepConversion")[0];
			edgeRDFid = entry.getKey();
			// the edgeRDFid doesn't exist in tempMap then add it with null
			if (!tempMap.containsKey(edgeRDFid)){
				tempMap.put(edgeRDFid, new HashMap<String, Integer>());
			}
			// Now the edgeRDFid is surely in the tempMap. Check if has already been visualized
			if (!tempMap.get(edgeRDFid).containsKey("complete")){
				// this edge hasn't been visualized yet.
				edgeName = graphBioPax.getPropertyInfo(edgeRDFid2, "standardName");
				// if it has already a starting node index
				if (tempMap.get(edgeRDFid).containsKey("startNode")){
					startNode = tempMap.get(edgeRDFid).get("startNode"); 
				}
				// if it hasn't a node index create new.
				else{
					startNode = nodeID++;
					tempMap.get(edgeRDFid).put("startNode", startNode);
				}
				nextEdgesRDFids = entry.getValue().get("nextStep");
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
				g.addEdge(edgeName, startNode, endNode);
				//g.addEdge(edgeName.substring(0, 25), startNode, endNode);
				//System.out.println(g.toString());
				//System.out.println(edgeRDFid);
				tempMap.get(edgeRDFid).put("complete", 1);	
			}
		}
		printTempMap(tempMap);
		return g;
	}

	private static void printTempMap(Map<String, Map<String, Integer>> tempMap) {
		for ( Entry<String, Map<String, Integer>> entry : tempMap.entrySet()){
			System.out.println("edgeRDFid: "+entry.getKey());
			System.out.println("	startNode: "+entry.getValue().get("startNode"));
			System.out.println("	complete : "+entry.getValue().get("complete"));
		}
		
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

	public static void creatingAGraphAndAddingVerticesAndEdges(){
		// Graph<V, E> where V is the type of the vertices
		 // and E is the type of the edges
		 Graph<Integer, String> g = new SparseMultigraph<Integer, String>();
		 // Add some vertices. From above we defined these to be type Integer.
		 g.addVertex((Integer)1);
		 g.addVertex((Integer)2);
		 g.addVertex((Integer)3);
		 // Add some edges. From above we defined these to be of type String
		 // Note that the default is for undirected edges.
		 g.addEdge("Edge-A", 1, 2); // Note that Java 1.5 auto-boxes primitives
		 g.addEdge("Edge-B", 2, 3);
		 // Let's see what we have. Note the nice output from the
		 // SparseMultigraph<V,E> toString() method
		 System.out.println("The graph g = " + g.toString());
		 // Note that we can use the same nodes and edges in two different graphs.
		 Graph<Integer, String> g2 = new SparseMultigraph<Integer, String>();
		 g2.addVertex((Integer)1);
		 g2.addVertex((Integer)2);
		 g2.addVertex((Integer)3);
		 g2.addEdge("Edge-A", 1,3);
		 g2.addEdge("Edge-B", 2,3, EdgeType.DIRECTED);
		 g2.addEdge("Edge-C", 3, 2, EdgeType.DIRECTED);
		 g2.addEdge("Edge-P", 2,3); // A parallel edge
		 System.out.println("The graph g2 = " + g2.toString()); 
	}
	
	public static void constructingADirectedGraphWithCustomEdges(){
		//run BasicDirectedGraph.java for this example
	}
}
