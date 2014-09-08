package models;

import play.db.ebean.Model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@SuppressWarnings("serial")
@Entity
public class Problem extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	public String userId;

	public String name;

	@OneToMany(cascade = CascadeType.ALL)
	public List<Parameter> parameters;

	public static Finder<Long, Problem> find = new Finder<Long, Problem>(
			Long.class, Problem.class);

	public Stage currentStage;

	public enum Stage {
		Definition, Compatibility
	}
}