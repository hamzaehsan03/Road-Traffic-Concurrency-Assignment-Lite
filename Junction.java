public class Junction extends Thread {

    private final Road entryRoad;
    private final Road exitRoad;
    private final long greenDuration;
    private final String junctionName; 
    private boolean isGreen;
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
        long greenLightStart = clock.getCurrentTime();

        while(!Thread.currentThread().isInterrupted())
        {
            long currentTime = clock.getCurrentTime();
            //long elapsedTime = currentTime - greenLightStart;
            if ((currentTime - greenLightStart) >= greenDuration * 1000)
            {
                // Change the light
                isGreen = !isGreen;

                if (isGreen)
                {
                    greenLightStart = currentTime;
                    carsPassed = 0;
                }
                //greenLightStart = currentTime;

                // Reset the amount of cars that passed the light per cycle
                //carsPassed = 0;
            }

            if (isGreen)
            {
                System.out.println(junctionName + " traffic light is green.");
                processVehicles();
            }
            else
            {
                System.out.println(junctionName + " traffic light is red.");
            }

            try
            {
                clock.waitTick();
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
            

        //     if (currentTime < greenLightStart + greenDuration)
        //     {
        //         // Light's Green
        //         Vehicle vehicle = entryRoad.consumeVehicle();
        //         if (vehicle != null && exitRoad.addVehicle(vehicle))
        //         {
        //             // 60/12 = 5 seconds perVehicle
        //             try
        //             {
        //                 clock.waitForAdditionalTime(5000);
        //             }
        //             catch (InterruptedException e)
        //             {
        //                 Thread.currentThread().interrupt();
        //             }

        //         }
        //         else
        //         {
        //             // If the exit road is full or there's no cars, wait for the next tick
        //             try
        //             {
        //                 clock.waitTick();
        //             }
        //             catch(InterruptedException e)
        //             {
        //                 Thread.currentThread().interrupt();
        //             }
        //         }
        //     }
        // }
        // System.out.println(junctionName + ": Simulation end. Total cars passed: " + carsPassed);
        }
    }

    private void processVehicles()
    {

        if (exitRoad.hasSpace())
        {
            Vehicle vehicle = entryRoad.consumeVehicle();
            if (vehicle != null) {
                carsPassed++;
    
                // Simulate the vehicle moving through the junction for 5 seconds
                try {
                    clock.waitForAdditionalTime(5000 / 360); // Adjusted for the simulation speed
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        
    }
    
}
