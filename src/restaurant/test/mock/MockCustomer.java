package restaurant.test.mock;


import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements Customer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public EventLog log = new EventLog();
	public MockCustomer(String name) {
		super(name);

	}

	@Override
	public void msgGiveChange(double change) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ change));
	}

	@Override
	public void YouOweUs(double remaining_cost) {
		log.add(new LoggedEvent("Received YouOweUs from cashier. Debt = "+ remaining_cost));
	}

	

}
