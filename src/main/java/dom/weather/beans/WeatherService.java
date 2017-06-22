	package dom.weather.beans;

import dom.rss.Feed;
import dom.rss.RSSFeedParser;

	public class WeatherService {
	  public static WeatherItem getWeatherDetails() {
		  
		  WeatherItem weatherDetails = new WeatherItem();
		  
		  RSSFeedParser parser = new RSSFeedParser("http://www.rssweather.com/wx/ca/on/toronto/rss.php");
		  
		  Feed feed = parser.readFeed();	 
		 
		 String current = feed.getEntries().get(0).getDescription();
		 String morning = feed.getEntries().get(0).getDescription();
		 
		 weatherDetails.setCurrent(current);
	    	

	   
		  
		  return weatherDetails;

	  }
	} 
