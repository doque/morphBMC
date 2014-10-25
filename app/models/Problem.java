package models;

import play.db.ebean.Model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@SuppressWarnings("serial")
@Entity
public class Problem extends Model {

	@Id
	public long id;

	public String userId;

	public String owner;

	public String name;

	@OneToMany(cascade = CascadeType.ALL)
	@OrderBy("problem DESC")
	public List<Parameter> parameters = new ArrayList<Parameter>();

	/**
	 * registers the current stage of a project. this will be updated
	 * when the owner clicks one of the navigation links
	 */
	public Stage stage;

	public enum Stage {
		Definition,
		Refinement,
		Compatibility,
		ConflictResolution,
		Exploration,
		Results
	}

	public static Finder<Long, Problem> find = new Finder<Long, Problem>(
			Long.class, Problem.class);
}