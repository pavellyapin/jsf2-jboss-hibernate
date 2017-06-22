package com.myobjects.bean;


import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import com.session.bean.UserContext;
import com.myobjects.ejb.UserServiceBean;
import com.myobjects.model.FriendUsers;
import com.myobjects.model.RegisteredUser;
import com.myobjects.model.ReminderItem;
import com.myobjects.model.ScheduleItem;
import com.myobjects.producer.UserProducer;

@Model
public class UserManager {
  
	@Inject
    UserContext userSession;
	@Inject
	UserServiceBean ejb;	
	@Inject
	UserProducer producer;	
	@Produces
	@Named
	RegisteredUser newUser;	 
	
	@Produces
	@Named
	RegisteredUser userLogin;
	
	@PostConstruct
	public void initNewUser() {
		newUser = new RegisteredUser();
		userLogin = new RegisteredUser();
	}

	public RegisteredUser findUser()
	{
		RegisteredUser user = null;
		
		if (userLogin.getUsername().length()> 1 || userLogin.getPassword().length() < 1)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter UserName and Password", ""));
			user = null;
		}
		else
		{
		 user = producer.findUser(userLogin.getUsername());
			
		}
		
		user = producer.findUser(userLogin.getUsername());
		return user;
	}
	
	public RegisteredUser login()
	{
		RegisteredUser user = producer.findUser(userLogin.getUsername());
		
		if (user == null || !user.getPassword().equals(userLogin.getPassword()))
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong UserName or Password", ""));
			user = null;
		}

		return user;	
	}
	public void registerUser(){	
		newUser.setUsername(newUser.getUsername().toLowerCase());
		ejb.register(newUser);
	    producer.setUser(newUser.getUsername());		 
		userSession.setRegisteredUser(producer.getUser());
		ejb.setDefaultSocks();
		ejb.setDefaultTasks();
	}
	
	public void setUserSession(RegisteredUser user) {
		userSession.setRegisteredUser(user);
	}

	public void setUserFriends(List<FriendUsers> friends) {
		ejb.addUserFriends(friends , userSession.getRegisteredUser());
	}
	
	public List<FriendUsers> getUserFriends() {
	 List<FriendUsers> friends = ejb.getUserFriends();
	 return friends;
	}
	
	public void addFriendUser(FriendUsers friend) {
		ejb.addFriendUser(friend);		
	}
	
	public List<ReminderItem> getReminderItemList() {
		 List<ReminderItem> items = ejb.getReminderItems();
		 return items;
		}
	
	public void addReminderItem(ReminderItem reminder) {
		ejb.addReminderItem(reminder);		
	}
	
	public void addScheduleItem(ScheduleItem meeting) {
		ejb.addScheduleItem(meeting);		
	}
		
	public List<ScheduleItem> getScheduleItemList() {
		 List<ScheduleItem> items = ejb.getScheduleItems();
		 return items;
		}
	
 
}
