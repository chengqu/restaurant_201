package restaurant.gui;


import restaurant.CustomerAgent;
import restaurant.HostAgent;

import java.awt.*;

import javax.swing.ImageIcon;

public class HostGui implements Gui {
	
	RestaurantGui gui;
    private HostAgent agent = null;

    private int xPos = -20, yPos = -20;//default waiter position
    public int xDestination = -20, yDestination = -20;//default start position
    
    private Image img;
    private String hostpic = "cashier.png";
    
    public static final int xTable = 200;
    public static final int yTable = 250;
    private AnimationPanel anipanel;

    public HostGui(HostAgent agent) {
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

       
			
    }
    
    public void pause(){
		agent.pauseAgent();
	}
    public void jixu(){
    	agent.resumeAgent();
    }
    public void draw(Graphics2D g) {
    	g.drawImage(img,10,0,null);
    }

    public boolean isPresent() {
        return true;
    }

   

   

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

}
