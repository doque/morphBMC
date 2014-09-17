package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity
public class Compatibility extends Model {

	@Id
	public long id;

	@ManyToOne
	public Attribute att1;

	@ManyToOne
	public Attribute att2;

	public String userId;

	@ManyToOne
	public Rating rating;

	public static Finder<Long, Compatibility> find = new Finder<Long, Compatibility>(
			Long.class, Compatibility.class);

	@Override
	public String toString() {
		return "ID :" + id + ", Rating: " + rating.value;
	}

}
