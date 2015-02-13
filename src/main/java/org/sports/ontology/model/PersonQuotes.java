package org.sports.ontology.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sports.ontology.enums.SentimentEnum;

public class PersonQuotes {
	private String person;
	private Map<String, SentimentEnum> quotes;

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

	public List<QuoteSentiment> getQuotes() {
		List<QuoteSentiment> result = new ArrayList<QuoteSentiment>();
		for (Entry<String, SentimentEnum> entry : this.quotes.entrySet()) {
			QuoteSentiment quote = new QuoteSentiment();
			quote.setQuote(entry.getKey());
			quote.setSentiment(entry.getValue());
			result.add(quote);
		}
		
		return result;
	}

	public PersonQuotes() {
		this.person = "";
		this.quotes = new HashMap<String, SentimentEnum>();
	}
}
