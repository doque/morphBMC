package models;

import play.db.ebean.Model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
	
    @Column(columnDefinition = "TEXT")
    public String statement;

	@OneToMany(cascade = CascadeType.ALL)
	@OrderBy("problem DESC")
	public List<Parameter> parameters = new ArrayList<Parameter>();

	/**
	 * registers the current stage of a project. this will be updated
	 * when the owner clicks one of the navigation links
	 */
	public Stage stage;

	public enum Stage {
		STATEMENT,
		DEFINITION,
		REFINEMENT,
		COMPATIBILITY,
		RESOLUTION,
		EXPLORATION,
		RESULTS;
		
		/**
		 * grabs an angular #anchor in lowercase and returns the enum for it
		 * @param v - malformed anchor
		 * @return Stage enum
		 */
		public static Stage getStage(String v) {
			return Stage.valueOf(v.replace(" ", "").toUpperCase());
		}
	}

	public static Finder<Long, Problem> find = new Finder<Long, Problem>(
			Long.class, Problem.class);
}