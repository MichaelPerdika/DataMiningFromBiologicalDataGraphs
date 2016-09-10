package main.jung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.uci.ics.jung.graph.DirectedGraph;

enum distMetric {
	   EUCLIDEAN, EUCLIDEAN_WITH_WEIGHTS, BIOPAX_METRIC //TODO
	}

enum linkMetric {
	   MIN, MAX //TODO
	}

public class ClusteringAlgorithm {

	GraphQueriesAPI gQAPI;
	private List<Integer> graphSizes;
	private List<List<Double>> distanceMatrixGraphs;
	private List<Integer> patternSizes;
	private List<List<Double>> distanceMatrixPatterns;
	private List<List<Double>> similarityMatrixPatterns;
	private List<Double> linkageDistancesPatterns;
	private List<List<String>> linkageClustersPatterns;
	private List<Double> linkageDistancesGraphs;
	private List<List<String>> linkageClustersGraphs;
	private List<DirectedGraph<Integer, MyEdge>> subGraphListWhole;

	/**
	 * never to be called
	 */
	@SuppressWarnings("unused")
	private ClusteringAlgorithm() {
		// TODO Auto-generated constructor stub
	}

	public ClusteringAlgorithm(GraphQueriesAPI graphQueries) {
		// TODO Auto-generated constructor stub
		gQAPI = graphQueries;
		patternSizes = new ArrayList<Integer>();
		graphSizes = new ArrayList<Integer>();
		linkageDistancesPatterns = new ArrayList<Double>();
		linkageClustersPatterns = new ArrayList<List<String>>();
		linkageDistancesGraphs = new ArrayList<Double>();
		linkageClustersGraphs = new ArrayList<List<String>>();
		subGraphListWhole = new ArrayList<DirectedGraph<Integer, MyEdge>>();
		
		// for the pattern distance matrix
		fillPatternSizesAndWholeSubGraphList();
		initializeDistanceMatrixPatterns();
		initializeSimilarityMatrixPatterns();
		
		// for the graph distance matrix
		fillGraphSizes();
		initializeDistanceMatrixGraphs();
	}

