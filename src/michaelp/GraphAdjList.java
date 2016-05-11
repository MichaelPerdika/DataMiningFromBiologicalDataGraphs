package michaelp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphAdjList extends Graph{
	private Map<Integer, ArrayList<Integer>> adjListMap;
	
	@Override
	public void implementAddVertex(){
		int v = getNumVertices();
		ArrayList<Integer> neighbors = new ArrayList<Integer>();
		adjListMap.put(v, neighbors);
		//to parapanw na mpei se try except. Giati mporei na uparxei hdh to vertex
	}
	
	@Override
	public void implementAddEdge(int v, int w){
		(adjListMap.get(v)).add(w);
		// kai auto se try except kai gia to v kai to w????
	}
	
	@Override
	public List<Integer> getNeighbors(int v){
		//return adjListMap.get(v); //it can't because we return the exact List. we need to return a copy
		return new ArrayList<Integer>(adjListMap.get(v));
	}

	@Override
	public List<Integer> getInNeighbors(int v) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
