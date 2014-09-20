package models;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "RatedCompatibility.findAll", query = "SELECT r.name, r.value, p.name as param1, a.name as att1, p2.name as param2, a2.name as att2 "
		+ "FROM compatibility c JOIN attribute a ON c.att1_id = a.id "
		+ "JOIN attribute a2 ON c.att2_id = a2.id JOIN PARAMETER p ON a.parameter_id = p.id"
		+ " JOIN PARAMETER p2 ON a2.parameter_id = p2.id JOIN rating r ON c.rating_id = r.id")
public class RatedCompatibility {

	public long problemId;

	public String name;

	public String value;

	public String param1;

	public String param2;

	public String att1;

	public String att2;

	// public static Finder<Long, RatedCompatibility> find = new Finder<Long,
	// RatedCompatibility>(
	// Long.class, RatedCompatibility.class);
}
