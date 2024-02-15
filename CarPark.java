public class CarPark extends Thread {

    private final Road destinationRoad;
    private final int capacity;
    private final String parkName;
    private int totalParked = 0;
    private long parkedTime = 0;
    private final Clock clock;

    public CarPark(Road destinationRoad, int capacity, String parkName, Clock clock)
    {
        this.destinationRoad = destinationRoad;
        this.capacity = capacity;
        this.parkName = parkName;
        this.clock = clock;
    }

    @Override
    public void run()
    {
        while(!Thread.currentThread().isInterrupted())
        {
            if (totalParked < capacity)
            {
                Vehicle vehicle = destinationRoad.consumeVehicle();
                if (vehicle != null)
                {
                    System.out.println(parkName + ": Car attempting to park at " + clock.getCurrentTime() + " seconds.");
                    long parkTimeElapsed = clock.getCurrentTime();
                    vehicle.setParkTime(parkTimeElapsed);
                    parkedTime += parkTimeElapsed - vehicle.getEntryTime();
                    totalParked++;

                    System.out.println(parkName + ": Car parked at " + clock.getCurrentTime() + " seconds. Total cars parked: " + totalParked);
                    
                    try
                    {
                        clock.waitForAdditionalTime(1200); // 12s
                    } catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                    }
                }
                if (totalParked >= capacity)
                {
                    System.out.println(parkName + " is full.");
                }

                else 
                {
                    try
                    {
                        clock.waitTick();
                    }
                    catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            else
            {
                break;
            }
         
        }
        
        long avgParkTime = totalParked > 0 ? parkedTime / totalParked : 0;
        System.out.println(parkName + ": Simulation end. Average parking time: " + avgParkTime + " seconds");
    }
}
