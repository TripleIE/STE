package TE;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import TextProcess.NLPEngine;
import TextProcess.removestopwords;
import edu.cmu.lti.ws4j.demo.SimilarityCalculationDemo;
import util.CosineSimilarity;
import util.Dataset;
import util.NGramAnalyzer;
import util.ReadXMLFile;
import util.predicateOntoDictionary;
import util.readfiles;

public class syntactic {
	
	
	public static void main(String[] args) throws Exception {
		  
		getSyntaticPattern(null, null) ; 
	  }

	public static ArrayList<String> getSyntaticPattern(String orgSentence, Map<String, Integer> allconcepts) throws MalformedURLException, IOException
	{
		File filename  = new File("F:\\Freepal\\medicalFreepaltest.txt") ;
		List<String> patts = readfiles.readLinesbylines(filename.toURL()) ;
		ArrayList<String> RelInstances =  new ArrayList<String>();
		for (String patt : patts)
		{
			String RelationInstance = null ; 
			String Sentence = orgSentence ;
			patt = patt.toLowerCase() ;
			String[] tokens = patt.split("!");
			RelationInstance= tokens[1] + "," ;
			patt = tokens[0];
			patt = patt.replace("[x]", "!") ; 
			patt = patt.replace("[y]", "!") ;
			patt = patt.replace("[", "!") ;
			String[] toks = patt.trim().split("!") ;
			String startP =  toks[0].trim() ;
			String middleP = toks[1].trim() ;
			String endP = toks[2].trim() ;
			Sentence = Sentence.trim() ;
			if ( !startP.isEmpty() )
			{
				// if not empty then it should be there otherwise we skip to next pattern
				if (StringUtils.containsIgnoreCase(Sentence, startP + " ")/*|| StringUtils.containsIgnoreCase( NLPEngine.getLemma(Sentence), startP + " ")*/)
				{
					Sentence = Sentence.replace(startP, "!") ;
					String[] tokss = Sentence.split("!") ;
					if (tokss.length > 1) 
						Sentence = tokss[1] ;
					else
						Sentence = "" ;
				}
				else
				{
					continue ; 
				}
			}
			else
			{
				
				Sentence = Sentence ; 
			}
			
			Sentence = Sentence.trim().replaceAll("\\s+", " ") ;
			// find the first encounter concept 
			//String[] tokenS = Sentence.split(" ") ;
			
			String Tokens[] = Sentence.split(middleP) ; 
			Map<String, Integer> mentions = new HashMap<String, Integer>();
			mentions = NGramAnalyzer.entities(1,5, Tokens[0] + middleP ) ;
			Boolean foundconcept = false ; 
			String bestmatch1 = "";
			for ( String tok : mentions.keySet())
			{
				if (allconcepts.containsKey(tok) )
				{
					if ( bestmatch1.length() < tok.length())
					{
						bestmatch1 = tok ;
						foundconcept = true ; 
						//break ;
					}
				}
			}
			
			if ( foundconcept)
			{
				RelationInstance = RelationInstance + bestmatch1 + ",";
				/*Sentence = Sentence.replace(bestmatch1 , "!") ;
				String[] tokss = Sentence.split("!") ;
				if (tokss.length > 1) 
					Sentence = tokss[1] ;
				else
					Sentence = "" ;*/
			}
			
			// no concept found 
			if (!foundconcept)
				continue ; 
			
			Sentence = Sentence.trim() ;
			if ( !middleP.isEmpty() )
			{
				String temp = NLPEngine.getLemma(Sentence) ;
				// if not empty then it should be there otherwise we skip to next pattern
				if (StringUtils.containsIgnoreCase(Sentence," " + middleP + " ") || StringUtils.containsIgnoreCase(Sentence, middleP + " ") /*|| StringUtils.containsIgnoreCase(NLPEngine.getLemma(Sentence), middleP + " ")*/)
				{
					//Sentence = Sentence.replace(middleP, "!") ;
					String[] tokss = Sentence.split(middleP) ;
					if (tokss.length > 1) 
						Sentence = tokss[1] ;
					else
						Sentence = "" ;
					 
				}
				else
				{
					continue ; 
				}
			}
			else
			{
				Sentence = Sentence.trim() ; 
			}
			
			
			Sentence = Sentence.trim() ;
			// find the second encounter concept 
			String[] tokenSs = Sentence.split(" ") ;
			mentions = NGramAnalyzer.entities(1,5, Sentence) ;
			Boolean foundconcept2 = false ; 
			String bestmatch = "";
			for ( String tok : mentions.keySet())
			{
				if (allconcepts.containsKey(tok) )
				{
					if ( bestmatch.length() < tok.length())
					{
						bestmatch = tok ;
        				foundconcept2 = true ; 
						//break ;
					}
				}
			}
			
			
			if ( foundconcept2)
			{
				RelationInstance = RelationInstance + bestmatch + ",";
				Sentence = Sentence.replace(bestmatch , "!") ;
				String[] tokss = Sentence.split("!") ;
				if (tokss.length > 1) 
					Sentence = tokss[1] ;
				else
					Sentence = "" ;
				
				foundconcept2 = true ; 
				//break ;
			}
			
			
			
			// no concept found 
			if (!foundconcept2)
				continue ; 
			
			Sentence = Sentence.trim() ;
			if ( !endP.isEmpty() )
			{
				// if not empty then it should be there otherwise we skip to next pattern
				if (!StringUtils.containsIgnoreCase(Sentence," " + endP))
				{
					continue ;
				}
			}
			else
			{
				Sentence = Sentence ; 
			}
			
			RelationInstance = RelationInstance + tokens[2].trim();
			RelInstances.add(RelationInstance) ;
			System.out.println("found") ;
				
			
			
			
			
	
		}
		return RelInstances; 
	}
	
