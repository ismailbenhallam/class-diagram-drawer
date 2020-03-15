package org.mql.test;

public class Employee<T> extends Person {
	private float salary;

	public Employee(int id, String fisrtName, String lastName, float salary, Address address) {
		setId(id);
		setFisrtName(fisrtName);
		setLastName(lastName);
		setSalary(salary);
		setAddress(address);
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}

	public float getSalary() {
		return salary;
	}

}
