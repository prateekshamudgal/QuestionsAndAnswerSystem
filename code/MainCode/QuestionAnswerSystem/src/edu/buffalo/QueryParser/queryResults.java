package edu.buffalo.QueryParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import edu.buffalo.queryanalyser.QueryParser;
import uk.ac.shef.wit.simmetrics.similaritymetrics.QGramsDistance;

public class queryResults {
	public static final String WikiLongAbstract = "http://localhost:8983/solr/WikiLongAbstract";
	public static final String WikiShortAbstract = "http://localhost:8983/solr/WikiShortAbstract";
	public static final String WikiInfobox = "http://localhost:8983/solr/WikiInfobox";
	public String PrimaryEntity = "";
	public String SecondaryEntity = "";
	HttpSolrServer LongAbstractServer = new HttpSolrServer(WikiLongAbstract);
	HttpSolrServer ShortAbstractServer = new HttpSolrServer(WikiShortAbstract);
	HttpSolrServer InfoboxServer = new HttpSolrServer(WikiInfobox);
	SolrQuery q1 ;
	SolrQuery q2 = new SolrQuery();
	SolrQuery q3 = new SolrQuery();
	ArrayList<String> names = new ArrayList<String>();
	QGramsDistance ds;
	boolean isPhrase;
	boolean hasAttribute;
	public String answer;
	public String handle;

	public queryResults(String entity,QueryParser qp,String path)

	{
		System.out.println("Entity: "+entity);
		qp.setQuery(entity);
		qp.ParseQuery();
		this.PrimaryEntity = qp.getTopic().trim();
		this.SecondaryEntity = qp.getAttribute();
		System.out.println("secondary entity"+SecondaryEntity);
		this.isPhrase = qp.isPhrase();
		this.hasAttribute = qp.isProcessed();
		System.out.println("has attribute : "+ hasAttribute);
		System.out.println("Is Phase  : "+ isPhrase);
		//System.out.println();
	}

