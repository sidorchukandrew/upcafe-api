package com.sidorchuk.upcafe.resource.entity.signin;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Staff {

	@Id
	@GeneratedValue
	private int id;
	private String username;
	private String password;
	private boolean isAdmin;

	public Staff() {
	}
	
	public Staff(String username, String password, boolean isAdmin) {
		super();
		this.username = username;
		this.password = password;
		this.isAdmin = isAdmin;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public String toString() {
		return "Worker [id=" + id + ", username=" + username + ", password=" + password + ", isAdmin=" + isAdmin + "]";
	}

}
