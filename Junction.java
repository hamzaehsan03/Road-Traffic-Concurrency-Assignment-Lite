public class Junction extends Thread {

    private final Road entryRoad;
    private final Road exitRoad;
    private final long greenDuration;
    private final String junctionName; 
    private final Clock clock;
    private int carsPassed = 0;

    public Junction(Road entryRoad, Road exitRoad, long greenDuration, String junctionName, Clock clock)
    {
        this.entryRoad = entryRoad;
        this.exitRoad = exitRoad;
        this.greenDuration = greenDuration;
        this.junctionName = junctionName;
        this.clock = clock;
    }

    @Override
    public void run()
    {
        long greenLightStart = 0;

        while(!Thread.currentThread().isInterrupted())
        {
            long currentTime = clock.getCurrentTime();
            if (currentTime >= greenLightStart + greenDuration)
            {
                // Change the light
                greenLightStart = currentTime;

                // Reset the amount of cars that passed the light per cycle
                carsPassed = 0;
            }

            if (currentTime < greenLightStart + greenDuration)
            {
                // Light's Green
                Vehicle vehicle = entryRoad.consumeVehicle();
                if (vehicle != null && exitRoad.addVehicle(vehicle))
                {
                    // 60/12 = 5 seconds perVehicle
                    try
                    {
                        clock.waitForAdditionalTime(5000);
                    }
                    catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                    }

                }
                else
                {
                    // If the exit road is full or there's no cars, wait for the next tick
                    try
                    {
                        clock.waitTick();
                    }
                    catch(InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        System.out.println(junctionName + ": Simulation end. Total cars passed: " + carsPassed);
    }
    
}
