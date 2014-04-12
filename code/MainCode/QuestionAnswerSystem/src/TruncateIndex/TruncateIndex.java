package TruncateIndex;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;


public class TruncateIndex {
	//public static final String WikiInfobox = "http://localhost:8983/solr/WikiInfobox";
	//public static final String WikiOrganization = "http://localhost:8983/solr/WikiOrganization";
	//public static final String WikiLocation = "http://localhost:8983/solr/WikiLocation";
public static final String WikiShortAbstract = "http://localhost:8983/solr/WikiLongAbstract";
	//public static final String WikiMisc = "http://localhost:8983/solr/WikiMiscelleneous";
//	HttpSolrServer PersonServer;
//	HttpSolrServer LocationServer;
//	HttpSolrServer OrganizationServer;
//	HttpSolrServer InfoboxServer;
	HttpSolrServer ShortAbstractServer;
	
	public void Truncate() 
	{
	//	InfoboxServer = new HttpSolrServer(WikiInfobox);
//		LocationServer = new HttpSolrServer(WikiLocation);
//		OrganizationServer = new HttpSolrServer(WikiOrganization);
//		MiscServer = new HttpSolrServer(WikiMisc);
		ShortAbstractServer = new HttpSolrServer(WikiShortAbstract);
		try {
		//	InfoboxServer.deleteByQuery( "*:*" );
//			PersonServer.deleteByQuery("*:*");
//			OrganizationServer.deleteByQuery("*:*");
//			MiscServer.deleteByQuery("*:*");
			ShortAbstractServer.deleteByQuery("*:*");
			//ShortAbstractServer.commit();
		} catch (SolrServerException e2) {
			// TODO Auto-generated catch block
			System.out.println("Error in deleting all docs");
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			System.out.println("Exception in IO Block");
			e2.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		TruncateIndex ti = new TruncateIndex();
		ti.Truncate();
	}

}
