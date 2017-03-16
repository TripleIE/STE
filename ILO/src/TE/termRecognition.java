package TE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DS.QueryEngine;
import TextProcess.removestopwords;
import util.NGramAnalyzer;

public class termRecognition {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getTerms("runny nose cuased by common cold",3,true) ;
	}
	
	public static void getTerms(String Sent, int wordlength,  boolean longmatch )
	{
		
		// remove stop word
		Sent = removestopwords.removestopwordfromsen(Sent) ;
		// get the ngarm between 1 to 4 
		Map<String, Integer> terms  = NGramAnalyzer.entities(1,4,Sent)  ;
		

		// Term>>>>
		//           concept>>>
		//                       URI
		//                       URI
		//           concept>>>  
		//                       URI
		
		// for each term find the long match / or not 
		Map<String, Map<String, List<String>>> Terms = new HashMap<String, Map<String, List<String>>>();
		for (String term:terms.keySet())
		{
			if (term.length() < wordlength)
				continue ; 
			// get UMLs Atom Unique Identifiers (AUI) that match the term as synonyms 
			List<String> AUIs = QueryEngine.lifedataterms(term); 
			if(AUIs == null)
				continue ; 
			
			Map<String, List<String>> AUIlist = new HashMap<String, List<String>>();

			for (String AUI:AUIs )
			{
				// calculate Context Concept weight
				// find the concepts that AUI belong 2 
				
				String[] tokens = AUI.split("\\|") ;
				
				List<String> conpt = AUIlist.get(tokens[0]) ; 
				if(conpt== null)
				{
					List<String> AUIconcept = new ArrayList<String>()  ;
					AUIconcept.add(tokens[1]);
					AUIlist.put(tokens[0], AUIconcept);
				}
				else
				{
					 conpt.add(tokens[1]);
					AUIlist.put(tokens[0],  conpt);
				}
				
				
				
			}
			
			Terms.put(term, AUIlist);
			
		}
		
		
		// if term is disambiguate and has no overlap I'm eliminated it 
		Map<String, Map<String, List<String>>> Termsin = new HashMap<String, Map<String, List<String>>>(Terms);
		Map<String, Map<String, List<String>>> retTerms = new HashMap<String, Map<String, List<String>>>();
		for (String term:Terms.keySet())
		{
			
			Map<String, List<String>> cons = Terms.get(term) ; 
			
			if (cons.size() <= 1 )
			{

				Map<String, List<String>> retAUIlist = new HashMap<String, List<String>>(cons);
				retTerms.put(term, retAUIlist) ;
				continue ; 	
			}

			Double maxoverlap =  0.0 ; 
			String candidate =  null ; 
			for (String con:cons.keySet())
			{
			
				
				Double overlap =  0.0 ; 
				for (String termin:Termsin.keySet())
				{
					if(termin.equals(term))
						continue ; 
					
					Map<String, List<String>> conin = Termsin.get(termin) ;
					
					if(conin.get(con) != null)
					{
						overlap ++ ; 
					}
					
				}
				
				if (overlap > maxoverlap)
				{
					maxoverlap = overlap ; 
					candidate = con ; 
				}
			}
			
			if (candidate == null)
				continue ; 
			
			Termsin.get(term).get(candidate) ;
			Map<String, List<String>> retAUIlist = new HashMap<String, List<String>>();
			retAUIlist.put(candidate, Termsin.get(term).get(candidate));
			retTerms.put(term, retAUIlist) ;
			
			
			
			
		}
		
		
		
		
	}

}
