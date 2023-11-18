import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/*
 * The Supermarket runs a simulation based on the input parameters given by the user and displays
 * a time stamp and description of each event that occurs during the simulation. 
 * The Supermarket has various types of checkout lines and determines which checkout line 
 * is ideal for each new customer by the number of items the new customer has. However, if there is 
 * an empty checkout line then it is most ideal for the customer to go there instead regardless of 
 * the number of items they may have. After the simulation is finished running, statistics 
 * about the simulation are displayed for each checkout counter (i.e: average waiting time,
 * maximum length, number of customers, number of items processed per hour, and average free time for 
 * each checkout counter.
 */
public class SuperMarket 
{
	/* Queue of customers waiting to be inserted into a checkout line*/
	private Queue<Customer> queueOfCustomers;
	
	private ArrayList<CheckoutCounter> counters;
	
	private SuperExpressCounter superExpressCounter;
	
	private ExpressCounter expressCounter1;
	
	private ExpressCounter expressCounter2;
	
	/* Maximum number of items someone can have to enter a super express line*/
	private int numSuper; 
	/* Maximum number of items someone can have to enter a express line*/
	private int numExp;
	/* Number of standard checkout lines */
	private int numStandLines;
	/* Arrival rate of customers (in hours)*/
	private double arrivalRate;
	/* Maximum number of items a customer can have*/
	private int maxItems;
	/* How long the simulation will continue (in hours) (*60 *60 = seconds)*/
	private int maxSimTime;
	/* The maximum time it takes for the cashier to process an item (in seconds)*/
	private final int timeProcessItem = 5; //time it takes for 1 item to be processed.
	/* Simulated clock (in seconds)*/
	private int clock = 0;
	
	
	public static void main(String[] args)
	{
		SuperMarket market = new SuperMarket(); 
		
		market.enterData();
		market.runSimulation(); 
		market.displayStats(); 
	}
	
	public SuperMarket()
	{
		queueOfCustomers = new LinkedList<Customer>();
	}
	
	public void enterData() 
	{
		Scanner scan = new Scanner(System.in);
		
		System.out.print("Enter numStandLines: ");
		createCounters(scan.nextInt());
				
		System.out.print("Enter numSuper: ");
		numSuper = scan.nextInt();
		
		System.out.print("Enter numExp: ");
		numExp = scan.nextInt();
		
		System.out.print("Enter arrivalRate (customers/hour): ");
		arrivalRate = scan.nextDouble()/3600; 
		
		System.out.print("Enter maxItems: ");
		maxItems = scan.nextInt();
		
		System.out.print("Enter maxSimTime (in seconds): ");
		maxSimTime = scan.nextInt();
		
		System.out.println();
	}
	
	public void createCounters(int numStandLines)
	{
		this.numStandLines = numStandLines;
		
		counters = new ArrayList<CheckoutCounter>(numStandLines + 3);

		superExpressCounter = new SuperExpressCounter("Super Express Counter");
		expressCounter1 = new ExpressCounter("Express Counter 1");
		expressCounter2 = new ExpressCounter("Express Counter 2");
		
		counters.add(superExpressCounter);  //the array list at position 0 has superExpressCounter
		counters.add(expressCounter1);		//the array list at position 1 has expressCounter1
		counters.add(expressCounter2);		//the array list at position 2 has expressCounter2
		
		for(int i = 1; i < numStandLines + 1; i++) //the array list at any position over 2 has standardCounters.
		{
			counters.add(new CheckoutCounter("Standard Counter " + i));
		}
	}
	
	public void runSimulation() 
	{
		for(clock = 0; clock < maxSimTime; clock++) 
		{
			checkArrival(clock); //checks if a customer arrived using arrivalRate.
			
			if(!queueOfCustomers.isEmpty())
			{
				insertInLine(clock); //puts customer in the big line.
			}
			
			startServeDemo();
		}
	}
	
