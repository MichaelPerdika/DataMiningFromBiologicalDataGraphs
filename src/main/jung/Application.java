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
			
			/**
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
			// print the similarity matrix of the patterns
			clustAlg.printSimilarityMatrixPatterns();
			// print the distance matrix of the patterns
			clustAlg.printDistanceMatrixPatterns();
			// print the distance matrix of the graphs
			//clustAlg.printDistanceMatrixGraphs();
			
			// linkages
			//linkage for patterns using MIN
			clustAlg.linkagePattern(linkMetric.MIN);
			// print clusters for Patterns
			clustAlg.printPatternClusters();
			//linkage for graphs using MIN
			clustAlg.linkageGraph(linkMetric.MIN);
			// print clusters for Graphs
			clustAlg.printGraphClusters();
			
			// Queries about: the visualization of 2 graphs in each level of the clustering
			// algorithm which shows the 2 graphs and highlights the common patterns 
			clustAlg.highlightPatternsInGraphPair(0, 3);
			
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
