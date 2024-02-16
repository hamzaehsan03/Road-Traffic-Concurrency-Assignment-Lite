public class EntryPoint extends Thread {
    
    // Class to generate vehicles

    private final Road road;
    private final int carsPH;
    private final String destination;
    private final Clock clock; // Use the simulated clock for timing

    public EntryPoint(Road road, int carsPH, String destination, Clock clock)
    {
        this.road = road;
        this.carsPH = carsPH;
        this.destination = destination;
        this.clock = clock;
    }

    @Override
    public void run()
    {
        
        long interval = (3600 / carsPH) * 1000 / 10; 
        while (!Thread.currentThread().isInterrupted())
        {
            long elapsed = clock.getCurrentTime();
            if (elapsed >= 3600*1000)
            {
                break; // after an hour
            }

            try{

                if (road.hasSpace())
                {
                    Vehicle vehicle = new Vehicle(destination, elapsed);
                    System.out.println("Creating vehicle at EntryPoint with destination: " + destination + " at time: " + elapsed);
                    if (road.addVehicle(vehicle))
                    {
                        System.out.println("Vehicle added to road from EntryPoint: " + destination);
                    }

                    else
                    {
                        System.out.println("Road is full, vehicle not added from EntryPoint: " + destination);
                    }
                    //road.addVehicle(vehicle);
                    Thread.sleep(interval);
                   
                }

                // Vehicle vehicle = new Vehicle(destination, elapsed);
                // if (road.addVehicle(vehicle))
                // {
                //     Thread.sleep(interval); // synchronise
                // }
                else{
                    clock.waitTick(); // synchronise
                }
                
            } catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }
}
