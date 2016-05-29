package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.*;

import main.jung.GraphQueriesAPI;

public class EqualListsTest {


	GraphQueriesAPI gQueris;
	List<String> list1; 
	List<String> list2; 
	
	
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
		gQueris = null;
		list1 = null;
		list2 = null;
	}
	
	@Test
	public void exactlySame() {
		list1 = TestingUtils.quickArray("a","b");
		list2 = TestingUtils.quickArray("a","b");
		assertTrue(gQueris.equalLists(list1, list2));
	}
	
	@Test
	public void sameWithDifferentOrder() {
		list1 = TestingUtils.quickArray("b","a");
		list2 = TestingUtils.quickArray("a","b");
		assertTrue(gQueris.equalLists(list1, list2));
	}
	
	@Test
	public void allNullOptions() {
		list1 = TestingUtils.quickArray("a");
		assertTrue(gQueris.equalLists(null, null));
		assertFalse(gQueris.equalLists(list1, null));
		assertFalse(gQueris.equalLists(null, list1));
	}
	
	@Test
	public void differentSize(){
		list1 = TestingUtils.quickArray("a","b");
		list2 = TestingUtils.quickArray("a");
		assertFalse(gQueris.equalLists(list1, list2));
	}
}
