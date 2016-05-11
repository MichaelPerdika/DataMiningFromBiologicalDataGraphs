package michaelp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.biopax.paxtools.controller.EditorMap;
import org.biopax.paxtools.controller.PathAccessor;
import org.biopax.paxtools.controller.PropertyEditor;
import org.biopax.paxtools.controller.SimpleEditorMap;
import org.biopax.paxtools.examples.ProteinNameLister;
import org.biopax.paxtools.examples.Tutorial;
import org.biopax.paxtools.io.BioPAXIOHandler;
import org.biopax.paxtools.io.SimpleIOHandler;
import org.biopax.paxtools.model.BioPAXElement;
import org.biopax.paxtools.model.BioPAXFactory;
import org.biopax.paxtools.model.BioPAXLevel;
import org.biopax.paxtools.model.Model;
import org.biopax.paxtools.model.level3.BiochemicalPathwayStep;
import org.biopax.paxtools.model.level3.BiochemicalReaction;
import org.biopax.paxtools.model.level3.Entity;
import org.biopax.paxtools.model.level3.Interaction;
import org.biopax.paxtools.model.level3.Pathway;
import org.biopax.paxtools.model.level3.Protein;
import org.biopax.paxtools.model.level3.Process;
import org.biopax.paxtools.model.level3.ProteinReference;
import org.biopax.paxtools.model.level3.SmallMolecule;
import org.biopax.paxtools.model.level3.UnificationXref;
import org.biopax.paxtools.model.level3.Xref;

public class BiopaxAPI {

	private BiopaxAPI(){}
	
	public static void accesGraph_old(Model model) {
		// TODO Auto-generated method stub
		iterateAllBioPAXElements_old(model);
	}
	
	/**
	* this function calls iterateAllBioPAXElements(model,"All") with 
	* instance = "All" which means that will access all BioPAXElements.
	*/
	public static void iterateAllBioPAXElements_old(Model model) {
		
		iterateAllBioPAXElements_old(model,"All");
	}
	
	/**
	* Iterate through all BioPAX Elements (BioPAXElement).
	* instance = {"All", "Protein", "Interaction", "Pathway"}. Given the corresponding 
	* instance value it will print only the BioPAXElements of these instances.
	*/
	public static void iterateAllBioPAXElements_old(Model model, String instance) {
		
		
		Set<BioPAXElement> elementSet = model.getObjects();
		String[][] properties = null;
		boolean printElement;
		for (BioPAXElement currentElement : elementSet)
		{
			properties = getlistPropertiesFromBPE(currentElement);
			switch (instance)
			{
			case "Protein":
				printElement = (currentElement instanceof Protein);
				break;
			case "Interaction":
				printElement = (currentElement instanceof Interaction);
				break;
			case "Pathway":
				printElement = (currentElement instanceof Pathway);
				break;
			case "All":
				printElement = true;
				break;
			default:
				printElement = true;
			}
			if (printElement)
			{
				for (int i=0;i < properties.length;i++)
				{
					if (properties[i][0].equals("left")){
						// convert [http://...] to http://...
						String[] https = extractHttps(properties[i][1]);
						for (String s : https){
							System.out.println("left is: " + s);
						}
						System.out.println(" ");
						//printBioPAXElementProperties(model,subString);
						//System.out.println("left is: " + properties[i][1].substring(1));
						//System.out.println("left is: " + properties[i][1]);
						//System.out.println("left is: " + properties[i][1].substring(1,properties[i][1].length()-1));
					}
				}
			}
		}
	}
	
	/**
	* This function prints the properties of a BioPAXElement given by the 
	* String http.
	*/
	public static void printBioPAXElementProperties(Model model, String http) {
		
		BioPAXElement mjk = model.getByID(http);
		String[][] temp2 = getlistPropertiesFromBPE(mjk);
		for (int i=0;i < temp2.length;i++)
		{
			System.out.println("BioPAXElement " +(i+1)+" is: ["+ temp2[i][0] + "], "+ temp2[i][1]+"");
		}
		
	}
	
	/**
	* This function gets the properties of a BioPAXElement given by the 
	* String http. It calls the funtion getlistPropertiesFromBPE
	*/
	public static String[][] getListPropertiesFromHttp(Model model, String http) {
		
		BioPAXElement bpe = model.getByID(http);
		String[][] properties = getlistPropertiesFromBPE(bpe);
		return properties;
		
	}
	