	public static Map<String,List<String>> getExactMatchSynonym(Map<String,List<String>> triples,  Map<String, Dataset> resources)
	{		
		Map<String,List<String>> exactMatchTriples = new HashMap <String, List<String>>() ;
		Map<String,List<String>> tempconcepts = new HashMap <String, List<String>>() ;
		tempconcepts.putAll(triples);
		
		for (String concept : triples.keySet())
		{
			List<String> statement = new ArrayList<String>() ;
			// get triples for specific concepts
			List<String> _triples = triples.get(concept) ;
			
			 for (String triple: _triples)
			 {
				 try 
				 {
				 
					 // start getting the object and predicate from the triples 
					 String tokens[] =  triple.split("~") ;
					 String Object ;
					 if(tokens.length < 3)
						 continue ; 
					 // most triples have label and it located at index 4 and should be used as object 
					 if(tokens.length == 4)
					 {
						 String _tokens[] =  tokens[3].split("\\[|\\^+");
						 // removing the lang 
						 String Objects[] = _tokens[0].split("@") ;
						 Object = Objects[0] ;
					 }
					 else
					 {
						 String _tokens[] =  tokens[2].split("\\[|\\^+");
						// removing the lang 
						 String Objects[] = _tokens[0].split("@") ;
						 Object = Objects[0] ;
					 }
					 
					 
					 String temp  = tokens[0] ;
					 String subj  = temp.replaceAll("http://linkedlifedata.com/sparql","")  ;
					 
					 // skip this one , since we need to find the relation with other concepts 
					 if ( concept.equals(Object)|| Object.equals(subj.trim()))
					 {
						continue ;  
					 }
					 
					 Dataset dataset =  resources.get(concept); 
					 List<String> variation = dataset.Synonym ;
					 if (variation.contains(Object))
					 {
							 statement.add(triple) ;
					 }
				 }
				 catch(Exception e)
				 {
					continue ;  
				 }
			 }

			 
			 exactMatchTriples.put(concept, statement) ;
			
		}
		if (exactMatchTriples.size() >0)
		{
			return exactMatchTriples ;
		}
		return null; 
	}
	
