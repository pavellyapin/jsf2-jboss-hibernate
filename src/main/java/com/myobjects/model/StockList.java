package com.myobjects.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class StockList {
 
	
    @Id
    @GeneratedValue
    private Long id;
    
    private String registeredUserid;
    private String name;
    private String stock1;     
    private String stock2;    
    private String stock3;
    private String stock4;
    private String stock5;
    private String stock6;
    private String stock7;
    private String stock8;
    private String stock9;
    private String stock10;
 
   	public String getRegisteredUser() {
		return registeredUserid;
	}

	public void setRegisteredUser(String registeredUser) {
		this.registeredUserid = registeredUser;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStock1() {
		return stock1;
	}

	public void setStock1(String stock1) {
		this.stock1 = stock1;
	}

	public String getStock2() {
		return stock2;
	}

	public void setStock2(String stock2) {
		this.stock2 = stock2;
	}

	public String getStock5() {
		return stock5;
	}

	public void setStock5(String stock5) {
		this.stock5 = stock5;
	}

	public String getStock4() {
		return stock4;
	}

	public void setStock4(String stock4) {
		this.stock4 = stock4;
	}

	public String getStock3() {
		return stock3;
	}

	public void setStock3(String stock3) {
		this.stock3 = stock3;
	}

	public String getStock6() {
		return stock6;
	}

	public void setStock6(String stock6) {
		this.stock6 = stock6;
	}

	public String getStock7() {
		return stock7;
	}

	public void setStock7(String stock7) {
		this.stock7 = stock7;
	}

	public String getStock8() {
		return stock8;
	}

	public void setStock8(String stock8) {
		this.stock8 = stock8;
	}

	public String getStock9() {
		return stock9;
	}

	public void setStock9(String stock9) {
		this.stock9 = stock9;
	}

	public String getStock10() {
		return stock10;
	}

	public void setStock10(String stock10) {
		this.stock10 = stock10;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



    
}
