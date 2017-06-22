package com.myobjects.model;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class ScheduleItem {
 
	
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    
    private String registeredUserid;
    
    private String involvedFriends;
	
	private Date startDate;
	
	private Date endDate;
    
    private boolean reminder;
    
    private String location;
     
    private String type;
     
    private boolean priority;  
    
    private boolean active;
    
    @Column(columnDefinition = "TEXT", length = 65535)
    private String notes;
     
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

	public String getInvolvedFriends() {
		return involvedFriends;
	}

	public void setInvolvedFriends(String involvedFriends) {
		this.involvedFriends = involvedFriends;
	}

	public boolean getReminder() {
		return reminder;
	}

	public void setReminder(boolean reminder) {
		this.reminder = reminder;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean getPriority() {
		return priority;
	}

	public void setPriority(boolean priority) {
		this.priority = priority;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
    

}
