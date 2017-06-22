package dom.beans;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

import com.myobjects.model.ScheduleItem;
import com.myobjects.producer.UserProducer;

@FacesValidator("taskValidator")
public class TaskValidator implements Validator{
	
	@Inject
    ScheduleView manager;

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String newname = (String) value;
		newname = newname.toLowerCase();
		
		ScheduleItem item = manager.getScheduleItem();
		
		if (item.getName() == null || item.getType()==null || item.getStartDate() == null || item.getEndDate() == null)
		{
			FacesMessage msg =
		              new FacesMessage(" Required Field is Missing",
		              "Please enter : Task Title , Category , Start and Finish Time");
		      msg.setSeverity(FacesMessage.SEVERITY_WARN);	      
		      
			throw new ValidatorException(msg);
		}
			
	}


}
