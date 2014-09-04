package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
@Table(name = "problems")
public class Problem extends Model {

	@Id
	public long id;

	public String userId;

	public String name;

	@OneToMany(cascade = CascadeType.ALL)
	public List<Parameter> parameters;

	public static Finder<Long, Problem> find = new Finder<Long, Problem>(
			Long.class, Problem.class);

}