package com.myobjects.ejb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.myobjects.model.FriendUsers;
import com.myobjects.model.RegisteredUser;
import com.myobjects.model.ReminderItem;
import com.myobjects.model.ScheduleItem;
import com.myobjects.model.StockList;
import com.myobjects.producer.FriendUserProducer;
import com.myobjects.producer.ReminderItemProducer;
import com.myobjects.producer.ScheduleItemProducer;
import com.myobjects.producer.StocksProducer;
import com.session.bean.UserContext;

import dom.rss.RSSFeedParser;



@Stateful

public class  UserServiceBean   {

	@Inject
	private Event<RegisteredUser> userEventSrc;	
	@Inject
	private Event<StockList> stocksEventSrc;
	@Inject
	private Event<FriendUsers> friendUserEventSrc;	
	@Inject
	private Event<ScheduleItem> scheduleItemEventSrc;	
	@Inject
	private Event<ReminderItem> reminderItemEventSrc;	
	@Inject
    UserContext userSession;	
	@Inject
	private EntityManager em;	
	@Inject
	FriendUserProducer friendUser;	
	@Inject
	ScheduleItemProducer ScheduleItems;	
	@Inject
	ReminderItemProducer ReminderItems;	
	@Inject
	StocksProducer StockList;
	
	DateFormat dateFormat = new SimpleDateFormat("EEE , MMM dd, hh:mm");
		
	public List<FriendUsers> getUserFriends(){	
		
		List<FriendUsers> friends = friendUser.getFriendUserList();
		return friends;	
	}
	
	public List<ReminderItem> getReminderItems(){	
		
		List<ReminderItem> reminders = ReminderItems.getReminderItemList();
		return reminders;	
	}
	
	public List<ScheduleItem> getScheduleItems(){	
		
		List<ScheduleItem> meetings = ScheduleItems.getScheduleItemList();
		return meetings;	
	}
	
	public List<StockList> getStockList(){	
		
		List<StockList> stocks = StockList.getStockList();
		return stocks;	
	}
	
	public ScheduleItem updateScheduleItem(ScheduleItem meeting){
		
		em.merge(meeting);
		em.flush();
		return meeting;
			
	}
	
	public ScheduleItem updateScheduleItemWithReminder(ScheduleItem meeting){
		
		ReminderItem reminder = ReminderItems.getReminderItem(Long.toString(meeting.getId()));
		
		 
		if(reminder==null)
		{
			reminder = new ReminderItem();
			reminder.setRemindDate(dateFormat.format(meeting.getStartDate()));
	    	reminder.setActive(true);
	    	reminder.setDescr(meeting.getName());
	    	reminder.setScheduleItemid(meeting.getId().toString());
			addReminderItem(reminder);
		}
		else
		{
			reminder.setDescr(meeting.getName());
			reminder.setRemindDate(dateFormat.format(meeting.getStartDate()));
			
		}
		
		em.merge(meeting);
		em.flush();

		return meeting;
			
	}
	
	public FriendUsers updateFriendUser(FriendUsers friend){
		
		em.merge(friend);
		em.flush();
		return friend;
			
	}
	
	
	
	public ScheduleItem addScheduleItem(ScheduleItem meeting){			

		    meeting.setRegisteredUser(userSession.getRegisteredUser().getUsername());
		    meeting.setActive(true);
			em.persist(meeting);			
			scheduleItemEventSrc.fire(meeting);	
			em.flush();
			return meeting;
				
	}
	
	public void addScheduleItemWithReminder(ScheduleItem meeting) {
		
		ReminderItem reminder = new ReminderItem(); 
		reminder.setRemindDate(dateFormat.format(meeting.getStartDate()));
    	reminder.setActive(true);
    	reminder.setDescr(meeting.getName());
		
		meeting = addScheduleItem(meeting);	
		reminder.setScheduleItemid(meeting.getId().toString());
		addReminderItem(reminder);
	}
	
	public void addReminderItem(ReminderItem reminder){			

		reminder.setRegisteredUser(userSession.getRegisteredUser().getUsername());
		em.persist(reminder);
		reminderItemEventSrc.fire(reminder);			
			
    }
		
