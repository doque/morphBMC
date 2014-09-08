package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
public class Attribute extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	public String name;

	@ManyToOne
	@JsonIgnore
	public Parameter parameter;

	public String userId;

	public static Finder<Long, Attribute> find = new Finder<Long, Attribute>(
			Long.class, Attribute.class);

}