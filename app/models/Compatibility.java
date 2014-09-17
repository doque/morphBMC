package models;

import play.db.ebean.Model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity
public class Compatibility extends Model {

	@Id
	public long id;

	@ManyToMany
	public List<Attribute> attributes = new ArrayList<Attribute>();

	public String userId;

	@ManyToOne
	public Rating rating;

	public static Finder<Long, Compatibility> find = new Finder<Long, Compatibility>(
			Long.class, Compatibility.class);

	@Override
	public String toString() {
		return String.format("ID %d rated %d (%s) attr [%d, %d]", this.id,
				this.rating.value, this.rating.name, this.attributes.get(0),
				this.attributes.get(1));
	}

}
