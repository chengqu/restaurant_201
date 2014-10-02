package restaurant;

import agent.Agent;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class WaiterAgent extends Agent implements Waiter{
	
	public List<MyCustomer> Customers
	= new ArrayList<MyCustomer>();
	private Menu menu = new Menu();
	private boolean origin = false;
	private boolean atcook = false;
	private boolean served = false;
	private String name;
	public int seatnum;
	private Semaphore atTable = new Semaphore(0,true);
	private CookAgent Cook;
	public WaiterGui waiterGui = null;
	public HostAgent host;
	public CashierAgent cashier;
	public enum CustomerState{DoingNothing,Waiting, Seated, ReadyToOrder, Asked,Ordered, BeingServed,Served, Leaving, Eating, WaitingForfood, Reorder,ReadyToPay, Paying, ReadyToSeat};
	private boolean isBreak = false;
	private boolean atCashier = false;
	private boolean atCustomer = false;
	public WaiterAgent(String name) {
		super();

		this.name = name;
		// make some tables
		
		
	}
	public void setCashier(CashierAgent c){
		this.cashier = c;
	}
	public void setHost(HostAgent h){
		this.host = h;
	}
	public void setCook(CookAgent cook){
		this.Cook = cook;
	}
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public List getMyCustomers() {
		return Customers;
	}

	public void msgSeatCustomer(CustomerAgent c, int table){
		Do("msgSeatCustomer");
		Customers.add(new MyCustomer(c,table,CustomerState.Waiting));
		stateChanged();
	}
	public void msgIWantFood(CustomerAgent c) {
		Do("msgIWantFood");
		for(MyCustomer mycust : Customers){
			if(mycust.getCust() == c)
			{
				MyCustomer mc = mycust;
				mc.s = CustomerState.ReadyToOrder;
			}
				}
		stateChanged();
	}
	
	public void msgHereIsMyOrder(CustomerAgent c, String Choice){
		Do("HereIsMyOrder");
		for(MyCustomer mycust : Customers){
			if(mycust.getCust() == c)
			{
				MyCustomer mc = mycust;
				mc.Choice = Choice;
				mc.s = CustomerState.Asked;
				seatnum = mc.table;
			}
				}
		stateChanged();
	}
	
	public void msgFoodReady(String Choice, int table){
		Do("msgFoodReady");
		for(MyCustomer mycust : Customers){
			if(mycust.table == table && mycust.Choice == Choice){
				mycust.s = CustomerState.BeingServed;
			}
		}
		stateChanged();
	}
	public void msgIWantToPay(CustomerAgent c){
		Do("msgIWantToPay");
		for(MyCustomer mycust : Customers){
			if(mycust.getCust() == c){
				mycust.s = CustomerState.Paying;
			}
		}
		stateChanged();
	}
	public void msgLeavingTable(CustomerAgent c) {
		Do("msgLeavingTable");
		for(MyCustomer mycust : Customers){
			if(mycust.getCust() == c)
			{
				mycust.s = CustomerState.Leaving;
			}
		}
		
				stateChanged();
	}
	
	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}
	public void msgIsorigin(){
		origin = true;
		stateChanged();
	}
	public void msgAtCook(){
		atcook = true;
		stateChanged();
	}
	public void msgAtCashier(){
		atCashier = true;
		stateChanged();
	}
	public void msgAtCustomer(){
		atCustomer = true;
		stateChanged();
	}
	public void msgOutOfFood(String Choice, int table){
		Do("msgOutOfFood");
		for(MyCustomer mycust : Customers){
			if(mycust.table == table && mycust.Choice.equals(Choice)){
				menu.menu.remove(Choice);
				mycust.s = CustomerState.Reorder;
			}
		}
		stateChanged();
	}
	public void msgOnBreak(){
		isBreak = true;
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		try{
		for(MyCustomer mycust : Customers){
			if(mycust.s == CustomerState.Leaving && atCashier == true){
				atCashier = false;
			NotifyHost(mycust);
			return true;
			}
		}
		
		for(MyCustomer mycust : Customers) {
				if (mycust.s == CustomerState.Waiting && origin == true) {
					origin = false;
					PickCustomer(mycust);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		
		for(MyCustomer mycust : Customers) {
			if (mycust.s == CustomerState.ReadyToSeat && atCustomer == true) {
				atCustomer = false;
				seatCustomer(mycust);//the action
				return true;//return true to the abstract agent to reinvoke the scheduler.
			}
		}
		
		for(MyCustomer mycust : Customers){
			if(mycust.s == CustomerState.ReadyToOrder && atcook == false){
			TakeOrder(mycust);
			return true;
			}
		}
		for(MyCustomer mycust : Customers){
			//System.out.println("!"+mycust.s);
			if(mycust.s == CustomerState.Reorder && atcook == true){
				atcook = false;
			ReOrder(mycust);
			return true;
			}
		}
		for(MyCustomer mycust : Customers){
			if(mycust.s == CustomerState.Ordered){
			WaitOrder(mycust);
			return true;
			}
		}
		for(MyCustomer mycust : Customers){
			if(mycust.s == CustomerState.Asked){
			GiveOrder(mycust);
			return true;
			}
		}
		
		for(MyCustomer mycust : Customers){
			if(mycust.s == CustomerState.WaitingForfood){
			WaitCook(mycust);
			return true;
			}
		}
		for(MyCustomer mycust : Customers ){
			if(mycust.s == CustomerState.BeingServed && atcook == true){
				atcook = false;
				ServeFood(mycust);
			return true;
			}
		}
		for(MyCustomer mycust : Customers){
			if(mycust.s == CustomerState.Paying){
			TakeCheck(mycust);
			return true;
			}
		}
		if(Customers.size() == 0 && origin == true && isBreak == true ){
			isBreak = false;
			isOnBreak();
			return true;
		}
		}catch(ConcurrentModificationException e){
			return false;
		}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	private void PickCustomer(MyCustomer c){
		Do("PickingUpCustomer");
		waiterGui.DoGoToCustomer();
		c.c.msgReadyToSeat();
		c.s = CustomerState.ReadyToSeat;
	}
	private void seatCustomer(MyCustomer c) {
		Do("seatcustomer");
		waiterGui.DoBringToTable(c.c,c.table); 
		c.c.msgFollowMe(new Menu(),c.table,this);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiterGui.DoLeaveCustomer();
		c.s = CustomerState.Seated;
		
	}

	// The animation DoXYZ() routines
	private void TakeOrder(MyCustomer c){
		Do("TakingOrder");
		waiterGui.DoBringToTable(c.c,c.table); 
		c.c.msgWhatDoYouWant();
		c.s = CustomerState.Ordered;
	}
	
	private void WaitOrder(MyCustomer c){
		waiterGui.DoBringToTable(c.c,c.table); 
	}
	private void GiveOrder(MyCustomer c){
		Do("Giving the Order");
		
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiterGui.DoGoToCook();
		c.s = CustomerState.WaitingForfood;
		Cook.msgCookOrder(this, c.Choice, c.table);
	}
	private void WaitCook(MyCustomer c){
		waiterGui.DoGoToCook();
		
	}
	private void ReOrder(MyCustomer c){
		Do("Let Customer Reorder");
		waiterGui.DoBringToTable(c.c, c.table);
		c.c.msgReorder(menu);
		c.s = CustomerState.Ordered;
	}
	
	private void ServeFood(MyCustomer c){
		Do("Serving food");
		waiterGui.msgshowOrder(c.Choice);
		waiterGui.DoBringToTable(c.c, c.table);
		System.out.println(c.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.c.msgHereIsTheFood();
		c.s = CustomerState.Eating;
		waiterGui.DoLeaveCustomer();
		waiterGui.msghideOrder();
	}
	private void TakeCheck(MyCustomer c){
		Do("Taking The Check");
		waiterGui.DoBringToTable(c.c, c.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiterGui.DoGoToCashier();
		cashier.msgComputeCheck(this, c.c, c.Choice);
		c.s = CustomerState.Leaving;
	}
	private void NotifyHost(MyCustomer c){
		Do("NotifyHost");
		waiterGui.DoLeaveCustomer();
		c.c.msgGoToPay();
		host.msgLeavingTable(c.table);
		c.s = CustomerState.DoingNothing;
		Customers.remove(c);
	}
	public void IWantBreak(){
		host.msgWaiterOnBreak(this);
	}
	public void OffBreak(){
		
		host.msgWaiterOffBreak(this);
	}
	private void isOnBreak(){
		waiterGui.setWaiterEnabled();
	}
	//utilities

	public void setGui(WaiterGui gui,int count) {
		waiterGui = gui;
		waiterGui.setNumber(count);
	}

	public WaiterGui getGui() {
		return waiterGui;
	}
	public void setFood(){
		Cook.setRunOutOfFood();
	}
	private class MyCustomer {
		CustomerAgent c;
		int table;
		String Choice;
		CustomerState s;
		double Cash;
		
		MyCustomer(CustomerAgent c, int table, CustomerState s){
			this.c = c;
			this.table = table;
			this.s = s;
		}
		public CustomerAgent getCust(){
			return this.c;
		}
		
	}
		
}

