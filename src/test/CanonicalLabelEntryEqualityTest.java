package test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.*;

import main.jung.GraphQueriesAPI;

public class CanonicalLabelEntryEqualityTest {

	GraphQueriesAPI gQueris;
	Map<Integer, List<String>> entry1; 
	Map<Integer, List<String>> entry2;
	
	
	/**
	 * Sets up the test fixture. 
	 * (Called before every test case method.)
	 */
	@Before
	public void setUp() {
		gQueris = new GraphQueriesAPI();
		entry1 = new HashMap<Integer, List<String>>();
		entry2 = new HashMap<Integer, List<String>>();
		/************entry1****************/
		entry1.put(0, TestingUtils.quickArray("a", "m"));
		entry1.put(1, TestingUtils.quickArray("b"));
		entry1.put(2, TestingUtils.quickArray("c","x","y"));
		/************entry2****************/
		entry2.put(44, TestingUtils.quickArray("a", "m"));
		entry2.put(11, TestingUtils.quickArray("b"));
		entry2.put(22, TestingUtils.quickArray("c","x","y"));
	}
	
	/**
	 * Tears down the test fixture. 
	 * (Called after every test case method.)
	 */
	@After
	public void tearDown() {
		entry1 = null;
		entry2 = null;
		gQueris = null;
	}
	
	@Test
	public void differentSize() {
		entry2.remove(11);
		/** entry1 = {0 :[a, m], 1:[b], 2 :[c, x, y]} 
		 *  entry2 = {44:[a, m],        22:[c, x, y]}**/
		assertFalse("different size", gQueris.canonicalLabelEntryEquality(entry1, entry2));
	}
	
	@Test
	public void emptyEntries() {
		entry1 = new HashMap<Integer, List<String>>();
		entry2 = new HashMap<Integer, List<String>>();
		/** entry1 = {} 
		 *  entry2 = {}**/
		assertTrue("different size", gQueris.canonicalLabelEntryEquality(entry1, entry2));
	}
	
	@Test
	public void sameMaps() {
		//System.out.println(entry1);
		//System.out.println(entry2);
		/** entry1 = {0 :[a, m], 1: [b], 2 :[c, x, y]} 
		 *  entry2 = {44:[a, m], 10:[b], 22:[c, x, y]}**/
		assertTrue("same maps",gQueris.canonicalLabelEntryEquality(entry1, entry2));
	}
		
	@Test
	public void allSameExeptOneMissingEdge() {
		entry2.get(44).remove(1);
		/** entry1 = {0 :[a, m], 1: [b], 2 :[c, x, y]} 
		 *  entry2 = {44:[a]   , 10:[b], 22:[c, x, y]}**/
		assertFalse("all same except one vertex misses an edge",gQueris.canonicalLabelEntryEquality(entry1, entry2));
	}
	
	@Test
	public void atLeastOneAlteredEdge(){
		entry2.get(44).remove(1);
		entry2.get(44).add(1, "x");
		/** entry1 = {0 :[a, m], 1: [b], 2 :[c, x, y]} 
		 *  entry2 = {44:[a, x], 10:[b], 22:[c, x, y]}**/
		assertFalse("all same except one edge name differs",gQueris.canonicalLabelEntryEquality(entry1, entry2));
	
	}
	
	@Test
	public void allNullOptions() {
		assertFalse(gQueris.canonicalLabelEntryEquality(entry1, null));
		assertFalse(gQueris.canonicalLabelEntryEquality(null, entry1));
		assertTrue(gQueris.canonicalLabelEntryEquality(null, null));

	}
	

	

}


