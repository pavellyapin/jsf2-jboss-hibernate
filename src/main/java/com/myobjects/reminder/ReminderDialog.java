package com.myobjects.reminder;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.myobjects.bean.UserManager;
import com.myobjects.model.ReminderItem;

@Model
public class ReminderDialog {
	
	
	@Inject
    private UserManager userManager;
	
	@Produces
	@Named
	ReminderItem newReminder;	
	
    @PostConstruct
    public void init() {    	
    	newReminder = new ReminderItem();
    }
    
    
	public void addReminder(){	
		
 		userManager.addReminderItem(newReminder); 		
	}
	
	

}
