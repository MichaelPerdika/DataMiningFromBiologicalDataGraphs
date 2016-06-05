package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.uci.ics.jung.graph.DirectedGraph;
import main.jung.MyEdge;

public class TestingUtils {

	public static void addEdge(DirectedGraph<Integer, MyEdge> graph, 
			String edge, Integer startNode, Integer endNode) {
		graph.addEdge(new MyEdge(edge, startNode, endNode), startNode, endNode);
		
	}
	
	public static List<String> quickArray(String ... strings) {
		List<String> tempList = new ArrayList<String>();
		for (String s : strings){
			tempList.add(s);
		}
		return tempList;
	}
	
	public static boolean equalLists(List<String> one, List<String> two){     
	    if (one == null && two == null){
	        return true;
	    }
	    if((one == null && two != null) 
	      || one != null && two == null
	      || one.size() != two.size()){
	    	System.out.println(one.size());
	    	System.out.println(two.size());

	        return false;
	    }
	    if (one.size() == two.size() && one.size()==0){
	    	
	    	return true;
	    }
	    //to avoid messing the order of the lists we will use a copy
	    //as noted in comments by A. R. S.
	    one = new ArrayList<String>(one); 
	    two = new ArrayList<String>(two);   

	    Collections.sort(one);
	    Collections.sort(two);      
	    return one.equals(two);
	}
}
