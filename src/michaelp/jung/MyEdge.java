package michaelp.jung;

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
        
		String temp = "[";
		for (String t : eCNumber){
			temp += t+" ";
		}
		temp += "]";
		return temp; //eCNumber
		//return edgeName.substring(1,edgeName.length()-1);//remove the []
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
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof MyEdge))return false;
	    MyEdge otherMyEdge = (MyEdge)other;
	    if (this.edgeName.equals(otherMyEdge.getEdgeName())) return true;
		return false;
	}
}
