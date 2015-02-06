package org.sports.ontology;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class SportsOntology {
	protected static final String uri = "http://www.sports.org/gate/ontology/1.0#";

	/**
	 * returns the URI for this schema
	 * 
	 * @return the URI for this schema
	 */
	public static String getURI() {
		return uri;
	}

	private static Model m = ModelFactory.createDefaultModel();

	public static final Property DOCUMENT = m.createProperty(uri + "DOCUMENT");	
	public static final Property QUOTE = m.createProperty(uri + "QUOTE");
	public static final Resource PERSON = m.createResource(uri + "PERSON");
	public static final Property COMPETITORS = m.createProperty(uri + "COMPETITORS");
	public static final Property COMPETITOR = m.createProperty(uri + "COMPETITOR");
	public static final Property COMPETITOR_ORDER = m.createProperty(uri + "COMPETITOR_ORDER");
	public static final Property COMPETITOR_NAME = m.createProperty(uri + "COMPETITOR_NAME");
	public static final Property RESULT = m.createProperty(uri, "RESULT");
	public static final Property RESULT_TYPE = m.createProperty(uri, "RESULT_TYPE");
	public static final Property DATE = m.createProperty(uri, "DATE");
	public static final Property URL = m.createProperty(uri, "URL");
	public static final Property LOCATION = m.createProperty(uri, "LOCATION");
	public static final Property EVENT = m.createProperty(uri, "EVENT");
	public static final Property TERM = m.createProperty(uri, "TERM");
	public static final Property SENTIMENT = m.createProperty(uri, "SENTIMENT");
	public static final Property QUOTEDTEXT = m.createProperty(uri + "QUOTEDTEXT");
	public static final Property PERSONNAME = m.createProperty(uri + "PERSONNAME");
}
