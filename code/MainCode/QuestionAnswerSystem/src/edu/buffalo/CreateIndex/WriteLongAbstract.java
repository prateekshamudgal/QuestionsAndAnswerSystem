package edu.buffalo.CreateIndex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.sound.sampled.Line;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

public class WriteLongAbstract {
	public static final String WikiLongAbstract = "http://localhost:8983/solr/WikiLongAbstract";
	SolrInputDocument doc = new SolrInputDocument();
	HttpSolrServer LongAbstractServer;
	static final String read= "/home/rahul/Downloads/wikiData/Lesi.txt";
	//static final String InfoboxReader= "/home/rahul/Downloads/wikiData/long_abstract.txt";
	BufferedReader br;

	public void fileOpen() throws FileNotFoundException
	{
		br = new BufferedReader(new FileReader(read));
	}
	public void Connection() throws SolrServerException
	{
		LongAbstractServer = new HttpSolrServer(WikiLongAbstract);
	}

	public void write() throws IOException, SolrServerException
	{
		fileOpen();
		Connection();
		String line;
		int LineCount = 0;
		while((line=br.readLine())!= null)
		{
			
			String[] values = line.split("~");
			
			if(values.length==7)
			{
				
				doc.addField("WikiName", values[0]);
				doc.addField("WikiActualName", values[0].replaceAll("_", " "));
				doc.addField("WikiLongAbstract", values[1]);
				doc.addField("WikiInfobox", values[2]);
				doc.addField("WikiLongStatic", Double.parseDouble(values[3]));
				doc.addField("WikiExternalStatic", Double.parseDouble(values[4]));
				doc.addField("WikiShortStatic", Double.parseDouble(values[5]));
				doc.addField("WikiInfoboxStatic", Double.parseDouble(values[6]));
				
			}
			LongAbstractServer.add(doc);
			doc = new SolrInputDocument();
			LineCount++;
			if(LineCount%5000 == 0)
			{
				LongAbstractServer.commit();
				System.out.println("5000 committed");
			}
				
			
		}
		LongAbstractServer.commit();
		System.out.println("final commit");
		System.out.println("Completed :)");
	}
	
	public static void main(String[] args) throws IOException, SolrServerException {
		WriteLongAbstract wla = new WriteLongAbstract();
		wla.write();
	}

}
