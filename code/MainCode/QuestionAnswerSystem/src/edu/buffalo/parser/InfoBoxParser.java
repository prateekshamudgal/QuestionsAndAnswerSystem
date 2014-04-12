/**
 * 
 */
package edu.buffalo.parser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//import edu.jhu.nlp.wikipedia.InfoBox;
//import edu.jhu.nlp.wikipedia.WikiTextParser;

 

/**
 * @author PrateekshaMudgal
 * 
 */
public class InfoBoxParser extends DefaultHandler {

	// For SAX Parser
	private boolean textElementStart = false;
	private StringBuilder textElementCompleteData = new StringBuilder("");
	private String textString = "";

	boolean titleElementStart = false;
	private String title = "";
	
	// MAPS

	// Map of title and infobox text of a wikipedia page
	private static Map<String, String> titleInfoBoxRawMap = new HashMap<>();

	// A map of Key -- title of the page && value -- infobox map of its properties
	@SuppressWarnings("rawtypes")
	private static Map<String, HashMap> titleInfoBoxMap = new HashMap<>();

	// A Map of Key -- title of thr page && value -- links in the infobox
	@SuppressWarnings("rawtypes")
	private static Map<String, HashSet> titleInfoBoxLinks = new HashMap<>();

	
	@SuppressWarnings("rawtypes")
	public Map<String, HashMap> getTitleInfoboxMap() {
		// TODO Auto-generated method stub
		return titleInfoBoxMap;

	}
	@SuppressWarnings({ "rawtypes"})
	public Map<String, HashSet> getTitleInfoboxLinks() {
		// TODO Auto-generated method stub
		return titleInfoBoxLinks;

	}
	
	
	
	/**
	 * Called internally by SAX Parser when it encounters a startElement
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("title"))
			titleElementStart = true;
		if (qName.equalsIgnoreCase("text")) {
			textElementStart = true;
			textElementCompleteData.setLength(0);
		}
	}

	/**
	 * Called internally by SAX Parser when it encounters an End Element
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equalsIgnoreCase("title"))
			titleElementStart = false;
		if (qName.equalsIgnoreCase("text")) {
			textElementStart = false;
		//	WikiTextParser wikiParser = new WikiTextParser(textElementCompleteData.toString());

			try {
			//	InfoBox infoBox = wikiParser.getInfoBox();
			//	titleInfoBoxRawMap.put(title, infoBox.dumpRaw());
			} catch (Exception e) {
				//e.printStackTrace();
				System.out.println("Page with title  '"+title+"'   does not contain an Infobox.");
			}
		}
	}

	/**
	 * Called internally by SAX Parser when it encounters Charachters
	 */
	public void characters(char ch[], int start, int length)
			throws SAXException {
		if (titleElementStart)
			title = new String(ch, start, length);
		if (textElementStart) {
			textString = new String(ch, start, length);
			textElementCompleteData = textElementCompleteData
					.append(textString);
		}
	}

