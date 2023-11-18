/*
 * The ExpressCounter is a counter that is meant for customers
 * whose items are in the range: numSuper < CustomerItems <= numExpress
 */
public class ExpressCounter extends CheckoutCounter
{

	public ExpressCounter(String queueName)
	{
		super(queueName);
	}

}
