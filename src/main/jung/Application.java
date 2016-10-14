package main.jung;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.uci.ics.jung.graph.DirectedGraph;

public class Application {

	private static GraphQueriesAPI graphQueries;
	private static ClusteringAlgorithm clustAlg;
	
	public static void main(String[] args)  {
			
			// assign the path to the .bpinit file that the .biopax files are stored
			// artificial test
			String biopaxLoader0 = "src/data/biopaxLoader.bpinit";
			// L-Lysine Biosynthesis
			String biopaxLoader1 = "src/data/exp1.bpinit";
			// TCA Cycle
			String biopaxLoader2 = "src/data/exp2.bpinit";
			// Cholesterol Biosynthesis  +   plant sterol biosynthesis  + Lipid A-core biosynthesis
			String biopaxLoader3 = "src/data/exp3.bpinit";
			// exp1 + exp2 + exp3
			String biopaxLoader4 = "src/data/expALL.bpinit";
			
			String biopaxLoader = biopaxLoader4;
			System.out.println("Launching Application, reading .biopax data from: "+biopaxLoader);
			// load all .biopax files from a .bpinit and create the graphList
			List<DirectedGraph<Integer, MyEdge>> graphList =
					loadGraphListFromBiopaxLoaderBPINIT(biopaxLoader);
			
			// initialize the API
			graphQueries = new GraphQueriesAPI(graphList);
			// find patterns in graphs. The main algorithm.
			graphQueries.findPatternsInGraphs(1.0);
			// print the pattern table
			graphQueries.printApplicationOutput();
			
			/** comment that **/
			//visualize graphList
			//graphQueries.visualizeGraphList();
			//visualize subGraphList
			//graphQueries.visualizeSubGraphList();
			//visualize subGraphListComplementary
			//graphQueries.visualizeComplementarySubGraphList();
			/** */
			
			
			// calculate the hierarchical clustering
			// initialize the clustering Algorithm
			clustAlg = new ClusteringAlgorithm(graphQueries);
			
			// calculate the pattern distances 
			clustAlg.calculatePatternDistances(distMetric.EUCLIDEAN_WITH_WEIGHTS);
			//linkage for patterns using MIN
			clustAlg.linkagePattern(linkMetric.MIN);
			
			
			// calculate the graph distances 
			clustAlg.calculateGraphScores();
			clustAlg.calculateGraphDistances(distMetric2.LINEAR);
			//linkage for graphs using MIN
			clustAlg.linkageGraph(linkMetric.MIN);


			// print the similarity matrix of the patterns
			clustAlg.printSimilarityMatrixPatterns();
			// print the distance matrix of the patterns
			clustAlg.printDistanceMatrixPatterns();
			// print the score matrix of the graphs
			clustAlg.printScoreMatrixGraphs();
			// print the distance matrix of the graphs
			clustAlg.printDistanceMatrixGraphs();
			
			// print clusters for Patterns
			clustAlg.printPatternClusters();
			// print clusters for Graphs
			clustAlg.printGraphClusters();
			
			// Queries about: the visualization of 2 graphs in each level of the clustering
			// algorithm which shows the 2 graphs and highlights the common patterns 
			//clustAlg.highlightPatternsInGraphPair(0, 2);
			
			//user interaction
			userInteraction();
			
			System.out.println("Writing output data and exiting Application...");
			clustAlg.writeOutputData();
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

	private static void userInteraction(){
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		//Successfully
		System.out.println("\n\nThe program ran Successfully\n");
		boolean run = true;
		while(run){
			System.out.println("what do you want to do?:\n"
					+ "1. visualize graphs\n"
					+ "2. visualize patterns\n"
					+ "3. compare two graphs\n"
					+ "4. exit\n");
			int choice = reader.nextInt(); // Scans the next token of the input as an int.
			switch (choice)
			{
			case 1:
				System.out.println("Visualizing graphs");
				graphQueries.visualizeGraphList();
				System.out.println("Done");
				break;
			case 2:
				System.out.println("Visualizing patterns");
				graphQueries.visualizeSubGraphList();
				graphQueries.visualizeComplementarySubGraphList();
				System.out.println("Done");
				break;
			case 3:
				boolean run2 = true;
				int graphNum = graphQueries.getGraphList().size();
				while(run2){
					System.out.println("Enter the first graph number");
					int first = reader.nextInt();
					System.out.println("Enter the second graph number");
					int second = reader.nextInt();
					if ( (first < 0) || (first>=graphNum) ||
							(second < 0) || (second>=graphNum)  ){
						System.out.println("Wrong graph IDs. There are "+graphNum+" graphs"
								+"please give a number between 0 and "+(graphNum-1));
					}
					else if (first == second){
						System.out.println("Please give different graph IDs");
					}
					else{
						System.out.println("Visualizing and comparing the graphs ("
					+first+", "+second+")");
						clustAlg.highlightPatternsInGraphPair(first, second);
						System.out.println("Done");
						run2 = false;
					}
				}
				break;
			case 4:
				run = false;
				break;
			default:
				System.out.println("Wrong input. Try again!");
			}
		}
		reader.close();
	}
}