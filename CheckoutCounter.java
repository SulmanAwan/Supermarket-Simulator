import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/*
 * Each checkout counter has a line of customers, each checkout counter
 * needs the functionality to add new customers to line and delete customers
 * who have been served.
 */
public class CheckoutCounter 
{
	/* queue of customers in this current checkout line */
	private Queue<Customer> customerLine; 
	/* max length of this checkout line*/
	private int maxLength;
	/* total number of items processed by this checkout line */
	private double numItemProcess;
	/* total wait time for a particular customer in the checkout line */
	private double totalWait;
	/* total number of customers served */
	private double numServed;
	/* total number of customers ever entered into the line*/
	private double totalCustomers;
	/* total amount of time in the simulation that the checkout line has 0 customers*/
	private double totalFreeTime;
	/* time left to process the first customer in line*/
//	private int timeLeft;
	/* name of this line*/
	private String queueName;
	
	public CheckoutCounter(String queueName)
	{
		this.queueName = queueName;
		customerLine = new LinkedList<Customer>();
	}
	
	public void checkArrival(int currentTime, Customer customer)
	{
		totalCustomers++;
		
		if(maxLength < customerLine.size())
		{
			maxLength = customerLine.size();
		}		
	}
	
	
	public int update(int clock)
	{	
		Customer nextCustomer = customerLine.remove();
		int wait = clock - nextCustomer.getArrivalTime();
		totalWait += wait;
		numServed++;
		numItemProcess += nextCustomer.getNumOfItems();
				
		System.out.println("Time " + clock + ": " + "cashier at " + getName() 
				+ " served customer with time stamp " + nextCustomer.getArrivalTime()
				+ ", new counter size: " + getSize());
				
		return clock + 5;
	}
	
	public void updateDemo(int clock)
	{
		Customer customer = customerLine.peek();

		if(customer.getTimeServe() == 0)
		{
			Customer nextCustomer = customerLine.remove();
			int wait = clock - nextCustomer.getArrivalTime();
			totalWait += wait;
			numServed++;
			numItemProcess += nextCustomer.getNumOfItems();
					
			System.out.println("Time " + clock + ": " + "cashier at " + getName() 
					+ " served customer with time stamp " + nextCustomer.getArrivalTime()
					+ ", new counter size: " + getSize());
		}
		else if(customer.getTimeServe() != 0)
		{
			customer.setTimeServe(customer.getTimeServe() - 1);
		}
	}
	
	public void incrementFreeTime()
	{
		totalFreeTime++;
	}
	
	public double getTotalWait()
	{				
		return totalWait;
	}
	
	public double getNumServed()
	{
		return numServed;
	}
	
	public int getMaxLength()
	{
		return maxLength;
	}
	
	public double getTotalCustomers()
	{
		return totalCustomers;
	}
	
	public double getAvgWaitTime()
	{
		if(getNumServed() == 0)
		{
			return 0;
		}
		else
		{
			return getTotalWait()/getNumServed();

		}
	}
	
	public double getNumItemProcess()
	{
		return numItemProcess;
	}
	
	public double getTotalFreeTime()
	{
		return totalFreeTime;
	}
	
	public int getSize()
	{
		return customerLine.size();
	}
	
	public String getName()
	{
		return queueName;
	}
	
	public void add(Customer customer) 
	{
		customerLine.add(customer);
	}
	
	public void pop()
	{
		customerLine.remove();
	}
}
