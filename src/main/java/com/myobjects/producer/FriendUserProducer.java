package com.myobjects.producer;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.session.bean.UserContext;
import com.myobjects.model.FriendUsers;
import com.myobjects.repository.UserRepositoryManager;

 
@RequestScoped
public class FriendUserProducer {
	@Inject UserRepositoryManager db;
	
	@Inject UserContext session;

	private List<FriendUsers> frienduserList;
	
	@Produces
	@Named
	public List<FriendUsers> getFriendUserList() {
		
		frienduserList = db.getFriendUsers(session.getRegisteredUser().getUsername());
		
		return frienduserList;
	}

	public void setFriendUserList(List<FriendUsers> frienduserList) {
		this.frienduserList = frienduserList;
	}
	

}
