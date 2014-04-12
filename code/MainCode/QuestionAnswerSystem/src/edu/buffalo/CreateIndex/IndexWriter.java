package edu.buffalo.CreateIndex;
/*
 * @Author RahulTejwani
 */

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;

import edu.buffalo.parser.InfoBoxParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IndexWriter {
	public static final String WikiPerson = "http://localhost:8983/solr/WikiPerson";
	public static final String WikiOrganization = "http://localhost:8983/solr/WikiOrganization";
	public static final String WikiLocation = "http://localhost:8983/solr/WikiLocation";
	public static final String WikiMisc = "http://localhost:8983/solr/WikiMisc";
	SelectCore sc = new SelectCore();
	
	
	
	
	public void connectToSolr(String core)
	{
		 HttpSolrServer server = new HttpSolrServer(core);
		 
	}
	
	public void WriteIndex()
	{
		/*
		 * Filling up the Maps containing classified Wiki Pages
		 * Establishing connection with solr
		 */
		int count = 0;
		sc.fillMap();
		Map<String , String> person = sc.getPersonMap();
		Map<String , String> location = sc.getLocationMap();
	//	System.out.println(location.keySet());
		Map<String , String> organization = sc.getOrganizationMap();
		Map<String , String> misc = sc.getMiscMap();
		Map<String, HashMap> infobox = sc.getParsedInfoboxMap();
		HttpSolrServer PersonServer = new HttpSolrServer(WikiPerson);
		HttpSolrServer LocationServer = new HttpSolrServer(WikiLocation);
		HttpSolrServer OrganizationServer = new HttpSolrServer(WikiOrganization);
		HttpSolrServer MiscServer = new HttpSolrServer(WikiMisc);
		try {
			LocationServer.deleteByQuery( "*:*" );
			PersonServer.deleteByQuery("*:*");
			OrganizationServer.deleteByQuery("*:*");
			MiscServer.deleteByQuery("*:*");
		} catch (SolrServerException e2) {
			// TODO Auto-generated catch block
			System.out.println("Error in deleting all docs");
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		for (Map.Entry<String, HashMap> e : infobox.entrySet())
		{
			
			SolrInputDocument doc = new SolrInputDocument();
			if(location.containsKey(e.getKey()))
			{
				doc.addField("WikiName", e.getKey());
				doc.addField("WikiMisc",e.getValue() );
				try {
					LocationServer.add(doc);
				} catch (SolrServerException e1) {
					// TODO Auto-generated catch block
					System.out.println("Error in adding docs");
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
			if(location.containsKey(e.getKey()))
			{
				doc.addField("WikiName", e.getKey());
				doc.addField("WikiMisc",e.getValue() );
				try {
					LocationServer.add(doc);
				} catch (SolrServerException e1) {
					// TODO Auto-generated catch block
					System.out.println("Error in adding docs");
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
			count++;
			if(count%100==0)
			{
				try
				{
				PersonServer.commit();
				LocationServer.commit();
				OrganizationServer.commit();
				MiscServer.commit();
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
		}
		
	}
		
	
	
  public static void main(String[] args) throws IOException, SolrServerException 
  {
//    HttpSolrServer server = new HttpSolrServer("http://localhost:8983/solr");
//    for(int i=0;i<1000;++i) {
//      SolrInputDocument doc = new SolrInputDocument();
//      doc.addField("cat", "book");
//      doc.addField("id", "book-" + i);
//      doc.addField("name", "The Legend of the Hobbit part " + i);
//      server.add(doc);
//      if(i%100==0) server.commit();  // periodically flush
//    }
//    server.commit(); 
	  IndexWriter iw = new IndexWriter();
	  iw.WriteIndex();
	 
		
 }
}