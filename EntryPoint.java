public class EntryPoint extends Thread {
    
    private final Road road;
    private final int carsPH;
    private final String destination;
    private final Clock clock; // Use the simulated clock for timing
    private static int totalCreated = 0;
    private volatile boolean running = true;

    public EntryPoint(Road road, int carsPH, String destination, Clock clock)
    {
        this.road = road;
        this.carsPH = carsPH;
        this.destination = destination;
        this.clock = clock;
    }

    public void shutdown()
    {
        this.running = false;
    }

    public static synchronized void incrementTotalCreated()
    {
        totalCreated++;
    }

    public static synchronized int getTotalCreated()
    {
        return totalCreated;
    }

    @Override
    public void run()
    {
        
        long interval = (3600 / carsPH) * 1000 / 10; 
        while (running && !Thread.currentThread().isInterrupted())
        {
            long elapsed = clock.getCurrentTime();
            if (elapsed >= 3600*1000)
            {
                break; // after an hour
            }

            try{

                if (road.hasSpace() && road.getCount() <= 60)
                {
                    Vehicle vehicle = new Vehicle(destination, elapsed);
                    //System.out.println("Creating vehicle at EntryPoint with destination: " + destination + " at time: " + elapsed);
                    if (road.addVehicle(vehicle))
                    {
                        //System.out.println("Vehicle added to road from EntryPoint: " + destination);
                        EntryPoint.incrementTotalCreated();
                    }

                    else
                    {
                        //System.out.println("Road is full, vehicle not added from EntryPoint: " + destination);
                    }
                    //road.addVehicle(vehicle);
                    if (!running) break;
                    Thread.sleep(interval);
                   
                }

                // Vehicle vehicle = new Vehicle(destination, elapsed);
                // if (road.addVehicle(vehicle))
                // {
                //     Thread.sleep(interval); // synchronise
                // }
                else
                {
                    if (!running) break;
                    clock.waitTick(); // synchronise
                }
                
            } catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }
}
