package dom.news.beans;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.session.bean.UserContext;

@Model
public class NewsManager {

	@Produces
	@Named
	String newsFeed;
	
	@Inject
    UserContext userSession;
	
    @PostConstruct
    public void init() {
    	if(newsFeed==null)
    	setNewsFeed("http://www.cbc.ca/cmlink/rss-world"); 
    	
    	userSession.getRegisteredUser().getUsername();
    }
  
    //The Economist    
    public void economistBusiness()   { setNewsFeed("http://www.economist.com/sections/business-finance/rss.xml");
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Economist Business Displayed", ""));
	}
    public void economistWorld()   { setNewsFeed("http://www.economist.com/sections/international/rss.xml");
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Economist World Displayed", ""));
	}
    public void economistTech()   { setNewsFeed("http://www.economist.com/sections/science-technology/rss.xml");
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Economist Tech Displayed", ""));
	} 
    
    //Reuters
    public void reutersWorld()    { setNewsFeed("http://feeds.reuters.com/Reuters/worldNews");
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Reuters World News Displayed", ""));
	}
    
    //CBC
    public void cbcWorld()   {setNewsFeed("http://www.cbc.ca/cmlink/rss-world");
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "CBC World News Displayed", ""));
	}    
    public void cbcPolitics()   { setNewsFeed("http://www.cbc.ca/cmlink/rss-politics");
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "CBC Politics Displayed", ""));
	}    
    public void cbcBusiness()    {setNewsFeed(" http://www.cbc.ca/cmlink/rss-business");
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "CBC Business Displayed", ""));
	}
    public void cbcHealth()    { setNewsFeed("http://www.cbc.ca/cmlink/rss-health");
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "CBC Health Displayed", ""));
	}
  
    //BBC
    public void bbcWorld()    {setNewsFeed("http://feeds.bbci.co.uk/news/world/rss.xml");
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "BBC World News Displayed", ""));
	}
  
    //Global
    public void globalWorld()   {setNewsFeed("http://globalnews.ca/world/feed/");
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Global World News Displayed", ""));
	}
    
    //RealClearPolitics
    public void clearPolitics()   {setNewsFeed("http://feeds.feedburner.com/realclearpolitics/qlMj");
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Real Clear Politics Displayed", ""));
	}
    
    //Washington Post
    public void washPost()   {setNewsFeed("http://feeds.washingtonpost.com/rss/politics");
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Washington Post Politics Displayed", ""));
	}
    
    //The Telegraph
    public void telegraphWorld()   {setNewsFeed("http://www.telegraph.co.uk/news/worldnews/rss");
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Telegraph World News Displayed", ""));
	}
    public void telegraphScience()   {setNewsFeed("http://www.telegraph.co.uk/news/science/science-news/rss");
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Telegraph Science Displayed", ""));
	}
    public void telegraphTech()   {setNewsFeed("http://www.telegraph.co.uk/technology/rss");
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Telegraph Technology Displayed", ""));
	}
    public void telegraphHealth()   {setNewsFeed("http://www.telegraph.co.uk/foodanddrink/foodanddrinkadvice/rss");
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Telegraph Health Displayed", ""));
	}
    public void telegraphEcon()   {setNewsFeed("http://www.telegraph.co.uk/finance/economics/rss");
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Telegraph Economics Displayed", ""));
	}

	public String getNewsFeed() {
		return newsFeed;
	}

	public void setNewsFeed(String newsFeed) {
		this.newsFeed = newsFeed;
	}    

}
