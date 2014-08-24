package models;

import java.util.ArrayList;

public class Parameter {

	public long id;
	public String name;
	protected ArrayList<Attribute> attributes;

	public Parameter() {
		this.attributes = new ArrayList<Attribute>();
	}

	public Parameter addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
		return this;
	}

	public ArrayList<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(ArrayList<Attribute> attributes) {
		this.attributes = attributes;
	}

	public Parameter setId(long id) {
		this.id = id;
		return this;
	}

	public Parameter setName(String value) {
		this.name = value;
		return this;
	}
}
