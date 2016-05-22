package main.jung;

public class MyNode {
	private int nodeId; 
	
	public MyNode(){
	}
	
	public MyNode(int id){
		this.nodeId = id;
		}
	
	public int getNodeId(){
		return this.nodeId;
	}
	
	public String toString() {
		
        return "V"+nodeId;
    }
}
