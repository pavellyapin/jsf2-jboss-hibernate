package com.session.bean;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

import com.myobjects.producer.UserProducer;

@FacesValidator("userValidator")
public class UserValidator implements Validator{
	
	@Inject
    UserProducer manager;

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String newname = (String) value;
		newname = newname.toLowerCase();
		
		List<String> usernames = manager.findUserNames();
		
		if (usernames.contains(newname) || newname.length() < 8  )
		{
			FacesMessage msg =
		              new FacesMessage(" Please Choose A Diferent User Name",
		              "Username must be at least 8 characters, and not exist in the system.");
		      msg.setSeverity(FacesMessage.SEVERITY_WARN);	      
		      
			throw new ValidatorException(msg);
		}
			
	}


}
