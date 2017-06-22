package dom.rss;

import java.util.ArrayList;
import java.util.List;

import dom.rss.FeedMessage;

public class Feed {

	private String title;
	private String link;
	private String description;
	private String language;
	private String copyright;
	private String pubDate;

	  private final List<FeedMessage> entries = new ArrayList<FeedMessage>();

	  public Feed(String title, String link, String description, String language,
	      String copyright, String pubDate) {
	    this.title = title;
	    this.link = link;
	    this.description = description;
	    this.language = language;
	    this.copyright = copyright;
	    this.pubDate = pubDate;
	  }

	  public List<FeedMessage> getMessages() {
	    return getEntries();
	  }

	  public String getTitle() {
	    return title;
	  }

	  public String getLink() {
	    return link;
	  }

	  public String getDescription() {
	    return description;
	  }

	  public String getLanguage() {
	    return language;
	  }

	  public String getCopyright() {
	    return copyright;
	  }

	  public String getPubDate() {
	    return pubDate;
	  }

	  @Override
	  public String toString() {
	    return "Feed [copyright=" + copyright + ", description=" + description
	        + ", language=" + language + ", link=" + link + ", pubDate="
	        + pubDate + ", title=" + title + "]";
	  }

	public List<FeedMessage> getEntries() {
		return entries;
	}

	} 