	/**
	* this function calls iterateAllBioPAXElementsAndPrintProperties(model,"All") with 
	* instance = "All" which means that will access and print all BioPAXElements.
	*/
	public static void iterateAllBioPAXElementsAndPrintProperties(Model model) {
		
		iterateAllBioPAXElementsAndPrintProperties(model,"All");
	}
	
	/**
	* Iterate through all BioPAX Elements (BioPAXElement) and print basic info.
	* instance = {"All", "Protein", "Interaction", "Pathway"}. Given the corresponding 
	* instance value it will print only the BioPAXElements of these instances.
	*/
	public static void iterateAllBioPAXElementsAndPrintProperties(Model model, String instance) {
		
		Set<BioPAXElement> elementSet = model.getObjects();
		String[][] properties = null;
		boolean printElement;
		for (BioPAXElement currentElement : elementSet)
		{
			properties = getlistPropertiesFromBPE(currentElement);
			switch (instance)
			{
			case "Protein":
				printElement = (currentElement instanceof Protein);
				break;
			case "Interaction":
				printElement = (currentElement instanceof Interaction);
				break;
			case "Pathway":
				printElement = (currentElement instanceof Pathway);
				break;
			case "SmallMolecule":
				printElement = (currentElement instanceof SmallMolecule);
				break; 
			case "BiochemicalReaction":
				printElement = (currentElement instanceof BiochemicalReaction);
				break;
			case "BiochemicalPathwayStep":
				printElement = (currentElement instanceof BiochemicalPathwayStep);
				break;
			case "All":
				printElement = true;
				break;
			default:
				printElement = true;
				
			}
			if (printElement)
			{
				System.out.println("getRDFid is: "+currentElement.getRDFId());
				for (int i=0;i < properties.length;i++)
				{
					System.out.println("BioPAXElement " +(i+1)+" is: ["+ properties[i][0] + "], "+ properties[i][1]+"");
				}
				System.out.println("");
			}
		}
	}

