
/*
 * The SuperExpressCounter is for customers who hold very few items,
 * it is meant for customers whose items are in the range: numSuper <= numItems
 */
public class SuperExpressCounter extends CheckoutCounter 
{

	public SuperExpressCounter(String queueName)
	{
		super(queueName);
	}
	
}
