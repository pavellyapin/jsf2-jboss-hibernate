package com.myobjects.reminder;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.myobjects.bean.UserManager;
import com.myobjects.model.ReminderItem;

import dom.money.beans.StockItem;
import dom.money.beans.StockService;
 
@Named
@ViewScoped
public class ReminderView implements Serializable {
     
    private List<ReminderItem> reminders;
     
    private ReminderItem selectedReminder;
     
	@Inject
    private UserManager userManager;
     
    @PostConstruct
    public void init() {
    	reminders = userManager.getReminderItemList();
    }
 
    public List<ReminderItem> getReminders() {
        return reminders;
    }
 
    public ReminderItem getSelectedReminder() {
        return selectedReminder;
    }
 
    public void setSelectedReminder(ReminderItem selectedReminder) {
        this.selectedReminder = selectedReminder;
    }
}