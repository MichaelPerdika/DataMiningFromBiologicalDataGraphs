package main.jung;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
			// assign the path to the .bpinit file that the .biopax files are stored
			String biopaxLoader = "src/data/biopaxLoader.bpinit";
			// load all .biopax files from a .bpinit and create the graphList
			List<DirectedGraph<Integer, MyEdge>> graphList =
					loadGraphListFromBiopaxLoaderBPINIT(biopaxLoader);
			
			// initialize the API
			GraphQueriesAPI graphQueries = new GraphQueriesAPI(graphList);
			// find patterns in graphs. The main algorithm.
			graphQueries.findPatternsInGraphs(1.0);
			// print the pattern table
			graphQueries.printPatternTable();
			//visualize graphList
			graphQueries.visualizeGraphList();
			//visualize subGraphList
			graphQueries.visualizeSubGraphList();
			// calculate the hierarchical clustering
			//TODO
			ClusteringAlgorithm clustAlg = new ClusteringAlgorithm(graphQueries);
			clustAlg.calculateDistances(distMetric.EUCLIDEAN_WITH_WEIGHTS);
			clustAlg.printDistanceMatrix();
			
			/**
			 * expected results:
			 * tolerance 100%:
	g0 = Vertices:0,1,2,3,4,5,6,7,8
	Edges:1.3.5.1 [3,6]  [1,7] 1.1.5.4 [4,5] 4.2.1.2 [6,4]  [0,1] 1.1.1.37 [4,5] 2.3.3.1 2.3.3.16 [5,0] 1.1.1.42 [7,8]  [8,2] 6.2.1.5 [2,3] 
	g1 = Vertices:0,1,2,3,4,5,6,7,8
	Edges: [6,7]  [7,3] 1.1.1.41 [3,2] 1.1.1.37 [1,5] 6.2.1.5 [3,4] 1.3.5.1 [4,0] 4.2.1.2 [0,1] 6.4.1.1 [8,5]  [2,3] 2.3.3.1 2.3.3.16 [5,6] 
	g2 = Vertices:0,1,2,3,4,5,6,7,8
	Edges:1.3.5.1 [5,0]  [3,6] 2.3.3.1 2.3.3.16 [2,3] 1.1.1.41 [7,8] 4.2.1.2 [0,1]  [6,7] 6.2.1.5 [4,5]  [8,4] 1.1.1.37 [1,2] 
	g3 = Vertices:0,1,2,3,4,5,6,7,8
	Edges: [4,5] 1.1.1.37 [6,7] 6.2.1.5 [5,0] 4.2.1.3 [8,2] 4.2.1.2 [1,6] 1.1.1.42 [3,4] 1.1.5.4 [6,7] 1.3.5.1 [0,1] 4.2.1.3 [2,3] 2.3.3.1 [7,8] 
	
	p0 = Vertices:0,2,3,4,5,6
	Edges:6.2.1.5 [2,3] 1.3.5.1 [3,6] 4.2.1.2 [6,4] 1.1.1.37 [4,5] 2.3.3.1 2.3.3.16 [5,0] 
	p1 = Vertices:2,3,4,5,6
	Edges:1.1.5.4 [4,5] 1.3.5.1 [3,6] 6.2.1.5 [2,3] 1.1.1.37 [4,5] 4.2.1.2 [6,4] 
	p2 = Vertices:7,8
	Edges:1.1.1.42 [7,8] 
	p3 = Vertices:2,3
	Edges:1.1.1.41 [3,2] 
	p4 = Vertices:0,1,3,4,5
	Edges:1.1.1.37 [1,5] 4.2.1.2 [0,1] 6.2.1.5 [3,4] 1.3.5.1 [4,0] 
	
	   g0 g1 g2 g3 
	p0 1  1  1  0  
	p1 1  0  0  1  
	p2 1  0  0  1  
	p3 0  1  1  0  
	p4 1  1  1  1
	
	tolerance 75%:
	g0 = Vertices:0,1,2,3,4,5,6,7,8
	Edges:1.3.5.1 [3,6]  [1,7] 1.1.5.4 [4,5] 4.2.1.2 [6,4]  [0,1] 1.1.1.37 [4,5] 2.3.3.1 2.3.3.16 [5,0] 1.1.1.42 [7,8]  [8,2] 6.2.1.5 [2,3] 
	g1 = Vertices:0,1,2,3,4,5,6,7,8
	Edges: [6,7]  [7,3] 1.1.1.41 [3,2] 1.1.1.37 [1,5] 6.2.1.5 [3,4] 1.3.5.1 [4,0] 4.2.1.2 [0,1] 6.4.1.1 [8,5]  [2,3] 2.3.3.1 2.3.3.16 [5,6] 
	g2 = Vertices:0,1,2,3,4,5,6,7,8
	Edges:1.3.5.1 [5,0]  [3,6] 2.3.3.1 2.3.3.16 [2,3] 1.1.1.41 [7,8] 4.2.1.2 [0,1]  [6,7] 6.2.1.5 [4,5]  [8,4] 1.1.1.37 [1,2] 
	g3 = Vertices:0,1,2,3,4,5,6,7,8
	Edges: [4,5] 1.1.1.37 [6,7] 6.2.1.5 [5,0] 4.2.1.3 [8,2] 4.2.1.2 [1,6] 1.1.1.42 [3,4] 1.1.5.4 [6,7] 1.3.5.1 [0,1] 4.2.1.3 [2,3] 2.3.3.1 [7,8] 
	
	p0 = Vertices:4,5
	Edges:1.1.1.* [4,5] 
	p1 = Vertices:4,6
	Edges:4.2.1.* [6,4] 
	p2 = Vertices:4,5,6
	Edges:4.2.1.* [6,4] 1.1.1.* [4,5] 
	
	   g0 g1 g2 g3 
	p0 2  2  2  2  
	p1 1  1  1  3  
	p2 1  1  1  2
	 
	tolerance 50%:
	g0 = Vertices:0,1,2,3,4,5,6,7,8
	Edges:1.3.5.1 [3,6]  [1,7] 1.1.5.4 [4,5] 4.2.1.2 [6,4]  [0,1] 1.1.1.37 [4,5] 2.3.3.1 2.3.3.16 [5,0] 1.1.1.42 [7,8]  [8,2] 6.2.1.5 [2,3] 
	g1 = Vertices:0,1,2,3,4,5,6,7,8
	Edges: [6,7]  [7,3] 1.1.1.41 [3,2] 1.1.1.37 [1,5] 6.2.1.5 [3,4] 1.3.5.1 [4,0] 4.2.1.2 [0,1] 6.4.1.1 [8,5]  [2,3] 2.3.3.1 2.3.3.16 [5,6] 
	g2 = Vertices:0,1,2,3,4,5,6,7,8
	Edges:1.3.5.1 [5,0]  [3,6] 2.3.3.1 2.3.3.16 [2,3] 1.1.1.41 [7,8] 4.2.1.2 [0,1]  [6,7] 6.2.1.5 [4,5]  [8,4] 1.1.1.37 [1,2] 
	g3 = Vertices:0,1,2,3,4,5,6,7,8
	Edges: [4,5] 1.1.1.37 [6,7] 6.2.1.5 [5,0] 4.2.1.3 [8,2] 4.2.1.2 [1,6] 1.1.1.42 [3,4] 1.1.5.4 [6,7] 1.3.5.1 [0,1] 4.2.1.3 [2,3] 2.3.3.1 [7,8] 
	
	p0 = Vertices:4,5
	Edges:1.1.* [4,5] 
	
	   g0 g1 g2 g3 
	p0 3  2  2  3
	
	tolerance 25%:
	g0 = Vertices:0,1,2,3,4,5,6,7,8
	Edges:1.3.5.1 [3,6]  [1,7] 1.1.5.4 [4,5] 4.2.1.2 [6,4]  [0,1] 1.1.1.37 [4,5] 2.3.3.1 2.3.3.16 [5,0] 1.1.1.42 [7,8]  [8,2] 6.2.1.5 [2,3] 
	g1 = Vertices:0,1,2,3,4,5,6,7,8
	Edges: [6,7]  [7,3] 1.1.1.41 [3,2] 1.1.1.37 [1,5] 6.2.1.5 [3,4] 1.3.5.1 [4,0] 4.2.1.2 [0,1] 6.4.1.1 [8,5]  [2,3] 2.3.3.1 2.3.3.16 [5,6] 
	g2 = Vertices:0,1,2,3,4,5,6,7,8
	Edges:1.3.5.1 [5,0]  [3,6] 2.3.3.1 2.3.3.16 [2,3] 1.1.1.41 [7,8] 4.2.1.2 [0,1]  [6,7] 6.2.1.5 [4,5]  [8,4] 1.1.1.37 [1,2] 
	g3 = Vertices:0,1,2,3,4,5,6,7,8
	Edges: [4,5] 1.1.1.37 [6,7] 6.2.1.5 [5,0] 4.2.1.3 [8,2] 4.2.1.2 [1,6] 1.1.1.42 [3,4] 1.1.5.4 [6,7] 1.3.5.1 [0,1] 4.2.1.3 [2,3] 2.3.3.1 [7,8] 
	
	p0 = Vertices:3,6
	Edges:1.* [3,6] 
	p1 = Vertices:2,3
	Edges:6.* [2,3] 
	
	   g0 g1 g2 g3 
	p0 4  3  3  4  
	p1 1  2  1  1
	*/
			
			
			
			System.out.println("Goodbye");		
		}
	

	/**
	 * this method requires a biopaxFile.bpinit that has in every line the paths
	 * of all the biopax files to be loaded in the application
	 * @param biopaxLoader
	 * @return an arrayList of DirectedGraphs to be loaded to the GraphQueriesAPI
	 */
	private static List<DirectedGraph<Integer, MyEdge>> loadGraphListFromBiopaxLoaderBPINIT(
			String biopaxLoader) {
		// TODO Auto-generated method stub
		List<String> biopaxFiles = loadBiopaxFiles(biopaxLoader);
		List<DirectedGraph<Integer, MyEdge>> graphList = 
				new ArrayList<DirectedGraph<Integer, MyEdge>>();
		for (String biopax : biopaxFiles){
			System.out.println("Loading: "+biopax);
			DirectedGraph<Integer, MyEdge> graph = createGraphFromBiopaxFile(biopax);
			graphList.add(graph);
		}
		return graphList;
	}


	private static List<String> loadBiopaxFiles(String biopaxLoader) {
		// TODO Auto-generated method stub
		
		List<String> biopaxFiles = new ArrayList<String>();
		System.out.println("Reading Biopax Loader file"+biopaxLoader);
		try(BufferedReader br = new BufferedReader(new FileReader(biopaxLoader))) {
		    for(String line; (line = br.readLine()) != null; ) {
		        // process the line.
		    	if(line.contains(".biopax")) 
		    		 biopaxFiles.add(line);
		    	else{
		    		System.out.println("Warning, File "+" must contain only lines with "
		    				+ "<name>.biopax");
		    		System.out.println("	found line {"+line+"}");
		    	}
		    		
		    }
		    // line is not visible here.
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found, please give correct path of file .bpinit");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return biopaxFiles;
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
		DirectedGraph<Integer, MyEdge> graph = new Biopax2JungConverter(graphBiopax).getJungGraph();
		return graph;
	}


}

//[BiochemicalReaction] == Edge
//[SmallMolecule] == Node with standardName == the correct name.
//[BiochemicalPathwayStep] with stepDirection == LEFT_TO_RIGHT or RIGHT_TO_LEFT.
