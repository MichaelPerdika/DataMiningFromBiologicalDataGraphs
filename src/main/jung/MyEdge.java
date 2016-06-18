package main.jung;

import java.util.Map;
import java.util.Map.Entry;

public class MyEdge {
	private int edgeId;
	private String edgeRDFid;
	private String[] startNodes;
	private String[] endNodes;
	private String[] eCNumber;
	private String edgeName;
	private String[] nextStepRDFids;
	private String[] stepConversion;
	private Integer startNode;
	private Integer endNode;
	
	/**
	 * this constructor is used only for testing
	 * it has to be erased later on!!!!!!!!
	 * @param edge 
	 * @param startNode
	 * @param endNode
	 */
	public MyEdge(String edgeName, Integer startNode, Integer endNode){
		//TODO erase this constructor.
		this.edgeRDFid = edgeName;
		this.startNodes = null;
		this.endNodes = null;
		this.eCNumber = new String[] {edgeName};
		this.edgeName = edgeName;
		this.nextStepRDFids = null;
		this.stepConversion = null;
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	/**
	 * this constructor is used in method getGraphListWithNewEdge.
	 * @param edge 
	 * @param startNode
	 * @param endNode
	 */
	public MyEdge(MyEdge edge, Integer startNode, Integer endNode){
		//This function gets a copy edge but with different start and end Nodes
		this.edgeRDFid = edge.getEdgeRDFid();
		this.startNodes = edge.startNodes;
		this.endNodes = edge.endNodes;
		this.eCNumber = edge.getECNumber();
		this.edgeName = edge.getEdgeName();
		this.nextStepRDFids = edge.getNextStepRDFids();
		this.stepConversion = edge.stepConversion;
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	/**
	 * this constructor is used only for testing
	 * I can keep it because is used in getMyEdgeFromThreshold
	 * @param edge 
	 * @param startNode
	 * @param endNode
	 */
	public MyEdge(String[] edgeName, Integer startNode, Integer endNode){
		//TODO erase this constructor.
		this.edgeRDFid = edgeName[0];
		this.startNodes = null;
		this.endNodes = null;
		this.eCNumber = edgeName;
		this.edgeName = edgeName[0];
		this.nextStepRDFids = null;
		this.stepConversion = null;
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	/**
	 * this constructor takes a BioPaxGraph edge entry and converts it to MyEdge
	 * @param entry
	 */
	public MyEdge(Entry<String, Map<String, String[]>> entry) {
		this.edgeRDFid = entry.getKey();
		this.startNodes = entry.getValue().get("startNodes");
		this.endNodes = entry.getValue().get("endNodes");
		this.eCNumber = entry.getValue().get("eCNumber");
		this.edgeName = entry.getValue().get("edgeName")[0];
		this.nextStepRDFids = entry.getValue().get("nextStep");
		this.stepConversion = entry.getValue().get("stepConversion");
		
	}
	
	@Override
	public String toString() {
		//TODO
		String temp = "";
		for (String t : eCNumber){
			temp += t+" ";
		}
		return temp;
        /*
		String temp = "[";
		for (String t : eCNumber){
			temp += t+" ";
		}
		temp += "]";
		return temp
		*/
    }

	public String getEdgeRDFid() {
		return this.edgeRDFid;
	}
	
	public String[] getNextStepRDFids(){
		return this.nextStepRDFids;
	}
	
	public String[] getECNumber(){
		return this.eCNumber;
	}
	
	public String getEdgeName(){
		return this.edgeName;
	}
	
	public Integer getStartNode(){
		return this.startNode;
	}
	
	public Integer getEndNode(){
		return this.endNode;
	}
	
	public void setEdgeNodes(Integer startNode, Integer endNode){
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	/**
	 * as long as they have the same name. Don't worry for start and end nodes
	 */
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof MyEdge))return false;
	    MyEdge otherMyEdge = (MyEdge)other;
	    if (this.edgeName.equals(otherMyEdge.getEdgeName())) return true;
		return false;
	}
	
	/**
	 * same as equals but here we want the edges to be identical
	 */
	public boolean isIdentical(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof MyEdge))return false;
	    MyEdge otherMyEdge = (MyEdge)other;
	    if (this.edgeName.equals(otherMyEdge.getEdgeName())
	    		&& this.startNode == otherMyEdge.getStartNode()
	    		&& this.endNode == otherMyEdge.getEndNode()) return true;
		return false;
	}
}
