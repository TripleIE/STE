package TE;

import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import DS.ConceptsDiscovery;
import DS.QueryEngine;
import util.Dataset;
import util.ReadXMLFile;
import util.readfiles;
import util.surfaceFormDiscovery;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
public class client {

	
	// adapted from String.hashCode()
	public static long hash(String string) {
	  long h = 1125899906842597L; // prime
	  int len = string.length();

	  for (int i = 0; i < len; i++) {
	    h = 31*h + string.charAt(i);
	  }
	  
	  return h;
	}
	
	
	public static  ResultSet JenaSparqlExample1LLD(String entity) {

		//Querying remote SPARQL services	
			ResultSet results = null ; 
			try 
			{
			// String ontology_service = "http://lod.openlinksw.com/sparql";
			   String ontology_service = "http://linkedlifedata.com/sparql";
			
			String sparqlQuery=
					"PREFIX p: <http://dbpedia.org/property/>"+
					"PREFIX dbpedia: <http://dbpedia.org/resource/>"+
					"PREFIX category: <http://dbpedia.org/resource/Category:>"+
					"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
					"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"+
					"PREFIX geo: <http://www.georss.org/georss/>"+
					"PREFIX w3: <http://www.w3.org/2002/07/owl#>" +
				    //"select distinct  ?o ?p where {<http://dbpedia.org/resource/Michelle_Obama> ?p ?o. } ";
			       // " Select Distinct  ?concept1 ?concept2 WHERE {?concept1 skos:exactMatch ?concept2. FILTER(?concept1 != ?concept2) }";
				    " Select Distinct  ?concept1  ?labelconcept1  ?concept2  ?labelconcept2  WHERE {?concept1 skos:related ?concept2. " 
                    +"?concept1 skos:altLabel|skos:prefLabel|rdfs:label ?labelconcept1."
                    + "?concept2 skos:altLabel|skos:prefLabel|rdfs:label ?labelconcept2." 
                    + "FILTER(?concept1 != ?concept2) }limit 2" ;

			QueryExecution x = QueryExecutionFactory.sparqlService(ontology_service, sparqlQuery);
			 results = x.execSelect();
			 List<String> triple = new ArrayList<String>();
			for (; results.hasNext();) 
			{
				// Result processing is done here.
		         QuerySolution soln = results.nextSolution() ;
				 System.out.println(soln.get("concept1").toString());
				 triple.add(soln.get("concept1").toString()+ ":" + soln.get("concept2").toString()) ;
			}
			
			ReadXMLFile.Serializeddir(triple,"F:\\eclipse64\\eclipse\\Related") ;
				
			}
			catch(QueryParseException e)
			{
				System.out.println(e.getMessage()); 
				return null ; 
			}
			catch (Exception e)
			{
				System.out.println(e.getMessage()); 
				 return null ; 
			}
			//ResultSetFormatter.out(System.out, results);
			 return results ;
		}
	public static  ResultSet JenaSparqlExample1(String entity) {

		//Querying remote SPARQL services	
			ResultSet results = null ; 
			try 
			{
			// String ontology_service = "http://lod.openlinksw.com/sparql";
			   String ontology_service = "http://sparql.hegroup.org/sparql/";
			
			String sparqlQuery=
					"PREFIX p: <http://dbpedia.org/property/>"+
					"PREFIX dbpedia: <http://dbpedia.org/resource/>"+
					"PREFIX category: <http://dbpedia.org/resource/Category:>"+
					"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
					"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"+
					"PREFIX geo: <http://www.georss.org/georss/>"+
					"PREFIX w3: <http://www.w3.org/2002/07/owl#>" +
				    //"select distinct  ?o ?p where {<http://dbpedia.org/resource/Michelle_Obama> ?p ?o. } ";
			        "select distinct  ?s  where {?s <http://www.geneontology.org/formats/oboInOwl#hasOBONamespace> ?onto. Filter (regex(?onto,\"disease_ontology\")) } ";

			QueryExecution x = QueryExecutionFactory.sparqlService(ontology_service, sparqlQuery);
			 results = x.execSelect();
			 List<String> atts = new ArrayList<String>();
				for (; results.hasNext();) 
				{
					// Result processing is done here.
			         QuerySolution soln = results.nextSolution() ;
					 System.out.println(soln.get("s").toString());
					 atts.add(soln.get("s").toString()) ;
				}
				List<String> atts1 = new ArrayList<String>(atts);
				for (String s:atts)
				{
					for (String s1:atts1)
					{
						
						
						
						String sparqlQuery1=
								"PREFIX p: <http://dbpedia.org/property/>"+
								"PREFIX dbpedia: <http://dbpedia.org/resource/>"+
								"PREFIX category: <http://dbpedia.org/resource/Category:>"+
								"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
								"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"+
								"PREFIX geo: <http://www.georss.org/georss/>"+
								"PREFIX w3: <http://www.w3.org/2002/07/owl#>" +
							    //"select distinct  ?o ?p where {<http://dbpedia.org/resource/Michelle_Obama> ?p ?o. } ";
						        "select distinct  ?rel  where {<" +  s + ">" + " ?rel " + "<" +  s1 + "> " + " } ";

						QueryExecution x1 = QueryExecutionFactory.sparqlService(ontology_service, sparqlQuery1);
						results = x1.execSelect();
							for (; results.hasNext();) 
							{
								// Result processing is done here.
						         QuerySolution soln = results.nextSolution() ;
								 System.out.println(soln.get("rel").toString());

							}
						
						
					}
				}
				
			}
			

			
			
			
			
			catch(QueryParseException e)
			{
				System.out.println(e.getMessage()); 
				return null ; 
			}
			catch (Exception e)
			{
				System.out.println(e.getMessage()); 
				 return null ; 
			}
			//ResultSetFormatter.out(System.out, results);
			 return results ;
		}
	
