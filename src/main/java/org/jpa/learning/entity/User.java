package org.jpa.learning.entity;

import static org.apache.commons.lang.WordUtils.capitalize;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name="T_USUARIO")
public class User {

	private Long id;
	private String username;
	private String password;
	private String name;
	private String lastName;

	@Id
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = capitalize(name);
	}

	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = capitalize(lastName);
	}

	@Transient
	public String getfullName()
	{
		return String.format("%s %s",getName(),getLastName());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(null)
				.append(id)
				.append(getfullName())
				.toString();
	}
}

