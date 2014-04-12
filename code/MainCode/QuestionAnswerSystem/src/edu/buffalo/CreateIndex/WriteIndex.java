package edu.buffalo.CreateIndex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;

public class WriteIndex {
	public static final String WikiPerson = "http://localhost:8983/solr/WikiPerson";
	public static final String WikiOrganization = "http://localhost:8983/solr/WikiOrganization";
	public static final String WikiLocation = "http://localhost:8983/solr/WikiLocation";
	public static final String WikiMisc = "http://localhost:8983/solr/WikiMiscelleneous";
	String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
	AbstractSequenceClassifier<?> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
	Map<String, String> person = new HashMap<>();
	Map<String, String> location = new HashMap<>();
	Map<String, String> organization = new HashMap<>();
	Map<String, String> misc = new HashMap<>();
	SolrInputDocument doc = new SolrInputDocument();
	SolrInputDocument loc = new SolrInputDocument();
	SolrInputDocument org = new SolrInputDocument();
	SolrInputDocument msc = new SolrInputDocument();
	BufferedReader br;
	HttpSolrServer PersonServer;
	HttpSolrServer LocationServer;
	HttpSolrServer OrganizationServer;
	HttpSolrServer MiscServer;
	static final String path = "/home/rahul/Downloads/wikiData/InfoBox.txt";

