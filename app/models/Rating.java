package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@SuppressWarnings("serial")
@Entity
public class Rating extends Model {

	@Id
	public long id;

	public String name;

	public Float value;

	public Rating(String name, Float value) {
		this.name = name;
		this.value = value;
	}

	public static Finder<Long, Rating> find = new Finder<Long, Rating>(
			Long.class, Rating.class);

}
