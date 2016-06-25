package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.jung.GraphQueriesAPI;
import main.jung.MyEdge;

public class GetCommonEdgeFromThresholdTest {

	GraphQueriesAPI gQueris;
	MyEdge edge1;
	MyEdge edge2;
	MyEdge resultEdge;
	
	
	/**
	 * Sets up the test fixture. 
	 * (Called before every test case method.)
	 */
	@Before
	public void setUp() {
		gQueris = new GraphQueriesAPI();
	}
	
	/**
	 * Tears down the test fixture. 
	 * (Called after every test case method.)
	 */
	@After
	public void tearDown() {
		edge1 = null;
		edge2 = null;
		resultEdge = null;
		gQueris = null;
	}
	
	@Test
	public void oneNameEqual100() {
		edge1 = new MyEdge("2.3.3.1", 0, 1);
		edge2 = new MyEdge("2.3.3.1", 0, 1);
		resultEdge = new MyEdge("2.3.3.1", 0, 1);
		assertTrue(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 1)));
		assertFalse(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0.6)));
		assertFalse(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0.25)));
		assertFalse(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0)));
	}
	
	@Test
	public void oneNameEqual50() {
		edge1 = new MyEdge("2.3.3.1", 0, 1);
		edge2 = new MyEdge("2.3.5.5", 0, 1);
		resultEdge = new MyEdge("2.3.*", 0, 1);
		assertFalse(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 1)));
		assertFalse(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0.75)));
		assertTrue(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0.5)));
		assertFalse(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0.25)));
		assertFalse(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0)));
	}
	
	@Test
	public void twoNameEqual100() {
		edge1 = new MyEdge(new String[] {"2.3.3.1", "4.1.1.1"}, 0, 1);
		edge2 = new MyEdge(new String[] {"2.3.3.1", "4.1.1.1"}, 0, 1);
		resultEdge = new MyEdge(new String[] {"2.3.3.1", "4.1.1.1"}, 0, 1);
		assertTrue(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 1)));
		assertFalse(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0.5)));
		assertFalse(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0)));
	}
	
	@Test
	public void twoNameEqual50case1() {
		edge1 = new MyEdge(new String[] {"2.3.3.1", "4.1.1.1"}, 0, 1);
		edge2 = new MyEdge(new String[] {"2.3.3.3"}, 0, 1);
		resultEdge = new MyEdge("2.3.3.*", 0, 1);
		// 0.75 * 1/2 + 0*1/2 = 0.375
		assertFalse(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0.376)));
		assertTrue(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0.375)));
		assertFalse(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0)));
	}
	
	@Test
	public void twoNameEqual50case2() {
		edge1 = new MyEdge(new String[] {"2.3.5.5", "2.3.3.4"}, 0, 1);
		edge2 = new MyEdge(new String[] {"2.3.3.1"}, 0, 1);
		resultEdge = new MyEdge(new String[] {"2.3.*", "2.3.3.*"}, 0, 1);
		// 0.5 *1/2 + 0.75 * 1/2 = 0.625
		assertFalse(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0.63)));
		assertTrue(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0.625)));
		assertFalse(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0)));
	}
	
	@Test
	public void twoNameEqual50case3() {
		edge1 = new MyEdge(new String[] {"2.3.5.5", "4.4.4.4"}, 0, 1);
		edge2 = new MyEdge(new String[] {"2.3.3.1", "4.4.1.1"}, 0, 1);
		resultEdge = new MyEdge(new String[] {"2.3.*", "4.4.*"}, 0, 1);
		assertFalse(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0.6)));
		assertTrue(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0.5)));
		assertFalse(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0)));
	}
	
	@Test
	public void oneNameIsEmpty() {
		edge1 = new MyEdge(new String[] {"2.3.3.1", "4.1.1.1"}, 0, 1);
		edge2 = new MyEdge("", 0, 1);
		assertTrue(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 1) == null);
		assertTrue(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0.5) == null);
		assertTrue(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0.1) == null);
		resultEdge = new MyEdge(" ", 0, 1);
		assertFalse(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0)));
		resultEdge = new MyEdge("", 0, 1);
		assertTrue(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0)));
	}
	
	@Test
	public void bothNamesEmpty() {
		edge1 = new MyEdge("", 0, 1);
		edge2 = new MyEdge("", 0, 1);
		
		resultEdge = new MyEdge("", 0, 1);
		assertTrue(null == (GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 1)));
		//assertFalse(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0.5)));
		//assertFalse(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0)));
		resultEdge = new MyEdge(" ", 0, 1);
		//assertFalse(resultEdge.isIdentical(GraphQueriesAPI.getCommonEdgeFromThreshold(edge1, edge2, 0.5)));
		
	}
}
