package org.sports.ontology.model;

import java.util.HashMap;
import java.util.Map;

import org.sports.ontology.enums.SentimentEnum;

public class PersonQuotes {
	private String person;	
	private Map<String, SentimentEnum> quotes;
	
	public Map<String, SentimentEnum> getQuotes() {
		return quotes;
	}
	
	public void setQuotes(Map<String, SentimentEnum> quotes) {
		this.quotes = quotes;
	}
	
	public String getPerson() {
		return person;
	}
	
	public void setPerson(String person) {
		this.person = person;
	}
	
	public void addQuote(String quote, SentimentEnum sentiment) {
		this.quotes.put(quote, sentiment);
	}
	
	public PersonQuotes() {
		this.person = "";
		this.quotes = new HashMap<String, SentimentEnum>();
	}
}
