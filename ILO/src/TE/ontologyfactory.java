package TE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Dataset;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class ontologyfactory {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	public static void getontosyntax(Map<String,List<String>> TripleCandidates, Map<String, Dataset> lookupresources)
	{
		// construct whole subgraph for each concept
		for (String concept: TripleCandidates.keySet())
   	 	{
			Dataset dataset = lookupresources.get(concept) ;
			Model graph = dataset.getcandidateGraph();
			 for (String triple: TripleCandidates.get(concept) )
			 {
				 
				 // start getting the object and predicate from the triples 
				 String tokens[] =  triple.split("~") ;
				 String Object ;
				 if(tokens.length < 3)
					 continue ; 

				 {
					 String _tokens[] =  tokens[2].split("\\[|\\^+");
					// removing the lang 
					 String Objects[] = _tokens[0].split("@") ;
					 Object = Objects[0] ;
				 }
				 
				 String temp  = tokens[0] ;
				 String subj  = temp.replaceAll("http://linkedlifedata.com/sparql","")  ;
				 String predicate = tokens[1] ;
				 
				 Resource entity = graph.createResource(subj);
	 	         final Property p = ResourceFactory.createProperty(predicate) ;
	 	         entity.addProperty(p, Object);	 
			 }
	   		
   	 	}
	}
	public static void getontoSyntaticPattern(ArrayList<String> RelInstances , Model Sentgraph, Map<String, Dataset> lookupresources)
	{
		// construct whole subgraph for each concept
		for (String rel: RelInstances)
   	 	{
			
			  String[] tokens = rel.split(",") ;
			  Dataset dataset = lookupresources.get(tokens[1].trim().toLowerCase()) ;
			  Resource rec = Sentgraph.createResource(dataset.PrefLabel);
			  final Property p = ResourceFactory.createProperty(tokens[3]) ;
	          rec.addProperty(p, tokens[2]);	  		
   	 	}
	}

	public static void getonto (Map<String, Dataset> lookupresources)
	{
		// construct whole subgraph for each concept
		for (String concept: lookupresources.keySet())
   	 	{
			
	   		Dataset dataset = lookupresources.get(concept) ;
	   		Model graph = dataset.getcandidateGraph();
   			String uri = ""; 
	   		for (String onto: dataset.getonto().keySet())
	   		{
	   			List<String> UIRs = dataset.getontoURIs(onto) ;
	   			double max = 0.0 ;

		    	for (String URI: UIRs)
		    	{    		
		    		String[] words = URI.split("!");  
		    		if (Double.parseDouble(words[1]) > max)
		    		{
		    			max = Double.parseDouble(words[1]) ;
		    			uri = words[0] ; 
		    		}

		    	}
	   		}
	   		
	   		// set the lexical alt label
	   		 List<String> altlabels = dataset.getaltlebel() ;
	   		if (altlabels != null)
	   		{
	   			for (String label: altlabels)
	   			{
	   				Resource rec = graph.createResource(uri);
 	        		// add the property
	 	         	final Property p = ResourceFactory.createProperty("skos:altLabel") ;
	 	         	rec.addProperty(p, label);	
	   			}
	   			
	   		}
	   		
   	 	}
	}
	
	
	public static void getontoHierarchy (Map<String, Dataset> lookupresources)
	{
		// construct whole subgraph for each concept
		for (String concept: lookupresources.keySet())
   	 	{
			
	   		Dataset dataset = lookupresources.get(concept) ;
	   		Model graph = dataset.getcandidateGraph();
   			String uri = ""; 
	   		for (String onto: dataset.getonto().keySet())
	   		{
	   			List<String> UIRs = dataset.getontoURIs(onto) ;
	   			double max = 0.0 ;

		    	for (String URI: UIRs)
		    	{    		
		    		String[] words = URI.split("!");  
		    		if (Double.parseDouble(words[1]) > max)
		    		{
		    			max = Double.parseDouble(words[1]) ;
		    			uri = words[0] ; 
		    		}

		    	}
	   		}
	   		
	   		// set the lexical alt label
	   		 List<String> Hierarchy = dataset.Hierarchy ;
	   		 
	   		if (Hierarchy != null)
	   		{
	   			for (int i = Hierarchy.size()-2 ; i > -1; i--)
	   			{
	   				String hier = Hierarchy.get(i) ;
	   				String tokens[] = hier.split("|") ;
	   				Resource child = graph.createResource(uri);
	   				Resource parent = graph.createResource(tokens[0]);
	   				
 	        		// add the property
	 	         	final Property p = ResourceFactory.createProperty("skos:broader") ;
	 	         	child.addProperty(p, parent);
	 	         	final Property pp = ResourceFactory.createProperty("rdfs:label") ;
	 	         	parent.addProperty(pp, tokens[1]);
	   			}
	   			
	   		}
	   		
   	 	}
	}
	public static void getontoSemanticType (Map<String, Dataset> lookupresources)
	{
		// construct whole subgraph for each concept
		for (String concept: lookupresources.keySet())
   	 	{
			
	   		Dataset dataset = lookupresources.get(concept) ;
	   		Model graph = dataset.getcandidateGraph();
   			String uri = ""; 
	   		for (String onto: dataset.getonto().keySet())
	   		{
	   			List<String> UIRs = dataset.getontoURIs(onto) ;
	   			double max = 0.0 ;

		    	for (String URI: UIRs)
		    	{    		
		    		String[] words = URI.split("!");  
		    		if (Double.parseDouble(words[1]) > max)
		    		{
		    			max = Double.parseDouble(words[1]) ;
		    			uri = words[0] ; 
		    		}

		    	}
	   		}
	   		
	   		// set the lexical alt label
	   		 List<String> Category = dataset.Category ;
	   		if (Category != null)
	   		{
	   			for (String Definition: Category)
	   			{
	   				Resource rec = graph.createResource(uri);
 	        		// add the property
	 	         	final Property p = ResourceFactory.createProperty("rdf:type") ;
	 	         	rec.addProperty(p, Definition);
	   			}
	   			
	   		}
	   		
   	 	}
	}
	public static void getontodefinition (Map<String, Dataset> lookupresources)
	{
		// construct whole subgraph for each concept
		for (String concept: lookupresources.keySet())
   	 	{
			
	   		Dataset dataset = lookupresources.get(concept) ;
	   		Model graph = dataset.getcandidateGraph();
   			String uri = ""; 
	   		for (String onto: dataset.getonto().keySet())
	   		{
	   			List<String> UIRs = dataset.getontoURIs(onto) ;
	   			double max = 0.0 ;

		    	for (String URI: UIRs)
		    	{    		
		    		String[] words = URI.split("!");  
		    		if (Double.parseDouble(words[1]) > max)
		    		{
		    			max = Double.parseDouble(words[1]) ;
		    			uri = words[0] ; 
		    		}

		    	}
	   		}
	   		
	   		// set the lexical alt label
	   		 List<String> Definitions = dataset.Definition ;
	   		if (Definitions != null)
	   		{
	   			for (String Definition: Definitions)
	   			{
	   				Resource rec = graph.createResource(uri);
 	        		// add the property
	 	         	final Property p = ResourceFactory.createProperty("skos:definition") ;
	 	         	rec.addProperty(p, Definition);
	   			}
	   			
	   		}
	   		
   	 	}
	}
	
	public static void getontoscheme (Map<String, Dataset> lookupresources)
	{
		// construct whole subgraph for each concept
		for (String concept: lookupresources.keySet())
   	 	{
			
	   		Dataset dataset = lookupresources.get(concept) ;
	   		Model graph = dataset.getcandidateGraph();
   			String uri = ""; 
	   		for (String onto: dataset.getonto().keySet())
	   		{
	   			List<String> UIRs = dataset.getontoURIs(onto) ;
	   			double max = 0.0 ;

		    	for (String URI: UIRs)
		    	{    		
		    		String[] words = URI.split("!");  
		    		if (Double.parseDouble(words[1]) > max)
		    		{
		    			max = Double.parseDouble(words[1]) ;
		    			uri = words[0] ; 
		    		}

		    	}
	   		}
	   		
	   		// set the lexical alt label
	   		 List<String> scheme = dataset.ontology ;
	   		if (scheme != null)
	   		{
	   			for (String label: scheme)
	   			{
	   				Resource rec = graph.createResource(uri);
 	        		// add the property
	 	         	final Property p = ResourceFactory.createProperty("skos:inScheme") ;
	 	         	rec.addProperty(p, label);
	   			}
	   			
	   		}
	   		
   	 	}
	}
	public static void getontoPreflabel (Map<String, Dataset> lookupresources)
	{
		// construct whole subgraph for each concept
		for (String concept: lookupresources.keySet())
   	 	{
			
	   		Dataset dataset = lookupresources.get(concept) ;
	   		Model graph = dataset.getcandidateGraph();
   			String uri = ""; 
	   		for (String onto: dataset.getonto().keySet())
	   		{
	   			List<String> UIRs = dataset.getontoURIs(onto) ;
	   			double max = 0.0 ;

		    	for (String URI: UIRs)
		    	{    		
		    		String[] words = URI.split("!");  
		    		if (Double.parseDouble(words[1]) > max)
		    		{
		    			max = Double.parseDouble(words[1]) ;
		    			uri = words[0] ; 
		    		}

		    	}
	   		}
   			
   			Map<String,List<String>> syn = new HashMap<String, List<String>>();
   			String PrefLabel = dataset.PrefLabel ;
   			
			Resource rec = graph.createResource(uri);
    		// add the property
         	final Property p = ResourceFactory.createProperty("skos:altLabel") ;
         	rec.addProperty(p, PrefLabel);	
   	 	}
	}
	
	public static void getontoSynonym (Map<String, Dataset> lookupresources)
	{
		// construct whole subgraph for each concept
		for (String concept: lookupresources.keySet())
   	 	{
			
	   		Dataset dataset = lookupresources.get(concept) ;
	   		Model graph = dataset.getcandidateGraph();
   			String uri = ""; 
	   		for (String onto: dataset.getonto().keySet())
	   		{
	   			List<String> UIRs = dataset.getontoURIs(onto) ;
	   			double max = 0.0 ;

		    	for (String URI: UIRs)
		    	{    		
		    		String[] words = URI.split("!");  
		    		if (Double.parseDouble(words[1]) > max)
		    		{
		    			max = Double.parseDouble(words[1]) ;
		    			uri = words[0] ; 
		    		}

		    	}
	   		}
   			
   			Map<String,List<String>> syn = new HashMap<String, List<String>>();
   			List<String> Syns = dataset.Synonym ;
	   			
   			double max = 0.0 ;
   			List<String> alts = new ArrayList<String>()  ;
	    	for (String synon: Syns)
	    	{    		
	    		String[] words = synon.split("|");  
	    		
	    		if (syn.containsKey(words[1].toLowerCase()))
	    		{
	    			alts = syn.get(syn);
	    			if (!alts.contains(words[2].toLowerCase()))  
	    			{
	    				alts.add(words[2].toLowerCase()) ;
	    			}
	    		}
	    		else
	    		{
	    			alts.add(words[2].toLowerCase()) ;
	    			syn.put(words[1].toLowerCase(), alts) ;
	    		}

	    	}
	    	
	    	for (String label: syn.keySet())
	    	{
   				Resource rec = graph.createResource(uri);
	        		// add the property
 	         	final Property p = ResourceFactory.createProperty("skos:altLabel") ;
 	         	rec.addProperty(p, label);
	    		
	    		
	    		
	    	}
	    	
	   		
   	 	}
	}
	
	
	public static void getontoassociate (Map<String, Dataset> lookupresources)
	{
		 Map<String, Dataset> tempresources = new HashMap<String, Dataset>();
		 tempresources.putAll(lookupresources);
		// construct whole subgraph for each concept
		for (String concept: lookupresources.keySet())
   	 	{
			System.out.println("******************" + concept + "******************");
			
	   		Dataset dataset = lookupresources.get(concept) ;
	   		Model graph = dataset.getGraph();
	   		Model candidategraph = dataset.getcandidateGraph() ;
			// list the statements in the Model
			StmtIterator iter =  graph.listStatements();
	   		
	    	while (iter.hasNext())
			{
			    Statement stmt      = iter.nextStatement();  // get next statement
			    Resource  subject   = stmt.getSubject();     // get the subject
			    Property  predicate = stmt.getPredicate();   // get the predicate
			    RDFNode   object    = stmt.getObject();      // get the object
			    for (String conceptin: tempresources.keySet()) 
			    {
			    	
			    	if (concept.equals(conceptin))
			    		continue ; 
			   		Dataset datasetin = lookupresources.get(concept) ;
			   		Model graphin = dataset.getGraph();
					// list the statements in the Model
					StmtIterator iterin =  graph.listStatements();
					while (iterin.hasNext())
					{
					    Statement stmtin      = iterin.nextStatement();  // get next statement
					    Resource  subjectin   = stmtin.getSubject();     // get the subject
					    Property  predicatein = stmtin.getPredicate();   // get the predicate
					    RDFNode   objectin    = stmtin.getObject();      // get the object
					    
					    // add a resource
					    if (object.toString().equals(subjectin.toString()) )
					    {
			   				Resource rec = candidategraph.createResource(subject);
		 	        		// add the property
			 	         	final Property p = ResourceFactory.createProperty(predicate.toString()) ;
			 	         	rec.addProperty(p, subjectin.toString());
			 	         	System.out.println(object.toString() + ", " + predicate.toString() + ", " + subjectin.toString());
					    }
					    
					    // add a resource
					    if(object.toString().equals(objectin.toString()) && predicate.toString().equals(predicatein))
					    {
			   				Resource rec = candidategraph.createResource(subject);
		 	        		// add the property
			 	         	final Property p = ResourceFactory.createProperty(predicate.toString()) ;
			 	         	rec.addProperty(p, subjectin.toString());
			 	         	System.out.println(object.toString() + ", " + predicate.toString() + ", " + subjectin.toString());
					    }
			    	
					}

			    }
	   		
   	 		}
   	 	}
	}

}
