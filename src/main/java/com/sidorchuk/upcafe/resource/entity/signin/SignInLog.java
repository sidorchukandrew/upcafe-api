package com.sidorchuk.upcafe.resource.entity.signin;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "customer_sign_ins")
public class SignInLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	private Date dateSignedIn;
	private Date dateSignedOut;
	private Time timeSignedIn;
	private Time timeSignedOut;
	
	public SignInLog(Customer customer, Date dateSignedIn, Date dateSignedOut, Time timeSignedIn, Time timeSignedOut) {
		super();
		this.customer = customer;
		this.dateSignedIn = dateSignedIn;
		this.dateSignedOut = dateSignedOut;
		this.timeSignedIn = timeSignedIn;
		this.timeSignedOut = timeSignedOut;
	}
	
	public SignInLog() { }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Date getDateSignedIn() {
		return dateSignedIn;
	}

	public void setDateSignedIn(Date dateSignedIn) {
		this.dateSignedIn = dateSignedIn;
	}

	public Date getDateSignedOut() {
		return dateSignedOut;
	}

	public void setDateSignedOut(Date dateSignedOut) {
		this.dateSignedOut = dateSignedOut;
	}

	public Time getTimeSignedIn() {
		return timeSignedIn;
	}

	public void setTimeSignedIn(Time timeSignedIn) {
		this.timeSignedIn = timeSignedIn;
	}

	public Time getTimeSignedOut() {
		return timeSignedOut;
	}

	public void setTimeSignedOut(Time timeSignedOut) {
		this.timeSignedOut = timeSignedOut;
	}

	@Override
	public String toString() {
		return "SignInLog [id=" + id + ", customer=" + customer + ", dateSignedIn=" + dateSignedIn + ", dateSignedOut="
				+ dateSignedOut + ", timeSignedIn=" + timeSignedIn + ", timeSignedOut=" + timeSignedOut + "]";
	}
	
	
}
