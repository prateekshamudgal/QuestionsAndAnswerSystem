package edu.buffalo.parser;
//import junit.framework.TestCase;
//import edu.jhu.nlp.util.FileUtil;
//import edu.jhu.nlp.wikipedia.*;

/**
 * @author Rahul Tejwani
 */
public class infoboxTest {
  public boolean doTest() {
//    String wikiText = FileUtil.readFile("./file/five_entries.xml");
  //  WikiTextParser parser = new WikiTextParser(wikiText);
   // WikiXMLParser wx = new WikiXMLDOMParser(wikiText);
    try {
    	
		//WikiPageIterator iter = wx.getIterator();
		//while(iter.hasMorePages()){
		//WikiPage page = iter.nextPage();
	//	InfoBox infoBox = parser.getInfoBox();
	//	System.out.println(infoBox.dumpRaw());
    	
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    
//    if(infoBox != null) 
//     System.out.println(infoBox.dumpRaw());
    return false;
  }

  public static void main(String [] args) {
    infoboxTest test = new infoboxTest();
    test.doTest();
  }
}