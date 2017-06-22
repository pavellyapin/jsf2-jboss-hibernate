package dom.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.DragDropEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.myobjects.ejb.UserServiceBean;
import com.myobjects.model.FriendUsers;
import com.myobjects.model.RegisteredUser;
import com.myobjects.model.ReminderItem;
import com.myobjects.model.ScheduleItem;
import com.myobjects.producer.FriendUserProducer;
import com.session.bean.UserContext;

import dom.pdf.beans.SimpleTaskConvert;
 
@ViewScoped
@Named("scheduleView")

public class ScheduleView implements Serializable {
	
	@Inject
	UserServiceBean manager;
	
	@Inject
	UserContext userContext;
	
	@Produces
	@Named
	ScheduleItem scheduleItem;
	
	@Produces
	@Named
	FriendUsers newFriend;
 
    private ScheduleModel eventModel;
    DateFormat dateFormat = new SimpleDateFormat("EEE , MMM dd, hh:mm");
    
    //Buttons
    private boolean saveDisabled = false;
    private boolean pdfDisabled = false;
    private boolean createDisabled = false;
    private boolean deleteDisabled = false;
    
     
    //Professional
    private boolean showMeeting = true;    
    private boolean showToDoWork = true;    
    private boolean showAppointment = true;
    
    //Home&Daily    
    private boolean showMeals = true;
    private boolean showOrder = true;
    private boolean showLeasure = true;
    
    //With Somebody
    private boolean showMorning = true;
    private boolean showLunch = true;
    private boolean showNight = true;
    
    private ScheduleEvent event = new DefaultScheduleEvent();    
    @Inject
    private FriendUserProducer service;    
    private List<FriendUsers> friends;    
    private List<FriendUsers> droppedFriends;    
    private List<FriendUsers> filteredFriends;    
        
    private List<ScheduleItem> items;
    private Long friendId;
 

        @PostConstruct
        public void init() {
        	
        	newFriend = new FriendUsers();
        	friends = service.getFriendUserList();
        	droppedFriends = new ArrayList<FriendUsers>();        	
        	eventModel = new DefaultScheduleModel(); 
        	if (userContext.getTaskID() != null)
        	{
        		findTask(userContext.getTaskID());
        		updateView ();
        	}
        	else
        	{
        	scheduleItem = new ScheduleItem();
        	updateView ();
      	  	pdfDisabled = true;
      	    setDeleteDisabled(true);
        	} 
        }
    	public boolean isInBoth(String value, String[] arrayA, String[] arrayB) {
    		
    		boolean found = false;
    		   		
    		List<String> arrayAlist = new ArrayList<String>();
    		List<String> arrayBlist = new ArrayList<String>();
    		
    		if (arrayA == null || arrayB == null)
    		{
    			return found;
    		}
    		else
    		{
    			arrayAlist = Arrays.asList(arrayA);
        		arrayBlist = Arrays.asList(arrayB);
        		
        		if (arrayAlist.contains(value) && arrayBlist.contains(value))
        		{
        			found = true;
        		}
    		}   		
    		    		
    		return found;
    	}
    	
