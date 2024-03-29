package models;

import play.db.ebean.Model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
public class Parameter extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	public String name;

	public String userId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnore
	public Problem problem;

	@OneToMany(cascade = CascadeType.ALL)
	@OrderBy("parameter DESC")
	public List<Attribute> attributes = new ArrayList<Attribute>();
	
	public Long created;

	public static Finder<Long, Parameter> find = new Finder<Long, Parameter>(
			Long.class, Parameter.class);

}
