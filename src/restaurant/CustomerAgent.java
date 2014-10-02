package restaurant;

import restaurant.gui.CustomerGui;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Customer;
import agent.Agent;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Restaurant customer agent.
 */
public class CustomerAgent extends Agent implements Customer{
	public int count = 0;
    public int tablex2;
    public int tabley2;
    
    public int tablex3;
    public int tabley3;
	public int tablenum;
	private int ThinkingTime =10000;
	private int EatingTime = 5000;
	public String Choice;
	public double price;
	private Menu menu;
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private CustomerGui customerGui;
	// agent correspondents
	private WaiterAgent waiters;
	//    private boolean isHungry = false; //hack for gui
	private HostAgent host;
	private CashierAgent cashier;
	private int seatnum;
	private boolean atCashier = false;
	private boolean tableFull = false;
	public enum AgentState
	{ReadyToSeat,DoingNothing, WaitingInRestaurant, BeingSeated,Ordered,Eating, DoneEating, Leaving, TakingOrder, Paied, Leave};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followHost, seated,ordering,beingServed, doneEating, doneLeaving, TakingOrder, Reorder, Paied, Paying, ReadyToSeat};
	AgentEvent event = AgentEvent.none;
	
	private double Cash;
	
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerAgent(String name){
		super();
		this.name = name;
		menu = new Menu();
		Random r2 = new Random();
		Cash = r2.nextInt(10)+20;
		
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setPosition(){
		tablex2 = customerGui.tablex2;
		tablex3 = customerGui.tablex3;
		tabley2 = customerGui.tabley2;
		tabley3 = customerGui.tabley3;
	}
	
	public void setWaiter(WaiterAgent w) {
		this.waiters = w;
	}
	public void setHost(HostAgent host) {
		this.host = host;
	}
	public void setCashier(CashierAgent cashier){
		this.cashier = cashier;
	}
	// Messages
	public void msgReadyToSeat(){
		Do("ReadyToBeSeated");
		event = AgentEvent.ReadyToSeat;
		stateChanged();
	}
	
	public void gotHungry() {//from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}

	public void msgFollowMe(Menu m,int table,WaiterAgent w) {
		print("Received msgSitAtTable");
		Random r = new Random();
		int setChoice = r.nextInt(4);
		for(int i=0; i<m.menu.size(); i++){
			if(Cash < m.getPrice(i))
				setChoice = 3;
		}
		this.waiters = w;
		seatnum = table;
		event = AgentEvent.followHost;
		this.menu = m;
		Choice = m.getName(setChoice);
		price = m.getPrice(setChoice);
		//if (Cash < m.getPrice(setChoice))
			
		stateChanged();
		
	}

	public void msgWhatDoYouWant() {
		//from animation
		print("msgWhatDoYouWant");
		event = AgentEvent.TakingOrder;
		stateChanged();
	}
	public void msgReorder(Menu m){
		Do("msgReorder");
		this.menu = m;
		Random r = new Random();
		int setChoice = r.nextInt(menu.menu.size());
		Choice = m.getName(setChoice);
		price = m.getPrice(setChoice);
		event = AgentEvent.TakingOrder;
		state = AgentState.TakingOrder;
		stateChanged();
	}
	public void msgHereIsTheFood() {
		//from animation
		Do("msgHereIsTheFood");
		event = AgentEvent.beingServed;
		stateChanged();
	}
	public void msgGoToPay(){
		Do("msgGoToPay");
		event = AgentEvent.Paying;
		stateChanged();
	}
	public void msgGiveChange(double change){
		Do("msgGiveChange");
		Cash = change;
		event = AgentEvent.Paied;
		stateChanged();
	}
	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		Do("msgAnimationFinishedLeaveRestaurant");
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	@Override
	public void YouOweUs(double remaining_cost) {
		// TODO Auto-generated method stub
		
	}
	public void msgAtCashier(){
		atCashier = true;
		stateChanged();
	}
	public void msgTableFull(){
		tableFull = true;
		stateChanged();
	}
	public void msgTableAvailable(){
		tableFull = false;
		stateChanged();
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.ReadyToSeat;
			goToRestaurant();
			return true;
		}
		
		if (state == AgentState.ReadyToSeat && event == AgentEvent.ReadyToSeat ){
			state = AgentState.WaitingInRestaurant;
			goToWait();
			return true;
				}

		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.ordering){
			state = AgentState.TakingOrder;
			TakeOrder();
			return true;
		}
		if (state == AgentState.TakingOrder && event == AgentEvent.TakingOrder){
			state = AgentState.Ordered;
			HereIsMyOrder();
			return true;
		}
		if (state == AgentState.Ordered && event == AgentEvent.beingServed){
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.Leaving;
			IWantToPay();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.Paying){
			state = AgentState.Paied;
			Pay();
			return true;
		}
		if (state == AgentState.Paied && event == AgentEvent.Paied && atCashier == true){
			state = AgentState.Leave;
			atCashier = false;
			Leave();
			return true;
		}
		if (state == AgentState.Leave && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			//no action
			return true;
		}
		return false;
	}

	// Actions
	private void goToWait(){
		Do("MovingToTheFirst");
		customerGui.DoGoToWait();
	}
	private void goToRestaurant() {
		Do("Going to restaurant");
		if(tableFull == true)
		{
			timer.schedule(new TimerTask(){
				public void run(){
					Do("I'm thinking about leaving");
					Random r3 = new Random();
					int leave = r3.nextInt(2);
					if(leave == 0){
						Do("Table is full, I'm leaving");
						state = AgentState.Paied;
						event = AgentEvent.Paied;
						atCashier = true;
					}
						else {
							Do("See if there is a table");
							state = AgentState.DoingNothing;
							event = AgentEvent.gotHungry;
						}
						stateChanged();
					
				}
			}, 10000);
			
		}
		else {
			host.msgIWantFood(this);
		}
	}

	private void SitDown() {
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat(seatnum);//hack; only one table	
		timer.schedule(new TimerTask(){
			public void run(){
				Do("I'm thinking about the order");
				event = AgentEvent.ordering;
				stateChanged();
			}
		}, ThinkingTime);
	}

	private void TakeOrder(){
		Do("Take the order");
		customerGui.msgSetOrder();
		waiters.msgIWantFood(this);
	}
	private void HereIsMyOrder(){
		Do("Here is my order");
		waiters.msgHereIsMyOrder(this, Choice);
		customerGui.msgCancelSignal();
	}
	private void EatFood() {
		Do("Eating Food");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				print("Done eating, cookie=" + cookie);
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		EatingTime);//getHungerLevel() * 1000);//how long to wait before running task
		customerGui.msgShowOrder();
	}
	
	private void IWantToPay() {
		Do("Paying.");
		waiters.msgIWantToPay(this);
		
	}
	private void Pay(){
		Do("Leave Table to Pay");
		waiters.msgLeavingTable(this);
		cashier.msgPay(this, Cash);
		customerGui.DoGoToCashier();
		customerGui.msgHideOrder();
	}
	// Accessors, etc.
	private void Leave(){
		customerGui.DoExitRestaurant();
	}
	public String getName() {
		return name;
	}
	
	public double getMoney(){
		return Cash;
	}
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui g, int number) {
		customerGui = g;
		customerGui.setNumber(number);
	}

	public CustomerGui getGui() {
		return customerGui;
	}

	


	

	
}

