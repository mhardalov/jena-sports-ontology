package org.sports.ontology.model;


public class DocumentResults {
	private DocumentModel document;
	private ResultRelation results;

	public DocumentResults(DocumentModel document, ResultRelation results) {
		this.setDocument(document);
		this.setResults(results);
	}

	public DocumentModel getDocument() {
		return document;
	}

	public void setDocument(DocumentModel document) {
		this.document = document;
	}

	public ResultRelation getResults() {
		return results;
	}

	public void setResults(ResultRelation results) {
		this.results = results;
	}
}
