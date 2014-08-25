package models;

public class Attribute {

	public long id;
	public String name;

	public Attribute() {
	}

	public Attribute(String name) {
		this.setName(name);
	}

	public Attribute setId(long id) {
		this.id = id;
		return this;
	}

	public Attribute setName(String name) {
		this.name = name;
		return this;
	}

}