	public void EstabilishConnection() 
	{
		PersonServer = new HttpSolrServer(WikiPerson);
		LocationServer = new HttpSolrServer(WikiLocation);
		OrganizationServer = new HttpSolrServer(WikiOrganization);
		MiscServer = new HttpSolrServer(WikiMisc);
		try {
			LocationServer.deleteByQuery( "*:*" );
			PersonServer.deleteByQuery("*:*");
			//			OrganizationServer.deleteByQuery("*:*");
			//			MiscServer.deleteByQuery("*:*");
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

	public void FileRead()
	{
		try{
			br = new BufferedReader(new FileReader(path));
		}catch (FileNotFoundException e){
			System.out.println("File not found");
		}

	}
	public void initializeOrganzation()
	{
		organization.put("website", "WikiWebsite");
		
		organization.put("industry", "WikiIndustry");
		organization.put("founder", "WikiFounder");
		organization.put("founded", "WikiFounded");
		organization.put("employees", "WikiEmployees");
		
		organization.put("products", "WikiProducts");	
		organization.put("revenue", "WikiRevenue");	
		organization.put("headquarter", "WikiHeadquarters");	
		organization.put("type", "WikiType");	
		
	}

	public void initializePerson()
	{
		person.put("name", "WikiBirthName");
		person.put("birthPlace", "WikiPlaceOfBirth");
		person.put("deathPlace", "WikiPlaceOfDeath");
		person.put("deathDate", "WikiDateOfDeath");
		person.put("birthDate", "WikiDateOfBirth");
		person.put("spouse", "WikiSpouce");
		person.put("children", "WikiChildren");
		person.put("parents", "WikiParents");
		person.put("occupation", "WikiOccupation");
		person.put("office", "WikiOffice");
		person.put("netWorth", "WikiNetWorth");
		person.put("deathCause", "WikiDeathCause");
		person.put("nationality", "WikiNationality");
		person.put("education", "WikiEducation");
		person.put("salary", "WikiSalary");
		person.put("height", "WikiHeight");
		person.put("weight", "WikiWeight");
		person.put("age", "WikiAge");
		person.put("yearsActive", "WikiYearsActive");
		person.put("website", "WikiWebsite");		
	}

	public void initializeLocation()
	{

		location.put("capital", "WikiCapital");
		location.put("CommonLanguages", "WikiLanguage");
		location.put("officialLanguages", "WikiLanguage");
		location.put("language", "WikiLanguage");
		location.put("currency", "WikiCurrency");
		location.put("totalarea", "WikiArea");
		location.put("waterarea", "WikiArea");
		location.put("landarea", "WikiArea");
		location.put("populationAsOf", "WikiPopulation");
		location.put("timezone", "WikiTimeZone");
		location.put("country", "WikiCountry");
		location.put("state", "WikiState");
		location.put("website", "WikiWebsite");		
	}


	public String FindCore(String name){
		String core = "misc";
		name = name.replaceAll("_", " ");
		String buffer = classifier.classifyWithInlineXML(name);
		//System.out.println(buffer);


		if(buffer.startsWith("<PERSON>") && buffer.endsWith("</PERSON>"))
			core = "person";
		else if(buffer.startsWith("<LOCATION>") && buffer.endsWith("</LOCATION>"))
			core = "location";
		else if(buffer.startsWith("<ORGANIZATION>") && buffer.endsWith("</ORGANIZATION>"))
			core = "organization";

		return core;
	}

	public void Write() throws FileNotFoundException,IOException
	{
		int Pcount=1 , Lcount = 1, Ocount = 1, Mcount = 1;
		FileRead();
		String[] triplets = null;
		String line = "";
		/*
		 * filling the field maps
		 */
		EstabilishConnection();
		initializePerson();
		initializeLocation();
		initializeOrganzation();
		String prev = "";

		while((line = br.readLine()) != null)
		{
			triplets = line.split("~");
			if(! triplets[0].equals("") && triplets[0]!=null && triplets.length == 3)
			{
				String core = FindCore(triplets[0]);
				if(core.equals("person"))
				{
					if(! prev.equals(triplets[0])){
						String key = (String) doc.getFieldValue("WikiName");
						
						if(key != null)
						{
							try{
								PersonServer.add(doc);
								doc = new SolrInputDocument();
								Pcount++;
							}catch (SolrServerException e)
							{
								e.printStackTrace();
								System.out.println("Error Adding Document");
							}
						}
						else{
							doc.addField("WikiName", triplets[0]);
							System.out.println("Person: "+doc.getFieldValue("WikiName"));
							prev = triplets[0];
							if(person.containsKey(triplets[1]))
							{
								doc.addField(person.get(triplets[1]), triplets[2]);
								System.out.println("Fields-"+triplets[1]+"~"+triplets[2]);
							}
							else
							{
								doc.addField("WikiMisc", triplets[1] + "~" + triplets[2]);
								System.out.println("Misc-"+triplets[1]+"~"+triplets[2]);
							}
						}
					}
					else
					{
						if(person.containsKey(triplets[1]))
						{
							doc.addField(person.get(triplets[1]), triplets[2]);
							System.out.println("Fields-"+triplets[1]+"~"+triplets[2]);
						}
						else
						{
							doc.addField("WikiMisc", triplets[1] + "~" + triplets[2]);
							System.out.println("Misc-"+triplets[1]+"~"+triplets[2]);
						}

					}
				}
				if(core.equals("location"))
				{

					if(! prev.equals(triplets[0])){
						String key = (String) loc.getFieldValue("WikiName");

						if(key != null)
						{
							try{
								LocationServer.add(loc);
								loc = new SolrInputDocument();
								Lcount++;
							}catch (SolrServerException e)
							{
								e.printStackTrace();
								System.out.println("Error Adding Document");
							}
						}
						else{
							loc.addField("WikiName", triplets[0]);
							System.out.println("Location: "+loc.getFieldValue("WikiName"));
							prev = triplets[0];
							if(location.containsKey(triplets[1]))
							{
								loc.addField(location.get(triplets[1]), triplets[2]);
								System.out.println("Fields-"+triplets[1]+"~"+triplets[2]);
							}
							else
							{
								loc.addField("WikiMisc", triplets[1] + "~" + triplets[2]);
								System.out.println("Misc-"+triplets[1]+":"+triplets[2]);
							}
						}
					}
					else
					{
						if(location.containsKey(triplets[1]))
						{
							loc.addField(location.get(triplets[1]), triplets[2]);
							System.out.println("Fields-"+triplets[1]+":"+triplets[2]);
						}
						else
						{
							loc.addField("WikiMisc", triplets[1] + "~" + triplets[2]);
							System.out.println("Misc-"+triplets[1]+":"+triplets[2]);
						}

					}

				}
				if(core.equals("organization"))
				{


					if(! prev.equals(triplets[0])){
						String key = (String) org.getFieldValue("WikiName");

						if(key != null)
						{
							try{
								OrganizationServer.add(org);
								org = new SolrInputDocument();
								Ocount++;
							}catch (SolrServerException e)
							{
								e.printStackTrace();
								System.out.println("Error Adding Document");
							}
						}
						else{
							org.addField("WikiName", triplets[0]);
							System.out.println("organization: "+org.getFieldValue("WikiName"));
							prev = triplets[0];
							if(organization.containsKey(triplets[1]))
							{
								org.addField(organization.get(triplets[1]), triplets[2]);
								System.out.println("Fields-"+triplets[1]+"~"+triplets[2]);
							}
							else
							{
								org.addField("WikiMisc", triplets[1] + "~" + triplets[2]);
								System.out.println("Misc-"+triplets[1]+":"+triplets[2]);
							}
						}
					}
					else
					{
						if(organization.containsKey(triplets[1]))
						{
							org.addField(organization.get(triplets[1]), triplets[2]);
							System.out.println("Fields-"+triplets[1]+":"+triplets[2]);
						}
						else
						{
							org.addField("WikiMisc", triplets[1] + "~" + triplets[2]);
							System.out.println("Misc-"+triplets[1]+":"+triplets[2]);
						}

					}

				
				}
				if(core.equals("misc"))
				{

					if(! prev.equals(triplets[0])){
						String key = (String) msc.getFieldValue("WikiName");
						
						if(key != null)
						{
							try{
								MiscServer.add(msc);
								msc = new SolrInputDocument();
								Mcount++;
							}catch (SolrServerException e)
							{
								e.printStackTrace();
								System.out.println("Error Adding Document");
							}
						}
						else{
							msc.addField("WikiName", triplets[0]);
							System.out.println("Misc: "+msc.getFieldValue("WikiName"));
							prev = triplets[0];
							msc.addField("WikiMisc", triplets[1] + "~" + triplets[2]);
							System.out.println("Misc-"+triplets[1]+"~"+triplets[2]);
						}
					}
					else
					{
						msc.addField("WikiMisc", triplets[1] + "~" + triplets[2]);
						System.out.println("Misc-"+triplets[1]+"~"+triplets[2]);
					}
				
				}
			}
			if(Pcount%500==0)
			{
				try{
					PersonServer.commit();
					System.out.println("100 values committed in person Core Count= "+ Pcount);
					//System.out.println(Pcount);
				}
				catch(SolrServerException e)
				{
					e.printStackTrace();
					System.out.println("Unable to commit Person core");
				}
			}
			if(Lcount%300==0)
			{

				try{
					LocationServer.commit();
					System.out.println("10 values committed in Location core Count= "+Lcount);
					
				}
				catch(SolrServerException e)
				{
					e.printStackTrace();
					System.out.println("Unable to commit Location core");
				}

			}
			if(Ocount%200==0)
			{


				try{
					OrganizationServer.commit();
					System.out.println("10 values committed in Organization core Count= "+Ocount);
					
				}
				catch(SolrServerException e)
				{
					e.printStackTrace();
					System.out.println("Unable to commit Organization core");
				}

			
			}
			if(Mcount%1000 == 0)
			{



				try{
					MiscServer.commit();
					System.out.println("1000 values committed in Misc core Count= "+Mcount);
					
				}
				catch(SolrServerException e)
				{
					e.printStackTrace();
					System.out.println("Unable to commit Misc core");
				}

			
			
			}

		}
	}





	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WriteIndex wi = new WriteIndex();
		try {
			wi.Write();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
