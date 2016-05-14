package michaelp.jung;
import java.io.FileNotFoundException;

import org.biopax.paxtools.model.Model;

public class Application {

	public static void main(String[] args)  {
			
			System.out.println("hello World!");
			String fileName1 = "src/data/TCA cycle I (prokaryotic).biopax";
			String fileName2 = "src/data/TCA cycle, aerobic respiration.biopax";
			
			System.out.println("The TCA cycle I (prokaryotic) graph: "+fileName1);
			Object graph1 = createGraphFromBiopaxFile(fileName1);
			//BioPAXGraphAdjList graph1 = new BioPAXGraphAdjList(fileName1);
			//graph1.printGraph();
			
			/*
			System.out.println("The TCA cycle, aerobic respiration graph: "+fileName2);
			BioPAXGraphAdjList graph2 = new BioPAXGraphAdjList(fileName2);
			graph2.printGraph();
			*/
			
			System.out.println("Goodbye");		
		}
	
	private static Object createGraphFromBiopaxFile(String fileName) {
		BioPAXGraphAdjList graph1 = new BioPAXGraphAdjList(fileName);
		graph1.printGraph();
		return null;
	}

}

//[BiochemicalReaction] == Edge
//[SmallMolecule] == Node with standardName == the correct name.
//[BiochemicalPathwayStep] with stepDirection == LEFT_TO_RIGHT or RIGHT_TO_LEFT.
