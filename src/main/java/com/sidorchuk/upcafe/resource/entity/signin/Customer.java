package com.sidorchuk.upcafe.resource.entity.signin;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String photoUrl;
	private Date dateAccountCreated;
	
	public Customer(int id, String firstName, String lastName, String email, String photoUrl, Date dateAccountCreated) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.photoUrl = photoUrl;
		this.dateAccountCreated = dateAccountCreated;
	}
	
	public Customer() { }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	
	public void setDateAccountCreated(Date dateAccountCreated) {
		this.dateAccountCreated = dateAccountCreated;
	}
	
	public Date getDateAccountCreated() {
		return dateAccountCreated;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", photoUrl=" + photoUrl + "]";
	}
	
}
