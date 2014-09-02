package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.ebean.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name = "parameters")
public class Parameter extends Model {

	@Id
	public long id;

	public String name;

	public String userId;

	@ManyToOne
	@JsonIgnore
	public Problem problem;

	@OneToMany
	public List<Attribute> attributes;

	public static Finder<Long, Parameter> find = new Finder<Long, Parameter>(
			Long.class, Parameter.class);

}
