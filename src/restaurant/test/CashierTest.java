package restaurant.test;

import restaurant.CashierAgent;
import restaurant.CookAgent;
//import restaurant.CashierAgent.cashierBillState;
//import restaurant.WaiterAgent.Bill;
import restaurant.test.mock.MockCustomer;
import restaurant.test.mock.MockHost;
import restaurant.test.mock.MockMarket;
import restaurant.test.mock.MockWaiter;

import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CashierAgent cashier;
	CookAgent cook;
	MockWaiter waiter;
	MockCustomer customer;
	MockMarket market;
	MockHost host;
	MockMarket market2;
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new CashierAgent("cashier");		
		customer = new MockCustomer("mockcustomer");		
		waiter = new MockWaiter("mockwaiter");
		market = new MockMarket("mockmarket");
		host = new MockHost("mockhost");
		market2 = new MockMarket("mockmarket2");
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testOneNormalCustomerScenario()
	{
		//setUp() runs first before this test!
		
		customer.cashier = cashier;//You can do almost anything in a unit test.	
		assertFalse("Cashier's scheduler should have returned false (no actions to do before a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.customer.size(), 0);
		assertFalse("Cashier's scheduler should have returned false (no actions to do before a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		cashier.msgComputeCheck(waiter, customer, "Steak");
		
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.customer.size(), 1);
		assertEquals("CashierCustomer should contain a bill of price = $15.99. It contains something else instead: $" 
				+ cashier.customer.get(0).check,15.99, cashier.customer.get(0).check);
		
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		assertTrue("Cashier's scheduler should have returned true (needs to react to market's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		cashier.msgPay(customer, 20);
		
		
		assertEquals("CashierCustomer should contain a cash of price = $20. It contains something else instead: $" 
				+ cashier.customer.get(0).Cash,20.0, cashier.customer.get(0).Cash);
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourChange from cashier. Change = 4.01"));
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	
	}//end one normal customer scenario
	public void testOneNormalMarketScenario(){
		market.cashier = cashier;
		assertFalse("Cashier's scheduler should have returned false (no actions to do before a bill from a market), but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bill.size(), 0);
		
		cashier.msgMarketBill(market, 34.2);
		
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bill.size(), 1);
		assertEquals("CashierBill should contain a money of price = $34.2. It contains something else instead: $" 
				+ cashier.bill.get(0).money,34.2, cashier.bill.get(0).money);
		assertTrue("CashierBill should contain a bill with the right market in it. It doesn't.", 
				cashier.bill.get(0).m == market);
		assertTrue("Cashier's scheduler should have returned true (needs to react to market's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("MockMarket should have logged an event for receiving \"HereIsYourMoney\" with the correct amount, but his last event logged reads instead: " 
				+ market.log.getLastLoggedEvent().toString(), market.log.containsString("Received PayMarket from cashier. Total = 34.2"));
		
	}
	public void testTwoMarketScenario(){
		market.cashier = cashier;
		assertFalse("Cashier's scheduler should have returned false (no actions to do before a bill from a market), but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bill.size(), 0);
		cashier.msgMarketBill(market, 20);
		cashier.msgMarketBill(market2, 30);
		assertEquals("Cashier should have 2 bills in it. It doesn't.",cashier.bill.size(), 2);
		assertTrue("Cashier's scheduler should have returned true (needs to react to market's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's scheduler should have returned true (needs to react to market's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("MockMarket1 should have logged an event for receiving \"HereIsYourMoney\" with the correct amount, but his last event logged reads instead: " 
				+ market.log.getLastLoggedEvent().toString(), market.log.containsString("Received PayMarket from cashier. Total = 20.0"));
		assertTrue("MockMarket2 should have logged an event for receiving \"HereIsYourMoney\" with the correct amount, but his last event logged reads instead: " 
				+ market2.log.getLastLoggedEvent().toString(), market2.log.containsString("Received PayMarket from cashier. Total = 30.0"));
		
	}
	
	public void testCustomerNoMoneyScenario(){
		customer.cashier = cashier;
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.customer.size(), 0);
		assertFalse("Cashier's scheduler should have returned false (no actions to do before a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		cashier.msgComputeCheck(waiter, customer, "Steak");
		
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.customer.size(), 1);
		assertEquals("CashierCustomer should contain a bill of price = $15.99. It contains something else instead: $" 
				+ cashier.customer.get(0).check,15.99, cashier.customer.get(0).check);
		
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		
		cashier.msgPay(customer, 0);
		
		
		assertEquals("CashierCustomer should contain a cash of price = $20. It contains something else instead: $" 
				+ cashier.customer.get(0).Cash,0.0, cashier.customer.get(0).Cash);
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("MockCustomer should have logged an event for receiving \"YouOweUs\" with the correct change, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received YouOweUs from cashier. Debt = 15.99"));
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
	}
	public void testCashierNoMoneyScenario(){
		host.cashier = cashier;
		cashier.setHost(host);
		assertFalse("Cashier's scheduler should have returned false (no actions to do before a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bill.size(), 0);
		
		cashier.msgMarketBill(market, 108);
		
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bill.size(), 1);
		assertEquals("CashierBill should contain a money of price = $108. It contains something else instead: $" 
				+ cashier.bill.get(0).money,108.0, cashier.bill.get(0).money);
		assertTrue("Cashier's scheduler should have returned true (needs to react to market's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("MockHost should have logged an event for receiving \"INeedMoney\" with the correct amount, but his last event logged reads instead: " 
				+ host.log.getLastLoggedEvent().toString(), host.log.containsString("Received INeedMoney from cashier. Loan = 8.0"));
		
		cashier.msgHereIsMoney(8);
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to market's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should contain a money of price = $0. It contains something else instead: $" 
				+ cashier.money,0.0, cashier.money);
		assertTrue("MockMarket should have logged an event for receiving \"HereIsYourMoney\" with the correct amount, but his last event logged reads instead: " 
				+ market.log.getLastLoggedEvent().toString(), market.log.containsString("Received PayMarket from cashier. Total = 108.0"));
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		cashier.msgComputeCheck(waiter, customer, "Steak");
		
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.customer.size(), 1);
		assertEquals("CashierCustomer should contain a bill of price = $15.99. It contains something else instead: $" 
				+ cashier.customer.get(0).check,15.99, cashier.customer.get(0).check);
		
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		assertTrue("Cashier's scheduler should have returned true (needs to react to market's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		cashier.msgPay(customer, 20);
		assertTrue("Cashier's scheduler should have returned true (needs to react to market's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		cashier.msgMarketBill(market, 2);
		assertTrue("Cashier's scheduler should have returned true (needs to react to market's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("CashierRich should be true", cashier.rich);
		assertTrue("Cashier's scheduler should have returned true (needs to react to market's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("MockHost should have logged an event for receiving \"PayTheDebt\" with the correct change, but his last event logged reads instead: " 
				+ host.log.getLastLoggedEvent().toString(), host.log.containsString("Received PayDebt from cashier. Debt = 8.0"));
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
	}
	public void testCustomerInterleavingMarketScenario(){
		market.cashier = cashier;
		customer.cashier = cashier;
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.customer.size(), 0);
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bill.size(), 0);
		assertFalse("Cashier's scheduler should have returned false (no actions to do before a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		cashier.msgMarketBill(market, 56.3);
		cashier.msgComputeCheck(waiter, customer, "Salad");
		
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.customer.size(), 1);
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bill.size(), 1);
		assertEquals("CashierCustomer should contain a bill of price = $5.99. It contains something else instead: $" 
				+ cashier.customer.get(0).check,5.99, cashier.customer.get(0).check);
		
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		assertEquals("CashierBill should contain a money of price = $56.3. It contains something else instead: $" 
				+ cashier.bill.get(0).money,56.3, cashier.bill.get(0).money);
		assertTrue("Cashier's scheduler should have returned true (needs to react to market's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's scheduler should have returned true (needs to react to market's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("MockMarket should have logged an event for receiving \"HereIsYourMoney\" with the correct amount, but his last event logged reads instead: " 
				+ market.log.getLastLoggedEvent().toString(), market.log.containsString("Received PayMarket from cashier. Total = 56.3"));

		cashier.msgPay(customer, 10);
		
		
		assertEquals("CashierCustomer should contain a cash of price = $10. It contains something else instead: $" 
				+ cashier.customer.get(0).Cash,10.0, cashier.customer.get(0).Cash);
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourChange from cashier. Change = 4.01"));
		
	}
}
