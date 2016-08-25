package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import main.jung.ClusteringAlgorithm;
import main.jung.GraphQueriesAPI;
import main.jung.MyEdge;

public class GetSimilarityScoreBetweenTwoPatternsTest {
	ClusteringAlgorithm clustAlg;
	GraphQueriesAPI gQAPI;
	List<DirectedGraph<Integer, MyEdge>> patternList;
	DirectedGraph<Integer, MyEdge> pattern1;
	DirectedGraph<Integer, MyEdge> pattern2;
	
	
	/**
	 * Sets up the test fixture. 
	 * (Called before every test case method.)
	 */
	@Before
	public void setUp() {
		pattern1 = new DirectedSparseMultigraph<Integer, MyEdge>();
		pattern2 = new DirectedSparseMultigraph<Integer, MyEdge>();
		patternList = new ArrayList<DirectedGraph<Integer, MyEdge>>();
		patternList.add(pattern1);
		patternList.add(pattern2);
	}
	
	/**
	 * Tears down the test fixture. 
	 * (Called after every test case method.)
	 */
	@After
	public void tearDown() {
		gQAPI = null;
		clustAlg = null;
		pattern1 = null;
		pattern2 = null;
	}
	
	@Test
	public void sameGraphs1() {
		// erase that. sameGraphs2 is more complete
		/*********pattern1***********/
		TestingUtils.addEdge(pattern1, "1.1.1.1", 0, 1);
		TestingUtils.addEdge(pattern1, "3.3.3.3", 0, 3);
		TestingUtils.addEdge(pattern1, "2.2.2.2", 1, 2);
		/*********pattern2***********/
		TestingUtils.addEdge(pattern2, "1.1.1.1", 5, 6);
		TestingUtils.addEdge(pattern2, "3.3.3.3", 5, 8);
		TestingUtils.addEdge(pattern2, "2.2.2.2", 6, 7);
		
		//This has to be done every time
		gQAPI = new GraphQueriesAPI(patternList);
		clustAlg = new ClusteringAlgorithm(gQAPI);
		assertEquals(clustAlg.getSimilarityScoreBetweenTwoPatterns(pattern1, pattern2), 1.0, 0.0001);
	}

	@Test
	public void sameGraphs2() {
		/*********pattern1***********/
		TestingUtils.addEdge(pattern1, "1.1.1.1", 0, 1);
		TestingUtils.addEdge(pattern1, "3.3.3.3", 0, 3);
		TestingUtils.addEdge(pattern1, "2.2.2.2", 1, 2);
		TestingUtils.addEdge(pattern1, "4.4.4.4", 1, 4);
		/*********pattern2***********/
		TestingUtils.addEdge(pattern2, "1.1.1.1", 5, 6);
		TestingUtils.addEdge(pattern2, "3.3.3.3", 5, 8);
		TestingUtils.addEdge(pattern2, "2.2.2.2", 6, 7);
		TestingUtils.addEdge(pattern2, "4.4.4.4", 6, 9);
		
		//This has to be done every time
		gQAPI = new GraphQueriesAPI(patternList);
		clustAlg = new ClusteringAlgorithm(gQAPI);
		assertEquals(clustAlg.getSimilarityScoreBetweenTwoPatterns(pattern1, pattern2), 1.0, 0.0001);
	}
	
	@Test
	public void sameGraphsDiffdirection() {
		/*********pattern1***********/
		TestingUtils.addEdge(pattern1, "1.1.1.1", 1, 0);
		TestingUtils.addEdge(pattern1, "2.2.2.2", 2, 1);
		TestingUtils.addEdge(pattern1, "3.3.3.3", 3, 0);
		/*********pattern2***********/
		TestingUtils.addEdge(pattern2, "1.1.1.1", 5, 6);
		TestingUtils.addEdge(pattern2, "2.2.2.2", 6, 7);
		TestingUtils.addEdge(pattern2, "3.3.3.3", 5, 8);
		
		//This has to be done every time
		gQAPI = new GraphQueriesAPI(patternList);
		clustAlg = new ClusteringAlgorithm(gQAPI);
		assertEquals(clustAlg.getSimilarityScoreBetweenTwoPatterns(pattern1, pattern2), 0.333333, 0.0001);
	}
	
	@Test
	public void difGraphs66Percent() {
		/*********pattern1***********/
		TestingUtils.addEdge(pattern1, "1.1.1.1", 0, 1);
		TestingUtils.addEdge(pattern1, "2.2.2.2", 1, 2);
		TestingUtils.addEdge(pattern1, "4.4.4.4", 0, 3);
		/*********pattern2***********/
		TestingUtils.addEdge(pattern2, "1.1.1.1", 5, 6);
		TestingUtils.addEdge(pattern2, "2.2.2.2", 6, 7);
		TestingUtils.addEdge(pattern2, "3.3.3.3", 5, 8);
		
		//This has to be done every time
		gQAPI = new GraphQueriesAPI(patternList);
		clustAlg = new ClusteringAlgorithm(gQAPI);
		
		//basically ==2/3
		assertEquals(clustAlg.getSimilarityScoreBetweenTwoPatterns(pattern1, pattern2), 0.666666, 0.0001);

	}
	
	@Test
	public void difGraphs625Percent() {
		/*********pattern1***********/
		TestingUtils.addEdge(pattern1, "1.1.1.1", 0, 1);
		TestingUtils.addEdge(pattern1, "2.2.2.2", 1, 2);
		TestingUtils.addEdge(pattern1, "4.4.4.4", 0, 3);
		TestingUtils.addEdge(pattern1, "5.5.3.3", 3, 4);
		/*********pattern2***********/
		TestingUtils.addEdge(pattern2, "1.1.1.1", 5, 6);
		TestingUtils.addEdge(pattern2, "2.2.2.2", 6, 7);
		TestingUtils.addEdge(pattern2, "3.3.3.3", 5, 8);
		TestingUtils.addEdge(pattern2, "5.5.5.5", 8, 9);
		//This has to be done every time
		gQAPI = new GraphQueriesAPI(patternList);
		clustAlg = new ClusteringAlgorithm(gQAPI);
		
		// now they are 1/4 + 1/4 + 0 + 1/4*0.5 = 0,625
		assertEquals(clustAlg.getSimilarityScoreBetweenTwoPatterns(pattern1, pattern2), 0.625, 0.0001);
	}
	
	@Test
	public void allNullOptions() {
		//This has to be done every time
		gQAPI = new GraphQueriesAPI(patternList);
		clustAlg = new ClusteringAlgorithm(gQAPI);
		assertEquals(clustAlg.getSimilarityScoreBetweenTwoPatterns(pattern1, null), 0.0, 0.0);
		assertEquals(clustAlg.getSimilarityScoreBetweenTwoPatterns(null, pattern2), 0.0, 0.0);
		assertEquals(clustAlg.getSimilarityScoreBetweenTwoPatterns(null, null), 0.0, 0.0);
	}
}

