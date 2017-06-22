package dom.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;


public class quiz {
	

	/**
	 * Question 1:
	 * 
	 * Create the hashCode and equals method for the following class.
	 */
	private static class MyObject {
		
		private String name;
		private Integer quality;
		private final int MAXIMUM = 23;
		
		@Override
		public boolean equals(Object obj)
		{				
							if(this == obj)
								return true;
							if((obj == null) || (obj.getClass() != this.getClass()))
								return false;
							// object must be MyObject at this point
							MyObject test = (MyObject)obj;
							return quality == test.quality &&
							(name == test.name || (name != null && name.equals(test.name)));			 	
			
		}
		
		@Override
		public int hashCode(){
		
			 final int prime = 31;
			    int result = 1;
			    result = prime * MAXIMUM + (int) (quality ^ (name.hashCode() >>> 32));
			    result = prime * MAXIMUM + (int) (quality ^ (name.hashCode() >>> 32));
			    result = prime * MAXIMUM + (int) (quality ^ (name.hashCode() >>> 32));
			    return result;
		    
		  }		
	}
	
	
	/**
	 * Question 2:
	 * 
     * This method was written for the purpose of removing all nulls from the given list.
	 *
	 * Improve this method as much as possible, while maintaining the method signature and the 
	 * original purpose.
	 * 
	 * Hint: The input list may very, very large.
	 */
	public void clean(LinkedList<Integer> f) {

	    boolean nullIsThere = true;

		//Utilizing the removeFirstOccurrence(Object obj) method 
		while (nullIsThere)
		{
			synchronized(f)
			{
			nullIsThere = f.removeFirstOccurrence(null);
			}
			
		}	
		
	}
	
	/**
	 * Question 3:
	 * 
	 * Simplify the 'check' method as much as possible, while maintaining the method signature and current functionality.
	 * Notes: 
	 * 		- x,y,z are always guaranteed to be > 0.
	 
	 */
	 //the method w(int x, int z) returns the multiply of x and z 
	 //Since all values are greater then 0 , x < w(x,z) results in false only when z = 1
	 //The right hand side will be simplified to: z > 1 && y > z
	 //The left hand side will be simplified to: z > 1 && y < z
	 //We can then conclude that as long as z > 1 and z not equal y, the if statement will return true.
	 
	public boolean check(int x, int y, int z) {
		//if (x < w(x,z) && y < z || x < w(x,z) && y > z && z < y) {
		//The simplified version will return the same result as commented one
		if (z > 1 && y != z) {
			return true;
		}
		return false;
	}
	private int w(int x, int z) {
		int f = 0;
		for (int i = 0; i < z; i++) {
			f += x;
		}
		return f;
	}
	
	
	/**
	 * Question 4
	 * 
	 * A task uses a master thread (not shown here) to share the work of scanning DNA among multiple threads. 
	 * The master thread invokes the scanDNA method (below) on all of the other threads at once, with each
	 * thread provided with its own private share of the work (the dnaList, individual to each thread).  
	 * 
	 * Each thread shares the common 'results' object as a way to report the progress of the scan to the master
	 * thread of the task.  It is very important for the task that the total count of the successful scans is 
	 * always accurate.
	 * 
	 * Improve the overall performance of the task by modifying the scanDNA method below and fix any defects.
	 */
	private interface DNA {
		public boolean scan();
		
	}
	public void scanDNA(List<DNA> dnaList, Properties results) {
		
		//following naming convention for property names
		final String key = "successful_scans";
		
			Iterator<DNA> i = dnaList.iterator();
			
			while (i.hasNext()) {
				
				if (i.next().scan()) {					
					//Only lock the variable when update is required
					synchronized (results)					
					{
						results.setProperty(key,String.valueOf(Integer.valueOf(results.getProperty(key)) + 1));
					}
				}
			}
		
	}
	

	
	
	
	/**
	 * Question 5
	 * 
	 * This method is supposed to return the number of 'active' employees that live in a city with a given name.
	 * Please correct all issues (both logic and performance issues) with the following method, 
	 * without changing the signature.
	 * 
	 * Notes:
	 * There are two tables used here: EMPLOYEE and CITY.
	 * The columns are:
	 * 		EMPLOYEE.EMP_ID			The employee identifier.  Primary key.
	 * 		EMPLOYEE.CITY_ID		The city where the employee lives.  A foreign key to the CITY table.
	 * 		EMPLOYEE.EMP_ACTIVE		Contains "Y" if and only if the employee is 'active'. 
	 * 		CITY.CITY_ID			Contains the primary key of the city.
	 * 		CITY.CITY_NAME			The name of the city, unique.
	 * 
	 * All columns are separately indexed.
	 * 
	 */
	//Answer
	//My Vesrion: Pavel Lyapin
	//Move more of the processing to the database side, add try catch block with SQLException, closing connection to DB
	
		public int countActiveEmployeesInCity(Connection c, String city) throws SQLException {
    		
    		
    		ResultSet rs = null;
    		try{
    		//Will return a list of unique employee IDs of employees living in the given named city
    		PreparedStatement ps = 	c.prepareStatement("SELECT DISTINCT EMP_ID FROM EMPLOYEE E INNER JOIN CITY C ON E.CITY_ID = C.CITY_ID WHERE C.CITY_NAME = ? AND E.EMP_ACTIVE = 'Y'");
    		ps.setString(1, city);
    		rs = ps.executeQuery();
    		} catch (SQLException e)
    		{
    			System.out.println(e.getMessage());
    		}
    		finally{
    			//Closing connection to database
    			c.close();
    		}
    		
    		int numActive = 0;
    		
    		if (rs != null)
    		{
    			while (rs.next()) {    				
    					numActive++;    				
    			} 
    		}
    		
    		return numActive;
    	}
	//--------------------------------------------------------------------------------------
	/**
	 * Question 6
	 * 
	 * Improve the following method as much as possible (without modifying the method signature).  
	 * Ensure that it returns true if and only if both arrays contain the given value.
	 *  
	 * Note: The lists may be null and may contain null values.
	 */
	//Answer:Pavel Lyapin
	//Verifying none of the arrays is NULL , utilizing ArrayList contains(Object obj) method
	
	    	public boolean isInBoth(String value, String[] arrayA, String[] arrayB) {
    		
    		boolean found = false;
    		   		
    		List<String> arrayAlist = new ArrayList<String>();
    		List<String> arrayBlist = new ArrayList<String>();
    		
    		if (arrayA == null || arrayB == null)
    		{
    			return found;
    		}
    		else
    		{
    			arrayAlist = Arrays.asList(arrayA);
        		arrayBlist = Arrays.asList(arrayB);
        		
        		if (arrayAlist.contains(value) && arrayBlist.contains(value))
        		{
        			found = true;
        		}
    		}   		
    		    		
    		return found;
    	}
	
	
}