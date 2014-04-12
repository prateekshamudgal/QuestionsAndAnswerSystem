package edu.buffalo.ui;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WikiParser 
{
	private final String urlStr = "http://en.wikipedia.org/wiki/";
	
	public String extractFirstPara(String title) throws IOException
	{
		String url = urlStr + title;
		Document doc = Jsoup.connect(url).timeout(1000*1000).get();
		doc.setBaseUri(urlStr);
		
		Elements paragraphs = doc.select(".mw-content-ltr p");
	    Element firstParagraph = paragraphs.first();
		
	    return firstParagraph.text();
	}
	
	public String extractInfoBox(String title) throws IOException
	{
		String url = urlStr + title;
		Document doc = Jsoup.connect(url).timeout(1000*1000).get();
		doc.setBaseUri(urlStr);
		
		Element contentDiv = doc.select("div[id=content]").first();
		Elements infobox = contentDiv.select("table.infobox");
	    
	    if (infobox.isEmpty())
	    {
	    	System.out.println("Info box does not Exist");
	    	return "";
	    }

	    return infobox.toString();
	}
}
