package util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException ;



public class parseJson {
	
	
	public void getSenses(String Jsonstring) 
	{
			
			// read the json file

			JSONParser parser = new JSONParser();
	        try {     
	            Reader targetReader = new StringReader(Jsonstring);
	            targetReader.close();
	            
	            Object obj = parser.parse(targetReader);
                 
	            JSONObject jsonObject =  (JSONObject) obj;

	            String name = (String) jsonObject.get("name");
	            System.out.println(name);

	            String city = (String) jsonObject.get("city");
	            System.out.println(city);

	            String job = (String) jsonObject.get("job");
	            System.out.println(job);

	            // loop array
	            JSONArray cars = (JSONArray) jsonObject.get("cars");
	            Iterator<String> iterator = cars.iterator();
	            while (iterator.hasNext()) {
	             System.out.println(iterator.next());
	            }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	    }
	  
	}

