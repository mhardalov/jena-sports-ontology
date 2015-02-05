package org.sports.ontology.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ResultRelation {

	private String result;
	private List<String> competitors;

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
	
	public ResultRelation() {
		this.result = "";
		this.competitors = new ArrayList<String>();
	}
	
	@Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            // if deriving: appendSuper(super.hashCode()).
            append(result).
            append(competitors).
            toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
       if (!(obj instanceof ResultRelation))
            return false;
        if (obj == this)
            return true;

        ResultRelation rhs = (ResultRelation) obj;
        return new EqualsBuilder().
            // if deriving: appendSuper(super.equals(obj)).
            append(result, rhs.result).
            append(competitors, rhs.competitors).
            isEquals();
    }
}
