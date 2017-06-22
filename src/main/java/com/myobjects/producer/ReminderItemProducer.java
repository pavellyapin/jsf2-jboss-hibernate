package com.myobjects.producer;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.session.bean.UserContext;
import com.myobjects.model.ReminderItem;
import com.myobjects.repository.UserRepositoryManager;

 
@RequestScoped
public class ReminderItemProducer {
	
	@Inject UserRepositoryManager db;
	
	@Inject UserContext session;

	private List<ReminderItem> ReminderItems;
	
	@Produces
	@Named
	public List<ReminderItem> getReminderItemList() {
		
		ReminderItems = db.getReminderItems(session.getRegisteredUser().getUsername());		
		return ReminderItems;
	}

	public void setReminderItemList(List<ReminderItem> ReminderItems) {
		this.ReminderItems = ReminderItems;
	}
	
	public ReminderItem getReminderItem(String id)
	{
		ReminderItem Item = db.getReminderItem(id);
		
		return Item;
		
	}
	

}
