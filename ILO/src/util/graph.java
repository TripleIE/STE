package util;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class graph {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	   Model candidategraphA = ModelFactory.createDefaultModel(); 

   	   Resource white = candidategraphA.createResource("http://www.linkeddatatools.com/colors#white");
   	   final Property pwhite = ResourceFactory.createProperty("http://www.linkeddatatools.com/clothing-features#color") ;
   	   white.addProperty(pwhite,"white");

	   Resource recA = candidategraphA.createResource("http://www.linkeddatatools.com/clothes#t-shirt");
   	   final Property pA1 = ResourceFactory.createProperty("http://www.linkeddatatools.com/clothing-features#size") ;
   	   recA.addProperty(pA1,"12");
   	   final Property pA2 = ResourceFactory.createProperty("http://www.linkeddatatools.com/clothing-features#color") ;
   	   recA.addProperty(pA2,white);
   	   //candidategraphA.write(System.out, "RDF/XML-ABBREV") ;     
   	   
   	   
	   Model candidategraphB = ModelFactory.createDefaultModel(); 

   	   Resource whiteB = candidategraphB.createResource("http://www.linkeddatatools.com/colors#white");
   	   final Property pwhiteB = ResourceFactory.createProperty("http://www.linkeddatatools.com/clothing-features#color") ;
   	   whiteB.addProperty(pwhiteB,"white");
	   
	   
	   Resource recB = candidategraphB.createResource("http://www.linkeddatatools.com/clothes#shirt");
   	   final Property pB1 = ResourceFactory.createProperty("http://www.linkeddatatools.com/clothing-features#size") ;
   	   recB.addProperty(pB1,"13");

   	   
   	   final Property pB2 = ResourceFactory.createProperty("http://www.linkeddatatools.com/clothing-features#color") ;
   	   recB.addProperty(pB2,whiteB);

   	   //candidategraphB.write(System.out, "RDF/XML-ABBREV") ;  
  	   
   	   
		Model Intersection = candidategraphB.union(candidategraphA);
		Intersection.write(System.out, "RDF/XML-ABBREV"); 
   	   
	}

}