	/**
	* Iterate through all pathways in the model
	*/
	public static void iterateThroughAllPathways(Model model) {
		
		
		Set<Pathway> allPathways = model.getObjects(Pathway.class);
		for (Pathway aPathway :allPathways)
		{
			System.out.println("Pathway: "+ aPathway.getName());
			extractProteinUrefsFromPathway(aPathway);
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
	 * This function gets the Proteins of a model and prints the 
	 * Name and DisplayName
	 */
	public static void getProteinsOnly(Model model) {
		
		Set<Protein> proteinSet = model.getObjects(Protein.class);
		for (Protein currentProtein : proteinSet)
		{
			System.out.println(currentProtein.getName() + ": "+ currentProtein.getDisplayName());
		}
		
	}

	/**
	* Iterate through all BioPAX Elements and print basic info
	*/
	public static void iterateAllBioPAXElementsAndPrint(Model model) {
		
		Set<BioPAXElement> elementSet = model.getObjects();
		for (BioPAXElement currentElement : elementSet)
		{
			String rdfId = currentElement.getRDFId();
			String className = currentElement.getClass().getName();
			System.out.println("Element: "+ rdfId +": "+ className);
		}
		
	}

	
	/**
	* This function extracts Protein Urefs from a Pathway
	*/
	public static void extractProteinUrefsFromPathway(Pathway aPathway) 
	{
		
		for (Process aProcess: aPathway.getPathwayComponent())
		{
			if (aProcess instanceof Pathway )
			{
				extractProteinUrefsFromPathway((Pathway)aProcess);
			}
			else
			{
				extractAndPrintProteinUrefs((Interaction)aProcess);
			}
		}
	}

	/**
	* This function Extracts and prints the Urefs of the Proteins
	*/
	public static void extractAndPrintProteinUrefs(Interaction anInteraction) {
		
		for (Entity participant : anInteraction.getParticipant())
		{
			if (participant instanceof Protein)
			{
				ProteinReference entityReference = (ProteinReference) ((Protein) participant).getEntityReference();
				if (entityReference != null)
				{
					Set<Xref> xrefSet = entityReference.getXref();
					for (Xref currentRef : xrefSet)
					{
						if (currentRef instanceof UnificationXref)
						{
							System.out.println("Unification XREf: "+ currentRef.getDb()+ ": " + currentRef.getId() );
						}
					}
				}
			}
		}
		
	}

	/**
	* This function lists the properties of a BioPAXElement.
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
			//Second columnis the value e.g. "p53"
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
	 * @return a String[] with each cell an http (RDFid)
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
	
	

	/*
	 * from this point and bellow is the useful methods
	 * all the above are for debugging
	 */
	public static void createGraph(String fileName) {
		Model model = null;
		try {
			model = BiopaxAPI.createModelFromFile(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("This is the catch section!. It couldn't open the BioPax file."+fileName);
			System.exit(1);
		}
		accesGraph(model);
		//accesGraphOld(model);
		//accesGraph2(model);
		//iterateAllBioPAXElementsAndPrintProperties(model,"BiochemicalPathwayStep");
		//printBioPAXElementProperties(model, "http://http://BioCyc.org//META/pathway-biopax?type=3%38object=TCACatalysis46039");
	}

/**
 * this is the final form of accesGraph. It should return the whole graph. 
 * It is void for now
 * @param model
 */
	private static void accesGraph(Model model) {
		//printBioPAXElementProperties(model, "http://http://BioCyc.org//META/pathway-biopax?type=3%38object=TCADeltaG47288");
		Set<BioPAXElement> elementSet = filterBioPAXElementsByInstance(model, "BiochemicalPathwayStep");
	
		//BioPathSteps are the Edges. This might need a rename in the future
		Map<String, Map<String, String[]>> bioPathSteps = new HashMap<String, Map<String, String[]>>();
		
		addBiochemicalPathwayStep(model, bioPathSteps, elementSet);
	
		printGraph(model, bioPathSteps);
		
	}

	private static void printGraph(Model model, Map<String, Map<String, String[]>> bioPathSteps) {
		// TODO Auto-generated method stub
		System.out.println("size_BioPathSteps is: "+bioPathSteps.size());
		for (Map.Entry<String, Map<String, String[]>> entry : bioPathSteps.entrySet()){
		    System.out.println(entry.getKey() + " ==> ");
		    for (Map.Entry<String, String[]> subEntry: entry.getValue().entrySet()){
		    	System.out.print(subEntry.getKey() + ": ");
		    	for (String s : subEntry.getValue()){
		    		System.out.print(s+" ");
		    	}
		    	System.out.println("");
		    }    
		    //print also the edge standardName of the bioPathwaySteps
			//for (String edge_http : extractHttps(getPropertyInfo(model, entry.getValue().get("stepConversion")[0], "standardName"))) {System.out.println("    "+getPropertyInfo(model, edge_http, "standardName"));}
			System.out.println("Edge standardName is: "+getPropertyInfo(model, entry.getValue().get("stepConversion")[0], "standardName"));
		    System.out.println("");
		}
		
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
	private static void addBiochemicalPathwayStep(Model model, Map<String, Map<String, String[]>> BioPathSteps, Set<BioPAXElement> elementSet) {
		
		String RDFid;
		
		for (BioPAXElement currentElement : elementSet)
		{
			// these 4 lines could be a function
			RDFid = currentElement.getRDFId();
			
			if (!BioPathSteps.containsKey(RDFid)){
				
				//System.out.println("BiochemicalPathwayStep RDFid is: "+RDFid);
				//System.out.println("direction is "+getPropertyInfo(model, RDFid, "stepDirection"));
				
				/*The valuesMap will have:
				 * stepConversion --> the current edge
				 * fromNode --> left nodes if destination is LEFT_TO_RIGHT.
				 * toNode   --> right nodes if destination is LEFT_TO_RIGHT
				 * eCNumber --> eCNumber
				 * nextStep --> information for the next edges
				 * previousStep???? see if i can do that
				 */
				Map<String, String[]> valuesMap = new HashMap<String, String[]>();
				//add stepConversion
				valuesMap.put("stepConversion", extractHttps(getPropertyInfo(model, RDFid, "stepConversion")));
				// TODO this for loop should have only 1 edge Http. must check what happens if it has more than 1.
				//add "fromNode", "toNode" and "eCNumber"
				addFromAndToNodeToMap(model, valuesMap, RDFid);
				
				//add nextStep
				valuesMap.put("nextStep", extractHttps(getPropertyInfo(model, RDFid, "nextStep")));
				
				//finish with this RDFid Entry and move on to the next one
				BioPathSteps.put(RDFid, valuesMap); 
				
				Set<BioPAXElement> nextElementSet = filterBioPAXElementsByHttps(model, extractHttps(getPropertyInfo(model, RDFid, "nextStep")));
				addBiochemicalPathwayStep(model, BioPathSteps, nextElementSet);
							
			}			
		}
	}

	/**
	 * This method adds the left and right Nodes of an Edge (adds the RDFids). 
	 * You must give the Sub-Map of the specific BiochemicalPathwayStep. 
	 * The valueMap will be mutated. Although void this function will "return" this mutated Map.
	 * it adds: "fromNode", "toNode" and "eCNumber"
	 * @param model
	 * @param valueMap the Sub-Map of the BiochemicalPathwayStep.
	 * @param BioPathStepRDFid is the RDFid of the BiochemicalPathwayStep
	 */
	private static void addFromAndToNodeToMap(Model model, Map<String, String[]> valuesMap, String BioPathStepRDFid) {
		
		String direction = getPropertyInfo(model, BioPathStepRDFid, "stepDirection");
		//remove the [ and ] from LEFT_TO_RIGHT or RIGHT_TO_LEFT
		direction = direction.substring(1,direction.length()-1);
		// TODO this for loop should have only 1 edge Http. must check what happens if it has more than 1.
		for (String currentEdgeHttp : extractHttps(getPropertyInfo(model, BioPathStepRDFid, "stepConversion"))){
			//printLeftAndRightOfEdge(model, currentEdgeHttp);
			if (direction.equals("LEFT_TO_RIGHT")){
				valuesMap.put("fromNode", extractHttps(getPropertyInfo(model, currentEdgeHttp, "left")));
				valuesMap.put("toNode", extractHttps(getPropertyInfo(model, currentEdgeHttp, "right")));
			}
			else if (direction.equals("RIGHT_TO_LEFT")){
				valuesMap.put("fromNode", extractHttps(getPropertyInfo(model, currentEdgeHttp, "right")));
				valuesMap.put("toNode", extractHttps(getPropertyInfo(model, currentEdgeHttp, "left")));
			}
			else{
				System.out.println("error in addFromAndToNodeToMap direction is not LEFT_TO_RIGHT or RIGHT_TO_LEFT it is: " +direction);
				System.exit(1);
			}
			valuesMap.put("eCNumber", extractHttps(getPropertyInfo(model, currentEdgeHttp, "eCNumber")));
		}
	}

	/**
	 * Similar to "filterBioPAXElementsByInstance". Only that we give a
	 * String[] of the https of the desired BioPAXElement that we want
	 * to extract
	 * @param model
	 * @param https
	 */
	private static Set<BioPAXElement> filterBioPAXElementsByHttps(Model model, String[] https) {
		
		Set<BioPAXElement> newElementSet = new HashSet<BioPAXElement>();
		for (String http : https){
			newElementSet.add(model.getByID(http));
		}
		return newElementSet;
	}

	/**
	 * This function prints the left and right Nodes of an Edge. 
	 * You must give the http (RDFid) of the edge
	 * @param model
	 * @param EdgeHttp
	 */
	private static void printLeftAndRightOfEdge(Model model,String EdgeHttp) {
		//print name of edge
		System.out.println("edge  standardName is:"+getPropertyInfo(model, EdgeHttp, "standardName"));
		//left nodes of the edge
		//extractNodesFromEdge can be more generic because it is used also above for the edges
		for (String node_http : extractNodesFromEdge(model, EdgeHttp, "left")){
			//System.out.println("left  node_http is "+node_http);
			System.out.println("left  standardName is "+getPropertyInfo(model, node_http, "standardName")+"["+node_http+"]");
			//printBPEattributes(model.getByID(node_http));
		}
		for (String node_http : extractNodesFromEdge(model, EdgeHttp, "right")){
			//System.out.println("right node_http is "+node_http);
			System.out.println("right standardName is "+getPropertyInfo(model, node_http, "standardName")+"["+node_http+"]");
			//printBPEattributes(model.getByID(node_http));
		}
		System.out.println("");
		
	}



	/**
	 * Returns the RDFId's of the left or right nodes of an edge (given its RDFid)
	 * @param model
	 * @param RDFid This is the RDFid of the edge
	 * @param leftOrRight this is either "left" or "right"
	 * @return returns String[] of all the RDFid's left or right
	 */
	private static String[] extractNodesFromEdge(Model model, String RDFid, String leftOrRight) {
		return extractHttps(getPropertyInfo(model, RDFid, leftOrRight));
	}	

/**
 * this function gets the model and returns a set of BioPAXElement filtered by the 
 * desired instance name. e.g. get all BiochemicalPathwayStep instances
 * @param model
 * @param instance
 */
	private static Set<BioPAXElement> filterBioPAXElementsByInstance(Model model, String instance) {

		
		Set<BioPAXElement> oldElementSet = model.getObjects();
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

	public static void accesGraphOld(Model model) {
		Set<BioPAXElement> elementSet = model.getObjects();
		String[][] properties = null;
		Map<Integer, String> nodes = new HashMap<Integer, String>();

		int ids = 0;
		for (BioPAXElement currentElement : elementSet)
		{
			properties = getlistPropertiesFromBPE(currentElement);

			for (int i=0;i < properties.length;i++)
			{
				if (properties[i][0].equals("left")){
					String[] https = extractHttps(properties[i][1]);
					for (String http : https){
						System.out.println("left is: " + getPropertyInfo(model, http, "name"));
						if (!nodes.containsValue(getPropertyInfo(model, http, "name"))){
							nodes.put(ids, getPropertyInfo(model, http, "name"));
							ids++;					
							//BioPAXElement mjk = model.getByID(http);
							//System.out.println("protein is: "+mjk.getModelInterface());
						}
					}
					System.out.println(" ");
				}
				else if (properties[i][0].equals("right")){
					printBioPAXElementProperties(model, currentElement.toString());
					String[] https = extractHttps(properties[i][1]);
					for (String http : https){
						System.out.println("right is: " + getPropertyInfo(model, http, "name"));
						if (!nodes.containsValue(getPropertyInfo(model, http, "name"))){
							nodes.put(ids, getPropertyInfo(model, http, "name"));
							ids++;
							//BioPAXElement mjk = model.getByID(http);
							//System.out.println("protein is: "+mjk.getModelInterface());
						}
					}
					System.out.println(" ");
				}
				
			}
		
		}
		printNodes(nodes);
	}

	public static void accesGraph2(Model model) {
		Set<BioPAXElement> elementSet = model.getObjects();
		String[][] properties = null;
		Map<Integer, String> nodes = new HashMap<Integer, String>();

		int ids = 0;
		for (BioPAXElement currentElement : elementSet)
		{
			properties = getlistPropertiesFromBPE(currentElement);

			for (int i=0;i < properties.length;i++)
			{
				if (properties[i][0].equals("stepDirection")){
					printBPEattributes(currentElement);

					
					System.out.println(properties[i][1]);
					/*
					for (int j=0;j < properties.length;j++)
					{
						//System.out.println("BioPAXElement " +(j+1)+" is: ["+ properties[j][0] + "], "+ properties[j][1]+"");
						if (properties[j][0].equals("stepProcess")){
							String[] https = extractHttps(properties[j][1]);
							for (String http : https){
								printBioPAXElementProperties(model,http);
							}
						}
					}*/
					/*
					String[] https = extractHttps(properties[i][1]);
					for (String http : https){
						System.out.println("left is: " + getPropertyInfo(model, http, "name"));
						if (!nodes.containsValue(getPropertyInfo(model, http, "name"))){
							nodes.put(ids, getPropertyInfo(model, http, "name"));
							ids++;					
							//BioPAXElement mjk = model.getByID(http);
							//System.out.println("protein is: "+mjk.getModelInterface());
						}
					}*/
					System.out.println(" ");
				}
			}
		
		}
		printNodes(nodes);
	}
		
		
	public static void printBPEattributes(BioPAXElement bpe){
		System.out.println("toString == "+bpe.toString());
		System.out.println("getRDFId == "+bpe.getRDFId());
		System.out.println("equivalenceCode == "+bpe.equivalenceCode());
		System.out.println("hashCode == "+bpe.hashCode());
		System.out.println("getAnnotations == "+bpe.getAnnotations());
		System.out.println("getClass == "+bpe.getClass());
		System.out.println("getModelInterface == "+bpe.getModelInterface());
	}
		
	private static void printNodes(Map<Integer, String> nodes) {
		for (Map.Entry<Integer, String> entry : nodes.entrySet()){
		    System.out.println(entry.getKey() + " ==> " + entry.getValue());
		}
		
	}

	/**
	 * returns the information of the given property.
	 * @param model
	 * @param http the RDFId of the model object
	 * @param string is the desired property that want to be returned
	 * @return returns the information as String. 
	 * If the property is not found then "property not found" is returned
	 */
	private static String getPropertyInfo(Model model, String http, String property) {
		
		BioPAXElement bpe = model.getByID(http);
		String[][] properties = getlistPropertiesFromBPE(bpe);
		for (int i=0;i < properties.length;i++)
		{
			if (properties[i][0].equals(property)){
				return properties[i][1];
			}
			
		}
		
		return "property not found";
		
		
	}
		
	

	
}
