package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
@Table(name = "attributes")
public class Attribute extends Model {

	@Id
	@GeneratedValue
	long id;

	@Required
	String name;

	@ManyToOne
	Parameter parameter;

	@ManyToOne
	Compatibility compatibility;

	String userId;
	String providerId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static Finder<Long, Attribute> find = new Finder<Long, Attribute>(
			Long.class, Attribute.class);

}