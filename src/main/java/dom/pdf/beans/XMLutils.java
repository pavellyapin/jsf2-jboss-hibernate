package dom.pdf.beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.myobjects.model.ScheduleItem;



public class XMLutils {
	
	public static DOMSource SingleTaskXML(ScheduleItem item , List<String> Friends)
	{
		Document document = null;
		try {
			document = createDocument("html");
			Element SingleTaskXML = document.getDocumentElement();
			
			//Current Date
			
			DateFormat currentdateFormat = new SimpleDateFormat("yyyy/MM/dd");
			DateFormat dateFormat = new SimpleDateFormat("EEE , MMM dd, hh:mm");
			Date date = new Date();

			
			Element head = document.createElement("head");
			SingleTaskXML.appendChild(head);
			
			Element currentDate = document.createElement("div");
			currentDate.setAttribute("style", "text-align:right");
			Text dateText = document.createTextNode(currentdateFormat.format(date));
			currentDate.appendChild(dateText);
			

			Element table = document.createElement("table");
			Element tr = document.createElement("tr");
			Element td1 = document.createElement("td");
			td1.setAttribute("style", "font-weight: bold");
			Text titleLabel = document.createTextNode("Title:");
			td1.appendChild(titleLabel);
			Element td = document.createElement("td");
			Text titleText = document.createTextNode(item.getName());
			td.appendChild(titleText);
			tr.appendChild(td1);
			tr.appendChild(td);
			
			Element locationtr = document.createElement("tr");
			Element locationlabeltd = document.createElement("td");
			locationlabeltd.setAttribute("style", "font-weight: bold");
			Text locationLabel = document.createTextNode("Location:");
			locationlabeltd.appendChild(locationLabel);
			Element locationtd = document.createElement("td");
			Text locationText = document.createTextNode(item.getLocation());
			locationtd.appendChild(locationText);
			locationtr.appendChild(locationlabeltd);
			locationtr.appendChild(locationtd);
			
			table.appendChild(tr);
			table.appendChild(locationtr);
			
			Element middletable = document.createElement("table");
			Element timetable = document.createElement("table");
			Element starttimetr = document.createElement("tr");
			Element startlabeltd= document.createElement("td");
			startlabeltd.setAttribute("style", "font-weight: bold");
			Text startlabel = document.createTextNode("Start Time:");
			startlabeltd.appendChild(startlabel);
			Element starttd = document.createElement("td");
			starttd.setAttribute("style", "margin-right: 15px");
			Text startText = document.createTextNode(dateFormat.format(item.getStartDate()).toString());
			starttd.appendChild(startText);
			starttimetr.appendChild(startlabeltd);
			starttimetr.appendChild(starttd);
			
			Element endstarttimetr = document.createElement("tr");
			Element endlabeltd= document.createElement("td");
			endlabeltd.setAttribute("style", "font-weight: bold");
			Text endlabel = document.createTextNode("Finish Time:");
			endlabeltd.appendChild(endlabel);
			Element endtd = document.createElement("td");
			Text endText = document.createTextNode(dateFormat.format(item.getEndDate()).toString());
			endtd.appendChild(endText);
			endstarttimetr.appendChild(endlabeltd);
			endstarttimetr.appendChild(endtd);
			
			timetable.appendChild(starttimetr);
			timetable.appendChild(endstarttimetr);
			
			
			
			Element friendList = document.createElement("div");
			friendList.setAttribute("style", "font-weight: bold;margin-left:50px");
			Text friendslabel = document.createTextNode("Involved Contacts:");
			friendList.appendChild(friendslabel);
			
			if(Friends.size() > 0)
			{		//Involved Friends
				
				for(int i = 0; i < Friends.size() ; i++)
				{
					Element friend = document.createElement("div");
					Text friendText = document.createTextNode(Friends.get(i));
					friend.appendChild(friendText);
					friendList.appendChild(friend);
				}				
				
			}
			
			Element middletr = document.createElement("tr");
			Element middletime= document.createElement("td");
			Element middlefriends= document.createElement("td");
			
			middlefriends.appendChild(friendList);
			middletime.appendChild(timetable);			
			middletr.appendChild(middletime);
			middletr.appendChild(middlefriends);
			middletable.appendChild(middletr);	
			
			//Name of Task
			Element notes = document.createElement("div");
			Text notesText = document.createCDATASection(item.getNotes());
			notes.appendChild(notesText);
			

				
			Element body = document.createElement("body");
			Element hr = document.createElement("hr");
			Element hr2 = document.createElement("hr");
			body.setAttribute("style", "font-family: 'Times New Roman', Times, serif");
			
			//Append All Elements to ROOT
			body.appendChild(currentDate);			
			body.appendChild(table);
			body.appendChild(hr);
			body.appendChild(middletable);
			body.appendChild(hr2);
			body.appendChild(notes);
			SingleTaskXML.appendChild(body);
	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		DOMSource doc = new DOMSource(document);

        return doc;
	}
	
	public static DocumentBuilder getDocumentBuilder() throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder;
    }
    
    public static Document createDocument(String rootElementName) throws Exception{
        DocumentBuilder builder = getDocumentBuilder(); 
        Document document = builder.newDocument();
        document.appendChild(document.createElement(rootElementName));
        return document;
    }

}
