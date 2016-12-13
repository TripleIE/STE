package DS;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.eurecom.nerd.client.NERD;
import fr.eurecom.nerd.client.schema.Entity;
import fr.eurecom.nerd.client.type.DocumentType;
import fr.eurecom.nerd.client.type.ExtractorType;

public class nerd {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getNerdEntities("Diabetes is impacting  Detroit city") ; 
	}
	
	public static Map<String, Integer> getNerdEntities(String sentence)
	{
		   // trim it and if it empty then start next sentence 
		if (sentence.trim().isEmpty() )
		{
			return null ; 
		}
		
		String API_KEY = "18rh720550qlven000a1c2i5u5bpnd7d" ;
		//String text = "Richard Goldbloom was born in Montreal, the son of Alton Goldbloom and Annie Ballon";
		NERD nerd = new NERD(API_KEY);
//		String json = nerd.annotateJSON(ExtractorType.COMBINED, 
//		                                DocumentType.PLAINTEXT,
// 		                                text);
		List<Entity> elist1 = nerd.annotate(ExtractorType.COMBINED, DocumentType.PLAINTEXT,sentence);
		Map<String, Integer> entities = new HashMap<String, Integer>();
	    for (Entity obj:  elist1) // solution for NER
		{
			entities.put(obj.getLabel(), 1)  ;

		}
		return entities ;
	}

}
