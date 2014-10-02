package restaurant.test.mock;


import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockHost extends Mock implements Host {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public EventLog log = new EventLog();
	public MockHost(String name) {
		super(name);

	}

	@Override
	public void msgINeedMoney(double loan) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received INeedMoney from cashier. Loan = "+ loan));
	}

	@Override
	public void msgPayDebt(double debt) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received PayDebt from cashier. Debt = "+ debt));
	}


	

}
