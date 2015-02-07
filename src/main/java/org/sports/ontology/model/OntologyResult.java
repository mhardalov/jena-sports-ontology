package org.sports.ontology.model;

import java.util.ArrayList;
import java.util.List;

public class OntologyResult {
	private List<DocumentResults> results;
	private List<DocumentQuotes> quotes;

	public OntologyResult() {
		this.results = new ArrayList<DocumentResults>();
		this.quotes = new ArrayList<DocumentQuotes>();
	}

	public List<DocumentResults> getResults() {
		return results;
	}

	public void setResults(List<DocumentResults> results) {
		this.results = results;
	}
	
	public void addResult(DocumentResults result) {		
		this.results.add(result);
	}

	public List<DocumentQuotes> getQuotes() {
		return quotes;
	}

	public void setQuotes(List<DocumentQuotes> quotes) {
		this.quotes = quotes;
	}
	
	public void addQuote(DocumentQuotes quote) {
		this.quotes.add(quote);
	}
}