	public void startServeDemo()
	{
		for(int i = 0 ; i < counters.size(); i ++)
		{
			if(counters.get(i).getSize() > 0)
			{
				counters.get(i).updateDemo(clock);
			}
			else
			{
				counters.get(i).incrementFreeTime();
			}
		}
	}
	
	public void checkArrival(int time) 
	{	
		if(Math.random() < arrivalRate)
		{
			Customer customer = new Customer(time, maxItems);
			queueOfCustomers.add(customer);
			
			System.out.println("Time " + clock + ": New customer arrival, customer has " 
							  + customer.getNumOfItems() + " items.");
		}
	}
	
	public void insertInLine(int clock)
	{
		Customer customer = queueOfCustomers.remove();
		int items = customer.getNumOfItems();
		CheckoutCounter shortest = null;
		
		for(int i = 3; i < counters.size(); i++)
		{
			if(shortest == null)
			{
				shortest = counters.get(i);
			}
			if(shortest.getSize() > counters.get(i).getSize())
			{
				shortest = counters.get(i); //gets the shortest Standard Counter.
			}
		}
		
		if(items > numExp && shortest != null) 
		{
			shortest.add(customer);
			shortest.checkArrival(clock, customer);
			
			System.out.println("Time " + clock + ": Customer with time stamp " 
								+ customer.getArrivalTime() + " added to " 
								+ shortest.getName() + ", counter size: " 
								+ shortest.getSize());
			return;
		}
		
		if(items > numSuper && items <= numExp)
		{
			if(shortest != null && shortest.getSize() == 0)
			{
				shortest.add(customer);
				shortest.checkArrival(clock, customer);
				
				System.out.println("Time " + clock + ": Customer with time stamp " 
						+ customer.getArrivalTime() + " added to " 
						+ shortest.getName() + ", counter size: " 
						+ shortest.getSize());
				return;
			}
			else
			{
				if(expressCounter1.getSize() > expressCounter2.getSize())
				{
					expressCounter2.add(customer);
					expressCounter2.checkArrival(clock, customer);
					
					System.out.println("Time " + clock + ": Customer with time stamp " 
							+ customer.getArrivalTime() + " added to Express Counter 2, counter size: "
							+ expressCounter2.getSize());
					return;
				}
				else
				{
					expressCounter1.add(customer);
					expressCounter1.checkArrival(clock, customer);
					
					System.out.println("Time " + clock + ": Customer with time stamp " 
							+ customer.getArrivalTime() + " added to Express Counter 1, counter size: "
							+ expressCounter1.getSize());
					return;
				}
			}
		}
		
		if(items <= numSuper)
		{
			if(shortest != null && shortest.getSize() == 0)
			{
				shortest.add(customer);
				shortest.checkArrival(clock, customer);
				
				System.out.println("Time " + clock + ": Customer with time stamp " 
						+ customer.getArrivalTime() + " added to " 
						+ shortest.getName() + ", counter size: " 
						+ shortest.getSize());
				return;
			}	
			
			else if(expressCounter1.getSize() == 0)
			{
				expressCounter1.add(customer);
				expressCounter1.checkArrival(clock, customer);
				
				System.out.println("Time " + clock + ": Customer with time stamp " 
						+ customer.getArrivalTime() + " added to Express Counter 1, counter size: "
						+ expressCounter1.getSize());
				return;
			}
			else if(expressCounter2.getSize() == 0)
			{
				expressCounter2.add(customer);
				expressCounter2.checkArrival(clock, customer);
				
				System.out.println("Time " + clock + ": Customer with time stamp " 
						+ customer.getArrivalTime() + " added to Express Counter 2, counter size: "
						+ expressCounter2.getSize());
				return;
			}
			else
			{
				superExpressCounter.add(customer);
				superExpressCounter.checkArrival(clock, customer);
				
				System.out.println("Time " + clock + ": Customer with time stamp " 
						+ customer.getArrivalTime() + " added to Super Express Counter, counter size: "
						+ superExpressCounter.getSize());
				return;
			}
		}	
	}
	
