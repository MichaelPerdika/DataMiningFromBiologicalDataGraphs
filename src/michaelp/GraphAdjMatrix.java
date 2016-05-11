package michaelp;
import java.util.ArrayList;
import java.util.List;

public class GraphAdjMatrix extends Graph{
private int[][] adjMatrix;
	
	// constructor, methods update values of adjMatrix
	/**
	 * v is startPoint vertex
	 * w is endPoint vertex
	 * */
	public void implementAddEdge(int v, int w){
		adjMatrix[v][w] = 1;
	}
	
	public void implementAddVertex(){
		int v = getNumVertices();
		if (v >= adjMatrix.length){
			int[][] newAdjMatrix = new int[v*2][v*2];
			for (int i=0; i<adjMatrix.length; i++){
				for (int j=0; j<adjMatrix.length; j++){
					newAdjMatrix[i][j] = adjMatrix[i][j];
				}
			}
			adjMatrix = newAdjMatrix;
		}
		for (int i=0; i<adjMatrix[v].length; i++){
			adjMatrix[v][i] = 0;
		}
	}
	
	public List<Integer> getNeighbors(int v){
		List<Integer> neighbors = new ArrayList<Integer>();
		for (int i =0; i <getNumVertices(); i++){
			//if (!adjMatrix[v][i]) neighbors.add(i);// i check if !=0 because it could be 1,2 or w/e
			for (int j=0; j< adjMatrix[v][i]; j++) neighbors.add(i); // this is correct if values >1 are possible.
		}
		return neighbors;
	}

	@Override
	public List<Integer> getInNeighbors(int v) {
		// TODO Auto-generated method stub
		return null;
	}
}
