package org.mql.test;

import java.util.Vector;

public class Company {
	private int id;
	private String name;
	private Vector<Person> employees;
	private int[] test;

	public Company(int id, String name, Vector<Person> employees) {
		this.id = id;
		this.name = name;
		this.employees = employees;
	}

	public Company() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector<Person> getEmployees() {
		return employees;
	}

	public void setEmployees(Vector<Person> employees) {
		this.employees = employees;
	}

}