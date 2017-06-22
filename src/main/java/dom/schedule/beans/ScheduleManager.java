package dom.schedule.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.myobjects.ejb.UserServiceBean;
import com.myobjects.model.ScheduleItem;

@Named
@ViewScoped
public class ScheduleManager implements Serializable {
	
	@Inject
	UserServiceBean ejb;
	
	@Produces
	@Named
	ScheduleItem newScheduleItem;
	
    @PostConstruct
    public void init() {
    	newScheduleItem = new ScheduleItem();
    
    }
    
 	public void addScheduleItem(){	
		
 		ejb.addScheduleItem(newScheduleItem);	
	}

}
