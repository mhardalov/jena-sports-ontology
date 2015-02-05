package org.sports.ontology;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

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

	public void open(String inputFileName) {
		// use the FileManager to find the input file
		InputStream in = FileManager.get().open(inputFileName);
		if (in != null) {
//			throw new IllegalArgumentException("File: " + inputFileName
//					+ " not found");

			// read the RDF/XML file
			model.read(in, null);

			// write it to standard out
			model.write(System.out);
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
				.createResource(docModel.getKey())
				.addProperty(SportsOntology.DOCUMENT, docModel.getUrl())
				.addProperty(SportsOntology.DATE, docModel.getDate().toString());

		return document;
	}

	public OntologyResult query(final String docURI) {
		OntologyResult result = new OntologyResult();
		
		StmtIterator iter = model.listStatements(new SimpleSelector(null,
				SportsOntology.QUOTE, (RDFNode) null) {
			@Override
			public boolean selects(Statement s) {
				return s.getSubject().getURI().equalsIgnoreCase(docURI);
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
