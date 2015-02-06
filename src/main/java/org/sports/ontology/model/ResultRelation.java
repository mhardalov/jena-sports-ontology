package org.sports.ontology.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.sports.ontology.enums.ResultTypeEnum;

public class ResultRelation {

	private String result;
	private List<String> competitors;
	private String location;
	private ResultTypeEnum type;

	public ResultRelation(String type) {
		this.setType(ResultTypeEnum.findByText(type));
		this.result = "";
		this.competitors = new ArrayList<String>();
		this.location = "";
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<String> getCompetitors() {
		return competitors;
	}

	public void setCompetitors(List<String> competitors) {
		this.competitors = competitors;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
				// if deriving: appendSuper(super.hashCode()).
				append(result).append(competitors).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ResultRelation))
			return false;
		if (obj == this)
			return true;

		ResultRelation rhs = (ResultRelation) obj;
		return new EqualsBuilder()
				.
				// if deriving: appendSuper(super.equals(obj)).
				append(result, rhs.result).append(competitors, rhs.competitors)
				.isEquals();
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public ResultTypeEnum getType() {
		return type;
	}

	public void setType(ResultTypeEnum type) {
		this.type = type;
	}
}