	public static Map<String,List<String>> getExactMatch(Map<String,List<String>> triples,  Map<String, Dataset> resources)
	{		
		Map<String,List<String>> exactMatchTriples = new HashMap <String, List<String>>() ;
		Map<String,List<String>> tempconcepts = new HashMap <String, List<String>>() ;
		tempconcepts.putAll(triples);
		
		for (String concept : triples.keySet())
		{
			List<String> statement = new ArrayList<String>() ;
			// get triples for specific concepts
			List<String> _triples = triples.get(concept) ;
			
			 for (String triple: _triples)
			 {
				 try 
				 {
				 
					 // start getting the object and predicate from the triples 
					 String tokens[] =  triple.split("~") ;
					 String Object ;
					 if(tokens.length < 3)
						 continue ; 
					 // most triples have label and it located at index 4 and should be used as object 
					 if(tokens.length == 4)
					 {
						// String _tokens[] =  tokens[3].split("\\[|\\^+");
						 String _tokens[] =  tokens[2].split("\\[|\\^+");
						 // removing the lang 
						 String Objects[] = _tokens[0].split("@") ;
						 Object = Objects[0] ;
					 }
					 else
					 {
						 String _tokens[] =  tokens[2].split("\\[|\\^+");
						// removing the lang 
						 String Objects[] = _tokens[0].split("@") ;
						 Object = Objects[0] ;
					 }
					 
					 String temp  = tokens[0] ;
					 String subj  = temp.replaceAll("http://linkedlifedata.com/sparql","")  ;
 
					 
					 
					 // skip this one , since we need to find the relation with other concepts 
					 if ( concept.equals(Object) || Object.equals(subj.trim()))
					 {
						continue ;  
					 }
					 
					 
					 if (resources.containsKey(Object))
					 {
							 statement.add(triple) ;
					 }
				 }
				 catch(Exception e)
				 {
					continue ;  
				 }
			 }

			 
			 exactMatchTriples.put(concept, statement) ;
			
		}
		if (exactMatchTriples.size() >0)
		{
			return exactMatchTriples ;
		}
		return null; 
	}

	public static Map<String,List<String>> getExactMatch(Map<String,List<String>> triples, String Sentence)
	{		
		Map<String,List<String>> exactMatchTriples = new HashMap <String, List<String>>() ;
		Map<String,List<String>> tempconcepts = new HashMap <String, List<String>>() ;
		tempconcepts.putAll(triples);
		
		for (String concept : triples.keySet())
		{
			List<String> statement = new ArrayList<String>() ;
			
			// remove the concepts from the sentence before we do match 
			String sent =  Sentence.replaceFirst(concept, "");
			
			// get triples for specific concepts
			List<String> _triples = triples.get(concept) ;
			
			 for (String triple: _triples)
			 {
				 try 
				 {
				 
				 // start getting the object and predicate from the triples 
				 String tokens[] =  triple.split("~") ;
				 String Object ;
				 if(tokens.length < 3)
					 continue ; 
				 // most triples have label and it located at index 4 and should be used as object 
				 if(tokens.length == 4)
				 {
					 String _tokens[] =  tokens[3].split("\\[|\\^+");
					 // removing the lang 
					 String Objects[] = _tokens[0].split("@") ;
					 Object = Objects[0] ;
				 }
				 else
				 {
					 String _tokens[] =  tokens[2].split("\\[|\\^+");
					// removing the lang 
					 String Objects[] = _tokens[0].split("@") ;
					 Object = Objects[0] ;
				 }
				 
				 if (sent.contains(Object))
				 {
//					 String _tokens[] =  tokens[1].split("/");
//					 Predicate = _tokens[_tokens.length -1] ;
//					 if (sent.contains(Predicate)) 
//					 {
						 statement.add(triple) ;
						 
					 //}
				 }
				 else
				 {
					 // see if the one of the other concepts exist in the object
					 for (String tempconcept : tempconcepts.keySet())
					 {
						 if(tempconcept == concept)
							 continue ;
						 if (Object.contains(tempconcept))
						 {
/*							 String _tokens[] =  tokens[1].split("/");
							 Predicate = _tokens[_tokens.length -1] ;
							 if (sent.contains(Predicate)) 
							 {*/
								 statement.add(triple) ;
								 
							// } 
						 }
					 }
				 }
			 }
			 catch(Exception e)
			 {
				continue ;  
			 }
			 }

			 
			 exactMatchTriples.put(concept, statement) ;
			
		}
		if (exactMatchTriples.size() >0)
		{
			return exactMatchTriples ;
		}
		return null; 
	}
	
