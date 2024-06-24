package com.smart.contact.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "USER")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
private int id;


@NotBlank(message = "name feild is mandatory")
@Size(min = 5,max = 20,message = "min 5 and max 20 character required")
    private String name;

@NotBlank(message = "email field should be filled up correctly")
@Column(unique = true)
    private String email;

    private String password;

    private String imageUrl;
    
    private String role;
    
    private boolean enabled;
    
@Column(length = 500)
    private String about;

@OneToMany(cascade = CascadeType.ALL,mappedBy = "user",fetch = FetchType.LAZY)
private List<Contact> contacts=new ArrayList<>();
public User() {
	super();
	// TODO Auto-generated constructor stub
}

public User(int id, String name, String email, String password, String imageUrl, String role, boolean enabled,
		String about, List<Contact> contacts) {
	super();
	this.id = id;
	this.name = name;
	this.email = email;
	this.password = password;
	this.imageUrl = imageUrl;
	this.role = role;
	this.enabled = enabled;
	this.about = about;
	this.contacts = contacts;
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
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getImageUrl() {
	return imageUrl;
}
public void setImageUrl(String imageUrl) {
	this.imageUrl = imageUrl;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
	this.role = role;
}
public boolean isEnabled() {
	return enabled;
}
public void setEnabled(boolean enabled) {
	this.enabled = enabled;
}
public String getAbout() {
	return about;
}
public void setAbout(String about) {
	this.about = about;
}
public List<Contact> getContacts() {
	return contacts;
}
public void setContacts(List<Contact> contacts) {
	this.contacts = contacts;
}

@Override
public String toString() {
	return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", imageUrl="
			+ imageUrl + ", role=" + role + ", enabled=" + enabled + ", about=" + about + ", contacts=" + contacts
			+ "]";
}


}