	public static Boolean EntityMentionDetectionBio(String concept)
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
					"select distinct  ?p  where {?o ?p  ?o. } ";
			
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
			    QuerySolution soln = results1.nextSolution() ;
		         String subj = soln.get("p").toString();  //get the subject
		         System.out.println(subj) ;
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
	
	
	// this function return whole subgraph of resource of certain depth
	public static Model GetSubGraph(String URI, Model model, int depth) throws IOException
	{
		 

		//for (int i = 1; i < level ; i++)
		{
			inferenc.likehoodrel(URI,false, depth,1,model);
		} 
		
		return model ; 
	}
	
	
	public static void main(String[] args) throws IOException 
	{
		// TODO Auto-generated method stub
		
		//JenaSparqlExample1("kk") ;
		//EntityMentionDetectionBio("kk") ; 
		//JenaSparqlExample1LLD("kk") ;
		Map<String,Map<String,List<String>>> trainset = null ; 
		Map<String, List<String>> titles =  ReadXMLFile.ReadCDR_TestSet_BioC()  ;
		
		
		trainset  = ReadXMLFile.DeserializeT("F:\\eclipse64\\eclipse\\TrainsetTest") ;
		LinkedHashMap<String, Integer> TripleDict  = new LinkedHashMap<String, Integer>();
		Map<String,List<Integer>> Labeling= new HashMap<String,List<Integer>>() ;
		
		MetaMapApi api = new MetaMapApiImpl();
		List<String> theOptions = new ArrayList<String>();
	    theOptions.add("-y");  // turn on Word Sense Disambiguation
	    theOptions.add("-u");  //  unique abrevation 
	    theOptions.add("--negex");  
	    theOptions.add("-v");
	    theOptions.add("-c");   // use relaxed model that  containing internal syntactic structure, such as conjunction.
	    if (theOptions.size() > 0) {
	      api.setOptions(theOptions);
	    }
	    
		
		if (trainset == null )
		{
			trainset = new HashMap<String, Map<String,List<String>>>();
		}
		
		
		/************************************************************************************************/
		//Map<String, Integer> bagofwords = semantic.getbagofwords(titles) ; 
		//trainxmllabeling(trainset,bagofwords); 
		/************************************************************************************************/
		
		
		int count  = 0 ;
		Model candidategraph = ModelFactory.createDefaultModel(); 
		for(String title : titles.keySet())
		{
			
			Model Sentgraph = ModelFactory.createDefaultModel();
			if (trainset.containsKey(title))
				continue ; 
			
			count++ ; 
			Map<String,List<String>> TripleCandidates = new HashMap<String, List<String>>();
			Map<String, List<String>> triples = null ;
			// get the goldstandard concepts for current title 
			List<String> GoldSndconcepts = titles.get(title); 
			Map<String, Integer> allconcepts = null ; 
			
			// this is optional and not needed here , it used to measure the concepts recall 
			Map<String, List<String>> temptitles = new HashMap<String, List<String>>(); 
			temptitles.put(title,GoldSndconcepts) ;
			
			// Khaild
/*			Map<String,List<String>> ConcpFormstem = new HashMap<String, List<String>>();
			Map<String,Integer> wordformstem = new HashMap<String, Integer>();
			for (String concptem:GoldSndconcepts)
			{
				List<String> form1 = new ArrayList<String>()  ;
				wordformstem  = surfaceFormDiscovery.getsurfaceFormMesh(concptem);
				for (String form:wordformstem.keySet())
				{
					form1.add(form) ;
				}
				ConcpFormstem.put(concptem, form1)  ;
			}
			
			ReadXMLFile.Serialized(ConcpFormstem, "F:\\eclipse64\\eclipse\\forms");*/
			
			// get the concepts 
			allconcepts  = ConceptsDiscovery.getconcepts(temptitles,api);

			
			
			
			//featureextractor.getFeatures(title,allconcepts);
			
			// Enrichment  
/*			Map<String,List<String>> ConcpForms = new HashMap<String, List<String>>();
			Map<String,Integer> wordforms = new HashMap<String, Integer>();
			for (String concp:allconcepts.keySet())
			{
				List<String> form1 = new ArrayList<String>()  ;
				wordforms  = surfaceFormDiscovery.getsurfaceFormMesh(concp);
				for (String form:wordforms.keySet())
				{
					form1.add(form) ;
				}
				ConcpForms.put(concp, form1)  ;
			}*/
			
			

			
			// Ontology Lookup
			Map<String, Dataset> lookupresources =  new HashMap<String, Dataset>();
			
			for ( String cpt : allconcepts.keySet() )
			{
				 Dataset dataset = new Dataset();
				 lookupresources.put(cpt, dataset) ;
			}
			
			
			//ArrayList<String> RelInstances = syntactic.getSyntaticPattern(title,allconcepts) ;
			//ontologyfactory.getontoSyntaticPattern(RelInstances,Sentgraph,lookupresources) ;
			
			
			// Enrichment
			Enrichment.SemanticEnrich(lookupresources);
			
			// using matching RDF literals
			lookupresources = inferenc.resourcesSemanticLookup(lookupresources) ;
					
			
			// Ranking algorithm  
		    	Map<String, Dataset> ret = ranking.URIRankingLLD(allconcepts,lookupresources,api) ;
			
			// Pruning
             pruning.URIspruning(lookupresources) ;
			
			
			// construct whole subgraph for each concept
/*			for (String concept: lookupresources.keySet())
	   	 	{
				Model model = ModelFactory.createDefaultModel();
		   		Dataset dataset = lookupresources.get(concept) ;
		   		
		   		// set the lexical alt label
		   		dataset.Setaltlebel(ConcpForms.get(concept)) ;
		   		
		   		for (String onto: dataset.getonto().keySet())
		   		{
		   			List<String> UIRs = dataset.getontoURIs(onto) ;
			    	for (String URI: UIRs)
			    	{    		
			    		String[] words = URI.split("!");  
			    		//construct the whole sub graph from a given resource in RDF Graph
			    		dataset.SetGraph(GetSubGraph(words[0],model,2));
			    	}
		   		}
		   		
	   	 	}*/
			
			
			
			// generating onto of lexical representation as altLabels
			//ontologyfactory.getontoAltlabel(lookupresources) ;
			
			// generating onto
			//ontologyfactory.getontoassociate (lookupresources) ;
			
             
             // Syntax & syntactic match 
			
			triples = inferenc.TriplesRetrieval (lookupresources) ;
		    Map<String,List<String>> Tripleresult = new HashMap<String, List<String>>();	
			Tripleresult  = syntactic.getExactMatch(triples,lookupresources) ;
			
			ontologyfactory.getontosyntax(Tripleresult,lookupresources) ;
/*			if (Tripleresult != null)
			{
				TripleCandidates = inferenc.addTriple (Tripleresult,TripleCandidates,"getExactMatch");
				inferenc.printtriples(TripleCandidates) ;
			}*/
			
			Tripleresult  = syntactic.getExactMatchSynonym(triples,lookupresources) ;
			ontologyfactory.getontosyntax(Tripleresult,lookupresources) ;
			
/*			if (Tripleresult != null)
			{
				TripleCandidates = inferenc.addTriple (Tripleresult,TripleCandidates,"getExactMatch");
				inferenc.printtriples(TripleCandidates) ;
			}*/
			
			
			
			// using Syntactic patterns
			ArrayList<String> RelInstances = syntactic.getSyntaticPattern(title,allconcepts) ;
			ontologyfactory.getontoSyntaticPattern(RelInstances,Sentgraph,lookupresources) ;
			
			
			//semantic graph
			for ( String cptt : allconcepts.keySet() )
			{
				 
				 Dataset dataset = lookupresources.get(cptt);
				 Model model = dataset.getGraph() ;
				 Map<String, Double> TopUris= dataset.geturiconfident() ;
				 
				 for (String topuri : TopUris.keySet())
				 {
					 GetSubGraph(topuri,model, 2) ;
					 break ; 
				 }	 
			}
			
			
			
			//semantic graph
			for ( String cptt : allconcepts.keySet() )
			{
				 
				 Dataset dataset = lookupresources.get(cptt);
				 Model model = dataset.getGraph() ;
				 Map<String, Double> TopUris= dataset.geturiconfident() ;
				 
				 model.write(System.out, "RDF/XML-ABBREV") ;
				 
				 
				 for ( String cpttin : allconcepts.keySet() )
				 {
					 if (cptt.equals(cpttin))
						 continue ; 
					 
					 Dataset datasetin = lookupresources.get(cpttin);
					 Model modelin = datasetin.getGraph() ;
					 Model Intersection = model.intersection(modelin);
					 Intersection.write(System.out, "RDF/XML-ABBREV");
				 }
				  
			}
			
			
			
			
			
	/* 		Tripleresult  =  syntactic.getSimilairMatch(triples,title) ;
			if (Tripleresult != null)
			{
				TripleCandidates = inferenc.addTriple (Tripleresult,TripleCandidates,"SimilairMatch"); 
				inferenc.printtriples(TripleCandidates) ;
			}


				
			Tripleresult  =  semantic.getSemanticMatch(triples,title) ;
			if (Tripleresult != null)
				TripleCandidates = inferenc.addTriple (Tripleresult,TripleCandidates,"getSemanticMatch"); 

			
			//TripleCandidates = inferenc.getProperty(TripleCandidates) ;
			trainset.put(title, TripleCandidates) ;
			if (count> 30 )
			{
					ReadXMLFile.SerializedT(trainset,"F:\\eclipse64\\eclipse\\Trainset") ;
					count = 0 ;
			}*/
			

		}

	}
	
	
	public static void trainxmllabeling (Map<String,Map<String,List<String>>> trainset,Map<String, Integer> bagofwords) throws IOException
	{
		LinkedHashMap<String, Integer> TripleDict  = new LinkedHashMap<String, Integer>();
		Map<String,List<Integer>> Labeling= new HashMap<String,List<Integer>>() ;
		{
            // building the header 
			// building arff file 
			List<String> atts = new ArrayList<String>(bagofwords.keySet());
			
			
		/*		int count = 0 ; 
			for (String att : atts)
			{
				count++ ;
				String attr = "@attribute Att" + Integer.toString(count) + " numeric" ; 
				readfiles.Writestringtofile(attr,"F:\\eclipse64\\eclipse\\triplelabeling.arff" ) ;
				
			}  */
			
			 // labeling training set 
			for ( String T : trainset.keySet())
			{
				Map<String,List<String>> C =  trainset.get(T) ;
				for ( String CC : C.keySet())
				{
					List<String> TT = C.get(CC);
					for (String TTT:TT)
					{
						TripleDict.put(TTT, 1) ;				
					}
					
				}
			} 
			
			// building the  Labeling part in triplelabeling.xml
			// where each triple is a label
			List<String> indexes = new ArrayList<String>(TripleDict.keySet());
			
			
			int indx = 0 ; 
			readfiles.Writestringtofile("<?xml version=\"1.0\" encoding=\"utf-8\"?>","F:\\eclipse64\\eclipse\\triplelabeling.xml" ) ;
			readfiles.Writestringtofile("<labels xmlns=\"http://mulan.sourceforge.net/labels\">","F:\\eclipse64\\eclipse\\triplelabeling.xml" ) ;
			for (String label:indexes)
			{
				indx++ ;
				String lab = "<label name=" + "\""+ "Label" + Integer.toString(indx) +"\""+  "></label>" ; 
				String attr = "@attribute " +  "Label" + Integer.toString(indx)+ " {0,1}" ;
				readfiles.Writestringtofile(lab,"F:\\eclipse64\\eclipse\\triplelabeling.xml" ) ;
				//readfiles.Writestringtofile(attr,"F:\\eclipse64\\eclipse\\triplelabeling.arff" ) ;
			}

			readfiles.Writestringtofile("</labels>","F:\\eclipse64\\eclipse\\triplelabeling.xml" ) ;
			
			// building the data section 

			
			// built the sparse matrix
	         for ( String T : trainset.keySet()) 
				{
	        	 
	        	 
	        	   // Map<String, Integer> bagofwords1 = semantic.getbagofwords(T) ; 
	        	    
	        	    List<String> bagofwords1 = new ArrayList<String>(semantic.getbagofwords(T).keySet());
	        	    String att = "" ;
	        	    
					for(int i = 0; atts.size() > i ; i++)
					{
						if (bagofwords1.contains(atts.get(i))) 
						    att += Integer.toString(i+1)+ " 1," ;
					}
	        	    
					if (!att.isEmpty())
					{
						att = att.substring(0,att.length()-1) ;
						readfiles.Writestringtofilenonewline("{" + att,"F:\\eclipse64\\eclipse\\triplelabeling.arff" ) ;
					}
	        	    
	        	    
					Map<String,List<String>> C =  trainset.get(T) ;
					List<Integer> labels = new ArrayList<Integer>() ; 
					for ( String CC : C.keySet())
					{
						List<String> TT = C.get(CC);
						
						for (String TTT:TT)
						{
							int index = indexes.indexOf(TTT); 
							labels.add(index) ;
						}
						
					}
					
					String lab = "" ;
					for(int i = 0; indexes.size() > i ; i++)
					{
						if (labels.contains(i)) 
							lab += Integer.toString(i+1 + atts.size())+ " 1," ;
					}
					
					if (!lab.isEmpty())
					{
						lab = lab.substring(0,lab.length()-1) ;
						readfiles.Writestringtofilenonewline("," +lab+ "}","F:\\eclipse64\\eclipse\\triplelabeling.arff" ) ;
						readfiles.Writestringtofilenonewline( "new","F:\\eclipse64\\eclipse\\triplelabeling.arff" ) ;
					}
					else
					{
						
						readfiles.Writestringtofilenonewline("}","F:\\eclipse64\\eclipse\\triplelabeling.arff" ) ;
						readfiles.Writestringtofilenonewline( "new","F:\\eclipse64\\eclipse\\triplelabeling.arff" ) ;
					}
					Labeling.put(T, labels) ;
				}
	         
	         

			
		}
		
		/*	        for ( String T : trainset.keySet()) 
		{
			
			Map<String,List<String>> C =  trainset.get(T) ;
			List<Integer> labels = new ArrayList<Integer>() ; 
			for ( String CC : C.keySet())
			{
				List<String> TT = C.get(CC);
				
				for (String TTT:TT)
				{
					int index = indexes.indexOf(TTT); 
					labels.add(index) ;
				}
				
			}
			Labeling.put(T, labels) ;
		}
		
		ReadXMLFile.Serializedlabel(Labeling,"F:\\eclipse64\\eclipse\\TrainsetLabels") ;*/
	}
	
	
	/*public static Model GetSubGraph1(String URI,int level) throws IOException
	{
		 
		Model model = ModelFactory.createDefaultModel();
		for (int i = 1; i < level ; i++)
		{

			//inferenc.likehoodrel("http://purl.obolibrary.org/obo/DOID_10459",false, i,1,model);
			//RDFDataMgr.write(System.out,model, Lang.NTRIPLES);
			inferenc.likehoodrel(URI,false, i,1,model);
			
			double count1  = model.size() ;
			Model model2 = ModelFactory.createDefaultModel();
			inferenc.likehoodrel("http://purl.obolibrary.org/obo/DOID_974",false, i,1,model2);
			RDFDataMgr.write(System.out,model2, Lang.NTRIPLES);
			double count2  = model2.size() ;
			// http://purl.obolibrary.org/obo/DOID_12491
			//	"http://purl.obolibrary.org/obo/DOID_4483
			 modelgragh = model.intersection(model2);
			 double match = modelgragh.size() ;
			 System.out.print("level " + i + " = " );
			 System.out.println((match/((count1 + count2)/2)) * 100);

		}
		 
		
		//RDFDataMgr.write(System.out,modelgragh, Lang.RDFXML);
		
		// list the statements in the Model
		StmtIterator iter =  model.listStatements();
		// print out the predicate, subject and object of each statement
     	while (iter.hasNext())
		{
		    Statement stmt      = iter.nextStatement();  // get next statement
		    Resource  subject   = stmt.getSubject();     // get the subject
		    Property  predicate = stmt.getPredicate();   // get the predicate
		    RDFNode   object    = stmt.getObject();      // get the object

		    System.out.print(subject.toString());
		    System.out.print(" " + predicate.toString() + " ");
		    if (object instanceof Resource) {
		       System.out.print(object.toString());
		    } else {
		        // object is a literal
		        System.out.print(" \"" + object.toString() + "\"");
		        
		    }
		  //  lexical.put(URI, info) ;
		    System.out.println(" .");
		}    
		
		return model ; 
	}*/
	

}







