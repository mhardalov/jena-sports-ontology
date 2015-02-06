package org.sports.ontology;

import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.sports.ontology.model.DocumentModel;
import org.sports.ontology.model.OntologyResult;
import org.sports.ontology.model.PersonQuotes;
import org.sports.ontology.model.ResultRelation;

import com.hp.hpl.jena.rdf.model.Resource;

public class OntologyTests {
	
	static String ontologyFile = "/home/momchil/Projects/spring-demo/gate-sports-processor/src/main/resources/gate/sports_terms/ontology/sports.owl";
	
	@Test
	public void ontologyTest() {
		PersonQuotes quotes = new PersonQuotes();
		quotes.setPerson("John Smith");
		quotes.addQuote("I'm the best.");
		quotes.addQuote("Ontology testing with some quotes by me.");
		quotes.addQuote("Third sentsence for today.");
		
		ResultRelation relation = new ResultRelation();
		relation.setResult("7:2");
		relation.getCompetitors().add("Levski");
		relation.getCompetitors().add("CSKA");
		
		DocumentModel document = new DocumentModel();
		document.setContent("Empty");
		document.setUrl("http://somewhere/JohnSmith");
		document.setDate(Calendar.getInstance().getTime());
		document.setKey("http://somewhere/JohnSmith");
		
		OntologyHandler handler = new OntologyHandler();
		Resource resource = handler.registerDocument(document);
		handler.addPersonQuote(quotes, resource);
		handler.addResultRelation(relation, resource);
		
		quotes = new PersonQuotes();
		quotes.setPerson("Tom Johnes");		
		quotes.addQuote("Second Quote by me.");
		quotes.addQuote("Come on do it!");
		
		relation = new ResultRelation();
		relation.setResult("10s");
		relation.getCompetitors().add("Bolt");	
		document = new DocumentModel();
		document.setContent("Empty");
		document.setUrl("http://somewhere/TomJohnes");
		document.setDate(Calendar.getInstance().getTime());
		document.setKey("http://somewhere/TomJohnes");
		
		resource = handler.registerDocument(document);
		handler.addPersonQuote(quotes, resource);
		handler.addResultRelation(relation, resource);
		
		handler.print();		
	}
	
	@Test
	public void ontologyQueryTest() {
		OntologyHandler handler = new OntologyHandler();
		handler.open(ontologyFile);
		//bg.sportal.www:http/news.php?news=342208
		//http://www.sportal.bg/news.php?news=342208
		OntologyResult result = handler.query("http://www.sportal.bg/news.php?news=344152");
		
		Assert.assertEquals(result.getQuotes().size(), 1);
	}
	
	@Test
	public void ontologyQueryByPerson() {
		OntologyHandler handler = new OntologyHandler();
		handler.open(ontologyFile);
		//bg.sportal.www:http/news.php?news=342208
		//http://www.sportal.bg/news.php?news=342208
		List<PersonQuotes> quotes = handler.getQuotes("Стефан Киков", null, null);
		Assert.assertEquals(quotes.size(), 1);
		
	}
}
