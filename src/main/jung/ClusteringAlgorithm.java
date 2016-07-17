package main.jung;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.jung.graph.DirectedGraph;

enum distMetric {
	   EUCLIDEAN, EUCLIDEAN_WITH_WEIGHTS, BIOPAX_METRIC //TODO
	}

public class ClusteringAlgorithm {

	GraphQueriesAPI gQAPI;
	private List<Integer> graphSizes;
	private List<List<Double>> distanceMatrixGraphs;
	private List<Integer> patternSizes;
	private List<List<Double>> distanceMatrixPatterns;

	/**
	 * never to be called
	 */
	@SuppressWarnings("unused")
	private ClusteringAlgorithm() {
		// TODO Auto-generated constructor stub
	}

	public ClusteringAlgorithm(GraphQueriesAPI graphQueries) {
		// TODO Auto-generated constructor stub
		this.gQAPI = graphQueries;
		patternSizes = new ArrayList<Integer>();
		graphSizes = new ArrayList<Integer>();
		
		// for the pattern distance matrix
		fillPatternSizes();
		initializeDistanceMatrixPatterns();
		
		// for the graph distance matrix
		fillGraphSizes();
		initializeDistanceMatrixGraphs();
	}

	/**
	 * this method fills patternSizes List which is the edge count 
	 * of every subGraph/pattern and the complementary ones.
	 */
	private void fillPatternSizes() {
		// TODO Auto-generated method stub
		// for patterns
		for (DirectedGraph<Integer, MyEdge> subGraph : gQAPI.getSubGraphList()){
			patternSizes.add(subGraph.getEdgeCount());
		}
		// for complementary patterns
		for (DirectedGraph<Integer, MyEdge> subGraphCom : gQAPI.getSubGraphListComplementary()){
			patternSizes.add(subGraphCom.getEdgeCount());
		}
	}
	
	/**
	 * this method fills graphSizes List which is the edge count 
	 * of every graph
	 */
	private void fillGraphSizes() {
		// TODO Auto-generated method stub
		for (DirectedGraph<Integer, MyEdge> graph : gQAPI.getGraphList()){
			graphSizes.add(graph.getEdgeCount());
		}
	}

	/** 
	 * initializes distanceMatrix with all null elements.
	 */
	private void initializeDistanceMatrixPatterns() {
		// TODO Auto-generated method stub
		distanceMatrixPatterns = new ArrayList<List<Double>>();
		for (int i=0;i<patternSizes.size();i++){
			List<Double> rowList = new ArrayList<Double>();
			for (int j=0;j<patternSizes.size();j++){
				rowList.add(null);
			}
			distanceMatrixPatterns.add(rowList);
		}
	}
	
	/** 
	 * initializes distanceMatrix with all null elements.
	 */
	private void initializeDistanceMatrixGraphs() {
		// TODO Auto-generated method stub
		distanceMatrixGraphs = new ArrayList<List<Double>>();
		for (int i=0;i<graphSizes.size();i++){
			List<Double> rowList = new ArrayList<Double>();
			for (int j=0;j<graphSizes.size();j++){
				rowList.add(null);
			}
			distanceMatrixGraphs.add(rowList);
		}
	}
	
	/**
	 * this method calculates the distance matrix between every pair of graphs.
	 * A distance metric (distMetric enum) must be passed.
	 * @param metric 
	 */
	public void calculatePatternDistances(distMetric metric) {
		//Iterate through all patterns.
		for(int col=0;col<distanceMatrixPatterns.size();col++){
			for(int row=0;row<distanceMatrixPatterns.size();row++){
				// the main diagonal line has all zeros.
				if (col==row) distanceMatrixPatterns.get(col).set(row, 0.0);
				else{
					distanceMatrixPatterns.get(col).set(row, calculatePatternDistancesCell(col,row, metric));
				}
			}
		}
	}
	
	/**
	 * this method calculates the distance matrix between every pair of graphs.
	 * A distance metric (distMetric enum) must be passed.
	 * @param metric 
	 */
	public void calculateGraphDistances(distMetric metric) {
		//Iterate through all graphs.
		for(int col=0;col<distanceMatrixGraphs.size();col++){
			for(int row=0;row<distanceMatrixGraphs.size();row++){
				// the main diagonal line has all zeros.
				if (col==row) distanceMatrixGraphs.get(col).set(row, 0.0);
				else{
					distanceMatrixGraphs.get(col).set(row, calculateGraphDistancesCell(col,row, metric));
				}
			}
		}
	}
	
