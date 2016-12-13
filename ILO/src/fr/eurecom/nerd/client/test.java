package fr.eurecom.nerd.client;

import java.util.List;

import fr.eurecom.nerd.client.schema.Entity;
import fr.eurecom.nerd.client.type.DocumentType;
import fr.eurecom.nerd.client.type.ExtractorType;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String API_KEY = "18rh720550qlven000a1c2i5u5bpnd7d" ;
		String text = "Richard Goldbloom was born in Montreal, the son of Alton Goldbloom and Annie Ballon";
		NERD nerd = new NERD(API_KEY);
		String json = nerd.annotateJSON(ExtractorType.COMBINED, 
		                                DocumentType.PLAINTEXT,
		                                text);
		List<Entity> ann =  nerd.annotate(ExtractorType.COMBINED, DocumentType.PLAINTEXT,text);
		
		System.out.println(json);
		
		

	}

}
