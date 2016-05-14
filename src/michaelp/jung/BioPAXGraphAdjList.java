package michaelp.jung;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.biopax.paxtools.controller.EditorMap;
import org.biopax.paxtools.controller.PropertyEditor;
import org.biopax.paxtools.controller.SimpleEditorMap;
import org.biopax.paxtools.io.BioPAXIOHandler;
import org.biopax.paxtools.io.SimpleIOHandler;
import org.biopax.paxtools.model.BioPAXElement;
import org.biopax.paxtools.model.Model;
import org.biopax.paxtools.model.level3.BiochemicalPathwayStep;
import org.biopax.paxtools.model.level3.BiochemicalReaction;
import org.biopax.paxtools.model.level3.Interaction;
import org.biopax.paxtools.model.level3.Pathway;
import org.biopax.paxtools.model.level3.Protein;
import org.biopax.paxtools.model.level3.SmallMolecule;

public class BioPAXGraphAdjList {
	private Model model;
	private Map<String, Map<String, String[]>> bioPathStepsGraph;

	/**
	 * empty constructor assigns "null" to private variables.
	 * It is not used for now.
	 */
	public BioPAXGraphAdjList(){
		this.model = null;
		this.bioPathStepsGraph = null;
	}
	
	/**
	 * Constructor. It will create the "Model model" and the Map "bioPathStepsGraph"
	 * @param fileName is the fileName of the .biopax file that we want to implement
	 */
	public BioPAXGraphAdjList(String fileName){
		initializeModel(fileName);
		createGraph();
	}