	/**
	 * this method returns the distance of two graphs with index i,j.
	 * 
	 * Distance between graphs is calculated from the following metrics:
	 * 1) The distance of two graphs with x, y number of edges respectively has a difference
	 * of abs(x-y).
	 * 2) A pattern with "x" number of edges has a weight of "x" points.
	 * 
	 * @param i index of graph(i)
	 * @param j index of graph(j)
	 * @param metric is the metric that is used in calculating the distance. 
	 * EUCLIDEAN_WITH_WEIGHTS is default 
	 * @return returns the distance of graphs graph(i), graph(j)
	 */
	private Double calculatePatternDistancesCell(int i, int j, distMetric metric) {
		// TODO Auto-generated method stub
		Double dist=0.0;
		//1) distance between the edge count of graphs.
		dist += Math.abs(patternSizes.get(i) - patternSizes.get(j));
		//2) for every pattern that is found 
		switch (metric)
		{
		case EUCLIDEAN_WITH_WEIGHTS:
			for (int index=0;index<gQAPI.getGraphList().size();index++){
				Integer cellI = gQAPI.getPatternTableWholeCell(i, index);
				Integer cellJ = gQAPI.getPatternTableWholeCell(j, index);
				int weight = gQAPI.getGraphList().get(index).getEdgeCount();
				dist += weight*Math.sqrt(Math.pow(cellI - cellJ, 2));
			}
			break;
		case EUCLIDEAN:
			for (int index=0;index<gQAPI.getGraphList().size();index++){
				Integer cellI = gQAPI.getPatternTableWholeCell(i, index);
				Integer cellJ = gQAPI.getPatternTableWholeCell(j, index);
				dist += Math.sqrt(Math.pow(cellI - cellJ, 2));
			}
			break;
		case BIOPAX_METRIC:
			for (int index=0;index<gQAPI.getGraphList().size();index++){
				//TODO
				continue;
			}
			break;
		default:
			//TODO do EUCLIDEAN_WITH_WEIGHTS
		}
		
		return dist;
	}
	
	/**
	 * this method returns the distance of two graphs with index i,j.
	 * 
	 * Distance between graphs is calculated from the following metrics:
	 * 1) The distance of two graphs with x, y number of edges respectively has a difference
	 * of abs(x-y).
	 * 2) A pattern with "x" number of edges has a weight of "x" points.
	 * 
	 * @param i index of graph(i)
	 * @param j index of graph(j)
	 * @param metric is the metric that is used in calculating the distance. 
	 * EUCLIDEAN_WITH_WEIGHTS is default 
	 * @return returns the distance of graphs graph(i), graph(j)
	 */
	private Double calculateGraphDistancesCell(int i, int j, distMetric metric) {
		// TODO Auto-generated method stub
		Double dist=0.0;
		//1) distance between the edge count of graphs.
		dist += Math.abs(graphSizes.get(i) - graphSizes.get(j));
		//2) for every pattern that is found 
		switch (metric)
		{
		case EUCLIDEAN_WITH_WEIGHTS:
			for (int index=0;index<gQAPI.getSubGraphList().size();index++){
				Integer cellI = gQAPI.getPatternTableCell(index, i);
				Integer cellJ = gQAPI.getPatternTableCell(index, j);
				int weight = gQAPI.getSubGraphList().get(index).getEdgeCount();
				dist += weight*Math.sqrt(Math.pow(cellI - cellJ, 2));
			}
			break;
		case EUCLIDEAN:
			for (int index=0;index<gQAPI.getSubGraphList().size();index++){
				Integer cellI = gQAPI.getPatternTableCell(index, i);
				Integer cellJ = gQAPI.getPatternTableCell(index, j);
				dist += Math.sqrt(Math.pow(cellI - cellJ, 2));
			}
			break;
		case BIOPAX_METRIC:
			for (int index=0;index<gQAPI.getSubGraphList().size();index++){
				//TODO
				continue;
			}
			break;
		default:
			//TODO do EUCLIDEAN_WITH_WEIGHTS
		}
		
		return dist;
	}

	/**
	 * this method prints the distanceMatrixPatterns
	 */
	public void printDistanceMatrixPatterns() {
		// TODO Auto-generated method stub
		
		/**** print the grid******/
		System.out.println("\nThe Distance Matrix of the patterns");
		System.out.print("    ");
		for (int num=0; num<distanceMatrixPatterns.size();num++){
			System.out.print("p"+num+"   ");
		}
		System.out.println("");
		for (int row=0;row<distanceMatrixPatterns.size();row++){
			System.out.print("p"+row+"  ");
			for (int col=0;col<distanceMatrixPatterns.get(row).size();col++){
				System.out.print(distanceMatrixPatterns.get(row).get(col)+"  ");
			}
			System.out.println("");
		}
		
	}

	/**
	 * this method prints the distanceMatrix
	 */
	public void printDistanceMatrixGraphs() {
		// TODO Auto-generated method stub
		
		/**** print the grid******/
		System.out.println("\nThe Distance Matrix of graphs");
		System.out.print("    ");
		for (int num=0; num<distanceMatrixGraphs.size();num++){
			System.out.print("g"+num+"   ");
		}
		System.out.println("");
		for (int row=0;row<distanceMatrixGraphs.size();row++){
			System.out.print("g"+row+"  ");
			for (int col=0;col<distanceMatrixGraphs.get(row).size();col++){
				System.out.print(distanceMatrixGraphs.get(row).get(col)+"  ");
			}
			System.out.println("");
		}
		
	}
}
