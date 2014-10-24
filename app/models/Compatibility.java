package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity
public class Compatibility extends Model {

	@Id
	public long id;

	@ManyToOne(fetch = FetchType.EAGER)
	public Attribute attr1;

	@ManyToOne(fetch = FetchType.EAGER)
	public Attribute attr2;

	public String userId;

	@ManyToOne(fetch = FetchType.EAGER)
	public Rating rating;

	public static Finder<Long, Compatibility> find = new Finder<Long, Compatibility>(
			Long.class, Compatibility.class);

	@Override
	public String toString() {
		return "ID :" + id + ", Rating: " + rating.value;
	}

}
