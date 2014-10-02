package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.HostAgent;

import java.awt.*;

import javax.swing.ImageIcon;

public class CustomerGui implements Gui{

	private CustomerAgent agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	
	 	public int tablex2;
	    public int tabley2;
	    
	    public int tablex3;
	    public int tabley3;
	    
	//private HostAgent host;
	RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;
	private AnimationPanel anipanel;
	public static final int xTable = 200;
	public static final int yTable = 250;
	public static final int xTable2 = 320;
	public static final int yTable2 = 250;
	public static final int xTable3 = 30;
	public static final int yTable3= 250;
	public static final int xCook = 300;
	public static final int yCook = 400;
	private String customerpic = "customer.png";
	private Image img;
	public boolean ReadyToOrder = false;
	public boolean ShowOrder = false;
	private int number;
	public CustomerGui(CustomerAgent c, RestaurantGui gui){ //HostAgent m) {
		anipanel = gui.animationPanel;
		tablex2 = anipanel.tablex2;
		tablex3 = anipanel.tablex3;
		tabley2 = anipanel.tabley2;
		tabley3 = anipanel.tabley3;
		agent = c;
		xPos = 550;
		yPos = 50;
		xDestination = 550;
		yDestination = 50;
		//maitreD = m;
		this.gui = gui;
		ImageIcon customer = new ImageIcon(this.getClass().getResource(customerpic));
		img = customer.getImage();
	}
	public void msgSetOrder(){
		ReadyToOrder = true;
	}
	public void msgCancelSignal(){
		ReadyToOrder = false;
	}
	public void msgShowOrder(){
		ShowOrder = true;
	}
	public void msgHideOrder(){
		ShowOrder = false;
	}
	public void setNumber(int i){
		this.number = i;
    	xDestination = 550 ;
		yDestination = 50 + number*70;
		xPos = 550;
		yPos =50 + number*70;
	}
	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
		 if(xPos == xDestination && yPos == yDestination
	        		& (xDestination == 0) & (yDestination == 0))
	        	agent.msgAtCashier();
		 
		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}
	public void pause(){
		agent.pauseAgent();
	}
	public void jixu(){
		agent.resumeAgent();
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(img,xPos,yPos,null);
		if(ReadyToOrder ==true){
	    Font font = new Font("Arial", Font.PLAIN, 30);
	    g.setFont(font);
	    g.setColor(Color.black);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawString(agent.Choice+"?", xPos, yPos);
		}
		if(ShowOrder == true){
			Font font = new Font("Arial", Font.PLAIN, 30);
		    g.setFont(font);
		    g.setColor(Color.black);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawString(agent.Choice, xPos, yPos+80);
		}
		//g.setColor(Color.GREEN);
		//g.fillRect(xPos, yPos, 20, 20);
	}
	
	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		if(seatnumber == 1)
		{
			 xDestination = xTable + 20;
		     yDestination = yTable - 20;
		}
		command = Command.GoToSeat;
		if(seatnumber == 2)
		{
			xDestination = xTable2 + 20;
    		yDestination = yTable2 - 20;
			command = Command.GoToSeat;
		}
		if(seatnumber == 3){
			xDestination = xTable3 + 20;
    		yDestination = yTable3 - 20;
			command = Command.GoToSeat;
		}
	}

	public void DoExitRestaurant() {
		xDestination = 550;
		yDestination = -60;
		command = Command.LeaveRestaurant;
	}
	public void DoGoToCashier() {
		// TODO Auto-generated method stub
		xDestination = 0;
		yDestination = 0;
	}
	public void DoGoToWait(){
		xDestination = 550;
		yDestination = 50;
	}
}
