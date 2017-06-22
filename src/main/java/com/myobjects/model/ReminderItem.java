package com.myobjects.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ReminderItem {
 
	
    @Id
    @GeneratedValue
    private Long id;
    
    private String registeredUserid;    
    private String scheduleItemid;    
    private String remindDate;     
    private String descr;    
    private boolean active;
 
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

	public String getScheduleItemid() {
		return scheduleItemid;
	}

	public void setScheduleItemid(String scheduleItemid) {
		this.scheduleItemid = scheduleItemid;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getRemindDate() {
		return remindDate;
	}

	public void setRemindDate(String remindDate) {
		this.remindDate = remindDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

    
}