	/**
	 * this method fills patternSizes List which is the edge count 
	 * of every subGraph/pattern and the complementary ones.
	 */
	private void fillPatternSizesAndWholeSubGraphList() {
		// TODO Auto-generated method stub
		// for patterns
		for (DirectedGraph<Integer, MyEdge> subGraph : gQAPI.getSubGraphList()){
			patternSizes.add(subGraph.getEdgeCount());
			subGraphListWhole.add(subGraph);
		}
		// for complementary patterns
		for (DirectedGraph<Integer, MyEdge> subGraphCom : gQAPI.getSubGraphListComplementary()){
			patternSizes.add(subGraphCom.getEdgeCount());
			subGraphListWhole.add(subGraphCom);
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
	private void initializeSimilarityMatrixPatterns() {
		// TODO Auto-generated method stub
		similarityMatrixPatterns = new ArrayList<List<Double>>();
		for (int i=0;i<patternSizes.size();i++){
			List<Double> rowList = new ArrayList<Double>();
			for (int j=0;j<patternSizes.size();j++){
				rowList.add(null);
			}
			similarityMatrixPatterns.add(rowList);
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
		// distance matrix is symmetric so find for upper right and assign
		// the same values for the lower left
		for(int col=0;col<distanceMatrixPatterns.size();col++){
			for(int row=col;row<distanceMatrixPatterns.size();row++){
				// the main diagonal line has all zeros.
				if (col==row) {
					distanceMatrixPatterns.get(col).set(row, 0.0);
					similarityMatrixPatterns.get(col).set(row, 0.0);
					}
				else{
					double dist = calculatePatternDistancesCell(col,row, metric);
					double similarity = getSimilarityScoreBetweenTwoPatterns(
							subGraphListWhole.get(row), subGraphListWhole.get(col));
					// upper right
					distanceMatrixPatterns.get(col).set(row, (1-similarity)*dist);
					similarityMatrixPatterns.get(col).set(row, similarity);
					// lower left
					distanceMatrixPatterns.get(row).set(col, (1-similarity)*dist);
					similarityMatrixPatterns.get(row).set(col, similarity);
				}
			}
		}
	}
	
	/**
	 * this method gets two patterns/subGraphs and returns the similarity between them.
	 * 0 means that the patterns have nothing in common and 1 that they are the same
	 * @param pattern1
	 * @param pattern2
	 * @return the similarity between pattern1 and 2 a number from 0. to 1.
	 */
	public double getSimilarityScoreBetweenTwoPatterns(
			DirectedGraph<Integer, MyEdge> pattern1,
			DirectedGraph<Integer, MyEdge> pattern2) {
		// TODO Auto-generated method stub
		
		if (pattern1 == null || pattern2 == null)
			return 0.0;
		double similarity = 0.0;
		// get both canonical label adjacent lists
		Map<Integer, Map<String, Map<Integer, List<String>>>> cLA1 = 
				gQAPI.getCanonicalLabelAdjList(pattern1);
		Map<Integer, Map<String, Map<Integer, List<String>>>> cLA2 = 
				gQAPI.getCanonicalLabelAdjList(pattern2);
		
		// get all the important info between these two patterns.
		Map<String, Map<String, List<List<String>>>> combinations = 
				createCombinationsBetween2CLA(cLA1, cLA2);
		//System.out.println("combinations are: "+combinations);
		
		List<List<String[]>> edgePairList = new ArrayList<List<String[]>>();
	    
		for (Integer k1 : cLA1.keySet()){
	        for (Integer k2 : cLA2.keySet()){
	        	List<List<String[]>> tempEdgePairList = 
	        			getEdgePairList(k1, k2, combinations);
	        	for (int k=0; k<tempEdgePairList.size();k++){
	        		edgePairList.add(tempEdgePairList.get(k));
	        	}
	        }
	    }
		 
		int maxNum = Math.max(pattern1.getEdgeCount(), pattern2.getEdgeCount());
		similarity = getSimilarity(edgePairList, maxNum);
		//System.out.println("similarity is: "+similarity);
		return similarity;
	}

	/**
	 * this method returns the similarity given an edgePairList to compare
	 * @param edgePairList
	 * @param maxNum
	 * @return
	 */
	private double getSimilarity(List<List<String[]>> edgePairList, int maxNum) {
		// TODO Auto-generated method stub
		double maxSimilarity = 0.;
	    // max equals the getEdgeCount from graph
	    //System.out.println("final edgePairList:");
	    for (int i=0; i<edgePairList.size();i++){
	        List<String[]> listOfPairs = edgePairList.get(i);
	        double tempSimilarity = 0;
	        //System.out.println("New PairList");
	    	for (int j=0; j<listOfPairs.size();j++){
	    		String[] pair = listOfPairs.get(j);
	            //System.out.println( pair[0] + pair[1]);
	            
	            //System.out.println("parsed : "+ getPairScore(pair[0], pair[1]));
	            double tempPairScore = getPairScore(pair[0], pair[1]);
	            
	            tempSimilarity += tempPairScore/maxNum;
	    	}
	        if (maxSimilarity < tempSimilarity)
	            maxSimilarity = tempSimilarity;
	        if (Math.abs(maxSimilarity-1) < 0.0001)
	            return maxSimilarity;
	    }
	    return maxSimilarity;
	}

	/**
	 * this method takes two edge names and it checks how similar they are
	 * @param pair1
	 * @param pair2
	 * @return score between 0.0 and 1.0
	 */
	private double getPairScore(String pair1, String pair2) {
		// TODO Auto-generated method stub
		String[] pai1 = pair1.substring(1, pair1.length() - 1).split(" ");
		String[] pai2 = pair2.substring(1, pair2.length() - 1).split(" ");
		//System.out.print(pai1[0]+ ": "); System.out.println(pai1.length);
		//System.out.print(pai2[0]+ ": "); System.out.println(pai2.length);
		List<List<String>> e1 = GraphQueriesAPI.parseEdgeNames(pai1);
		List<List<String>> e2 = GraphQueriesAPI.parseEdgeNames(pai2);
		
		if ((e1 == null && e2 == null) || 
				(e1.size()==0 && e2.size()==0) ){
	        return 0.0;
			
	    }

		if((e1 == null && e2 != null) 
			      || (e1 != null && e2 == null)
			      || (e1.size()==0 && e2.size() !=0)
			      || (e1.size()!=0 && e2.size() ==0)){
			        return 0.0;
		}
		
		double maxScore = -1, avgScore = 0;
		String[] comName = new String[e1.size()];
		for (int i=0;i<e1.size();i++){
			maxScore = -1;
			String tempComName = "";
			for (int j=0;j<e2.size();j++){
				/*******this is the case of "" == ""
				 * it is unequal for now but in the future fix this.
				 */
				if (e1.get(i).get(0).isEmpty() && e2.get(j).get(0).isEmpty()) 
					return 0.0;
				/******/
				double tempScore = GraphQueriesAPI.getParsedEdgeScore(e1.get(i), e2.get(j));
				String tempName = GraphQueriesAPI.getParsedEdgeName(e1.get(i), e2.get(j));
				if (maxScore < tempScore){
					maxScore = tempScore;
					tempComName = tempName;
				}
			}
			avgScore += maxScore;
			comName[i] = tempComName;
		}
		if (e1.size()==0 && e2.size() !=0)
			return 0.0;
		else
			avgScore = avgScore/e1.size();
		
		return avgScore;
	}

	/**
	 * this method returns a new edgePairList to be checked later on.
	 * @param vertex1 id of first vertex
	 * @param vertex2 id of second vertex
	 * @param combinations a Map that has all the appropriate info.
	 * @return
	 */
	private List<List<String[]>> getEdgePairList(Integer vertex1,	Integer vertex2,
			Map<String, Map<String, List<List<String>>>> combinations) {
		// TODO Auto-generated method stub
		Map<String, Map<String, List<List<String>>>> curCombinations = 
				DeepClone.deepClone(combinations);
		List<List<String[]>> edgePairList = new ArrayList<List<String[]>>();
	    List<List<String>> firstList = 
	    		DeepClone.deepClone(curCombinations.get(vertex1+"-"+vertex2).get("first"));
	    List<List<String>> secondList = 
	    		DeepClone.deepClone(curCombinations.get(vertex1+"-"+vertex2).get("second"));
	    List<String> visitedVertices = new ArrayList<String>();
	    int i = 0;
	    int j = 0;
	    while (true){
	        List<String> firstPairList, secondPairList;
	        List<String[]> curEdgePairList = new ArrayList<String[]>();
	        List<String> visitedPairs = new ArrayList<String>();
			try{
	            firstPairList = firstList.get(i);
	            secondPairList = secondList.get(i);
	        }
	        catch(IndexOutOfBoundsException e){
	            //out of bounds then return from loop
	            break;
	        }
	        while (true){
	        	String f,s;
	            try{
	                f = firstPairList.get(j);
	                s = secondPairList.get(j);
	            }
	            catch(IndexOutOfBoundsException e){
	                //out of bounds then return from loop
	                break;
	            }
	            
	            //real code here
	            //System.out.println(firstPairList);
	            //System.out.println(secondPairList);
	            String[] beforeV =  { f.split("-->")[0], s.split("-->")[0] };
	            String[] afterV = { f.split("-->")[2], s.split("-->")[2] };
	            String[] curV = { f.split("-->")[1], s.split("-->")[1] };
	            //before vertices
	            //System.out.println("suka"+beforeV[0]);
	            if ( !(beforeV[0].equals("-1") || beforeV[1].equals("-1")) && 
	            		(!visitedVertices.contains(beforeV[0]+"-"+beforeV[1])) ){
	            	//System.out.println("gotcha"+f+s);
	                List<List<List<String>>> newCheckings = 
	                		checkBefore(beforeV, curV, combinations, 
	                				curEdgePairList, edgePairList, visitedPairs);
	                appendNewCheckList(newCheckings, firstList, secondList, 
	                		firstPairList, secondPairList);
	            }
	            //after vertices
	            //System.out.println("suka2"+afterV[0]);
	            if (!(afterV[0].equals("-1") || afterV[1].equals("-1")) && 
	            		(!visitedVertices.contains(afterV[0]+"-"+afterV[1])) ){
	            	List<List<List<String>>> newCheckings = 
	            			checkAfter(afterV, curV, combinations, 
	            					curEdgePairList, edgePairList, visitedPairs);
	            	appendNewCheckList(newCheckings, firstList, secondList, 
	            			firstPairList, secondPairList);
	            }
	            if (!visitedVertices.contains(curV[0]+"-"+curV[1])) 
	            	visitedVertices.add(curV[0]+"-"+curV[1]);
	            j++;
	        }
	        i++;
	    }
	    /**
	    System.out.println("finally " + vertex1 +"~"+ vertex2);
	    for (int k=0;k<edgePairList.size();k++){
	    	System.out.println("----");
	    	for (int m=0;m<edgePairList.get(k).size();m++){
	    		System.out.println("--");
	    		for (int l=0;l<edgePairList.get(k).get(m).length;l++) 
	    			System.out.print(edgePairList.get(k).get(m)[l]+" ");
	    		System.out.print("\n");
	    	}
	    }
	    */
	    return edgePairList;
	}

	private List<List<List<String>>> checkAfter(String[] afterV, String[] currentV,
			Map<String, Map<String, List<List<String>>>> combinations, 
			List<String[]> curEdgePairList,
			List<List<String[]>> edgePairList, List<String> visitedPairs) {
		// TODO Auto-generated method stub
		//System.out.println("mjk2"+combinations);
		//System.out.println(afterV[0]+"-"+afterV[1]);
		List<List<String>> firstList = combinations.get(afterV[0]+"-"+afterV[1]).get("first");
	    List<List<String>> secondList = combinations.get(afterV[0]+"-"+afterV[1]).get("second"); 
	    List<List<String>> edges1 = combinations.get(afterV[0]+"-"+afterV[1]).get("edgesFirst");
	    List<List<String>> edges2 = combinations.get(afterV[0]+"-"+afterV[1]).get("edgesSecond");   
	    List<List<String[]>> edgeList = new ArrayList<List<String[]>>();
	    List<List<String>> newCheck1 = new ArrayList<List<String>>();
	    List<List<String>> newCheck2 = new ArrayList<List<String>>();
	    List<String> originalVisitedPairs = DeepClone.deepClone(visitedPairs);
	    
	    for (int i=0;i<firstList.size();i++){
	    	List<String> fL = firstList.get(i);
	    	List<String> sL = secondList.get(i);
	    	List<String> edge1 = edges1.get(i);
	    	List<String> edge2 = edges2.get(i);
	    	
	        List<String[]> edgeList1 = new ArrayList<String[]>();
	        List<String> newCheck11 = new ArrayList<String>();
	        List<String> newCheck22 = new ArrayList<String>();
	        //for f, s, e1, e2 in zip(fL, sL, edge1, edge2):
	        for (int j=0;j<fL.size();j++){
	        	String f = fL.get(j);
	        	String s = sL.get(j);
	        	String e1 = edge1.get(j);
	        	String e2 = edge2.get(j);
	        	
	        	String[] beforeV = {f.split("-->")[0], s.split("-->")[0]};
	            if ( (beforeV[0].equals(currentV[0])) && (beforeV[1].equals(currentV[1])) ){
	            	visitedPairs.add(beforeV[0]+"~"+currentV[0]);
	            	String[] temp = {e1.split("-->")[0], e2.split("-->")[0]};
	            	edgeList1.add(temp);
					for (int k=0; k<fL.size();k++) newCheck11.add(fL.get(k));
					for (int k=0; k<sL.size();k++) newCheck22.add(sL.get(k));
	            }
	        }
	        if (edgeList1.size() > 0){
	            edgeList.add(edgeList1);
	            newCheck1.add(newCheck11);
	            newCheck2.add(newCheck22);
	        }
	    }
	    // current edgeList is finished now. If it is size() = 1 then append as it is.
	    // if size() >1 then do deep copy of edgePairList and then append.
	    if (edgeList.size() > 0){
	    	List<String[]> original = DeepClone.deepClone(curEdgePairList);
	    	// for the first list(0) of edgeList add it to existing edgePairList
	    	for (int i=0; i< edgeList.get(0).size();i++){
	        	String[] pair = edgeList.get(0).get(i);
	        	curEdgePairList.add(pair);
	        }
	    	// if there are more then deepCopy edgePairList and append the new
	    	// potential combinations of edgeList starting from the second cell
	    	if (edgeList.size() > 1){
	    		// start from 1 and not 0 because for 0 we used first/secondPairList
	    		for (int k=1; k < edgeList.size();k++){
	    			List<String[]> temp = DeepClone.deepClone(original);
	    			for (int i=0; i < edgeList.get(k).size();i++){
	    	        	String[] pair = edgeList.get(0).get(i);
	    	        	temp.add(pair);
	    	        }
	    			// append temp to edgePairList
	    			edgePairList.add(temp);
	    		}
	    		
	    	}
	    	else{
	    		if (edgePairList.isEmpty()){
	    			edgePairList.add(curEdgePairList);
	    		}
	    	}
	    	
	    }
	    // else size == 0 do nothing for edgePairList
	   
	    List<List<List<String>>> newCheckings = new ArrayList<List<List<String>>>();
		newCheckings.add(newCheck1);
	    newCheckings.add(newCheck2);
	    return newCheckings;
	}

	private void appendNewCheckList(List<List<List<String>>> newCheckings, 
			List<List<String>> firstList,
			List<List<String>> secondList, 
			List<String> firstPairList, 
			List<String> secondPairList) {
		// TODO Auto-generated method stub
		List<List<String>> newCheck1 = newCheckings.get(0);
		List<List<String>> newCheck2 = newCheckings.get(1);
		
		if (newCheck1.size()>0){
			List<String> original1 = DeepClone.deepClone(firstPairList);
	        List<String> original2 = DeepClone.deepClone(secondPairList);
	        // for the first list(0) of newCheckx add it to first/secondPairList
	        for (int j=0;j<newCheck1.get(0).size();j++){	
	            firstPairList.add(newCheck1.get(0).get(j)); 
	            secondPairList.add(newCheck2.get(0).get(j));
	        }
	        // if there are more then create new lists in first/secondList
	        if (newCheck1.size() > 1){
	        	// start from 1 and not 0 because for 0 we used first/secondPairList
	        	for (int i=1; i<newCheck1.size();i++){
	        		List<String> temp1 = DeepClone.deepClone(original1);
	    	        List<String> temp2 = DeepClone.deepClone(original2);
	    	        for (int j=0;j<newCheck1.get(i).size();j++){	
	    	        	temp1.add(newCheck1.get(i).get(j)); 
	    	        	temp2.add(newCheck2.get(i).get(j));
	    	        }
	        		firstList.add(temp1);
			    	secondList.add(temp2);
	        	}
	        	
	        }
		}
		// else if size == 0 do nothing
	}

	/**
	 * this method returns a newCheck list that is going to be added to the 
	 * outer loop (while) that checks for valid combinations.
	 * @param beforeV
	 * @param currentV
	 * @param combinations
	 * @param curEdgePairList
	 * @return a new list to be appended
	 */
	private List<List<List<String>>> checkBefore(String[] beforeV, String[] currentV, 
			Map<String, Map<String, List<List<String>>>> combinations,
			List<String[]> curEdgePairList, List<List<String[]>> edgePairList,
			List<String> visitedPairs) {
		
		//System.out.println("mjk"+combinations);
		//System.out.println(beforeV[0]+"-"+beforeV[1]);
		List<List<String>> firstList = combinations.get(beforeV[0]+"-"+beforeV[1]).get("first");
	    List<List<String>> secondList = combinations.get(beforeV[0]+"-"+beforeV[1]).get("second");
	    List<List<String>> edges1 = combinations.get(beforeV[0]+"-"+beforeV[1]).get("edgesFirst");
	    List<List<String>> edges2 = combinations.get(beforeV[0]+"-"+beforeV[1]).get("edgesSecond");  
	    List<List<String[]>> edgeList = new ArrayList<List<String[]>>();
	    List<List<String>> newCheck1 = new ArrayList<List<String>>();
	    List<List<String>> newCheck2 = new ArrayList<List<String>>();
	    for (int i=0;i<firstList.size();i++){
	    	List<String> fL = firstList.get(i);
	    	List<String> sL = secondList.get(i);
	    	List<String> edge1 = edges1.get(i);
	    	List<String> edge2 = edges2.get(i);
	    	
	        List<String[]> edgeList1 = new ArrayList<String[]>();
	        List<String> newCheck11 = new ArrayList<String>();
	        List<String> newCheck22 = new ArrayList<String>();
	        for (int j=0;j<fL.size();j++){
	        	String f = fL.get(j);
	        	String s = sL.get(j);
	        	String e1 = edge1.get(j);
	        	String e2 = edge2.get(j);
	        	
	        	String[] afterV = {f.split("-->")[2], s.split("-->")[2]};
	            if ( (afterV[0].equals(currentV[0])) && ( afterV[1].equals(currentV[1])) 
	            		&& (!visitedPairs.contains(currentV[0]+"~"+afterV[0]))){
	            	visitedPairs.add(currentV[0]+"~"+afterV[0]);
	            	String[] temp = {e1.split("-->")[1], e2.split("-->")[1]};
	            	edgeList1.add(temp);
					for (int k=0; k<fL.size();k++) newCheck11.add(fL.get(k));
					for (int k=0; k<sL.size();k++) newCheck22.add(sL.get(k));
	            }
	        }
	        
	        if (edgeList1.size() > 0){
	            edgeList.add(edgeList1);
	            newCheck1.add(newCheck11);
	            newCheck2.add(newCheck22);
	        }
	    }
	    // current edgeList is finished now. If it is size() = 1 then append as it is.
	    // if size() >1 then do deep copy of edgePairList and then append.
	    if (edgeList.size() > 0){
	    	List<String[]> original = DeepClone.deepClone(curEdgePairList);
	    	// for the first list(0) of edgeList add it to existing edgePairList
	    	for (int i=0; i< edgeList.get(0).size();i++){
	    		String[] pair = edgeList.get(0).get(i);
	    		curEdgePairList.add(pair);
	    	}
	    	// if there are more then deepCopy edgePairList and append the new
	    	// potential combinations of edgeList starting from the second cell
	    	if (edgeList.size() > 1){
	    		// start from 1 and not 0 because for 0 we used first/secondPairList
	    		for (int k=1; k < edgeList.size();k++){
	    			List<String[]> temp = DeepClone.deepClone(original);
	    			for (int i=0; i < edgeList.get(k).size();i++){
	    				String[] pair = edgeList.get(0).get(i);
	    				temp.add(pair);
	    			}
	    			// append temp to edgePairList
	    			edgePairList.add(temp);
	    		}
	    		
	    	}
	    	else{
	    		if (edgePairList.isEmpty()){
	    			edgePairList.add(curEdgePairList);
	    		}
	    	}
	    	
	    }
	    // else size == 0 do nothing for edgePairList
	    
	    /**
	    if (edgeList.size() == 1){
	        for (int k=0; k< edgeList.get(0).size();k++){
	        	String[] pair = edgeList.get(0).get(k);
	        	curEdgePairList.add(pair); //TODO
	        }
	    }
	    else{
	        System.out.println("hello there from checkBefore");
	        //System.out.println(edgeList);
	    }
	    */
	    
	    List<List<List<String>>> newCheckings = new ArrayList<List<List<String>>>();
		newCheckings.add(newCheck1);
	    newCheckings.add(newCheck2);
	    return newCheckings;
	}

	/**
	 * This method returns all
	 * @param cLA1
	 * @param cLA2
	 * @return
	 */
	private Map<String, Map<String, List<List<String>>>> createCombinationsBetween2CLA(
			Map<Integer, Map<String, Map<Integer, List<String>>>> cLA1,
			Map<Integer, Map<String, Map<Integer, List<String>>>> cLA2) {
		// TODO Auto-generated method stub
		
		Map<Integer, List<String>> vComb1 = createVertexCombinations(cLA1);
		Map<Integer, List<String>> vComb2 = createVertexCombinations(cLA2);
		
		Map<String, Map<String, List<List<String>>>> combinations = 
				new HashMap<String, Map<String, List<List<String>>>>(); 
		for (Entry<Integer, List<String>> comb1 : vComb1.entrySet()){
			for (Entry<Integer, List<String>> comb2 : vComb2.entrySet()){
				Map<String, List<List<String>>> temp = 
						getListOfPossiblePairs(comb1.getValue(), comb2.getValue());
				combinations.put(comb1.getKey()+"-"+comb2.getKey(), temp);
				
				List<List<String>> firstList = 
						combinations.get(comb1.getKey()+"-"+comb2.getKey()).get("first");
				List<List<String>> secondList = 
						combinations.get(comb1.getKey()+"-"+comb2.getKey()).get("second");
	            List<List<String>> edgesFirst = new ArrayList<List<String>>();
	            List<List<String>> edgesSecond = new ArrayList<List<String>>();
	            for (int i=0; i< firstList.size();i++){
	            	List<String> first = firstList.get(i);
	            	List<String> second = secondList.get(i);
	            	
	            	List<String> curEdge1 = new ArrayList<String>();
	            	List<String> curEdge2 = new ArrayList<String>();
	                for (int j=0; j<first.size();j++){
	                	String f = first.get(j);
	                	String s = second.get(j);
	                	
	                    String[] beforeV = {f.split("-->")[0], s.split("-->")[0]};
	                    String[] afterV = {f.split("-->")[2], s.split("-->")[2]};
	                    String[] prevName = new String[2], nextName = new String[2];
	                    if ( !( (cLA1.get(comb1.getKey()).get("previous").containsKey(-1)) || 
	                    		(cLA2.get(comb2.getKey()).get("previous").containsKey(-1))) ){ 
	                    	prevName[0] = cLA1.get(comb1.getKey()).get("previous").get(Integer.parseInt(beforeV[0])).toString()+"-->";
	                    	prevName[1] = cLA2.get(comb2.getKey()).get("previous").get(Integer.parseInt(beforeV[1])).toString()+"-->";
	                    }
	                    else{ 
	                    	prevName[0] = "x-->";
	                    	prevName[1] = "x-->";
	                    }
	                    if (! ((cLA1.get(comb1.getKey()).get("next").containsKey(-1)) || 
	                    	   (cLA2.get(comb2.getKey()).get("next").containsKey(-1))) ){
	                        nextName[0] = cLA1.get(comb1.getKey()).get("next").get(Integer.parseInt(afterV[0])).toString(); 
	                        nextName[1] = cLA2.get(comb2.getKey()).get("next").get(Integer.parseInt(afterV[1])).toString();
	                       }
	                    else{
	                        nextName[0] = "x";
	                        nextName[1] = "x";
	                    }
	                    curEdge1.add(prevName[0]+nextName[0]);
	                    curEdge2.add(prevName[1]+nextName[1]);
	                }
	                edgesFirst.add(curEdge1);
	                edgesSecond.add(curEdge2);
	            }
	            combinations.get(comb1.getKey()+"-"+comb2.getKey()).put("edgesFirst", edgesFirst);
	            combinations.get(comb1.getKey()+"-"+comb2.getKey()).put("edgesSecond", edgesSecond);
				
			}
		}
		
		/*
	    for (Entry<String, Map<String, List<List<String>>>> c : 
	    	combinations.entrySet()){
	        System.out.println(c.getKey());
	        System.out.println("  first :" + c.getValue().get("first"));
	        System.out.println("  second:" + c.getValue().get("second"));
	        System.out.println("  edgesFirst :" + c.getValue().get("edgesFirst"));
	        System.out.println("  edgesFirst :" + c.getValue().get("edgesFirst"));
	    }
	    */
		return combinations;
	}
	
	/**
	 * this method gets 
	 * @param list1
	 * @param list2
	 * @return
	 */
	private Map<String, List<List<String>>> getListOfPossiblePairs(
			List<String> list1, List<String> list2) {
		// TODO Auto-generated method stub
		int len1 = list1.size();
		int len2 = list2.size();
		int minLen, maxLen;
		List<String> possibilities, result;
		String alias1, alias2;
		if (len1 < len2){
	        minLen = len1;
	        maxLen = len2;
	        //copy
	        possibilities = DeepClone.deepClone(list2);
	        result = list1;
	        alias1 = "second";
	        alias2 = "first";
		}
	    else{
	        minLen = len2;
	        maxLen = len1;
	        //copy
	        possibilities = DeepClone.deepClone(list1);
	        result = list2;
	        alias1 = "first";
	        alias2 = "second";
	    }
		List<Integer> tempNumbers = new ArrayList<Integer>();
		for (int i=0;i<minLen;i++){
			tempNumbers.add(maxLen-i);
		}
		tempNumbers.add(1);
		//System.out.println("tempNumbers");
		//System.out.println(tempNumbers);
		List<Integer> numbers = new ArrayList<Integer>();
		for (int i=0;i<minLen;i++) 
			numbers.add(0);
		
		for (int i=1;  i<tempNumbers.size();i++){
			
	        int mult = 1;
	        for (int j=i;j<tempNumbers.size();j++){ 
	        	mult = mult * tempNumbers.get(j);
	        }
	        numbers.set(i-1, mult);
		}
		
		int maxIter = 1;
		for (int i=0; i<tempNumbers.size()-1;i++){
			int x = tempNumbers.get(i);
			maxIter *= x;
		}
	    // pair = maxIter * minLen
		List<String> tempPair = new ArrayList<String>();
    	for (int j=0;j<minLen;j++){
    		tempPair.add(null);
    	}
	    List<List<String>> pair = new ArrayList<List<String>>();
	    for (int i=0;i<maxIter;i++){
	    	pair.add(new ArrayList<String>(tempPair));
	    }
	    
	    // on first call row must be 0!!! and curLen!!!
	    // rename row to startingRow
	    extendPairs(0, maxIter, numbers, minLen, 0, pair, possibilities);
		//for (List<String> p  : pair)
	    	//System.out.println(p+" :: "+ result);
		
		Map<String,List<List<String>>> returnMap = new HashMap<String,List<List<String>>>();
		returnMap.put(alias1, DeepClone.deepClone(pair));
		List<List<String>> tempResult = new ArrayList<List<String>>();
		for (int i=0;i<pair.size();i++)
			tempResult.add(DeepClone.deepClone(result));
		returnMap.put(alias2, DeepClone.deepClone(tempResult));
		return returnMap;
	}
	
	
	private void extendPairs(int row, int maxIter, 
			List<Integer> numbers, int minLen, int curLen, 
			List<List<String>> pair,
			List<String> possibilities) {
		// TODO Auto-generated method stub
		if (curLen != minLen){
			
			for (int i=0;i<maxIter;i++){
				int index = (int)(i/numbers.get(curLen));
				if (curLen == 0)
					pair.get(i).set(curLen, possibilities.get(index));
				else{
					pair.get(row+i).set(curLen, possibilities.get(index));
				}
				List<String> newPosibilities = new ArrayList<String>();
				for (int j=0; j<possibilities.size();j++)
					if (j != index)
	                    newPosibilities.add(possibilities.get(j));
				if (i%numbers.get(curLen) == 0)
					extendPairs(row + i, numbers.get(curLen), numbers, minLen, curLen +1,
	                     pair, newPosibilities);
			}
	    }
	}

	/**
	 * This method returns all the possible combinations of previous
	 * and next vertices passing through a specific vertex.
	 * @param cLA
	 * @return combination of vertices
	 */
	private Map<Integer, List<String>> createVertexCombinations(
			Map<Integer, Map<String, Map<Integer, List<String>>>> cLA) {
		// TODO Auto-generated method stub
		
		Map<Integer, List<String>> vComb = new HashMap<Integer, List<String>>();
		for (Entry<Integer, Map<String, Map<Integer, List<String>>>> c :cLA.entrySet()){

			vComb.put(c.getKey(), new ArrayList<String>() );
			Map<Integer, List<String>> next = c.getValue().get("next");
			Map<Integer, List<String>> previous = c.getValue().get("previous");
			if (next.isEmpty()){
				List<String> tempList = new ArrayList<String>();
				tempList.add("x");
				next.put(-1, tempList);
			}
			if (previous.isEmpty()){
				List<String> tempList = new ArrayList<String>();
				tempList.add("x");
				previous.put(-1, tempList);
			}
			for (Entry<Integer, List<String>> p : previous.entrySet()){
				for (Entry<Integer, List<String>> n : next.entrySet()){
					String name = p.getKey()+"-->"+c.getKey()+"-->"+n.getKey();
					
					vComb.get(c.getKey()).add(name);
				}
			}
	
		}
		//System.out.println(vComb);
		return vComb;
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
				//System.out.print(distanceMatrixPatterns.get(row).get(col)+"  ");
				System.out.printf("%.1f  ", distanceMatrixPatterns.get(row).get(col));
			}
			System.out.println("");
		}
		
	}
	
	/**
	 * this method prints the distanceMatrixPatterns
	 */
	public void printSimilarityMatrixPatterns() {
		// TODO Auto-generated method stub
		
		/**** print the grid******/
		System.out.println("\nThe Similarity Matrix of the patterns");
		System.out.print("    ");
		for (int num=0; num<similarityMatrixPatterns.size();num++){
			System.out.print("p"+num+"   ");
		}
		System.out.println("");
		for (int row=0;row<similarityMatrixPatterns.size();row++){
			System.out.print("p"+row+"  ");
			for (int col=0;col<similarityMatrixPatterns.get(row).size();col++){
				//System.out.print(similarityMatrixPatterns.get(row).get(col)+"  ");
				System.out.printf("%.1f  ", similarityMatrixPatterns.get(row).get(col));
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

	/**
	 * linkage in pattern distanceMatrixPatterns
	 * @param metric
	 */
	public void linkagePattern(linkMetric metric) {
		linkage("patterns", metric);
	}
	
	/**
	 * linkage in graph distanceMatrixGraphs
	 * @param metric
	 */
	public void linkageGraph(linkMetric metric) {
		linkage("graphs", metric);
	}
	
	/**
	 * this method performs linkage in the distanceMatrixPatterns or distanceMatrixGraphs
	 * depending on type
	 * @param type "pattern" or "graph" 
	 * @param metric
	 */
	private void linkage(String type, linkMetric metric) {
		// TODO Auto-generated method stub
		List<List<Double>> distanceMatrix;
		if (type.equals("patterns")){
			distanceMatrix = distanceMatrixPatterns;
		}
		else if (type.equals("graphs")){
			distanceMatrix = distanceMatrixGraphs;
		}
		else{
			distanceMatrix = null;
			System.exit(1);
		}
		List<String> rowCols = new ArrayList<String>(); 
		for (int i=0; i <distanceMatrix.size();i++) rowCols.add(type.substring(0, 1)+i);// "p" or "g"
		List<Double> linkageDistances = new ArrayList<Double>();
		List<List<String>> linkageClusters = new ArrayList<List<String>>();
		linkageDistances.add(0.0);
		linkageClusters.add(new ArrayList<String>(rowCols));
		
		switch (metric)
		{
		case MIN:
			
			while(distanceMatrix.size()>1){
				double min = Double.MAX_VALUE;
				int row = -1;
				int col = -1;
				for (int i=0; i<distanceMatrix.size();i++){
					// with j=i+1 we skip the main diagonal line
					for (int j=i+1; j<distanceMatrix.get(i).size();j++){
						if (distanceMatrix.get(i).get(j) < min){
							row = i;
							col = j;
							min = distanceMatrix.get(i).get(j);
						}
					}
				}
	
				String temp;
				int left;
				int right;
				
				// in the merge of two graphs put the result in the most left position
				// and remove the rightmost
				if (row<col){
					left = row;
					right = col;
				}
				else{
					left = col;
					right = row;
				}
				
				temp = rowCols.get(left);
				rowCols.set(left, temp+"-"+rowCols.get(right));
				rowCols.remove(right);
				
				// in the 2 above columns put the minimum value in each cell
				for (int i=0;i<distanceMatrix.size();i++){
					Double leftVal = distanceMatrix.get(i).get(left);
					Double rightVal = distanceMatrix.get(i).get(right);
					if (leftVal < rightVal){
						distanceMatrix.get(i).set(left, leftVal);
						distanceMatrix.get(i).set(right,leftVal);
					}
					else{
						distanceMatrix.get(i).set(left, rightVal);
						distanceMatrix.get(i).set(right,rightVal);
					}
				}
				linkageDistances.add(min);
				linkageClusters.add(new ArrayList<String>(rowCols));
				// remove the entire row
				distanceMatrix.remove(right);
				// remove the entire column
				for (int i=0;i<distanceMatrix.size();i++){
					distanceMatrix.get(i).remove(right);
				}
				
			}
			if (type.equals("patterns")){
				linkageDistancesPatterns = linkageDistances;
				linkageClustersPatterns = linkageClusters;
			}
			else if (type.equals("graphs")){
				linkageDistancesGraphs = linkageDistances;
				linkageClustersGraphs = linkageClusters;
			}
			
			
			break;
		case MAX:
			
			break;
		default:
			//TODO MIN??? MAX???
		}
	}

	/**
	 * this method prints the pattern clusters created from the linkage method (linkagePattern)
	 */
	public void printPatternClusters() {
		printClusters("patterns");
	}
	
	/**
	 * this method prints the graph clusters created from the linkage method (linkageGraph)
	 */
	public void printGraphClusters() {
		printClusters("graphs");
		
	}
	
	/**
	 * this method implements printPatternClusters and printGraphClusters
	 * It prints the linkageDistances and linkageClusters arrays
	 * @param type
	 */
	private void printClusters(String type) {
		// TODO Auto-generated method stub
		
		List<Double> linkageDistances;
		List<List<String>> linkageClusters;
		
		if (type.equals("patterns")){
			linkageDistances = linkageDistancesPatterns;
			linkageClusters = linkageClustersPatterns;
		}
		else if (type.equals("graphs")){
			linkageDistances = linkageDistancesGraphs;
			linkageClusters = linkageClustersGraphs;
		}
		else{
			linkageDistances = null;
			linkageClusters = null;
			System.exit(1);
		}
		System.out.println("\nThe clusters created for "+type);
		System.out.println("       Distance ::         Clusters");
		for (int i=0;i<linkageDistances.size();i++){
			System.out.print("lvl"+i+"-->  ");
			//System.out.println(linkageDistances.get(i)+" :: "+linkageClusters.get(i));
			System.out.printf("%.1f :: ", linkageDistances.get(i));
			System.out.println(linkageClusters.get(i));

		}
		
	}
	
}