	/**
	 * Constructor : Initializes SAX Parser Calls parseAndSetInfoBoxFields to
	 * extract Info box from Wikipedia page and set InfoBoxBean object with
	 * constituent fields
	 * 
	 * @param fileName
	 *            : location of the input file
	 */
	public InfoBoxParser(String fileName) {
		SAXParserFactory saxParserfactory = SAXParserFactory.newInstance();

		try {
			SAXParser saxParser = saxParserfactory.newSAXParser();
			if (fileName != null && !fileName.equals(""))
				saxParser.parse(new File(fileName), this);

			// Iterate over the collection
			Iterator<Entry<String, String>> it = titleInfoBoxRawMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entryPair = (Map.Entry) it.next();
				formatInfoBoxForParsing(entryPair.getKey().toString(),
						entryPair.getValue().toString());
			}
		} catch (ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param title
	 * @param infoBoxString
	 */
	private void formatInfoBoxForParsing(String title, String infoBoxString) {

		boolean bracketOpen = false;
		
		infoBoxString = infoBoxString.replaceFirst("[{]+", "");
		
		for (int i = 0 ; i < infoBoxString.length() ; i++)
		{
			char ch = infoBoxString.charAt(i);
			
			if (ch == '{' || ch == '[')
				bracketOpen = true;
			if (ch == '}' || ch == ']')
				bracketOpen = false;
			
			if (bracketOpen && ch == '|')
				infoBoxString = infoBoxString.substring(0, i) + "~" + infoBoxString.substring(i+1);
		}
		
		// Replace pipe by newline
		infoBoxString = infoBoxString.replaceAll("[\\|]", "\n");
		infoBoxString = infoBoxString.replaceAll("[~]", "|");
		// Process links
		String processedInfoBox = processLinks(title , infoBoxString);
		//System.out.println(processedInfoBox);
		//System.out.println(infoBoxString);
		// if ("Saturday Supercade".equals(title))
		//setInfoBoxMap(title, infoBoxString);
		setInfoBoxMap(title, processedInfoBox);
	}

	/**
	 * Split the entire text by NewLine
	 * In each array element first char to = is the key
	 * After =, every thing is Value  
	 * set infoBoxPropsMap with key value pair
	 * set titleInfoBoxMap with title and the infoBoxPropsMap pair.
	 * 
	  * @param title
	 * @param infoBoxString
	 */
	private void setInfoBoxMap(String title, String infoBoxString) {
		String[] infoBoxArr = infoBoxString.split("[\n]");
		String key = "";
		String value = "";
		String lasEnteredKey = "";
		String tempValue = "";

		boolean resetLastEntry = false;
		
		// Single infoboxmap with its properties and their values
		HashMap<String, String> infoBoxPropsMap = new HashMap<>();

		
		
		// System.out.println(title);
		//System.out.println(infoBoxString);
		
		// Add the first key -- Type of Info Box
		infoBoxPropsMap.put("TYPE",
				infoBoxString.substring(8, infoBoxString.indexOf("\n")).trim());

		for (int numProp = 1; numProp < infoBoxArr.length - 1; numProp++) 
		{
			if (!(infoBoxArr[numProp].trim().isEmpty()
					|| "".equals(infoBoxArr[numProp].trim()) || infoBoxArr[numProp] == null)) {
				try {

					key = infoBoxArr[numProp].split("[=]")[0].trim().replaceAll("[\\s]+", " ");
					value = infoBoxArr[numProp].split("[=]")[1].trim().replaceAll("[\\s]+", " ");

					resetLastEntry = true;
					if (!"".equals(tempValue) && resetLastEntry) {
						resetLastEntry = false;
						infoBoxPropsMap.remove(lasEnteredKey);
						infoBoxPropsMap.put(lasEnteredKey,tempValue.replaceAll("[\\s]+", " "));
						tempValue = "";
					}
					if (!(value.isEmpty() || "".equals(value) || value == null)
							&& !(key.isEmpty() || "".equals(key) || key == null)) {
						infoBoxPropsMap.put(key, value);
						lasEnteredKey = key;
					}
				} catch (Exception e) {
					if (!infoBoxArr[numProp].endsWith("=")) {
						if ("".equals(tempValue))
							tempValue = value + " " + key;
						else
							tempValue = tempValue + " " + key;
					}
				}
			}
		}
		titleInfoBoxMap.put(title, infoBoxPropsMap);
	}

	// FROM FIRST PROJECT
	/**
	 * Method to parse links and URLs. Refer:
	 * http://en.wikipedia.org/wiki/Help:Wiki_markup#Links_and_URLs
	 * 
	 * @param text
	 *            : The text to be parsed
	 * @return An array containing two elements as follows - The 0th element is
	 *         the parsed text as visible to the user on the page The 1st
	 *         element is the link url
	 */
	public static String[] parseLinks(String text) {
		String[] linksArray = new String[2];
		String tempStartText = "";
		String tempEndText = "";
		if (text == null || text.equals("")) {
			linksArray[0] = "";
			linksArray[1] = "";

			return linksArray;
		}

		// For External Links
		if (text.startsWith("[http://")) {
			text = text.replace("[", "").replace("]", "");
		}

		// for blending examples
		if (text.contains("[[")) {
			tempStartText = text.substring(0, text.indexOf("[["));
			tempEndText = text.substring(text.indexOf("]]") + 2);
			text = text.substring(text.indexOf("[[") + 2, text.indexOf("]]"));
		}

		if (text.toLowerCase().startsWith("image:")) {
			text = "";
			return linksArray;
		}

		if (text.contains("|")) {
			if (text.indexOf("|") != text.lastIndexOf("|")) {
				// multiple Splits
				String[] tempStringArray = text.split("[|]");
				linksArray[1] = "";
				for (int arraySize = 0; arraySize < tempStringArray.length; arraySize++) {
					if (arraySize == tempStringArray.length - 1)
						linksArray[0] = tempStringArray[arraySize];
				}
			} else {
				if (text.endsWith("|")) {
					// Remove pipe from the end
					// NO SPLIT
					text = text.replace("|", "");
					// "|" is present at end.. Processing needed

					// check for ":"
					if (text.contains(":")) {
						if (text.indexOf(":") == text.lastIndexOf(":")) {
							if (text.indexOf("#") != -1) {
								// NO PROCESSING
								linksArray[0] = text;
								linksArray[1] = "";
							} else {
								// Single ":" .. Split By : and Check for () and
								// , Auto Hiding

								linksArray[0] = text.split(":")[1];
								linksArray[1] = "";

								if (text.indexOf("(") != -1) {
									linksArray[0] = tempStartText
											+ linksArray[0].substring(0,
													linksArray[0].indexOf("("))
													.trim() + tempEndText;
								} else if (text.indexOf(",") != -1) {
									linksArray[0] = tempStartText
											+ linksArray[0].substring(0,
													linksArray[0].indexOf(","))
													.trim() + tempEndText;
								} else {
									// Do nothing
								}
							}
						} else {
							// multiple Splits
							String[] tempStringArray = text.split("[:]");
							linksArray[1] = "";
							for (int element = 1; element < tempStringArray.length; element++) {
								if (linksArray[0] == null
										|| linksArray[0].equals(""))
									linksArray[0] = tempStringArray[element]
											+ ":";
								else
									linksArray[0] = linksArray[0]
											+ tempStringArray[element];
							}
						}

					} else {
						// Check for Brackets and comma and automatically hide
						// them.
						if (text.indexOf("(") != -1) {
							linksArray[0] = tempStartText
									+ text.substring(0, text.indexOf("("))
											.trim() + tempEndText;
							linksArray[1] = text.replace(" ", "_");
							// AUTO CAPITALIZATION
							linksArray[1] = linksArray[1].substring(0, 1)
									.toUpperCase() + linksArray[1].substring(1);
						} else if (text.indexOf(",") != -1) {
							linksArray[0] = tempStartText
									+ text.substring(0, text.indexOf(","))
											.trim() + tempEndText;
							linksArray[1] = text.replace(" ", "_");
							// AUTO CAPITALIZATION
							linksArray[1] = linksArray[1].substring(0, 1)
									.toUpperCase() + linksArray[1].substring(1);
						} else {
							linksArray[0] = tempStartText + text + tempEndText;
							linksArray[1] = text.replace(" ", "_");
							// AUTO CAPITALIZATION
							linksArray[1] = linksArray[1].substring(0, 1)
									.toUpperCase() + linksArray[1].substring(1);
						}

					}
				} else {
					// Single Split

					// check for ":"
					if (text.contains(":")) {
						linksArray[0] = tempStartText + text.split("[|]")[1]
								+ tempEndText;
						linksArray[1] = text.split("[|]")[0];

						if (linksArray[1].startsWith("Wik")) {
							// auto capitalization
							linksArray[1] = linksArray[1].substring(0, 1)
									.toUpperCase() + linksArray[1].substring(1);
							linksArray[1] = linksArray[1].replaceAll(" ", "_");
						} else {
							linksArray[1] = "";
						}
					} else {
						linksArray[0] = tempStartText + text.split("[|]")[1]
								+ tempEndText;
						linksArray[1] = text.split("[|]")[0].replaceAll(" ",
								"_");
						// Auto Capitalization
						linksArray[1] = linksArray[1].substring(0, 1)
								.toUpperCase() + linksArray[1].substring(1);
					}
				}

			}
		} else if (text.contains(":")) {
			// Does not contain "|" and contains Name Space
			// Handle Categories
			if (text.contains("Category:")) {
				linksArray[0] = text.substring(text.indexOf(":") + 1);
				linksArray[1] = "";

			}
			// Process only if Name Space is WIKI
			else if (text.startsWith("Wik")) {
				linksArray[0] = tempStartText + text + tempEndText;
				linksArray[1] = "";
			}
			// Language links
			else if (text.charAt(2) == ':') {
				linksArray[0] = text;
				linksArray[1] = "";
			}
			// check if it is of the form http:
			else if (text.startsWith("http://")) {
				if (text.indexOf(" ") == -1) {
					linksArray[0] = "";
					linksArray[1] = "";
				} else {
					linksArray[0] = text.split("[ ]")[1];
					linksArray[1] = "";
				}
			} else {
				linksArray[0] = "";
				linksArray[1] = "";
			}
		} else {
			// does not contain "|" , ":" .. no splitting
			linksArray[0] = tempStartText + text + tempEndText;
			linksArray[1] = text.replace(" ", "_");
			// Auto Capitalization
			linksArray[1] = linksArray[1].substring(0, 1).toUpperCase()
					+ linksArray[1].substring(1);
		}

		return linksArray;

	}

	/**
	 * 1) increment counter , store its position in stack when [ is encountered.
	 * 2) decrement counter , every time ] is encountered. Take this as end
	 * position ; start position is last entry from stack. 3) take these inner
	 * brackets and process for links 4) replace the text and process the entire
	 * thing again.
	 * 
	 * @param completeText
	 */
	public static String processLinks(String title, String infoBoxText) {
		char[] buffer = new char[1024 * 1024];
		infoBoxText.getChars(0, infoBoxText.length(), buffer, 0);

		Stack bracketPos = new Stack();
		int beginIndex = 0;
		int endIndex = 0;
		String processingString = "";
		String[] stringArray = new String[2];
		
		// A set of infobox links
		HashSet<String> links = new HashSet<String>();

		for (int index = 0; index < infoBoxText.length() - 1; index++) {
			if (buffer[index] == '[' && buffer[index + 1] == '[') {
				bracketPos.push(index + 2);
			}
			if (buffer[index] == ']' && buffer[index + 1] == ']'
					&& !bracketPos.empty()) {
				endIndex = index;
				if (!bracketPos.isEmpty()) {
					beginIndex = (Integer) bracketPos.pop();
				}
				processingString = infoBoxText.substring(beginIndex, endIndex);

				stringArray = parseLinks(processingString);

				// replace the strings
				try
				{
					if (stringArray[1] != null && !stringArray[1].equals(""))
						links.add(new String(stringArray[1]));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
				// take out the difference in length of processing string and
				// stringArray[0] and fill it with spaces.
				if (stringArray[0] == null)
					stringArray[0] = "";

				int lengthdiff = (processingString.length() + 4)
						- stringArray[0].length();
				String spaces = "";
				for (int i = 1; i <= lengthdiff; i++) {
					spaces = spaces + " ";
				}
				infoBoxText = infoBoxText.substring(0, beginIndex - 2)
						+ stringArray[0] + spaces
						+ infoBoxText.substring(endIndex + 2);
			}
		}
		
		titleInfoBoxLinks.put(title, links);
		return infoBoxText;
	}

	/**
	 * Main method of the class Calls the constructor
	 * 
	 * @param args
	 *            : Location of i/p file
	 */
	public static void main(String[] args) {
		InfoBoxParser infoBoxParser = new InfoBoxParser("./file/five_entries.xml");
		InfoBoxBean infoBoxBean = new InfoBoxBean();
		
		infoBoxBean.setTitleInfoBoxLinks(titleInfoBoxLinks);
		infoBoxBean.setTitleInfoBoxMap(titleInfoBoxMap);
		System.out.println(titleInfoBoxMap.size());
		for (Map.Entry<String, HashMap> e : titleInfoBoxMap.entrySet())
		    System.out.println(e.getKey() + ": " + e.getValue());
	}

}
