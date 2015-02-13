package org.sports.ontology.model;

import org.sports.ontology.enums.SentimentEnum;

public class QuoteSentiment {
	private String quote;
	private SentimentEnum sentiment;

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public SentimentEnum getSentiment() {
		return sentiment;
	}

	public void setSentiment(SentimentEnum sentiment) {
		this.sentiment = sentiment;
	}
}