    	public int countActiveEmployeesInCity(Connection c, String city) throws SQLException {
    		
    		
    		ResultSet rs = null;
    		try{
    		//Will return a list of unique employee IDs of employees living in the given named city
    		PreparedStatement ps = 	c.prepareStatement("SELECT DISTINCT EMP_ID FROM EMPLOYEE E INNER JOIN CITY C ON E.CITY_ID = C.CITY_ID WHERE C.CITY_NAME = ? AND E.EMP_ACTIVE = 'Y'");
    		ps.setString(1, city);
    		rs = ps.executeQuery();
    		} catch (SQLException e)
    		{
    			System.out.println(e.getMessage());
    		}
    		finally{
    			//Closing connection to database
    			c.close();
    		}
    		
    		int numActive = 0;
    		
    		if (rs != null)
    		{
    			while (rs.next()) {    				
    					numActive++;    				
    			} 
    		}
    		
    		return numActive;
    	}
        public void updateView ()
        {
        	eventModel = new DefaultScheduleModel();
        	
        	items = manager.getScheduleItems();
        	
        	 for(int i = 0; i < items.size();i ++)
         	{
             	ScheduleItem item =  items.get(i);
             	String type = item.getType();
             	
             	Date from = item.getStartDate();
             	Date to = item.getEndDate();
             	String descr = item.getName();    
             	
             	if("mealsHome".equals(type) && showMeals == true )
             	{
             		DefaultScheduleEvent event = new DefaultScheduleEvent(descr,from,to,"mealsHome");
             		event.setData(item.getId());
                 	eventModel.addEvent(event);
             	}
             	else if ("leasureHome".equals(type) && showLeasure == true)
             	{
             		DefaultScheduleEvent event = new DefaultScheduleEvent(descr,from,to,"leasureHome");
             		event.setData(item.getId());
                 	eventModel.addEvent(event);
             	}
             	else if ("orderHome".equals(type) && showOrder == true)
             	{
             		DefaultScheduleEvent event = new DefaultScheduleEvent(descr,from,to,"orderHome");
             		event.setData(item.getId());
                 	eventModel.addEvent(event);
             	}
             	else if ("morningContact".equals(type) && showMorning == true)
             	{
             		DefaultScheduleEvent event = new DefaultScheduleEvent(descr,from,to,"contactMorning");
             		event.setData(item.getId());
                 	eventModel.addEvent(event);
             	}
            	else if ("lunchContact".equals(type) && showLunch == true)
             	{
             		DefaultScheduleEvent event = new DefaultScheduleEvent(descr,from,to,"contactLunch");
             		event.setData(item.getId());
                 	eventModel.addEvent(event);
             	}
            	else if ("nightContact".equals(type) && showNight == true)
             	{
             		DefaultScheduleEvent event = new DefaultScheduleEvent(descr,from,to,"contactNight");
             		event.setData(item.getId());
                 	eventModel.addEvent(event);
             	}
                 	else if ("appoinmentPro".equals(type) && showAppointment == true)
                 	{
                 		DefaultScheduleEvent event = new DefaultScheduleEvent(descr,from,to,"appoinmentPro");
                 		event.setData(item.getId());
                     	eventModel.addEvent(event);
                 	}                    	
                 	else if ("todoPro".equals(type) && showToDoWork == true)
                 	{
                 		DefaultScheduleEvent event = new DefaultScheduleEvent(descr,from,to,"todoPro");
                 		event.setData(item.getId());
                     	eventModel.addEvent(event);
                 	}
                 	else if ("meetingPro".equals(type) && showMeeting == true)
                 	{
                     		DefaultScheduleEvent event = new DefaultScheduleEvent(descr,from,to,"meetingPro");
                     		event.setData(item.getId());
                         	eventModel.addEvent(event);
                 	}
             	
                
         		
         	}
        	
        	
        }
   
 //--------Friend Methods
        public boolean AddFriendValid()
        {
        	
        	
        	
        	return true;
        }
        
        
     	public void addFriend(){
     		FacesContext context = FacesContext.getCurrentInstance();  
     		
    		if (newFriend.getId() != null)
    		{
    			manager.updateFriendUser(newFriend);
    			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Contact Updated: ",  newFriend.getFirstname() + "," + newFriend.getLastname()) );
            	
    		}
    		else
    		{
     		newFriend.setUrl("");
     		newFriend.setImg("./images/localContact.png");
     		manager.addFriendUser(newFriend);	
     		
     		          	
        	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Contact Created: ",  newFriend.getFirstname() + "," + newFriend.getLastname()) );
        	   
     		
    		}
    		
    		newFriend = new FriendUsers();
    		
    		 		
    	}
     	
     	public void findFriendId()
     	{
           	String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("friendid");
        	Long itemId = Long.parseLong(id);
        	
        	friendId = itemId;
     		
     	}
     	
     	public void findFriend(){
     		

        	
        	for(int i = 0; i < friends.size(); i ++)
        	{
        		if(friendId.equals(friends.get(i).getId()))
        		{
        			newFriend = friends.get(i);
        			break;
        		}
        	}
     	}
     	
