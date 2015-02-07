package org.sports.ontology.model;


public class DocumentQuotes {
	private DocumentModel document;
	private PersonQuotes quotes;

	public DocumentQuotes(DocumentModel document, PersonQuotes quotes) {
		this.setDocument(document);
		this.setQuotes(quotes);
	}

	public DocumentModel getDocument() {
		return document;
	}

	public void setDocument(DocumentModel document) {
		this.document = document;
	}

	public PersonQuotes getQuotes() {
		return quotes;
	}

	public void setQuotes(PersonQuotes quotes) {
		this.quotes = quotes;
	}
}
