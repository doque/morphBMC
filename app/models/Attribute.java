package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.ebean.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name = "attributes")
public class Attribute extends Model {

	@Id
	public long id;

	public String name;

	@ManyToOne
	@JsonIgnore
	public Parameter parameter;

	public String userId;

	public static Finder<Long, Attribute> find = new Finder<Long, Attribute>(
			Long.class, Attribute.class);

}