package TE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import util.Dataset;
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
				title = "Antiandrogenic therapy can cause coronary arterial disease." ;
				// title = "runny nose is the symptom of common cold" ;
				temptitles.put(title,GoldSndconcepts) ;
				break ; 
			}
			
			Map<String, Integer> allconcepts =  ReadXMLFile.Deserializedir("F:\\eclipse64\\eclipse\\concepts") ; 
			if (allconcepts == null)
			{
			   allconcepts  = ConceptsDiscovery.getconcepts(temptitles);
			   ReadXMLFile.Serializeddir(allconcepts, "F:\\eclipse64\\eclipse\\concepts");
			}
			
			
			Map<String, Dataset> lookupresources = ReadXMLFile.Deserializersrc( "F:\\eclipse64\\eclipse\\resources"); 
			if (lookupresources == null)
			{
				lookupresources = resourcesSemanticLookup(allconcepts) ;
				ReadXMLFile.Serializedsrc(lookupresources, "F:\\eclipse64\\eclipse\\resources");
			}
			
			triples = TriplesRetrieval (lookupresources) ;
			ReadXMLFile.Serialized(triples, "F:\\eclipse64\\eclipse\\triples");
		}

		Map<String,List<String>> TripleCandidates = new HashMap<String, List<String>>();
		Map<String,List<String>> Tripleresult = new HashMap<String, List<String>>();
		
		Tripleresult  = syntactic.getExactMatch(triples,"Antiandrogenic therapy can cause coronary arterial disease.") ;	
		if (Tripleresult != null)
			TripleCandidates = addTriple (Tripleresult,TripleCandidates);
		printtriples(TripleCandidates) ;
		Tripleresult  =  syntactic.getSimilairMatch(triples,"Antiandrogenic therapy can cause coronary arterial disease.") ;
		if (Tripleresult != null)
			TripleCandidates = addTriple (Tripleresult,TripleCandidates); 
		
		printtriples(TripleCandidates) ;
		TripleCandidates = getProperty(TripleCandidates) ;
		printtriples(TripleCandidates) ;
	
		
        return ; 
	}
	public static Map<String,List<String>> addTriple(Map<String,List<String>> source, Map<String,List<String>> dest)
	{
		Map<String,List<String>> TripleCandidates = new HashMap<String, List<String>>();
		TripleCandidates.putAll(dest);
		
		if (TripleCandidates.size() <= 0 )
		{
			TripleCandidates.putAll(source);
			return TripleCandidates ; 
		}
		
		for (String concept : source.keySet())
		{
			
			System.out.println("***********************" + concept + "**************************") ;
			// get triples for specific concepts
			List<String> _triplesdest = TripleCandidates.get(concept) ;
			List<String> _triplesrc = source.get(concept) ;
			
			
			
			 for (String triple: _triplesrc)
			 {

				 _triplesdest.add(triple) ;
			 }
		}
		
		return TripleCandidates ;
	}
	
	public static Map<String,List<String>> getProperty(Map<String,List<String>>  TripleCandidates )
	{
		Map<String,List<String>> _TripleCandidates = TripleCandidates ;
		QueryEngine queryengine = new QueryEngine() ;
		for (String concept : TripleCandidates.keySet())
		{
			
			System.out.println("***********************" + concept + "**************************") ;
			// get triples for specific concepts
			List<String> _triples = TripleCandidates.get(concept) ;
			List<String> _triplelist  = new ArrayList<String>()  ;

			
			 for (String triple: _triples)
			 {
				 String onto[] = triple.split("\\*") ;
				 String tokens[] = onto[1].split("~") ;
				 ResultSet results = queryengine.TriplePropertyaquery(tokens[0],onto[0]) ; 
				 _triplelist.addAll(getstatements(results,tokens[0],onto[0]));

			 }
			 _triples.addAll(_triplelist) ;
		}
		
		return _TripleCandidates ;
	}

	public static Map<String, Dataset>  resourcesLookup(Map<String, Integer> concepts) throws IOException
	{
		
		Map<String, Dataset> conceptURIs = new HashMap<String, Dataset>();
		QueryEngine queryengine = new QueryEngine() ;
		Map<String, Integer> surfaceForm = null ;
		List<String> tempuris = null ; 
		List<String> tempuris1 = null ; 
		List<String> tempuris2 = null ; 
		List<String> tempuris3 = null ;
		List<String> tempuris4 = null ;
		List<String> tempuris5 = null ;

		
		for(String concept : concepts.keySet())
			
		{		  
			   List<String> uris = new ArrayList<String>() ;
			   List<String> uris1 = new ArrayList<String>() ;
			   List<String> uris2 = new ArrayList<String>() ;
			   List<String> uris3 = new ArrayList<String>() ;
			   List<String> uris4 = new ArrayList<String>() ;
			   List<String> uris5 = new ArrayList<String>() ;
			   
			   Dataset dataset = new Dataset();  		  
			   tempuris = queryengine.bio2rdfResourceLookup(concept, pred, urilimit) ;
			   tempuris1 =  QueryEngine.BioResourceLookup(concept, urilimit) ;
			   tempuris2 =  queryengine.LODResourceLookup(concept, pred,urilimit) ;
			   //tempuris3 =  queryengine.lifedataResourceLookup(term, pred,urilimit) ;
			   tempuris4 =  QueryEngine.BeeResourceLookup(concept, urilimit) ;
			   tempuris5 =  QueryEngine.DBpResourceLookup(concept, pred, urilimit) ;
			 
			   
				 if ( tempuris != null && tempuris.size() > 0 ) 
				 {
                      uris.addAll(tempuris) ; 
				 }
				 if ( tempuris1 != null && tempuris1.size() > 0 )  
				 {
                     uris1.addAll(tempuris1) ;
				 }
				 if ( tempuris2 != null && tempuris2.size() > 0)  
				 {
                     uris2.addAll(tempuris2) ;
				 }
				 if ( tempuris3 != null && tempuris3.size() > 0)  
				 {
                     uris3.addAll(tempuris3) ;
                     
				 }
				 if ( tempuris4 != null && tempuris4.size() > 0)  
				 {
                     uris4.addAll(tempuris4) ;
                     
				 }
				 if ( tempuris5 != null && tempuris5.size() > 0)  
				 {
                     uris5.addAll(tempuris5) ;
                     
				 }
			   

				   
				 if ( uris.size() > 0 ) 
				 {
                      dataset.Setonto("http://bio2rdf.org/sparql", uris);
				 }
				 if ( uris1.size() > 0 )  
				 {

                     dataset.Setonto("http://sparql.bioontology.org/sparql/", uris1);
				 }
				 if ( uris2.size() > 0)  
				 {
                     dataset.Setonto("http://lod.openlinksw.com/sparql", uris2);
				 }
				 if (uris3.size() > 0)  
				 {

                     dataset.Setonto("http://linkedlifedata.com/sparql", uris3);
                     
                     
				 }
				 if ( uris4.size() > 0)  
				 {
                     dataset.Setonto("http://sparql.hegroup.org/sparql/", uris4);
                     
				 }
			   
				 if ( uris5.size() > 0)  
				 {
                     dataset.Setonto("http://dbpedia.org/sparql", uris5);
                     
				 }
				 
			   conceptURIs.put(concept, dataset) ;

		}
		
		return conceptURIs ;
	}
	
	public static Map<String, Dataset>  resourcesSemanticLookup(Map<String, Integer> concepts) throws IOException
	{
		
		Map<String, Dataset> conceptURIs = new HashMap<String, Dataset>();
		QueryEngine queryengine = new QueryEngine() ;
		Map<String, Integer> surfaceForm = null ;
		List<String> tempuris = null ; 
		List<String> tempuris1 = null ; 
		List<String> tempuris2 = null ; 
		List<String> tempuris3 = null ;
		List<String> tempuris4 = null ;
		List<String> tempuris5 = null ;

		
		for(String concept : concepts.keySet())
			
		{		  
			   List<String> uris = new ArrayList<String>() ;
			   List<String> uris1 = new ArrayList<String>() ;
			   List<String> uris2 = new ArrayList<String>() ;
			   List<String> uris3 = new ArrayList<String>() ;
			   List<String> uris4 = new ArrayList<String>() ;
			   List<String> uris5 = new ArrayList<String>() ;
			   
			   Dataset dataset = new Dataset(); 
			   surfaceForm  = surfaceFormDiscovery.getsurfaceFormMesh(concept); 
			   if (surfaceForm != null)
			   {
				   
				   for(String term : surfaceForm.keySet()) 
				   {
					   String[] tokens  = term.split("@") ;
					   term = tokens[0] ;
					  
					   tempuris = queryengine.bio2rdfResourceLookup(term, pred, urilimit) ;
					   tempuris1 =  QueryEngine.BioResourceLookup(term, urilimit) ;
					   tempuris2 =  queryengine.LODResourceLookup(term, pred,urilimit) ;
					   //tempuris3 =  queryengine.lifedataResourceLookup(term, pred,urilimit) ;
					   tempuris4 =  QueryEngine.BeeResourceLookup(term, urilimit) ;
					   tempuris5 =  QueryEngine.DBpResourceLookup(term, pred, urilimit) ;
					 
					   
						 if ( tempuris != null && tempuris.size() > 0 ) 
						 {
		                      uris.addAll(tempuris) ; 
						 }
						 if ( tempuris1 != null && tempuris1.size() > 0 )  
						 {
		                     uris1.addAll(tempuris1) ;
						 }
						 if ( tempuris2 != null && tempuris2.size() > 0)  
						 {
		                     uris2.addAll(tempuris2) ;
						 }
						 if ( tempuris3 != null && tempuris3.size() > 0)  
						 {
		                     uris3.addAll(tempuris3) ;
		                     
						 }
						 if ( tempuris4 != null && tempuris4.size() > 0)  
						 {
		                     uris4.addAll(tempuris4) ;
		                     
						 }
						 if ( tempuris5 != null && tempuris5.size() > 0)  
						 {
		                     uris5.addAll(tempuris5) ;
		                     
						 }
				   }

				   
					 if ( uris.size() > 0 ) 
					 {
	                      dataset.Setonto("http://bio2rdf.org/sparql", uris);
					 }
					 if ( uris1.size() > 0 )  
					 {

	                     dataset.Setonto("http://sparql.bioontology.org/sparql/", uris1);
					 }
					 if ( uris2.size() > 0)  
					 {
	                     dataset.Setonto("http://lod.openlinksw.com/sparql", uris2);
					 }
					 if (uris3.size() > 0)  
					 {

	                     dataset.Setonto("http://linkedlifedata.com/sparql", uris3);
	                     
	                     
					 }
					 if ( uris4.size() > 0)  
					 {
	                     dataset.Setonto("http://sparql.hegroup.org/sparql/", uris4);
	                     
					 }
				   
					 if ( uris5.size() > 0)  
					 {
	                     dataset.Setonto("http://dbpedia.org/sparql", uris5);
	                     
					 }

			   }
			   
			   conceptURIs.put(concept, dataset) ;

		}
		
		return conceptURIs ;
	}
	public static  Map<String, List<String>> TriplesRetrieval (Map<String, Dataset> resources) throws  IOException
	 { 
	   Map<String, List<String>> triples =  new HashMap <String, List<String>>() ;

       try 
       {

    	 ResultSet results = null ; 
    	 QueryEngine queryengine = new QueryEngine() ;
    	 for (String concept: resources.keySet())
    	 {
    		 Dataset dataset = resources.get(concept) ;
    		 List<String> statements=  new ArrayList<String>() ;
        	 for (String onto: dataset.getonto().keySet())
        	 { 
        		 List<String> UIRs = dataset.getontoURIs(onto) ;
		    	 for (String URI: UIRs)
		    	 {	
		    		try 
		    		{
		    			if (onto.contains("bio2rdf"))
		    			{
			    		   results = QueryEngine.bio2rdfquery(URI);
	                       statements.addAll(getstatements(results,URI,onto)) ;

		    			}
		    			
		    			if (onto.contains("bioontology"))
		    			{
			    		   results = QueryEngine.bioPortaluery(URI);
			    		   statements.addAll(getstatements(results,URI,onto)) ;

		    			}
		    			
		    			if (onto.contains("openlinksw"))
		    			{
			    		   results = QueryEngine.LODquery(URI);
			    		   statements.addAll(getstatements(results,URI,onto)) ;

		    			}
		    			
		    			if (onto.contains("linkedlifedata"))
		    			{
			    		   results = QueryEngine.lifedataquery(URI);
			    		   statements.addAll(getstatements(results,URI,onto)); 

		    			}
		    			
		    			if (onto.contains("hegroup"))
		    			{
			    		   results = QueryEngine.beerdfquery(URI);
			    		   statements.addAll(getstatements(results,URI,onto)) ;
		    			}
		    			
		    			if (onto.contains("dbpedia"))
		    			{
			    		   results = QueryEngine.dbpediaquery(URI);
			    		   statements.addAll(getstatements(results,URI,onto)) ;
		    			}
			        	
		    		}
	    	       catch (Exception e)
	    	       {
	    	    	   System.out.println(e) ;
	    	       }
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
	
	
	public static List<String> getstatements(ResultSet results,String URI,String onto)
	{
		List<String> statements=  new ArrayList<String>() ;
		try 
		{

        	for (; results.hasNext();) 
 			{
 			    // Result processing is done here.
 		         QuerySolution soln = results.nextSolution() ;
 		         String Prediate = soln.get("p").toString();  //get the predicate
 		         String object = soln.get("o").toString();  //get the object
 		         String objectlabel = "" ;
 		         try
 		         {
 		        	 objectlabel = soln.getLiteral("label").toString();  //get the object
 		         }
 		         catch (Exception e)
 		         {
	    	    	  objectlabel = "" ;
 		         }
 		         String triple = onto + "*" +  URI +"~"+ Prediate + "~" + object + "~"+ objectlabel;
 		         statements.add(triple);
 		         System.out.println(triple) ;
 			}
		}
       catch (Exception e)
       {
    	   System.out.println(e) ;
       }
		
		return statements ;
	}
	
	public static  Map<String, List<String>> TriplesRetrievalsigle (Map<String, List<String>> resources) throws  IOException
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
		    		results = QueryEngine.bioPortaluery(URI);
		        	for (; results.hasNext();) 
		 			{
		 			    // Result processing is done here.
		 		         QuerySolution soln = results.nextSolution() ;
		 		         String Prediate = soln.get("p").toString();  //get the predicate
		 		         String object = soln.get("o").toString();  //get the object
		 		         String objectlabel = "" ;
		 		         try
		 		         {
		 		        	 objectlabel = soln.getLiteral("label").toString();  //get the object
		 		         }
		 		         catch (Exception e)
		 		         {
	 	    	    	  objectlabel = "" ;
		 		         }
		 		         String triple = URI +"~"+ Prediate + "~" + object + "~"+ objectlabel;
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
	
	public static void printtriples(Map<String, List<String>> triples)
	{
		for (String concept : triples.keySet())
		{
			
			System.out.println("***********************" + concept + "**************************") ;
			// get triples for specific concepts
			List<String> _triples = triples.get(concept) ;
			
			 for (String triple: _triples)
			 {
				 System.out.println(triple) ;
			 }
		}
		
	}
}
