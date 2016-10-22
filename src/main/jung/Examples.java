package main.jung;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import test.TestingUtils;

public class Examples {

	private static GraphQueriesAPI graphQueries;
	private static ClusteringAlgorithm clustAlg;
	
	public static void main(String[] args)  {
		
		//Load correct experiment
		List<DirectedGraph<Integer, MyEdge>> graphList0 = loadExperimentOfChapter3();
		List<DirectedGraph<Integer, MyEdge>> graphList1 = loadExperiment4_1();
		List<DirectedGraph<Integer, MyEdge>> graphList2 = loadExperiment4_2();
		List<DirectedGraph<Integer, MyEdge>> graphList3 = loadExperiment4_3();
		
		// initialize the API
		graphQueries = new GraphQueriesAPI(graphList3);
		// find patterns in graphs. The main algorithm.
		graphQueries.findPatternsInGraphs(1.0);
		// print the pattern table
		graphQueries.printApplicationOutput();
		
		//visualize graphList
		//graphQueries.visualizeGraphList();
		//visualize subGraphList
		//graphQueries.visualizeSubGraphList();
		//visualize subGraphListComplementary
		//graphQueries.visualizeComplementarySubGraphList();
		
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
		
		System.out.println("Exiting Application...\nDone");
		//The End	
		clustAlg.writeOutputData();
	}

	private static List<DirectedGraph<Integer, MyEdge>> loadExperimentOfChapter3() {
		List<DirectedGraph<Integer, MyEdge>> graphList = 
				new ArrayList<DirectedGraph<Integer, MyEdge>>();
		
		DirectedSparseMultigraph<Integer, MyEdge> graph1 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		DirectedSparseMultigraph<Integer, MyEdge> graph2 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		DirectedSparseMultigraph<Integer, MyEdge> graph3 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		DirectedSparseMultigraph<Integer, MyEdge> graph4 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		/*********graph1***********/
		TestingUtils.addEdge(graph1, "1.1.1.1", 0, 1);
		TestingUtils.addEdge(graph1, "2.2.2.2", 1, 2);
		TestingUtils.addEdge(graph1, "4.4.4.4", 1, 3);
		TestingUtils.addEdge(graph1, "3.3.3.3", 3, 2);
		/*********graph2***********/
		TestingUtils.addEdge(graph2, "1.1.1.1", 5, 6);
		TestingUtils.addEdge(graph2, "2.2.2.2", 6, 7);
		TestingUtils.addEdge(graph2, "6.6.6.6", 5, 8);
		TestingUtils.addEdge(graph2, "5.5.5.5", 8, 7);
		/*********graph3***********/
		TestingUtils.addEdge(graph3, "1.1.1.1", 0, 1);
		TestingUtils.addEdge(graph3, "4.4.4.4", 1, 2);
		TestingUtils.addEdge(graph3, "6.6.6.6", 2, 3);
		TestingUtils.addEdge(graph3, "5.5.5.5", 3, 4);
		TestingUtils.addEdge(graph3, "3.3.3.3", 0, 5);
		TestingUtils.addEdge(graph3, "6.6.6.6", 5, 6);
		TestingUtils.addEdge(graph3, "5.5.5.5", 6, 7);
		/*********graph4***********/
		TestingUtils.addEdge(graph4, "6.6.6.6", 9, 8);
		TestingUtils.addEdge(graph4, "5.5.5.5", 8, 7);
		graphList.add(graph1); graphList.add(graph2);
		graphList.add(graph3); graphList.add(graph4);
		return graphList;
	}
	