	private QueryResponse primaryQueryGenerator(boolean isPhrase) throws SolrServerException
	{	
		QueryResponse response = new QueryResponse();
		String q = "";

		if(!isPhrase)
				q = "(WikiActualName:" + PrimaryEntity + ")^2 AND WikiLongAbstract:" + PrimaryEntity +
				" AND WikiInfobox:" + PrimaryEntity;
		else
		{
			q = "WikiLongAbstract:\"" + PrimaryEntity +
					"\"~0"+ " OR WikiInfobox:\"" + PrimaryEntity + "\"~0";
		}
		

		q1 = new SolrQuery();
		q1.set("q", q);
		q1.set("sort", "sum(product(WikiLongStatic,0.60),product(WikiShortStatic,0.25)"
				+ ",product(WikiExternalStatic,0.10)) desc");
		q1.set("qt", "/spell");
		q1.set("wt", "xml");
		q1.set("rows", 30);
		System.out.println("query: "+q);
		response = LongAbstractServer.query(q1);
		//SolrDocumentList t = response.getResults();
		//System.out.println(t.getNumFound());
		return response;
	}
	public QueryResponse query()
	{
		QueryResponse response = new QueryResponse();
		q1.set("q", PrimaryEntity);
		return response;

	}
	public double calculateSimilarity(String s1, String s2, int count)
	{
		ds =  new QGramsDistance();
		double d =ds.getSimilarity(s1, s2);
		double staScore = 1/count;
//		System.out.println("Similarity = "+d);
		//System.out.println(s1+" ; "+s2);
		return 0.8*d + 0.2*staScore;
	}
	public String findTopic () throws SolrServerException
	{
		QueryResponse response = new QueryResponse();
		double val;
		double max =0;
		String result = null;
		int count = 1,maxIndex = -1;
		response = primaryQueryGenerator(isPhrase);
		SolrDocumentList d = response.getResults();
		SpellCheckResponse spellCheckResponse = response.getSpellCheckResponse();
		for (SolrDocument solrDocument : d) {
			String temp = solrDocument.getFieldValue("WikiActualName").toString();
			temp = temp.replaceAll("\\[", "");
			temp = temp.replaceAll("\\]", "");
			names.add(temp);
			
			
		}
		//System.out.println("primary entity: "+PrimaryEntity);
		for (String string : names) {
			val = calculateSimilarity(string.toLowerCase(), PrimaryEntity.toLowerCase(),count);
//			System.out.println("primary: "+qr.PrimaryEntity);
		//	System.out.println("string: "+string.toLowerCase());
	//		System.out.println(string.replaceAll(" ", "_") +" score value = "+ val);
			if(val>max)
			{
				max = val;
				maxIndex = count -1;
			}
			count++;
		}
		if(maxIndex == -1)
		{
			if (!spellCheckResponse.isCorrectlySpelled()) {
				result = spellCheckResponse.getCollatedResult();
				if(result == null)
				{
					result = spellCheckResponse.getFirstSuggestion(PrimaryEntity);
				}
			    
			}
		}
		else
		{
			result = names.get(maxIndex);
			result = result.replaceAll(" ", "_");
		}
	
		System.out.println(result);
		
	return result;
	}
	public void findAttribute() throws SolrServerException
	{
		String result;
		Collection<Object> misc;
		answer = "";
		int count1 = -1;
		double max = 0.4;
		int maxIndex = -1;
		//HashMap<String, String> otherFields = new HashMap<String, String>();
		handle = findTopic();
		System.out.println("Wikipedia Handle:"+ handle);
		//boolean hasAttribute = false;
		QueryResponse response = new QueryResponse();
		q2 = new SolrQuery();
		q2.set("q", "WikiName:\"+" + handle + "\"~1");
		q2.set("rows", 20);
		response = InfoboxServer.query(q2);
		SolrDocumentList docs = response.getResults();
		for (SolrDocument solrDocument : docs) {
			count1++;
			String temp1 = solrDocument.getFieldValue("WikiName").toString();
			if(temp1.equals(PrimaryEntity))
			{
				break;
			}
			
		}
		SolrDocument solrDocument = docs.get(count1);
		System.out.println(solrDocument.getFieldNames().toString());
	//	System.out.println("check" + solrDocument.getFieldValue(SecondaryEntity).toString());
		if(hasAttribute && (solrDocument.getFieldValue(SecondaryEntity) != null))
		{
			
			answer = solrDocument.getFieldValue(SecondaryEntity).toString();
			System.out.println("answer" + answer);
		}
		else
		{
			misc = solrDocument.getFieldValues("WikiMisc");
			ArrayList<Object> miscArr = new ArrayList<Object>();
			miscArr.addAll(misc);
			System.out.println(misc);
			String[] temp=null;
			//String[] temp = misc.split("~");
			SecondaryEntity = SecondaryEntity.replaceFirst("Wiki", "");
			for (int i = 0; i < miscArr.size(); i++) {
				temp = miscArr.get(i).toString().split("~");
				ds =  new QGramsDistance();
				double d =ds.getSimilarity(SecondaryEntity.toLowerCase(), temp[0].toLowerCase());
				System.out.println("query value: " + SecondaryEntity + " misc field: " + temp[0] + "Score: "+ d);
				if(d>max)
				{
					max= d;
					maxIndex = i;
				}
			}
			
			if(max>0.4)
			{
				answer = miscArr.get(maxIndex).toString().split("~")[1];
				//System.out.println("max : "+ max + "\tvalue" + temp[maxIndex]);
			}
			else
			{
				QueryResponse response1 = new QueryResponse();
				int count3 = -1;
				q3 = new SolrQuery();
				q3.set("q", "WikiName:\"+" + handle + "\"~1");
				q3.set("rows", 20);
				response1 = ShortAbstractServer.query(q3);
				SolrDocumentList list = response1.getResults();
				for (SolrDocument solrDocument2 : list) {
					count3++;
					String temp1 = solrDocument2.getFieldValue("WikiName").toString();
					//System.out.println("short Abstract Block"+temp1);
					if(temp1.equals(PrimaryEntity));
					{
						//System.out.println("break block");
						break;
					}
					
				}
				SolrDocument sd = list.get(count3);
				answer = sd.getFieldValue("WikiShortAbstract").toString();
				
			}
			System.out.println("max : "+ max );
			System.out.println("***************************************************");
			System.out.println("Answer is: " + answer);
		}
		
		
		
		
		
		
	}
	
	
	public static void main(String[] args) throws SolrServerException {
		//QueryParser qp = new QueryParser();
		//queryResults qr = new queryResults("what is capital of india",qp,);
		//qr.findAttribute();
		
	}

}
