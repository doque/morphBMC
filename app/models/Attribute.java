package models;

public class Attribute {

	public String value;

	public String getValue() {
		return value;
	}

	public Attribute setValue(String value) {
		this.value = value;
		return this;
	}

	public Attribute(String value) {
		this.setValue(value);
	}

}