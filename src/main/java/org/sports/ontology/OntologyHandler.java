package org.sports.ontology;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.sports.ontology.model.DocumentModel;
import org.sports.ontology.model.OntologyResult;
import org.sports.ontology.model.PersonQuotes;
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
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(tz);
		Date date = df.parse(dateStr);

		return date;
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

		for (String quote : quotes.getQuotes()) {

			document.addProperty(
					SportsOntology.QUOTE,
					model.createResource()
							.addProperty(SportsOntology.QUOTEDTEXT, quote)
							.addProperty(SportsOntology.PERSONNAME, person));

		}
	}

	public void addResultRelation(ResultRelation resultRelation,
			Resource document) {
		List<String> competitors = resultRelation.getCompetitors();

		document.addProperty(SportsOntology.EVENT, model.createResource()
				.addProperty(SportsOntology.RESULT, resultRelation.getResult()));

		for (String competitor : competitors) {
			document.addProperty(SportsOntology.COMPETITOR, competitor);
		}
	}

	public Resource registerDocument(DocumentModel docModel) {
		Resource document = model
				.createResource(docModel.getUrl())
				.addProperty(SportsOntology.DOCUMENT, docModel.getKey())
				.addProperty(SportsOntology.DATE, docModel.getDate().toString());

		return document;
	}

	public List<PersonQuotes> getQuotes(final String personName,
			final Date afterDate, final Date beforeDate) {

		List<PersonQuotes> result = new ArrayList<PersonQuotes>();

		StmtIterator iter = model.listStatements(new SimpleSelector(null,
				SportsOntology.QUOTE, (RDFNode) null) {
			@Override
			public boolean selects(Statement s) {
				boolean found = true;

				if (found && personName != "") {
					String ontoPerson = s
							.getProperty(SportsOntology.PERSONNAME).getString();
					found = found && ontoPerson.equalsIgnoreCase(personName);
				}

				if (found && (afterDate != null || beforeDate != null)) {
					String startDate = s.getSubject()
							.getProperty(SportsOntology.DATE).getString();

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
								&& (afterDate != null && date
										.compareTo(afterDate) >= 0);
					}

					if (beforeDate != null) {
						found = found
								&& (beforeDate != null && date
										.compareTo(afterDate) <= 0);
					}
				}

				return found;
			}
		});

		if (iter.hasNext()) {
			while (iter.hasNext()) {
				Statement stmn = iter.nextStatement();

				String quote = stmn.getProperty(SportsOntology.QUOTEDTEXT)
						.getString();

				String person = stmn.getProperty(SportsOntology.PERSONNAME)
						.getString();

				PersonQuotes personQuote = new PersonQuotes();
				personQuote.addQuote(quote);
				personQuote.setPerson(person);

				result.add(personQuote);
			}
		}

		return result;
	}

	public OntologyResult query(final String docURI) {
		OntologyResult result = new OntologyResult();

		StmtIterator iter = model.listStatements(new SimpleSelector(null,
				SportsOntology.QUOTE, (RDFNode) null) {
			@Override
			public boolean selects(Statement s) {
				Statement docUrl = s.getSubject()
						.getProperty(SportsOntology.DOCUMENT);
				
				return (docUrl != null) && docUrl.getString().equalsIgnoreCase(docURI);
			}
		});

		if (iter.hasNext()) {
			while (iter.hasNext()) {
				Statement stmn = iter.nextStatement();

				String quote = stmn.getProperty(SportsOntology.QUOTEDTEXT)
						.getString();

				String person = stmn.getProperty(SportsOntology.PERSONNAME)
						.getString();

				PersonQuotes personQuote = new PersonQuotes();
				personQuote.addQuote(quote);
				personQuote.setPerson(person);

				result.addQuote(personQuote);
			}
		}

		return result;
	}

	public void print() {
		model.write(System.out);
	}

	public void save(String outputFileName) throws FileNotFoundException {
		OutputStream out = new FileOutputStream(outputFileName);
		model.write(out);
	}
}
