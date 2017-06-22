package com.myobjects.producer;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.session.bean.UserContext;
import com.myobjects.model.ScheduleItem;
import com.myobjects.model.FriendUsers;
import com.myobjects.repository.UserRepositoryManager;

 
@RequestScoped
public class ScheduleItemProducer {
	@Inject UserRepositoryManager db;
	
	@Inject UserContext session;

	private List<ScheduleItem> ScheduleItems;
	
	@Produces
	@Named
	public List<ScheduleItem> getScheduleItemList() {
		
		ScheduleItems = db.getScheduleItems(session.getRegisteredUser().getUsername());		
		return ScheduleItems;
	}

	public void setScheduleItemList(List<ScheduleItem> ScheduleItems) {
		this.ScheduleItems = ScheduleItems;
	}
	

}