	public void addUserFriends(List<FriendUsers> friends , RegisteredUser activeUser){	
		
		for (int i =0 ; i < friends.size() ; i++)
		{
			addFriendUser(friends.get(i));						
		}		
	}
	
	public FriendUsers addFriendUser(FriendUsers friend){	
		
		    friend.setRegisteredUser(userSession.getRegisteredUser().getUsername());
			em.persist(friend);
			friendUserEventSrc.fire(friend);
			em.flush();
			
			return friend;				
	}
		
	public void register(RegisteredUser p){		
		 em.persist(p);
		 userEventSrc.fire(p);
	}

	public void setDefaultSocks() {
		  	
		StockList list1 = new StockList();
		list1.setRegisteredUser(userSession.getRegisteredUser().getUsername());
		list1.setName("Tech Goods");
		list1.setStock1("GOOG");
		list1.setStock2("AAPL");
		list1.setStock3("ORCL");
		list1.setStock4("MSFT");
		list1.setStock5("FB");
		list1.setStock6("YHOO");
		list1.setStock7("AMZN");
		list1.setStock8("NOK");
		list1.setStock9("LG");
		list1.setStock10("AMD");
		
		
		StockList list2 = new StockList();
		list2.setRegisteredUser(userSession.getRegisteredUser().getUsername());
		list2.setName("Physical Goods");
		list2.setStock1("BA");
		list2.setStock2("TEVA");
		list2.setStock3("XOM");
		list2.setStock4("TSLA");
		list2.setStock5("SBUX");
		list2.setStock6("HMC");
		list2.setStock7("TIME");
		list2.setStock8("AEO");
		list2.setStock9("TM");		
		list2.setStock10("SNE");
		
		em.persist(list1);
		stocksEventSrc.fire(list1);
		
		em.persist(list2);
		stocksEventSrc.fire(list2);
		
	}
	
	public StockList updateStockList(StockList list)
	{
		StockList list1 = em.find(StockList.class,list.getId());		
		
		em.merge(list);
		
		return list;
	}

