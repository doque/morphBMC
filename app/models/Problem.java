package models;

import java.util.ArrayList;

public class Problem {

	public ArrayList<Parameter> parameters;

	public ArrayList<Parameter> getParameters() {
		return parameters;
	}

	/*
	 * returns all possible attributes within a problem shortcut to iterating
	 * each parameter
	 * 
	 * used for rendering empty table cells.
	 */
	public ArrayList<Attribute> getAllAttributes() {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		for (Parameter p : getParameters()) {
			for (Attribute attr : p.getAttributes()) {
				attributes.add(attr);
			}
		}
		return attributes;
	}

	public void setParameters(ArrayList<Parameter> parameters) {
		this.parameters = parameters;
	}

	public Problem addParameter(Parameter parameter) {
		this.parameters.add(parameter);
		return this;
	}

	public Problem() {
		this.parameters = new ArrayList<Parameter>();
	}

}