     	public void newFriend(){     		

        			newFriend = new FriendUsers();        			
     	}
     	
     	
     	public void getFriendList(){     		

     		friends = service.getFriendUserList();        			
     	}
        
     	
        public void findTask(){
        	
        	String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        	Long itemId = Long.parseLong(id);         
            
            
            for (int i = 0; i< items.size(); i ++)
            {
            	if(itemId.equals(items.get(i).getId()))
            	{
            		scheduleItem = items.get(i);

            		break;
            	}
            }
            
            droppedFriends = new ArrayList<FriendUsers>();
            String withFriends = scheduleItem.getInvolvedFriends();
            List<Long> friendIds = new ArrayList<Long>();
            
            if (withFriends!=null)
            {
            StringTokenizer st2 = new StringTokenizer(withFriends, ",");
            
    		while (st2.hasMoreElements()) {
    			
    			friendIds.add(Long.valueOf(st2.nextElement().toString()).longValue());			
    			
    		}
    		
    		for (int i =0; i<friends.size();i++ )
    		{
    			if (friendIds.contains(friends.get(i).getId()))
    			{
    				droppedFriends.add(friends.get(i));
    			}
    		}
           }
                 
      	  pdfDisabled = false;  		
  		  deleteDisabled = false;
 
        	
        }
  
//------------Task Methods
        
        public ScheduleItem getScheduleItem(){
        	
        	return this.scheduleItem;
        	
        }
        
        public void findTask(String id){  
        	
        	items = manager.getScheduleItems();
        	Long itemId = Long.parseLong(id);            
            
            for (int i = 0; i< items.size(); i ++)
            {
            	if(itemId.equals(items.get(i).getId()))
            	{
            		scheduleItem = items.get(i);
            		break;
            	}
            }
            
            droppedFriends = new ArrayList<FriendUsers>();
            String withFriends = scheduleItem.getInvolvedFriends();
            List<Long> friendIds = new ArrayList<Long>();
            
            if (withFriends!=null)
            {
            StringTokenizer st2 = new StringTokenizer(withFriends, ",");
            
    		while (st2.hasMoreElements()) {
    			
    			friendIds.add(Long.valueOf(st2.nextElement().toString()).longValue());			
    			
    		}
    		
    		for (int i =0; i<friends.size();i++ )
    		{
    			if (friendIds.contains(friends.get(i).getId()))
    			{
    				droppedFriends.add(friends.get(i));
    			}
    		}
           }
        
        	userContext.setTaskID(null);
        	  pdfDisabled = false;  		
      		  deleteDisabled = false;
        }
        
        
        
        
        
        public void createPDF(){
        	
        	List<String> friendList = new ArrayList<String>();
        	
        	if (droppedFriends.size() > 0 )
        	{
        		for (int i = 0; i < droppedFriends.size(); i++)
        		{
        			friendList.add(droppedFriends.get(i).toString());        			
        		}
        	}
        	
        	SimpleTaskConvert.convert(scheduleItem, friendList);
        	
        }
         
        public boolean AddTaskValidation ()
        {
        	boolean valid = true;
        	if (scheduleItem.getName() == null || scheduleItem.getType()==null || scheduleItem.getStartDate() == null || scheduleItem.getEndDate() == null)
    		{    		      	
    		      valid = false;
    		}
    			    	

    		return valid;
        }
        
        	public void AddEvent() {
        		
        	FacesContext context = FacesContext.getCurrentInstance();
        		
        	if(AddTaskValidation ())
        	{
        	
        	String involvedFriends = "";
        	
        	if (droppedFriends.size() > 0 )
        	{
        		for (int i = 0; i < droppedFriends.size(); i++)
        		{
        			involvedFriends = involvedFriends + droppedFriends.get(i).getId().toString() + ",";        			
        		}
        	}
        	 
            if (!"".equals(involvedFriends))
            scheduleItem.setInvolvedFriends(involvedFriends.substring(0, involvedFriends.length()-1));
            
            if(scheduleItem.getId() == null)
            {
            scheduleItem.setId(null);
            if(scheduleItem.getReminder() ==true)
            {
            	
            	manager.addScheduleItemWithReminder(scheduleItem);
            }
            else
            {
            	manager.addScheduleItem(scheduleItem); 
            }
            
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Schedule Item Created: ",  scheduleItem.getName()) );
        	  pdfDisabled = false;  		
      		  deleteDisabled = false;
      		  
            }
            else
            {
            	if(scheduleItem.getReminder() ==true)
                {
                  	manager.updateScheduleItemWithReminder(scheduleItem);
                }
                else
                {
                	manager.updateScheduleItem(scheduleItem); 
                }
            	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Schedule Item Updated: ",  scheduleItem.getName()) );
            	  pdfDisabled = false;  		
          		  deleteDisabled = false;
            }
            event = new DefaultScheduleEvent();     
                        

        	}
        	else
        	{
        	
        		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Missing Required Field: ",  "Task Title , Category , Start and Finish Time" ));
        		
        	}
        		
        		
        }
        	
      
        public void newTask()
        {
        	
        	scheduleItem = new ScheduleItem();
        	droppedFriends = new ArrayList<FriendUsers>();
        	friends = service.getFriendUserList();
        	  pdfDisabled = true;  		
      		  deleteDisabled = true;
        	
        }
        
