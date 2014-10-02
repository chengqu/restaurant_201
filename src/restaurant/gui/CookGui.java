package restaurant.gui;





import java.awt.*;

import javax.swing.ImageIcon;

import restaurant.CookAgent;

public class CookGui implements Gui {
	
	RestaurantGui gui;
    private CookAgent agent = null;

    private int xPos = 300, yPos = 400;//default waiter position
    public int xDestination = 300, yDestination = 400;//default start positio
    private Image img;
    private String hostpic = "cook.png";
    private String Choice;
    private boolean showOrder = false;
    public CookGui(CookAgent agent) {
        this.agent = agent;
        ImageIcon host = new ImageIcon(this.getClass().getResource(hostpic));
        img = host.getImage();
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
        		& (xDestination == 400) & (yDestination == 400))
        	agent.msgAtBingxiang();
        if(xPos == xDestination && yPos == yDestination
        		& (xDestination == 350) & (yDestination == 400))
        	agent.msgAtGrill();
        if(xPos == xDestination && yPos == yDestination
        		& (xDestination == 250) & (yDestination == 400))
        	agent.msgAtPlating();
        if(xPos == xDestination && yPos == yDestination
        		& (xDestination == 300) & (yDestination == 400))
        	agent.msgAtOrigin();
    }
    public void pause(){
		agent.pauseAgent();
	}
    public void jixu(){
    	agent.resumeAgent();
    }
    public void msgshowOrder(String c){
    	showOrder = true;
    	this.Choice = c;
    	
    }
    public void msghideOrder(){
    	showOrder = false;
    	System.out.println("asdfdsafasdfsafsaf");
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
    
    public void DoGoToBingxiang() {
        xDestination = 400;
        yDestination = 400;
    }
    
    public void DoPlating(){
    	xDestination = 250;
    	yDestination = 400;
    }
    public void DoCooking(){
    	xDestination = 350;
    	yDestination = 400;
    }
    public void DoGoBack(){
    	xDestination = 300;
    	yDestination = 400;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }


	



	
}
