package com.myobjects.model;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostRemove;

@Entity
public class FriendUsers {
 
	
    @Id
    @GeneratedValue
    private Long id;
    
    private String registeredUserid;
	
	private String firstname;
    
    private String lastname;
    
    private String url; 
    
    private String location;
    
    private String email;
     
    private String phone;
    
    private String img;
    
    @Override
    public String toString() {
        return lastname + "," + firstname;
    }
 
    public String getFirstname() {
        return firstname;
    }
 
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
 
    public String getLastname() {
        return lastname;
    }
 
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
 
       
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public String getPhone() {
        return phone;
    }
 
    public void setPhone(String phone) {
        this.phone = phone;
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRegisteredUser() {
		return registeredUserid;
	}

	public void setRegisteredUser(String registeredUser) {
		this.registeredUserid = registeredUser;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
    

}
