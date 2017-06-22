package com.myobjects.producer;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.myobjects.model.RegisteredUser;
import com.myobjects.repository.UserRepositoryManager;

 
@RequestScoped
public class UserProducer {
	@Inject UserRepositoryManager db;

	private RegisteredUser user;
	 
	
	@Produces
	@Named
	public RegisteredUser getUser() {
		return user;
	}

	public void setUser(String username) {		
		RegisteredUser currentuser = db.getCurrentUser(username);
		this.user = currentuser;
	}
	
	public RegisteredUser findUser(String username)
	{
		RegisteredUser currentuser = db.getCurrentUser(username);
		return currentuser;
	}
	
	public List<String> findUserNames()
	{
		List<String> usernames = db.getUserNames();
		return usernames;
	}

}
