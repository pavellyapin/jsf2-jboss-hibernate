package dom.money.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.myobjects.ejb.UserServiceBean;
import com.myobjects.model.ScheduleItem;
import com.myobjects.model.StockList;

import dom.money.beans.StockItem;
import dom.money.beans.StockService;
 
@Named
@ViewScoped
public class StockPortfolioView implements Serializable {
     
    private List<StockItem> stocks1;
    private List<StockItem> stocks2;
    private String port1Name;
    private String port2Name;
     
    private StockItem selectedStock;
     
	@Produces
	@Named
	StockList stockList1;
	
	@Produces
	@Named
	StockList stockList2;
	
	@Inject
	UserServiceBean manager;
     
    @PostConstruct
    public void init() {
    	
    	stockList1 = manager.getStockList().get(0);
    	stockList2 = manager.getStockList().get(1);
    	
    	port1Name = stockList1.getName();
    	port2Name = stockList2.getName();
    	
    	
    	try{
    	setStocks1(StockService.getStockItems(stockList1));
    	setStocks2(StockService.getStockItems(stockList2));
    	}
    	catch (IndexOutOfBoundsException exc)
    	{
    		
    	}
    }
 
    public void update()
    {
    	stockList1 = manager.updateStockList(stockList1);
    	stockList2 = manager.updateStockList(stockList2);
    	
    	port1Name = stockList1.getName();
    	port2Name = stockList2.getName();    	
    	
    }
 
    public StockItem getSelectedStock() {
        return selectedStock;
    }
 
    public void setSelectedStock(StockItem selectedStock) {
        this.selectedStock = selectedStock;
    }

	public List<StockItem> getStocks1() {
		return stocks1;
	}

	public void setStocks1(List<StockItem> stocks1) {
		this.stocks1 = stocks1;
	}

	public List<StockItem> getStocks2() {
		return stocks2;
	}

	public void setStocks2(List<StockItem> stocks2) {
		this.stocks2 = stocks2;
	}

	public String getPort1Name() {
		return port1Name;
	}

	public void setPort1Name(String port1Name) {
		this.port1Name = port1Name;
	}

	public String getPort2Name() {
		return port2Name;
	}

	public void setPort2Name(String port2Name) {
		this.port2Name = port2Name;
	}
}