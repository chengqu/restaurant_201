package restaurant;

import java.util.*;

import restaurant.CustomerAgent.AgentEvent;
import restaurant.interfaces.Market;



import agent.Agent;

public class MarketAgent extends Agent implements Market{
	
	String name;
	List<MyFood>food = Collections.synchronizedList(new ArrayList<MyFood>());
	CookAgent cook;
	CashierAgent cashier;
	Timer timer;
	private double money = 0;
	private enum FoodState{Pending, Sending, ReadyToSend, Out};
	private boolean send = false;
	
	public MarketAgent(String name, int st, int pi, int chi, int sa){
		super();
		this.name = name;
		timer = new Timer();
		food.add(new MyFood("Steak",st,15.99));
		food.add(new MyFood("Pizza",pi,10.99));
		food.add(new MyFood("Chicken",chi,8.99));
		food.add(new MyFood("Salad",sa,5.99));
	}
	
	public void setCook(CookAgent c){
		this.cook = c;
	}
	public void setCashier(CashierAgent c){
		this.cashier = c;
	}
	public String getName() {
		return name;
	}
	public void msgPayMarket(double money){
		Do("Money "+ money +" Recieved");
		this.money += money;
	}
	public void msgOutOfFood(String Choice){
		Do("msgCookOutOfFood");
		for(MyFood f : food){
			if(f.Choice.equals(Choice)){
				if(f.inventory == 0){
					Do("Run out of Food go to next market");
					f.s = FoodState.Out;
				}
				else
				{
					Do("Prepare to ship the food");
					f.s = FoodState.ReadyToSend;
				}
			}
		}
		stateChanged();
	}
	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		for(MyFood f:food){
			if(f.s == FoodState.Out)
			{
				RunOutFood();
				return true;
			}
			}
		
		for(MyFood f:food){
			if(f.s == FoodState.ReadyToSend)
			{
				PrepareFood(f);
				return true;
			}
			}
		
		for(MyFood f:food){
		if(f.s == FoodState.Sending && send == true)
		{
			send = false;
			DeliverFood(f);
			return true;
		}
		}
		return false;
	}
	private void PrepareFood(MyFood f){
		Do("Preparing Food");
		timer.schedule(new TimerTask() {
			public void run() {
				send = true;
				stateChanged();
			}
		},
		5000);
		f.s = FoodState.Sending;
	}
	private void RunOutFood(){
		Do("Market Run Out of food");
		cook.msgMarketNoFood(this);
	}
	private void DeliverFood(MyFood f){
		Do("Delivering Food");
				cook.msgHereIsDelivery(f.Choice,5);
				cashier.msgMarketBill(this,5*f.price);
				f.s = FoodState.Pending;
	}
	private class MyFood{
		String Choice;
		int inventory;
		FoodState s;
		double price;
		MyFood(String Choice, int number,double price){
			this.Choice = Choice;
			this.inventory = number;
			this.price = price;
		}
	}
}
