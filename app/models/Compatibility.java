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
	
	public String overrideComment = "";
	
	@Override
	public String toString() {
		return "ID :" + id + ", Rating: " + rating.value + ", Attr1: "
				+ attr1.toString() + ", Attr2: " + attr2.toString();
	}
	
	/**
	 * compares two compatibilities to see if the pair of attributes is equal
	 * @param c the other compatibility
	 * @return
	 */
	public boolean sameAttributes(Compatibility c) {
		return (c.attr1.id == attr1.id && c.attr2.id == attr2.id)
				|| (c.attr1.id == attr2.id && c.attr2.id == attr1.id);
	}

}
