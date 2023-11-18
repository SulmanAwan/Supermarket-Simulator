import java.util.Random;

/*
 * Customers entering the supermarket are assigned a random number
 * items, each item takes 5 seconds to be processed by the cashier. 
 */
public class Customer 
{
	private int numOfItems;
	private int arrivalTime;
	private int timeServe;
	private Random generator;
	
	/** 
	 * In every construction of a customer object they get an 
	 * arrival time and a certain number of items.
	 *
	 * @param arrivalTime
	 * @param numOfItems
	 */
	public Customer(int arrivalTime, int maxItems) 
	{
		generator = new Random();
		this.arrivalTime = arrivalTime;
		
		//numOfItems bought in range of 1 to maxItems.
		numOfItems = generator.nextInt(maxItems) + 1;
		timeServe = numOfItems * 5;
	}
	
	public int getNumOfItems()
	{
		return numOfItems;
	}
	
	public int getArrivalTime()
	{
		return arrivalTime;
	}
	
	public int getTimeServe()
	{
		return timeServe;
	}
	
	public void setTimeServe(int timeServe)
	{
		this.timeServe = timeServe;
	}
}


