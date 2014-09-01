package models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
@Table(name = "compatibilities")
public class Compatibility extends Model {

	@Id
	@GeneratedValue
	long id;

	@OneToMany(cascade = CascadeType.ALL)
	Set<Attribute> attributes;

	String userId;
	String providerId;

	@Required
	float rating;

	public static Finder<Long, Compatibility> find = new Finder<Long, Compatibility>(
			Long.class, Compatibility.class);

}
