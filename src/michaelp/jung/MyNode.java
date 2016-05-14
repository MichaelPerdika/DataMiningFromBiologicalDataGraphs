package michaelp.jung;

public class MyNode {
	static private int numOfNodes = 0;
	private int nodeId; 
	
	public MyNode(){
		this.nodeId = numOfNodes++;
	}
	
	public MyNode(int id){
		this.nodeId = id;
		numOfNodes++;
		}
	
	static public int getNumOfEdges(){
		return numOfNodes;
	}
	
	public int getNodeId(){
		return this.nodeId;
	}
	
}
