package restaurant.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;


public class AnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 700;
    private final int WINDOWY = 800;
    private final int TABLEX = 200;
    private final int TABLEY = 250;
    private final int TABLEW = 50;
    private final int TABLEH = 50;
    private final int t = 10;
    
    private JButton addTable;
    private JButton pause;
    private JButton Break;
    public boolean goBreak = false;
    public int clicked = 0;
    public boolean table2 = false;
    public boolean table3 = false;
    public int tablew2;
    public int tableh2;
    public int tablex2;
    public int tabley2;
    
    public int tablew3;
    public int tableh3;
    public int tablex3;
    public int tabley3;
    
    private Image bufferImage;
    private Dimension bufferSize;
    private List<Gui> guis = new ArrayList<Gui>();
    public AnimationPanel() {
    	
    	addTable = new JButton();
    	addTable.setText("addTable");
    	addTable.addActionListener(this);
    	//add(addTable);
    	
    	
    	
    	pause = new JButton();
    	pause.setText("Pause");
    	pause.addActionListener(this);
    	add(pause);
    	
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(t, this );
    	timer.start();
    }
    
	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
		if(e.getSource() == pause){
			if(pause.getText().compareTo("Pause") == 0){
			pause.setText("Resume");
	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.pause();
	            }
	        }
		}
			else if(pause.getText().compareTo("Resume")== 0){
				pause.setText("Pause");
				 for(Gui gui : guis) {
			            if (gui.isPresent()) {
			                gui.jixu();
			            }
				 }
			}
		}
		
		
		}
		
	
		
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(180, 250, 100, 100);//200 and 250 need to be table params
        
        
        //if(table2 == true){
        g2.setColor(Color.ORANGE);
        g2.fillRect(320, 250, 100, 100);//200 and 250 need to be table params
        //}
        //if(table3 == true){
        g2.setColor(Color.ORANGE);
        g2.fillRect(30, 250, 100, 100);//200 and 250 need to be table params
        //}
        g2.setColor(Color.lightGray);
        g2.fillRect(550, 50, 100, 400);
        
        g2.setColor(Color.blue);
        g2.fillRect(460, 400, 30, 20);
        g2.drawString("Fridge", 450, 400);
        
        g2.setColor(Color.red);
        g2.fillRect(360, 400, 30, 20);
        g2.drawString("Grill", 360, 400);
        
        g2.setColor(Color.black);
        g2.fillRect(250, 400, 20, 50);
        g2.drawString("Plating", 250, 400);
        
        g2.setColor(Color.lightGray);
        g2.fillRect(550, 50, 100, 400);
        
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    
    
    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }
    public void addGui(CookGui gui){
    	guis.add(gui);
    }
    public void addGui(HostGui gui) {
        guis.add(gui);
    }
    public void addGui(WaiterGui gui){
    	guis.add(gui);
    }
}