	/**
	 * 3 similar graphs with other 2 similar graphs
	 * @return
	 */
	private static List<DirectedGraph<Integer, MyEdge>> loadExperiment4_1() {
		List<DirectedGraph<Integer, MyEdge>> graphList = 
				new ArrayList<DirectedGraph<Integer, MyEdge>>();
		
		DirectedSparseMultigraph<Integer, MyEdge> graph1 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		DirectedSparseMultigraph<Integer, MyEdge> graph2 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		DirectedSparseMultigraph<Integer, MyEdge> graph3 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		DirectedSparseMultigraph<Integer, MyEdge> graph4 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		DirectedSparseMultigraph<Integer, MyEdge> graph5 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		
		/*********graph1***********/
		TestingUtils.addEdge(graph1, "1.1.1.1", 0, 1);
		TestingUtils.addEdge(graph1, "2.2.2.2", 1, 2);
		TestingUtils.addEdge(graph1, "4.4.4.4", 2, 3);
		TestingUtils.addEdge(graph1, "3.3.3.3", 3, 4);
		/*********graph2***********/
		TestingUtils.addEdge(graph2, "1.1.1.1", 0, 1);
		TestingUtils.addEdge(graph2, "2.2.2.2", 1, 2);
		TestingUtils.addEdge(graph2, "4.4.4.4", 2, 3);
		TestingUtils.addEdge(graph2, "3.3.3.3", 3, 4);
		TestingUtils.addEdge(graph2, "5.5.5.5", 4, 5);//extra
		/*********graph3***********/
		TestingUtils.addEdge(graph3, "1.1.1.1", 0, 1);
		TestingUtils.addEdge(graph3, "2.2.2.2", 1, 2);
		TestingUtils.addEdge(graph3, "4.4.4.4", 2, 3);
		TestingUtils.addEdge(graph3, "3.3.3.3", 3, 4);
		TestingUtils.addEdge(graph3, "5.5.5.5", 4, 5);//extra
		TestingUtils.addEdge(graph3, "6.6.6.6", 5, 6);//extra
		
		/*********graph4***********/
		TestingUtils.addEdge(graph4, "1.2.3.4", 5, 6);
		TestingUtils.addEdge(graph4, "2.3.4.5", 6, 7);
		TestingUtils.addEdge(graph4, "3.4.5.6", 5, 8);
		TestingUtils.addEdge(graph4, "2.1.4.5", 8, 7);
		/*********graph5***********/
		TestingUtils.addEdge(graph5, "1.2.3.4", 5, 6);
		TestingUtils.addEdge(graph5, "2.3.4.5", 6, 7);
		TestingUtils.addEdge(graph5, "3.4.5.6", 5, 8);
		TestingUtils.addEdge(graph5, "2.1.4.5", 8, 7);
		TestingUtils.addEdge(graph5, "3.3.3.3", 8, 9);//extra
		
		graphList.add(graph1); graphList.add(graph2);
		graphList.add(graph3); graphList.add(graph4); graphList.add(graph5);
		return graphList;
	}
	
	/**
	 * 5 similar graphs with other 0 similar graphs
	 * @return
	 */
	private static List<DirectedGraph<Integer, MyEdge>> loadExperiment4_2() {
		List<DirectedGraph<Integer, MyEdge>> graphList = 
				new ArrayList<DirectedGraph<Integer, MyEdge>>();
		
		DirectedSparseMultigraph<Integer, MyEdge> graph1 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		DirectedSparseMultigraph<Integer, MyEdge> graph2 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		DirectedSparseMultigraph<Integer, MyEdge> graph3 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		DirectedSparseMultigraph<Integer, MyEdge> graph4 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		DirectedSparseMultigraph<Integer, MyEdge> graph5 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		
		/*********graph1***********/
		TestingUtils.addEdge(graph1, "1.1.1.1", 0, 1);
		TestingUtils.addEdge(graph1, "2.2.2.2", 1, 2);
		TestingUtils.addEdge(graph1, "4.4.4.4", 2, 3);
		TestingUtils.addEdge(graph1, "3.3.3.3", 3, 4);
		/*********graph2***********/
		TestingUtils.addEdge(graph2, "1.1.1.1", 0, 1);
		TestingUtils.addEdge(graph2, "2.2.2.2", 1, 2);
		TestingUtils.addEdge(graph2, "4.4.4.4", 2, 3);
		TestingUtils.addEdge(graph2, "3.3.3.3", 3, 4);
		TestingUtils.addEdge(graph2, "5.5.5.5", 4, 5);//extra
		/*********graph3***********/
		TestingUtils.addEdge(graph3, "1.1.1.1", 0, 1);
		TestingUtils.addEdge(graph3, "2.2.2.2", 1, 2);
		TestingUtils.addEdge(graph3, "4.4.4.4", 2, 3);
		TestingUtils.addEdge(graph3, "3.3.3.3", 3, 4);
		TestingUtils.addEdge(graph3, "5.5.5.5", 4, 5);//extra
		TestingUtils.addEdge(graph3, "6.6.6.6", 5, 6);//extra
		/*********graph4***********/
		TestingUtils.addEdge(graph4, "1.1.1.1", 0, 1);
		TestingUtils.addEdge(graph4, "2.2.2.2", 1, 2);
		TestingUtils.addEdge(graph4, "4.4.4.4", 2, 3);
		TestingUtils.addEdge(graph4, "3.3.3.3", 3, 4);
		TestingUtils.addEdge(graph4, "1.2.3.4", 9, 0);//extra
		/*********graph5***********/
		TestingUtils.addEdge(graph5, "1.1.1.1", 0, 1);
		TestingUtils.addEdge(graph5, "2.2.2.2", 1, 2);
		TestingUtils.addEdge(graph5, "4.4.4.4", 2, 3);
		TestingUtils.addEdge(graph5, "3.3.3.3", 3, 4);
		TestingUtils.addEdge(graph5, "1.2.3.4", 9, 0);//extra
		TestingUtils.addEdge(graph5, "4.3.2.1", 8, 9);//extra
		
		graphList.add(graph1); graphList.add(graph2);
		graphList.add(graph3); graphList.add(graph4); graphList.add(graph5);
		return graphList;
	}
	
