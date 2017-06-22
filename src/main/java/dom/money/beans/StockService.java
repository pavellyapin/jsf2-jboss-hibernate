	package dom.money.beans;

	import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.myobjects.model.StockList;

import dom.rss.FeedMessage;
import dom.rss.RSSFeedParser;
import dom.rss.Feed;

	public class StockService {
	  public static List<StockItem> getStockItems(StockList list) {
		  
		  List<StockItem> stockList = new ArrayList<StockItem>();
		  String url  = "http://www.nasdaq.com/aspxcontent/NasdaqRSS.aspx?data=quotes&symbol=" 
		  + list.getStock1() 
		  + "&symbol=" + list.getStock2()
		  + "&symbol=" + list.getStock3()
		  + "&symbol=" + list.getStock4()
		  + "&symbol=" + list.getStock5()
		  + "&symbol=" + list.getStock6()
		  + "&symbol=" + list.getStock7()
		  + "&symbol=" + list.getStock8()
		  + "&symbol=" + list.getStock9()
		  + "&symbol=" + list.getStock10();
		  
		  RSSFeedParser parser = new RSSFeedParser(url);
		  
		  Feed feed = parser.readFeed();
		  
		 FeedMessage msg =  feed.getEntries().get(0);
		 
		 String descr = msg.getDescription();			
		   	    	
	    	Document doc = Jsoup.parse(descr);   	
	    	
	    	Elements allTables = doc.getElementsByTag("table");    	
	    	
	    	
	    	for (int i = 0; i < allTables.size()/2 ; i ++)
	    	{
	    		StockItem stock = new StockItem();
	    		
	    	    Elements stockDetails = allTables.get(i*2).getElementsByTag("td");
	    	    
	    	    
	    	    if (stockDetails.size() > 2)	    
	    	   {
	    	    Elements imgE = stockDetails.get(4).getElementsByTag("img");
			    	    if(imgE.size() > 0)
			    	    {
			    	    Element id = stockDetails.get(0).getElementsByTag("a").get(0);	 
			    	    String price = stockDetails.get(2).text();
			    	    Element img = stockDetails.get(4).getElementsByTag("img").get(0);
			    	    String imgString = img.attr("src");
			    	    String change = stockDetails.get(4).text();
			    	    String prec = stockDetails.get(6).text();
			    	    String volume = stockDetails.get(8).text();
			    	    stock.setSymbol(id.text());
			    	    stock.setUrl(id.attr("href"));
			    	    stock.setPrice(price);
			    	    stock.setSign(change + " " + prec);
			    	    stock.setVolume(volume);
			    	    stock.setImg(imgString);
			    	    }
			    	    else
			    	    {
			    	    	Element id = stockDetails.get(0).getElementsByTag("a").get(0);	 
				    	    String price = stockDetails.get(2).text();		    	    
				    	    String volume = stockDetails.get(8).text();
				    	    stock.setSymbol(id.text());
				    	    stock.setPrice(price);
				    	    stock.setSign("UNAVAILABLE");
				    	    stock.setVolume(volume);
				    	    stock.setImg("http://www.nasdaq.com/images/redarrowsmall.gif?pfdrid_c=true");
			    	    }
	    	    }
	    	    else
	    	    {
	    	       stock.setSymbol("NOT FOUND");
	    	       stock.setImg("http://www.nasdaq.com/images/redarrowsmall.gif?pfdrid_c=true");
	    	    }
	    	    
	    	    stockList.add(stock);
	    		
	    	}
	   
		  
		  return stockList;

	  }
	} 
