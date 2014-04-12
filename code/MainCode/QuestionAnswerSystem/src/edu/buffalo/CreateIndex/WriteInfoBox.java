package edu.buffalo.CreateIndex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

/*
 * @Author RahulTejwani
 */

public class WriteInfoBox {

	public static final String WikiInfobox = "http://localhost:8983/solr/WikiInfobox";
	Map<String, String> Infobox = new HashMap<>();
	SolrInputDocument doc = new SolrInputDocument();
	HttpSolrServer InfoboxServer;
	BufferedReader br;
	static final String path = "/home/rahul/Downloads/wikiData/InfoBox.txt";
	public void EstabilishConnection() 
	{
		InfoboxServer = new HttpSolrServer(WikiInfobox);
	}

	public void FileRead()
	{
		try{
			br = new BufferedReader(new FileReader(path));
		}catch (FileNotFoundException e){
			System.out.println("File not found");
		}

	}


	public void FillMap()
	{
		Infobox.put("name", "WikiBirthName");
		Infobox.put("birthName", "WikiBirthName");
		Infobox.put("nativeName", "WikiBirthName");
		Infobox.put("birthPlace", "WikiPlaceOfBirth");
		Infobox.put("deathPlace", "WikiPlaceOfDeath");
		Infobox.put("deathDate", "WikiDateOfDeath");
		Infobox.put("birthDate", "WikiDateOfBirth");
		Infobox.put("shortDescription", "WikiShortDescription");
		Infobox.put("spouse", "WikiSpouce");
		Infobox.put("partner", "WikiSpouce");
		Infobox.put("children", "WikiChildren");
		Infobox.put("parents", "WikiParents");
		Infobox.put("occupation", "WikiOccupation");
		Infobox.put("office", "WikiOffice");
		Infobox.put("networth", "WikiNetWorth");
		Infobox.put("knownFor", "WikiKnownFor");
		Infobox.put("title", "WikiTitle");
		Infobox.put("deathCause", "WikiDeathCause");
		Infobox.put("nationality", "WikiNationality");
		Infobox.put("citizenship", "WikiNationality");
		Infobox.put("education", "WikiEducation");
		Infobox.put("salary", "WikiSalary");
		Infobox.put("height", "WikiHeight");
		Infobox.put("weight", "WikiWeight");
		Infobox.put("age", "WikiAge");
		Infobox.put("yearsActive", "WikiYearsActive");
		Infobox.put("website", "WikiWebsite");	
		Infobox.put("capital", "WikiCapital");
		Infobox.put("CommonLanguages", "WikiLanguage");
		Infobox.put("officialLanguages", "WikiLanguage");
		Infobox.put("language", "WikiLanguage");
		Infobox.put("currency", "WikiCurrency");
		Infobox.put("totalarea", "WikiArea");
		Infobox.put("waterarea", "WikiArea");
		Infobox.put("areaTotalSqMi", "WikiArea");
		Infobox.put("areaKm", "WikiArea");
		Infobox.put("areaSqMi", "WikiArea");
		Infobox.put("landarea", "WikiArea");
		Infobox.put("populationAsOf", "WikiPopulation");
		Infobox.put("populationTotal", "WikiPopulation");
		Infobox.put("populationCensus", "WikiPopulation");
		Infobox.put("2000pop", "WikiPopulation");
		Infobox.put("population", "WikiPopulation");
		Infobox.put("pop", "WikiPopulation");
		Infobox.put("timezone", "WikiTimeZone");
		Infobox.put("timeZone", "WikiTimeZone");
		Infobox.put("country", "WikiCountry");
		Infobox.put("state", "WikiState");
		Infobox.put("industry", "WikiIndustry");
		Infobox.put("founder", "WikiFounder");
		Infobox.put("founded", "WikiFounded");
		Infobox.put("employees", "WikiEmployees");
		Infobox.put("numEmployees", "WikiEmployees");
		Infobox.put("products", "WikiProducts");	
		Infobox.put("revenue", "WikiRevenue");	
		Infobox.put("headquarter", "WikiHeadquarters");	
		Infobox.put("locationCity", "WikiHeadquarters");	
		Infobox.put("type", "WikiType");	

	}

	public void write() throws IOException, SolrServerException
	{
		FileRead();
		FillMap();
		int totalCount = 0;
		EstabilishConnection();
		int count = 0;
		String prev = "";
		String line;

		while((line = br.readLine()) != null)
		{
			String[] triplets = line.split("~");
			if(! triplets[0].equals("") && triplets[0]!=null && triplets.length == 3)
			{
				if(! prev.equals(triplets[0]) && triplets[0] != null)
				{
					if(!doc.isEmpty())
					{

						try{
							InfoboxServer.add(doc);
							doc = new SolrInputDocument();
							count++;
						}catch (SolrServerException e)
						{
							e.printStackTrace();
							System.out.println("Error Adding Document");
						}
					}
					else
					{
						doc.addField("WikiName", triplets[0]);
						prev = triplets[0];
						if(Infobox.containsKey(triplets[1]))
						{
							doc.addField(Infobox.get(triplets[1]), triplets[2]);
						}
						else
						{
							doc.addField("WikiMisc", triplets[1] + "~" + triplets[2]);
						}
					}
				}
				else
				{
					if(Infobox.containsKey(triplets[1]))
					{
						doc.addField(Infobox.get(triplets[1]), triplets[2]);
					}
					else
					{
						doc.addField("WikiMisc", triplets[1] + "~" + triplets[2]);
					}
				}
			}

			if(count%50000 == 0)
			{
				InfoboxServer.commit();
				totalCount = totalCount + 5000;
				System.out.println(totalCount + ": docs written");
			}
		}
		
		InfoboxServer.commit();
		System.out.println("final commit");
	}

	public static void main(String[] args) {
		WriteInfoBox wi = new WriteInfoBox();
		try {
			wi.write();
		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			System.out.println("File read error");
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Solr Server connection error");
		}
	}


}