	/**
	 * 4 similar graphs with other 1 similar graphs
	 * @return
	 */
	private static List<DirectedGraph<Integer, MyEdge>> loadExperiment4_3() {
		List<DirectedGraph<Integer, MyEdge>> graphList = 
				new ArrayList<DirectedGraph<Integer, MyEdge>>();
		
		DirectedSparseMultigraph<Integer, MyEdge> graph1 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		DirectedSparseMultigraph<Integer, MyEdge> graph2 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		DirectedSparseMultigraph<Integer, MyEdge> graph3 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		DirectedSparseMultigraph<Integer, MyEdge> graph4 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		DirectedSparseMultigraph<Integer, MyEdge> graph5 = 
				new DirectedSparseMultigraph<Integer, MyEdge>();
		
		/*********graph1***********/
		TestingUtils.addEdge(graph1, "1.1.1.1", 0, 1);
		TestingUtils.addEdge(graph1, "2.2.2.2", 1, 2);
		TestingUtils.addEdge(graph1, "4.4.4.4", 2, 3);
		TestingUtils.addEdge(graph1, "3.3.3.3", 3, 4);
		/*********graph2***********/
		TestingUtils.addEdge(graph2, "1.1.1.1", 0, 1);
		TestingUtils.addEdge(graph2, "2.2.2.2", 1, 2);
		TestingUtils.addEdge(graph2, "4.4.4.4", 2, 3);
		TestingUtils.addEdge(graph2, "3.3.3.3", 3, 4);
		TestingUtils.addEdge(graph2, "5.5.5.5", 4, 5);//extra
		/*********graph3***********/
		TestingUtils.addEdge(graph3, "1.1.1.1", 0, 1);
		TestingUtils.addEdge(graph3, "2.2.2.2", 1, 2);
		TestingUtils.addEdge(graph3, "4.4.4.4", 2, 3);
		TestingUtils.addEdge(graph3, "3.3.3.3", 3, 4);
		TestingUtils.addEdge(graph3, "5.5.5.5", 4, 5);//extra
		TestingUtils.addEdge(graph3, "6.6.6.6", 5, 6);//extra
		/*********graph4***********/
		TestingUtils.addEdge(graph4, "1.1.1.1", 0, 1);
		TestingUtils.addEdge(graph4, "2.2.2.2", 1, 2);
		TestingUtils.addEdge(graph4, "4.4.4.4", 2, 3);
		TestingUtils.addEdge(graph4, "3.3.3.3", 3, 4);
		TestingUtils.addEdge(graph4, "1.2.3.4", 9, 0);//extra
		/*********graph5***********/
		TestingUtils.addEdge(graph5, "1.2.3.4", 0, 1);
		TestingUtils.addEdge(graph5, "5.6.2.2", 1, 2);
		TestingUtils.addEdge(graph5, "1.4.4.4", 2, 3);
		TestingUtils.addEdge(graph5, "3.4.5.3", 3, 4);
		TestingUtils.addEdge(graph5, "1.2.3.4", 9, 0);//extra
		TestingUtils.addEdge(graph5, "4.3.2.1", 8, 9);//extra
		
		graphList.add(graph1); graphList.add(graph2);
		graphList.add(graph3); graphList.add(graph4); graphList.add(graph5);
		return graphList;
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
