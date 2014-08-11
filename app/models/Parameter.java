package models;

import java.util.ArrayList;

public class Parameter {

	public String name;

	public ArrayList<Attribute> attributes;

	public ArrayList<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(ArrayList<Attribute> attributes) {
		this.attributes = attributes;
	}

	public Parameter addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
		return this;
	}

	public String getName() {
		return name;
	}

	public Parameter setName(String value) {
		this.name = value;
		return this;
	}

	public Parameter() {
		this.attributes = new ArrayList<Attribute>();
	}
}
