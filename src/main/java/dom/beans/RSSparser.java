package dom.beans;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

 
@FacesConverter("rssParser")
public class RSSparser implements Converter {
 
	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object value) {
    	String description = (String)value.toString(); 
    	Document doc = Jsoup.parse(description);
    	String descr = null;
    	Elements body = doc.getElementsByTag("body");
    	if (body.size()!=0)
    	{	descr = body.get(0).text();
    	}
    	else
    	{
    		Element p = doc.getElementsByTag("p").get(0);
    		descr = p.text();
    	}
    	//String descr = description.substring(description.indexOf(".value=") + 7, description.indexOf("<img"));
      return descr;
	}   
}      