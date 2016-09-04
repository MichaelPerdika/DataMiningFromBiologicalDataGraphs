package main.jung;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MyEdge {
	private String edgeRDFid;
	private String[] startNodesRDFids;
	private String[] endNodesRDFids;
	private List<String> startNodeNames;
	private List<String> endNodeNames;
	private String[] eCNumber;
	private String edgeName;
	private String[] nextStepRDFids;
	private String[] stepConversion;
	private Integer startNode;
	private Integer endNode;
	private String stepDirection;
	
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
		this.startNodesRDFids = null;
		this.endNodesRDFids = null;
		this.eCNumber = new String[] {edgeName};
		this.edgeName = edgeName;
		this.nextStepRDFids = null;
		this.stepConversion = null;
		this.startNode = startNode;
		this.endNode = endNode;
		this.stepDirection = null;
		fillStartAndEndNames();
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
		this.startNodesRDFids = edge.startNodesRDFids;
		this.endNodesRDFids = edge.endNodesRDFids;
		this.eCNumber = edge.getECNumber();
		this.edgeName = edge.getEdgeName();
		this.nextStepRDFids = edge.getNextStepRDFids();
		this.stepConversion = edge.stepConversion;
		this.startNode = startNode;
		this.endNode = endNode;
		this.stepDirection = edge.getStepDirection();
		fillStartAndEndNames();
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
		this.startNodesRDFids = null;
		this.endNodesRDFids = null;
		this.eCNumber = edgeName;
		this.edgeName = edgeName[0];
		this.nextStepRDFids = null;
		this.stepConversion = null;
		this.startNode = startNode;
		this.endNode = endNode;
		this.stepDirection = null;
		fillStartAndEndNames();
	}
	
	/**
	 * this constructor takes a BioPaxGraph edge entry and converts it to MyEdge
	 * @param entry
	 */
	public MyEdge(Entry<String, Map<String, String[]>> entry) {
		this.edgeRDFid = entry.getKey();
		this.startNodesRDFids = entry.getValue().get("startNodes");
		this.endNodesRDFids = entry.getValue().get("endNodes");
		this.eCNumber = entry.getValue().get("eCNumber");
		this.edgeName = entry.getValue().get("edgeName")[0];
		this.nextStepRDFids = entry.getValue().get("nextStep");
		this.stepConversion = entry.getValue().get("stepConversion");
		this.stepDirection = entry.getValue().get("stepDirection")[0];
		fillStartAndEndNames();
	}
	
	private void fillStartAndEndNames() {
		List<String> start = new ArrayList<String>();
		List<String> end = new ArrayList<String>();
		
		if (this.stepDirection != null){
			// remove the [ and ] from the beginning and end.
			String name = edgeName.substring(1, edgeName.length()-1);
			
			// remove the " &rarr; " or " &harr; " etc.
			// regular expression for " &(anyCharacter4Times); "
			// \\h --> a horizontal whitspace
			// \\Q&\\E --> &
			// .{4} --> any character four times
			// //Q;//E --> ;
			String[] leftAndRight = name.split("\\h\\Q&\\E.{4}\\Q;\\E\\h");
			
			if (leftAndRight.length != 2){
				System.out.println("ERROR in fillStartAndEndNames/MyEdge, "
						+ "leftAndRight has no length=2. It has: "
						+leftAndRight.length+" with values:");
				for (int i=0;i<leftAndRight.length;i++){
					System.out.println(i+"-->"+leftAndRight[i]);
				}
				System.exit(1);
			}
			
			String from = null, to = null;
			if (stepDirection.equals("LEFT_TO_RIGHT")){
				from = leftAndRight[0];
				to = leftAndRight[1];
			}
			else if (stepDirection.equals("RIGHT_TO_LEFT")){
				from = leftAndRight[1];
				to = leftAndRight[0];
			}
			else{
				System.out.println("ERROR in fillStartAndEndNodes/MyEdge, stepDirection is: "+stepDirection);
				System.exit(1);
			}
			
			// add to startNodesNames the names split with " + "
			for (String f : from.split("\\h\\Q+\\E\\h")){
				start.add(f);
			}
			// add to endNodesNames the names split with " + "
			for (String t : to.split("\\h\\Q+\\E\\h")){
				end.add(t);
			}
			
		}
		
		this.startNodeNames = start;
		this.endNodeNames = end;

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

	public List<String> getStartNodeNames() {
		return startNodeNames;
	}

	public List<String> getEndNodeNames() {
		return endNodeNames;
	}

	public String getStepDirection(){
		return stepDirection;
	}
	
	public void setECNumber(String name){
		eCNumber = new String[] {name};;
	}
}
