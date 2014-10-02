package restaurant.gui;

import restaurant.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {
	 private RestaurantGui gui; //reference to main gui
    //Host, cook, waiters and customers
    private HostAgent host = new HostAgent("Sarah");
    private HostGui hostGui = new HostGui(host);
    
    private WaiterAgent waiter = new WaiterAgent("MikeCai");
    private WaiterGui waiterGui = new WaiterGui(waiter,gui);

    private CashierAgent cashier = new CashierAgent("Cashier");
    private CashierGui cashierGui = new CashierGui(cashier);
    
    private CookAgent cook = new CookAgent("Chef");
    private CookGui cookGui = new CookGui(cook);
    
    private MarketAgent Qmarket = new MarketAgent("Quincy Market",10,10,10,10);
    private MarketAgent Market1 = new MarketAgent("Market1",10,10,10,10);
    private MarketAgent Market2 = new MarketAgent("Market2",10,10,10,10);
    
    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();
    
    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private WaiterPanel waiterPanel = new WaiterPanel(this,"Waiters");
    private JPanel group = new JPanel();
    public JTextField namefield = new JTextField(10);
    
   

    public RestaurantPanel(RestaurantGui gui) {
    	namefield.setSize(10, 10);
        this.gui = gui;
        
        cook.setMarket(Qmarket);
        cook.setMarket1(Market1);
        cook.setMarket2(Market2);
        Qmarket.setCook(cook);
        Qmarket.setCashier(cashier);
        Qmarket.startThread();
        
        Market1.setCook(cook);
        Market1.setCashier(cashier);
        Market1.startThread();
        
        Market2.setCook(cook);
        Market2.setCashier(cashier);
        Market2.startThread();
        
        host.setWaiter(waiter);
        host.setCashier(cashier);
        gui.animationPanel.addGui(hostGui);
        host.startThread();
        System.out.println("host start");
        
        waiter.setHost(host);
        waiter.setGui(waiterGui,0);
        cook.setGui(cookGui);
        gui.animationPanel.addGui(cookGui);
        gui.animationPanel.addGui(waiterGui);
        waiter.startThread();
        
        waiter.setCook(cook);
        waiter.setCashier(cashier);
        cashier.setHost(host);
        cashier.startThread();
        cook.startThread();
        
        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(customerPanel);
        group.add(waiterPanel);
        group.add(namefield);
        initRestLabel();
        add(restLabel);
        add(group);
    }


    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showInfo(String type, String name) {

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                CustomerAgent temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        if (type.equals("Waiters")) {
            for (int i = 0; i < waiters.size(); i++) {
                WaiterAgent temp = waiters.get(i);
                if (temp.getName() == name)
                    gui.updateWaiterPanel(temp);
            }
        }
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name, int number) {

    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name);	
    		CustomerGui g = new CustomerGui(c, gui);
    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g,number);
    		customers.add(c);
    		c.startThread();
    		if(customerPanel.flag == true)
    			c.getGui().setHungry();
    	}
    }
    public void addWaiter(String type, String name, int number){
    	if (type.equals("Waiters")){
    		WaiterAgent w = new WaiterAgent(name);
    		WaiterGui g = new WaiterGui(w,gui);
    		waiters.add(w);
    		w.setGui(g,number);
    		gui.animationPanel.addGui(g);
    		w.setHost(host);
    		w.setCook(cook);
    		w.setCashier(cashier);
    		host.setWaiter(w);
    		w.startThread();
    	}
    }

}