	public static Map<String,List<String>> getSimilairMatch(Map<String,List<String>> triples, String Sentence)
	{		
		Map<String,List<String>> exactMatchTriples = new HashMap <String, List<String>>() ;
		Map<String,List<String>> tempconcepts = new HashMap <String, List<String>>() ;
		tempconcepts.putAll(triples);
		CosineSimilarity sim = new CosineSimilarity() ;
		for (String concept : triples.keySet())
		{
			List<String> statement = new ArrayList<String>() ;
			String sent =  Sentence.replaceFirst(concept, "");
			List<String> _triples = triples.get(concept) ;
			 for (String triple: _triples)
			 {
				 String tokens[] =  triple.split("~") ;
				 String Object ;
				 String Predicate ; 
				 if(tokens.length < 3)
					 continue ; 
				 if(tokens.length == 4)
				 {
					 String _tokens[] =  tokens[3].split("\\[|\\^+");
					 String Objects[] = _tokens[0].split("@") ;
					 Object = Objects[0] ;
				 }
				 else
				 {
					 String _tokens[] =  tokens[2].split("\\[|\\^+");
					 String Objects[] = _tokens[0].split("@") ;
					 Object = Objects[0] ;
				 }
				 
                 if(Object.isEmpty())
                	 continue ; 
                 
                 removestopwords rem = new removestopwords(); 
                 
                 sent =  rem.removestopwordfromsen(sent) ;
                 Object =  rem.removestopwordfromsen(Object) ;
                 if(Object.isEmpty())
                	 continue ; 
                 
				 Map<String,Integer>  vectormain = getVector(sent,Object) ;
				 Map<String, Integer>  vectorobj = getVector(Object,vectormain) ;
				 Map<String, Integer>  vectorsent = getVector(sent,vectormain) ;
				 
				 if (sim.cosineSimilarity(vectorobj, vectorsent) > 0.30)
				 {
						 statement.add(triple) ;
				 }

			 }
			 exactMatchTriples.put(concept, statement) ;
			
		}
		if (exactMatchTriples.size() >0)
		{
			return exactMatchTriples ;
		}
		return null; 
	}
	 public static Map<String, Integer>  getVector(String text1, String text2)
 	 {
 		Map<String, Integer>  vector = new HashMap<String, Integer>();
 		
 		String tokens[] = text1.split(" ") ;
    	for (String token : tokens) 
    	{
    		vector.put(token,0) ;
    	}
    	
		String tokens1[] = text2.split(" ") ;
    	for (String token : tokens1) 
    	{
    		vector.put(token,0) ;
    	}
    	
		return vector;
 	 }
	 
	 public static Map<String, Integer>  getVector(String text1, Map<String, Integer> vectorin)
 	 {
 		Map<String, Integer>  vector = new HashMap<String, Integer>();
 		vector.putAll(vectorin);
 		String tokens[] = text1.split(" ") ;
    	for (String token : tokens) 
    	{
    		vector.put(token, vector.get(token) + 1);
    	}   	
		return vector;
 	 }
	 
	 public static Map<String,List<String>> getPatternRelation(Map<String, Integer> concepts, String sentence)
	 {
		 
		 List<String> uri = new ArrayList<String>() ;
		 Map<String,List<String>> PreURI =  new HashMap <String, List<String>>() ;
		 List<String> predicates  = predicateOntoDictionary.getPredicatDictionary() ;
		 for (String concept1 : concepts.keySet()) 
		 {
			 for (String concept2 : concepts.keySet())
			 {
				 if (concept1.equals(concept2))
					 continue ; 
				 List<String> relations = new ArrayList<String>() ;
				 Pattern p = Pattern.compile(Pattern.quote(concept1) + "(.*?)" + Pattern.quote(concept2)) ;
				 Matcher m = p.matcher(sentence);
				 while (m.find()) {
				   System.out.println(m.group(1));
				   relations.add(m.group(1)) ;
				 }
				 
				 for (String rel:relations)
				 {
					Map<String, Integer> mentions = new HashMap<String, Integer>();
					rel = rel.trim() ;
					String tokens[] = rel.split(" ") ;
					
					mentions = NGramAnalyzer.entities(1,tokens.length, rel) ;
					 for (String term: mentions.keySet())	
					 {
		                 if(term.trim().isEmpty())
		                	 continue ; 
						 for (String predicate : predicates)
						 {
							 String _tokens[] =  predicate.split("/");
							 predicate = _tokens[_tokens.length -1] ;
							 _tokens =  predicate.split("#");
							 predicate = _tokens[0] ;

							 
							if ( predicate.contains(term))
							{
								uri.add(concept1 + "," + predicate + "," + concept2) ;
							}
							 
						 }
						 PreURI.put(term,uri) ;
					 }
					 
				 }
			 }
		 }
			try {
				ReadXMLFile.Serialized(PreURI, "F:\\eclipse64\\eclipse\\pattern");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    return PreURI ; 
	 }

}
