package michaelp;
import java.util.List;

public abstract class Graph {
	private int numVertices;
	private int numEdges;

	public Graph(){
	numVertices = 0;
	numEdges = 0;
	}

	public int getNumVertices(){
		return numVertices;
	}
	
	public int getNumEdges(){
		return numEdges;
	}
	
	public abstract void implementAddVertex();
	
	public abstract void implementAddEdge(int v, int w);
	
	/**
	 * get the neighbors that v points to.
	 */
	public abstract List<Integer> getNeighbors(int v);
	
	/**
	 * get the neighbors that point to v.
	 */
	public abstract List<Integer> getInNeighbors(int v);
	


}
