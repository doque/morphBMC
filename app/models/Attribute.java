package models;

import play.db.ebean.Model;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
public class Attribute extends Model {

	@Id
	public long id;
	public String name;

	public String userId;

	@ManyToOne(cascade = CascadeType.DETACH)
	@JsonIgnore
	public Parameter parameter;

	@Version
	private Timestamp version;// = System.currentTimeMillis();
	
	
	@Override
	public String toString() {
		return "[" + id + "/" + name + "]";
	}

	public static Finder<Long, Attribute> find = new Finder<Long, Attribute>(
			Long.class, Attribute.class);

}