package edu.ucsf.lava.core.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class ViewPropertyAttributeParser {

	protected static String ALL_DELIMITERS = " =\t\n\r\"";
	protected static String DOUBLE_QUOTE = "\"";
	protected static String EQUALS_SIGN = "=";		
	
	public static Map<String,String> parseAttributes(String attributes){
	  Map<String,String> parsedAttributes = new HashMap<String,String>();
	  if(attributes==null){return parsedAttributes;}
	  
	  List<String> result = new ArrayList<String>();
      String delimiters = ALL_DELIMITERS;
	  StringTokenizer parser = new StringTokenizer(attributes,delimiters,true);
	  String token = null;
	  while ( parser.hasMoreTokens() ) {
	      token = parser.nextToken(delimiters);
	      //if the token found is not a double quote then it is either a property or a value
	      if ( !token.equals(DOUBLE_QUOTE) && !token.equals(EQUALS_SIGN)){
	        result.add(token.trim());
	      } else if (token.equals(DOUBLE_QUOTE)){ //if the token is a double quote then flip the delimiters so we we either grab the next property or the full quote delimited value
	        delimiters = (delimiters.equals(DOUBLE_QUOTE))? ALL_DELIMITERS : DOUBLE_QUOTE;
	      }
	    }

	  //now pack the tokens into a map and return
	   Iterator iter = result.iterator();
	   while(iter.hasNext()){
		   String prop = (String)iter.next();
		   String value = null;
		   if(iter.hasNext()){
			   value = (String)iter.next();
		   }
		   parsedAttributes.put(prop,value);
	   }
	   return parsedAttributes;
		   
	}
}