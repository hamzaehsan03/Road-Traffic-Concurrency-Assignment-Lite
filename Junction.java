import java.io.*;

public class Junction extends Thread {

    private final Road entryRoad;
    private final Road exitRoad;
    private final long greenDuration;
    private final String junctionName; 
    private boolean isGreen;
    private final Clock clock;
    private int carsPassed = 0;
    private volatile boolean running = true;

    public Junction(Road entryRoad, Road exitRoad, long greenDuration, String junctionName, Clock clock)
    {
        this.entryRoad = entryRoad;
        this.exitRoad = exitRoad;
        this.greenDuration = greenDuration;
        this.junctionName = junctionName;
        this.clock = clock;
    }

    public void shutdown()
    {
        this.running = false;
    }

    private void logActivity(String message)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(junctionName + "_log.txt" , true)))
        {
            writer.write(message);
            writer.newLine();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        long greenLightStart = clock.getCurrentTime();

        while(running && !Thread.currentThread().isInterrupted())
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
                //System.out.println(junctionName + " traffic light is green.");
                processVehicles();
            }
            else
            {
                //System.out.println(junctionName + " traffic light is red.");
                String logMessage;
                if (entryRoad.getCount() > 0 && !exitRoad.hasSpace())
                {
                    logMessage = String.format("Time: %dm%ds - %s: 0 cars through, %d cars waiting. GRIDLOCK", (currentTime / 1000) / 60,
                    (currentTime / 1000) % 60, junctionName, entryRoad.getCount());
                }
                else
                {
                    logMessage = String.format("Time: %dm%ds - %s: %d cars through, %d cars waiting.", (currentTime / 1000) / 60,
                    (currentTime / 1000) % 60, junctionName, carsPassed, entryRoad.getCount());
                }

                logActivity(logMessage);
            }

            try
            {
                if (!running) break;
                clock.waitTick();
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }

        }
    }

    private void processVehicles() {
        while (isGreen && carsPassed < 12) // 12 cars can pass per minute
        { 
            Vehicle vehicle = entryRoad.consumeVehicle();
            if (vehicle != null && exitRoad.hasSpace()) {
                exitRoad.addVehicle(vehicle);
                carsPassed++;
               // System.out.println(junctionName + ": Vehicle passed from entryRoad to exitRoad");
                
                try {
                    // Sleep for the time it takes one car to move through the junction
                    clock.waitForAdditionalTime(5000); // Wait for 5 simulated seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return; // Stop processing if interrupted
                }
            } else {
                // If no vehicle or exit road is full, break the loop and log the gridlock
                if (vehicle == null) {
                    //System.out.println(junctionName + ": No vehicle to pass.");
                }
                if (!exitRoad.hasSpace()) {
                    //System.out.println(junctionName + ": Exit road is full, cannot pass vehicle.");
                }
                break;
            }
        }
        
    }
    
}
