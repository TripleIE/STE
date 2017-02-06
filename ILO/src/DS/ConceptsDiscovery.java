package DS;

import edu.stanford.nlp.trees.Tree;
import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import TextProcess.NLPEngine;
import TextProcess.removestopwords;
import util.NGramAnalyzer;
import util.ReadXMLFile;
import util.readfiles;
import util.surfaceFormDiscovery;
public class ConceptsDiscovery {

	public static void main(String[] args) throws IOException {
/*		// TODO Auto-generated method stub
		String CLINICALFOLDER = "C:\\Users\\mazina\\Desktop\\School\\Khalid\\Paper\\SCKIE\\related work\\data set\\cellfinder1_brat.tar\\15971941.txt";
		String ClinicalNote = null ;
		File file = new File(CLINICALFOLDER);
		try {
			ClinicalNote = readfiles.readLinestostring(file.toURL()) ;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//"Runny nose is the Symptom of common cold"
            getconcepts("Famotidine-associated delirium. A series of six cases.".toLowerCase()) ; */
            
		Map<String, List<String>> titles =  ReadXMLFile.ReadCDR_TestSet_BioC()  ; 
		getmeasureMetMapconcepts(titles) ;
		HashMap<String, Map<String, List<String>>> result  = getcachconcepts(titles);
		ReadXMLFile.Serialized(result, "F:\\eclipse64\\eclipse\\conceptDictionarylldwithtitle");
		System.out.println(result);                      
            
	}
	public static HashMap<String, Map<String, List<String>>> getcachconcepts(Map<String, List<String>> titles) throws IOException
	{
		
		double avgRecall = 0  ; 
		double avgPrecision = 0 ;
		double avgFmeasure = 0 ; 
		
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
	    
		int counter = 0 ;
		HashMap<String, Map<String, List<String>>> diectwithtitle = new HashMap<String, Map<String, List<String>>>();
		for(String title : titles.keySet())
		{
			String orgTitle = title ;
			Map<String, Integer> allconcepts = new HashMap<String, Integer>();
			counter++ ; 
			try {
				
				// find all concepts that exist in the UMLS
				Map<String, Integer> metmapconcepts = MetamapConcepts.getconcepts(title,api) ;
				String[] arr = new String[metmapconcepts.size()] ;
				int i= 0 ; 
				for( String concept : metmapconcepts.keySet())
				{
					arr[i] = concept ;
					i++ ; 
				}
				
				
			// sort them Descending  
				arr = insertionSort(arr); 
				// pruning the concepts 
				for( String concept : arr)
				{
					if ( title.contains(concept.toLowerCase()) )
					{
						title = title.replace(concept.toLowerCase(), "") ;
					}
					else
					{
						metmapconcepts.remove(concept) ;
					}
				}
				
				allconcepts.putAll(metmapconcepts);
				
/*				Map<String, Integer> nerdconcepts = nerd.getNerdEntities(title) ;
				String[] arrnerd = new String[nerdconcepts.size()] ;
				i = 0 ; 
				for( String concept : nerdconcepts.keySet())
				{
					arrnerd[i] = concept ;
					i++ ; 
				}
				
				arrnerd = insertionSort(arrnerd); 
				for( String concept : arrnerd)
				{
					title = title.replace(concept.toLowerCase(), "") ;
				}  */
				
				
				Map<String, Integer> lodconcepts = new HashMap<String, Integer>();
				Map<String, Integer> mentions = new HashMap<String, Integer>();
				
				mentions = NGramAnalyzer.entities(1,3, title) ;
				
				for(String mention : mentions.keySet())
				{
					// no need to examine the stopwords
					if (!mention.isEmpty() && !removestopwords.removestopwordsingle(mention.trim()) && LDConcepts.EntityMentionDetection(mention) ) 
						lodconcepts.put(mention, 1) ;
				}				
				allconcepts.putAll(lodconcepts);
				

				// get the surface form & synonym of each concepts 
				   Map<String, List<String>> diect = new HashMap<String, List<String>>();

				   for (String concept:allconcepts.keySet())
				   {
					   Map<String, Integer> surfaceFormmesh = null ;
					   Map<String, Integer> surfaceFormlld = null ;
					   surfaceFormlld  = surfaceFormDiscovery.getsurfaceFormLLD(concept); // getsurfaceFormMesh(concept); 
					   surfaceFormmesh  = surfaceFormDiscovery.getsurfaceFormMesh(concept);
					   List<String> forms = new ArrayList<String>();
					   Map<String, Integer> diectform = new HashMap<String, Integer>();
					   if (surfaceFormmesh != null)
					   {
						   
						   for(String term : surfaceFormmesh.keySet()) 
						   {
							   String[] tokens  = term.split("@") ;
							   term = tokens[0] ; 
							   if (diectform.put(term, 1)  ==  null) 
								   forms.add(term) ;
						   }
						   
						   for(String term : surfaceFormlld.keySet()) 
						   {
							   String[] tokens  = term.split("@") ;
							   term = tokens[0] ; 
							   if (diectform.put(term, 1)  ==  null) 
								   forms.add(term) ;
						   }
					   }
					   diect.put(concept, forms) ;
				   }
				   
				   diectwithtitle.put(orgTitle, diect) ;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			
		}
		
		
	
		return diectwithtitle ;
	}
	
	public static Map<String, Integer> getconcepts(Map<String, List<String>> titles)
	{
		
		double avgRecall = 0  ; 
		double avgPrecision = 0 ;
		double avgFmeasure = 0 ; 
		
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
	    
		int counter = 0 ;
		Map<String, Integer> allconcepts = new HashMap<String, Integer>();
		NLPEngine nlpprocessor = new NLPEngine() ;

		for(String title : titles.keySet())
		{
			Tree parsertree = nlpprocessor.getParseTreeSentence(title) ;
			List<String> verbs = new ArrayList<String>() ;
			nlpprocessor.getVerbs(parsertree, verbs);
			
			counter++ ;
			List<String> GoldSndconcepts = titles.get(title); 
			  
			try {
				
				// find all concepts that exist in the UMLS
				Map<String, Integer> metmapconcepts = MetamapConcepts.getconcepts(title,api) ;
				String[] arr = new String[metmapconcepts.size()] ;
				int i= 0 ; 
				for( String concept : metmapconcepts.keySet())
				{
					arr[i] = concept ;
					i++ ; 
				}
				
								
			// sort them Descending  
				arr = insertionSort(arr); 
				// pruning the concepts 
				for( String concept : arr)
				{
					if ( title.contains(concept.toLowerCase()) )
					{
						title = title.replace(concept.toLowerCase(), "") ;
					}
					else
					{
						metmapconcepts.remove(concept) ;
					}
				}
				

				
				for (String verb :verbs )
				{
					metmapconcepts.remove(verb) ;
				}

				allconcepts.putAll(metmapconcepts);

				
				Map<String, Integer> lodconcepts = new HashMap<String, Integer>();
				Map<String, Integer> mentions = new HashMap<String, Integer>();
				
				mentions = NGramAnalyzer.entities(1,3, title) ;
				
				for(String mention : mentions.keySet())
				{
					// no need to examine the stopwords
					if (!mention.isEmpty() && !removestopwords.removestopwordsingle(mention.trim()) && LDConcepts.EntityMentionDetection(mention) ) 
						lodconcepts.put(mention, 1) ;
				}
				
				
				for (String verb :verbs )
				{
					lodconcepts.remove(verb) ;
				}
				allconcepts.putAll(lodconcepts);
				

				
				
				// measure the recall precision and  F-measure 
				double relevent = 0 ;
				for( String concept : allconcepts.keySet())
				{
/*	               if (GoldSndconcepts.contains(concept.toLowerCase().replace("-", " ")))
	               {
	            	   relevent++ ; 
	               }*/
					
					for( String Gconcept : GoldSndconcepts)
					{
						 if (Gconcept.replace("-", " ").equals(concept.toLowerCase()) )
			               {
			            	   relevent++ ; 
			               }
					}
					
				}
				
				// calculate the Recall 
				//For example for text search on a set of documents recall is the number of correct results divided by the number of results that should have been returned
				double recall = 0 ;
				if(GoldSndconcepts.size() > 0)
				   recall = relevent / GoldSndconcepts.size() ;
				avgRecall += recall ; 
				double precision = 0 ;
				if(allconcepts.size() > 0)
				    precision = relevent / allconcepts.size() ; 
				avgPrecision += precision ;	
				double Fmeasure = 0 ;
				if(precision + recall > 0)
				   Fmeasure = 2* ((precision * recall) / (precision + recall)) ;
				avgFmeasure += Fmeasure ;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}
		

		return allconcepts ;
	}
	public static void getconcepts(String Sent)
	{
		try {
			
			// find all concepts that exist in the UMLS and remove them from the sentences
			Map<String, Integer> metmapconcepts = MetamapConcepts.getconcepts(Sent) ;
			String[] arr = new String[metmapconcepts.size()] ;
			int i= 0 ; 
			for( String concept : metmapconcepts.keySet())
			{
				arr[i] = concept ;
				i++ ; 
			}
			// sort them Descending  
			arr = insertionSort(arr); 
			
			for( String concept : arr)
			{
				if ( Sent.contains(concept.toLowerCase()) )
				{
				  Sent = Sent.replace(concept.toLowerCase(), "") ;
				}
				else
				{
					metmapconcepts.remove(concept) ;
				}
			}
			
/*			Map<String, Integer> nerdconcepts = nerd.getNerdEntities(Sent) ;
			String[] arrnerd = new String[nerdconcepts.size()] ;
			i = 0 ; 
			for( String concept : nerdconcepts.keySet())
			{
				arrnerd[i] = concept ;
				i++ ; 
			}
			
			arrnerd = insertionSort(arrnerd); 
			for( String concept : arrnerd)
			{
				Sent = Sent.replace(concept.toLowerCase(), "") ;
			}*/
			
			
			Map<String, Integer> lodconcepts = new HashMap<String, Integer>();
			
			lodconcepts = NGramAnalyzer.entities(1,3, Sent) ;
			
			for(String concept : lodconcepts.keySet())
			{
				// no need to examine the stopwords
				if (!concept.isEmpty() && !removestopwords.removestopwordsingle(concept.trim()) && LDConcepts.EntityMentionDetection(concept) ) 
					lodconcepts.put(concept, 1) ;
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	

}
	
	public static String getmeasureMetMapconcepts(Map<String, List<String>> titles)
	{
		
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
		
		double avgRecall = 0.0  ; 
		double avgPrecision = 0.0 ;
		double avgFmeasure = 0.0 ; 
		int counter = 0 ;
		Map<String, List<String>> notfoundconcepts = new HashMap<String, List<String>>(); 
		for(String title : titles.keySet())
		{
			counter++ ;
			if (counter > 80  )
			{
				break ; 
			}
			List<String> GoldSndconcepts = titles.get(title); 
			List<String> notfoundGoldSndconcepts = titles.get(title); 
			
			try {
				
				// find all concepts that exist in the UMLS
				Map<String, Integer> metmapconcepts = MetamapConcepts.getconcepts(title,api) ;
				String[] arr = new String[metmapconcepts.size()] ;
				int i= 0 ; 
				for( String concept : metmapconcepts.keySet())
				{
					arr[i] = concept;
					i++ ; 
				}
				// measure the recall precision and  F-measure 
				double relevent = 0 ;
				for( String concept : arr)
				{
                   if (GoldSndconcepts.contains(concept.toLowerCase()))
                   {
                	   relevent++ ; 
                	   notfoundGoldSndconcepts.remove(concept.toLowerCase()) ;
                   }
					
				}
				
				// calculate the Recall 
				//For example for text search on a set of documents recall is the number of correct results divided by the number of results that should have been returned
				double recall = 0.0 ; 
				if (GoldSndconcepts.size() > 0  )
				{
					recall = relevent / GoldSndconcepts.size() ;
				}

				avgRecall = recall  + avgRecall; 
				
				double precision = relevent / arr.length ; 
				avgPrecision += precision ;	
				
				double Fmeasure  = 0.0 ;
				if (precision + recall > 0 )
				   Fmeasure = 2* ((precision * recall) / (precision + recall)) ;
				   avgFmeasure = Fmeasure + avgFmeasure ;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			notfoundconcepts.put(title, notfoundGoldSndconcepts) ;
		}
		avgRecall = avgRecall / titles.size() ;
		avgPrecision = avgPrecision / titles.size() ;
		avgFmeasure = avgFmeasure / titles.size() ;
		
		String result = Double.toString(avgRecall) + " " +  Double.toString(avgPrecision) +" " +  Double.toString(avgFmeasure) ;
		try {
			ReadXMLFile.Serialized(notfoundconcepts, "F:\\eclipse64\\eclipse\\conceptDictionarylldwithtitlenotfound");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result ;
	}
	
	public static String getmeasureNERDconcepts(Map<String, List<String>> titles)
	{
		

		double avgRecall = 0.0  ; 
		double avgPrecision = 0.0 ;
		double avgFmeasure = 0.0 ; 
		int size_ = titles.size() ;
		int counter = 0 ; 
		for(String title : titles.keySet())
		{
			counter++  ; 
			List<String> GoldSndconcepts = titles.get(title); 
			
			try {
				
				
				Map<String, Integer> nerdconcepts = nerd.getNerdEntities(title) ;
				String[] arr = new String[nerdconcepts.size()] ;
				int i = 0 ; 
				for( String concept : nerdconcepts.keySet())
				{
					arr[i] = concept.toLowerCase() ;
					i++ ; 
				}

				// measure the recall precision and  F-measure 
				double relevent = 0 ;
				for( String concept : arr)
				{
                   if (GoldSndconcepts.contains(concept.toLowerCase()))
                   {
                	   relevent++ ; 
                   }
					
				}
				
				// calculate the Recall 
				//For example for text search on a set of documents recall is the number of correct results divided by the number of results that should have been returned
				double recall = 0.0 ; 
				if (GoldSndconcepts.size() > 0  )
				{
					recall = relevent / GoldSndconcepts.size() ;
				}

				avgRecall = recall  + avgRecall; 
				
				double precision = 0 ; 
				if ( arr.length > 0  )
				   precision = relevent / arr.length ;
				
				 avgPrecision += precision ;	
				
				double Fmeasure  = 0.0 ;
				if (precision + recall > 0 )
				   Fmeasure = 2* ((precision * recall) / (precision + recall)) ;
				   avgFmeasure = Fmeasure + avgFmeasure ;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		avgRecall = avgRecall / titles.size() ;
		avgPrecision = avgPrecision / titles.size() ;
		avgFmeasure = avgFmeasure / titles.size() ;
		
		String result = Double.toString(avgRecall) + " " +  Double.toString(avgPrecision) +" " +  Double.toString(avgFmeasure) ;
		return result ;
	}
	public static String getmeasureLODconcepts(Map<String, List<String>> titles)
	{
		

		double avgRecall = 0.0  ; 
		double avgPrecision = 0.0 ;
		double avgFmeasure = 0.0 ; 
		int size_ = titles.size() ;
		int counter = 0 ; 
		for(String title : titles.keySet())
		{
			counter++  ; 

			

			
			List<String> GoldSndconcepts = titles.get(title); 
			
			try {
				
				
				Map<String, Integer> lodconcepts = new HashMap<String, Integer>();
				Map<String, Integer> mentions = new HashMap<String, Integer>();
				mentions = NGramAnalyzer.entities(1,3, title) ;
				
				for(String concept : mentions.keySet())
				{
					if (concept.equals("convulsive") )
					{
						int kk = 0 ;
						kk++ ;
								
					}
					// no need to examine the stopwords
					if (!concept.isEmpty()  && !removestopwords.removestopwordsingle(concept.trim()) && LDConcepts.EntityMentionDetection(concept) ) 
						lodconcepts.put(concept, 1) ;
				}

				String[] arr = new String[lodconcepts.size()] ;
				int i = 0 ; 
				for( String concept : lodconcepts.keySet())
				{
					arr[i] = concept.toLowerCase() ;
					i++ ; 
				}

				// measure the recall precision and  F-measure 
				double relevent = 0 ;
				for( String concept : arr)
				{
                   if (GoldSndconcepts.contains(concept.toLowerCase()))
                   {
                	   relevent++ ; 
                   }
					
				}
				
				// calculate the Recall 
				//For example for text search on a set of documents recall is the number of correct results divided by the number of results that should have been returned
				double recall = 0.0 ; 
				if (GoldSndconcepts.size() > 0  )
				{
					recall = relevent / GoldSndconcepts.size() ;
				}

				avgRecall = recall  + avgRecall; 
				
				double precision = 0 ; 
				if ( arr.length > 0  )
				   precision = relevent / arr.length ;
				
				 avgPrecision += precision ;	
				
				double Fmeasure  = 0.0 ;
				if (precision + recall > 0 )
				   Fmeasure = 2* ((precision * recall) / (precision + recall)) ;
				   avgFmeasure = Fmeasure + avgFmeasure ;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		avgRecall = avgRecall / titles.size() ;
		avgPrecision = avgPrecision / titles.size() ;
		avgFmeasure = avgFmeasure / titles.size() ;
		
		String result = Double.toString(avgRecall) + " " +  Double.toString(avgPrecision) +" " +  Double.toString(avgFmeasure) ;
		return result ;
	}
	
		
	
	public static String[] insertionSort(String[] arr) {
	    for(int i=1;i<arr.length;i++) {
	        int j = 0;
	        for(;j<i;j++) {
	            if(arr[j].length() < arr[j+1].length()) {
	                String temp = arr[j];
	                arr[j] = arr[j+1];
	                arr[j+1] = temp;
	            }
	        }
	    }
	    return arr;
	}

}
