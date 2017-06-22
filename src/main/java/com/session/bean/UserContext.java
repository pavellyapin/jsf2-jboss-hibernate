package com.session.bean;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;

import com.myobjects.bean.UserManager;
import com.myobjects.ejb.UserServiceBean;
import com.myobjects.model.RegisteredUser;

import dom.beans.ScheduleView;

@Named
@SessionScoped
public class UserContext implements Serializable{
    
    public static AtomicLong INSTANCE_COUNT = new AtomicLong(0);
    
    private RegisteredUser registeredUser;
    private String taskID;
    
    @Inject
    private UserManager userManager;
    
    @Inject
    private ScheduleView scheduleManager;
    
    @PostConstruct
    public void onNewSession(){
        INSTANCE_COUNT.incrementAndGet();
    }

    @PreDestroy
    public void onSessionDestruction(){
        INSTANCE_COUNT.decrementAndGet();
    }
    
	public String handleLogin(){
		RegisteredUser user = userManager.login();
		if (user!=null)
		{
		this.setRegisteredUser(user);
		return "main?faces-redirect=true";
		}
		
		return "";
	}	
	
	public String toMain(){		
		return "main?faces-redirect=true";
	}
	
	public String handleSignUp(){
		return "signup?faces-redirect=true";
	}
	
	public String onSignUp(){
		userManager.registerUser();
		return "main?faces-redirect=true";
	}
	
	public String toMonth(){		
		return "month?faces-redirect=true";
	}
	
	public String reminderLink(){
		
    	String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");    	     	
    	setTaskID(id);
    	return "month?faces-redirect=true";		
	}

	public RegisteredUser getRegisteredUser() {
		return registeredUser;
	}

	public void setRegisteredUser(RegisteredUser registeredUser) {
		this.registeredUser = registeredUser;
	}

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
    
}