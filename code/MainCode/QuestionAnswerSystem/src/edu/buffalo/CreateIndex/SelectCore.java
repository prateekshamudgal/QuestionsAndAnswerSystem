package edu.buffalo.CreateIndex;
/*
 * Using Stanford NER to resolve the document into the corresponding Cores
 * The Unresolved Documents will be added to WikiMisc core . WikiMisc core will contain the 
 * entire Index
 * Core Selection based on Document Title
 */
import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import edu.buffalo.parser.InfoBoxBean;
import edu.buffalo.parser.InfoBoxParser;

public class SelectCore {
	
	String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
	AbstractSequenceClassifier<?> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
	private static Map<String, String> PersonMap = new HashMap<>();
	private static Map<String, String> LocationMap = new HashMap<>();
	private static Map<String, String> OrganizationMap = new HashMap<>();
	private static Map<String, String> MiscMap = new HashMap<>();
	InfoBoxParser parser = new InfoBoxParser("./file/WikiDump_1600.xml");
	Map <String, HashMap> InfoboxData = parser.getTitleInfoboxMap();
	public Map<String, HashMap> getParsedInfoboxMap()
	{
		return InfoboxData;
	}
	public Map<String, String> getPersonMap()
	{
		return PersonMap;
	}
	public Map<String, String> getLocationMap()
	{
		return LocationMap;
	}
	public Map<String, String> getOrganizationMap()
	{
		return OrganizationMap;
	}
	public Map<String, String> getMiscMap()
	{
		return MiscMap;
	}
	
public String classify()
{
	return null;	
}

public void fillMap()
{
	for (Map.Entry<String, HashMap> e : InfoboxData.entrySet())
	{
		String buffer = classifier.classifyWithInlineXML(e.getKey().toString());
		if (buffer.contains("<PERSON>"))
		{
			PersonMap.put(e.getKey(), "Person");
		}
		else if (buffer.contains("<ORGANIZATION>"))
		{
			OrganizationMap.put(e.getKey(), "Organization");
		}
		else if (buffer.contains("<LOCATION>"))
		{
			LocationMap.put(e.getKey(), "Person");
		}
		else
		{
			MiscMap.put(e.getKey(), "Misc");
		}
	}
}


		
public static void main(String[] args) throws IOException {
//	String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
//	AbstractSequenceClassifier<?> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
//	String s1 = "Paris";
//	System.out.println(classifier.classifyWithInlineXML(s1));
	
	
	SelectCore sc = new SelectCore();
	sc.fillMap();
//	for (Map.Entry<String, String> e : PersonMap.entrySet())
//    System.out.println(e.getKey() + ": " + e.getValue());
	System.out.println("Size of PersonMap: "+PersonMap.size());
	System.out.println("Size of LocationMap: "+LocationMap.size());
	System.out.println("Size of OrganizationMap: "+OrganizationMap.size());
	System.out.println("Size of MISCMap: "+MiscMap.size());
	System.out.println("Size of totalSize: "+sc.InfoboxData.size());
}
		
	
}
