package TE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class semantic {

	public static Map<String,List<String>> getSemanticMatch(Map<String,List<String>> triples, String Sentence)
	{		
		Map<String,List<String>> semanticMatchTriples = new HashMap <String, List<String>>() ;
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
				 
				 
			 }
			
		}
		
		if (semanticMatchTriples.size() >0)
		{
			return semanticMatchTriples ;
		}
		return null; 
	}

}
