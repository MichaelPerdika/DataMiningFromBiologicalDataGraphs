package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import main.jung.GraphQueriesAPI;
import main.jung.MyEdge;

public class ParseEdgeNamesTest {
	GraphQueriesAPI gQueris;
	MyEdge myEdge;
	
	
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
		myEdge = null;
		gQueris = null;
	}
	
	@Test
	public void oneNameEqual() {
		List<String> s = new ArrayList<String>();
		s.add("2");	s.add("3");	s.add("3");	s.add("1");
		myEdge = new MyEdge("2.3.3.1", 0, 1);
		assertTrue(TestingUtils.equalLists(gQueris.parseEdgeNames(myEdge).get(0), s));
	}
	
	@Test
	public void twoNamesEqual() {
		List<List<String>> listS = new ArrayList<List<String>>();
		List<String> s = new ArrayList<String>();
		s.add("2");	s.add("3");	s.add("3");	s.add("1");
		listS.add(s);
		s = new ArrayList<String>();
		s.add("4");	s.add("1");	s.add("2");	s.add("3");
		listS.add(s);
		myEdge = new MyEdge(new String[] {"2.3.3.1", "4.1.2.3"}, 0, 1);
		assertTrue(TestingUtils.equalLists(gQueris.parseEdgeNames(myEdge).get(0), listS.get(0)));
		assertTrue(TestingUtils.equalLists(gQueris.parseEdgeNames(myEdge).get(1), listS.get(1)));
		
	}
	
	@Test
	public void oneNameNonEqual() {
		List<String> s = new ArrayList<String>();
		s.add("2");	s.add("3");	s.add("3");	s.add("4");
		myEdge = new MyEdge("2.3.3.1", 0, 1);
		assertFalse(TestingUtils.equalLists(gQueris.parseEdgeNames(myEdge).get(0), s));
	}
	
	@Test
	public void twoNamesNonEqual() {
		List<List<String>> listS = new ArrayList<List<String>>();
		List<String> s = new ArrayList<String>();
		s.add("2");	s.add("3");	s.add("3");	s.add("1");
		listS.add(s);
		s = new ArrayList<String>();
		s.add("4");	s.add("1");	s.add("2");	s.add("4");
		listS.add(s);
		myEdge = new MyEdge(new String[] {"2.3.3.1", "4.1.2.3"}, 0, 1);
		assertTrue(TestingUtils.equalLists(gQueris.parseEdgeNames(myEdge).get(0), listS.get(0)));
		assertFalse(TestingUtils.equalLists(gQueris.parseEdgeNames(myEdge).get(1), listS.get(1)));
		
	}
	
	@Test
	public void twoEmptyStrings() {
		List<String> s = new ArrayList<String>();
		myEdge = new MyEdge("", 0, 1);
		System.out.println(s);
		System.out.println(gQueris.parseEdgeNames(myEdge).get(0));
		assertTrue(TestingUtils.equalLists(gQueris.parseEdgeNames(myEdge).get(0), s));
	}
}