        public void deleteTask()
        {
        	
        	manager.deleteScheduleItem(scheduleItem);        	
        	newTask();
        	
        }
        	
        	
//---------------Calendar Funcs
        	
        public void onEventSelect(SelectEvent selectEvent) {      	
        	
            event = (ScheduleEvent) selectEvent.getObject();
            Long itemId = (Long) event.getData();           
            
            for (int i = 0; i< items.size(); i ++)
            {
            	if(itemId.equals(items.get(i).getId()))
            	{
            		scheduleItem = items.get(i);
            		break;
            	}
            }
            
            //Initialize again
            event = new DefaultScheduleEvent();
            
            droppedFriends = new ArrayList<FriendUsers>();
            String withFriends = scheduleItem.getInvolvedFriends();
            List<Long> friendIds = new ArrayList<Long>();
            
            if (withFriends!=null)
            {
            StringTokenizer st2 = new StringTokenizer(withFriends, ",");
            
    		while (st2.hasMoreElements()) {
    			
    			friendIds.add(Long.valueOf(st2.nextElement().toString()).longValue());			
    			
    		}
    		
    		for (int i =0; i<friends.size();i++ )
    		{
    			if (friendIds.contains(friends.get(i).getId()))
    			{
    				droppedFriends.add(friends.get(i));
    			}
    		}
           }
            
            pdfDisabled = false;  		
    		  deleteDisabled = false;
        }
       
        public void onDateSelect(SelectEvent selectEvent) {
        	
            scheduleItem.setStartDate((Date) selectEvent.getObject());
            scheduleItem.setEndDate((Date) selectEvent.getObject());
        }
        
        public void onFriendDrop() {
        	
        	String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("friendid");
        	Long itemId = Long.parseLong(id);
        	
        	FriendUsers friend = new FriendUsers();
        	for(int i =0; i < friends.size(); i++)
        	{
        		if (itemId.equals(friends.get(i).getId()))
        		{
        			friend = friends.get(i);
        			break;
        		}
        	}
        	if (!droppedFriends.contains(friend))
        	{
        	droppedFriends.add(friend); 
        	}       	
        
        }
        	
        
        public void onFriendRemove() {
        	
        	String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("friendid");
        	Long itemId = Long.parseLong(id);
        	
        	for(int i =0; i < droppedFriends.size(); i++)
        	{
        		if (itemId.equals(droppedFriends.get(i).getId()))
        		{
        			droppedFriends.remove(i);
        		}
        	}
        	
        }
         
        public void setService(FriendUserProducer service) {
            this.service = service;
        }
     
        public List<FriendUsers> getFriends() {
            return friends;
        }
     
        public List<FriendUsers> getDroppedFriends() {
            return droppedFriends;
        }    
         
        public ScheduleModel getEventModel() {
            return eventModel;
        }
         
        public ScheduleEvent getEvent() {
            return event;
        }
     
        public void setEvent(ScheduleEvent event) {
            this.event = event;
        } 
        
