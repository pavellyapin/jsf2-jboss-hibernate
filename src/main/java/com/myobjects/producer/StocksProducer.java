package com.myobjects.producer;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.session.bean.UserContext;
import com.myobjects.model.StockList;
import com.myobjects.repository.UserRepositoryManager;

 
@RequestScoped
public class StocksProducer {
	
	@Inject UserRepositoryManager db;
	
	@Inject UserContext session;

	private List<StockList> stockList;
	
	@Produces
	@Named
	public List<StockList> getStockList() {
		
		stockList = db.getStocks(session.getRegisteredUser().getUsername());		
		return stockList;
	}

	public void setStockList(List<StockList> stockList) {
		this.stockList = stockList;
	}
	

}
