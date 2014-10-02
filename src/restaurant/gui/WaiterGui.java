package restaurant.gui;


import restaurant.CustomerAgent;
import restaurant.WaiterAgent;


import java.awt.*;

import javax.swing.ImageIcon;

public class WaiterGui implements Gui {
	
	RestaurantGui gui;
    private WaiterAgent agent = null;

    public int xPos;//default waiter position
	public int yPos;
    public int xDestination, yDestination;//default start position
    private enum Command {noCommand, OffBreak, GoToBreak};
	private Command command=Command.noCommand;
    private Image img;
    private String hostpic = "host.png";
    private String Choice;
    public static final int xTable = 180;
    public static final int yTable = 250;
    public static final int xTable2 = 320;
    public static final int yTable2 = 250;
    public static final int xTable3 = 30;
    public static final int yTable3= 250;
    public static final int xCook = 300;
    public static final int yCook = 400;
    private AnimationPanel anipanel;
    public boolean showOrder = false;
    public boolean isBreak = false;
    private int number =0;
    public WaiterGui(WaiterAgent agent, RestaurantGui gui) {
        this.agent = agent;
        ImageIcon host = new ImageIcon(this.getClass().getResource(hostpic));
        img = host.getImage();
        this.gui = gui;
    }
    public void setNumber(int count){
    	this.number = count;
    	xDestination = 370-number*70 ;
		yDestination = 30;
		xPos = 370-number*70;
		yPos =30;
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

        if (xPos == xDestination && yPos == yDestination
        		& (((xDestination == xTable +20) & (yDestination == yTable - 20)) || (((xDestination == xTable2 + 20 ) && (yDestination == yTable2 - 20)||(xDestination == xTable3 + 20) && (yDestination == yTable3 - 20))))) {
           agent.msgAtTable();
        }
        
        /*if (xPos == xDestination && yPos == yDestination
        		& (((xDestination == xTable + 10) & (yDestination == yTable - 10)) || (((xDestination == 310 ) && (yDestination == 240)||(xDestination == 110) && (yDestination == 240))))) {
           agent.msgAtTable();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (((xDestination == xTable) & (yDestination == yTable)) || (((xDestination == 300 ) && (yDestination == 250)||(xDestination == 100) && (yDestination == 250))))) {
           agent.msgAtTable();
        }*/
        if(xPos == xDestination && yPos == yDestination
        		& (xDestination ==370- number*70) & (yDestination == 30))
        	agent.msgIsorigin();
        if(xPos == xDestination && yPos == yDestination
        		& (xDestination == 250) & (yDestination == yCook))
        	agent.msgAtCook();
        if(xPos == xDestination && yPos == yDestination
        		& (xDestination == 0) & (yDestination == 0))
        	agent.msgAtCashier();
        if(xPos == xDestination && yPos == yDestination
        		& (xDestination == 550) & (yDestination == 50))
        	agent.msgAtCustomer();
    }
    public void pause(){
		agent.pauseAgent();
	}
    public void jixu(){
    	agent.resumeAgent();
    }
    public void setFood(){
    	agent.setFood();
    }
    public void msgshowOrder(String c){
    	showOrder = true;
    	this.Choice = c;
    	
    }
    public void msghideOrder(){
    	showOrder = false;
    	
    }
    public void draw(Graphics2D g) {
    	g.drawImage(img, xPos,yPos,null);
    	if(showOrder == true){
    		 Font font = new Font("Arial", Font.PLAIN, 30);
    		    g.setFont(font);
    		    g.setColor(Color.black);
    			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    			g.drawString(Choice, xPos, yPos);
    	}
        //g.setColor(Color.MAGENTA);
        //g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }
    public boolean isBreak(){
    	return isBreak;
    }
    public void setBreak(){
    	isBreak = true;
    	agent.IWantBreak();
    	command = command.GoToBreak;
    }
    public void setOffBreak(){
    	command = command.OffBreak;
    	isBreak = false;
    	agent.OffBreak();
    }
    public void setWaiterEnabled(){
    	if(command == command.GoToBreak){
    	gui.setWaiterEnabled(agent);
    	}
    }
    public void DoBringToTable(CustomerAgent customer, int tablenum) {
    	customer.setPosition();
    	if(tablenum == 1){
        xDestination = xTable + 20;
        yDestination = yTable - 20;}
    	if(tablenum == 2){
    		xDestination = xTable2 + 20;
    		yDestination = yTable2 - 20;
    	}
    	if(tablenum == 3){
    		xDestination = xTable3 + 20;
    		yDestination = yTable3 - 20;
    	}
    }

    public void DoLeaveCustomer() {
        xDestination = 370 - number*70;
        yDestination = 30;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }


	

	public void DoGoToCook() {
		// TODO Auto-generated method stub
		xDestination = 250;
		yDestination = yCook;
	}
	public void DoGoToBreak(){
		xDestination = 450;
		yDestination = -20;
	}
	public void DoGoToCashier() {
		// TODO Auto-generated method stub
		xDestination = 0;
		yDestination = 0;
	}

	public void DoGoToCustomer() {
		// TODO Auto-generated method stub
		xDestination = 550;
		yDestination = 50;
	}


	
}
