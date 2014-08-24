package models;

public class Attribute {

	public long id;
	public String value;

	public Attribute(String value) {
		this.setValue(value);
	}

	public Attribute setId(long id) {
		this.id = id;
		return this;
	}

	public Attribute setValue(String value) {
		this.value = value;
		return this;
	}

}