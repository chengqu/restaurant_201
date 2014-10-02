package restaurant.test.mock;

/**
 * This is the base class for all mocks.
 *
 * @author Sean Turner
 *
 */
public class Mock {
	private String name;
	private EventLog log;
	public Mock(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return this.getClass().getName() + ": " + name;
	}

}
