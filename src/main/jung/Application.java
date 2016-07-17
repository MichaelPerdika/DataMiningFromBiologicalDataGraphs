package main.jung;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import edu.uci.ics.jung.graph.DirectedGraph;

public class Application {

	public static void main(String[] args)  {
			
			
			// assign the path to the .bpinit file that the .biopax files are stored
			String biopaxLoader = "src/data/biopaxLoader.bpinit";
			System.out.println("Launching Application, reading .biopax data from: "+biopaxLoader);
			// load all .biopax files from a .bpinit and create the graphList
			List<DirectedGraph<Integer, MyEdge>> graphList =
					loadGraphListFromBiopaxLoaderBPINIT(biopaxLoader);
			
			// initialize the API
			GraphQueriesAPI graphQueries = new GraphQueriesAPI(graphList);
			// find patterns in graphs. The main algorithm.
			graphQueries.findPatternsInGraphs(1.0);
			// print the pattern table
			graphQueries.printApplicationOutput();
			/*
			//visualize graphList
			graphQueries.visualizeGraphList();
			//visualize subGraphList
			graphQueries.visualizeSubGraphList();
			//visualize subGraphListComplementary
			graphQueries.visualizeComplementarySubGraphList();
			*/
			
			
			// calculate the hierarchical clustering
			//TODO clustering Algorithm
			// initialize the clustering Algorithm
			ClusteringAlgorithm clustAlg = new ClusteringAlgorithm(graphQueries);
			// calculate the pattern distances 
			clustAlg.calculatePatternDistances(distMetric.EUCLIDEAN_WITH_WEIGHTS);
			// calculate the graph distances 
			clustAlg.calculateGraphDistances(distMetric.EUCLIDEAN_WITH_WEIGHTS);
			// print the distance matrix of the patterns
			clustAlg.printDistanceMatrixPatterns();
			// print the distance matrix of the graphs
			clustAlg.printDistanceMatrixGraphs();
			
			// linkages
			//linkage for patterns using MIN
			clustAlg.linkagePattern(linkMetric.MIN);
			// print clusters for Patterns
			clustAlg.printPatternClusters();
			//linkage for graphs using MIN
			clustAlg.linkageGraph(linkMetric.MIN);
			// print clusters for Graphs
			clustAlg.printGraphClusters();
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
			
			
			
			System.out.println("Application ran succesfully...");		
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
		    	// the path must end to ".biopax". 
		    	// A line is "commented" if it starts with "#"
		    	if(line.contains(".biopax") && !line.startsWith("#")) 
		    		 biopaxFiles.add(line);
		    	else if (line.startsWith("#"))
		    		// this is a commented line.
		    		continue; 
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
