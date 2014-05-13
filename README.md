Team Members: [[Rahul Tejwani|https://github.com/rahultejwani]] and [[Puneet Singh Ludu|https://github.com/puneetsl]]
== About the Project ==
1. We have used following DBpedia parsed data from http://wiki.dbpedia.org/Downloads39:<br>
	a) Raw Infobox Properties<br>
	b) External Links<br>
	c) Short Abstracts<br>
	d) Extended Abstracts<br>
2. We used perl and java to parse nt files, you may find them in cleanData folder.<br>
3. After Cleaning all the files, we calculated static scores(using Word count, external links, size of info box) as mentioned in the report.<br>
4. We will create our primary index using Extended(Long) Abstarcts, with all the static scores combined in one single base file which was merged using Apache Pig(script in "Merge" Folder).<br>
5. Configurations for 3 cores:<br>
	a) LongAbstract<br>
	b) ShortAbstract<br>
	c) InfoBox<br>
	is in the respective folders within the "Cores" folder<br>
6. Our complete working code is in "MainCode" folder<br>



==A few pointers==

1. To run the program "HelloWorld_properties" folder must be put in user's home directory, which consists of mappings, and classifier model for NER parser. <br>
2. It might take some time to clean, calculate scores, merge and index 1.6 million data sets.<br>
3. To install system, simply put the QuestionAnswerSystem folder(which is inside MaiCode folder) in webapps folder and to run open any modern browser and open link :<br>
	http://localhost:8080/QuestionAnswerSystem/QuestionTimeHome.jsp<br>
4. Remember to put lib folder of QuestionAnswerSystem in CLASSPATH.<br>
5. Jar files needed to be put in lib folder(we didn't do it since the size was too large)<br>
commons-cli-1.2.jar<br>
commons-codec-1.7.jar<br>
commons-configuration-1.6.jar<br>
commons-fileupload-1.2.1.jar<br>
commons-io-2.1.jar<br>
commons-lang-2.6.jar<br>
commons-logging-1.1.3.jar<br>
concurrentlinkedhashmap-lru-1.2.jar<br>
dom4j-1.6.1.jar<br>
ejml-0.23.jar<br>
guava-14.0.1.jar<br>
hadoop-annotations-2.0.5-alpha.jar<br>
hadoop-auth-2.0.5-alpha.jar<br>
hadoop-common-2.0.5-alpha.jar<br>
hadoop-hdfs-2.0.5-alpha.jar<br>
httpclient-4.2.3.jar<br>
httpcore-4.2.2.jar<br>
httpmime-4.2.3.jar<br>
joda-time-2.2.jar<br>
jsoup-1.7.3.jar<br>
lucene-analyzers-common-4.5.1.jar<br>
lucene-analyzers-kuromoji-4.5.1.jar<br>
lucene-analyzers-phonetic-4.5.1.jar<br>
lucene-codecs-4.5.1.jar<br>
lucene-core-4.5.1.jar<br>
lucene-grouping-4.5.1.jar<br>
lucene-highlighter-4.5.1.jar<br>
lucene-join-4.5.1.jar<br>
lucene-memory-4.5.1.jar<br>
lucene-misc-4.5.1.jar<br>
lucene-queries-4.5.1.jar<br>
lucene-queryparser-4.5.1.jar<br>
lucene-spatial-4.5.1.jar<br>
lucene-suggest-4.5.1.jar<br>
noggit-0.5.jar<br>
org.restlet-2.1.1.jar<br>
org.restlet.ext.servlet-2.1.1.jar<br>
protobuf-java-2.4.0a.jar<br>
QueryParser.jar<br>
simmetrics_jar_v1_6_2_d07_02_07.jar<br>
slf4j-api-1.7.5.jar<br>
slf4j-simple-1.7.5.jar<br>
solr-core-4.5.1.jar<br>
solr-solrj-4.5.1.jar<br>
spatial4j-0.3.jar<br>
stanford-ner-2013-11-12.jar<br>
stanford-parser-3.3.0-models.jar<br>
stanford-parser.jar<br>
wstx-asl-3.2.7.jar<br>
zookeeper-3.4.5.jar<br>
