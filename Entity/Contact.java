package com.smart.contact.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONTACT")
public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
private int cid;

private String name;

private String lastName;
private String phone;
private String work;

private String email;
    
private String image;
@Column(length = 1000)
private String description;

@ManyToOne
@JsonIgnore
private User user;
public Contact() {
	super();
	// TODO Auto-generated constructor stub
}

public Contact(int cid, String name, String lastName, String phone, String work, String email, String image,
		String description, User user) {
	super();
	this.cid = cid;
	this.name = name;
	this.lastName = lastName;
	this.phone = phone;
	this.work = work;
	this.email = email;
	this.image = image;
	this.description = description;
	this.user = user;
}

public int getCid() {
	return cid;
}
public void setCid(int cid) {
	this.cid = cid;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getLastName() {
	return lastName;
}
public void setLastName(String lastName) {
	this.lastName = lastName;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public String getWork() {
	return work;
}
public void setWork(String work) {
	this.work = work;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getImage() {
	return image;
}
public void setImage(String image) {
	this.image = image;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public User getUser() {
	return user;
}
public void setUser(User user) {
	this.user = user;
}


}
