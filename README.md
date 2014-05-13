#########################################################
# _______  _______  _______  ______   _______  _______  #
#(  ____ )(  ____ \(  ___  )(  __  \ (       )(  ____ \ #     
#| (    )|| (    \/| (   ) || (  \  )| () () || (    \/ #
#| (____)|| (__    | (___) || |   ) || || || || (__     #
#|     __)|  __)   |  ___  || |   | || |(_)| ||  __)    #
#| (\ (   | (      | (   ) || |   ) || |   | || (       #
#| ) \ \__| (____/\| )   ( || (__/  )| )   ( || (____/\ #
#|/   \__/(_______/|/     \|(______/ |/     \|(_______/ #
#########################################################
|----------------------------------------------------------------------------------|                                                                |
|Team Members: [[Rahul Tejwani|https://github.com/rahultejwani]] and [[Puneet Singh Ludu|https://github.com/puneetsl]]   |
|----------------------------------------------------------------------------------|

1. We have used following DBpedia parsed data from http://wiki.dbpedia.org/Downloads39:
	a) Raw Infobox Properties
	b) External Links
	c) Short Abstracts
	d) Extended Abstracts
2. We used perl and java to parse nt files, you may find them in cleanData folder.
3. After Cleaning all the files, we calculated static scores(using Word count, external links, size of info box) as mentioned in the report.
4. We will create our primary index using Extended(Long) Abstarcts, with all the static scores combined in one single base file which was merged using Apache Pig(script in "Merge" Folder).
5. Configurations for 3 cores:
	a) LongAbstract
	b) ShortAbstract
	c) InfoBox
	is in the respective folders within the "Cores" folder
6. Our complete working code is in "MainCode" folder

=====================================================================================

A few pointers:
=====================================================================================
1. To run the program "HelloWorld_properties" folder must be put in user's home directory, which consists of mappings, and classifier model for NER parser. 
2. It might take some time to clean, calculate scores, merge and index 1.6 million data sets.
3. To install system, simply put the QuestionAnswerSystem folder(which is inside MaiCode folder) in webapps folder and to run open any modern browser and open link :
	http://localhost:8080/QuestionAnswerSystem/QuestionTimeHome.jsp
4. Remember to put lib folder of QuestionAnswerSystem in CLASSPATH.
5. Jar files needed to be put in lib folder(we didn't do it since the size was too large)
commons-cli-1.2.jar
commons-codec-1.7.jar
commons-configuration-1.6.jar
commons-fileupload-1.2.1.jar
commons-io-2.1.jar
commons-lang-2.6.jar
commons-logging-1.1.3.jar
concurrentlinkedhashmap-lru-1.2.jar
dom4j-1.6.1.jar
ejml-0.23.jar
guava-14.0.1.jar
hadoop-annotations-2.0.5-alpha.jar
hadoop-auth-2.0.5-alpha.jar
hadoop-common-2.0.5-alpha.jar
hadoop-hdfs-2.0.5-alpha.jar
httpclient-4.2.3.jar
httpcore-4.2.2.jar
httpmime-4.2.3.jar
joda-time-2.2.jar
jsoup-1.7.3.jar
lucene-analyzers-common-4.5.1.jar
lucene-analyzers-kuromoji-4.5.1.jar
lucene-analyzers-phonetic-4.5.1.jar
lucene-codecs-4.5.1.jar
lucene-core-4.5.1.jar
lucene-grouping-4.5.1.jar
lucene-highlighter-4.5.1.jar
lucene-join-4.5.1.jar
lucene-memory-4.5.1.jar
lucene-misc-4.5.1.jar
lucene-queries-4.5.1.jar
lucene-queryparser-4.5.1.jar
lucene-spatial-4.5.1.jar
lucene-suggest-4.5.1.jar
noggit-0.5.jar
org.restlet-2.1.1.jar
org.restlet.ext.servlet-2.1.1.jar
protobuf-java-2.4.0a.jar
QueryParser.jar
simmetrics_jar_v1_6_2_d07_02_07.jar
slf4j-api-1.7.5.jar
slf4j-simple-1.7.5.jar
solr-core-4.5.1.jar
solr-solrj-4.5.1.jar
spatial4j-0.3.jar
stanford-ner-2013-11-12.jar
stanford-parser-3.3.0-models.jar
stanford-parser.jar
wstx-asl-3.2.7.jar
zookeeper-3.4.5.jar
