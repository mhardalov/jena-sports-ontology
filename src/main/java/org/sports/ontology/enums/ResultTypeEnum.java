package org.sports.ontology.enums;

public enum ResultTypeEnum {
	RACE("location"),
	MATCH("sample");
	
	private final String text;	

	private ResultTypeEnum(final String text) {
		this.text = text;		
	}

	public static ResultTypeEnum findByText(String text) {
		for (ResultTypeEnum v : values()) {
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
