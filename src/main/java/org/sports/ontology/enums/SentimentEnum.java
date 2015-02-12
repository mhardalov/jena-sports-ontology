package org.sports.ontology.enums;

public enum SentimentEnum {
	POSITIVE("positive"),
	NEGATIVE("negative"),
	NEUTRAL("neutral");
	
	private final String text;	

	private SentimentEnum(final String text) {
		this.text = text;		
	}

	public static SentimentEnum findByText(String text) {
		for (SentimentEnum v : values()) {
			if (v.getText().contains(text) || text.contains(v.getText())) {
				return v;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return text;
	}

	public String getText() {
		return text;
	}
}
