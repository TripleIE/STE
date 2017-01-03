package util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable; 
public class Dataset implements Serializable
{
	
	Map<String, List<String>> NSURIs = new HashMap <String, List<String>>() ;
	
	public void Setonto(String onto,List<String> uris )
	{
		NSURIs.put(onto, uris) ;
	}
	
	public List<String>  getontoURIs(String onto)
	{
		return NSURIs.get(onto);
	}

	public Map<String, List<String>>  getonto()
	{
		return NSURIs;
	}
}
