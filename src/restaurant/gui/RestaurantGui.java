package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.WaiterAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.sound.sampled.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	JFrame animationFrame = new JFrame("Restaurant Animation");
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    private ListPanel listPanel = new ListPanel(restPanel,"");
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JPanel waiterPanel;
    private JLabel waiterLabel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    private JCheckBox stateWB;
    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */
    private JPanel aniPanel;
    private static File file;
    private JButton Clear;
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     * @throws URISyntaxException 
     */
    public RestaurantGui() throws URISyntaxException {
        int WINDOWX = 700;
        int WINDOWY = 900;
        
        Clear = new JButton();
        Clear.setText("SetCookFood0");
        //animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
        //animationFrame.setVisible(true);
    	//animationFrame.add(animationPanel); 
        file = new File(this.getClass().getResource("test.wav").toURI());
    	
    	setBounds(50, 50, WINDOWX, WINDOWY);

        setLayout(new BoxLayout((Container) getContentPane(), 
        		BoxLayout.Y_AXIS));
    	//setLayout(new FlowLayout());
        Dimension restDim = new Dimension(500,210);
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        add(restPanel);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(500,(int) 50);
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("customerInformation"));
        // Setup the waiter panel;
        Dimension waiterDim = new Dimension(500,(int) 50);
        waiterPanel = new JPanel();
        waiterPanel.setPreferredSize(waiterDim);
        waiterPanel.setMinimumSize(waiterDim);
        waiterPanel.setMaximumSize(waiterDim);
        waiterPanel.setBorder(BorderFactory.createTitledBorder("waiterInformation"));
        
        stateCB = new JCheckBox();
        stateCB.setVisible(true);
        stateCB.setText("Hungry?");
        stateCB.addActionListener(this);
        
        stateWB = new JCheckBox();
        stateWB.setText("Break?");
        stateWB.addActionListener(this);
        
        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        add(infoPanel);
        
        waiterPanel.setLayout(new GridLayout(1, 2, 10, 0));
        waiterLabel = new JLabel();
        waiterLabel.setText("<html><pre><i>Click Add to make waiters</i></pre></html>");
        waiterPanel.add(waiterLabel);
        waiterPanel.add(stateWB);
        //waiterPanel.add(Clear);
        add(waiterPanel);
        
        Dimension aniDim = new Dimension(700,800);
        animationPanel.setPreferredSize(aniDim);
        animationPanel.setMaximumSize(aniDim);
        animationPanel.setMinimumSize(aniDim);
        add(animationPanel);
       
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateWaiterPanel(Object person){
    	stateWB.setVisible(true);
    	currentPerson = person;
    	if (person instanceof WaiterAgent) {
        	//stateCB.setVisible(false);
        	stateWB.setVisible(true);
            WaiterAgent waiter = (WaiterAgent) person;
            stateWB.setText("Break?");
          //Should checkmark be there? 
            stateWB.setSelected(waiter.getGui().isBreak());
          //Is customer hungry? Hack. Should ask customerGui
            stateWB.setEnabled(!waiter.getGui().isBreak());
          // Hack. Should ask customerGui
            waiterLabel.setText(
               "<html><pre>     Name: " + waiter.getName() + " </pre></html>");
        }
    	waiterPanel.validate();
    }
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(true);
       
        currentPerson = person;

        if (person instanceof CustomerAgent) {
        	stateCB.setVisible(true);
        	
            CustomerAgent customer = (CustomerAgent) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + "       Cash"+customer.getMoney()+" </pre></html>");
        }
        
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof CustomerAgent) {
                CustomerAgent c = (CustomerAgent) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
        }
        if (e.getSource() == stateWB) {
            if (currentPerson instanceof WaiterAgent) {
                WaiterAgent w = (WaiterAgent) currentPerson;
                if(stateWB.isSelected() == true){
                	System.out.println("I want Break");
                	w.getGui().setBreak();
                	stateWB.setEnabled(false);
                }
                else{
                	System.out.println("I'm off Break");
                	w.getGui().setOffBreak();
                }
            }
        }
        if(e.getSource() == Clear){
        	 if (currentPerson instanceof WaiterAgent) {
                 WaiterAgent w = (WaiterAgent) currentPerson;
                 w.getGui().setFood();
        	 }
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerAgent c) {
        if (currentPerson instanceof CustomerAgent) {
            CustomerAgent cust = (CustomerAgent) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    public void setWaiterEnabled(WaiterAgent w) {
        if (currentPerson instanceof WaiterAgent) {
            WaiterAgent waiter = (WaiterAgent) currentPerson;
            if (w.equals(waiter)) {
                stateWB.setEnabled(true);
                stateWB.setSelected(true);
            }
        }
    }
    /**
     * Main routine to get gui started
     * @throws LineUnavailableException 
     * @throws IOException 
     * @throws UnsupportedAudioFileException 
     * @throws URISyntaxException 
     */
    public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException, IOException, URISyntaxException {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //URL url = new URL("http://pscode.org/media/leftright.wav");
        
            Clip clip = AudioSystem.getClip();
            // getAudioInputStream() also accepts a File or InputStream
            AudioInputStream ais = AudioSystem.getAudioInputStream(file);
            clip.open(ais);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            /*SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    // A GUI element to prevent the Clip's daemon Thread
                    // from terminating at the end of the main()
                    JOptionPane.showMessageDialog(null, "Close to exit!");
                }
            });*/
        
    
}
}
