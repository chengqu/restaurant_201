package restaurant;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Menu {
		
	public List<Food> menu = new ArrayList<Food>();
	/**
	 * @param args
	 */
	Menu(){
	     menu.add(new Food("Steak", 15.99));
	     menu.add(new Food("Chicken",10.99));
	     menu.add(new Food("Pizza",8.99));
	     menu.add(new Food("Salad",5.99));
	}
	public String getName(int i){
		return menu.get(i).name;
	}
	public double getPrice(int i){
		return menu.get(i).price;
	}
	public void remove(String choice){
		for(Food f: menu){
			if(f.name.equals(choice) )
				menu.remove(f);
		}
	}
	private class Food{
		String name;
		double price;
		
		Food(String n, double p){
			this.name = n;
			this.price = p;
		}
	}

}
