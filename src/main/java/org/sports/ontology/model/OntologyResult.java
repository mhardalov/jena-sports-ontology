package org.sports.ontology.model;

import java.util.ArrayList;
import java.util.List;

public class OntologyResult {
	private List<ResultRelation> results;
	private List<PersonQuotes> quotes;

	public OntologyResult() {
		this.results = new ArrayList<ResultRelation>();
		this.quotes = new ArrayList<PersonQuotes>();
	}

	public List<ResultRelation> getResults() {
		return results;
	}

	public void setResults(List<ResultRelation> results) {
		this.results = results;
	}
	
	public void addResult(ResultRelation result) {
		this.results.add(result);
	}

	public List<PersonQuotes> getQuotes() {
		return quotes;
	}

	public void setQuotes(List<PersonQuotes> quotes) {
		this.quotes = quotes;
	}
	
	public void addQuote(PersonQuotes quote) {
		this.quotes.add(quote);
	}
}
