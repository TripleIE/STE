package TE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import util.ReadXMLFile;
import util._Entity;
import util.surfaceFormDiscovery;
import DS.ConceptsDiscovery;
import DS.QueryEngine;
public class inferenc {

	static String pred = "rdfs:label|skos:prefLabel|skos:altLabel" ;
    static int urilimit = 100 ; 
	static Map<String, Integer> _resources; 
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		
		Map<String, List<String>> triples = ReadXMLFile.Deserialize("F:\\eclipse64\\eclipse\\triples") ;
		if (triples == null)
		{
			Map<String, List<String>> titles =  ReadXMLFile.ReadCDR_TestSet_BioC()  ;
			Map<String, List<String>> temptitles = new HashMap<String, List<String>>(); 
			for(String title : titles.keySet())
			{
				
				List<String> GoldSndconcepts = titles.get(title); 
				//title = "common cold is a disease" ;
				title = "runny nose is the symptom of common cold" ;
				temptitles.put(title,GoldSndconcepts) ;
				break ; 
			}
			Map<String, Integer> allconcepts  = ConceptsDiscovery.getconcepts(temptitles);
			Map<String, List<String>> lookupresources = resourcesLookup(allconcepts) ;
			triples = TriplesRetrieval (lookupresources) ;
			ReadXMLFile.Serialized(triples, "F:\\eclipse64\\eclipse\\triples");
		}

		

	}

	public static Map<String, List<String>>  resourcesLookup(Map<String, Integer> concepts) throws IOException
	{
		Map<String, List<String>> conceptURIs = new HashMap<String, List<String>>(); 
		
		QueryEngine queryengine = new QueryEngine() ;
		Map<String, Integer> surfaceForm = null ;
		List<String> tempuris = null ; 
		List<String> tempuris1 = null ; 
		List<String> tempuris2 = null ; 
		List<String> tempuris3 = null ;
		List<String> uris = new ArrayList<String>() ;
		
		for(String concept : concepts.keySet())
			
		{		  

			   surfaceForm  = surfaceFormDiscovery.getsurfaceFormMesh(concept);
			   if (surfaceForm != null)
			   {
				   
				   for(String term : surfaceForm.keySet()) 
				   {
					   String[] tokens  = term.split("@") ;
					   term = tokens[0] ;
					  
					   tempuris = queryengine.bio2rdfResourceLookup(term, pred, urilimit) ;
					   //tempuris1 =  QueryEngine.BioResourceLookup(term, urilimit) ;
					  // tempuris2 =  queryengine.LODResourceLookup(term, pred,urilimit) ;
					// tempuris3 =  queryengine.lifedataResourceLookup(term, pred,urilimit) ;
					   
						 if ( tempuris != null && tempuris.size() > 0 )  
		                      uris.addAll(tempuris) ;
						 if ( tempuris1 != null && tempuris1.size() > 0 )  
		                     uris.addAll(tempuris1) ;
						 if ( tempuris2 != null && tempuris2.size() > 0)  
		                     uris.addAll(tempuris2) ;
						 if ( tempuris3 != null && tempuris3.size() > 0)  
		                     uris.addAll(tempuris3) ;
				   }

			   }
			   
			   conceptURIs.put(concept, uris) ;

		}
		
		return conceptURIs ;
	}
	
	public static  Map<String, List<String>> TriplesRetrieval (Map<String, List<String>> resources) throws  IOException
	 { 
	   Map<String, List<String>> triples =  new HashMap <String, List<String>>() ;

       try 
       {
 
    	  
    	 ResultSet results = null ; 
    	 QueryEngine queryengine = new QueryEngine() ;
    	 for (String concept: resources.keySet())
    	 {
    		 List<String> UIRs = resources.get(concept) ;
    		 List<String> statements=  new ArrayList<String>() ;
	    	 for (String URI: UIRs)
	    	 {	
	    		try 
	    		{
		    		results = QueryEngine.bio2rdfquery(URI);
		        	for (; results.hasNext();) 
		 			{
		 			    // Result processing is done here.
		 		         QuerySolution soln = results.nextSolution() ;
		 		         String Prediate = soln.get("p").toString();  //get the predicate
		 		         String object = soln.get("o").toString();  //get the object
		 		         String triple = URI +"#"+ Prediate + "#" + object ;
		 		         statements.add(triple);
		 		         System.out.println(triple) ;
		 			}
	    		}
    	       catch (Exception e)
    	       {
    	    	   System.out.println(e) ;
    	       }
	    	 }
	    	 triples.put(concept, statements);
    	 }
       }
       catch (Exception e)
       {
    	   System.out.println(e) ;
       }
	     
	   return   triples ; 
	       
	 }
}
