/*
 * The standardCounter is meant for customers who have items in the
 * range of: numItem > numExpress
 */
public class StandardCounter extends CheckoutCounter
{
	public StandardCounter(String queueName)
	{
		super(queueName);
	}
}
