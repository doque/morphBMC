package models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
@Table(name = "compatibilities")
public class Compatibility extends Model {

	@Id
	public long id;

	@ManyToMany
	public Set<Attribute> attributes;

	public String userId;

	@Required
	public float rating;

	public static Finder<Long, Compatibility> find = new Finder<Long, Compatibility>(
			Long.class, Compatibility.class);

}
