package michaelp;
import org.biopax.paxtools.controller.EditorMap;
import org.biopax.paxtools.controller.PropertyEditor;
import org.biopax.paxtools.controller.Traverser;
import org.biopax.paxtools.controller.Visitor;
import org.biopax.paxtools.model.BioPAXElement;
import org.biopax.paxtools.model.Model;
import org.biopax.paxtools.util.Filter;

/**
 * A controller that excises/extracts an element and all the elements it is
 * dependent on from a model and adds them into a new model.
 */

class Excisor implements Visitor
{
	private Traverser traverser;
	private EditorMap editorMap;
	private Model targetModel;
	
	public Excisor(EditorMap editorMap)
	{
		this.editorMap = editorMap;
		this.traverser = new Traverser(editorMap, this);
	}
	
	public Excisor(EditorMap editorMap, boolean filtering)
	{
		this.editorMap = editorMap;
		if (filtering)
		// We will filter next Step property, as Reactome pathways leads
		// outside the current pathway. Step processes are listed in the
		// pathway Component property as well so this does not affect the fetcher.
		{
			final Filter<PropertyEditor> nextStepFilter = new Filter<PropertyEditor>()
			{
				public boolean filter(PropertyEditor editor)
				{
					return !editor.getProperty().equals("nextStep");
				}
			};
			this.traverser = new Traverser(editorMap, this, nextStepFilter);
		}
		else this.traverser = new Traverser(editorMap, this);
	}
	
	//The visitor will add all elements that are reached into the new model,
	// and recursively traverse it
	public void visit(BioPAXElement domain, Object range, Model model,
	PropertyEditor editor)
	{
		// We are only interested in the BioPAXElements since
		// primitive fields are always copied by value
		if (range != null && range instanceof BioPAXElement)
		{
			BioPAXElement bpe =(BioPAXElement) range;
			if (!targetModel.contains(bpe))
			{
				targetModel.add(bpe);
				traverser.traverse(bpe, model);
			}
		}
	}
	
	public Model excise(Model sourceModel, String... ids)
	{
		// Create a new model that will contain the element(s) of interest
		this.targetModel = editorMap.getLevel().getDefaultFactory().createModel();
		for (String id : ids)
		{
			// Get the BioPAXelement
			BioPAXElement bpe = sourceModel.getByID(id);
			// Add it to the model
			targetModel.add(bpe);
			// Add the elements that bpe is dependent on
			traverser.traverse(bpe, sourceModel);
		}
		return targetModel;
	}
	
}