	public void startServe()
	{
		for(int i = 0; i < counters.size(); i++)
		{
			if(counters.get(i).getSize() > 0)
			{
				counters.get(i).update(clock);
			}
		}
	}
	
	public void displayStats()
	{		
		double overallWaitTime = 0;
		double overallCustomers = 0;
		double overallCustomerHour = 0;
		double overallItemProcess = 0;
		double overallFreeTime = 0;
		
		System.out.println("\nSimulation Statistics:\n");
		
		/* Average Waiting Time For Each Line: */
		for(int i = 0; i < counters.size(); i++)
		{
			System.out.print("Average waiting time for " + counters.get(i).getName() + ": ");
			
			if(counters.get(i).getTotalCustomers() != 0)
			{
				System.out.println(counters.get(i).getTotalWait()/counters.get(i).getTotalCustomers());
			}
			else
			{
				System.out.println("0");
			}
		} 
		/* End */
		
		/* Overall Average Waiting Time */
		for(int i = 0; i < counters.size(); i++)
		{
			overallWaitTime += counters.get(i).getTotalWait();
			overallCustomers += counters.get(i).getTotalCustomers();
		}
		
		if(overallCustomers != 0)
		{
			System.out.println("Overall average wait time: " +  overallWaitTime/overallCustomers);
		}
		else
		{
			System.out.println("Overall average wait time: 0");
		}
		/* End */
		
		/* Maximum Length Of Each Line */
		System.out.println();
		for(int i = 0; i < counters.size(); i++)
		{
			System.out.println("Maximum length of " + counters.get(i).getName() 
					+ ": " + counters.get(i).getMaxLength());
		}
		/* End */
		
		/* Number Of Customers Per Hour For Each Line */
		
		/*
		 * 100 customers      x customers
		 * -------------  =  --------------  =>  1800x = 36,000
		 * 1800 seconds       3600 seconds
		 * 
		 * will need to multiply the totalCustomers by 3600 and then divide the result by the simulation time.
		 */
		System.out.println();
		for(int i = 0; i < counters.size(); i++)
		{
			System.out.println("Number of customers per hour for " 
					+ counters.get(i).getName()+ ": "
					+  ((counters.get(i).getTotalCustomers() * 3600)/maxSimTime));
		}
		/* End */
		
		/* Number of customers per hour overall*/
		for(int i = 0; i < counters.size(); i++)
		{
			overallCustomerHour += counters.get(i).getTotalCustomers();
		}
		
		if(overallCustomerHour != 0)
		{
			System.out.println("Overall customers her hour: " + (overallCustomerHour * 3600/maxSimTime));
		}
		else
		{
			System.out.println("Overall customers per hour: 0");
		}
		/* End */
		
		/* Number of items processed per hour for each line */
		System.out.println();
		for(int i = 0; i < counters.size(); i++)
		{
			System.out.println("Number of items processed per hour for "
					+ counters.get(i).getName() + ": "
					+ (counters.get(i).getNumItemProcess() * 3600/maxSimTime));
		}
		/* End */
		
		/* Number of items processed per hour overall */
		for(int i = 0; i < counters.size(); i++)
		{
			overallItemProcess += counters.get(i).getNumItemProcess();
		}
		
		System.out.println("Overall number of items processed per hour: " + (overallItemProcess * 3600/maxSimTime));
		/* End */
		
		/* Average free time of each counter */
		System.out.println();
		for(int i = 0; i < counters.size(); i++)
		{
			System.out.println("Average free time for " 
					+ counters.get(i).getName() + ": "
					+ (counters.get(i).getTotalFreeTime() * 3600/maxSimTime)); //average free time per hour
		}
		/* End */
		
		/* Average free time overall */
		for(int i = 0; i < counters.size(); i++)
		{
			overallFreeTime += counters.get(i).getTotalFreeTime();
		}
		System.out.println("Overall average free time: " + (overallFreeTime * 3600 /maxSimTime));
	}

}
