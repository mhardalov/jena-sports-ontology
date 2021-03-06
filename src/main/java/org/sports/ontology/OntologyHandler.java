package org.sports.ontology;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.sports.ontology.enums.SentimentEnum;
import org.sports.ontology.model.DocumentModel;
import org.sports.ontology.model.DocumentQuotes;
import org.sports.ontology.model.DocumentResults;
import org.sports.ontology.model.OntologyResult;
import org.sports.ontology.model.PersonQuotes;
import org.sports.ontology.model.QuoteSentiment;
import org.sports.ontology.model.ResultRelation;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

public class OntologyHandler {
	// create an empty Model
	private final Model model;

	public OntologyHandler() {
		this.model = ModelFactory.createDefaultModel();
		model.setNsPrefix("sports", SportsOntology.getURI());
	}

	private Date decodeDate(String dateStr) throws ParseException {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
		df.setTimeZone(tz);
		Date date = df.parse(dateStr);

		return date;
	}
	
	private DocumentModel parseDocumentInfo(Statement statment) {
		
		String url = statment.getSubject().getProperty(SportsOntology.DOCUMENT).getString();
		String startDate = statment.getSubject().getProperty(SportsOntology.DATE)
				.getString();

		Date date;
		try {
			date = decodeDate(startDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			date = null;
		}
		
		DocumentModel document = new DocumentModel();
		document.setKey(url);
		document.setUrl(url);
		document.setDate(date);
		
		return document;
	}

	private boolean isQuoteAcceptable(final String docURI,
			final String personName, final Date afterDate,
			final Date beforeDate, Statement s) {

		Statement docUrl = s.getSubject().getProperty(SportsOntology.DOCUMENT);

		// ignore docUrl param if empty
		boolean found = (docUrl == null) || (docURI == "") || (docURI == null)
				|| docUrl.getString().equalsIgnoreCase(docURI);

		if (found && personName != null && personName != "") {
			String ontoPerson = s.getProperty(SportsOntology.PERSONNAME)
					.getString();
			found = found && ontoPerson.equalsIgnoreCase(personName);
		}

		if (found && (afterDate != null || beforeDate != null)) {
			String startDate = s.getSubject().getProperty(SportsOntology.DATE)
					.getString();

			Date date;
			try {
				date = decodeDate(startDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

			if (afterDate != null) {
				found = found
						&& (afterDate != null && date.compareTo(afterDate) >= 0);
			}

			if (beforeDate != null) {
				found = found
						&& (beforeDate != null && date.compareTo(beforeDate) <= 0);
			}
		}

		return found;
	}

	private boolean isResultAcceptable(final String docURI,
			final String competitor, final Date afterDate,
			final Date beforeDate, Statement s) {

		Statement docUrl = s.getSubject().getProperty(SportsOntology.DOCUMENT);

		// ignore docUrl param if empty
		boolean found = (docUrl == null) || (docURI == "") || (docURI == null)
				|| docUrl.getString().equalsIgnoreCase(docURI);

		if (found && competitor != null && competitor != "") {
			StmtIterator iter2 = s.getResource().listProperties(
					SportsOntology.COMPETITORS);

			boolean teamFound = false;

			if (iter2.hasNext()) {
				while (iter2.hasNext() && !teamFound) {
					Statement stmnCompetitor = iter2.nextStatement();

					teamFound = stmnCompetitor
							.getProperty(SportsOntology.COMPETITOR)
							.getProperty(SportsOntology.COMPETITOR_NAME)
							.getString().equalsIgnoreCase(competitor);
				}
			}

			found = found && teamFound;
		}

		if (found && (afterDate != null || beforeDate != null)) {
			String startDate = s.getSubject().getProperty(SportsOntology.DATE)
					.getString();

			Date date;
			try {
				date = decodeDate(startDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

			if (afterDate != null) {
				found = found
						&& (afterDate != null && date.compareTo(afterDate) >= 0);
			}

			if (beforeDate != null) {
				found = found
						&& (beforeDate != null && date.compareTo(beforeDate) <= 0);
			}
		}

		return found;
	}

	public void open(String inputFileName) {
		// use the FileManager to find the input file
		InputStream in = FileManager.get().open(inputFileName);
		if (in != null) {
			// throw new IllegalArgumentException("File: " + inputFileName
			// + " not found");

			// read the RDF/XML file
			model.read(in, null);

			// write it to standard out
			// model.write(System.out);
		}
	}

	public void addPersonQuote(PersonQuotes quotes, Resource document) {
		String person = quotes.getPerson();

		for (QuoteSentiment quote : quotes.getQuotes()) {

			document.addProperty(
					SportsOntology.QUOTE,
					model.createResource()
							.addProperty(SportsOntology.QUOTEDTEXT, quote.getQuote())
							.addProperty(SportsOntology.SENTIMENT, quote.getSentiment().toString())
							.addProperty(SportsOntology.PERSONNAME, person));

		}
	}

	public void addResultRelation(ResultRelation resultRelation,
			Resource document) {
		List<String> competitors = resultRelation.getCompetitors();

		Resource eventResource = model
				.createResource()
				.addProperty(SportsOntology.RESULT, resultRelation.getResult())
				.addProperty(SportsOntology.RESULT_TYPE,
						resultRelation.getType().toString());

		String location = resultRelation.getLocation();
		eventResource.addProperty(SportsOntology.LOCATION, location);

		for (int i = 0; i < competitors.size(); i++) {
			String competitor = competitors.get(i);

			eventResource.addProperty(
					SportsOntology.COMPETITORS,
					model.createResource().addProperty(
							SportsOntology.COMPETITOR,
							model.createResource()
									.addProperty(
											SportsOntology.COMPETITOR_NAME,
											competitor)
									.addProperty(
											SportsOntology.COMPETITOR_ORDER,
											Integer.toString(i))));
		}

		document.addProperty(SportsOntology.EVENT, eventResource);
	}

	public Resource registerDocument(DocumentModel docModel) {
		Resource document = model
				.createResource(docModel.getUrl())
				.addProperty(SportsOntology.DOCUMENT, docModel.getKey())
				.addProperty(SportsOntology.DATE, docModel.getDate().toString());

		return document;
	}

	private List<DocumentQuotes> getQuotes(final String docURI,
			final String personName, final Date afterDate, final Date beforeDate) {
		List<DocumentQuotes> result = new ArrayList<DocumentQuotes>();		

		StmtIterator iter = model.listStatements(new SimpleSelector(null,
				SportsOntology.QUOTE, (RDFNode) null) {
			@Override
			public boolean selects(Statement s) {

				return isQuoteAcceptable(docURI, personName, afterDate,
						beforeDate, s);
			}
		});

		if (iter.hasNext()) {
			while (iter.hasNext()) {
				Statement stmn = iter.nextStatement();

				String quote = stmn.getProperty(SportsOntology.QUOTEDTEXT)
						.getString();

				SentimentEnum sentiment = SentimentEnum.findByText(stmn.getProperty(SportsOntology.SENTIMENT)
						.getString());
				
				String person = stmn.getProperty(SportsOntology.PERSONNAME)
						.getString();

				PersonQuotes personQuote = new PersonQuotes();
				personQuote.addQuote(quote, sentiment);
				personQuote.setPerson(person);				
				
				DocumentModel document = this.parseDocumentInfo(stmn);
				DocumentQuotes docQuotes = new DocumentQuotes(document, personQuote);
				result.add(docQuotes);
			}
		}		

		return result;
	}

	private List<DocumentResults> getResults(final String docURI,
			final String competitor, final Date afterDate, final Date beforeDate) {
		List<DocumentResults> result = new ArrayList<DocumentResults>();		

		StmtIterator iter = model.listStatements(new SimpleSelector(null,
				SportsOntology.EVENT, (RDFNode) null) {
			@Override
			public boolean selects(Statement s) {

				return isResultAcceptable(docURI, competitor, afterDate,
						beforeDate, s);
			}
		});

		if (iter.hasNext()) {
			while (iter.hasNext()) {
				Statement stmn = iter.nextStatement();

				String type = stmn.getProperty(SportsOntology.RESULT_TYPE)
						.getString();
				ResultRelation resultRelation = new ResultRelation(type);

				resultRelation.setResult(stmn
						.getProperty(SportsOntology.RESULT).getString());

				resultRelation.setLocation(stmn.getProperty(
						SportsOntology.LOCATION).getString());

				StmtIterator iter2 = stmn.getResource().listProperties(
						SportsOntology.COMPETITORS);

				List<String> comps = new ArrayList<String>();
				if (iter2.hasNext()) {
					while (iter2.hasNext()) {
						Statement stmnCompetitor = iter2.nextStatement();

						comps.add(stmnCompetitor
								.getProperty(SportsOntology.COMPETITOR)
								.getProperty(SportsOntology.COMPETITOR_NAME)
								.getString());
					}
				}

				resultRelation.setCompetitors(comps);				
				
				DocumentModel document = this.parseDocumentInfo(stmn);
				DocumentResults docResults = new DocumentResults(document, resultRelation);
				result.add(docResults);
			}
		}

		return result;
	}

	public OntologyResult query(final String docURI) {
		OntologyResult result = new OntologyResult();

		result.setQuotes(this.getQuotes(docURI, "", null, null));
		result.setResults(this.getResults(docURI, "", null, null));

		return result;
	}

	public List<DocumentQuotes> queryQuotes(final String personName,
			final Date afterDate, final Date beforeDate) {

		return this.getQuotes("", personName, afterDate, beforeDate);
	}

	public List<DocumentResults> queryResults(final String competitor,
			final Date afterDate, final Date beforeDate) {

		return this.getResults("", competitor, afterDate, beforeDate);
	}

	public void print() {
		model.write(System.out);
	}

	public void save(String outputFileName) throws FileNotFoundException {
		OutputStream out = new FileOutputStream(outputFileName);
		model.write(out);
	}
}