        public void onEventMove(ScheduleEntryMoveEvent event) {
        	
        	Date start = event.getScheduleEvent().getStartDate();
        	Date end = event.getScheduleEvent().getEndDate();
        	
            FacesMessage message = 
            		new FacesMessage(FacesMessage.SEVERITY_INFO, "Schedule Time Updated", "") ;
            addMessage(message);
            message = 
            		new FacesMessage(FacesMessage.SEVERITY_INFO, "Old Time:",             				 
            		dateFormat.format(scheduleItem.getStartDate()).toString() + " - " + 
            		dateFormat.format(scheduleItem.getEndDate()).toString());            
            addMessage(message);
            message = 
            		new FacesMessage(FacesMessage.SEVERITY_INFO, "Time Time:", 
            		dateFormat.format(event.getScheduleEvent().getStartDate()) + " - " + 
            		dateFormat.format(event.getScheduleEvent().getEndDate()) ) ;
            addMessage(message);
            
            scheduleItem.setStartDate(start);
            scheduleItem.setEndDate(end);
        }
         
        public void onEventResize(ScheduleEntryResizeEvent event) {
        	Date start = event.getScheduleEvent().getStartDate();
        	Date end = event.getScheduleEvent().getEndDate();
        	
            FacesMessage message = 
            		new FacesMessage(FacesMessage.SEVERITY_INFO, "Schedule Time Updated", "") ;
            addMessage(message);
            message = 
            		new FacesMessage(FacesMessage.SEVERITY_INFO, "Old Time:",             				 
            		dateFormat.format(scheduleItem.getStartDate()).toString() + " - " + 
            		dateFormat.format(scheduleItem.getEndDate()).toString());
            addMessage(message);
            message = 
            		new FacesMessage(FacesMessage.SEVERITY_INFO, "Time Time:", 
            		dateFormat.format(event.getScheduleEvent().getStartDate()) + " - " + 
            		dateFormat.format(event.getScheduleEvent().getEndDate()) ) ;
            addMessage(message);
            
            scheduleItem.setStartDate(start);
            scheduleItem.setEndDate(end);
        }
         
        private void addMessage(FacesMessage message) {
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

		public boolean isShowMeeting() {
			return showMeeting;
		}

		public void setShowMeeting(boolean showMeeting) {
			this.showMeeting = showMeeting;
		}

		public boolean isShowToDoWork() {
			return showToDoWork;
		}

		public void setShowToDoWork(boolean showToDoWork) {
			this.showToDoWork = showToDoWork;
		}

		public boolean isShowAppointment() {
			return showAppointment;
		}

		public void setShowAppointment(boolean showAppointment) {
			this.showAppointment = showAppointment;
		}

		public List<FriendUsers> getFilteredFriends() {
			return filteredFriends;
		}

		public void setFilteredFriends(List<FriendUsers> filteredFriends) {
			this.filteredFriends = filteredFriends;
		}

		public boolean isShowMeals() {
			return showMeals;
		}

		public void setShowMeals(boolean showMeals) {
			this.showMeals = showMeals;
		}

		public boolean isShowOrder() {
			return showOrder;
		}

		public void setShowOrder(boolean showOrder) {
			this.showOrder = showOrder;
		}

		public boolean isShowLeasure() {
			return showLeasure;
		}

		public void setShowLeasure(boolean showLeasure) {
			this.showLeasure = showLeasure;
		}

		public boolean isShowMorning() {
			return showMorning;
		}

		public void setShowMorning(boolean showMorning) {
			this.showMorning = showMorning;
		}

		public boolean isShowLunch() {
			return showLunch;
		}

		public void setShowLunch(boolean showLunch) {
			this.showLunch = showLunch;
		}

		public boolean isShowNight() {
			return showNight;
		}

		public void setShowNight(boolean showNight) {
			this.showNight = showNight;
		}

		public boolean isSaveDisabled() {
			return saveDisabled;
		}

		public void setSaveDisabled(boolean saveDisabled) {
			this.saveDisabled = saveDisabled;
		}

		public boolean isCreateDisabled() {
			return createDisabled;
		}

		public void setCreateDisabled(boolean createDisabled) {
			this.createDisabled = createDisabled;
		}

		public boolean isPdfDisabled() {
			return pdfDisabled;
		}

		public void setPdfDisabled(boolean pdfDisabled) {
			this.pdfDisabled = pdfDisabled;
		}

		public Long getFriendId() {
			return friendId;
		}

		public void setFriendId(Long friendId) {
			this.friendId = friendId;
		}

		public boolean isDeleteDisabled() {
			return deleteDisabled;
		}

		public void setDeleteDisabled(boolean deleteDisabled) {
			this.deleteDisabled = deleteDisabled;
		}

    }