	public void setDefaultTasks() {
		
		Calendar start = new GregorianCalendar();
		start.set(Calendar.HOUR_OF_DAY, 19); //anything 0 - 23
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
	    Date start1 = start.getTime(); 
	    
		Calendar end = new GregorianCalendar();
		end.set(Calendar.HOUR_OF_DAY, 20); //anything 0 - 23
		end.set(Calendar.MINUTE, 30);
		end.set(Calendar.SECOND, 0);
	    Date end1 = end.getTime();
		
		ScheduleItem item = new ScheduleItem();
		item.setName("Make Dinner - Recipe is included");
		item.setNotes("<div style='margin: 12px 0px 4px; padding: 8px 0px 0px; border-width: 1px 0px 0px; border-style: solid; border-color: rgb(236, 233, 216); outline: 0px; color: rgb(51, 51, 51); font-family: Arial, Helvetica, sans-serif; font-size: 11px; background-color: rgb(255, 255, 255);'><span style='margin: 0px; padding: 0px; border: 0px; outline: 0px; letter-spacing: 0.05em; text-transform: uppercase; font-weight: bold; color: rgb(251, 100, 0); font-size: 13px;'>INGREDIENTS:</span></div><table border='0' cellpadding='0' cellspacing='0' width='100%' style='margin: 0px; padding: 0px; border: 0px; outline: 0px; border-collapse: collapse; border-spacing: 0px; color: rgb(51, 51, 51); font-family: Arial, Helvetica, sans-serif; font-size: 11px; background-color: rgb(255, 255, 255);'><tbody style='margin: 0px; padding: 0px; border: 0px; outline: 0px;'><tr style='margin: 0px; padding: 0px; border: 0px; outline: 0px;'><td width='50%' valign='top' style='margin: 0px; padding: 0px; border: 0px; outline: 0px;'><div data-role='recipe-ingredient' data-ingredientid='6494' data-grams='708' style='margin: 0px 8px 0px 0px; padding: 0px; border: 0px; outline: 0px;'>6 skinless, boneless chicken breast</div><div data-role='recipe-ingredient' data-ingredientid='6494' data-grams='708' style='margin: 0px 8px 4px 0px; padding: 0px; border: 0px; outline: 0px;'>halves</div><div data-role='recipe-ingredient' data-ingredientid='18740' data-grams='5.5' style='margin: 0px 8px 4px 0px; padding: 0px; border: 0px; outline: 0px;'>1 teaspoon garlic salt</div><div data-role='recipe-ingredient' data-ingredientid='16406' data-grams='1' style='margin: 0px 8px 4px 0px; padding: 0px; border: 0px; outline: 0px;'>ground black pepper to taste</div><div data-role='recipe-ingredient' data-ingredientid='6307' data-grams='27' style='margin: 0px 8px 4px 0px; padding: 0px; border: 0px; outline: 0px;'>2 tablespoons olive oil</div><div data-role='recipe-ingredient' data-ingredientid='4397' data-grams='110' style='margin: 0px 8px 4px 0px; padding: 0px; border: 0px; outline: 0px;'>1 onion, thinly sliced</div></td><td width='50%' valign='top' style='margin: 0px; padding: 0px; border: 0px; outline: 0px;'><div data-role='recipe-ingredient' data-ingredientid='10214' data-grams='392' style='margin: 0px 8px 4px 0px; padding: 0px; border: 0px; outline: 0px;'>1 (14.5 ounce) can diced tomatoes</div><div data-role='recipe-ingredient' data-ingredientid='18930' data-grams='120' style='margin: 0px 8px 4px 0px; padding: 0px; border: 0px; outline: 0px;'>1/2 cup balsamic vinegar</div><div data-role='recipe-ingredient' data-ingredientid='16379' data-grams='1.4' style='margin: 0px 8px 4px 0px; padding: 0px; border: 0px; outline: 0px;'>1 teaspoon dried basil</div><div data-role='recipe-ingredient' data-ingredientid='16403' data-grams='1.5' style='margin: 0px 8px 4px 0px; padding: 0px; border: 0px; outline: 0px;'>1 teaspoon dried oregano</div><div data-role='recipe-ingredient' data-ingredientid='16412' data-grams='1.090833' style='margin: 0px 8px 4px 0px; padding: 0px; border: 0px; outline: 0px;'>1 teaspoon dried rosemary</div><div data-role='recipe-ingredient' data-ingredientid='16417' data-grams='0.7166666' style='margin: 0px 8px 4px 0px; padding: 0px; border: 0px; outline: 0px;'>1/2 teaspoon dried thyme</div></td></tr></tbody></table><div style='margin: 12px 0px 4px; padding: 8px 0px 0px; border-width: 1px 0px 0px; border-style: solid; border-color: rgb(236, 233, 216); outline: 0px; color: rgb(51, 51, 51); font-family: Arial, Helvetica, sans-serif; font-size: 11px; background-color: rgb(255, 255, 255);'><span style='margin: 0px; padding: 0px; border: 0px; outline: 0px; letter-spacing: 0.05em; text-transform: uppercase; font-weight: bold; color: rgb(251, 100, 0); font-size: 13px;'>DIRECTIONS:</span></div><table border='0' cellpadding='0' cellspacing='0' style='margin: 0px; padding: 0px; border: 0px; outline: 0px; border-collapse: collapse; border-spacing: 0px; color: rgb(51, 51, 51); font-family: Arial, Helvetica, sans-serif; font-size: 11px; background-color: rgb(255, 255, 255);'><tbody style='margin: 0px; padding: 0px; border: 0px; outline: 0px;'><tr style='margin: 0px; padding: 0px; border: 0px; outline: 0px;'><td valign='top' style='margin: 0px; padding: 0px 5px 0px 0px; border: 0px; outline: 0px; font-weight: bold; color: rgb(251, 100, 0);'>1.</td><td valign='top' style='margin: 0px; padding: 0px 0px 8px; border: 0px; outline: 0px;'>Season both sides of chicken breasts with garlic salt and pepper.</td></tr><tr style='margin: 0px; padding: 0px; border: 0px; outline: 0px;'><td valign='top' style='margin: 0px; padding: 0px 5px 0px 0px; border: 0px; outline: 0px; font-weight: bold; color: rgb(251, 100, 0);'>2.</td><td valign='top' style='margin: 0px; padding: 0px 0px 8px; border: 0px; outline: 0px;'>Heat olive oil in a skillet over medium heat; cook seasoned chicken breasts until chicken is browned, 3 to 4 minutes per side. Add onion; cook and stir until onion is browned, 3 to 4 minutes.</td></tr><tr style='margin: 0px; padding: 0px; border: 0px; outline: 0px;'><td valign='top' style='margin: 0px; padding: 0px 5px 0px 0px; border: 0px; outline: 0px; font-weight: bold; color: rgb(251, 100, 0);'>3.</td><td valign='top' style='margin: 0px; padding: 0px 0px 8px; border: 0px; outline: 0px;'>Pour diced tomatoes and balsamic vinegar over chicken; season with basil, oregano, rosemary and thyme. Simmer until chicken is no longer pink and the juices run clear, about 15 minutes. An instant-read thermometer inserted into the center should read at least 165 degrees F (74 degrees C).</td></tr></tbody></table>");
		item.setStartDate(start1);
		item.setEndDate(end1);
		item.setLocation("Home");
		item.setType("mealsHome");
		item.setActive(true);
		item.setRegisteredUser(userSession.getRegisteredUser().getUsername());
		
		em.persist(item);
		scheduleItemEventSrc.fire(item);
		
		start = new GregorianCalendar();
		start.set(Calendar.HOUR_OF_DAY, 8); //anything 0 - 23
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
	    start1 = start.getTime(); 
	    
		end = new GregorianCalendar();
		end.set(Calendar.HOUR_OF_DAY, 9); //anything 0 - 23
		end.set(Calendar.MINUTE, 0);
		end.set(Calendar.SECOND, 0);
	    end1 = end.getTime();
		
		item = new ScheduleItem();
		item.setName("Breakfast - Eggs with zucchini");
		item.setNotes("<div style='margin: 12px 0px 4px; padding: 8px 0px 0px; border-width: 1px 0px 0px; border-style: solid; border-color: rgb(236, 233, 216); outline: 0px; color: rgb(51, 51, 51); font-family: Arial, Helvetica, sans-serif; font-size: 11px; background-color: rgb(255, 255, 255);'><span style='margin: 0px; padding: 0px; border: 0px; outline: 0px; letter-spacing: 0.05em; text-transform: uppercase; font-weight: bold; color: rgb(251, 100, 0); font-size: 13px;'>INGREDIENTS:</span></div><table border='0' cellpadding='0' cellspacing='0' width='100%' style='margin: 0px; padding: 0px; border: 0px; outline: 0px; border-collapse: collapse; border-spacing: 0px; color: rgb(51, 51, 51); font-family: Arial, Helvetica, sans-serif; font-size: 11px; background-color: rgb(255, 255, 255);'><tbody style='margin: 0px; padding: 0px; border: 0px; outline: 0px;'><tr style='margin: 0px; padding: 0px; border: 0px; outline: 0px;'><td width='50%' valign='top' style='margin: 0px; padding: 0px; border: 0px; outline: 0px;'><div data-role='recipe-ingredient' data-ingredientid='16317' data-grams='200' style='margin: 0px 8px 4px 0px; padding: 0px; border: 0px; outline: 0px;'>4 eggs, lightly beaten</div><div data-role='recipe-ingredient' data-ingredientid='16238' data-grams='10' style='margin: 0px 8px 4px 0px; padding: 0px; border: 0px; outline: 0px;'>2 tablespoons grated Parmesan cheese</div><div data-role='recipe-ingredient' data-ingredientid='6307' data-grams='27' style='margin: 0px 8px 4px 0px; padding: 0px; border: 0px; outline: 0px;'>2 tablespoons olive oil</div></td><td width='50%' valign='top' style='margin: 0px; padding: 0px; border: 0px; outline: 0px;'><div data-role='recipe-ingredient' data-ingredientid='4529' data-grams='118' style='margin: 0px 8px 4px 0px; padding: 0px; border: 0px; outline: 0px;'>1 zucchini, sliced 1/8- to 1/4-inch thick</div><div data-role='recipe-ingredient' data-ingredientid='16396' data-grams='0.6941667' style='margin: 0px 8px 4px 0px; padding: 0px; border: 0px; outline: 0px;'>garlic powder, or to taste</div><div data-role='recipe-ingredient' data-ingredientid='16421' data-grams='0.4' style='margin: 0px 8px 4px 0px; padding: 0px; border: 0px; outline: 0px;'>salt and ground black pepper to taste</div></td></tr></tbody></table><div style='margin: 12px 0px 4px; padding: 8px 0px 0px; border-width: 1px 0px 0px; border-style: solid; border-color: rgb(236, 233, 216); outline: 0px; color: rgb(51, 51, 51); font-family: Arial, Helvetica, sans-serif; font-size: 11px; background-color: rgb(255, 255, 255);'><span style='margin: 0px; padding: 0px; border: 0px; outline: 0px; letter-spacing: 0.05em; text-transform: uppercase; font-weight: bold; color: rgb(251, 100, 0); font-size: 13px;'>DIRECTIONS:</span></div><table border='0' cellpadding='0' cellspacing='0' style='margin: 0px; padding: 0px; border: 0px; outline: 0px; border-collapse: collapse; border-spacing: 0px; color: rgb(51, 51, 51); font-family: Arial, Helvetica, sans-serif; font-size: 11px; background-color: rgb(255, 255, 255);'><tbody style='margin: 0px; padding: 0px; border: 0px; outline: 0px;'><tr style='margin: 0px; padding: 0px; border: 0px; outline: 0px;'><td valign='top' style='margin: 0px; padding: 0px 5px 0px 0px; border: 0px; outline: 0px; font-weight: bold; color: rgb(251, 100, 0);'>1.</td><td valign='top' style='margin: 0px; padding: 0px 0px 8px; border: 0px; outline: 0px;'>Stir the eggs and Parmesan cheese together in a bowl; set aside.</td></tr><tr style='margin: 0px; padding: 0px; border: 0px; outline: 0px;'><td valign='top' style='margin: 0px; padding: 0px 5px 0px 0px; border: 0px; outline: 0px; font-weight: bold; color: rgb(251, 100, 0);'>2.</td><td valign='top' style='margin: 0px; padding: 0px 0px 8px; border: 0px; outline: 0px;'>Heat the olive oil in a large skillet over medium-high heat; cook the zucchini in the hot oil until softened and lightly browned, about 7 minutes. Season the zucchini with garlic powder, salt, and pepper. Reduce heat to medium; pour the egg mixture into the skillet. Cook, stirring gently, for about 3 minutes. Remove the skillet from the heat and cover. Keep covered off the heat until the eggs set, about 2 minutes more.</td></tr></tbody></table>");
		item.setStartDate(start1);
		item.setEndDate(end1);
		item.setLocation("Home");
		item.setType("mealsHome");
		item.setActive(true);
		item.setRegisteredUser(userSession.getRegisteredUser().getUsername());
		
		em.persist(item);
		scheduleItemEventSrc.fire(item);
		
		start = new GregorianCalendar();
		start.set(Calendar.HOUR_OF_DAY, 15); //anything 0 - 23
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
	    start1 = start.getTime(); 
	    
		end = new GregorianCalendar();
		end.set(Calendar.HOUR_OF_DAY, 17); //anything 0 - 23
		end.set(Calendar.MINUTE, 0);
		end.set(Calendar.SECOND, 0);
	    end1 = end.getTime();
		
		item = new ScheduleItem();
		item.setName("Prepare for Interview - Sample questions included");
		item.setNotes("<p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'>Remember, these responses are only suggestions. Try to personalize your response as much as possible.</p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>Question: Tell me about yourself.</strong></p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>A:</strong><span class='Apple-converted-space'>&nbsp;</span>Identify some of your main attributes and memorize them. Describe your qualifications, career history and range of skills, emphasizing those skills relevant to the job on offer.</p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>Q: What have your achievements been to date?</strong></p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>A:</strong><span class='Apple-converted-space'>&nbsp;</span>Select an achievement that is work-related and fairly recent. Identify the skills you used in the achievement and quantify the benefit it had to the company. For example, 'my greatest achievement has been to design and implement a new sales ledger system, bringing it in ahead of time and improving our debtors' position significantly, saving the company $50,000 per month in interest'.</p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>Q: Are you happy with your career to date?</strong></p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>A:</strong><span class='Apple-converted-space'>&nbsp;</span>This question is really about your self-esteem, confidence and career aspirations. The answer must be 'yes', followed by a brief explanation as to what it is about your career so far that's made you happy. If you have hit a career plateau, or you feel you are moving too slowly, then you must qualify your answer.</p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>Q: What is the most difficult situation you have had to face and how did you tackle it?</strong></p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>A:</strong><span class='Apple-converted-space'>&nbsp;</span>The purpose of this question is to find out what your definition of difficult is and whether you can show a logical approach to problem solving. In order to show yourself in a positive light, select a difficult work situation which was not caused by you and which can be quickly explained in a few sentences. Explain how you defined the problem, what the options were, why you selected the one you did and what the outcome was. Always end on a positive note.</p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>Q: What do you like about your present job?</strong></p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>A:</strong><span class='Apple-converted-space'>&nbsp;</span>This is a straightforward question. All you have to do is make sure that your 'likes' correspond to the skills etc. required in the job on offer. Be enthusiastic; describe your job as interesting and diverse but do not overdo it - after all, you are looking to leave.</p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>Q: What do you dislike about your present job?</strong></p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>A:</strong><span class='Apple-converted-space'>&nbsp;</span>Be cautious with this answer. Do not be too specific as you may draw attention to weaknesses that will leave you open to further problems. One approach is to choose a characteristic of your present company, such as its size or slow decision-making processes etc. Give your answer with the air of someone who takes problems and frustrations in your stride as part of the job.</p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>Q: What are your strengths?</strong></p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>A:</strong><span class='Apple-converted-space'>&nbsp;</span>This is one question that you know you are going to get so there is no excuse for being unprepared. Concentrate on discussing your main strengths. List three or four proficiencies e.g. your ability to learn quickly, determination to succeed, positive attitude, your ability to relate to people and achieve a common goal. You may be asked to give examples of the above so be prepared.</p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>Q: What is your greatest weakness?</strong></p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>A:</strong><span class='Apple-converted-space'>&nbsp;</span>Do not say you have none - this will lead to further problems. You have two options - use a professed weakness such as a lack of experience (not ability) on your part in an area that is not vital for the job. The second option is to describe a personal or professional weakness that could also be considered to be a strength, and the steps you have taken to combat it. An example would be, 'I know my team thinks I'm too demanding at times&nbsp;- I tend to drive them pretty hard but I'm getting much better at using the carrot and not the stick'.</p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>Q: Why do you want to leave your current employer?</strong></p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>A:</strong><span class='Apple-converted-space'>&nbsp;</span>State how you are looking for a new challenge, more responsibility, experience and a change of environment. Do not be negative in your reasons for leaving. It is rarely appropriate to cite salary as your primary motivator.</p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>Q: Why have you applied for this particular job?</strong></p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>A:</strong><span class='Apple-converted-space'>&nbsp;</span>The employer is looking for evidence that the job suits you, fits in with your general aptitudes, coincides with your long-term goals and involves doing things you enjoy. Make sure you have a good understanding of the role and the organization, and describe the attributes of the organization that interest you most.</p><p style='font: 11px/normal Arial, sans-serif; margin: 0px 0px 5px; padding: 0px 14px; width: 477px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; clear: left; word-spacing: 0px; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><strong>Other questions to consider:</strong></p><ul style='font: 11px/normal Arial, sans-serif; margin: 0px; padding: 5px 14px; color: rgb(0, 0, 0); text-transform: none; text-indent: 0px; letter-spacing: normal; word-spacing: 0px; float: left; list-style-type: none; white-space: normal; widows: 1; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;'><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>How does your job fit in to your department and company?</li><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>What do you enjoy about this industry?</li><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>Give an example of when you have worked under pressure.</li><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>What kinds of people do you like working with?</li><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>Give me an example of when your work was criticized.</li><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>Give me an example of when you have felt anger at work. How did you cope and did you still perform a good job?</li><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>What kind of people do you find it difficult to work with?</li><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>Give me an example of when you have had to face a conflict of interest at work.</li><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>Tell me about the last time you disagreed with your boss.</li><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>Give me an example of when you haven't got on with others.</li><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>Do you prefer to work alone or in a group? Why?</li><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>This organization is very different to your current employer - how do you think you are going to fit in?</li><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>What are you looking for in a company?</li><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>How do you measure your own performance?</li><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>What kind of pressures have you encountered at work?</li><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>Are you a self-starter? Give me examples to demonstrate this?</li><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>What changes in the workplace have caused you difficulty and why?</li><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>How do you feel about working long hours and/or weekends?</li><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>Give me an example of when you have been out of your depth.</li><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>What have you failed to achieve to date?</li><li style='background: url('http://www.michaelpage.ca/static/web/mp/us/img/mp4/global_list_bullet.gif') no-repeat 0px 3px; margin: 0px !important; padding: 0px 0px 0px 10px;'>What can you bring to this organization?</li></ul>");
		item.setStartDate(start1);
		item.setEndDate(end1);
		item.setLocation("Home");
		item.setType("todoPro");
		item.setActive(true);
		item.setRegisteredUser(userSession.getRegisteredUser().getUsername());
		
		em.persist(item);
		scheduleItemEventSrc.fire(item);
		em.flush();
		
		ReminderItem reminder = new ReminderItem(); 
		reminder.setRemindDate(dateFormat.format(item.getStartDate()));
    	reminder.setActive(true);
    	reminder.setDescr(item.getName());			
		reminder.setScheduleItemid(item.getId().toString());
		addReminderItem(reminder);
		
		FriendUsers pavel = new FriendUsers();
		pavel.setFirstname("Pavel");
		pavel.setLastname("Lyapin");
		pavel.setEmail("mr.pavellyapin@gmail.com");
		pavel.setImg("./images/linkedin.gif");
		pavel.setUrl("https://www.linkedin.com/in/pavellyapin");
		addFriendUser(pavel);
		
		start = new GregorianCalendar();
		start.set(Calendar.HOUR_OF_DAY, 17); //anything 0 - 23
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
	    start1 = start.getTime(); 
	    
		end = new GregorianCalendar();
		end.set(Calendar.HOUR_OF_DAY, 17); //anything 0 - 23
		end.set(Calendar.MINUTE, 30);
		end.set(Calendar.SECOND, 0);
	    end1 = end.getTime();
		
		item = new ScheduleItem();
		item.setName("Send feedback about PortalPlan");
		item.setNotes("Please let me know what you think about PortalPlan, either send me an email or leave a message on LinkedIn. Thank you!");
		item.setStartDate(start1);
		item.setEndDate(end1);
		item.setLocation("Home");
		item.setType("lunchContact");
		item.setActive(true);
		item.setInvolvedFriends(pavel.getId().toString());
		item.setRegisteredUser(userSession.getRegisteredUser().getUsername());
		
		em.persist(item);
		scheduleItemEventSrc.fire(item);
		em.flush();
		
		reminder = new ReminderItem(); 
		reminder.setRemindDate(dateFormat.format(item.getStartDate()));
    	reminder.setActive(true);
    	reminder.setDescr(item.getName());			
		reminder.setScheduleItemid(item.getId().toString());
		addReminderItem(reminder);
		
		
	
	
	}

	public void deleteScheduleItem(ScheduleItem scheduleItem) {
		
		ReminderItem reminder = ReminderItems.getReminderItem(Long.toString(scheduleItem.getId()));
		if(reminder!=null)
		{
			reminder.setActive(false);
			em.merge(reminder);			
		}
		
		scheduleItem.setActive(false);
		em.merge(scheduleItem);
		
	}
	 
}
