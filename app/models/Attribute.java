package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
public class Attribute extends Model {

	@Id
	public long id;
	public String name;

	public String userId;

	@ManyToOne
	@JsonIgnore
	public Parameter parameter;

	public static Finder<Long, Attribute> find = new Finder<Long, Attribute>(
			Long.class, Attribute.class);

}