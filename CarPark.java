public class CarPark extends Thread {

    private final Road destinationRoad;
    private final int capacity;
    private final String parkName;
    private int totalParked = 0;
    private long parkedTime = 0;
    private final Clock clock;
    private volatile boolean running = true;

    public CarPark(Road destinationRoad, int capacity, String parkName, Clock clock)
    {
        this.destinationRoad = destinationRoad;
        this.capacity = capacity;
        this.parkName = parkName;
        this.clock = clock;
    }

    public void shutdown()
    {
        this.running = false;
    }

    @Override
    public void run()
    {
        while(running && !Thread.currentThread().isInterrupted())
        {
            if (totalParked < capacity)
            {
                Vehicle vehicle = destinationRoad.consumeVehicle();
                if (vehicle != null)
                {
                    //System.out.println(parkName + ": Car attempting to park at " + clock.getCurrentTime()/1000 + " seconds.");
                    long parkTimeElapsed = clock.getCurrentTime();
                    vehicle.setParkTime(parkTimeElapsed);
                    parkedTime += parkTimeElapsed - vehicle.getEntryTime();
                    totalParked++;
                    

                    //System.out.println(parkName + ": Car parked at " + clock.getCurrentTime()/1000 + " seconds. Total cars parked: " + totalParked);
                    
                    try
                    {
                        if (!running) break;
                        clock.waitForAdditionalTime(12000); // 12s
                    } catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                    }
                }
                if (totalParked >= capacity)
                {
                    //System.out.println(parkName + " is full.");
                }

                else 
                {
                    try
                    {
                        if (!running) break;
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
        
        //long avgParkTime = totalParked > 0 ? parkedTime / totalParked : 0;
        //System.out.println(parkName + ": " + totalParked + " Cars parked. Average parking time: " + (avgParkTime/1000)/60 + "m " + (avgParkTime/1000)%60 + "s");

        // FinalLog = String.format("%d: %d Cars Parked. Average parking time: %dm%ds", (parkName, totalParked, (avgParkTime/1000)/60, (avgParkTime/1000) % 60));
        // System.out.println(FinalLog);
        // logMessage = String.format("Time: %dm%ds - %s: 0 cars through, %d cars waiting. GRIDLOCK", (currentTime / 1000) / 60,
        //             (currentTime / 1000) % 60, junctionName, entryRoad.getCount());
    }

    public int getCapacity()
    {
        return capacity;
    }

    public int getTotalParked()
    {
        return totalParked;
    }

    public int getAvailableSpaces()
    {
        return capacity - totalParked;
    }
}
