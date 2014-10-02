package restaurant;

import restaurant.CustomerAgent.AgentEvent;
import restaurant.WaiterAgent.CustomerState;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;
import agent.Agent;
import java.util.*;

public class CashierAgent extends Agent implements Cashier{
	private String name;
	private Menu m = new Menu();
	public double money = 100;
	public double loan;
	private Host host;
	public List<MyCustomer> customer =Collections.synchronizedList(new ArrayList<MyCustomer>());
	public enum CustomerState{Unpay,Paying, Paied, GoToPay, Owe};
	public enum BillState{Unpay,Paying,Paid, Owe};
	public List<Bill> bill = Collections.synchronizedList(new ArrayList<Bill>());
	public boolean rich = false;
	public boolean loanhere = false;
	private MyCustomer C = null;
	private Bill B = null;
	public CashierAgent(String name){
		super();
		this.name = name;
		this.loan = 0;
	}
	public void setHost(Host h){
		this.host = h;
	}
	public void msgComputeCheck(Waiter w,Customer c, String Choice){
		double price = 0;
		for(int i=0; i< m.menu.size(); i++){
			if(m.getName(i) == Choice){
				price  = m.getPrice(i);
			}
		}
		customer.add(new MyCustomer(w,c,Choice,CustomerState.Unpay, price));
		stateChanged();
	}
	
	public void msgPay(Customer c, double cash){
		for(MyCustomer mycust : customer){
			if(mycust.getCust() == c)
			{
				MyCustomer mc = mycust;
				mc.s = CustomerState.Paying;
				mc.setCash(cash);
			}
				}
		stateChanged();
	}
	public void msgMarketBill(Market m,double money){
		bill.add(new Bill(m,money,BillState.Unpay));
		stateChanged();
	}
	public void msgHereIsMoney(double money){
		this.money += money;
		loanhere = true;
		stateChanged();
	}
	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		MyCustomer C = null;
		Bill B = null;
		
		synchronized(customer){
			for(MyCustomer mycust: customer){
			if(mycust.s == CustomerState.Unpay){
				C = mycust;
				break;
			}
		}
	}
		
		if(C != null){
			ComputeCheck(C);
			return true;
		}
		
		synchronized(customer){
			for(MyCustomer mycust: customer){
			if(mycust.s == CustomerState.Paying){
				C = mycust;
				break;
			}
		}
		}
		
		if(C != null){
			GiveChange(C);
			return true;
		}
		
		synchronized(bill){
			for(Bill mybill:bill){
			if(mybill.s == BillState.Unpay){
				B = mybill;
				break;
				
			}
		}
		}
		
		if(B != null){
			PayMarket(B);
			return true;
		}
		
		synchronized(bill){
			for(Bill mybill:bill){
			if(mybill.s == BillState.Owe && loanhere == true){
				loanhere = false;
				B = mybill;
				break;
			}
		}
		}
		
		if(B != null){
			PayMarketAgain(B);
			return true;
		}
		
		
		
			if(rich == true){
				rich = false;
				PayDebt();
				return true;
		}
			
			return false;
	}
		
	
	private void ComputeCheck(MyCustomer c) {
		// TODO Auto-generated method stub
		Do("ComputingCheck");
		c.s = CustomerState.GoToPay;
		
	}
	private void GiveChange(MyCustomer c){
		double change = 0;
		if(c.Cash<c.check){
			c.c.YouOweUs(c.check -c.Cash);
			c.s = CustomerState.Owe;
		}
		else{
		money += c.check;
		change = c.Cash - c.check;
		c.setChange(change);
		c.c.msgGiveChange(c.Change);
		c.s = CustomerState.Paied;
		}
	}
	private void PayMarket(Bill b){
		if(money > b.money && (money - b.money) >loan){
		money -= b.money;
		b.m.msgPayMarket(b.money);
		b.s = BillState.Paid;
		rich = true;
		}
		else{
			b.s = BillState.Owe;
			loan = b.money - money;
			host.msgINeedMoney(loan);
		}
	}
	
	private void PayMarketAgain(Bill b){
		money -= b.money;
		b.m.msgPayMarket(b.money);
		b.s = BillState.Paid;
	}
	private void PayDebt(){
		money -= loan;
		host.msgPayDebt(loan);
	}
	
	public class MyCustomer{
		Waiter waiter;
		Customer c;
		public double check;
		String Choice;
		CustomerState s;
		public double Cash;
		public double Change;
		MyCustomer(Waiter w ,Customer c, String Choice, CustomerState s,double check){
			this.c = c;
			this.Choice = Choice;
			this.s = s;
			this.check = check;
			this.waiter = w;
		}
		public void setCash(double c){
			this.Cash = c;
		}
		public void setChange(double c){
			this.Change = c;
		}
		public Customer getCust(){
			return c;
		}
	}
	public class Bill{
		public Market m;
		public double money;
		BillState s;
		Bill(Market m,double money,BillState s){
			this.m = m;
			this.money = money;
			this.s = s;
		}
	}
	
}