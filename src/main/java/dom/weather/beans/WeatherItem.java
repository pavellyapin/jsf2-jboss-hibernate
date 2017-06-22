package dom.weather.beans;

public class WeatherItem {

	private String current;
	
	private String pubdate;
	
	private String morning;
	
	private String afternoon;
	
	private String evening;
	
	private String overnight;

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public String getPubdate() {
		return pubdate;
	}

	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}

	public String getMorning() {
		return morning;
	}

	public void setMorning(String morning) {
		this.morning = morning;
	}

	public String getEvening() {
		return evening;
	}

	public void setEvening(String evening) {
		this.evening = evening;
	}

	public String getAfternoon() {
		return afternoon;
	}

	public void setAfternoon(String afternoon) {
		this.afternoon = afternoon;
	}

	public String getOvernight() {
		return overnight;
	}

	public void setOvernight(String overnight) {
		this.overnight = overnight;
	}
		
}
