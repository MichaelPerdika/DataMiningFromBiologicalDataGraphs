package michaelp.jung;

import java.util.Map;
import java.util.Map.Entry;

public class MyEdge {
	static private int numOfEgdes = 0;
	private int edgeId;
	private String edgeRDFid;
	private String[] startNodes;
	private String[] endNodes;
	private String[] eCNumber;
	private String edgeName;
	private String[] nextStepRDFids;
	private String[] stepConversion;
	private MyNode startNode;
	private MyNode endNode;
	

	/**
	 * this constructor takes a BioPaxGraph edge entry and converts it to MyEdge
	 * @param entry
	 */
	public MyEdge(Entry<String, Map<String, String[]>> entry) {
		numOfEgdes++;
		this.edgeRDFid = entry.getKey();
		this.startNodes = entry.getValue().get("startNodes");
		this.endNodes = entry.getValue().get("endNodes");
		this.eCNumber = entry.getValue().get("eCNumber");
		this.edgeName = entry.getValue().get("edgeName")[0];
		this.nextStepRDFids = entry.getValue().get("nextStep");
		this.stepConversion = entry.getValue().get("stepConversion");
		
	}
	
	public String toString() {
		//TODO
        return "E"+edgeId;
    }
	
	public int getNumOfEdges(){
		return numOfEgdes;
	}

	public String getEdgeRDFid() {
		return this.edgeRDFid;
	}
	
	public String[] getNextStepRDFids(){
		return this.nextStepRDFids;
	}
	
	public void setEdgeNodes(MyNode startNode, MyNode endNode){
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public String getEdgeName(){
		return this.edgeName;
	}
	
	public MyNode getStartNode(){
		return this.startNode;
	}
	
	public MyNode getEndNode(){
		return this.endNode;
	}
}
