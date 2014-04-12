/**
 * 
 */
package edu.buffalo.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author PrateekshaMudgal
 *
 */
public class InfoBoxBean {

	// A map of Key -- title of the page && value -- infobox map of its properties
	private static Map<String, HashMap> titleInfoBoxMap = new HashMap<>();

	// A Map of Key -- title of thr page && value -- links in the infobox
	private static Map<String, HashSet> titleInfoBoxLinks = new HashMap<>();
	/**
	 * 
	 */
	public InfoBoxBean() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the titleInfoBoxMap
	 */
	public static Map<String, HashMap> getTitleInfoBoxMap() {
		return titleInfoBoxMap;
	}
	/**
	 * @param titleInfoBoxMap the titleInfoBoxMap to set
	 */
	public static void setTitleInfoBoxMap(Map<String, HashMap> titleInfoBoxMap) {
		InfoBoxBean.titleInfoBoxMap = titleInfoBoxMap;
	}
	/**
	 * @return the titleInfoBoxLinks
	 */
	public static Map<String, HashSet> getTitleInfoBoxLinks() {
		return titleInfoBoxLinks;
	}
	/**
	 * @param titleInfoBoxLinks the titleInfoBoxLinks to set
	 */
	public static void setTitleInfoBoxLinks(Map<String, HashSet> titleInfoBoxLinks) {
		InfoBoxBean.titleInfoBoxLinks = titleInfoBoxLinks;
	}

}
