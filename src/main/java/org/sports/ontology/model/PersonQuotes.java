package org.sports.ontology.model;

import java.util.ArrayList;
import java.util.List;

public class PersonQuotes {
	private String person;
	private List<String> quotes;
	
	public List<String> getQuotes() {
		return quotes;
	}
	
	public void setQuotes(List<String> quotes) {
		this.quotes = quotes;
	}
	
	public String getPerson() {
		return person;
	}
	
	public void setPerson(String person) {
		this.person = person;
	}
	
	public void addQuote(String quote) {
		this.quotes.add(quote);
	}
	
	public PersonQuotes() {
		this.person = "";
		this.quotes = new ArrayList<String>();
	}
}
