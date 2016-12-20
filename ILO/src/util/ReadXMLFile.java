package util;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadXMLFile {
    public static void main(String[] args) {
    	ReadCDR_TestSet_BioC() ;
    }

  public static String Read(String filename) {

    try {

	File fXmlFile = new File(filename);
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);

	//optional, but recommended
	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	doc.getDocumentElement().normalize();

	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
    String text =  doc.getElementsByTagName("TEXT").item(0).getTextContent() ; 
	System.out.println( doc.getElementsByTagName("TEXT").item(0).getTextContent());
	text = text.replaceAll("\n", " ") ;
	System.out.println("----------------------------");
	 return text ;
	 
    } catch (Exception e) {
	e.printStackTrace();
	 return null ; 
    }
    
  }

  public static Map<String, List<String>> ReadCDR_TestSet_BioC() {

	    Map<String, List<String>> goldstandard = null ;
	    goldstandard = Deserialize("F:\\eclipse64\\eclipse\\goldstandard") ;
	    if (goldstandard== null )
	    try {

        String filename = "F:\\eclipse64\\eclipse\\CDR_TestSet.BioC.xml" ;
	    goldstandard  = new HashMap<String, List<String>>();
		File fXmlFile = new File(filename);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
        
		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();

		NodeList passageList = doc.getElementsByTagName("passage");
		for (int i = 0; i < passageList.getLength() && i < 1 ; i++)
		{
			List<String> conclist = new ArrayList<String>() ;
			NodeList childList = passageList.item(i).getChildNodes();
			for (int j = 0; j < childList.getLength(); j++)
			{
				if ("infon".equals(childList.item(j).getNodeName()) && "title".equals(childList.item(j).getTextContent()))
				{
					String title = null ; 
					NodeList childList1 = passageList.item(i).getChildNodes();
					for (int kk = 0; kk < childList1.getLength(); kk++)
					{
						 System.out.println(childList1.item(kk).getNodeName());
						if ("text".equals(childList1.item(kk).getNodeName()))
						{
							 System.out.println(childList1.item(kk).getTextContent()
					                    .trim());
							title = childList1.item(kk).getTextContent()
				                    .trim().toLowerCase() ;
						}
						
						if ("annotation".equals(childList1.item(kk).getNodeName()))
						{
							NodeList childList2 = childList.item(kk).getChildNodes();
							for (int kkk = 0; kkk < childList2.getLength(); kkk++)
							{
								if ("text".equals(childList2.item(kkk).getNodeName()))
								{
									 conclist.add(childList2.item(kkk).getTextContent().trim().toLowerCase()) ;
									 System.out.println(childList2.item(kkk).getTextContent()
							                    .trim());
								}
							}

						}
					}
					
					goldstandard.put(title,conclist) ;
					
				}
			}
           
		}
			Serialized(goldstandard,"F:\\eclipse64\\eclipse\\goldstandard") ;
	    } 
	    catch (Exception e) {
		e.printStackTrace();
	    }
	    return goldstandard;
	  } ;
  
  
	public static  void Serialized(Map<String, List<String>> dictionary,String fileout) throws IOException
	 {

	     try
	     {
	    	 // Create output stream.
	         FileOutputStream fileOut =
	         new FileOutputStream(fileout);
		     // Create XML encoder.
		     XMLEncoder xenc = new XMLEncoder(fileOut);
	
		     // Write object.
		     xenc.writeObject(dictionary);
		     xenc.close();
	         fileOut.close();
	         System.out.printf("Serialized data is saved in" + fileout);
	     }catch(IOException i)
	     {
	          i.printStackTrace();
	     }
	 }
	
	
	 public static  Map<String, List<String>> Deserialize(String fileout)
	   {
			   Map<String, List<String>> dictionary ;
			   FileInputStream fileIn;
			try {
				fileIn = new FileInputStream(fileout);
			
			   XMLDecoder decoder =  new XMLDecoder(fileIn);
			   dictionary  = (Map<String, List<String>>)decoder.readObject();
			   decoder.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		      return dictionary;
	   }
	 

	}