	/**
	 * This function initializes the Model from the given .biopax file
	 * @param fileName is the fileName of the .biopax file that we want to implement
	 */
	private void initializeModel(String fileName) {
		try {
			this.model = createModelFromFile(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error in initializeModel. It couldn't open the BioPax file."+fileName);
			System.exit(1);
		}
	}
	
	/**
	* This function loads a .biopax file via Simple IO Handler to a model
	*/
	public static Model createModelFromFile(String FileName) throws FileNotFoundException {
		
		FileInputStream fin = new FileInputStream(FileName);
		BioPAXIOHandler handler = new SimpleIOHandler();
		Model model = handler.convertFromOWL(fin);
		return model;
		
	}
	
	/**
	 * This method will initialize the graph Map bioPathStepsGraph
	 */
	private void createGraph() {
		Set<BioPAXElement> elementSet = filterBioPAXElementsByInstance("BiochemicalPathwayStep");
		this.bioPathStepsGraph = new HashMap<String, Map<String, String[]>>();
		
		addBiochemicalPathwayStep(elementSet);	
	}
	
	/**
	 * this function a set of BioPAXElement filtered by the 
	 * desired instance name. e.g. get all BiochemicalPathwayStep instances
	 * @param instance String that can get the values: {Protein, Interaction, Pathway, SmallMolecule, 
	 * BiochemicalReaction, BiochemicalPathwayStep, All}
	 */
	private Set<BioPAXElement> filterBioPAXElementsByInstance(String instance) {

		
		Set<BioPAXElement> oldElementSet = this.model.getObjects();
		Set<BioPAXElement> newElementSet = new HashSet<BioPAXElement>();
		
		boolean hit;
		for (BioPAXElement currentElement : oldElementSet)
		{
			switch (instance)
			{
			case "Protein":
				hit = (currentElement instanceof Protein);
				break;
			case "Interaction":
				hit = (currentElement instanceof Interaction);
				break;
			case "Pathway":
				hit = (currentElement instanceof Pathway);
				break;
			case "SmallMolecule":
				hit = (currentElement instanceof SmallMolecule);
				break; 
			case "BiochemicalReaction":
				hit = (currentElement instanceof BiochemicalReaction);
				break;
			case "BiochemicalPathwayStep":
				hit = (currentElement instanceof BiochemicalPathwayStep);
				break;
			case "All":
				hit = true;
				break;
			default:
				hit = true;
			}
			if (hit)
			{
				newElementSet.add(currentElement);
			}
		}
		return newElementSet;
	}
		

	/**
	 * This function is adding a "BiochemicalPathwayStep" to the Map that
	 * contains the BiochemicalPathwayStep's. It calls recursively itself.
	 * @param model
	 * @param BioPathSteps its the Map that we want to MUTATE. Each recursion will update (add) to this map).
	 * Although void this function "returns" This Map
	 * @param elementSet a Set of "BioPAXElement" that we want to find their "route" in the graph
	 * @param id_b this is the incrementing value of the id's of the Map. On the first call of this function
	 * this should be set to '0'
	 */
	private void addBiochemicalPathwayStep( Set<BioPAXElement> elementSet) {
		
		String bioPathStepRDFid;
		
		for (BioPAXElement currentElement : elementSet)
		{
			bioPathStepRDFid = currentElement.getRDFId();
			
			if (!this.bioPathStepsGraph.containsKey(bioPathStepRDFid)){

				addBioPathStepSubMap(bioPathStepRDFid);	
			}			
		}
	}
	
	/**
	 * this method will add the sub-Map Map<String, String[]> as a Map.value
	 * to the Map.key bioPathStepRDFid. Although void this function "returns" the mutated
	 * sub-Map as a value to the corresponding key. And also "returns" the previous instance
	 * of the global bioPathStepRDFid Map
	 * @param bioPathStepRDFid is the key of the Map Map<String, Map<String, String[]>> bioPathStepsGraph
	 */
	private void addBioPathStepSubMap(String bioPathStepRDFid) {
		Map<String, String[]> valuesMap = new HashMap<String, String[]>();
		// the RDFid of the stepConversion of the [BiochemicalPathwayStep] must have only 1 RDFId
		String[] stepConversionRDFid = extractHttps(getPropertyInfo(bioPathStepRDFid, "stepConversion"));
		if (stepConversionRDFid.length != 1)
		{
			System.out.println("the stepConversion of the [BiochemicalPathwayStep] (" + bioPathStepRDFid+")");
			System.out.println("has more than one RDFids" + stepConversionRDFid);
			System.exit(1);
		}	
		//add stepConversion
		valuesMap.put("stepConversion", stepConversionRDFid);
		
		//add "startNodes" and "endNodes"
		addStartAndEndNodesToMap(valuesMap, bioPathStepRDFid, stepConversionRDFid[0]);
		
		//add eCNumber
		valuesMap.put("eCNumber", extractHttps(getPropertyInfo(stepConversionRDFid[0], "eCNumber")));
		
		//add nextStep
		valuesMap.put("nextStep", extractHttps(getPropertyInfo(bioPathStepRDFid, "nextStep")));
		
		//TODO add previousStep
		
		//finish with this RDFid Entry and move on to the next one
		this.bioPathStepsGraph.put(bioPathStepRDFid, valuesMap); 
		
		Set<BioPAXElement> nextElementSet = filterBioPAXElementsByHttps(extractHttps(getPropertyInfo(bioPathStepRDFid, "nextStep")));
		addBiochemicalPathwayStep(nextElementSet);
		
	}

	/**
	 * This method adds the start and end Nodes of a [BiochemicalReaction] (adds the RDFids). 
	 * You must give the Sub-Map of the specific BiochemicalPathwayStep. 
	 * The valueMap will be mutated. Although void this function will "return" this mutated Map.
	 * it adds: "fromNode", "toNode" and "eCNumber"
	 * @param valuesMap the Sub-Map of the BiochemicalPathwayStep.
	 * @param bioPathStepRDFid is the RDFid of the BiochemicalPathwayStep
	 * @param stepConversionRDFid is the RDFid of the stepConversion of the corresponding BiochemicalPathwayStep
	 */
	private void addStartAndEndNodesToMap(Map<String, String[]> valuesMap, String bioPathStepRDFid, String stepConversionRDFid) {
		String direction = getPropertyInfo(bioPathStepRDFid, "stepDirection");
		//remove the [ and ] from LEFT_TO_RIGHT or RIGHT_TO_LEFT
		direction = direction.substring(1,direction.length()-1);
		
		if (direction.equals("LEFT_TO_RIGHT")){
			valuesMap.put("startNodes", extractHttps(getPropertyInfo(stepConversionRDFid, "left")));
			valuesMap.put("endNodes", extractHttps(getPropertyInfo(stepConversionRDFid, "right")));
		}
		else if (direction.equals("RIGHT_TO_LEFT")){
			valuesMap.put("startNodes", extractHttps(getPropertyInfo(stepConversionRDFid, "right")));
			valuesMap.put("endNodes", extractHttps(getPropertyInfo(stepConversionRDFid, "left")));
		}
		else{
			System.out.println("error in addStartAndEndNodesToMap direction is not LEFT_TO_RIGHT or RIGHT_TO_LEFT it is: " +direction);
			System.exit(1);
		}
	}
	
	/**
	 * Similar to "filterBioPAXElementsByInstance". Only that we give a
	 * String[] of the https of the desired BioPAXElement that we want
	 * to extract
	 * @param model
	 * @param https
	 */
	private Set<BioPAXElement> filterBioPAXElementsByHttps(String[] https) {
		
		Set<BioPAXElement> newElementSet = new HashSet<BioPAXElement>();
		for (String http : https){
			newElementSet.add(this.model.getByID(http));
		}
		return newElementSet;
	}

	/**
	 * returns the information of the given property.
	 * @param RDFid the RDFId of the model object
	 * @param property is the desired property that want to be returned
	 * @return returns the information as String. 
	 * If the property is not found then "property not found" is returned
	 */
	public String getPropertyInfo(String RDFid, String property) {
		
		BioPAXElement bpe = this.model.getByID(RDFid);
		String[][] properties = getlistPropertiesFromBPE(bpe);
		for (int i=0;i < properties.length;i++)
		{
			if (properties[i][0].equals(property)){
				return properties[i][1];
			}
			
		}
		
		return "property not found";
	}
	
	/**
	 * This function lists the properties of a BioPAXElement.
	 * @param bpe a BioPAXElement instanse
	 * @return String[][] the properties of a BioPAXElement
	 */
	public static String[][] getlistPropertiesFromBPE(BioPAXElement bpe)
	{
		
		//In order to use properties we first need an EditorMap
		EditorMap editorMap = SimpleEditorMap.L3;
		//And then get all the editors for our biopax element
		Set<PropertyEditor> editors = editorMap.getEditorsOf(bpe);
		//Let's prepare a table to return values
		String value[][]= new String[editors.size()][2];
		int row = 0;
		//For each property
		for (PropertyEditor editor : editors)
		{
			//First column is the name of the property ,e.g. "Name"
			value[row][0]= editor.getProperty();
			//Second column is the value e.g. "p53"
			value[row][1]= editor.getValueFromBean(bpe).toString();
			//increase the row index
			row++;
		}
		return value;
	}
		
	/**
	 * this function gets a String in the form [http://.., http://, ...]
	 * and returns a String[] that has the https
	 * @param https is a string in the form [http://.., http://, ...]
	 * @return a String[] with each cell is a http (RDFid)
	 */
	public static String[] extractHttps(String https) {
		// convert [http://...] to http://...
		String subString = https.substring(1,https.length()-1);
		String[] subStrings;
		/* split http://...1, http://...2, http://...3 to  
		 * subStrings[0] = http://...1
		 * subStrings[1] =  http://...2
		 * subStrings[2] =  http://...3
		 */
		subStrings = subString.split(",");
		//System.out.println(subStrings[0]);
		for (int i=1;i<subStrings.length;i++){
			/* reformatting:                   to:  
			 * subStrings[0] = http://...1   subStrings[0] = http://...1
			 * subStrings[1] =  http://...2  subStrings[1] = http://...2
			 * subStrings[2] =  http://...3  subStrings[2] = http://...3
			 */
			subStrings[i] = subStrings[i].substring(1);
			//System.out.println(subStrings[i]);
		}
		return subStrings;
	}
	
	public void printGraph() {
		System.out.println("size_BioPathSteps is: "+this.bioPathStepsGraph.size());
		for (Map.Entry<String, Map<String, String[]>> entry : this.bioPathStepsGraph.entrySet()){
		    System.out.println(entry.getKey() + " ==> ");
		    for (Map.Entry<String, String[]> subEntry: entry.getValue().entrySet()){
		    	System.out.print(subEntry.getKey() + ": ");
		    	for (String s : subEntry.getValue()){
		    		System.out.print(s+" ");
		    	}
		    	System.out.println("");
		    }    
		    //print also the edge standardName of the bioPathwaySteps
			System.out.println("Edge standardName is: "+getPropertyInfo(entry.getValue().get("stepConversion")[0], "standardName"));
		    System.out.println("");
		}
		
	}
	
	/**
	 * is the getter of the bioPathStepsGraph
	 */
	public Map<String, Map<String, String[]>> getBioPathStepsGraph(){
		return this.bioPathStepsGraph;
	}
	
	/**
	 * This method prints valuable information of the BioPAXGraphAdjList class
	 */
	public void printInfo(){
		System.out.println("Class 'BioPAXGraphAdjList':");
		System.out.println("  Model model is the elementary object of the .biopax file");
		System.out.println("  Map<String, Map<String, String[]>> bioPathStepsGraph:");
		System.out.println("    is the graph representation of the .biopax file.");
		System.out.println("    every key is the RDFid of the [BiochemicalPathwayStep]");
		System.out.println("    every value is another Map (Map<String, String[]>)");
		System.out.println("    where the keys are:");
		System.out.println("      [stepConversion] = RDFid of [BiochemicalPathwayStep's] [BiochemicalReaction]");
		System.out.println("      [startNodes] = RDFid's of the starting side of the [BiochemicalReaction]");
		System.out.println("      [endNodes] = RDFid's of the ending side of the [BiochemicalReaction]");
		System.out.println("        !Note for [thisNodes] and [endNodes] the correct LEFT_TO_RIGHT or RIGHT_TO_LEFT direction has been taken into notice");
		System.out.println("      [eCNumber] = eCNumber of the [stepConversion] of the current [BiochemicalPathwayStep]");
		System.out.println("      [nextStep] = RDFid of the next [BiochemicalPathwayStep]");
		System.out.println("      [previousStep] = RDFid of the previous [BiochemicalPathwayStep] (NOT IMPLEMENTED YET!!!)");
	}
	
}
