package com.myobjects.repository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.myobjects.model.ReminderItem;
import com.myobjects.model.ScheduleItem;
import com.myobjects.model.FriendUsers;
import com.myobjects.model.RegisteredUser;
import com.myobjects.model.StockList;

 

public class UserRepositoryManager {
 
	@Inject
	private EntityManager em;	 
 
	
	
	public RegisteredUser  getCurrentUser(String name){
		
		RegisteredUser user = null;
		
		Query query = em.createQuery("FROM RegisteredUser where username = '" + name + "'");
		
		if (query.getResultList().size() > 0)
		{
			user =  (RegisteredUser) query.getResultList().get(0);
		}
		return user;     
	}
	
	public List<FriendUsers>  getFriendUsers(String regUser){
		
		Query query = em.createQuery("FROM FriendUsers where registeredUserid = '"  + regUser + "'");
		List <FriendUsers> list =  query.getResultList();
		return list;	      
	}
	
	public List<String>  getUserNames(){		
		Query query = em.createQuery("FROM RegisteredUser");
		List <RegisteredUser> list =  query.getResultList();
		List <String> namelist =  new ArrayList<String>();		
		for (int i = 0; i < list.size();i++)
		{
			namelist.add(list.get(i).getUsername());
		}
		return namelist;	      
	}
	
	public List<ScheduleItem>  getScheduleItems(String regUser){
		
		Query query = em.createQuery("FROM ScheduleItem where registeredUserid = '"  + regUser + "' and active = 1");
		List <ScheduleItem> list =  query.getResultList();
		return list;	      
	}
	
	public List<ReminderItem>  getReminderItems(String regUser){
		
		Query query = em.createQuery("FROM ReminderItem where registeredUserid = '"  + regUser + "' and active = 1");
		List <ReminderItem> list =  query.getResultList();
		return list;	      
	}
	public List<StockList> getStocks(String username) {
		
		Query query = em.createQuery("FROM StockList where registeredUserid = '"  + username + "'");
		List <StockList> list =  query.getResultList();
		return list;
	}
	public ReminderItem getReminderItem(String id) {
		
		Query query = em.createQuery("FROM ReminderItem where scheduleItemid = '"  + id + "'");
		ReminderItem item = null;
		
		if (query.getResultList().size() > 0)
		{
			item =  (ReminderItem) query.getResultList().get(0);
		}
		
		return item;
	}
		
}
