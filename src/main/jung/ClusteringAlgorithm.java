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
	private List<List<Double>> distanceMatrix;

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
		graphSizes = new ArrayList<Integer>();
		fillGraphSizes();
		initializeDistanceMatrix();
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
	private void initializeDistanceMatrix() {
		// TODO Auto-generated method stub
		distanceMatrix = new ArrayList<List<Double>>();
		for (int i=0;i<graphSizes.size();i++){
			List<Double> rowList = new ArrayList<Double>();
			for (int j=0;j<graphSizes.size();j++){
				rowList.add(null);
			}
			distanceMatrix.add(rowList);
		}
	}
	
	/**
	 * this method calculates the distance matrix between every pair of graphs.
	 * A distance metric (distMetric enum) must be passed.
	 * This method is the default which is EUCLIDEAN_WITH_WEIGHTS
	 */
	public void calculateDistances() {
		calculateDistances(distMetric.EUCLIDEAN_WITH_WEIGHTS);
	}
	/**
	 * this method calculates the distance matrix between every pair of graphs.
	 * A distance metric (distMetric enum) must be passed.
	 * @param euclidean 
	 */
	public void calculateDistances(distMetric euclidean) {
		//Iterate through all graphs.
		for(int col=0;col<distanceMatrix.size();col++){
			for(int row=0;row<distanceMatrix.size();row++){
				// the main diagonal line has all zeros.
				if (col==row) distanceMatrix.get(col).set(row, 0.0);
				else{
					distanceMatrix.get(col).set(row, calculateGraphDistances(col,row, euclidean));
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
	private Double calculateGraphDistances(int i, int j, distMetric metric) {
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
	 * this method prints the distanceMatrix
	 */
	public void printDistanceMatrix() {
		// TODO Auto-generated method stub
		
		/**** print the grid******/
		System.out.println("\nThe Distance Matrix");
		System.out.print("    ");
		for (int num=0; num<distanceMatrix.size();num++){
			System.out.print("g"+num+"   ");
		}
		System.out.println("");
		for (int row=0;row<distanceMatrix.size();row++){
			System.out.print("g"+row+"  ");
			for (int col=0;col<distanceMatrix.get(row).size();col++){
				System.out.print(distanceMatrix.get(row).get(col)+"  ");
			}
			System.out.println("");
		}
		
	}
}
