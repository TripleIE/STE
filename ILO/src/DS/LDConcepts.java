package DS;

import java.util.ArrayList;
import java.util.List;

import util._Entity;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

public class LDConcepts {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static Boolean EntityMentionDetection(String mention)
	{

		Boolean booleanask = false ;
		if ( !(booleanask = EntityMentionDetectionLLD(mention)))
			if (!(booleanask = EntityMentionDetectionBio(mention)))
				booleanask = EntityMentionDetectionLOD(mention) ;
		
		return booleanask;
		
	}
	
	public static Boolean EntityMentionDetectionLLD(String mention)
	{
		

		List<_Entity> entitymentions = new ArrayList<_Entity>() ;
		String queryString=
				"PREFIX p: <http://dbpedia.org/property/>"+
				"PREFIX dbpedia: <http://dbpedia.org/resource/>"+
				"PREFIX category: <http://dbpedia.org/resource/Category:>"+
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"+
				"PREFIX geo: <http://www.georss.org/georss/>"+
				"PREFIX w3: <http://www.w3.org/2002/07/owl#>"+
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
		        "ASK " +
			    "{ " +
		                   "?entity ?predicate" +  " \"" +   mention +  "\". "   +
		                  "?entity rdf:type skos:Concept" + 
		        " } "  ;
		

		
		// now creating query object
		try
		{
			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.sparqlService("http://linkedlifedata.com/sparql", query);
			Boolean results ;
			qexec.setTimeout(30000);
			results = qexec.execAsk(); 
			return results ;
		}
		catch(QueryParseException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		return false;
		
	}

	
	public static Boolean EntityMentionDetectionBio(String mention)
	{
		
	try
		{
			//APIkey 
			//396993d0-4ce2-4123-93de-214e9b9ebcf2
			String queryString=
					"PREFIX p: <http://dbpedia.org/property/>"+
					"PREFIX dbpedia: <http://dbpedia.org/resource/>"+
					"PREFIX category: <http://dbpedia.org/resource/Category:>"+
					"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
					"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"+
					"PREFIX geo: <http://www.georss.org/georss/>"+
					"PREFIX w3: <http://www.w3.org/2002/07/owl#>"+
					"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			        "Select * " +
				    "{ " +
			                   "?entity ?predicate  ?label."   +
			                  "?entity rdf:type skos:Concept" + 
			        " FILTER (CONTAINS ( UCASE(str(?label)), "  + "\"" +   mention.toUpperCase() +  "\") ) } LIMIT 1"  ;
			
/*			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.sparqlService("http://linkedlifedata.com/sparql", query);
			Boolean results ;
			qexec.setTimeout(30000);
			results = qexec.execAsk(); 
			return results ;*/
			
			// ask did not work
			 Query query = QueryFactory.create(queryString) ;
           
			 // http://sparql.bioontology.org/
			 // "http://sparql.bioontology.org/sparql/"
	         QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest("http://sparql.bioontology.org/sparql/", query); 		                      
	         qexec.addParam("apikey", "396993d0-4ce2-4123-93de-214e9b9ebcf2") ;
	         ResultSet results1 ;
	         qexec.setTimeout(300000);
	         results1 = qexec.execSelect() ;
	         for (; results1.hasNext();) 
	         {
	        	 return true ;
/*		         QuerySolution soln = results1.nextSolution() ;
		         String subj = soln.get("entity").toString();  //get the subject
		         System.out.println(subj) ;*/
	         }

		}
		catch(QueryParseException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			
			e.printStackTrace();
		}
		return false;
		
	}
	public static Boolean EntityMentionDetectionLOD(String mention)
	{
		
	try
		{
			//APIkey 
			//396993d0-4ce2-4123-93de-214e9b9ebcf2

		// get the concept 
		String queryString=
				"PREFIX p: <http://dbpedia.org/property/>"+
				"PREFIX dbpedia: <http://dbpedia.org/resource/>"+
				"PREFIX category: <http://dbpedia.org/resource/Category:>"+
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"+
				"PREFIX geo: <http://www.georss.org/georss/>"+
				"PREFIX w3: <http://www.w3.org/2002/07/owl#>"+
				"PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>"+

		        "ASK " +
			    "where { " +
		                   "?concept rdfs:label ?label." +
			               "?label <bif:contains> '" +"\"" +   mention +  "\"" + "'." + 
		                   "?concept rdfs:comment|dbpedia-owl:abstract ?def." +   // concept shell have a definition 
		                  
		           " } "  ;

		
/*			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.sparqlService("http://linkedlifedata.com/sparql", query);
			Boolean results ;
			qexec.setTimeout(30000);
			results = qexec.execAsk(); 
			return results ;*/
			
			// ask did not work
			 Query query = QueryFactory.create(queryString) ;
           
			 // http://sparql.bioontology.org/
			 // "http://sparql.bioontology.org/sparql/"
	         QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest("http://dbpedia.org/sparql", query); 		                      
	         Boolean results1 ;
	         qexec.setTimeout(300000);
	         results1 = qexec.execAsk();
/*	         for (; results1.hasNext();) 
	         {
	        	 return true ;
		         QuerySolution soln = results1.nextSolution() ;
		         String subj = soln.get("entity").toString();  //get the subject
		         System.out.println(subj) ;
	         }*/
	         

           return results1 ;
		}
		catch(QueryParseException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			
			e.printStackTrace();
		}
		return false;
		
	}
}
