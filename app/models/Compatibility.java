package models;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@SuppressWarnings("serial")
@Entity
public class Compatibility extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@ManyToMany
	public Set<Attribute> attributes;

	public String userId;

	@Required
	public float rating;

	public static Finder<Long, Compatibility> find = new Finder<Long, Compatibility>(
			Long.class, Compatibility.class);

}
