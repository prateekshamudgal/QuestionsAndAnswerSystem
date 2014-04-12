package edu.buffalo.CreateIndex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.hadoop.io.InputBuffer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

public class WriteShortAbstract {
	public static final String WikiShortAbstract = "http://localhost:8983/solr/WikiShortAbstract";
	SolrInputDocument doc = new SolrInputDocument();
	HttpSolrServer ShortAbstractServer;
	static final String read= "/home/rahul/Downloads/wikiData/short_abstract.txt";
	BufferedReader br;

	public void fileOpen() throws FileNotFoundException
	{
		br = new BufferedReader(new FileReader(read));
	}
	public void Connection() throws SolrServerException
	{
		ShortAbstractServer = new HttpSolrServer(WikiShortAbstract);
	}

	public void write() throws IOException, SolrServerException
	{
		fileOpen();
		Connection();
		String line;
		int LineCount = 0;
		while((line=br.readLine())!= null)
		{

			int count = 0;
			LineCount++;
			String[] values = line.split("~");
			if(values.length==2 && values[1] != null)
			{
				count = values[1].split("\\s+").length;
				doc = new SolrInputDocument();
				doc.addField("WikiName", values[0]);
				doc.addField("WikiShortAbstract", values[1]);
				doc.addField("WikiSmallStatic", count);
				ShortAbstractServer.add(doc);

				if(LineCount%5000 == 0)
				{
					ShortAbstractServer.commit();
					System.out.println("5000 committed");
				}
			}

		}
		System.out.println("Completed :)");
	}
	public static void main(String[] args) {
		WriteShortAbstract wsa = new WriteShortAbstract();
		try {
			wsa.write();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
