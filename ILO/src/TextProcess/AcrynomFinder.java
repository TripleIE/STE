package TextProcess;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import util.restcalls; 

import org.w3c.dom.Document ;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
public class AcrynomFinder {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		acronymFinderInSent("HBP is a disease" ) ;
	}

	public static String acronymFinder(String acry) 
	{
		String longform = null  ;
		// we need to check is this a uppercase and remove ")" etc.
		// we need to check is this a uppercase and remove ")" etc.
		if ( (acry = striprules(acry)) == null)
		{
			return null ; 
		}
		
		// using http://data.allie.dbcls.jp/index_en.html
		// Allie is a search service for abbreviations and long forms in life sciences.
		
	    String uri = "http://allie.dbcls.jp/rest/getPotentialPairsByAbbr?keywords=" +  acry;
	    try {
			String result = restcalls.httpGet(uri) ;
			InputStream stream = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(stream);

			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList =  doc.getElementsByTagName("item") ; 
			
			/*<item seqid="0">
			<pair_id xsi:type="xsd:int">514790</pair_id>
			<abbreviation xsi:type="xsd:string">HBP</abbreviation>
			<long_form xsi:type="xsd:string">high blood pressure</long_form>
			<pair_number xsi:type="xsd:int">361</pair_number>
			</item>*/

			// currently only we get the highest ranked long form
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					System.out.println("pair_id : " + eElement.getElementsByTagName("pair_id").item(0).getTextContent());
					System.out.println("abbreviation : " + eElement.getElementsByTagName("abbreviation").item(0).getTextContent());
					System.out.println("long_form : " + eElement.getElementsByTagName("long_form").item(0).getTextContent());
					System.out.println("pair_number : " + eElement.getElementsByTagName("pair_number").item(0).getTextContent());
					longform = eElement.getElementsByTagName("long_form").item(0).getTextContent() ;
					break ;

				}
			}
			System.out.println( longform );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  longform  ; 
	}
	
	
	
	// find acronym  in sentence and replace it 
	public static String acronymFinderInSent(String sent) 
	{
		
		String[] tokens = sent.split(" ") ;
		
		for (String token : tokens)
		{
			
		
			String longform = null  ;
			// we need to check is this a uppercase and remove ")" etc.
			if ( (token = striprules(token)) == null)
			{
				continue  ; 
			}
			
			// using http://data.allie.dbcls.jp/index_en.html
			// Allie is a search service for abbreviations and long forms in life sciences.
			
		    String uri = "http://allie.dbcls.jp/rest/getPotentialPairsByAbbr?keywords=" +  token;
		    
		    try {
				String result = restcalls.httpGet(uri) ;
				InputStream stream = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(stream);
		
				//optional, but recommended
				//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();
		
				System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
				NodeList nList =  doc.getElementsByTagName("item") ; 
				
				/*<item seqid="0">
				<pair_id xsi:type="xsd:int">514790</pair_id>
				<abbreviation xsi:type="xsd:string">HBP</abbreviation>
				<long_form xsi:type="xsd:string">high blood pressure</long_form>
				<pair_number xsi:type="xsd:int">361</pair_number>
				</item>*/
		
				// currently only we get the highest ranked long form
				for (int temp = 0; temp < nList.getLength(); temp++) {
		
					Node nNode = nList.item(temp);
		
					System.out.println("\nCurrent Element :" + nNode.getNodeName());
		
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		
						Element eElement = (Element) nNode;
		
						System.out.println("pair_id : " + eElement.getElementsByTagName("pair_id").item(0).getTextContent());
						System.out.println("abbreviation : " + eElement.getElementsByTagName("abbreviation").item(0).getTextContent());
						System.out.println("long_form : " + eElement.getElementsByTagName("long_form").item(0).getTextContent());
						System.out.println("pair_number : " + eElement.getElementsByTagName("pair_number").item(0).getTextContent());
						longform = eElement.getElementsByTagName("long_form").item(0).getTextContent() ;
						break ;
		
					}
				}
				System.out.println( longform );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    sent =  sent.replace(token, longform); 
		  
		}
		return sent ; 
}
	
	public static String striprules(String acry)
	{

		char[] arrcry; 
		arrcry = acry.toCharArray() ;
		
		// check acry is upper case
		boolean uppercase  = true ; 
		for ( int i=0 ;i < acry.length(); i++)
		{
			if (Character.isLowerCase(arrcry[i]))
			{
				uppercase = false ; 
				break ; 
			}
		}
		
		// remove ")" "(" and "\""
		if (uppercase)
		{
			removestopwords stopremove = new removestopwords() ; 
			acry = stopremove.removestopwordfromsen(acry) ;
			return acry;
		}
		
		
		return null;
		
	}
}
