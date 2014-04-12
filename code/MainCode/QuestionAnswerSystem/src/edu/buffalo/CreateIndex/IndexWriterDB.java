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
import org.apache.solr.search.grouping.endresulttransformer.MainEndResultTransformer;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;

/*
 * @Author RahulTejwani
 */

public class IndexWriterDB {

	public static final String WikiPerson = "http://localhost:8983/solr/WikiPerson";
	//	public static final String WikiOrganization = "http://localhost:8983/solr/WikiOrganization";
	//	public static final String WikiLocation = "http://localhost:8983/solr/WikiLocation";
	//	public static final String WikiMisc = "http://localhost:8983/solr/WikiMisc";
	String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
	AbstractSequenceClassifier<?> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
	Map<String, String> person = new HashMap<>();
	Map<String, String> location = new HashMap<>();
	Map<String, String> organization = new HashMap<>();
	Map<String, String> misc = new HashMap<>();

	public void initializePerson()
	{
		person.put("birth_name", "WikiBirthName");
		person.put("birth_place", "WikiPlaceOfBirth");
		person.put("death_place", "WikiPlaceOfDeath");
		person.put("death_date", "WikiDateOfDeath");
		person.put("birth_date", "WikiDateOfBirth");
		person.put("spouse", "WikiSpouce");
		person.put("children", "WikiChildren");
		person.put("parents", "WikiParents");
		person.put("occupation", "WikiOccupation");
		person.put("office", "WikiOffice");
		person.put("net_worth", "WikiNetWorth");
		person.put("death_cause", "WikiDeathCause");
		person.put("nationality", "WikiNationality");
		person.put("education", "WikiEducation");
		person.put("salary", "WikiSalary");
		person.put("height", "WikiHeight");
		person.put("weight", "WikiWeight");
		person.put("age", "WikiAge");
		person.put("years_active", "WikiYearsActive");
		person.put("website", "WikiWebsite");		
	}

	public String FindCore(String name){
		String core = "misc";
		name = name.replaceAll("_", " ");
		String buffer = classifier.classifyWithInlineXML(name);
		System.out.println(buffer);


		if(buffer.startsWith("<PERSON>") && buffer.endsWith("</PERSON>"))
			core = "person";
		else if(buffer.startsWith("<LOCATION>") && buffer.endsWith("</LOCATION>"))
			core = "location";
		else if(buffer.startsWith("<ORGANIZATION>") && buffer.endsWith("</ORGANIZATION>"))
			core = "organization";

		return core;
	}

	public void WriteIndex() throws FileNotFoundException,IOException
	{
		String line = null;
		HttpSolrServer PersonServer = new HttpSolrServer(WikiPerson);
		//		HttpSolrServer LocationServer = new HttpSolrServer(WikiLocation);
		//		HttpSolrServer OrganizationServer = new HttpSolrServer(WikiOrganization);
		//		HttpSolrServer MiscServer = new HttpSolrServer(WikiMisc);
		try {
			//			LocationServer.deleteByQuery( "*:*" );
			PersonServer.deleteByQuery("*:*");
			//			OrganizationServer.deleteByQuery("*:*");
			//			MiscServer.deleteByQuery("*:*");
		} catch (SolrServerException e2) {
			// TODO Auto-generated catch block
			System.out.println("Error in deleting all docs");
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		boolean flagNewDoc = false;
		BufferedReader br = new BufferedReader(new FileReader("/media/FreeAgent Drive/HelloWorld/WikiDB/InfoBox.txt"));
		String previous = null;
		int count =0;

		SolrInputDocument Pdoc = new SolrInputDocument();
		initializePerson();
		while (line != null) {
			System.out.println(line);

			String core = "";
			String[] triplet = line.split("~");
		try {
			// StringBuilder sb = new StringBuilder();
			line = br.readLine();

			
				if(previous != triplet[0])
				{

					core = FindCore(triplet[0]);
					//System.out.println(triplet[0]);
					flagNewDoc = true;
					previous = triplet[0];
				}


				if(core.equals("person"))
				{
					if(flagNewDoc)
					{
						count++;
						PersonServer.add(Pdoc);
						if(count%100==0)
							PersonServer.commit();
						System.out.println("commit");
						flagNewDoc = false;
					}
					Pdoc.addField("WikiName", triplet[0]);

					if(person.containsKey(triplet[1]))
					{
						Pdoc.addField(person.get(triplet[1]), triplet[2]);
					}
					else
						Pdoc.addField("WikiMisc", triplet[1] + "~" + triplet[2]);
				}
				if(core.equals("location"))
				{

				}
				if(core.equals("organization"))
				{

				}
				if(core.equals("misc"))
				{

				}
				
			}catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				br.close();
				try {
					PersonServer.commit();
				} catch (SolrServerException e) {
					// TODO Auto-generated catch block
					System.out.println("commit error");
					e.printStackTrace();
				}

			}

		} 


	}
	public static void main(String[] args) throws IOException, SolrServerException 
	{
		IndexWriterDB iw = new IndexWriterDB();
		iw.WriteIndex();

	}

}
