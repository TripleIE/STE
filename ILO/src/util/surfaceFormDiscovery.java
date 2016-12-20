package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class surfaceFormDiscovery {
	
	public static Map<String, Integer> getsurfaceFormLLD(String concept) throws IOException
	{
		Map<String, Integer> surfaceForm = new HashMap<String, Integer>() ;
		surfaceForm.put(concept,1) ;  

		String queryString=
				"PREFIX p: <http://dbpedia.org/property/>"+
				"PREFIX dbpedia: <http://dbpedia.org/resource/>"+
				"PREFIX category: <http://dbpedia.org/resource/Category:>"+
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"+
				"PREFIX geo: <http://www.georss.org/georss/>"+
				"PREFIX w3: <http://www.w3.org/2002/07/owl#>"+
				"select distinct ?label  where {?riskfactor skos:altLabel|skos:prefLabel|rdfs:label " + "\"" + concept.trim() + "\" ."  
						+ "?riskfactor skos:altLabel|skos:prefLabel|rdfs:label  ?label" +  "} ";
		
		// now creating query object
		try
		{
			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.sparqlService("http://linkedlifedata.com/sparql", query);
			//QueryExecution qexec = QueryExecutionFactory.sparqlService("http://lod.openlinksw.com/sparql", query);
			ResultSet results ;
			qexec.setTimeout(30000);
			results = qexec.execSelect(); 
			for (; results.hasNext();) 
			{
			    // Result processing is done here.
		         QuerySolution soln = results.nextSolution() ;
		         surfaceForm.put((soln.get("label").toString()),1) ;
		         
		    }

			return surfaceForm ;
		}
		catch(QueryParseException e)
		{
		}
		catch (Exception e)
		{
			
			e.printStackTrace();
		}
		return surfaceForm;
		
		
	}
	
	public static Map<String, Integer> getsurfaceFormMesh(String concept) throws IOException
	{
		Map<String, Integer> surfaceForm = new HashMap<String, Integer>() ;
		surfaceForm.put(concept,1) ;  

		String queryString =
				
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + 
				"PREFIX meshv: <http://id.nlm.nih.gov/mesh/vocab#>" + 
				"PREFIX mesh: <http://id.nlm.nih.gov/mesh/>" + 
				"PREFIX mesh2015: <http://id.nlm.nih.gov/mesh/2015/>" + 
				"PREFIX mesh2016: <http://id.nlm.nih.gov/mesh/2016/>" + 
				"PREFIX mesh2017: <http://id.nlm.nih.gov/mesh/2017/>" + 
				"select distinct ?label  where { " +
				"?d a meshv:Descriptor. " +   
				"?d meshv:concept ?c ." +
				"?d rdfs:label ?dName ." +
				"?c rdfs:label ?cName." +
				"?c meshv:term ?t ." +
				"?t rdfs:label ?label  " + 
				"FILTER(REGEX(?dName," + "'^" + concept + "$'" + " ,'i')" + "|| REGEX(?dName," + "'^" + concept + "$'" + " ,'i')"+ ")}"  ;
		
				// "FILTER(REGEX(?dName," + "'" + concept.trim() + "'" + ",'i' )" + ")}" /* + "  || REGEX(?cName," + concept  + ",'i')) }"*/ ;
		
		// now creating query object
		try
		{
			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.sparqlService("http://id.nlm.nih.gov/mesh/sparql?inference=true", query);
			//QueryExecution qexec = QueryExecutionFactory.sparqlService("http://lod.openlinksw.com/sparql", query);
			ResultSet results ;
			qexec.setTimeout(30000);
			results = qexec.execSelect(); 			
			for (; results.hasNext();) 
			{
			    // Result processing is done here.
		         QuerySolution soln = results.nextSolution() ;
		         surfaceForm.put((soln.get("label").toString()),1) ;
		         
		    }

			return surfaceForm ;
		}
		catch(QueryParseException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			
			e.printStackTrace();
		}
		return surfaceForm;
		
		
	}

}
