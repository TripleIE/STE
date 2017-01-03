package TE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import TextProcess.removestopwords;
import edu.cmu.lti.ws4j.demo.SimilarityCalculationDemo;
import util.CosineSimilarity;

public class syntactic {
	

	public static Map<String,List<String>> getExactMatch(Map<String,List<String>> triples, String Sentence)
	{		
		Map<String,List<String>> exactMatchTriples = new HashMap <String, List<String>>() ;
		Map<String,List<String>> tempconcepts = new HashMap <String, List<String>>() ;
		tempconcepts.putAll(triples);
		
		for (String concept : triples.keySet())
		{
			List<String> statement = new ArrayList<String>() ;
			
			// remove the concepts from the sentence before we do match 
			String sent = Sentence.replaceFirst(concept, "");
			
			// get triples for specific concepts
			List<String> _triples = triples.get(concept) ;
			
			 for (String triple: _triples)
			 {
				 
				 // start getting the object and predicate from the triples 
				 String tokens[] =  triple.split("~") ;
				 String Object ;
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
			String sent = Sentence.replaceFirst(concept, "");
			List<String> _triples = triples.get(concept) ;
			 for (String triple: _triples)
			 {
				 String tokens[] =  triple.split("~") ;
				 String Object ;
				 String Predicate ; 
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
                 
				 Map<CharSequence, Integer>  vectormain = getVector(sent,Object) ;
				 Map<CharSequence, Integer>  vectorobj = getVector(Object,vectormain) ;
				 Map<CharSequence, Integer>  vectorsent = getVector(sent,vectormain) ;
				 
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
	 public static Map<CharSequence, Integer>  getVector(String text1, String text2)
 	 {
 		Map<CharSequence, Integer>  vector = new HashMap<CharSequence, Integer>();
 		
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
	 
	 public static Map<CharSequence, Integer>  getVector(String text1, Map<CharSequence, Integer> vectorin)
 	 {
 		Map<CharSequence, Integer>  vector = new HashMap<CharSequence, Integer>();
 		vector.putAll(vectorin);
 		String tokens[] = text1.split(" ") ;
    	for (String token : tokens) 
    	{
    		vector.put(token, vector.get(token) + 1);
    	}   	
		return vector;
 	 }
	 

}
